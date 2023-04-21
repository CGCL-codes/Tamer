package playground.scnadine.drawODsForChoiceSetGeneration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.population.routes.LinkNetworkRouteImpl;
import org.matsim.core.utils.misc.NetworkUtils;

public class ChoiceSetStages {

    private ArrayList<ChoiceSetStage> choiceSetStages;

    private Network network;

    private double mWDist33Percentile;

    private double mWDist67Percentile;

    private double eUDist33Percentile;

    private double eUDist67Percentile;

    private double uMDist33Percentile;

    private double uMDist67Percentile;

    private double lRDist33Percentile;

    private double lRDist67Percentile;

    private double otherDist33Percentile;

    private double otherDist67Percentile;

    private HashMap<Integer, Integer> segmentationClassFrequencies;

    public ChoiceSetStages(Network network) {
        this.choiceSetStages = new ArrayList<ChoiceSetStage>();
        this.network = network;
        this.segmentationClassFrequencies = new HashMap<Integer, Integer>();
    }

    public void addChoiceSetStage(ChoiceSetStage choiceSetStage) {
        this.choiceSetStages.add(choiceSetStage);
    }

    public void clearChoiceSetStages() {
        this.choiceSetStages.clear();
    }

    public ChoiceSetStage createChoiceSetStage(Id stageKey, Id personId, Id tripId, Id stageId, GregorianCalendar startTime, Node startNode, Node endNode, LinkNetworkRouteImpl chosenRoute) {
        ChoiceSetStage stage = new ChoiceSetStage(stageKey, personId, tripId, stageId, startTime, startNode, endNode, chosenRoute);
        return stage;
    }

    public void createChoiceSetStages(File inputFile, boolean filterStages, ArrayList<String[]> stagesToBeFiltered) {
        long stageKey = 0;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
            in.readLine();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] entries = inputLine.split("\t");
                if (containsStage(stagesToBeFiltered, entries[0], entries[1], entries[2])) {
                }
                if (!(filterStages && containsStage(stagesToBeFiltered, entries[0], entries[1], entries[2]))) {
                    Id personId = new IdImpl(entries[0]);
                    Id tripId = new IdImpl(entries[1]);
                    Id stageId = new IdImpl(entries[2]);
                    GregorianCalendar timestamp = new GregorianCalendar();
                    timestamp.setTimeInMillis(Long.parseLong(entries[3]));
                    String startNodeID = entries[4];
                    String endNodeID = entries[5];
                    List<Link> linksOfChosenRoute = new LinkedList<Link>();
                    int i = 6;
                    while (!entries[i].equals("-1")) {
                        linksOfChosenRoute.add(this.network.getLinks().get(new IdImpl(entries[i])));
                        i++;
                    }
                    if (!linksOfChosenRoute.isEmpty()) {
                        LinkNetworkRouteImpl chosenRoute = new LinkNetworkRouteImpl(linksOfChosenRoute.get(0).getId(), linksOfChosenRoute.get(linksOfChosenRoute.size() - 1).getId());
                        chosenRoute.setLinkIds(linksOfChosenRoute.get(0).getId(), NetworkUtils.getLinkIds(linksOfChosenRoute), linksOfChosenRoute.get(linksOfChosenRoute.size() - 1).getId());
                        ChoiceSetStage currentStage = createChoiceSetStage(new IdImpl(stageKey), personId, tripId, stageId, timestamp, this.network.getNodes().get(new IdImpl(startNodeID)), this.network.getNodes().get(new IdImpl(endNodeID)), chosenRoute);
                        addChoiceSetStage(currentStage);
                        stageKey++;
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            System.out.println("Error while reading od pairs.");
            e.printStackTrace();
        }
    }

    public ArrayList<ChoiceSetStage> getAllChoiceSetStageWithPersonId(Id personID) {
        ArrayList<ChoiceSetStage> allStagesOfPerson = new ArrayList<ChoiceSetStage>();
        for (ChoiceSetStage currentStage : this.choiceSetStages) {
            if (currentStage.getPersonId().equals(personID)) {
                allStagesOfPerson.add(currentStage);
            }
        }
        return allStagesOfPerson;
    }

