package ecskernel.communicate.messages;

import ecskernel.communicate.AMessage;
import ecskernel.wrapper.IECSKernelAgentWrapper;

/**
 * Used to tell agents which other agents are active within their
 * team.
 * 
 * @author Kate Macarthur
 */
public class TeamUpdateMessage extends AMessage {

    IECSKernelAgentWrapper[] team;

    /**
	 * Creates a new instance of a TeamUpdateMessage.
	 * 
	 * @param active all agents that are active
	 * @param timestamp the time at which the message was sent
	 */
    public TeamUpdateMessage(IECSKernelAgentWrapper[] active, int timestamp) {
        super(TYPE_TEAM_UPDATE);
        this.team = active;
        this.timestamp = timestamp;
    }

    /**
	 * Creates a new instance of a TeamUpdateMessage as this type 
	 * of message should not get corrupted.
	 * 
	 * @return the new Message
	 */
    public AMessage corrupt() {
        return new TeamUpdateMessage(team, timestamp);
    }

    /**
	 * @return the list of active agents held in this instance.
	 */
    public IECSKernelAgentWrapper[] getTeam() {
        return team;
    }

    /**
	 * Calculates the utility of the current message.
	 * 
	 * @return the calculated utility
	 */
    public int getUtility() {
        return 0;
    }
}
