package ch.ethz.dcg.spamato.filter.earlgrey.client;

import java.io.*;
import java.net.URL;
import java.security.KeyPair;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import ch.ethz.dcg.spamato.base.common.util.exception.ServerCommunicationException;
import ch.ethz.dcg.spamato.base.common.util.msg.Message;
import ch.ethz.dcg.spamato.base.common.util.msg.data.ErrorObject;
import ch.ethz.dcg.spamato.filter.earlgrey.common.msg.*;
import ch.ethz.dcg.spamato.filter.earlgrey.common.msg.data.*;
import ch.ethz.dcg.spamato.filter.earlgrey.common.security.*;

/**
 * This class implements the communication between the EarlgreyClient and the
 * EarlgreyServer. The communication to the EarlgreyClient is synchronized and
 * one Requester Object can be used by several EarlgreyClients at the same time.
 * 
 * @author Nicolas Burri
 * @author Simon Schlachter
 * @author keno - connection pool
 */
public class Requester {

    public enum RequestResult {

        ERROR, OK, DOUBLE_ACTION, WORK_IN_PROGRESS, CONFIG_SAVE_ERROR, UNDEFINED
    }

    private ArrayBlockingQueue<Connection> connections;

    private File rsaKeyStoreFile;

    public Requester(File rsaKeyStoreFile) {
        this.rsaKeyStoreFile = rsaKeyStoreFile;
        init();
    }

    private void init() {
        removeTempFiles();
        int maxConnections = ClientConfig.getInstance().getMaxConnections();
        connections = new ArrayBlockingQueue<Connection>(maxConnections);
        for (int i = 0; i < maxConnections; i++) {
            connections.add(new Connection());
        }
    }

    public RequestResult registerNewUser() throws ServerCommunicationException {
        Connection connection = getConnection();
        try {
            connection.open();
            Message requestAccount = MessageFactory.createRegisterNewUserMessage(ClientConfig.getInstance().getUsername());
            connection.sendMessage(requestAccount);
            Message answer = connection.readMessage();
            if (answer.getMessageType().equals(MessageConstants.Server.DOUBLE_ACTION)) {
                return RequestResult.DOUBLE_ACTION;
            } else if (!answer.getMessageType().equals(MessageConstants.Server.RSA_PUBLIC_KEY)) {
                return RequestResult.ERROR;
            }
            KeyObject keyObj = (KeyObject) answer.getData();
            KeyPair myKeys = KeyUtil.createKeyPair();
            EncryptedMessage keyPairMsg = MessageFactory.createKeyPairMessage(myKeys, ClientConfig.getInstance().getUsername(), keyObj.getPublicKeyString());
            connection.sendMessage(keyPairMsg);
            answer = connection.readMessage();
            if (answer.getMessageType().equals(MessageConstants.Server.DOUBLE_ACTION)) {
                return RequestResult.WORK_IN_PROGRESS;
            } else if (answer.getMessageType().equals(MessageConstants.Server.ERROR)) {
                return RequestResult.ERROR;
            }
            ClientConfig.getInstance().setRegistrationState(ClientConfig.RegState.AWAITING_CHALLENGE);
            ClientConfig.getInstance().setKeyPair(myKeys);
            ClientConfig.getInstance().saveToFile();
            return RequestResult.OK;
        } finally {
            connection.close();
            connections.add(connection);
        }
    }

    public RequestResult enableUser(String challengeString) throws ServerCommunicationException, SigningException {
        Connection connection = getConnection();
        try {
            connection.open();
            Message enableUserMsg = MessageFactory.createEnableUserAnswer(ClientConfig.getInstance().getUsername(), challengeString);
            connection.sendSignedMessage(enableUserMsg);
            Message answer = connection.readMessage();
            if (!MessageConstants.Server.OK.equals(answer.getMessageType())) {
                return RequestResult.ERROR;
            } else {
                ClientConfig.getInstance().setRegistrationState(ClientConfig.RegState.REGISTERED);
                ClientConfig.getInstance().saveToFile();
                return RequestResult.OK;
            }
        } finally {
            connection.close();
            connections.add(connection);
        }
    }

