package ru.cos.sim.communication.dto;

import java.util.HashSet;
import java.util.Set;

/**
 * Frame data it is all data that are needed for Visualizer Engine to render screen
 * and visualize current simulation process state.<br>
 * FraneData is just a set of data transfer objects {@linkplain AbstractDTO} that describes
 * all objects that are now visible on the screen.
 * @author zroslaw
 */
public class FrameData {

    /**
	 * List of DTOs
	 */
    private Set<AbstractDTO> abstractDTOs = new HashSet<AbstractDTO>();

    /**
	 * Statistics of simulation process for the moment of time on which the frame data was gathered.
	 */
    private StatisticsData statisticsData;

    public Set<AbstractDTO> getDataTransferObjects() {
        return abstractDTOs;
    }

    public void setDataTransferObjects(Set<AbstractDTO> abstractDTOs) {
        this.abstractDTOs = abstractDTOs;
    }

    public StatisticsData getStatisticsData() {
        return statisticsData;
    }

    public void setStatisticsData(StatisticsData statisticsData) {
        this.statisticsData = statisticsData;
    }
}
