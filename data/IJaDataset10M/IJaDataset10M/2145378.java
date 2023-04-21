package net.sf.jml.protocol.outgoing;

import net.sf.jml.Email;
import net.sf.jml.MsnProtocol;
import net.sf.jml.protocol.MsnOutgoingMessage;
import net.sf.jml.util.LocaleIdUtils;
import net.sf.jml.util.NumberUtils;

/**
 * Tell server current version and other information.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax: CVR trId localeId osType osVersion computerArch clientName clientVersion MSMSGS email
 * 
 * @author Roger Chen
 */
public class OutgoingCVR extends MsnOutgoingMessage {

    public OutgoingCVR(MsnProtocol protocol) {
        super(protocol);
        setCommand("CVR");
        setLocalId("0x" + NumberUtils.toHexValue(LocaleIdUtils.getLocaleId()));
        setOsType("winnt");
        setOsVersion("5.1");
        setComputerArch("i386");
        setClientName("MSNMSGR");
        setClientVersion("8.1.0178");
        addParam("MSMSGS");
    }

    public void setLocalId(String localId) {
        setParam(0, localId);
    }

    public String getLocaleId() {
        return getParam(0);
    }

    public void setOsType(String osType) {
        setParam(1, osType);
    }

    public String getOsType() {
        return getParam(1);
    }

    public void setOsVersion(String osVersion) {
        setParam(2, osVersion);
    }

    public String getOsVersion() {
        return getParam(2);
    }

    public void setComputerArch(String arch) {
        setParam(3, arch);
    }

    public String getComputerArch() {
        return getParam(3);
    }

    public void setClientName(String clientName) {
        setParam(4, clientName);
    }

    public String getClientName() {
        return getParam(4);
    }

    public void setClientVersion(String version) {
        setParam(5, version);
    }

    public String getClientVersion() {
        return getParam(5);
    }

    public void setEmail(Email email) {
        setParam(7, email.toString());
    }

    public String getEmail() {
        return getParam(7);
    }
}