    public RequestResult reregisterExistingUser() throws ServerCommunicationException {
        Connection connection = getConnection();
        try {
            connection.open();
            RSA rsa = new RSA(128);
            Message reregMsg = MessageFactory.createReRegisterUserMessage(ClientConfig.getInstance().getUsername(), rsa.getPublic());
            connection.sendMessage(reregMsg);
            Message answer = connection.readMessage();
            if (answer.getMessageType().equals(MessageConstants.Server.ERROR)) {
                return RequestResult.ERROR;
            } else {
                ClientConfig.getInstance().setRegistrationState(ClientConfig.RegState.REREGISTERING_AWAITING_KEY);
                RSAKeyStore keyStore = new RSAKeyStore(rsa.getPrivate(), rsa.getPublic());
                ClientConfig.getInstance().saveToFile();
                if (saveRSA(keyStore)) {
                    return RequestResult.OK;
                } else {
                    return RequestResult.CONFIG_SAVE_ERROR;
                }
            }
        } finally {
            connection.close();
            connections.add(connection);
        }
    }

    public RequestResult rerequestChallenge() throws ServerCommunicationException {
        Connection connection = getConnection();
        try {
            connection.open();
            Message reSendMsg = MessageFactory.createReRequestChallenge(ClientConfig.getInstance().getUsername());
            connection.sendMessage(reSendMsg);
            Message answer = connection.readMessage();
            if (answer.getMessageType().equals(MessageConstants.Server.OK)) {
                return RequestResult.OK;
            } else {
                return RequestResult.ERROR;
            }
        } finally {
            connection.close();
            connections.add(connection);
        }
    }

    /**
	 * Sends a given Vector of URLs to the server and receives the spaminess of
	 * the message
	 */
    public RequestAnswerObject checkMail(Vector<URL> urls) throws ServerCommunicationException {
        if ((urls == null) || (urls.size() == 0)) {
            return null;
        }
        Connection connection = getConnection();
        try {
            connection.open();
            Message msg = MessageFactory.createCheckMessage(ClientConfig.getInstance().getUsername(), urls);
            connection.sendMessage(msg);
            Message answer = connection.readMessage();
            if (answer.getMessageType().equals(MessageConstants.Server.ERROR)) {
                ErrorObject error = (ErrorObject) answer.getData();
                throw new ServerCommunicationException("The Server answered with an unexpected Message or had an internal error:\n----" + error.getError() + "\n----");
            } else {
                RequestAnswerObject requestAnswer = (RequestAnswerObject) answer.getData();
                return requestAnswer;
            }
        } finally {
            connection.close();
            connections.add(connection);
        }
    }

    /**
	 * Report a mail as spam
	 */
    public RequestAnswerObject reportMail(Vector<URL> urls) throws ServerCommunicationException, SigningException {
        if ((urls == null) || (urls.size() == 0)) return null;
        Connection connection = getConnection();
        try {
            connection.open();
            Message m = MessageFactory.createReportMessage(ClientConfig.getInstance().getUsername(), urls);
            connection.sendSignedMessage(m);
            Message answer = connection.readMessage();
            if (answer.getMessageType().equals(MessageConstants.Server.ERROR)) {
                throw new ServerCommunicationException("The report failed.");
            } else {
                RequestAnswerObject requestAnswer = (RequestAnswerObject) answer.getData();
                return requestAnswer;
            }
        } finally {
            connection.close();
            connections.add(connection);
        }
    }

    public RequestAnswerObject revokeMail(Vector<URL> urls) throws ServerCommunicationException, SigningException {
        if ((urls == null) || (urls.size() == 0)) return null;
        Connection connection = getConnection();
        try {
            connection.open();
            Message msg = MessageFactory.createRevokeMessage(ClientConfig.getInstance().getUsername(), urls);
            connection.sendSignedMessage(msg);
            Message answer = connection.readMessage();
            if (answer.getMessageType().equals(MessageConstants.Server.ERROR)) {
                throw new ServerCommunicationException("The revoke failed.");
            } else {
                RequestAnswerObject reqAnswer = (RequestAnswerObject) answer.getData();
                return reqAnswer;
            }
        } finally {
            connection.close();
            connections.add(connection);
        }
    }

    private Connection getConnection() {
        while (true) {
            try {
                return connections.take();
            } catch (InterruptedException ie) {
            }
        }
    }

    /**
	 * delete unwanted temp files
	 */
    private void removeTempFiles() {
        if (ClientConfig.getInstance().getRegistrationState() == ClientConfig.RegState.REGISTERED) {
            if (rsaKeyStoreFile.exists()) rsaKeyStoreFile.delete();
        }
    }

    /**
	 * Saves the RSAKeyStore.
	 */
    private boolean saveRSA(RSAKeyStore keyStore) {
        try {
            keyStore.writeTo(rsaKeyStoreFile.getAbsolutePath());
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }
}
