package jeeves.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

/** Simple class used to send an e-mail
  */
public class EMail {

    private String sFrom;

    private String sTo;

    private String sSubject;

    private String sBody;

    private String sMailServer;

    private int iPort;

    private String sLastError;

    private BufferedReader in;

    private OutputStreamWriter out;

    private static final int DEFAULT_PORT = 25;

    public EMail(String mailServer) {
        setMailServer(mailServer, DEFAULT_PORT);
    }

    public EMail(String mailServer, int port) {
        setMailServer(mailServer, port);
    }

    /** setup the mail server (and port) to which the e-mail will be sent
	  */
    public void setMailServer(String mailServer, int port) {
        sMailServer = mailServer;
        iPort = port;
    }

    public void setFrom(String from) {
        sFrom = from;
    }

    public void setTo(String to) {
        sTo = to;
    }

    public void setSubject(String subject) {
        sSubject = subject;
    }

    public void setBody(String body) {
        sBody = body;
    }

    /** Sends the message to the mail server
	  */
    public boolean send() throws IOException {
        Socket socket = new Socket(sMailServer, iPort);
        in = new BufferedReader(new InputStreamReader(new DataInputStream(socket.getInputStream())));
        out = new OutputStreamWriter(new DataOutputStream(socket.getOutputStream()), "ISO-8859-1");
        if (lookMailServer()) if (sendData("2", "HELO " + InetAddress.getLocalHost().getHostName() + "\r\n")) if (sendData("2", "MAIL FROM: <" + sFrom + ">\r\n")) if (sendData("2", "RCPT TO: <" + sTo + ">\r\n")) if (sendData("354", "DATA\r\n")) if (sendData("2", buildContent())) if (sendData("2", "QUIT\r\n")) return true;
        sendData("2", "QUIT\r\n");
        return false;
    }

    public String getLastError() {
        return sLastError;
    }

    private boolean lookMailServer() throws IOException {
        sLastError = in.readLine();
        return sLastError.startsWith("2");
    }

    /** Sends a string to the socket
	  */
    private boolean sendData(String error, String data) throws IOException {
        out.write(data);
        out.flush();
        sLastError = in.readLine();
        return sLastError.startsWith(error);
    }

    /** Builds the message putting all pieces together
	  */
    private String buildContent() {
        return "Date: " + new Date().toString() + "\r\n" + "From: " + sFrom + "\r\n" + "Subject: " + sSubject + "\r\n" + "To: " + sTo + "\r\n" + "\r\n" + sBody + "\r\n" + "\r\n" + "." + "\r\n";
    }
}
