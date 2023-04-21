package net.cimd.packets;

import net.cimd.packets.parameters.*;
import java.util.*;

/**
 * Description of the Class
 *
 * @author    <a href="mailto:vsuman@gmail.com">Viorel Suman</a>
 * @version   $Id: CimdPacket.java,v 1.1 2007/03/14 14:15:08 viorel_suman Exp $
 */
public abstract class CimdPacket {

    private static final PacketRestriction restr = new PacketRestriction();

    protected byte opCode;

    protected byte packetNumber;

    protected HashMap params = new HashMap();

    public CimdPacket(byte opCode) {
        this.opCode = opCode;
    }

    CimdParameter getParam(String paramType) {
        return (CimdParameter) params.get(paramType);
    }

    public void validate() throws InvalidContentException {
        getPacketRestriction().checkPacket(this);
    }

    protected PacketRestriction getPacketRestriction() {
        return restr;
    }

    void addParam(CimdParameter param) {
        params.put(param.getType(), param);
    }

    public byte getOpCode() {
        return opCode;
    }

    public byte getPacketNumber() {
        return packetNumber;
    }

    public void setPacketNumber(byte packetNumber) {
        this.packetNumber = packetNumber;
    }

    protected String getStringParamValue(String paramType) {
        CimdParameter param = getParam(paramType);
        return param != null ? param.getValue() : null;
    }

    protected long getIntegerParamValue(String paramType) {
        CimdParameter param = getParam(paramType);
        return param != null ? Long.parseLong(param.getValue()) : 0;
    }

    protected byte[] getHexadecimalParamValue(String paramType) {
        CimdParameter param = getParam(paramType);
        return param != null ? HexadecimalParameter.parseString(param.getValue()) : null;
    }

    protected byte[] getUserDataParamValue(String paramType) throws InvalidContentException {
        CimdParameter param = getParam(paramType);
        return param != null ? UserDataParameter.parseString(param.getValue()) : null;
    }

    Collection getParameters() {
        return params.values();
    }

    public String toString() {
        return "CimdPacket{" + "opCode=" + opCode + ", packetNumber=" + (packetNumber & 0x00ff) + ", params=" + params + "}";
    }
}
