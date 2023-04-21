package dms.provider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import dms.mail.Header;

/**
 *
 * @author dseywald
 */
public class MBoxFolder extends Folder {

    private static final int START_NUMBER = 64;

    private File MBOX = null;

    private boolean open = false;

    private HashMap<Integer, Long> mboxIndex = null;

    private HashMap<Integer, Header> headers = null;

    public MBoxFolder(Store store, File file) {
        super(store);
        MBOX = file;
    }

    @Override
    public String getName() {
        return (MBOX != null) ? MBOX.getName() : null;
    }

    @Override
    public String getFullName() {
        return (MBOX != null) ? MBOX.getAbsolutePath() : null;
    }

    @Override
    public Folder getParent() throws MessagingException {
        return null;
    }

    @Override
    public boolean exists() throws MessagingException {
        return (MBOX != null) ? MBOX.exists() : false;
    }

    @Override
    public Folder[] list(String string) throws MessagingException {
        return new Folder[] { this };
    }

    @Override
    public char getSeparator() throws MessagingException {
        return System.getProperties().get("file.separator").toString().charAt(0);
    }

    @Override
    public int getType() throws MessagingException {
        if (MBOX.exists()) {
            return MBOX.isDirectory() ? HOLDS_FOLDERS : HOLDS_MESSAGES;
        }
        return 0;
    }

