package gov.sns.apps.slacs;

import gov.sns.xal.model.probe.traj.*;
import gov.sns.tools.beam.*;
import gov.sns.tools.plot.*;
import gov.sns.xal.smf.impl.*;

/**
 * This class to do a cosine fit of data and store results
 * Initial testing of this in late 2005 indicated it is not useful (BPM accuracy needs to be << 0.1 deg). This approach was abandoned, but the code is kept
 * incase it is used later.
 * @author  jdg
 */
public class CavPositionFinder {

    private AnalysisStuff analysisStuff;

    private double deg2rad = Math.PI / 180.;

    private double rad2deg = 180. / Math.PI;

    private BPM firstBPM, secondBPM;

    private TuneSet tuneSet;

    /** cosine fit data for BPM 1 measurements */
    private CosineFitData cosineFitData1;

    /** cosine fit data for BPM 2 measurements */
    private CosineFitData cosineFitData2;

    /** the measured cavity position offset from BPM1 data (mk) */
    protected double dLCavity1 = 0.;

    /** the measured cavity position offset from BPM1 data (mk) */
    protected double dLCavity2 = 0.;

    /** the error term that is the inconsistency of the position calc. from 1 BPM data compared to the other (m) */
    protected double errorTerm = 0.;

    /** local quantities that need to be passed */
    double dGap6_BPM1, dGap6_BPM2, tGap1, tGap6, tBPM1, tBPM2, WOut;

    /** Create a new empty object */
    public CavPositionFinder() {
    }

    /** Create a new ready to go  one*/
    public CavPositionFinder(AnalysisStuff as) {
        analysisStuff = as;
    }

    public void setCosineFitData(CosineFitData cfd1, CosineFitData cfd2) {
        cosineFitData1 = cfd1;
        cosineFitData2 = cfd2;
    }

