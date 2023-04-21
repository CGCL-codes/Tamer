package org.juicyapps.juicychat.prot;

import javax.swing.ImageIcon;

/**
 * 
 * @author hendrik@hhllcks.de
 * 
 */
public class Connection {

    private String strUsername;

    private String strOtherUsername;

    private String strServername;

    private String strServerAddress;

    private Integer intPort;

    private ImageIcon protocolIcon;

    public void connect(String connectionString) {
    }

    public void disconnect() {
    }

    ;

    public void sendMessage() {
    }

    ;

    public String getStrUsername() {
        return strUsername;
    }

    public void setStrUsername(String strUsername) {
        this.strUsername = strUsername;
    }

    public String getStrOtherUsername() {
        return strOtherUsername;
    }

    public void setStrOtherUsername(String strOtherUsername) {
        this.strOtherUsername = strOtherUsername;
    }

    public String getStrServername() {
        return strServername;
    }

    public void setStrServername(String strServername) {
        this.strServername = strServername;
    }

    public String getStrServerAddress() {
        return strServerAddress;
    }

    public void setStrServerAddress(String strServerAddress) {
        this.strServerAddress = strServerAddress;
    }

    public Integer getIntPort() {
        return intPort;
    }

    public void setIntPort(Integer intPort) {
        this.intPort = intPort;
    }

    public ImageIcon getProtocolIcon() {
        return protocolIcon;
    }

    public void setProtocolIcon(ImageIcon protocolIcon) {
        this.protocolIcon = protocolIcon;
    }
}
