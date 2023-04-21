package chatserver.model.channel;

import chatserver.model.ChannelType;
import chatserver.model.Race;

public abstract class RaceChannel extends Channel {

    protected Race race;

    public RaceChannel(ChannelType channelType, Race race) {
        super(channelType);
        this.race = race;
    }

    /**
	* @return the race
	*/
    public Race getRace() {
        return race;
    }
}
