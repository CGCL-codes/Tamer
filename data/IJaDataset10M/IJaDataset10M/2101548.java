package ru.cos.sim.ras.duo.algo.weighting;

import java.util.LinkedList;
import java.util.List;
import ru.cos.sim.agents.tlns.TrafficLightSignal;
import ru.cos.sim.agents.tlns.data.RegularTLNData;
import ru.cos.sim.agents.tlns.data.TimePeriodData;
import ru.cos.sim.ras.duo.PathExtensions;
import ru.cos.sim.ras.duo.Weighter;
import ru.cos.sim.ras.duo.digraph.roadnet.NodePartVertex;
import ru.cos.sim.ras.duo.digraph.roadnet.TransitionEdge;
import ru.cos.sim.ras.duo.helper.RegularTrafficLightsCalculator;
import ru.cos.sim.services.ServiceLocator;

public class EnlightedTransitionWeighter implements Weighter {

    public EnlightedTransitionWeighter(TransitionEdge transition, TravelTimeProvider travelTimeProvider) {
        this.transition = transition;
        this.travelTimeProvider = travelTimeProvider;
        if (transition.getLightsNetworkData() instanceof RegularTLNData) initialize((RegularTLNData) transition.getLightsNetworkData());
    }

    public static interface TravelTimeProvider {

        /**
		 * Should return travel time from current link to specified vertex of transition 
		 * or -1 to skip processing for that transition (average await time will be assumed)   
		 */
        public float getTravelTimeTo(NodePartVertex v, PathExtensions extensions);
    }

    private void initialize(RegularTLNData regularLights) {
        this.timeShift = regularLights.getScheduleTimeShift();
        this.phases = new LinkedList<PhaseInfo>();
        this.cycleLength = 0;
        for (TimePeriodData period : regularLights.getScheduleTable().getTimePeriods()) {
            float duration = period.getDuration();
            boolean isGreen = period.getTrafficLightSignals().get(transition.getLightsId()) == TrafficLightSignal.Green;
            this.phases.add(new PhaseInfo(duration, isGreen));
            this.cycleLength += duration;
            if (!isGreen) {
                this.redTimeLength += duration;
                this.redTimeCount++;
            }
        }
        this.phaseRatio = RegularTrafficLightsCalculator.getPhaseRatio(regularLights, transition.getLightsId());
    }

    private boolean isInitialized() {
        return phases != null;
    }

    private TransitionEdge transition;

    private TravelTimeProvider travelTimeProvider;

    private float timeShift = 0;

    private float cycleLength = 0;

    private float redTimeLength = 0;

    private float redTimeCount = 0;

    private float phaseRatio = 1;

    private List<PhaseInfo> phases;

    protected float getCurrentTime() {
        return ServiceLocator.getInstance().getTimeSerivce().getAbsoluteTime();
    }

    private static class PhaseInfo {

        public PhaseInfo(float duration, boolean isGreen) {
            this.duration = duration;
            this.isGreen = isGreen;
        }

        private float duration;

        public float getDuration() {
            return duration;
        }

        private boolean isGreen;

        public boolean isGreen() {
            return isGreen;
        }
    }

    private float getAverageAwaitTime() {
        return redTimeCount != 0 ? redTimeLength / redTimeCount / 2 * phaseRatio / (phaseRatio + 1) : 0;
    }

    private float getAwaitTimeAt(float time) {
        if (phases.size() == 0) return 0;
        if (redTimeCount == phases.size()) return Float.POSITIVE_INFINITY;
        time += cycleLength - timeShift;
        int i = 0;
        while ((time -= phases.get(i).getDuration()) > 0) {
            i = (i + 1) % phases.size();
        }
        if (phases.get(i).isGreen()) {
            return 0;
        } else {
            PhaseInfo phase;
            while (!(phase = phases.get(i = (i + 1) % phases.size())).isGreen()) time -= phase.getDuration();
            return -time;
        }
    }

    @Override
    public float getWeight(PathExtensions extensions) {
        if (!isInitialized()) return 0;
        float travelTime = travelTimeProvider.getTravelTimeTo(transition.getIncomingVertex(), extensions);
        return travelTime > 0 ? getAwaitTimeAt(getCurrentTime() + travelTime) : getAverageAwaitTime();
    }

    @Override
    public String toString() {
        return "Weight: uncertain";
    }
}
