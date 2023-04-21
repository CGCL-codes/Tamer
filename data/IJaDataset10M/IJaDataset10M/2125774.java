package org.ozoneDB.core.admin;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.ozoneDB.DxLib.DxCollection;
import org.ozoneDB.DxLib.DxIterator;
import org.ozoneDB.ExternalDatabase;
import org.ozoneDB.ExternalTransaction;
import org.ozoneDB.core.*;
import org.ozoneDB.xml.util.*;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.ParserAdapter;

/**
 * @version $Revision: 1.5 $ $Date: 2002/12/29 11:15:56 $
 * @author <a href="http://www.smb-tec.com">SMB</a>
 * @author <a href="http://www.medium.net/">Medium.net</a>
 */
public class AdminClient implements SAXChunkProducerDelegate {

    static final String COMMAND_NU = "newuser";

    static final String COMMAND_RU = "removeuser";

    static final String COMMAND_AU = "allusers";

    static final String COMMAND_NG = "newgroup";

    static final String COMMAND_RG = "removegroup";

    static final String COMMAND_AG = "allgroups";

    static final String COMMAND_U2G = "user2group";

    static final String COMMAND_SD = "shutdown";

    static final String COMMAND_TXS = "txs";

    static final String COMMAND_BACKUP = "backup";

    static final String COMMAND_RESTORE = "restore";

    static final String COMMAND_START_GARBAGECOLLECTION = "startGC";

    static final String DB_URL = "ozonedb:remote://localhost:3333";

    String dbURL = DB_URL;

    PrintWriter out;

    PrintWriter verboseOut;

    ExternalDatabase db;

    Admin admin;

    public static void main(String[] args) throws Exception {
        if (args.length == 0 || args[0].equals("-h") || args[0].equals("-help")) {
            printUsage();
            System.exit(1);
        }
        String command = args[0];
        AdminClient client = new AdminClient(args);
        try {
            client.open();
            if (command.equals(COMMAND_NU)) {
                client.newUser(args);
            } else if (command.equals(COMMAND_AU)) {
                client.allUsers(args);
            } else if (command.equals(COMMAND_RU)) {
                client.removeUser(args);
            } else if (command.equals(COMMAND_NG)) {
                client.newGroup(args);
            } else if (command.equals(COMMAND_AG)) {
                client.allGroups(args);
            } else if (command.equals(COMMAND_RG)) {
                client.removeGroup(args);
            } else if (command.equals(COMMAND_U2G)) {
                client.addUser2Group(args);
            } else if (command.equals(COMMAND_TXS)) {
                client.numberOfTxs();
            } else if (command.equals(COMMAND_SD)) {
                client.shutdown();
            } else if (command.equals(COMMAND_BACKUP)) {
                client.backup(args);
            } else if (command.equals(COMMAND_RESTORE)) {
                client.restore(args);
            } else if (command.equals(COMMAND_START_GARBAGECOLLECTION)) {
                client.startGarbageCollection();
            } else {
                System.out.println("Unknown command: " + command);
                printUsage();
            }
        } catch (Exception e) {
            client.out.println("Error: " + e.getMessage());
            e.printStackTrace(client.verboseOut);
        }
        client.close();
    }

    public static void printUsage() {
        System.out.println("usage: ozoneAdmin <command> [options]");
        System.out.println("");
        System.out.println("commands: " + COMMAND_NU + " " + COMMAND_RU + " " + COMMAND_AU + " " + COMMAND_NG + " " + COMMAND_RG + " " + COMMAND_AG + " " + COMMAND_TXS + " " + COMMAND_BACKUP + " " + COMMAND_RESTORE + " " + COMMAND_START_GARBAGECOLLECTION);
        System.out.println("");
        System.out.println("note: all -name=value must be within double quotes for Windows systems!");
        System.out.println("");
        System.out.println("overall options:");
        System.out.println("            \"-dburl=<URL>\"          The URL of the database. (default: " + DB_URL + ")");
        System.out.println("            \"-verbose\"              Be verbose.");
        System.out.println("");
        System.out.println(COMMAND_NU + " options:");
        System.out.println("            \"-name=<username>\"");
        System.out.println("            \"-id=<userid>\"");
        System.out.println("");
        System.out.println(COMMAND_RU + " options:");
        System.out.println("            \"-name=<username>\"");
        System.out.println("");
        System.out.println(COMMAND_AU + " options:");
        System.out.println("   -");
        System.out.println("");
        System.out.println(COMMAND_NG + " options:");
        System.out.println("            \"-name=<groupname>\"");
        System.out.println("            \"-id=<groupid>\"");
        System.out.println("");
        System.out.println(COMMAND_RG + " options:");
        System.out.println("            \"-name=<groupname>\"");
        System.out.println("");
        System.out.println(COMMAND_AG + " options:");
        System.out.println("   -");
        System.out.println("");
        System.out.println(COMMAND_U2G + " options:");
        System.out.println("            \"-username=<username>\"");
        System.out.println("            \"-groupname=<groupname>\"");
        System.out.println("");
        System.out.println(COMMAND_TXS + " options:");
        System.out.println("   -");
        System.out.println("");
        System.out.println(COMMAND_SD + " options:");
        System.out.println("   -");
        System.out.println("");
        System.out.println(COMMAND_BACKUP + " options:");
        System.out.println("            \"-outfile=<filename>\"   The name of the output file. (default: System.out)");
        System.out.println("            \"-compress\"             gzip the output.");
        System.out.println("            \"-indent\"               Indent the XML output.");
        System.out.println("");
        System.out.println(COMMAND_RESTORE + " options:");
        System.out.println("            \"-infile=<filename>\"    The name of the input file. (default: System.in)");
        System.out.println("            \"-compress\"             Input is gzipped.");
        System.out.println("");
        System.out.println(COMMAND_START_GARBAGECOLLECTION + ": initiate persistent object garbage collection.");
    }

