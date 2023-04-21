package org.stumeikle.NeuroCoSA;

import java.util.*;
import org.stumeikle.NeuroCoSA.NIS.*;

/** Bi-Mode Layer class. 
 *  As previously. Bimode layer is a layer which contains bimodeneurons - that is, 
 *  neurons which have learn and drive states. 
 */
public class BiModeLayer extends Layer {

    InfoWithSingleStore iWinningDriveNeuron;

    InfoDoubleWithSingleStore iWinningDriveSignalStrength;

    InfoWithSingleStore iWinningLearnNeuron;

    InfoDoubleWithSingleStore iWinningLearnSignalStrength;

    InfoWithMultiStore iAllLearningNeurons;

    BiModeNeuron iTmpWinningDriveNeuron;

    BiModeNeuron iTmpWinningLearnNeuron;

    double iTmpWinningDriveStrength;

    double iTmpWinningLearnStrength;

    LinkedList<Neuron> iLearningNeurons;

    public BiModeLayer(Cortex l) {
        super(l);
        iWinningDriveNeuron = new InfoWithSingleStore("WinningDrive", this);
        iWinningLearnNeuron = new InfoWithSingleStore("WinningLearn", this);
        iWinningDriveSignalStrength = new InfoDoubleWithSingleStore("WinningDriveSigStr", 0.0, this);
        iWinningLearnSignalStrength = new InfoDoubleWithSingleStore("WinningLearnSigStr", 0.0, this);
        iAllLearningNeurons = new InfoWithMultiStore("AllLearningNeurons", this);
        getInfoService().addPublication(iWinningDriveNeuron);
        getInfoService().addPublication(iWinningLearnNeuron);
        getInfoService().addPublication(iWinningDriveSignalStrength);
        getInfoService().addPublication(iWinningLearnSignalStrength);
        getInfoService().addPublication(iAllLearningNeurons);
    }

    public void getReady() {
    }

    protected void resetVars() {
        iTmpWinningLearnStrength = 0.0;
        iTmpWinningDriveStrength = 0.0;
        iTmpWinningLearnNeuron = null;
        iTmpWinningDriveNeuron = null;
        iLearningNeurons = new LinkedList<Neuron>();
    }

    protected void updateVars(BiModeNeuron bm) {
        boolean state_active = false;
        if (bm.getState() == Neuron.state_excited || bm.getState() == Neuron.state_inhibited) state_active = true;
        if (state_active && bm.getLearnSigStr() > iTmpWinningLearnStrength) {
            iTmpWinningLearnStrength = bm.getLearnSigStr();
            iTmpWinningLearnNeuron = bm;
        }
        if (state_active && bm.getDriveSigStr() > iTmpWinningDriveStrength) {
            iTmpWinningDriveStrength = bm.getDriveSigStr();
            iTmpWinningDriveNeuron = bm;
        }
        if (state_active && bm.getLearnSigStr() > bm.getExcitationThreshold()) {
            iLearningNeurons.add(bm);
        }
    }

    protected double getTmpWinningDriveStrength() {
        return iTmpWinningDriveStrength;
    }

    protected void transferVarsToLIS() {
        iWinningDriveNeuron.setValue(iTmpWinningDriveNeuron);
        iWinningLearnNeuron.setValue(iTmpWinningLearnNeuron);
        iWinningDriveSignalStrength.setDoubleValue(iTmpWinningDriveStrength);
        iWinningLearnSignalStrength.setDoubleValue(iTmpWinningLearnStrength);
        iAllLearningNeurons.setValue(iLearningNeurons);
    }

    public void updateNeurons() {
        resetVars();
        System.out.println("Bimodelayer:Updating neurons");
        ListIterator i = getNeurons().listIterator(0);
        for (; i.hasNext(); ) {
            BiModeNeuron bmn = (BiModeNeuron) i.next();
            bmn.update();
            updateVars(bmn);
        }
        transferVarsToLIS();
        System.out.println("Bimodelayer:Updated neurons");
    }

    public void updateNeurons(Vesicle lv) {
        resetVars();
        ListIterator i = getNeurons().listIterator();
        for (; i.hasNext(); ) {
            BiModeNeuron bmn = (BiModeNeuron) i.next();
            bmn.update(lv);
            updateVars(bmn);
        }
        transferVarsToLIS();
    }
}
