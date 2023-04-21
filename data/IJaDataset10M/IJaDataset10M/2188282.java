package playground.sergioo.Events;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.events.EventsManagerImpl;
import org.matsim.core.events.EventsReaderXMLv1;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.utils.geometry.CoordImpl;

public class MainDensityAnalysisWithPt {

    public static void main(String[] args) throws FileNotFoundException {
        boolean isOldEventFile = false;
        int binSizeInSeconds = 60;
        Coord center = new CoordImpl(4090, 5702);
        double radiusInMeters = 10000;
        String networkFile = "./data/youssef/siouxfalls_net1.xml";
        String eventsFile = "./data/youssef/SC/SC.50.events.xml.gz";
        Map<Id, Link> links = NetworkReadExample.getNetworkLinks(networkFile, center, radiusInMeters);
        InFlowInfoCollectorWithPt inflowHandler = new InFlowInfoCollectorWithPt(links, isOldEventFile, binSizeInSeconds);
        OutFlowInfoCollectorWithPt outflowHandler = new OutFlowInfoCollectorWithPt(links, isOldEventFile, binSizeInSeconds);
        inflowHandler.reset(0);
        outflowHandler.reset(0);
        EventsManagerImpl events = (EventsManagerImpl) EventsUtils.createEventsManager();
        events.addHandler(inflowHandler);
        events.addHandler(outflowHandler);
        EventsReaderXMLv1 reader = new EventsReaderXMLv1(events);
        reader.parse(eventsFile);
        HashMap<Id, int[]> linkInFlow = inflowHandler.getLinkInFlow();
        HashMap<Id, int[]> linkOutFlow = outflowHandler.getLinkOutFlow();
        HashMap<Id, int[]> deltaFlow = deltaFlow(linkInFlow, linkOutFlow);
        HashMap<Id, double[]> density = calculateDensity(deltaFlow, links);
        printDensityFile(density, links);
    }

    public static HashMap<Id, int[]> deltaFlow(HashMap<Id, int[]> linkInFlow, HashMap<Id, int[]> linkOutFlow) {
        HashMap<Id, int[]> result = new HashMap<Id, int[]>();
        for (Id linkId : linkInFlow.keySet()) {
            int[] inflowBins = linkInFlow.get(linkId);
            int[] outflowBins = linkOutFlow.get(linkId);
            int[] deltaflowBins = new int[inflowBins.length];
            result.put(linkId, deltaflowBins);
            for (int i = 0; i < inflowBins.length; i++) {
                if (deltaflowBins == null || inflowBins == null || outflowBins == null) {
                    System.out.println();
                } else deltaflowBins[i] = inflowBins[i] - outflowBins[i];
            }
            if (linkId.equals(new IdImpl(126323))) {
                System.out.println();
            }
        }
        return result;
    }

    public static HashMap<Id, double[]> calculateDensity(HashMap<Id, int[]> deltaFlow, Map<Id, Link> links) {
        HashMap<Id, double[]> density = new HashMap<Id, double[]>();
        for (Id linkId : deltaFlow.keySet()) {
            density.put(linkId, null);
        }
        double networkLength = 0;
        for (Id linkId : density.keySet()) {
            Link link = links.get(linkId);
            networkLength = networkLength + (link.getNumberOfLanes()) * (link.getLength());
        }
        for (Id linkId : density.keySet()) {
            int setAggregationLevel = 5;
            int[] deltaflowBins = deltaFlow.get(linkId);
            double[] densityBins = new double[deltaflowBins.length];
            double[] densityAggregation = new double[(deltaflowBins.length) / setAggregationLevel + 1];
            Link link = links.get(linkId);
            densityBins[0] = deltaflowBins[0];
            for (int i = 1; i < deltaflowBins.length; i++) {
                densityBins[i] = (densityBins[i - 1] + deltaflowBins[i]);
            }
            for (int i = 1; i < deltaflowBins.length; i++) {
                densityBins[i] = densityBins[i] * 1;
                densityBins[i] = densityBins[i] / link.getLength() / link.getNumberOfLanes() * 1000;
                double sumAggregation = 0;
                if (i % setAggregationLevel == 0) {
                    for (int j = 1; j < setAggregationLevel; j++) {
                        sumAggregation = sumAggregation + densityBins[i - setAggregationLevel + j];
                    }
                    densityAggregation[i / setAggregationLevel] = sumAggregation / setAggregationLevel;
                }
            }
            density.put(linkId, densityAggregation);
            deltaFlow.remove(linkId);
        }
        return density;
    }

    public static void printDensity(HashMap<Id, double[]> density, Map<Id, Link> links) {
        for (Id linkId : density.keySet()) {
            double[] bins = density.get(linkId);
            Link link = links.get(linkId);
            System.out.print(linkId + " - " + link.getCoord() + ": ");
            for (int i = 0; i < bins.length; i++) {
                System.out.print(bins[i] + "\t");
            }
            System.out.println();
        }
    }

    public static void printDensityFile(HashMap<Id, double[]> density, Map<Id, Link> links) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new File("./data/youssef/SC2.txt"));
        for (Id linkId : density.keySet()) {
            double[] bins = density.get(linkId);
            Link link = links.get(linkId);
            writer.print(linkId + " - " + link.getCoord() + ":\t");
            for (int i = 0; i < bins.length; i++) {
                writer.print(bins[i] + "\t");
            }
            writer.println();
        }
        writer.close();
    }

    public static void printBorderLinks(Map<Id, Link> borderLinks) {
        for (Id linkId : borderLinks.keySet()) {
            Link link = borderLinks.get(linkId);
            System.out.print("borderlink:" + linkId + " - " + link.getCoord() + ": ");
            System.out.println();
        }
    }
}