    public AdminClient(String[] _args) throws Exception {
        out = new PrintWriter(System.out, true);
        verboseOut = new PrintWriter(new CharArrayWriter());
        for (int i = 1; i < _args.length; i++) {
            String arg = _args[i];
            if (arg.startsWith("-dburl=")) {
                dbURL = arg.substring(7);
                verboseOut.println("dbURL: " + dbURL);
            } else if (arg.equals("-v") || arg.equals("-verbose")) {
                verboseOut = new PrintWriter(System.out, true);
                verboseOut.println("verbose=true");
            }
        }
    }

    public synchronized void open() throws Exception {
        db = ExternalDatabase.openDatabase(dbURL);
        db.reloadClasses();
        admin = db.admin();
    }

    protected synchronized void close() throws Exception {
        if (db != null) {
            db.close();
            db = null;
        }
    }

    protected void newUser(String[] _args) throws Exception {
        String name = null;
        int id = -1;
        for (int i = 1; i < _args.length; i++) {
            String arg = _args[i];
            if (arg.startsWith("-name=")) {
                name = arg.substring(6);
                verboseOut.println("name=" + name);
            } else if (arg.startsWith("-id=")) {
                id = Integer.parseInt(arg.substring(4));
                verboseOut.println("id=" + id);
            } else {
                out.println("Unknow argument: " + arg);
            }
        }
        try {
            admin.newUser(name, id);
        } catch (UserManagerException e) {
            out.println("Unable to create new user: " + e.getMessage());
        }
    }

    protected void removeUser(String[] _args) throws Exception {
        String name = null;
        for (int i = 1; i < _args.length; i++) {
            String arg = _args[i];
            if (arg.startsWith("-name=")) {
                name = arg.substring(6);
                verboseOut.println("name=" + name);
            } else {
                out.println("Unknow argument: " + arg);
            }
        }
        try {
            admin.removeUser(name);
        } catch (UserManagerException e) {
            out.println("Unable to remove user: " + e.getMessage());
        }
    }

    protected void allUsers(String[] _args) throws Exception {
        DxCollection users = null;
        try {
            users = admin.allUsers();
        } catch (UserManagerException e) {
            out.println("Unable to retrieve all users: " + e.getMessage());
        }
        if (users.isEmpty()) {
            out.println("No users found.");
        } else {
            for (DxIterator it = users.iterator(); it.next() != null; ) {
                User user = (User) it.object();
                out.println("user name: " + user.name());
                out.println("    id: " + user.id());
            }
        }
    }

    protected void newGroup(String[] _args) throws Exception {
        String name = null;
        int id = -1;
        for (int i = 1; i < _args.length; i++) {
            String arg = _args[i];
            if (arg.startsWith("-name=")) {
                name = arg.substring(6);
                verboseOut.println("name=" + name);
            } else if (arg.startsWith("-id=")) {
                id = Integer.parseInt(arg.substring(4));
                verboseOut.println("id=" + id);
            } else {
                out.println("Unknow argument: " + arg);
            }
        }
        try {
            admin.newGroup(name, id);
        } catch (UserManagerException e) {
            out.println("Unable to create new group: " + e.getMessage());
        }
    }

    protected void removeGroup(String[] _args) throws Exception {
        String name = null;
        for (int i = 1; i < _args.length; i++) {
            String arg = _args[i];
            if (arg.startsWith("-name=")) {
                name = arg.substring(6);
                verboseOut.println("name=" + name);
            } else {
                out.println("Unknow argument: " + arg);
            }
        }
        try {
            admin.removeGroup(name);
        } catch (UserManagerException e) {
            out.println("Unable to remove group: " + e.getMessage());
        }
    }

