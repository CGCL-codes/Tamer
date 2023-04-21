package org.warko.proto.https.command;

import java.io.IOException;
import java.util.Enumeration;
import org.warko.app.ApplicationInputStream;
import org.warko.app.ApplicationParameterList;
import org.warko.dapp.GenericParameterList;
import org.warko.proto.https.handler.Ph_https;
import org.warko.sts.ProtocolHandler;
import org.warko.sts.exception.ProtocolApplicationException;
import org.warko.sts.protocol.command.Endwith2newlineTextProtocolCommand;
import org.warko.sts.protocol.handler.TextBasedProtocolHandler;

/**
 * The <code>GetCommand</code> class ...<p>
 *
 * @author  Olivier Hoareau
 * @version 0.1, Feb 17, 2005
 * 
 * @since   JDK1.4
 */
public class Cmd_post extends Endwith2newlineTextProtocolCommand {

    private String httpVersion;

    private String uri;

    private ApplicationParameterList headers;

    private String content;

    /**
	 * TODO
	 * 
	 * @param ph
	 */
    public Cmd_post(ProtocolHandler ph) {
        this(ph, null, null, null, null);
    }

    public Cmd_post(ProtocolHandler ph, String httpVersion, String uri, ApplicationParameterList headers, String content) {
        super(ph, Ph_https.CMD_POST);
        this.httpVersion = httpVersion != null ? httpVersion : "1.0";
        this.uri = uri != null ? uri : "/";
        this.headers = headers != null ? headers : new GenericParameterList();
        this.content = content != null ? content : "";
    }

    public void parse(ApplicationInputStream is) throws ProtocolApplicationException {
        try {
            this.uri = TextBasedProtocolHandler.readStringWord(is);
            this.httpVersion = TextBasedProtocolHandler.readStringWord(is).split("/")[1];
            this.httpVersion = TextBasedProtocolHandler.readInteger(is).toString();
            String line = null;
            while (!(line = TextBasedProtocolHandler.readLine(is)).equals("")) {
                String[] data = line.split("\\s*:\\s*");
                this.headers.setParameter(data[0], data[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBytes() {
        String strHeaders = "";
        for (Enumeration en = this.headers.getParameterNames(); en.hasMoreElements(); ) {
            String key = (String) en.nextElement();
            strHeaders += (strHeaders.length() > 0 ? "\n" : "") + this.headers.getParameter(key);
        }
        return (Ph_https.CMD_POST + " " + uri + " HTTP/" + httpVersion + "\n" + strHeaders + (strHeaders.length() > 0 ? "\n" : "") + content + "\n").getBytes();
    }
}
