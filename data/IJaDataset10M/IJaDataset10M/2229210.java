package net.abhijat.se.process.integration.cvsi.client;

import java.io.*;

/**
 * The first two arguments are used for localhost and port 
 * (actually used by the superclass). The third line should be the 
 * author name (user name) and fourth line should be the name of the
 * temporary file where CVS puts the comment. If you look at the 
 * coresponding entry in verifymsg, you will notice that there is 
 * no fourth argument (the temporary comment file). Somehow CVS 
 * adds the fourth argument. I do not have a clue how!  
 * @author abhijat
 */
public class LogMessageVerifyClient extends CVSClient {

    /**
     * This will be used by the server to locate the correct handler 
     * for messages sent by this client type.
     */
    public static final String MESSAGE_TYPE = "verify-msg";

    public static final int NULL_LINE_READ = -1;

    public LogMessageVerifyClient(String server, int port) throws Exception {
        super(server, port);
    }

    /**
     * This will return the number of errors encounterd. -1 if there
     * was a system error.
     */
    protected int sendMessageImpl(String[] args) throws Exception {
        this.sendLine(MESSAGE_TYPE);
        int count = 0;
        for (count = 0; count < args.length - 1; count++) {
            this.sendLine(args[count]);
        }
        File file = new File(args[count]);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = reader.readLine()) != null) {
            this.sendLine(line);
        }
        reader.close();
        this.sendEndOfMessage();
        line = this.readLine();
        if (line == null) {
            return NULL_LINE_READ;
        }
        if (CVSClientConstants.OK.equals(line)) {
            return 0;
        } else {
            int errorCount = 1;
            printErrorLine(line);
            while ((line = this.readLine()) != null) {
                printErrorLine(line);
                errorCount++;
            }
            return errorCount;
        }
    }

    public static void main(String[] args) {
        try {
            String server = args[0];
            int port = Integer.parseInt(args[1].trim());
            CVSClient client = new LogMessageVerifyClient(server, port);
            int exitStatus = client.sendMessage(args);
            System.exit(exitStatus);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
