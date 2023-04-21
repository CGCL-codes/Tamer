package imtek.optsuite.stagecontrol;

import imtek.optsuite.acquisition.mtools.AbstractMeasurementTool;
import imtek.optsuite.acquisition.mtools.MeasurementToolPreferences;
import imtek.optsuite.acquisition.mtools.MeasurementToolRef;
import imtek.optsuite.acquisition.mtools.MeasurementToolTreeNode;
import imtek.optsuite.acquisition.mtools.composites.MeasurementToolRefDetailComposite;
import imtek.optsuite.acquisition.routine.MeasurementRoutineTreeNode;
import imtek.optsuite.stagecontrol.owis.StageControlOwis;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Alexander Bieber <fleque@users.sourceforge.net>
 *
 */
public class StageControlToolOWIS extends AbstractMeasurementTool implements StageControlTool {

    private Map<Integer, StageControl> controls = new HashMap<Integer, StageControl>();

    /**
	 * 
	 */
    public StageControlToolOWIS() {
        super();
    }

    public StageControl getStageControl(int motorNo) {
        StageControl control = controls.get(new Integer(motorNo));
        if (control == null) {
            control = new StageControlOwis(1, motorNo);
            controls.put(new Integer(motorNo), control);
        }
        return control;
    }

    public MeasurementToolRefDetailComposite createPreferencesGUI(Composite parent, long toolPrefID) {
        return null;
    }

    public MeasurementToolPreferences createNewDefaultPreferences() {
        return new StageControlToolPreferences();
    }

    public void adaptPreferences(MeasurementToolPreferences prefs) {
    }

    public MeasurementToolTreeNode createMeasurementToolTreeNode(MeasurementRoutineTreeNode parent, MeasurementToolRef toolRef) {
        MeasurementToolTreeNode node = new MeasurementToolTreeNode(parent, toolRef);
        node.setTitle("OWIS Stage");
        return node;
    }

    @Override
    public String getDescription() {
        return "StageControl by OWIS SM32 PCI Cards";
    }

    @Override
    public String getShortDescription() {
        return "StageControl - OWIS PCI";
    }
}
