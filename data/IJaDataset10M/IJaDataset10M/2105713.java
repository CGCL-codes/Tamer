package com.jpark.jamse.server;

import static java.lang.String.format;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.jpark.jamse.player.api.DetailInfo;
import com.jpark.jamse.player.api.IPlayer;
import com.jpark.jamse.player.api.IPlaylist;
import com.jpark.jamse.player.api.PlaylistItem;
import com.jpark.jamse.player.api.SongInfo;

public class ProtocolHandler {

    public static final byte[] VERSION = new byte[] { 5, 0 };

    private static Log log = LogFactory.getLog(ProtocolHandler.class);

    public static final String[] protocols = { "http:", "file:", "ftp:", "https:", "ftps:", "jar:" };

    private enum Dir {

        NULL(0), ROOT(0), CHILD_OF_PREV(1), ONLY_CHILD_OF_PREV(2), SIBLING_OF_PREV(3), LAST_SIBLING_OF_PREV(4), TERMINATOR(0xFF);

        private int value;

        Dir(int value) {
            this.value = (byte) value;
        }

        public byte value() {
            return (byte) value;
        }

        public byte struct(DirType type) {
            int x = (value << 4 & 0xF0) | (0x0F & type.value());
            return (byte) x;
        }
    }

    private enum DirType {

        DRIVE(0), DIRECTORY(1), FILE(2), UNEXPANDED_DIR(3);

        private byte value;

        DirType(int value) {
            this.value = (byte) value;
        }

        public byte value() {
            return value;
        }
    }

    private IPlayer player;

    private IConf config;

    private byte[] buffer = new byte[512];

    private ServerUtil util;

    public ProtocolHandler(IPlayer player, IConf config) {
        util = ServerUtil.getInstance();
        if (player == null) {
            log.error("Player is null");
        }
        if (config == null) {
            log.error("Config is null");
        }
        this.player = player;
        this.config = config;
    }

    public IPlayer getPlayer() {
        return player;
    }

    public void setPlayer(IPlayer player) {
        this.player = player;
    }

    public void handleCommand(Protocol cmd, DataInputStream in, DataOutputStream out) throws IOException {
        switch(cmd) {
            case CHCK:
                out.write("Y".getBytes());
                log.debug("servercheck response send");
                break;
            case GVOL:
                writeVolume(out);
                break;
            case VOLM:
                player.setVolume(in.readUnsignedByte());
                break;
            case INFO:
                writeSongInfo(out);
                break;
            case INF2:
                writeSongInfo2(out);
                break;
            case STRT:
                player.performButtonAction(IPlayer.Buttons.PLAY);
                break;
            case STOP:
                player.performButtonAction(IPlayer.Buttons.STOP);
                break;
            case PAUS:
                player.performButtonAction(IPlayer.Buttons.PAUSE);
                break;
            case NEXT:
                player.performButtonAction(IPlayer.Buttons.NEXT);
                break;
            case PREV:
                player.performButtonAction(IPlayer.Buttons.PREVIOUS);
                break;
            case SHFL:
                player.setShuffle(in.readByte() > 0);
                break;
            case REPT:
                player.setRepeat(in.readByte() > 0);
                break;
            case VERS:
                out.write(VERSION);
                break;
            case PLST:
                writePlaylist(out);
                break;
            case SLCT:
                int idx = in.readShort();
                player.setPlaylistIndex(idx);
                break;
            case LIST:
                writeDirectories(getFileList(), out, true);
                break;
            case LADD:
                addToPlayList(in);
                break;
            case RMAL:
                player.clearPlaylist();
                break;
            case DINF:
                writeDetailInfo(out);
                break;
            case FADE:
                player.fadeOut();
                break;
            case DLST:
                writeDirList(in, out);
                break;
            case FFWD:
                player.forward();
                break;
            case RWND:
                player.rewind();
                break;
            case PLEN:
                writePlaylistLength(out);
                break;
            case PLAY:
                setAndPlay(in, out);
                break;
            case FINF:
                writeActualSongFileLength(out);
                break;
            case SHUT:
                if (config.isAllowShutDown()) {
                    player.close();
                    throw new CloseException();
                }
                break;
            case STEN:
                player.stopAtEndOfTrack();
                break;
            case SEEK:
                seek(in);
                break;
            case DOWN:
                writeFile(in, out);
            default:
                log.error(format("Command %s is not supported", cmd.name()));
        }
    }

