package gg.de.sbmp3.backend.impl;

import gg.de.sbmp3.backend.BEFactory;
import gg.de.sbmp3.backend.data.FileBean;
import gg.de.sbmp3.backend.data.PlayerStateBean;
import gg.de.sbmp3.backend.data.PlaylistBean;
import gg.de.sbmp3.backend.data.UserBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClientPlayerBEImpl extends AbstractPlayerBE {

    private static Log log = LogFactory.getLog(ClientPlayerBEImpl.class);

    private String m3uContent = "";

    String[][] directories;

    public ClientPlayerBEImpl(UserBean user, String playerSystemId) {
        super(user, playerSystemId);
        boolean smb = playerSystemId.equals("http-smb");
        String dir = BEFactory.getStaticBE().getConfig("base", "mp3dirs");
        if (dir != null) {
            String[] dirs = dir.split("\n");
            directories = new String[dirs.length][2];
            for (int d = 0; d < dirs.length; d++) {
                String[] parts = dirs[d].split(";");
                if (parts.length == 3) {
                    directories[d][0] = parts[0];
                    if (smb) directories[d][1] = parts[1]; else directories[d][1] = parts[2];
                }
            }
        }
    }

    public void play(FileBean file) {
        m3uContent = convertFileName(file.getPath());
        m3uContent += "\n";
    }

    public void play(PlaylistBean playlist) {
    }

    public void play(FileBean[] files) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < files.length; i++) {
            buf.append(convertFileName(files[i].getPath()));
            buf.append("\n");
        }
        m3uContent = buf.toString();
    }

    private String convertFileName(String file) {
        String newFile = "";
        for (int i = 0; i < directories.length; i++) {
            String key = directories[i][0];
            if (file.startsWith(key)) {
                newFile = directories[i][1] + file.substring(key.length());
                break;
            }
        }
        assert !newFile.equals("");
        if (playerSystemId.equals("http-smb")) newFile = newFile.replace('/', '\\'); else if (playerSystemId.equals("http")) {
            newFile = newFile.replaceAll(" ", "%20");
        }
        return newFile;
    }

    public void rewind(int seconds) {
        log.warn("NOT IMPLEMENTED FOR CLIENT PLAYER - rewind()");
    }

    public void forward(int seconds) {
        log.warn("NOT IMPLEMENTED FOR CLIENT PLAYER - forward()");
    }

    public void pause() {
        log.warn("NOT IMPLEMENTED FOR CLIENT PLAYER - pause()");
    }

    public void next() {
        log.warn("NOT IMPLEMENTED FOR CLIENT PLAYER - next()");
    }

    public void prev() {
        log.warn("NOT IMPLEMENTED FOR CLIENT PLAYER - prev()");
    }

    public void volUp() {
        log.warn("NOT IMPLEMENTED FOR CLIENT PLAYER - volUp()");
    }

    public void volDown() {
        log.warn("NOT IMPLEMENTED FOR CLIENT PLAYER - volDown()");
    }

    public PlayerStateBean getPlayerState() {
        log.warn("NOT IMPLEMENTED FOR CLIENT PLAYER - getPlayerState()");
        return null;
    }

    public String getM3uContent() {
        return m3uContent;
    }

    public void resetM3uContent() {
        m3uContent = "";
    }
}
