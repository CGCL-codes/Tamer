package ssobjects;

public class TelnetMessage {

    public TelnetServerSocket sock;

    public String text;

    public TelnetMessage(TelnetServerSocket s, String t) {
        sock = s;
        text = t;
    }
}
