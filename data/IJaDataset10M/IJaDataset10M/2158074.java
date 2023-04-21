package oop.ex5.nameserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import oop.ex5.protocols.FMtoNSMsgType;
import oop.ex5.protocols.FMtoNSmsg;
import oop.ex5.protocols.MyFileSharingMsgFactory;
import oop.ex5.protocols.NStoFMmsgType;
import oop.ex5.protocols.NetworkAddress;

/**
 *  ClientThread is a thread created by the NameServer, each time it receives an incoming 
 *  commmunication from a file manager, thus enabling multi-client handling.
 * @author assafm02, shirpeled
 *
 */
public class ClientThread extends Thread {

    /**
	 * Socket timeout
	 */
    private final int TIMEOUT = 2000;

    /**
	 * Socket used by this thread
	 */
    private final Socket SOCKET;

    /**
	 * A reference to the server database
	 */
    private final FileDB FILE_DB;

    /**
	 * Time of termination of the thread, or null if not terminated yet
	 */
    private Date _deathDate;

    /**
	 * ctor.
	 * @param clientSocket the socket on which the thread will work
	 * @param db the server data base, holding the information regarding files and other FMs
	 */
    public ClientThread(Socket clientSocket, FileDB db) {
        SOCKET = clientSocket;
        FILE_DB = db;
        try {
            SOCKET.setSoTimeout(TIMEOUT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
	 * The thread run by this class, awaits messages from the user and act accordingly, unless
	 * a reannounce loop is issued - thread will terminate after the client request has been
	 * dealt with or timed out (2 seconds).
	 */
    public void run() {
        MyFileSharingMsgFactory messageFactory = new MyFileSharingMsgFactory();
        DataOutputStream out = null;
        DataInputStream in = null;
        try {
            out = new DataOutputStream(SOCKET.getOutputStream());
            in = new DataInputStream(SOCKET.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FMtoNSmsg message = messageFactory.getFMtoNSmessage(in);
            while (!FILE_DB.contains(message.getUploadAddress()) && message.getMessageType() != FMtoNSMsgType.BYE) {
                if (message.getMessageType() == FMtoNSMsgType.ANNOUNCE) {
                    announce(message, out);
                } else {
                    messageFactory.getNStoFMmessage(NStoFMmsgType.REANNOUNCE).send(out);
                }
                message = messageFactory.getFMtoNSmessage(in);
            }
            switch(message.getMessageType()) {
                case ANNOUNCE:
                    announce(message, out);
                    break;
                case REMOVE_FILE:
                    removeFile(message, out);
                    break;
                case ADD_FILE:
                    addFile(message, out);
                    break;
                case GET_FILE:
                    getFile(message, out);
                    break;
                case BYE:
                    removeFileManager(message, out);
                    break;
                default:
                    sendToClient(NStoFMmsgType.INVALID_SESSION, out);
                    break;
            }
        } catch (SocketTimeoutException ste) {
            closeConnection(in, out);
        } catch (IOException e) {
            try {
                messageFactory.getNStoFMmessage(NStoFMmsgType.INVALID_SESSION).send(out);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        closeConnection(in, out);
    }

    /**
	 * Closes connection and streams
	 * @param in input stream of the socket
	 * @param out output stream of the socket
	 */
    private void closeConnection(DataInputStream in, DataOutputStream out) {
        try {
            out.close();
            in.close();
            SOCKET.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        _deathDate = new Date();
    }

    /**
	 * Returns thread termination date
	 * @return thread termination date
	 */
    public Date getDeathDate() {
        return new Date(_deathDate.getTime());
    }

    /**
	 * Remove a fileManager from database
	 * @param message the message, assumed to be of type BYE
	 * @param out the output stream through which a reply will be sent
	 */
    private void removeFileManager(FMtoNSmsg message, DataOutputStream out) {
        if (FILE_DB.remove(message.getUploadAddress())) sendToClient(NStoFMmsgType.OK, out);
    }

    /**
	 * Retrieve a list of owners for the file and provide it to client, if no
	 * such file exists in db - return an INVALID message to client
	 * @param message the message from client, assume to be of GET_FILE type
	 * @param out output stream for delivery of list
	 */
    private void getFile(FMtoNSmsg message, DataOutputStream out) {
        if (!FILE_DB.contains(message.getFileName()) || FILE_DB.getOwnersList(message.getFileName()) == null) {
            sendToClient(NStoFMmsgType.FILE_NOT_FOUND, out);
        } else {
            HashSet<NetworkAddress> ownersList = new HashSet<NetworkAddress>(FILE_DB.getOwnersList(message.getFileName()));
            if (ownersList.isEmpty()) sendToClient(NStoFMmsgType.FILE_NOT_FOUND, out); else {
                try {
                    new MyFileSharingMsgFactory().getNStoFMmessage(ownersList).send(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
	 * Add a file to database
	 * @param message client message assumed to be of type ADD_FILE
	 * @param out output stream for result
	 */
    private void addFile(FMtoNSmsg message, DataOutputStream out) {
        FILE_DB.add(message.getFileName(), message.getUploadAddress());
        sendToClient(NStoFMmsgType.OK, out);
    }

    /**
	 * Remove file from server database
	 * @param message the message given by the client assumed to be of type
	 *        REMOVE_FILE
	 * @param out the output stream for result
	 */
    private void removeFile(FMtoNSmsg message, DataOutputStream out) {
        if (FILE_DB.remove(message.getFileName(), message.getUploadAddress())) sendToClient(NStoFMmsgType.OK, out); else sendToClient(NStoFMmsgType.INVALID_SESSION, out);
    }

    /**
	 * Deliver message to client
	 * @param msg the message
	 * @param out the output stream
	 */
    private void sendToClient(NStoFMmsgType msg, DataOutputStream out) {
        try {
            new MyFileSharingMsgFactory().getNStoFMmessage(msg).send(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Given an announce msg. the db should be updated. pre-condition - given
	 * message is of ANNOUNCE type.
	 * @param message the announcment message, assumed to be of type ANNOUNCE
	 * @param out the output stream through which replies will be sent
	 */
    private void announce(FMtoNSmsg message, DataOutputStream out) {
        FILE_DB.add(message.getUploadAddress());
        ArrayList<String> temp = new ArrayList<String>(message.getFileSet());
        for (int i = 0; i < temp.size(); i++) {
            FILE_DB.add(temp.get(i), message.getUploadAddress());
        }
        sendToClient(NStoFMmsgType.OK, out);
    }
}
