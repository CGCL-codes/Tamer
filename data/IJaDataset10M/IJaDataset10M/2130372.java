package common;

public class MessageBecomeSuperPeer extends Message {

    private static final long serialVersionUID = 4074249394677875635L;

    public Zone zone;

    public Position yourPosition;

    public int worldWidth;

    public int worldHeight;

    public MessageBecomeSuperPeer() {
        type = Type.BecomeSuperPeer;
    }
}
