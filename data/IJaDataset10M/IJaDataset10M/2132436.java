package chatter;

public interface Chatter extends Observable {

    public String getName();

    public String getLastMessage();

    public void sendMessage(String message);
}
