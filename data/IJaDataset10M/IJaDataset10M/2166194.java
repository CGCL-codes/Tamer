package playground.ou.singleintersectioncontrol;

import java.util.Iterator;
import java.util.Map;
import org.matsim.core.api.network.Link;
import org.matsim.core.network.NetworkLayer;
import org.matsim.core.utils.misc.Time;
import playground.ou.signalcontrolnetwork.ControledLink;
import playground.ou.signalcontrolnetwork.StandardIntersection;
import playground.ou.signaltimingstragety.FixCycle;
import playground.ou.signaltimingstragety.HCM;
import playground.ou.signaltimingstragety.Webster;
import playground.ou.volcreation.DirectionalVolume;
import playground.ou.volcreation.SlotVolume;

public class TwoLinkControl {

    static final int MINVOLUME = 20;

    static final int YELLOWTIME = 3;

    static final int ALLREDTIME = 0;

    static final int CMAX = 120;

    int CMIN;

    int cycleLength, phaseGreen[], phaseAactualGreen, yellow, allRed;

    int time, offset = 0;

    ControledLink linkOne, linkTwo;

    public TwoLinkControl(NetworkLayer network, StandardIntersection intersection) {
        Iterator<? extends Link> l_it = network.getNodes().get(intersection.getId()).getInLinks().values().iterator();
        while (l_it.hasNext()) {
            Link l = l_it.next();
            if (l.getFromNode().getId().toString().equalsIgnoreCase(intersection.getConnectedNodes().get(0).getId().toString())) {
                this.linkOne = (new ControledLink(network, l));
            }
            if (l.getFromNode().getId().toString().equalsIgnoreCase(intersection.getConnectedNodes().get(1).getId().toString())) {
                this.linkTwo = (new ControledLink(network, l));
            }
        }
        this.time = intersection.gettimeDatum_CycleStart();
        this.cycleLength = intersection.getCycleLength();
        this.offset = intersection.getCycleOffset();
        phaseGreen = new int[2];
        this.phaseGreen[1] = calcPhaseBGreen();
        this.CMIN = calcPhaseBGreen() + 2 * (YELLOWTIME + ALLREDTIME);
    }

    private int calcPhaseBGreen() {
        int maxLane = 1;
        if (linkOne != null) {
            if (maxLane < linkOne.getNumberOfLanes(0)) maxLane = (int) linkOne.getNumberOfLanes(0);
        }
        if (linkTwo != null) {
            if (maxLane < linkTwo.getNumberOfLanes(0)) maxLane = (int) linkTwo.getNumberOfLanes(0);
        }
        return 4 + maxLane * 3;
    }

    public void setTimeParameter(int strategy) {
        calc_Cycle(strategy);
        greenSplit();
    }

    public void calc_Cycle(int strategy) {
        int phaseNum = 2;
        switch(strategy) {
            case 0:
                setIntervalParameter(YELLOWTIME, ALLREDTIME);
                this.cycleLength = new FixCycle(this.CMIN, CMAX).calc_cyclelength((int) (time / 3600));
                break;
            case 1:
                this.cycleLength = new FixCycle(this.CMIN, CMAX).calc_cyclelength((int) (time / 3600));
                break;
            case 2:
                this.cycleLength = new Webster(this.CMIN, CMAX).calc_cyclelength(phaseNum, this.getPhaseCriticalFlow());
                break;
            case 3:
                this.cycleLength = new HCM(this.CMIN, CMAX).calc_cyclelength(phaseNum, this.getPhaseCriticalFlow());
                break;
            default:
                System.out.println("Error Strategy ");
                break;
        }
    }

    public void greenSplit() {
        if (this.calCriticalPhaseAVolume() < MINVOLUME) {
            phaseGreen[0] = this.cycleLength;
            phaseAactualGreen = phaseGreen[0];
        } else {
            phaseGreen[0] = this.cycleLength - phaseGreen[1];
            phaseAactualGreen = phaseGreen[0];
        }
    }

    private int[] getPhaseCriticalFlow() {
        int volume[];
        volume = new int[2];
        volume[0] = this.calCriticalPhaseAVolume();
        return volume;
    }

    public void setIntervalParameter(int yellow, int allred) {
        this.yellow = yellow;
        this.allRed = allred;
    }

    public int getCycleLength() {
        return this.cycleLength;
    }

    public int getPhaseAactualGreentime() {
        return this.phaseAactualGreen;
    }

    public int getClearInterval() {
        return YELLOWTIME + ALLREDTIME;
    }

    public ControledLink getLinkOne() {
        return this.linkOne;
    }

    public ControledLink getLinkTwo() {
        return this.linkTwo;
    }

    public void setCycleLength(int length) {
        this.cycleLength = length;
    }

    private int calCriticalPhaseAVolume() {
        int temp1, temp2;
        if (linkOne == null) {
            temp1 = 0;
        } else temp1 = linkOne.getLinkMovement().getGoThroughVolume() + linkOne.getLinkMovement().getTurnRightVolume() + linkOne.getLinkMovement().getTurnLeftVolume();
        if (linkTwo == null) {
            temp2 = 0;
        } else temp2 = linkTwo.getLinkMovement().getGoThroughVolume() + linkTwo.getLinkMovement().getTurnRightVolume() + linkTwo.getLinkMovement().getTurnLeftVolume();
        ;
        return Math.max(temp1, temp2);
    }

    public void setPhaseAactualGreen(int phaseAgreen) {
        this.phaseAactualGreen = phaseAgreen;
    }

    public int[] getPhaseASchedule() {
        return new TwoPhaseSchedule(cycleLength, offset, phaseAactualGreen).getPhaseASchedule();
    }

    public int[] getPhaseBSchedule() {
        return new TwoPhaseSchedule(cycleLength, offset, phaseAactualGreen).getPhaseBSchedule();
    }

    public void setLinkVolume(int timeId, Map<String, SlotVolume> hourlyVolume) {
        linkOne.setLinkVolume(linkOne.getNumberOfLanes(Time.UNDEFINED_TIME), getOneLinkVolume(linkOne.getId().toString(), timeId, hourlyVolume));
        linkTwo.setLinkVolume(linkOne.getNumberOfLanes(Time.UNDEFINED_TIME), getOneLinkVolume(linkTwo.getId().toString(), timeId, hourlyVolume));
    }

    private DirectionalVolume getOneLinkVolume(String linkId, int timeId, Map<String, SlotVolume> hourlyVolume) {
        DirectionalVolume linkvolume = new DirectionalVolume();
        if (linkId == null) {
            linkvolume.setZeroVol();
        } else if (hourlyVolume.get(linkId) == null) {
            linkvolume.setZeroVol();
        } else if (hourlyVolume.get(linkId).getSlotVolume().get(timeId) != null) {
            linkvolume = hourlyVolume.get(linkId).getSlotVolume().get(timeId);
        }
        return linkvolume;
    }
}