    public ChoiceSetStage getChoiceSetStageByPersonId(Id personID) {
        ChoiceSetStage choiceSetStage = null;
        for (ChoiceSetStage currentStage : this.choiceSetStages) {
            if (currentStage.getPersonId().equals(personID)) {
                choiceSetStage = currentStage;
                break;
            }
        }
        return choiceSetStage;
    }

    public ChoiceSetStage getChoiceSetStageByStageKey(Id stageKey) {
        ChoiceSetStage choiceSetStage = null;
        for (ChoiceSetStage currentStage : this.choiceSetStages) {
            if (currentStage.getStageKey().equals(stageKey)) {
                choiceSetStage = currentStage;
                break;
            }
        }
        return choiceSetStage;
    }

    public ArrayList<ChoiceSetStage> getChoiceSetStages() {
        return this.choiceSetStages;
    }

    public double getEUDist33Percentile() {
        return eUDist33Percentile;
    }

    public double getEUDist67Percentile() {
        return eUDist67Percentile;
    }

    public double getLRDist33Percentile() {
        return lRDist33Percentile;
    }

    public double getLRDist67Percentile() {
        return lRDist67Percentile;
    }

    public double getMWDist33Percentile() {
        return mWDist33Percentile;
    }

    public double getMWDist67Percentile() {
        return mWDist67Percentile;
    }

    public double getOtherDist33Percentile() {
        return otherDist33Percentile;
    }

    public double getOtherDist67Percentile() {
        return otherDist67Percentile;
    }

    public HashMap<Integer, Integer> getSegmentationClassFrequencies() {
        return segmentationClassFrequencies;
    }

    public double getUMDist33Percentile() {
        return uMDist33Percentile;
    }

    public double getUMDist67Percentile() {
        return uMDist67Percentile;
    }

    public void removeChoiceSetStage(int stageKey) {
        this.choiceSetStages.remove(stageKey);
    }

    public void removeChoiceSetStage(ChoiceSetStage choiceSetStage) {
        this.choiceSetStages.remove(choiceSetStage);
    }

    public void setEUDist33Percentile(double eUDist33Percentile) {
        this.eUDist33Percentile = eUDist33Percentile;
    }

    public void setEUDist67Percentile(double eUDist67Percentile) {
        this.eUDist67Percentile = eUDist67Percentile;
    }

    public void setLRDist33Percentile(double lRDist33Percentile) {
        this.lRDist33Percentile = lRDist33Percentile;
    }

    public void setLRDist67Percentile(double lRDist67Percentile) {
        this.lRDist67Percentile = lRDist67Percentile;
    }

    public void setMWDist33Percentile(double mWDist33Percentile) {
        this.mWDist33Percentile = mWDist33Percentile;
    }

    public void setMWDist67Percentile(double mWDist67Percentile) {
        this.mWDist67Percentile = mWDist67Percentile;
    }

    public void setOtherDist33Percentile(double otDist33Percentile) {
        this.otherDist33Percentile = otDist33Percentile;
    }

    public void setOtherDist67Percentile(double otDist67Percentile) {
        this.otherDist67Percentile = otDist67Percentile;
    }

    public void setUMDist33Percentile(double uMDist33Percentile) {
        this.uMDist33Percentile = uMDist33Percentile;
    }

    public void setUMDist67Percentile(double uMDist67Percentile) {
        this.uMDist67Percentile = uMDist67Percentile;
    }

    private boolean containsStage(ArrayList<String[]> stagesToBeFiltered, String personId, String tripId, String stageId) {
        boolean containsStage = false;
        for (String[] ids : stagesToBeFiltered) {
            if (ids[0].equals(personId) && ids[1].equals(tripId) && ids[2].equals(stageId)) {
                containsStage = true;
                break;
            }
        }
        return containsStage;
    }
}
