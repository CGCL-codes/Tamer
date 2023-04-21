package examples.livecommander;

import protopeer.*;
import protopeer.network.*;
import protopeer.time.*;

public class CommandFlooder extends BasePeerlet {

    private boolean captain;

    private Timer initialDelayTimer;

    public CommandFlooder(boolean captain) {
        this.captain = captain;
    }

    private NeighborManager getNeighborManager() {
        return (NeighborManager) getPeer().getPeerletOfType(NeighborManager.class);
    }

    @Override
    public void init(Peer peer) {
        super.init(peer);
        if (captain) {
            initialDelayTimer = getPeer().getClock().createNewTimer();
            initialDelayTimer.addTimerListener(new TimerListener() {

                public void timerExpired(Timer timer) {
                    CommandMessage commandMessage = new CommandMessage();
                    commandMessage.command = "fire at will";
                    commandMessage.ttl = 4;
                    System.out.println("Captain gives the first command to " + getNeighborManager().getNumNeighbors() + " neighbors");
                    for (Finger neighbor : getNeighborManager().getNeighbors()) {
                        System.out.println("peer " + getPeer().getNetworkAddress() + " sent " + commandMessage + " to " + neighbor.getNetworkAddress());
                        getPeer().sendMessage(neighbor.getNetworkAddress(), commandMessage);
                    }
                }
            });
            initialDelayTimer.schedule(10e3);
        }
    }

    @Override
    public void handleIncomingMessage(Message message) {
        if (message instanceof CommandMessage) {
            System.out.println("peer " + getPeer().getNetworkAddress() + " received " + message);
            CommandMessage commandMessage = (CommandMessage) message;
            if (--commandMessage.ttl > 0) {
                for (Finger neighbor : getNeighborManager().getNeighbors()) {
                    getPeer().sendMessage(neighbor.getNetworkAddress(), commandMessage);
                    System.out.println("peer " + getPeer().getNetworkAddress() + " sent " + message + " to " + neighbor.getNetworkAddress());
                }
            } else {
                getPeer().getMeasurementLogger().log("ttl0_command", 1);
            }
        }
    }
}