    @Override
    public boolean create(int i) throws MessagingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasNewMessages() throws MessagingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Folder getFolder(String string) throws MessagingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean delete(boolean bln) throws MessagingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean renameTo(Folder folder) throws MessagingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void open(int i) throws MessagingException {
        try {
            boolean created = MBOX.createNewFile();
            if (created == false) {
                caluclateMBoxIndex();
            }
            open = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void close(boolean bln) throws MessagingException {
        open = false;
        mboxIndex.clear();
        headers.clear();
        mboxIndex = null;
        headers = null;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public Flags getPermanentFlags() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getMessageCount() throws MessagingException {
        return (mboxIndex != null) ? mboxIndex.size() : 0;
    }

    @Override
    public Message getMessage(int i) throws MessagingException {
        return new MBoxMessage(getMessageHeader(i), readMessageBody(i));
    }

    @Override
    public void appendMessages(Message[] msgs) throws MessagingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Message[] expunge() throws MessagingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Header getMessageHeader(int index) {
        return headers.get(index);
    }

    public HashMap caluclateMBoxIndex() {
        mboxIndex = new HashMap<Integer, Long>(START_NUMBER);
        headers = new HashMap<Integer, Header>(START_NUMBER);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(MBOX));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
        String line = null;
        long mark = 0;
        int index = 0;
        boolean start = true;
        try {
            while ((line = br.readLine()) != null) {
                if (start && !isMboxFileLine(line)) {
                    System.out.println("NOT A VALID MBOX FILE.");
                    return null;
                }
                start = false;
                if (isMboxFileLine(line)) {
                    mark += line.length() + 2;
                    mboxIndex.put(index, mark);
                    br.mark(8192);
                    headers.put(index, parseHeader(br));
                    br.reset();
                    index++;
                } else {
                    mark += line.length() + 2;
                }
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return mboxIndex;
    }

    public String readMessageFromMBox(int index) {
        StringBuilder buffer = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(MBOX));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        long position = mboxIndex.get(index);
        try {
            br.skip(position);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (isMboxFileLine(line)) {
                    break;
                } else {
                    buffer.append(line + "\r\n");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        buffer.delete(buffer.length() - 4, buffer.length());
        return buffer.toString();
    }

    private boolean isMboxFileLine(String line) {
        boolean valid = false;
        if (line.startsWith("From ")) {
            String[] tmp = line.split(" ");
            if (tmp != null) {
                if (tmp.length >= 3) {
                    if (tmp[1].length() > 0 || isTimeStamp(tmp[2])) {
                        valid = true;
                    }
                }
            }
        }
        return valid;
    }

    private Header parseHeader(BufferedReader br) throws IOException, MessagingException {
        Header header = new Header();
        String line = null;
        long length = 0;
        Vector<String> entries = new Vector<String>();
        StringBuilder buffer = new StringBuilder();
        boolean start = true;
        while ((line = br.readLine()) != null) {
            if (isBlankLine(line) == false) {
                if (start) {
                    buffer.append(line);
                }
                if (line.startsWith(" ") && line.trim().length() > 0) {
                    buffer.append(line.trim());
                } else {
                    entries.add(buffer.toString());
                    buffer = new StringBuilder();
                    buffer.append(line);
                }
                length += (line.length() + 2);
                start = false;
            } else {
                break;
            }
        }
        for (int i = 0; i < entries.size(); i++) {
            String entry = entries.elementAt(i);
            if (entry.toUpperCase().startsWith("FROM")) {
                header.setFrom(getHeaderData(entry));
            } else if (entry.toUpperCase().startsWith("DATE")) {
                header.setDate(getHeaderData(entry));
            } else if (entry.toUpperCase().startsWith("SUBJECT")) {
                header.setSubject(getHeaderData(entry));
            } else if (entry.toUpperCase().startsWith("TO")) {
                header.setTo(getHeaderData(entry));
            } else if (entry.toUpperCase().startsWith("BCC")) {
                header.setBCC(getHeaderData(entry));
            } else if (entry.toUpperCase().startsWith("CC")) {
                header.setCC(getHeaderData(entry));
            }
        }
        header.setHeaderLength(length + 2);
        return header;
    }

    public String readMessageBody(int index) {
        StringBuilder buffer = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(MBOX));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        long position = mboxIndex.get(index);
        long headerLength = headers.get(index).getHeaderLength();
        try {
            br.skip(position);
            br.skip(headerLength);
            String line = null;
            boolean start = true;
            while ((line = br.readLine()) != null) {
                if (!start && isMboxFileLine(line)) {
                    break;
                } else {
                    buffer.append(line + "\r\n");
                }
                start = false;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        buffer.delete(buffer.length() - 4, buffer.length());
        return buffer.toString();
    }

    public boolean writeMessagesToMBox(Message message) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(MBOX, true);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
        try {
            String from = (message.getFrom() != null) ? message.getFrom()[0].toString() : "-";
            String fromLine = "From " + from + " " + new Date().toString();
            fos.write(fromLine.getBytes());
            fos.write("\r\n".getBytes());
            message.writeTo(fos);
            fos.write("\r\n".getBytes());
            fos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public boolean writeMessageToMBox(String message) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("TEST", true);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
        try {
            fos.write(message.getBytes());
            fos.write('\r');
            fos.write('\n');
            fos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    private boolean isBlankLine(String line) {
        if (line == null) {
            return true;
        }
        boolean blank = false;
        if (line.equals("") || line.equals("\n") || line.equals("\r\n") || line.length() <= 0) {
            blank = true;
        }
        return blank;
    }

    private String getHeaderData(String header) throws IOException {
        String[] data = header.split(":", 2);
        if (data == null) {
            System.out.println("NO SPLIT CHAR FOUND");
            return null;
        } else {
            if (data.length < 2) {
                System.out.println("WRONG AMOUNT OF TOKENS");
                return null;
            } else {
                return data[1];
            }
        }
    }

    private boolean isTimeStamp(String time) {
        boolean date = false;
        String[] ts = time.split(" ");
        if (ts == null || ts.length < 5) {
            date = false;
        } else {
            if (ts[0].length() == 3 && ts[1].length() == 3 && ts[2].length() == 2 && ts[3].length() > 0 && ts[4].length() == 4) {
                String[] tmp = ts[3].split(":");
                if (tmp != null && tmp.length == 3) {
                    date = true;
                }
            }
        }
        if (!date) {
            System.out.println("DATE FORMAT FALSE");
        }
        return date;
    }
}
