package nl.kbna.dioscuri.module.ata;

/**
 * Stores attributes of the ATPI.
 *
 */
public class Atpi {

    int command;

    int drqBytes;

    int totalBytesRemaining;

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public int getDrqBytes() {
        return drqBytes;
    }

    public void setDrqBytes(int drqBytes) {
        this.drqBytes = drqBytes;
    }

    public int getTotalBytesRemaining() {
        return totalBytesRemaining;
    }

    public void setTotalBytesRemaining(int totalBytesRemaining) {
        this.totalBytesRemaining = totalBytesRemaining;
    }
}
