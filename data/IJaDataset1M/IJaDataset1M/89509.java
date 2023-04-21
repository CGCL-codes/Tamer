package games.virtualworldgame;

public class UnknownLocationException extends Exception {

    private static final long serialVersionUID = 5566578030196687573L;

    public UnknownLocationException(VirtualWorldGamePlayer player) {
        super("Location of player " + player.getNick() + " unknown");
    }

    public UnknownLocationException(String location) {
        super("Location " + location + " does not exist");
    }
}
