package abricots.command;

/**
 *
 * @author charly
 */
public class PlayerCommand implements Command {

    public static final int CMD_UP = 0;

    public static final int CMD_LEFT = 1;

    public static final int CMD_RIGHT = 2;

    public static final int CMD_DOWN = 3;

    public static final int CMD_FIRE = 4;

    protected long gameTime;

    protected int commandId;

    protected boolean started;

    public PlayerCommand() {
    }

    public PlayerCommand(long gameTime, int commandId, boolean started) {
        this.gameTime = gameTime;
        this.commandId = commandId;
        this.started = started;
    }

    public int getCommandId() {
        return commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }

    public long getGameTime() {
        return gameTime;
    }

    @Override
    public boolean getStarted() {
        return started;
    }

    public void setGameTime(long gameTime) {
        this.gameTime = gameTime;
    }

    @Override
    public String toString() {
        return "BasicEntityCommand{" + "gameTime=" + gameTime + ", commandId=" + commandId + ", started=" + started + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlayerCommand other = (PlayerCommand) obj;
        if (this.gameTime != other.gameTime) {
            return false;
        }
        if (this.commandId != other.commandId) {
            return false;
        }
        if (this.started != other.started) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (this.gameTime ^ (this.gameTime >>> 32));
        hash = 53 * hash + this.commandId;
        hash = 53 * hash + (this.started ? 1 : 0);
        return hash;
    }
}
