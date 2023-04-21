package feup.aiad.msg;

import trasmapi.genAPI.mas.Message;
import feup.aiad.agents.macroscopic.globalsync.Direction;
import feup.aiad.agents.macroscopic.globalsync.Signal;

public class MessageAgentUpdate extends Message {

    public Direction current_direction;

    public int timeToReavaliation;

    public Signal signal;

    public double potential = 1;

    public MessageAgentUpdate(Direction my_direction, int my_window, Signal my_signal, double Potential) {
        current_direction = my_direction;
        timeToReavaliation = my_window;
        signal = my_signal;
        potential = Potential;
    }
}
