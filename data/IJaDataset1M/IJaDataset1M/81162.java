package vavi.util.iappli;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Pak.
 * <pre>
 * offset
 * [BOF]
 * 00		version. 1
 * 01		number of messages
 * 02 ~		messages
 * xx		number of images
 * xx xx	index of image 1
 * xx xx	index of image 2
 *
 * xx xx	index of image n
 * xx		number of sounds
 * xx xx	index of sound 1
 * xx xx	index of sound 2
 *
 * xx xx	index of sound n
 * yy ~		image 1
 * yy ~		image n
 * yy ~		sound 1
 * yy ~		sound n
 * [EOF]
 *
 * message
 *
 * 00		length of message
 * 01 ~		message
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040125 nsano initial version <br>
 */
public class Pak {

    /** */
    private int version = 1;

    /** */
    String imageListFilename;

    /** */
    public void setImageListFilename(String imageListFilename) {
        this.imageListFilename = imageListFilename;
    }

    /** */
    String soundListFilename;

    /** */
    public void setSoundListFilename(String soundListFilename) {
        this.soundListFilename = soundListFilename;
    }

    /** */
    String messagesFilename;

    /** */
    public void setMessagesFilename(String messagesFilename) {
        this.messagesFilename = messagesFilename;
    }

    /** */
    String output;

    /** */
    public void setOutput(String output) {
        this.output = output;
    }

    /** */
    private static final String FS = System.getProperty("file.separator");

    /** */
    public void create() throws IOException {
        List<String> messagesList = null;
        if (messagesFilename != null) {
            messagesList = getStringList(new File(messagesFilename));
        }
        List<File> imageFileList = null;
        if (imageListFilename != null) {
            imageFileList = getFileList(new File(imageListFilename));
        } else {
            imageFileList = new ArrayList<File>();
        }
        List<File> soundFileList = null;
        if (soundListFilename != null) {
            soundFileList = getFileList(new File(soundListFilename));
        } else {
            soundFileList = new ArrayList<File>();
        }
        int p = 0;
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(output)));
        dos.writeByte((byte) version);
        p += 1;
        if (messagesList != null) {
            dos.writeByte((byte) messagesList.size());
            for (int i = 0; i < messagesList.size(); i++) {
                String message = messagesList.get(i);
                byte[] buf = message.getBytes("Windows-31J");
                dos.writeByte((byte) buf.length);
                p += 1;
                dos.write(buf);
                p += buf.length;
            }
        } else {
            dos.writeByte((byte) 0);
            p += 1;
        }
        if (imageFileList.size() > 0) {
            dos.writeByte((byte) imageFileList.size());
            p += 1;
            p += imageFileList.size() * 2;
        }
        if (soundFileList.size() > 0) {
            dos.writeByte((byte) soundFileList.size());
            p += 1;
            p += soundFileList.size() * 2;
        }
        for (int i = 0; i < imageFileList.size(); i++) {
            File file = imageFileList.get(i);
            int length = (int) file.length();
            dos.writeShort((short) p);
            p += length;
        }
        for (int i = 0; i < soundFileList.size(); i++) {
            File file = soundFileList.get(i);
            int length = (int) file.length();
            dos.writeShort((short) p);
            p += length;
        }
        for (int i = 0; i < imageFileList.size(); i++) {
            File file = imageFileList.get(i);
            int length = (int) file.length();
            byte[] buf = new byte[length];
            InputStream is = new BufferedInputStream(new FileInputStream(file));
            int l = 0;
            while (l < length) {
                int bytesRead = is.read(buf, l, length - l);
                if (bytesRead == -1) {
                    throw new IOException(String.valueOf(file));
                }
                l += bytesRead;
            }
            dos.write(buf, 0, length);
        }
        for (int i = 0; i < soundFileList.size(); i++) {
            File file = soundFileList.get(i);
            int length = (int) file.length();
            byte[] buf = new byte[length];
            InputStream is = new BufferedInputStream(new FileInputStream(file));
            int l = 0;
            while (l < length) {
                int bytesRead = is.read(buf, l, length - l);
                if (bytesRead == -1) {
                    throw new IOException(String.valueOf(file));
                }
                l += bytesRead;
            }
            dos.write(buf, 0, length);
        }
        dos.flush();
        dos.close();
    }

    /** */
    private List<String> getStringList(File messagesFile) throws IOException {
        List<String> list = new ArrayList<String>();
        BufferedReader r = new BufferedReader(new FileReader(messagesFile));
        while (r.ready()) {
            String line = r.readLine();
            if (line == null) {
                break;
            }
            list.add(line);
        }
        r.close();
        return list;
    }

    /** */
    private List<File> getFileList(File listFile) throws IOException {
        List<File> list = new ArrayList<File>();
        String cwd = listFile.getParent();
        BufferedReader r = new BufferedReader(new FileReader(listFile));
        while (r.ready()) {
            String line = r.readLine();
            if (line == null) {
                break;
            }
            File file = null;
            if (line.startsWith("/")) {
                file = new File(line);
            } else {
                file = new File(cwd + FS + line);
            }
            list.add(file);
        }
        r.close();
        return list;
    }

    /** */
    public static void main(String[] args) throws IOException {
        Pak pak = new Pak();
        pak.setOutput(args[0]);
        pak.setMessagesFilename(args[1]);
        pak.setImageListFilename(args[2]);
        pak.create();
    }
}
