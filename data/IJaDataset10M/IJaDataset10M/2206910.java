package org.xbeans.communication.http.sender;

import org.w3c.dom.Document;
import org.xbeans.*;
import org.xbeans.communication.Representation;
import org.xbeans.communication.XbeanSender;
import java.net.*;
import java.io.*;

/**
 *
 *  @author Bruce Martin
 *  @version $Id: SenderBean.java,v 1.4 2002/10/02 22:56:09 brucemartin Exp $
 */
public class SenderBean implements XbeanSender {

    /**
	 */
    public SenderBean() {
    }

    private String remoteURL;

    private boolean compression = false;

    /**
	 *
	 *  @param evt
	 *
	 *  @throws XbeansException
	 */
    public void documentReady(DOMEvent evt) throws XbeansException {
        try {
            URL receiver = new URL(getId());
            URLConnection receiverConnection = receiver.openConnection();
            receiverConnection.setDoOutput(true);
            OutputStream receiverStream = receiverConnection.getOutputStream();
            byte[] externalRepresentation = Representation.externalize(evt, compression);
            receiverStream.write(externalRepresentation);
            receiverStream.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(receiverConnection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
        } catch (Throwable e) {
            e.printStackTrace(System.err);
        }
    }

    /**
	 *
	 *  @param newRemoteURL
	 */
    public void setId(String newRemoteURL) {
        remoteURL = newRemoteURL;
    }

    /**
	 *
	 *  @return String
	 */
    public String getId() {
        return remoteURL;
    }

    public void setCompression(boolean compressionOn) {
        compression = compressionOn;
    }

    public boolean getCompression() {
        return compression;
    }
}