    private void writeFile(DataInputStream in, DataOutputStream out) throws IOException {
        String file = getFileStructure(in);
        if (ServerUtil.isLinuxOs()) {
            file = getLinuxPath(file);
        }
        File f = new File(file);
        if (util.isM3UFile(file)) {
            util.cacheM3UFileContents(f);
        }
        InputStream fIn = new FileInputStream(f);
        int len;
        out.write("DOWNACK".getBytes());
        while ((len = fIn.read(buffer)) > -1) {
            out.write(buffer, 0, len);
        }
        saveClose(fIn);
    }

    private String getFileStructure(DataInputStream in) throws IOException {
        int len = in.readShort();
        byte[] buffer = new byte[len];
        in.read(buffer);
        return new String(buffer);
    }

    private void writeVolume(DataOutputStream out) throws IOException {
        log.debug("out volume:" + player.getVolume());
        byte volume = (byte) player.getVolume();
        out.write("GVOLACK".getBytes());
        out.writeByte(volume);
    }

    private void seek(DataInputStream in) throws IOException {
        int seconds = in.readInt();
        player.seek(seconds);
    }

    private void setAndPlay(DataInputStream in, DataOutputStream out) throws IOException {
        player.clearPlaylist();
        addToPlayList(in);
        int size = player.getPlaylist().getPlaylistItems().size();
        player.setPlaylistIndex(size - 1);
    }

    private void writePlaylistLength(DataOutputStream out) throws IOException {
        short len = (short) player.getPlaylist().getPlaylistItems().size();
        out.writeShort(len);
    }

    private void writeDirList(DataInputStream in, DataOutputStream out) throws IOException {
        String file = readString(in);
        List<File> files = new ArrayList<File>();
        files.add(new File(file));
        writeDirectories(files, out, false);
    }

    private List<File> getFileList() {
        Iterator<String> dirs = config.getDirList().iterator();
        List<File> files = new ArrayList<File>();
        while (dirs.hasNext()) {
            File dir = new File(dirs.next());
            files.add(dir);
        }
        if (config.isShowRadioEnabled()) {
            files.add(ServerUtil.getInstance().getRadioDir());
        }
        return files;
    }

    private void writeDetailInfo(DataOutputStream out) throws IOException {
        out.write("DINFACK".getBytes());
        DetailInfo info = player.getDetailInfo();
        out.writeInt(info.getBitrate());
        out.writeInt(info.getSamplRate());
        out.writeInt(info.getChannel());
    }

    private void addToPlayList(DataInputStream in) throws IOException {
        String path = readString(in);
        if (ServerUtil.isLinuxOs()) {
            path = getLinuxPath(path);
        }
        if (isProtocol(path)) {
            String title = ServerUtil.getInstance().getTitleFromCache(path);
            if (title == null) {
                player.addToPlaylist(path, path);
            } else {
                player.addToPlaylist(title, path);
            }
        } else {
            player.addToPlaylist(path);
        }
    }

    private void writeDirectories(List<File> files, DataOutputStream out, boolean expanded) throws IOException {
        out.write("LISTACK".getBytes());
        out.writeByte(0x01);
        out.writeByte(Dir.NULL.value());
        writeDirStructure(files, out, true, true);
        out.writeByte(Dir.TERMINATOR.value());
    }

