package gov.sns.apps.mpxgen;

import gov.sns.tools.xml.XmlDataAdaptor;
import gov.sns.tools.xml.XmlDataAdaptor.ParseException;
import gov.sns.tools.xml.XmlDataAdaptor.ResourceNotFoundException;
import gov.sns.xal.model.ModelException;
import gov.sns.xal.model.elem.Element;
import gov.sns.xal.model.mpx.ModelProxy;
import gov.sns.xal.model.mpx.ModelProxyListener;
import gov.sns.xal.model.probe.TransferMapProbe;
import gov.sns.xal.model.probe.traj.ProbeState;
import gov.sns.xal.model.probe.traj.Trajectory;
import gov.sns.xal.model.scenario.Scenario;
import gov.sns.xal.model.mpx.MPXStopWatch;
import gov.sns.xal.slg.LatticeError;
import gov.sns.xal.smf.Accelerator;
import gov.sns.xal.smf.AcceleratorNode;
import gov.sns.xal.smf.AcceleratorSeq;
import gov.sns.xal.smf.data.XMLDataManager;
import java.io.File;
import java.net.MalformedURLException;
import org.w3c.dom.Document;

/**An extended ModelProxy for the MPXMain application.
 * @author wdklotz
 * @version $Id: MPXProxy.java,v 1.1 2005/08/05 00:52:56 cvs Exp $
 */
public class MPXProxy extends ModelProxy {

    private File accelMasterFile = null;

    private Accelerator accel = null;

    private boolean bAccel;

    public String srcFlag;

    /**Find the probe's 
    for a given {@link gov.sns.xal.smf.AcceleratorNode accelerator node}.
   	 * @param id the identifier of the given accelerator node.
   	 * @return the probe state for the center of that node.
   	 * @throws ModelException
   	 * override h.sako
   	 */
    @Override
    public ProbeState stateForElement(String id) throws ModelException {
        ProbeState state;
        try {
            checkLattice();
            checkProbe();
            AcceleratorNode node = scenario.nodeWithId(id);
            Element elem = null;
            if (node.getType().equals("DH")) {
                elem = (Element) scenario.elementsMappedTo(node).get(1);
            } else {
                elem = (Element) scenario.elementsMappedTo(node).get(0);
            }
            String latticeElementId = elem.getId();
            if (probe instanceof TransferMapProbe) {
            }
            state = scenario.trajectoryStatesForElement(latticeElementId)[0];
        } catch (LatticeError e) {
            throw new ModelException(e.getMessage());
        }
        return state;
    }

    MPXProxy(String src) {
        super(src);
        bAccel = false;
        srcFlag = src;
    }

    @Override
    public void runModel() throws ModelException {
        MPXStopWatch.timeElapsed("...runModel(): begin! ");
        System.out.println("MPXProxy, resetting probe");
        getProbe().reset();
        super.runModel();
        MPXStopWatch.timeElapsed("...runModel(): end! ");
    }

    public void setAccelerator(File file) {
        if (file.equals(accelMasterFile)) {
            bAccel = true;
            return;
        } else {
            bAccel = false;
            accelMasterFile = file;
            MPXStopWatch.timeElapsed("...acceleratorWithPath(accelMasterFile.getPath()): begin! ");
            accel = XMLDataManager.acceleratorWithPath(accelMasterFile.getPath());
            MPXStopWatch.timeElapsed("...acceleratorWithPath(accelMasterFile.getPath()): end! ");
            bAccel = true;
            notifyListeners(ModelProxyListener.ACCEL_CHANGED);
        }
    }

    public void setAccelerator(Accelerator accl) {
        bAccel = true;
        accel = accl;
    }

