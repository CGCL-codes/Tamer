package org.efficientia.cimap.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;
import org.efficientia.cimap.connection.Connection;
import org.efficientia.cimap.connection.ConnectionFactory;
import org.efficientia.cimap.utils.Strings;

/**
 * The Server class defines all interactions with an IMAP
 * server that CIMAP is capable of.  It currently defines
 * both the public interface or Fa�ade to the server plus
 * the internal logic to 'talk' to the server.
 * 
 * @author Ram�n Jim�nez (rjimenezz AT users DOT sourceforge DOT net)
 *
 */
public class Server {

    /** Default IMAP port. */
    public static final String DEFAULT_PORT = "143";

    /** Default buffer size for network reads. */
    protected static final int MAX_BUFFER_SIZE = 4096;

    /** Default ISO-8859-1 encoding prefix. */
    protected static final String ISO_8859_1_PREFIX = "ISO-8859-1?Q?";

    /** Buffer for line-by-line reading from the network. */
    protected StringBuffer fragment;

    /** Server host. */
    protected String host;

    /** Server port. */
    protected String port;

    /** Underlying network connection. */
    protected Connection conn;

    /** Underlying network input stream. */
    protected InputStream in;

    /** Underlying network output stream. */
    protected OutputStream out;

    /** IMAP command sequence for this connection. */
    protected int commandSeq;

    /**
	 * Builds a new server for the specified host/port combination.
	 * 
	 * @param host Server host
	 * @param port Server port
	 */
    public Server(String host, String port) {
        this.host = host;
        this.port = port;
        fragment = new StringBuffer();
    }

    /**
	 * Builds a new server for the specified host, using the
	 * default IMAP port.
	 * 
	 * @param host Server host
	 */
    public Server(String host) {
        this(host, DEFAULT_PORT);
    }

    /**
	 * Attempts to connect to the server.
	 */
    public void connect() {
        ConnectionFactory cf = ConnectionFactory.getInstance();
        try {
            conn = cf.getConnection(host, port);
            in = conn.getInputStream();
            out = conn.getOutputStream();
            readLineFromServer();
        } catch (Exception e) {
            System.err.println("!!!" + e.getMessage());
        }
    }

    /**
	 * Attempts to login to the server.  Please note
	 * that this class assumes the caller to properly
	 * sequence calls - all calls will fail at the IMAP
	 * level if the login is not performed or not successful.
	 * 
	 * @param userName User name
	 * @param password Password
	 * @return Whether the connection was successful or not
	 */
    public boolean login(String userName, String password) {
        String[] args = new String[] { "LOGIN ", "\"", userName, "\" \"", password, "\"" };
        CommandResponse resp = sendCommand(args);
        return resp.wasSuccessful();
    }

