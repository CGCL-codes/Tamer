package de.uniAugsburg.MAF.test.core.ui.visualizers;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import de.uniAugsburg.MAF.attrm.model.attrm.AttrInstance;
import de.uniAugsburg.MAF.core.exceptions.VisualizerException;
import de.uniAugsburg.MAF.core.instantiation.IAttributeInstantiationWrapper;
import de.uniAugsburg.MAF.core.visualizer.IVisualizerResult;
import de.uniAugsburg.MAF.test.core.ui.DebugWindow;
import de.uniAugsburg.MAF.test.core.ui.DebugWindowFascade;

public class VisualizerResultUI implements IVisualizerResult {

    private DebugWindow debugWindow;

    private String[] objectIDs;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss:SSS");

    public VisualizerResultUI(DebugWindow debugWindow, String[] objectIDs) {
        this.debugWindow = debugWindow;
        this.objectIDs = objectIDs;
    }

    @Override
    public void processResult(Date startTime, Date endTime, IAttributeInstantiationWrapper iwrapper, Collection<AttrInstance> assignmentResults, Collection<AttrInstance> constraintsPassed, Collection<AttrInstance> constraintsViolated, Map<Object, Object> storageInstantiation, Map<Object, Object> storageEvaluation, Map<String, Integer> invocationMap) throws VisualizerException {
        DebugWindowFascade.log(debugWindow, "result of evaluation - java invocations current evaluation (" + invocationMap.get("java_evaluation") + ")", null);
        DebugWindowFascade.log(debugWindow, "result of evaluation - java invocations overall (" + invocationMap.get("java_overall") + ")", null);
        DebugWindowFascade.log(debugWindow, "result of evaluation - ocl invocations current evaluation (" + invocationMap.get("ocl_evaluation") + ")", null);
        DebugWindowFascade.log(debugWindow, "result of evaluation - ocl invocations overall (" + invocationMap.get("ocl_overall") + ")", null);
        DebugWindowFascade.log(debugWindow, "result of evaluation - displaying results", null);
        DebugWindowFascade.clearInstances(debugWindow, debugWindow.getTableResult());
        for (AttrInstance instance : assignmentResults) {
            DebugWindowFascade.displayInstance(debugWindow, debugWindow.getTableResult(), instance, endTime, objectIDs);
        }
        for (AttrInstance instance : constraintsPassed) {
            DebugWindowFascade.displayInstance(debugWindow, debugWindow.getTableResult(), instance, endTime, objectIDs);
        }
        for (AttrInstance instance : constraintsViolated) {
            DebugWindowFascade.displayInstance(debugWindow, debugWindow.getTableResult(), instance, endTime, objectIDs);
        }
        DebugWindowFascade.log(debugWindow, "result of evaluation - analyzing storage", null);
        Map circleMap = (Map) storageInstantiation.get("circles");
        if (circleMap != null) {
            DebugWindowFascade.log(debugWindow, "circles detected", null);
            for (Object circle_id : circleMap.keySet()) {
                DebugWindowFascade.log(debugWindow, " -> " + circle_id.toString() + ": " + circleMap.get(circle_id), null);
            }
        }
        Set notReachableSet = (Set) storageInstantiation.get("not_reachable");
        if (notReachableSet != null) {
            DebugWindowFascade.log(debugWindow, "not reachable nodes detected", null);
            for (Object nrNode : notReachableSet) {
                DebugWindowFascade.log(debugWindow, " -> <" + nrNode + "> ", null);
            }
        }
        Set notLiveSet = (Set) storageInstantiation.get("not_live");
        if (notLiveSet != null) {
            DebugWindowFascade.log(debugWindow, "not live nodes detected", null);
            for (Object nlNode : notLiveSet) {
                DebugWindowFascade.log(debugWindow, " -> <" + nlNode + "> ", null);
            }
        }
        DebugWindowFascade.log(debugWindow, sdf.format(endTime) + " evaluation complete " + (endTime.getTime() - startTime.getTime()) + "ms", null);
    }

    @Override
    public void dispose() {
    }
}
