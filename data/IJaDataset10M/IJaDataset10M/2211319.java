package chatserver.model.channel;

import chatserver.model.ChannelType;
import chatserver.model.Race;

public class RegionChannel extends RaceChannel {

    protected int mapId;

    /**
	* 
	* @param channelId
	* @param mapId
	* @param race
	*/
    public RegionChannel(int mapId, Race race) {
        super(ChannelType.PUBLIC, race);
        this.mapId = mapId;
    }

    /**
	* @return the mapId
	*/
    public int getMapId() {
        return mapId;
    }
}
