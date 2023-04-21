package ru.cos.sim.visualizer.traffic.graphs;

import ru.cos.sim.communication.dto.MeterDTO;
import ru.cos.sim.meters.impl.data.TrafficVolume;
import ru.cos.sim.visualizer.traffic.graphs.classes.Line;
import ru.cos.sim.visualizer.traffic.graphs.classes.VolumeMeterData;
import ru.cos.sim.visualizer.traffic.graphs.converter.TrafficVolumeConverter;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.List;

public class TrafficVolumeMeterGraph extends AbstractGraph<Line, VolumeMeterData, TrafficVolume> implements IGraph<VolumeMeterData, TrafficVolume> {

    private static String thisGraphName = "Traffic Volume Meter";

    private static String thisAxisXName = "Time [s]";

    private static String thisAxisYName = "Traffic volume [pcu]";

    /**
     * Gets instance of traffic volume meter graph
     * @param fileName - filename with statistics data
     * @return graph
     * @throws java.io.FileNotFoundException - if simulation statistics not found
     * @throws javax.xml.bind.JAXBException - if sumulation statistics xml not valid
     */
    public static TrafficVolumeMeterGraph getInstance(String fileName) throws JAXBException, FileNotFoundException {
        TrafficVolumeMeterGraph graph = new TrafficVolumeMeterGraph(12345);
        graph.createHistoryGraph(parseGraph(fileName).getVolumeMeter());
        return graph;
    }

    /**
     * Gets instance of cumulative volume graph
     * @param id
     * @return graph
     */
    public static TrafficVolumeMeterGraph getInstance(long id) {
        TrafficVolumeMeterGraph graph = new TrafficVolumeMeterGraph(id);
        graph.createEmptyGraph();
        return graph;
    }

    public static TrafficVolumeMeterGraph getInstance(MeterDTO<TrafficVolume> dto, long id) {
        TrafficVolumeMeterGraph graph = new TrafficVolumeMeterGraph(id);
        VolumeMeterData volumeMeterData = graph.converter.convertHistory(dto);
        graph.createHistoryGraph(volumeMeterData);
        return graph;
    }

    private TrafficVolumeMeterGraph(long id) {
        super(id, thisGraphName, thisAxisXName, thisAxisYName);
        converter = new TrafficVolumeConverter();
    }

    @Override
    protected Line getPointCollection(VolumeMeterData graphData) {
        return graphData.getLines().get(0);
    }

    @Override
    protected List<Line> getPointCollections(VolumeMeterData graphData) {
        return graphData.getLines();
    }

    @Override
    protected void createHistoryGraph(VolumeMeterData graphData) {
        chart.createSinglelineChart(getPointCollection(graphData));
    }

    @Override
    protected void createScheduledHistoryGraph(VolumeMeterData graphData) {
        chart.createMultilineChart(getPointCollections(graphData));
    }

    @Override
    public VolumeMeterData refreshInstant(MeterDTO<TrafficVolume> trafficVolumeMeterDTO) {
        return null;
    }
}