    /**
	 * Attempts to disconnect from the server.
	 * The answer is ignored.
	 */
    public void logout() {
        String[] args = new String[] { "LOGOUT" };
        sendCommand(args);
        commandSeq = 0;
        try {
            in.close();
            out.close();
            conn.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
	 * Lists all folders of the server,
	 * hierarchically organized.  Reserved
	 * folders are ignored.
	 * 
	 * @return Root folder loaded with all appropriate subfolders
	 */
    public Folder getFolders() {
        Folder inbox = Folder.createRootFolder();
        Vector pathList = new Vector();
        String mask = "INBOX.%";
        String more = ".%";
        boolean deeper = true;
        do {
            String[] command = new String[] { "LIST \"\" \"", mask, "\"" };
            String[] folders = sendCommand(command).getLines();
            for (int i = 0; i < folders.length - 1; i++) pathList.addElement(folders[i]);
            mask += more;
            deeper = hasDeeperSubFolders(folders);
        } while (deeper);
        pathList = extractFolderPaths(pathList);
        Enumeration paths = pathList.elements();
        while (paths.hasMoreElements()) {
            String eachFolder = (String) paths.nextElement();
            inbox.createSubFolders(eachFolder);
        }
        return inbox;
    }

    /**
	 * Retrieves all messages within a folder
	 * and adds them to the folder.
	 * 
	 * @param folder Folder to retrieve messages from
	 */
    public void getMessages(Folder folder) {
        String path = folder.getPath();
        CommandResponse selectResp = sendCommand(new String[] { "SELECT \"", path, "\"" });
        if (!selectResp.wasSuccessful()) {
            System.err.println("Oops!");
            return;
        }
        CommandResponse fetchResp = sendCommand(new String[] { "UID FETCH 1:* ", "(FLAGS BODY[HEADER.FIELDS(Subject)])" });
        fetchMessagesFromResponse(folder, fetchResp.getLines());
    }

    /**
	 * Retrieves the body of the specified message.
	 * Assumes that the folder that contains the message
	 * is currently selected.
	 * 
	 * @param msg Message to retrieve body for.
	 */
    public void getMessageBody(Message msg) {
        CommandResponse fetchResp = sendCommand(new String[] { "UID FETCH ", Integer.toString(msg.getUid()), " (BODY[TEXT])" });
        String[] lines = fetchResp.getLines();
        StringBuffer body = new StringBuffer();
        for (int i = 1; i < (lines.length - 2); i++) body.append(lines[i]);
        msg.setBody(body.toString().trim());
    }

    /**
	 * Determines whether the specified IMAP response
	 * indicates that at least one folder has children.
	 * 
	 * @param folders List of untagged responses to IMAP LIST command
	 * @return Whether more subfolders can be fetched
	 */
    protected boolean hasDeeperSubFolders(String[] folders) {
        String flag = "(\\HasChildren)";
        boolean deeper = false;
        for (int i = 0; i < folders.length; i++) if (folders[i].indexOf(flag) != -1) {
            deeper = true;
            break;
        }
        return deeper;
    }

    /**
	 * Extracts valid folder paths from the
	 * raw response of a LIST command.
	 *  
	 * @param folderList List of Strings defining a raw IMAP LIST command response
	 * @return List of cleansed folder paths
	 */
    protected Vector extractFolderPaths(Vector folderList) {
        Vector folderPaths = new Vector();
        for (int idx = 0; idx < folderList.size(); idx++) {
            String line = (String) folderList.elementAt(idx);
            if (line.startsWith("*")) {
                String[] pieces = Strings.split(line, " ");
                line = "";
                for (int c = 4; c < pieces.length; c++) line += (pieces[c] + ' ');
                line = line.substring(1);
                line = line.substring(0, line.length() - 2);
                folderPaths.addElement(line);
            }
        }
        return folderPaths;
    }

    /**
	 * Fetches messages from the provided IMAP response lines
	 * (as per {@link #getMessages(Folder)} and adds them
	 * to the specified folder.
	 * 
	 * @param folder Folder to which fetched messages belong
	 * @param fetchLines IMAP response of UID FETCH IMAP command
	 */
    protected void fetchMessagesFromResponse(Folder folder, String[] fetchLines) {
        Message msg = null;
        boolean deleted = false;
        int uid = 0;
        for (int i = 0; i < fetchLines.length; i++) {
            if (fetchLines[i].indexOf("\\Deleted") != -1) {
                deleted = true;
                continue;
            }
            if (fetchLines[i].indexOf("FETCH (UID") != -1) uid = getUIDFromResponse(fetchLines[i]); else if (fetchLines[i].startsWith("Subject: ")) {
                if (deleted) {
                    deleted = false;
                    continue;
                }
                String subject = fetchLines[i].substring("Subject: ".length());
                msg = new Message();
                msg.setUid(uid);
                msg.addHeader("Subject", cleanSubject(subject));
                folder.addMessage(msg);
            }
        }
    }

    /**
	 * Parses a response to the IMAP UID FETCH
	 * command sent by {@link #getMessages(Folder)}
	 * and returns the message's UID.
	 * 
	 * @param imap IMAP response to IMAP UID FETCH command
	 * @return Message UID
	 */
    protected int getUIDFromResponse(String imap) {
        imap = imap.substring(0, imap.indexOf(" BODY"));
        imap = imap.substring(imap.indexOf("FETCH (UID ") + "FETCH (UID ".length());
        imap = imap.substring(0, imap.indexOf(" "));
        return Integer.parseInt(imap);
    }

    /**
	 * Cleans a subject line, removing any line feeds
	 * it may contain (given that subjects are literals)
	 * and checking for ISO-8859-1 encoding.
	 *   
	 * @param subject Raw subject retrieved from IMAP server
	 * @return Cleansed subject
	 */
    public String cleanSubject(String subject) {
        String[] subjPieces = Strings.split(subject, "\r\n");
        subject = "";
        int pIdx = 0;
        for (; pIdx < subjPieces.length; pIdx++) subject += subjPieces[pIdx];
        if (subject.indexOf(ISO_8859_1_PREFIX) == -1) return subject;
        StringBuffer decoded = new StringBuffer();
        subjPieces = Strings.split(subject, "=?");
        for (pIdx = 0; pIdx < subjPieces.length; pIdx++) {
            if (subjPieces[pIdx].indexOf(ISO_8859_1_PREFIX) == -1) {
                decoded.append(subjPieces[pIdx]);
                continue;
            }
            subjPieces[pIdx] = subjPieces[pIdx].substring(ISO_8859_1_PREFIX.length());
            subjPieces[pIdx] = subjPieces[pIdx].substring(0, subjPieces[pIdx].length() - 2);
            for (int i = 0; i < subjPieces[pIdx].length(); i++) {
                char c = subjPieces[pIdx].charAt(i);
                if (c == ' ' || c == '?') continue;
                if (c == '_') {
                    decoded.append(' ');
                    continue;
                }
                if (c == '=') {
                    String hex = subjPieces[pIdx].substring(i + 1, i + 3);
                    decoded.append((char) Short.parseShort(hex, 16));
                    i += 2;
                    continue;
                }
                decoded.append(c);
            }
        }
        return decoded.toString();
    }

    /**
	 * Sends an IMAP command to the server.
	 * The command sequence is automatically inserted.
	 * All specified arguments are concatenated with
	 * a space in between.  Quoting the arguments is
	 * the responsibility of the caller.  The provided
	 * response includes both the response code (last
	 * line is tagged) and also the calculated status
	 * of the response from it.
	 * 
	 * @param args  Arguments of command to send
	 * @return Response object modeling the command's response
	 */
    protected CommandResponse sendCommand(String[] args) {
        CommandResponse resp = new CommandResponse();
        StringBuffer cmd = new StringBuffer(++commandSeq + " ");
        String response[] = null;
        for (int i = 0; i < args.length; i++) cmd.append(args[i]);
        cmd.append("\n");
        try {
            out.write(cmd.toString().getBytes());
            out.flush();
            response = readResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        resp.setLines(response);
        resp.setWasSuccessful(response[response.length - 1].startsWith(commandSeq + " OK "));
        return resp;
    }

    /**
	 * Reads a complete line from the server.
	 * Reading is buffered, supported by the <tt>buffer</tt>
	 * class member.  If there is not a complete line to 
	 * read from the buffer and the network combined, this
	 * method will block.
	 * 
	 * @return Complete line read from server
	 * @throws IOException if an error occurs attempting to read the line
	 */
    protected String readLineFromServer() throws IOException {
        byte[] buffer = new byte[MAX_BUFFER_SIZE];
        int termPos = -1;
        int nRead = 0;
        String line = null;
        while (true) {
            termPos = fragment.toString().indexOf("\r\n");
            if (fragment.length() == 0 || termPos == -1) {
                nRead = in.read(buffer, 0, MAX_BUFFER_SIZE);
                for (int i = 0; i < nRead; i++) fragment.append((char) buffer[i]);
            }
            termPos = fragment.toString().indexOf("\r\n");
            if (termPos != -1) {
                char[] lineChars = new char[termPos];
                fragment.getChars(0, termPos, lineChars, 0);
                line = new String(lineChars);
                fragment.delete(0, termPos + 2);
                break;
            }
        }
        return line;
    }

    /**
	 * Reads exactly the specified number of characters
	 * from the server.  Characters are read through the
	 * buffer so as not to break {@link #readLineFromServer()}.
	 * If there are not enough chars to read from the buffer and
	 * the network combined this method will block.
	 * 
	 * @param count Number of chars to read from server
	 * @return String with chars read
	 * @throws IOException if there is an error reading characters
	 */
    protected String readChars(int count) throws IOException {
        char[] chars = new char[count];
        byte[] buffer = new byte[MAX_BUFFER_SIZE];
        int nRead = 0;
        while ((fragment.length()) < count) {
            nRead = in.read(buffer, 0, MAX_BUFFER_SIZE);
            for (int i = 0; i < nRead; i++) fragment.append((char) buffer[i]);
        }
        fragment.getChars(0, count, chars, 0);
        fragment.delete(0, count);
        return new String(chars);
    }

    /**
	 * Reads the response of an IMAP command from
	 * the server.  This basically amounts to reading
	 * lines until we hit a tagged line.
	 * 
	 * @return IMAP response as an array of lines
	 * @throws IOException if an error occurs reading the response
	 */
    protected String[] readResponse() throws IOException {
        Vector lines = new Vector();
        boolean untaggedLine = true;
        while (untaggedLine) {
            String line = readLineFromServer();
            lines.addElement(line);
            untaggedLine = (line.startsWith("*"));
            if (untaggedLine && line.endsWith("}")) {
                int count = Integer.parseInt(line.substring(line.lastIndexOf('{') + 1, line.length() - 1));
                String literal = readChars(count);
                lines.addElement(literal);
                lines.addElement(readLineFromServer());
            }
        }
        int nLines = lines.size();
        String[] ret = new String[nLines];
        for (int i = 0; i < nLines; i++) ret[i] = (String) lines.elementAt(i);
        return ret;
    }
}
