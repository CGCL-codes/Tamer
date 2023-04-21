package gui.outputpanel;

public class MessageParameters {

    private messageType mType;

    private textStyle mStyle;

    private String channelID;

    private String ServerAddress;

    private String ServerPort;

    public enum textStyle {

        regular, error, boldBlue, boldGreen, boldOrange, boldWave
    }

    public enum messageType {

        fromClient, fromServer, system, toClient, toServer, fromTargetServer, fromTargetServerToClient, toTargetServer
    }

    public MessageParameters(messageType mType, textStyle mStyle, String channelID) {
        this.mType = mType;
        this.mStyle = mStyle;
        this.channelID = channelID;
    }

    public MessageParameters(messageType mType, textStyle mStyle, String ServerAddress, String ServerPort) {
        this.mType = mType;
        this.mStyle = mStyle;
        this.ServerAddress = ServerAddress;
        this.ServerPort = ServerPort;
    }

    public MessageParameters(messageType mType, textStyle mStyle) {
        this.mType = mType;
        this.mStyle = mStyle;
    }

    public messageType getMsgType() {
        return mType;
    }

    public void setMsgType(messageType mType) {
        this.mType = mType;
    }

    public textStyle getMsgStyle() {
        return mStyle;
    }

    public void setMsgStyle(textStyle mStyle) {
        this.mStyle = mStyle;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getServerAddress() {
        return ServerAddress;
    }

    public void setServerAddress(String ServerAddress) {
        this.ServerAddress = ServerAddress;
    }

    public String getServerPort() {
        return ServerPort;
    }

    public void setServerPort(String ServerPort) {
        this.ServerPort = ServerPort;
    }
}
