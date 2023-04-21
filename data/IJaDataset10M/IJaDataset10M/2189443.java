package imtek.optsuite.acquisition.routine;

import imtek.optsuite.acquisition.mtools.MeasurementTool;
import imtek.optsuite.acquisition.mtools.MeasurementToolPreferences;
import imtek.optsuite.acquisition.mtools.MeasurementToolPrefsStore;
import imtek.optsuite.acquisition.mtools.MeasurementToolRef;
import imtek.optsuite.acquisition.mtools.MeasurementToolRefProvider;
import imtek.optsuite.acquisition.mtools.MeasurementToolRegistry;
import imtek.optsuite.acquisition.mtools.MeasurementToolTreeNode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Abstract implementation of a MeasurementRoutineStep that holds
 * MeasurementTools that help to perform its measurement. A MeasurementTool can
 * be obtained through the static method {@link #getLockedMeasurementTool(MeasurementToolRoutineStep, String)}, 
 * which will also lock the requested tool.
 * 
 * @see #getLockedMeasurementTool(MeasurementToolRoutineStep, String)
 * @author Alexander Bieber <fleque@users.sourceforge.net>
 * 
 */
public abstract class MeasurementToolRoutineStep extends AbstractMeasurementRoutineStep {

    /**
	 * Should return a List of {@link Class} or {@link {@link String}}
	 *  of the required measurement tool types. The framework will make sure
	 * that at least one of every type will be created
	 * and made configurable.
	 * 
	 * @return A List of Class with the required MeasurementTool types.
	 */
    public abstract List<Class> getRequiredMeasurementToolTypes();

    /**
	 * key: String toolType
	 * value: MeasurementToolRef toolRef
	 */
    private Map<String, MeasurementToolRef> toolRefs = new HashMap<String, MeasurementToolRef>();

    /**
	 * key: MeasurementToolRef toolRef
	 * value: MeasurementToolPreferences toolPrefs
	 */
    private transient Map<MeasurementToolRef, MeasurementToolPreferences> toolPrefs;

    /**
	 * key: MeasurementToolRef toolRef
	 * value: MeasurementTool tool
	 */
    private transient Map<MeasurementToolRef, MeasurementTool> tools;

    /**
	 * Returns a cached instance of the MeasurementTool referenced by the
	 * given MeasurementToolRef.
	 * 
	 * @param toolRef The tool reference
	 */
    protected MeasurementTool getTool(MeasurementToolRef toolRef) {
        if (tools == null) tools = new HashMap<MeasurementToolRef, MeasurementTool>();
        MeasurementTool tool = tools.get(toolRef);
        if (tool != null) return tool;
        tool = MeasurementToolRegistry.sharedInstance().getMeasurementTool(toolRef);
        if (tool != null) tools.put(toolRef, tool);
        return tool;
    }

    /**
	 * Returns a cached instance of the MeasurementTool with the given toolType
	 * that is referenced by this RoutineStep.
	 * 
	 * @param toolType The toolType of the queried MeasurementTool
	 * @return The MeasurementTool of the given toolType.
	 */
    protected MeasurementTool getTool(String toolType) {
        MeasurementToolRef toolRef = getToolRef(toolType);
        if (toolRef == null) return null; else return getTool(toolRef);
    }

    /**
	 * Returns the MeasurementToolRef with the given toolType for this 
	 * MeasurementRoutine.
	 * @param toolType The toolType for the queried MeasurementToolRef
	 * @return The MeasurementToolRef with the given toolType
	 */
    protected MeasurementToolRef getToolRef(String toolType) {
        return (MeasurementToolRef) toolRefs.get(toolType);
    }

    /**
	 * Returns the MeasurementToolPrefernces for the given toolRef.
	 * 
	 * @param toolRef The tool reference the Preferences should be searched for.
	 * @return MeasurementToolPrefernces for the given toolRef.
	 */
    protected MeasurementToolPreferences getToolPreferences(MeasurementToolRef toolRef) {
        if (toolPrefs == null) toolPrefs = new HashMap<MeasurementToolRef, MeasurementToolPreferences>();
        MeasurementToolPreferences toolPref = toolPrefs.get(toolRef);
        if (toolPref != null) return toolPref;
        toolPref = MeasurementToolPrefsStore.sharedInstance().getToolPreferences(toolRef.toolPrefID);
        if (toolPref != null) toolPrefs.put(toolRef, toolPref);
        return toolPref;
    }

    /**
	 * Returns the MeasurementToolPrefernces for the given tool with the given 
	 * toolType referenced by this MeasurementRoutineStep.
	 * 
	 * @param toolType The toolType the Preferences should be searched for.
	 * @return MeasurementToolPrefernces for the given toolType.
	 */
    protected MeasurementToolPreferences getToolPreferences(String toolType) {
        MeasurementToolRef toolRef = getToolRef(toolType);
        if (toolRef == null) return null; else return getToolPreferences(toolRef);
    }

    /**
	 * Adds new MeasurementToolTreeNodes to the the given parentNode for all
	 * toolTypes returned by {@link #getRequiredMeasurementToolTypes()}.
	 * 
	 * @param parentNode The parent node where the MeasurementToolTreeNodes will be added.
	 */
    protected void addChildToolNodes(MeasurementRoutineStepTreeNode parentNode) {
        for (Iterator iter = getRequiredMeasurementToolTypes().iterator(); iter.hasNext(); ) {
            Class clazz = (Class) iter.next();
            MeasurementTool tool = getTool(clazz.getName());
            if (tool == null) continue;
            MeasurementToolTreeNode toolNode = tool.createMeasurementToolTreeNode(parentNode, getToolRef(clazz.getName()));
            parentNode.addChild(toolNode);
        }
    }

    /**
	 * Initializes the measurementRoutineStep for fist use.
	 */
    public void initializeRoutineStepRequiredMTools(MeasurementToolRefProvider toolRefProvider) {
        for (Iterator iter = getRequiredMeasurementToolTypes().iterator(); iter.hasNext(); ) {
            String typeName = null;
            Object obj = iter.next();
            if (obj instanceof String) typeName = (String) obj; else typeName = ((Class) obj).getName();
            MeasurementToolRef toolRef = toolRefProvider.getMeasurementToolRef(typeName);
            if (toolRef == null) continue;
            MeasurementTool tool = MeasurementToolRegistry.sharedInstance().getMeasurementTool(toolRef);
            if (tool == null) continue;
            MeasurementToolPreferences toolPref = tool.createNewDefaultPreferences();
            if (toolPref != null) {
                MeasurementToolPrefsStore.sharedInstance().addToolPref(toolPref);
                toolRef.setToolPrefID(toolPref.getToolPrefID());
                toolRefs.put(toolRef.toolType, toolRef);
            }
        }
    }

    /**
	 * @return Returns the toolRefs.
	 */
    public Map<String, MeasurementToolRef> getToolRefs() {
        return toolRefs;
    }

    /**
	 * @param toolRefs The toolRefs to set.
	 */
    public void setToolRefs(Map<String, MeasurementToolRef> toolRefs) {
        this.toolRefs = toolRefs;
    }

    public MeasurementTool getLockedTool(String toolType) {
        return getLockedMeasurementTool(this, toolType);
    }

    /**
	 * Returns a locked tool of the given tooltype, with the preferences
	 * adapted from the given MeasurementToolRoutineStep. The reciever has
	 * to make sure that the tool is released again after work is done on it,
	 * as other threads otherwise will be blocked when requesting to lock 
	 * this tool.
	 * 
	 * @param routineStep The routineStep holding the preferences the tool should be locked with.
	 * @param toolType The toolType of the desired MeasurementTool.
	 * @return A locked MeasurementTool of the given type.
	 */
    public static MeasurementTool getLockedMeasurementTool(MeasurementToolRoutineStep routineStep, String toolType) {
        MeasurementToolRef toolRef = routineStep.getToolRef(toolType);
        MeasurementTool tool = MeasurementToolRegistry.sharedInstance().getMeasurementTool(toolRef);
        MeasurementToolPreferences prefs = routineStep.getToolPreferences(toolRef);
        tool.lockTool(routineStep, prefs);
        return tool;
    }
}
