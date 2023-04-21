package chat;

public interface Chatter {

    void receiveAMessage(String msg, Chatter c);

    String getAlias();
}
