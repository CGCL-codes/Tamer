package allensoft.javacvs.client;

import mindbright.ssh.*;
import mindbright.security.*;
import mindbright.terminal.*;
import java.io.*;

public class MindBrightSSHConnection extends CVSConnection {

    public MindBrightSSHConnection(CVSClient client, RepositoryLocation location, LoginDetails details, int port) throws CVSConnectionException {
        super(client, location);
        SSHAuthenticator authenticator = new SSHPasswordAuthenticator(details.getUserName(), details.getPassword());
        SSHClientUser user = new ClientUser(location.getHostName());
        m_SSHClient = new SSHClient(authenticator, user);
        try {
            m_SSHClient.setConsole(new Console());
            m_SSHClient.doSingleCommand("cvs server", true, 0);
        } catch (IOException e) {
            throw new CVSConnectionException(e);
        }
    }

    public MindBrightSSHConnection(CVSClient client, RepositoryLocation location, LoginDetails details) throws CVSConnectionException {
        this(client, location, details, SSH.DEFAULTPORT);
    }

    public InputStream getInputStream() {
        return m_In;
    }

    public OutputStream getOutputStream() {
        return m_Out;
    }

    public void close() {
        if (m_SSHClient != null) {
            m_SSHClient.forcedDisconnect();
            m_SSHClient = null;
        }
    }

    /** Private class which keeps user notified of progress of connection. */
    private class Interactor extends SSHInteractorAdapter {

        public boolean isVerbose() {
            return true;
        }

        public void alert(String msg) {
            displayStatus(msg);
        }

        public void report(String msg) {
            displayStatus(msg);
        }

        public void connected(SSHClient client) {
            displayStatus("Connected to " + getRepositoryLocation());
        }
    }

    private class ClientUser extends SSHClientUserAdaptor {

        ClientUser(String sHostName) {
            super(sHostName);
        }

        public SSHInteractor getInteractor() {
            return new Interactor();
        }

        private SSHInteractor m_Interactor = new Interactor();
    }

    /** Private class to send on data received from the server so it can be read though the connection's input
	 stream. */
    private class Console implements SSHConsole {

        public Console() throws IOException {
            m_PipedOutputStream = new PipedOutputStream(m_In);
        }

        public Terminal getTerminal() {
            return null;
        }

        public void stdoutWriteString(byte[] str) {
            try {
                m_PipedOutputStream.write(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void stderrWriteString(byte[] str) {
            try {
                displayStatus(new String(str, "ascii"));
            } catch (UnsupportedEncodingException e) {
                throw new Error("ASCII not supoorted " + e.getMessage());
            }
        }

        public void print(String str) {
            displayStatus(str);
        }

        public void println(String str) {
            displayStatus(str);
        }

        public void serverConnect(SSHChannelController controller, Cipher sndCipher) {
        }

        public void serverDisconnect(String reason) {
            displayStatus("Server disconnected: " + reason);
            try {
                m_PipedOutputStream.close();
            } catch (IOException e) {
            }
        }

        private PipedOutputStream m_PipedOutputStream;
    }

    private class SSHOutputStream extends OutputStream {

        public void write(int b) throws IOException {
            m_SSHClient.stdinWriteChar((char) b);
        }

        public void write(byte[] bytes, int offset, int length) throws IOException {
            m_SSHClient.stdinWriteString(bytes, offset, length);
        }

        public void write(byte[] bytes) throws IOException {
            write(bytes, 0, bytes.length);
        }
    }

    private PipedInputStream m_In = new PipedInputStream();

    private OutputStream m_Out = new SSHOutputStream();

    private SSHClient m_SSHClient;
}