    protected void allGroups(String[] _args) throws Exception {
        DxCollection groups = null;
        try {
            groups = admin.allGroups();
        } catch (UserManagerException e) {
            out.println("Unable to retrieve all groups: " + e.getMessage());
            return;
        }
        if (groups.isEmpty()) {
            out.println("No groups found.");
        } else {
            for (DxIterator it = groups.iterator(); it.next() != null; ) {
                Group group = (Group) it.object();
                out.println("group name: " + group.name());
                out.println("    id: " + group.id());
                out.println("    user ID count: " + group.usersCount());
                DxIterator it2 = group.userIDs().iterator();
                int c = 1;
                String line = "    user IDs: ";
                while (it2.next() != null) {
                    line += (c > 1 ? ", " : "");
                    line += it2.object().toString();
                    if (c++ % 10 == 0) {
                        out.println(line);
                        line = "              ";
                    }
                }
                out.println(line);
            }
        }
    }

    protected void addUser2Group(String[] _args) throws Exception {
        String username = null;
        String groupname = null;
        for (int i = 1; i < _args.length; i++) {
            String arg = _args[i];
            if (arg.startsWith("-username=")) {
                username = arg.substring(10);
                verboseOut.println("user name=" + username);
            } else if (arg.startsWith("-groupname=")) {
                groupname = arg.substring(11);
                verboseOut.println("group name=" + groupname);
            } else {
                out.println("Unknow argument: " + arg);
            }
        }
        try {
            admin.addUser2Group(username, groupname);
        } catch (UserManagerException e) {
            out.println("Unable to add user to group: " + e.getMessage());
        }
    }

    protected void numberOfTxs() throws Exception {
        out.println(admin.numberOfTxs());
    }

    protected void shutdown() throws Exception {
        admin.shutdown();
    }

    protected void startGarbageCollection() {
        admin.startGarbageCollection();
    }

    protected void restore(String[] _args) throws Exception {
        String filename = "-";
        boolean compress = false;
        for (int i = 1; i < _args.length; i++) {
            String arg = _args[i];
            if (arg.equals("-compress")) {
                compress = true;
                verboseOut.println("compress=" + compress);
            } else if (arg.startsWith("-infile=")) {
                filename = arg.substring(8);
                verboseOut.println("filename=" + filename);
            } else {
                out.println("Unknow argument: " + arg);
            }
        }
        try {
            admin.beginRestore();
            InputStream in = System.in;
            if (!filename.equals("-")) {
                in = new FileInputStream(filename);
            }
            if (compress) {
                in = new GZIPInputStream(in, 4096);
            } else {
                in = new BufferedInputStream(in, 4096);
            }
            InputSource xmlSource = new InputSource(in);
            SAXChunkProducer producer = new SAXChunkProducer(this);
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            ParserAdapter adapter = new ParserAdapter(parser.getParser());
            adapter.setContentHandler(producer);
            adapter.parse(xmlSource);
        } catch (Exception e) {
            out.println("\nAn error occured while restoring the database.");
            out.println("The database is not entirely restored and is probably not usable!");
            out.println("Please completely install and restore again.");
            out.println("");
            e.printStackTrace(out);
        } finally {
            admin.processRestoreChunk(null);
        }
    }

    /**
     * This method is inherited from SAXChunkProducerDelegate. It is called
     * by the producer when a chunk is ready to be processed.
     */
    public void processChunk(SAXChunkProducer _producer) throws Exception {
        admin.processRestoreChunk(_producer.chunkStream().toByteArray());
    }

    protected void backup(String[] _args) throws Exception {
        String filename = "-";
        boolean indent = false;
        boolean raw = false;
        boolean compress = false;
        for (int i = 1; i < _args.length; i++) {
            String arg = _args[i];
            if (arg.equals("-compress")) {
                compress = true;
                verboseOut.println("compress=" + compress);
            } else if (arg.equals("-indent")) {
                indent = true;
                verboseOut.println("indent=" + indent);
            } else if (arg.equals("-raw")) {
                raw = true;
                verboseOut.println("raw=" + raw);
            } else if (arg.startsWith("-outfile=")) {
                filename = arg.substring(9);
                verboseOut.println("filename=" + filename);
            } else {
                out.println("Unknow argument: " + arg);
            }
        }
        ExternalTransaction tx = db.newTransaction();
        tx.begin();
        try {
            admin.beginBackup();
            OutputStream out = System.out;
            if (!filename.equals("-")) {
                out = new FileOutputStream(filename);
            }
            if (compress) {
                out = new GZIPOutputStream(out, 4096);
            } else {
                out = new BufferedOutputStream(out, 4096);
            }
            XMLSerializer serializer = new XMLSerializer(out, new OutputFormat("xml", "UTF-8", indent));
            SAXChunkConsumer consumer = new SAXChunkConsumer(serializer.asContentHandler());
            byte[] bytes = null;
            while ((bytes = admin.nextBackupChunk()) != null) {
                consumer.processChunk(bytes);
            }
            if (out instanceof GZIPOutputStream) {
                ((GZIPOutputStream) out).finish();
            }
            out.close();
        } finally {
            tx.rollback();
        }
    }
}
