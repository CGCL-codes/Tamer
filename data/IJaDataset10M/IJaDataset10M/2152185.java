package servers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.apache.commons.codec.binary.Base64;
import org.grlea.log.SimpleLogger;
import vo.yahoo.ListMessageResponse;
import vo.yahoo.Message;
import vo.yahoo.MessageInfo;
import domainhandler.IDomainHandler;
import domainhandler.smtp.YahooBetaHandler;
import exception.LoginException;

public class SMTPService implements Runnable {

    private Socket socket;

    String username;

    Message message;

    StringBuilder sbRawMsg;

    String password;

    private static final SimpleLogger log = new SimpleLogger(SMTPService.class);

    public void run() {
        log.entry("thread start");
        BufferedReader d = null;
        IDomainHandler domainHandler = null;
        boolean dataReadIsON = false;
        try {
            d = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (true) {
                String line = d.readLine();
                log.debugObject("line: ", line);
                if (dataReadIsON) {
                    if (getSbRawMsg() == null) setSbRawMsg(new StringBuilder());
                    getSbRawMsg().append(line);
                    if (line.trim().equals(".")) {
                        dataReadIsON = false;
                        log.debugObject("final string", getSbRawMsg());
                    } else continue;
                }
                if (line.toUpperCase().startsWith("EHLO")) {
                    StringBuffer sbResp = new StringBuffer("");
                    sbResp.append("250-PopAnyhting Offers following FIFTEEN extensions:\r\n");
                    sbResp.append("250 AUTH plain\r\n");
                    bw.write(sbResp.toString());
                    bw.flush();
                } else if (line.toUpperCase().startsWith("AUTH")) {
                    String plainAuthString = new String(Base64.decodeBase64(line.split(" ")[2].getBytes()));
                    log.debugObject("authString:", plainAuthString);
                    String[] auth = plainAuthString.split("\0");
                    String userId = auth[1].split("@")[0];
                    setUsername(userId);
                    setPassword(auth[2]);
                    domainHandler = new YahooBetaHandler();
                    domainHandler = new YahooBetaHandler();
                    try {
                        domainHandler.logIn(getUsername(), getPassword());
                        bw.write("235 you are authenticated..\r\n");
                    } catch (LoginException e) {
                        e.printStackTrace();
                        bw.write("550 Something is gone crazy..\r\n");
                    }
                    bw.flush();
                } else if (line.toUpperCase().startsWith("MAIL")) {
                    if (getMessage() == null) setMessage(new Message());
                    String fromEmail = line.split(" ")[1];
                    message.setFromEmail(fromEmail.substring(6, fromEmail.length() - 1));
                    log.debugObject("fromEmail", getMessage());
                    bw.write("250 Got the from address...\r\n");
                    bw.flush();
                } else if (line.toUpperCase().startsWith("RCPT")) {
                    String toEmail = line.split(" ")[1];
                    log.debugObject("to Email", toEmail);
                    bw.write("250 OK\r\n");
                    bw.flush();
                } else if (line.toUpperCase().startsWith("DATA")) {
                    dataReadIsON = true;
                    bw.write("354 OK\r\n");
                    bw.flush();
                } else if (line.toUpperCase().startsWith("RETR")) {
                } else if (line.toUpperCase().startsWith("TOP")) {
                } else if (line.toUpperCase().startsWith("QUIT")) {
                    bw.write("+OK quitting!\r\n");
                    log.debug("quiting the thread");
                    getSocket().close();
                    break;
                } else if (line.toUpperCase().startsWith("AUTH")) {
                    bw.write("-ERR I have no AUTH imple.. u come anyways!\r\n");
                    bw.flush();
                    log.debug("AUTH failed. not supported");
                } else if (line.toUpperCase().startsWith("CAPA")) {
                    bw.write("+OK \r\nUSER\r\nUIDL\r\nTOP\r\n.\r\n");
                    bw.flush();
                    log.debug("CAPAbilities are made known");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.exit("thread end");
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public StringBuilder getSbRawMsg() {
        return sbRawMsg;
    }

    public void setSbRawMsg(StringBuilder sbRawMsg) {
        this.sbRawMsg = sbRawMsg;
    }
}
