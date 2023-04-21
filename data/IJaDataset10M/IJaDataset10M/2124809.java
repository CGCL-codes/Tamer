package net.firefly.client.player;

public class PlayerStatus {

    protected String statusName;

    protected int status;

    public static final PlayerStatus STATUS_STOPPED = new PlayerStatus(0, "STOPPED");

    public static final PlayerStatus STATUS_PLAYING = new PlayerStatus(1, "PLAYING");

    public static final PlayerStatus STATUS_CONNECTING = new PlayerStatus(2, "CONNECTING");

    public static final PlayerStatus STATUS_READING_INFO = new PlayerStatus(3, "READING_INFO");

    public static final PlayerStatus STATUS_SEEKING = new PlayerStatus(4, "SEEKING");

    public static final PlayerStatus STATUS_PAUSED = new PlayerStatus(0, "PAUSED");

    protected PlayerStatus(int status, String statusName) {
        this.status = status;
        this.statusName = statusName;
    }

    public boolean equals(Object o) {
        try {
            PlayerStatus ps = (PlayerStatus) o;
            return this.status == ps.getStatus();
        } catch (ClassCastException e) {
            return false;
        }
    }

    public String toString() {
        return statusName;
    }

    /**
	 * @return Returns the status.
	 */
    public int getStatus() {
        return status;
    }

    /**
	 * @return Returns the statusName.
	 */
    public String getStatusName() {
        return statusName;
    }
}
