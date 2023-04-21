package parallab.unicore.plugins.iassh.njs;

import com.fujitsu.arcon.njs.Authoriser;
import com.fujitsu.arcon.njs.interfaces.*;
import org.unicore.*;
import org.unicore.upl.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class IALocal extends Thread {

    private ServerSocket serverSocket = null;

    private com.fujitsu.arcon.njs.interfaces.AFT.NJS njs;

    private Logger logger;

    private boolean listening = true;

    private UUDB uudb;

    public IALocal(com.fujitsu.arcon.njs.interfaces.AFT.NJS njs, int port) throws Exception {
        super("IA local");
        setDaemon(true);
        logger = Logger.getInstance("IA NJS");
        this.njs = njs;
        logger.info("IALocal server started");
        serverSocket = new ServerSocket(port);
        start();
    }

    public void run() {
        try {
            while (listening) new IAThread(serverSocket.accept()).start();
            serverSocket.close();
        } catch (Exception ex) {
            logger.info("IA ACCEPT FAILED: " + ex);
            listening = false;
        }
    }

    public Reply sendPublicKey(String public_key, IncarnatedUser iuser) {
        UnicoreResponse fromTSI = new UnicoreResponse();
        Reply replyTSI = new Reply();
        String reply = "";
        String command = "#/bin/sh\n#TSI_EXECUTESCRIPT\n#TSI_IDENTITY " + iuser.getXlogin() + " NONE \n mkdir -p $HOME/.ssh \n echo '" + public_key + "' >> $HOME/.ssh/authorized_keys \n chmod 600 $HOME/.ssh/authorized_keys";
        try {
            TSIConnection tsi = njs.getTSIConnection(iuser);
            reply = tsi.send(command);
            tsi.done();
        } catch (Exception ex) {
            fromTSI.setComment(ex.toString());
            logger.warning(ex.toString());
            fromTSI.setReturnCode(-2);
            fromTSI.setTimeStamp(new java.util.Date());
            replyTSI.addTraceEntry(fromTSI);
            return replyTSI;
        }
        if (reply.startsWith("TSI_FAILED")) {
            fromTSI.setComment(reply);
            logger.warning(reply);
            fromTSI.setReturnCode(-2);
            fromTSI.setTimeStamp(new java.util.Date());
            replyTSI.addTraceEntry(fromTSI);
            return replyTSI;
        }
        if (((reply.indexOf("Command execution FAILED") > -1) && reply.startsWith("TSI_OK")) || reply.startsWith("TSI_OK")) {
            fromTSI.setComment("Public key transfer completed for user: " + iuser.getXlogin());
            logger.info(reply);
            fromTSI.setReturnCode(0);
            fromTSI.setTimeStamp(new java.util.Date());
            replyTSI.addTraceEntry(fromTSI);
            return replyTSI;
        }
        return replyTSI;
    }

    /**
     * instruct the TSI to clear the unicore temporary keys of the user
     */
    public Reply doClearKeys(IncarnatedUser iuser) {
        UnicoreResponse fromTSI = new UnicoreResponse();
        Reply replyTSI = new Reply();
        String reply = "";
        String command = "#/bin/sh\n#TSI_EXECUTESCRIPT\n#TSI_IDENTITY " + iuser.getXlogin() + " NONE \n perl -pi -e 's/^from=.*ssh-rsa.*unicore-tmp-key\n$//g'  $HOME/.ssh/authorized_keys";
        logger.talk("IALOCAL: TSI delete keys");
        try {
            TSIConnection tsi = njs.getTSIConnection(iuser);
            reply = tsi.send(command);
            tsi.done();
        } catch (Exception ex) {
            fromTSI.setComment(ex.toString());
            logger.warning(ex.toString());
            fromTSI.setReturnCode(-2);
            fromTSI.setTimeStamp(new java.util.Date());
            replyTSI.addTraceEntry(fromTSI);
            return replyTSI;
        }
        if (reply.startsWith("TSI_FAILED")) {
            fromTSI.setComment(reply);
            logger.warning(reply);
            fromTSI.setReturnCode(-2);
            fromTSI.setTimeStamp(new java.util.Date());
            replyTSI.addTraceEntry(fromTSI);
            return replyTSI;
        }
        if (((reply.indexOf("Command execution FAILED") > -1) && reply.startsWith("TSI_OK")) || reply.startsWith("TSI_OK")) {
            fromTSI.setComment("Public keys deletet for user: " + iuser.getXlogin());
            logger.info(reply);
            fromTSI.setReturnCode(0);
            fromTSI.setTimeStamp(new java.util.Date());
            replyTSI.addTraceEntry(fromTSI);
            return replyTSI;
        }
        return replyTSI;
    }

    /**
     * Thread to authorize a user
     */
    private class IAThread extends Thread {

        private Socket socket = null;

        protected IAThread(Socket socket) {
            super("IA local");
            setDaemon(true);
            this.socket = socket;
        }

        public void run() {
            ObjectOutputStream oos = null;
            UnicoreResponse response = new UnicoreResponse();
            Reply reply = new Reply();
            Reply fromTSI = null;
            Object o = null;
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());
                o = ois.readObject();
                IncarnatedUser iuser = null;
                uudb = UUDB.getInstance();
                ConsignJob cj = (ConsignJob) o;
                try {
                    iuser = Authoriser.authoriseConsignJob(cj.getEndorser(), cj.getConsignor());
                } catch (Authoriser.UnauthorisedException uaex) {
                    response.setComment(uaex.toString());
                    logger.warning(uaex.toString());
                    response.setReturnCode(-1);
                    response.setTimeStamp(new java.util.Date());
                    reply.addTraceEntry(response);
                    oos.writeObject(reply);
                    return;
                }
                if (cj.getSignature() == null) {
                    response.setComment("No signature supplied in this ConsignJobRequest");
                    response.setReturnCode(-1);
                    logger.warning("No signature supplied in this ConsignJobRequest");
                    response.setTimeStamp(new java.util.Date());
                    reply.addTraceEntry(response);
                    oos.writeObject(reply);
                    return;
                }
                try {
                    Authoriser.verifySignature(cj.getAJO(), cj.getSignature(), cj.getEndorser());
                } catch (Authoriser.UnauthorisedException uaex) {
                    response.setComment(uaex.toString());
                    response.setReturnCode(-1);
                    logger.warning(uaex.toString());
                    response.setTimeStamp(new java.util.Date());
                    reply.addTraceEntry(response);
                    oos.writeObject(reply);
                    return;
                }
                String public_key = (String) ois.readObject();
                fromTSI = sendPublicKey(public_key, iuser);
                logger.info("Returning the following information to the requesting IA service.");
                logger.info("Xlogin: " + iuser.getXlogin());
                oos.writeObject(fromTSI);
                o = ois.readObject();
                if (((String) o).equals("CLEAR KEYS")) {
                    fromTSI = doClearKeys(iuser);
                    logger.info(fromTSI.getLastEntry().getComment());
                }
                oos.close();
                ois.close();
                logger.info("Thread done");
            } catch (Exception ex) {
                logger.warning("Problems with local listening: " + ex);
                try {
                    if (oos == null) {
                        oos = new ObjectOutputStream(socket.getOutputStream());
                    }
                    oos.writeObject(new Integer(-1));
                    oos.writeObject(ex.toString());
                } catch (Exception eex) {
                    logger.warning("Failed to write to peer: " + eex);
                }
                logger.info("IALocal done");
            }
        }
    }
}
