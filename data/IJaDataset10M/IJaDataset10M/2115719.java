package ngs.architecture.hybrid;

import ngs.architecture.centralised.*;
import ngs.*;

/**
 * Coolectes metrics about a node for hybrid centralised/distributed architectures.
 */
public abstract class HybridNode extends CentralisedNode {

    /**
	 * The overlay data downloaded in.
	 */
    private int overlayCapacityUsedIn[];

    /**
	 * The overlay data uploaded out.
	 */
    private int overlayCapacityUsedOut[];

    /**
	 * The game state data downloaded in.
	 */
    private int gameStateCapacityUsedIn[];

    /**
	 * The game state data uploaded out.
	 */
    private int gameStateCapacityUsedOut[];

    /**
	 * Default constructor, just initialises the id.
	 *
	 * @param simulationTime The length of time to be simulated.
	 * @param aggregate Should messages be aggregated together?
	 */
    public HybridNode(final double simulationTime, final boolean aggregate) {
        super(simulationTime, aggregate);
        overlayCapacityUsedIn = new int[(int) simulationTime];
        overlayCapacityUsedOut = new int[(int) simulationTime];
        gameStateCapacityUsedIn = new int[(int) simulationTime];
        gameStateCapacityUsedOut = new int[(int) simulationTime];
    }

    /**
	 * Get the maximum capacity used required inbound for the overlay.
	 *
	 * @return max in capacityUsed.
	 */
    public int getOverlayCapacityUsedInMax() {
        int max = 0;
        for (int i = 0; i < overlayCapacityUsedIn.length; i++) {
            if (overlayCapacityUsedIn[i] > max) {
                max = overlayCapacityUsedIn[i];
            }
        }
        return max;
    }

    /**
	 * Calcualte the average inbound capacity used for the overlay.
	 *
	 * @return The average inbound capacity used.
	 */
    public double getOverlayCapacityUsedInAvg() {
        int total = 0;
        for (int i = 0; i < overlayCapacityUsedIn.length; i++) {
            total += overlayCapacityUsedIn[i];
        }
        return ((double) total) / overlayCapacityUsedIn.length;
    }

    /**
	 * Get the maximum capacity used required outbound for the overlay.
	 *
	 * @return max out capacity used.
	 */
    public int getOverlayCapacityUsedOutMax() {
        int max = 0;
        for (int i = 0; i < overlayCapacityUsedOut.length; i++) {
            if (overlayCapacityUsedOut[i] > max) {
                max = overlayCapacityUsedOut[i];
            }
        }
        return max;
    }

    /**
	 * Calcualte the average outbound capacity used for the overlay.
	 *
	 * @return The average outbound capacity used.
	 */
    public double getOverlayCapacityUsedOutAvg() {
        int total = 0;
        for (int i = 0; i < overlayCapacityUsedOut.length; i++) {
            total += overlayCapacityUsedOut[i];
        }
        return ((double) total) / overlayCapacityUsedOut.length;
    }

    /**
	 * Get the maximum capacity used required inbound for the gameState.
	 *
	 * @return max in capacityUsed.
	 */
    public int getGameStateCapacityUsedInMax() {
        int max = 0;
        for (int i = 0; i < gameStateCapacityUsedIn.length; i++) {
            if (gameStateCapacityUsedIn[i] > max) {
                max = gameStateCapacityUsedIn[i];
            }
        }
        return max;
    }

    /**
	 * Calcualte the average inbound capacity used for the gameState.
	 *
	 * @return The average inbound capacity used.
	 */
    public double getGameStateCapacityUsedInAvg() {
        int total = 0;
        for (int i = 0; i < gameStateCapacityUsedIn.length; i++) {
            total += gameStateCapacityUsedIn[i];
        }
        return ((double) total) / gameStateCapacityUsedIn.length;
    }

    /**
	 * Get the maximum capacity used required outbound for the gameState.
	 *
	 * @return max out capacity used.
	 */
    public int getGameStateCapacityUsedOutMax() {
        int max = 0;
        for (int i = 0; i < gameStateCapacityUsedOut.length; i++) {
            if (gameStateCapacityUsedOut[i] > max) {
                max = gameStateCapacityUsedOut[i];
            }
        }
        return max;
    }

    /**
	 * Calcualte the average outbound capacity used for the gameState.
	 *
	 * @return The average outbound capacity used.
	 */
    public double getGameStateCapacityUsedOutAvg() {
        int total = 0;
        for (int i = 0; i < gameStateCapacityUsedOut.length; i++) {
            total += gameStateCapacityUsedOut[i];
        }
        return ((double) total) / gameStateCapacityUsedOut.length;
    }

    /**
	 * Receive a message.
	 *
	 * @param message The message received.
	 */
    public synchronized void receive(final Message message) {
        super.receive(message);
        if (message instanceof GameStateMessage) {
            gameStateCapacityUsedIn[(int) (message.getTime() + 0.0005)] += message.getSize();
            ((HybridNode) message.getSender()).gameStateBytesSent(message.getSize(), message.getTime());
        } else {
            overlayCapacityUsedIn[(int) (message.getTime() + 0.0005)] += message.getSize();
            ((HybridNode) message.getSender()).overlayBytesSent(message.getSize(), message.getTime());
        }
    }

    /**
	 * Game State bytes sent by this node.
	 *
	 * @param bytes How many bytes were sent.
	 * @param time The time they were sent at.
	 */
    public synchronized void gameStateBytesSent(final int bytes, final double time) {
        gameStateCapacityUsedOut[(int) (time + 0.0005)] += bytes;
    }

    /**
	 * Overlay bytes sent by this node.
	 *
	 * @param bytes How many bytes were sent.
	 * @param time The time they were sent at.
	 */
    public synchronized void overlayBytesSent(final int bytes, final double time) {
        overlayCapacityUsedOut[(int) (time + 0.0005)] += bytes;
    }
}
