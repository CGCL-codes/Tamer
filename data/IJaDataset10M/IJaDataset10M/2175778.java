package file.met;

import java.io.*;
import java.util.*;
import log.Log;

public class MetFileReader implements eDonkeyConstants {

    private static final Log log = new Log(new MetFileReader());

    public static MetFile readFromED2KLink(String s) {
        MetFile MetFile = new MetFile();
        try {
            String as[] = s.split("\\|");
            MetFile.setFileName(as[2]);
            try {
                MetFile.setFileSize(Long.parseLong(as[3]));
            } catch (NumberFormatException numberformatexception) {
                log.warning("Invalid size in link: " + as[3]);
            }
            byte abyte0[] = new byte[16];
            for (int i = 0; i < 16; i++) abyte0[i] = Byte.parseByte(as[4].substring(i * 2, i * 2 + 2), 16);
            MetFile.setMasterHash(abyte0);
        } catch (ArrayIndexOutOfBoundsException arrayindexoutofboundsexception) {
            log.warning("Malformed ed2k link");
        } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
            log.warning("Invalid hash in link");
        }
        return MetFile;
    }

    public static MetFile[] readFromKnownMet(File file) throws IOException {
        BufferedInputStream bufferedinputstream = new BufferedInputStream(new FileInputStream(file));
        MetFile aMetFile[] = new MetFile[0];
        long l = bufferedinputstream.read();
        if (l != 14L && l != 15L) throw new IOException("Not a valid known.met file.");
        l = read(bufferedinputstream, 4);
        if (l > 0L) {
            aMetFile = new MetFile[(int) l];
            aMetFile[0] = new MetFile(file);
            readFileObjectUnsure(bufferedinputstream, aMetFile[0]);
            int i = 1;
            do {
                if ((long) i >= l) break;
                aMetFile[i] = new MetFile(file);
                aMetFile[i].setNewMets(aMetFile[i - 1].isNewMets());
                try {
                    readFileObjectSure(bufferedinputstream, aMetFile[i]);
                } catch (NullPointerException nullpointerexception) {
                    if (nullpointerexception.getMessage().equals("old format")) {
                        MetFile aMetFile1[] = new MetFile[i];
                        for (int j = 0; j < i; j++) aMetFile1[j] = aMetFile[j];
                        aMetFile = aMetFile1;
                    } else {
                        throw nullpointerexception;
                    }
                    break;
                }
                i++;
            } while (true);
        }
        bufferedinputstream.close();
        return aMetFile;
    }

    public static MetFile readFromPartMet(File file) throws IOException {
        if (file.length() < 20L) throw new IOException("corruptFile");
        BufferedInputStream bufferedinputstream = new BufferedInputStream(new FileInputStream(file));
        MetFile MetFile = new MetFile(file);
        int i = bufferedinputstream.read();
        if (i == 224) {
            log.finest("Reading old part file");
            MetFile.setNewParts(false);
        } else if (i == 225) {
            log.finest("Reading new part file");
            MetFile.setNewParts(true);
        } else if (i == 226) {
            log.finest("Reading large part file");
            MetFile.setLarge(true);
            MetFile.setNewParts(false);
        } else {
            return MetFile;
        }
        readFileObjectUnsure(bufferedinputstream, MetFile);
        bufferedinputstream.close();
        return MetFile;
    }

    public static void readFromNetworkStream(InputStream inputstream, MetFile MetFile) throws IOException {
        BufferedInputStream bufferedinputstream = new BufferedInputStream(inputstream);
        if (bufferedinputstream.read() != 227) throw new IOException("incorrectStartByte");
        read(bufferedinputstream, 4);
        int i = bufferedinputstream.read();
        switch(i) {
            case 104:
                for (int k = 0; k < 16; k++) {
                    int j = bufferedinputstream.read();
                    if ((byte) j != MetFile.getMasterHash()[k]) {
                        log.warning("Returned Master Hash is not the same");
                        throw new IOException("incorrectMasterHash");
                    }
                }
                readNewHashes(bufferedinputstream, MetFile);
                break;
            default:
                throw new IOException("unknownMessageFormat");
        }
    }

    public static void readFileObjectUnsure(BufferedInputStream bufferedinputstream, MetFile MetFile) throws IOException {
        bufferedinputstream.mark(10);
        try {
            if (bufferedinputstream.read() != 2) throw new NullPointerException("old format");
            bufferedinputstream.reset();
            bufferedinputstream.mark(51200);
            MetFile.setNewMets(true);
            readFileObjectSure(bufferedinputstream, MetFile);
        } catch (NullPointerException nullpointerexception) {
            if (!nullpointerexception.getMessage().equals("old format")) throw nullpointerexception;
            bufferedinputstream.reset();
            MetFile.setNewMets(false);
            readFileObjectSure(bufferedinputstream, MetFile);
        }
    }

    public static void readFileObjectSure(BufferedInputStream bufferedinputstream, MetFile MetFile) throws IOException {
        if (MetFile.isNewMets()) bufferedinputstream.read();
        skip(bufferedinputstream, 4L);
        byte abyte0[] = new byte[16];
        bufferedinputstream.read(abyte0);
        MetFile.setMasterHash(abyte0);
        if (!MetFile.isNewMets()) {
            int i = (int) read(bufferedinputstream, 2);
            if (i < 0) return;
            byte abyte1[][] = new byte[i][16];
            for (int k = 0; k < i; k++) bufferedinputstream.read(abyte1[k]);
            MetFile.setChunkHashes(abyte1);
        }
        int j = (int) read(bufferedinputstream, 4);
        ArrayList<Long> arraylist = new ArrayList<Long>(20);
        boolean flag = false;
        for (int l = 0; l < j; l++) {
            int j1 = bufferedinputstream.read();
            boolean flag1 = false;
            log.finest("Reading tag type: " + j1);
            switch(j1) {
                case 1:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                default:
                    l = j;
                    continue;
                case 0:
                    if (MetFile.isNewMets()) {
                        MetFile.setNewMets(false);
                        throw new NullPointerException("old format");
                    }
                case 2:
                    try {
                        readString(bufferedinputstream, MetFile);
                    } catch (EOFException eofexception) {
                        return;
                    }
                    continue;
                case 11:
                    flag1 = true;
                    break;
                case 3:
                case 4:
                    break;
            }
            try {
                if (readInt(bufferedinputstream, MetFile, arraylist, flag1)) flag = true;
            } catch (EOFException eofexception1) {
                return;
            }
        }
        Object aobj[] = arraylist.toArray();
        if (!flag) Arrays.sort(aobj);
        long al[][] = new long[(aobj.length + 1) / 2][2];
        for (int i1 = 0; i1 < aobj.length; i1 += 2) {
            al[i1 / 2][0] = ((Long) aobj[i1]).longValue();
            if (i1 + 1 != aobj.length) al[i1 / 2][1] = ((Long) aobj[i1 + 1]).longValue(); else al[i1 / 2][1] = MetFile.getFileSize();
        }
        MetFile.setFileGaps(al);
        if (MetFile.isNewMets()) readNewHashes(bufferedinputstream, MetFile);
    }

    private static void readString(BufferedInputStream bufferedinputstream, MetFile MetFile) throws IOException {
        long l = read(bufferedinputstream, 2);
        if (l == 1L) {
            int i = bufferedinputstream.read();
            l = read(bufferedinputstream, 2);
            if (l < 0L) throw new EOFException();
            byte abyte0[] = new byte[(int) l];
            bufferedinputstream.read(abyte0);
            String s;
            if (l > 3L && abyte0[0] == -17 && abyte0[1] == -69 && abyte0[2] == -65) s = new String(abyte0, 3, abyte0.length - 3, "UTF-8"); else s = new String(abyte0);
            switch(i) {
                case 1:
                    if (MetFile.getFileName().equals("")) MetFile.setFileName(s);
                    break;
                case 3:
                    MetFile.setFileType(s);
                    break;
                case 4:
                    MetFile.setFileExtension(s);
                    break;
                case 11:
                    MetFile.setDescription(s);
                    break;
                case 18:
                    MetFile.setPartFileName(s);
                    break;
            }
        } else if (l == 9L) {
            byte abyte1[] = new byte[9];
            bufferedinputstream.read(abyte1);
            String s1 = new String(abyte1);
            l = read(bufferedinputstream, 2);
            if (s1.equals("localpart")) {
                byte abyte2[] = new byte[(int) l];
                bufferedinputstream.read(abyte2);
                MetFile.setLocalPart(new String(abyte2));
            } else {
                if (l < 0L) throw new EOFException();
                skip(bufferedinputstream, l);
            }
        } else {
            if (l < 0L) throw new EOFException();
            skip(bufferedinputstream, l);
            l = read(bufferedinputstream, 2);
            if (l < 0L) throw new EOFException();
            skip(bufferedinputstream, l);
        }
    }

    private static boolean readInt(BufferedInputStream bufferedinputstream, MetFile MetFile, ArrayList<Long> arraylist, boolean flag) throws IOException {
        long l = read(bufferedinputstream, 2);
        if (l == 1L) {
            int i = bufferedinputstream.read();
            l = read(bufferedinputstream, flag ? 8 : 4);
            switch(i) {
                case 2:
                    MetFile.setFileSize(l);
                    break;
                case 8:
                    MetFile.setAmountTransferred(l);
                    break;
                case 17:
                    MetFile.setVersion(l);
                    break;
                case 19:
                    MetFile.setUploadPriority((byte) ((byte) (int) l / 2 + 2));
                    break;
                case 25:
                    MetFile.setUploadPriority((byte) (int) l);
                    break;
                case 20:
                    MetFile.setPauseState((byte) (int) l);
                    break;
                case 24:
                    if (MetFile.isEMule()) MetFile.setDownloadPriority((byte) (int) l); else MetFile.setDownloadPriority((byte) (int) (l + 1L));
                    break;
                case 42:
                    return true;
            }
        } else {
            if (l < 0L) throw new EOFException();
            int j = bufferedinputstream.read();
            if (j == 9 || j == 10) {
                skip(bufferedinputstream, l - 1L);
                arraylist.add(new Long(read(bufferedinputstream, flag ? 8 : 4)));
            } else {
                skip(bufferedinputstream, (l - 1L) + (long) (flag ? 8 : 4));
            }
        }
        return false;
    }

    private static long read(InputStream inputstream, int i) throws IOException {
        long l = 0L;
        for (int j = 0; j < i; j++) l |= (long) inputstream.read() << (j << 3);
        return l;
    }

    private static void skip(InputStream inputstream, long l) throws IOException {
        long l1 = inputstream.skip(l);
        if (l1 != l) {
            log.finer("Skip failed, reading bytes: " + l1);
            inputstream.read(new byte[(int) l - (int) l1]);
        }
    }

    private static void readNewHashes(BufferedInputStream bufferedinputstream, MetFile MetFile) throws IOException {
        long l = MetFile.getFileSize();
        if (l > 0x947000L) {
            bufferedinputstream.mark(10);
            int i = bufferedinputstream.read();
            if (i == 1) {
                i = (int) Math.ceil((double) l / 9728000D);
                byte abyte0[][] = new byte[i][16];
                for (int k = 0; k < i; k++) {
                    bufferedinputstream.read(abyte0[k]);
                }
                MetFile.setChunkHashes(abyte0);
                log.finest("Processed chunk hashes");
            } else if (i != 0) {
                bufferedinputstream.reset();
                return;
            }
        }
        if (l > 0x76c00L) {
            bufferedinputstream.mark(10);
            int j = bufferedinputstream.read();
            if (j == 1) {
                j = (int) Math.ceil((double) l / 486400D);
                int ai[][][] = new int[(j + 19) / 20][][];
                for (int i1 = 0; i1 < ai.length; i1++) {
                    if (j < 20) {
                        ai[i1] = new int[j][2];
                    } else {
                        ai[i1] = new int[20][2];
                        j -= 20;
                    }
                    for (int j1 = 0; j1 < ai[i1].length; j1++) {
                        ai[i1][j1][0] = (int) read(bufferedinputstream, 4);
                        ai[i1][j1][1] = (int) read(bufferedinputstream, 4);
                    }
                }
                MetFile.setCrumbHashes(ai);
                log.finest("Processed crumb hashes");
            } else if (j != 0) bufferedinputstream.reset();
        }
    }
}