    /** find the position of the cavity by running the model 2 times */
    public void findPositions() {
        double v1, v2, dtChangeBPM1Meas, dtChangeBPM2Meas;
        double freqBPM1, freqBPM2, tAccela, tAccelb;
        double tDriftNoBL1a, tDriftWBL1a, tDriftNoBL2a, tDriftWBL2a;
        double tDriftNoBL1b, tDriftWBL1b, tDriftNoBL2b, tDriftWBL2b;
        double tCorrectBL1 = 0.;
        double tCorrectBL2 = 0.;
        tuneSet = analysisStuff.controller.tuneSet;
        firstBPM = tuneSet.getFirstBPM();
        secondBPM = tuneSet.getSecondBPM();
        BasicGraphData bgd1 = analysisStuff.controller.activeCavityData.BPM1PhaseOn.getDataContainer(0);
        BasicGraphData bgd2 = analysisStuff.controller.activeCavityData.BPM2PhaseOn.getDataContainer(0);
        freqBPM1 = firstBPM.getBPMBucket().getFrequency() * 1.e6;
        freqBPM2 = secondBPM.getBPMBucket().getFrequency() * 1.e6;
        double phiMax1 = -cosineFitData1.getPhase() * rad2deg;
        double phiMax = phiMax1 - analysisStuff.cavPhaseOffset;
        double phiMin = phiMax + 180.;
        double phiMin1 = phiMax1 + 180.;
        if (phiMin > 180.) phiMin -= 360.;
        if (phiMin1 > 180.) phiMin1 -= 360.;
        analysisStuff.setDownstreamAmplitudes(false);
        runModel(phiMax);
        v1 = IConstants.LightSpeed * ParameterConverter.computeBetaFromEnergies(WOut, 939.301);
        tAccela = tGap6 - tGap1;
        if (analysisStuff.useBeamLoading) {
            tDriftNoBL1a = tBPM1 - tGap6;
            tDriftNoBL2a = tBPM2 - tGap6;
            analysisStuff.setDownstreamAmplitudes(true);
            runModel(phiMax);
            tDriftWBL1a = tBPM1 - tGap6;
            tDriftWBL2a = tBPM2 - tGap6;
            runModel(phiMin);
            tDriftWBL1b = tBPM1 - tGap6;
            tDriftWBL2b = tBPM2 - tGap6;
            analysisStuff.setDownstreamAmplitudes(false);
            runModel(phiMin);
            tDriftNoBL1b = tBPM1 - tGap6;
            tDriftNoBL2b = tBPM2 - tGap6;
            tCorrectBL1 = ((tDriftWBL1a - tDriftNoBL1a) - (tDriftWBL1b - tDriftNoBL1b));
            tCorrectBL2 = ((tDriftWBL2a - tDriftNoBL2a) - (tDriftWBL2b - tDriftNoBL2b));
        } else {
            tCorrectBL2 = 0.;
            tCorrectBL1 = 0.;
            runModel(phiMin);
        }
        v2 = IConstants.LightSpeed * ParameterConverter.computeBetaFromEnergies(WOut, 939.301);
        tAccelb = tGap6 - tGap1;
        double dPhi1 = (bgd1.getValueY(phiMax1) - bgd1.getValueY(phiMin1));
        if (dPhi1 < -180.) dPhi1 += 360.;
        dtChangeBPM1Meas = dPhi1 / (360. * freqBPM1);
        dLCavity1 = (dtChangeBPM1Meas - (tAccela - tAccelb) - tCorrectBL1) / (1. / v1 - 1. / v2);
        double phase2 = -cosineFitData1.getPhase() + cosineFitData2.getPhase();
        double dPhi2 = (bgd2.getValueY(phiMax1) - bgd2.getValueY(phiMin1));
        if (dPhi2 < -180) dPhi2 += 360.;
        dtChangeBPM2Meas = dPhi2 / (360. * freqBPM2);
        dLCavity2 = (dtChangeBPM2Meas - (tAccela - tAccelb) - tCorrectBL2) / (1. / v1 - 1. / v2);
        double BPM1PosCorrection = firstBPM.getBPMBucket().getOrientation() * firstBPM.getBPMBucket().getLength() / 2.;
        double BPM2PosCorrection = secondBPM.getBPMBucket().getOrientation() * secondBPM.getBPMBucket().getLength() / 2.;
        double dLCavity1Calc = dGap6_BPM1 + BPM1PosCorrection;
        double dLCavity2Calc = dGap6_BPM2 + BPM2PosCorrection;
        dLCavity1 = dLCavity1Calc - dLCavity1;
        dLCavity2 = dLCavity2Calc - dLCavity2;
        errorTerm = dLCavity1 - dLCavity2;
    }

    /** get the phase from the cosine  fit */
    public void runModel(double cavPhase) {
        Trajectory traj;
        RfGap gap1, gap6;
        ProbeState stateBPM1, stateBPM2, stateGap1, stateGap6;
        ProbeState[] states;
        TuneSet tuneSet = analysisStuff.controller.tuneSet;
        BPM firstBPM = tuneSet.getFirstBPM();
        BPM secondBPM = tuneSet.getSecondBPM();
        SCLCavity cav = tuneSet.getCavity();
        Object[] gaps = (cav.getGaps()).toArray();
        gap1 = (RfGap) gaps[0];
        gap6 = (RfGap) gaps[gaps.length - 1];
        boolean runOK = analysisStuff.runModel(analysisStuff.WIn, cavPhase, analysisStuff.cavityVoltage);
        if (runOK) {
            traj = analysisStuff.theModel.getProbe().getTrajectory();
        } else {
            analysisStuff.dumpErr("In cav pos finder, model evaluation failed at Phase = " + cavPhase);
            return;
        }
        stateBPM1 = traj.stateForElement(firstBPM.getId());
        stateBPM2 = traj.stateForElement(secondBPM.getId());
        stateGap1 = traj.stateForElement(gap1.getId());
        stateGap6 = traj.stateForElement(gap6.getId());
        WOut = stateBPM1.getKineticEnergy() / 1.e6;
        tGap1 = stateGap1.getTime();
        tGap6 = stateGap6.getTime();
        tBPM1 = stateBPM1.getTime();
        tBPM2 = stateBPM2.getTime();
        dGap6_BPM1 = stateBPM1.getPosition() - stateGap6.getPosition();
        dGap6_BPM2 = stateBPM2.getPosition() - stateGap6.getPosition();
    }
}