    /** Getter for the DOM of the master accelerator file.
	 * 
	 * @return the DOM of the accelerator master file.
	 */
    Document getAcceleratorAsDocument() {
        Document accelDoc = XmlDataAdaptor.newEmptyDocumentAdaptor("NULL", null).document();
        try {
            checkAccelerator();
        } catch (LatticeError e) {
            e.printStackTrace();
            return accelDoc;
        }
        try {
            accelDoc = XmlDataAdaptor.adaptorForFile(accelMasterFile, false).document();
        } catch (ParseException e1) {
            e1.printStackTrace();
            return accelDoc;
        } catch (ResourceNotFoundException e1) {
            e1.printStackTrace();
            return accelDoc;
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return accelDoc;
        }
        return accelDoc;
    }

    public Scenario getScenario() {
        return super.scenario;
    }

    @Override
    public void setAcceleratorSeq(AcceleratorSeq seq) {
        if (seq.equals(acceleratorSequence)) {
            return;
        } else {
            try {
                MPXStopWatch.timeElapsed("...setAcceleratorSeq(seq): begin! ");
                super.setAcceleratorSeq(seq);
                MPXStopWatch.timeElapsed("...setAcceleratorSeq(seq): end! ");
                acceleratorSequence = seq;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** Getter for the accelerator sequence as a DOM
	 * 
	 * @return the DOM of the acecelerator sequence.
	 */
    public Document getSequenceAsDocument() throws LatticeError {
        checkLattice();
        XmlDataAdaptor sequenceAdptr = XmlDataAdaptor.newEmptyDocumentAdaptor(null, null);
        acceleratorSequence.write(sequenceAdptr.createChild("ComboSequence"));
        return sequenceAdptr.document();
    }

    /** Getter for the trajectory structure as a DOM
	 * 
	 * @return the one line model trajectory object as a DOM.
	 */
    Document getTrajectoryAsDocument() {
        if (!isProbePropagated()) {
            throw new Error("Probe not propagated yet!");
        }
        Trajectory trajectory = scenario.getTrajectory();
        XmlDataAdaptor trajAdptr = XmlDataAdaptor.newEmptyDocumentAdaptor(null, null);
        trajectory.save(trajAdptr);
        return trajAdptr.document();
    }

    Accelerator getAccelerator() {
        try {
            checkAccelerator();
        } catch (LatticeError e) {
            e.printStackTrace();
            return null;
        }
        return accel;
    }

    File getAcceleratorMasterFile() {
        return accelMasterFile;
    }

    AcceleratorSeq getAcceleratorSeq() {
        try {
            checkLattice();
        } catch (LatticeError e) {
            e.printStackTrace();
            return null;
        }
        return acceleratorSequence;
    }

    MPXPhaseVectorTable getPhaseVectorTable() {
        return new MPXPhaseVectorTable(getProbeType(), scenario.getTrajectory());
    }

    MPXTwissFunctionTable getTwissFunctionTable() {
        return new MPXTwissFunctionTable(getProbeType(), scenario.getTrajectory());
    }

    MPXStatePerElementTable getStatePerElementTable(String id) throws ModelException, LatticeError {
        MPXStatePerElementTable result;
        checkProbe();
        int probeType = getProbeType();
        ProbeState state = stateForElement(id);
        result = new MPXStatePerElementTable(state, probeType);
        return result;
    }

    MPXStatePerElementTable[] getStatePerElementTables(String id) throws ModelException {
        MPXStatePerElementTable[] result;
        try {
            checkProbe();
        } catch (LatticeError e) {
            throw new ModelException(e.getMessage());
        }
        int probeType = getProbeType();
        ProbeState[] states;
        states = statesForElement(id);
        int stateCnt = states.length;
        result = new MPXStatePerElementTable[stateCnt];
        for (int i = 0; i < stateCnt; i++) {
            result[i] = new MPXStatePerElementTable(states[i], probeType);
        }
        return result;
    }

    boolean hasAccelerator() {
        return bAccel;
    }

    public void checkAccelerator() throws LatticeError {
        if (!hasAccelerator()) {
            notifyListeners(ModelProxyListener.MISSING_INPUT);
            throw new LatticeError("Missing Accelerator Master:");
        }
    }
}
