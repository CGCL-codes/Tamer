package message;

import java.io.Serializable;

/**
 * Tired of the casts..  replace super and sub-classes with one
 *  big do-everything class with all the stuff in it
 * Keeping the other two around, just in case change mind..
 *
 * @author Kevinsean Halle
 */
public class RTMsg implements Serializable, Cloneable {

    private String destServName;

    private short destServPort;

    private MsgType1 msgType1;

    private MsgType2 msgType2;

    private MsgType3 msgType3;

    private Level levelFrom;

    private int msgID;

    private short whoFrom;

    private short undividerPort;

    private int dataSize;

    private short dataOwnerPort;

    private int origMsgID;

    private short origWhoFrom;

    public String toString() {
        String retString = "RTMsg: ";
        retString += " DestPort:" + destServPort;
        retString += " " + msgType1;
        retString += " " + msgType2;
        retString += " " + msgType3;
        retString += " " + levelFrom;
        retString += " ID: " + msgID;
        retString += " whoFrom: " + whoFrom + "\n";
        retString += " Undivider Port: " + undividerPort;
        retString += " data size: " + dataSize;
        retString += " data owner's port: " + dataOwnerPort;
        retString += " original Msg's ID: " + origMsgID;
        retString += " orig who from: " + origWhoFrom + "\n";
        return retString;
    }

    public RTMsg clone() {
        RTMsg theClone = null;
        try {
            theClone = (RTMsg) super.clone();
        } catch (Exception e) {
        }
        return theClone;
    }

    /**
    * Creates a new instance of RTMsg 
    */
    public RTMsg() {
    }

    public void setServTo(String _destServName) {
        destServName = _destServName;
    }

    public String getServTo() {
        return destServName;
    }

    public void setPortTo(int _destServPort) {
        destServPort = (short) _destServPort;
    }

    public short getPortTo() {
        return destServPort;
    }

    public void setMsgType1(MsgType1 _msgType1) {
        msgType1 = _msgType1;
    }

    public MsgType1 getMsgType1() {
        return msgType1;
    }

    public void setMsgType2(MsgType2 _msgType2) {
        msgType2 = _msgType2;
    }

    public MsgType2 getMsgType2() {
        return msgType2;
    }

    public void setMsgType3(MsgType3 _msgType3) {
        msgType3 = _msgType3;
    }

    public MsgType3 getMsgType3() {
        return msgType3;
    }

    public void setLevelFrom(Level _levelFrom) {
        levelFrom = _levelFrom;
    }

    public Level getLevelFrom() {
        return levelFrom;
    }

    public void setMsgID(int _msgID) {
        msgID = _msgID;
    }

    public int getMsgID() {
        return msgID;
    }

    public void setWhoFrom(short _whoFrom) {
        whoFrom = _whoFrom;
    }

    public short getWhoFrom() {
        return whoFrom;
    }

    public void setUndivider(short _undividerPort) {
        undividerPort = _undividerPort;
    }

    public short getUndivider() {
        return undividerPort;
    }

    public void setDataSize(int _dataSize) {
        dataSize = _dataSize;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataOwner(short _dataOwnerPort) {
        dataOwnerPort = _dataOwnerPort;
    }

    public short getDataOwner() {
        return dataOwnerPort;
    }

    public void setOrigMsgID(int _origMsgID) {
        origMsgID = _origMsgID;
    }

    public int getOrigMsgID() {
        return origMsgID;
    }

    public void setOrigWhoFrom(short _origWhoFrom) {
        origWhoFrom = _origWhoFrom;
    }

    public short getOrigWhoFrom() {
        return origWhoFrom;
    }
}
