package ru.cos.sim.road.init.factories;

import ru.cos.sim.road.init.data.LaneData;
import ru.cos.sim.road.link.Lane;

/**
 * 
 * @author zroslaw
 */
public class LaneFactory {

    public static Lane createLane(LaneData laneData) {
        Lane lane = new Lane(laneData.getIndex(), laneData.getLength());
        lane.setWidth(laneData.getWidth());
        return lane;
    }
}
