package dsrwebserver.pages.dsr.playbackapplet;

import dsr.data.UnitData;
import dsrwebserver.maps.ServerMapSquare;

public class PlayBackGameData {

    public int max_turns, max_events, total_sides, map_width;

    public UnitData units[];

    public ServerMapSquare map[][];

    public PlayBackGameData() {
        super();
    }

    public int getMapWidth() {
        return map.length;
    }

    public int getMapHeight() {
        return map[0].length;
    }
}