    private void writeDirStructure(List<File> files, DataOutputStream out, boolean expanded, boolean toplevel) throws IOException {
        for (int i = 0; i < files.size(); i++) {
            File f = files.get(i);
            byte struct = getDirStructureInfo(i, files, expanded).struct(getDirType(f, expanded));
            out.writeByte(struct);
            String path = null;
            if (toplevel) {
                path = f.getAbsolutePath();
            } else {
                path = f.getName();
            }
            out.write(path.getBytes());
            out.writeByte(Dir.NULL.value());
            log.debug(String.format("(0x%s, %s,(0x%s)", Integer.toHexString(struct), path, Dir.NULL.value()));
            if (expanded && f.isDirectory()) {
                List<File> fList = Arrays.asList(f.listFiles());
                if (fList.size() > 0) {
                    writeDirStructure(fList, out, expanded, false);
                }
            }
        }
    }

    private Dir getDirStructureInfo(int idx, List<File> files, boolean expanded) {
        if (files.size() == 1) {
            return Dir.ONLY_CHILD_OF_PREV;
        } else if (idx == 0) {
            return Dir.CHILD_OF_PREV;
        } else if (idx < (files.size() - 1)) {
            return Dir.SIBLING_OF_PREV;
        } else {
            return Dir.LAST_SIBLING_OF_PREV;
        }
    }

    private DirType getDirType(File file, boolean expanded) {
        if (file.isDirectory()) {
            if (expanded) {
                return DirType.DIRECTORY;
            } else {
                return DirType.UNEXPANDED_DIR;
            }
        } else {
            return DirType.FILE;
        }
    }

    private void writeActualSongFileLength(DataOutputStream out) throws IOException {
        int idx = player.getPlaylist().getIndex();
        PlaylistItem item = player.getPlaylist().getPlaylistItems().get(idx);
        File f = new File(item.getSongPath());
        int len = (int) f.length();
        out.write("FINFACK".getBytes());
        out.writeInt(len);
    }

    private void writeSongInfo(DataOutputStream out) throws IOException {
        int playing = 0;
        int pause = 0;
        SongInfo info = player.getSongInfo();
        String songPlaying = info.getSongTitle();
        pause = toByte(info.isPaused());
        playing = toByte(info.isPlaying());
        out.write("INFOACK".getBytes());
        out.writeByte((pause << 1 & 0x02) | (playing & 0x01));
        out.writeInt(info.getTotalTime());
        out.writeInt(info.getCurrentTime());
        out.writeByte(toByte(player.getShuffle()));
        out.writeByte(toByte(player.getRepeat()));
        out.write(songPlaying.getBytes());
    }

    private void writeSongInfo2(DataOutputStream out) throws IOException {
        writeSongInfo(out);
        out.writeByte(0x00);
    }

    private byte toByte(boolean value) {
        return (value) ? (byte) 1 : (byte) 0;
    }

    private void writePlaylist(DataOutputStream out) throws IOException {
        IPlaylist list = player.getPlaylist();
        out.write("PLSTACK".getBytes());
        int idx = list.getIndex();
        if (list.getPlaylistItems().size() > 0 && idx < 0) {
            idx = 0;
        }
        out.writeShort(idx);
        Iterator<PlaylistItem> iterator = list.getPlaylistItems().iterator();
        while (iterator.hasNext()) {
            out.write(iterator.next().getSongTitle().getBytes());
            out.write("\n".getBytes());
        }
        out.write(0x00);
    }

    private String readString(DataInputStream in) throws IOException {
        int len = in.readShort();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (len > 0) {
            int read = (len > buffer.length) ? buffer.length : len;
            in.read(buffer, 0, read);
            out.write(buffer, 0, read);
            len -= read;
        }
        return out.toString();
    }

    private void saveClose(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }

    private boolean isProtocol(String location) {
        for (String prot : protocols) {
            if (location != null && location.startsWith(prot)) {
                return true;
            }
        }
        return false;
    }

    private String getLinuxPath(String path) {
        path = path.replaceAll("\\\\/", "/");
        path = path.replaceAll("\\\\", "/");
        return path;
    }
}
