package palus.model;

import palus.Log;
import palus.PalusUtil;
import palus.main.PalusOptions;
import palus.trace.InitEntryEvent;
import palus.trace.InitExitEvent;
import palus.trace.MethodEntryEvent;
import palus.trace.MethodExitEvent;
import palus.trace.TraceEvent;
import plume.Pair;
import randoop.Globals;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This class keeps track of the dependent relations between trace events. The current
 * implementation mainly focuses on the the receivers/parameters of a constructor/method
 * call entry event. For example:
 * 
 * Trace event 1: A a = new A();
 * Trace event 2: a.foo();
 * Trace event 3: B b = new B(a);
 * Trace event 4: b.bar(a);
 * 
 * This class analyzes the trace to find out the a object in trace event 3 comes
 * from trace event 2. Thus, a 'dependence edge' between them will be established.
 * 
 * This class also analyzes that the object a (b) in trace event 2 (4) comes from the
 * result of trace event 1 (3). That relation might seems to be trivial, but could
 * be useful if there are multiple constructors with long method-call sequences.
 * 
 * TODO one possible limitation: it has not consider the side-effect of parameter.
 * For instance, in the current implementation, it also identifies that the a object
 * in trace event 4 comes from 2, instead of the state after trace event 3.
 * 
 * To overcome this:
 * 1. use a parameter mutability analysis to identify which method has side-effects.
 * 2. employ some heuristics.
 * 
 * @author saizhang@google.com (Sai Zhang)
 * 
 * */
public class TraceDependenceRepository {

    /**
   * The data structure keeping the trace dependences. It represents:
   * the position in: key.position of key.traceevent depends on the output of
   * the value.position of value.traceevent
   * 
   * There are some constraints (limitations) here. First, the key.position only
   * in parameters/receiver, key.traceevent could only be InitEntryEvent or MethodEntryEvent.
   * value.position only in ret value or receiver position, and value.event only
   * belongs to InitExitEvent and MethodExitEvent
   * */
    private static Map<TraceEventAndPosition, TraceEventAndPosition> traceDependences = new LinkedHashMap<TraceEventAndPosition, TraceEventAndPosition>();

    /**
   * @param traces
   *     a valid list of trace events, without any unmatched items
   * @param traceMap
   *     traces classified by its type and instance.
   * This method will initialize the <code>traceDependences</code> structure to
   * keep all these dependences
   * @throws ClassNotFoundException 
   * @throws BugInPalusException 
   * */
    public static void buildTraceDependences(List<TraceEvent> traces, Map<Class<?>, Map<Instance, List<TraceEventAndPosition>>> traceMap) throws ClassNotFoundException, BugInPalusException {
        PalusUtil.checkNull(traces, "The event trace list could not be null.");
        PalusUtil.checkNull(traceMap, "The trace map could not be null.");
        checkTraceSeqNumberValidity(traces);
        for (TraceEvent trace : traces) {
            if (!(trace instanceof MethodEntryEvent) && !(trace instanceof InitEntryEvent)) {
                continue;
            }
            if (trace instanceof MethodEntryEvent) {
                Class<?> thisType = trace.getReceiverType();
                int thisID = trace.getReceiverObjectID();
                Position thizPosition = Position.getThisPosition();
                if (thisID != 0) {
                    Pair<TraceEventAndPosition, TraceEventAndPosition> dependentPairThis = findDependentTracePair(traceMap, thisType, thisID, thizPosition, trace);
                    if (dependentPairThis != null) {
                        traceDependences.put(dependentPairThis.a, dependentPairThis.b);
                    }
                }
            }
            String[] serilizableParamValues = trace.getSerializableParams();
            int[] paramIDs = trace.getParamObjectIDs();
            PalusUtil.checkTrue(serilizableParamValues.length == paramIDs.length, "The serializable param value " + "length: " + serilizableParamValues.length + " should == param id length: " + paramIDs.length);
            for (int i = 0; i < serilizableParamValues.length; i++) {
                Class<?> paramType = trace.getParamType(i);
                int paramID = paramIDs[i];
                Position paramPosition = Position.getParaPosition(i + 1);
                Pair<TraceEventAndPosition, TraceEventAndPosition> dependentPairParam = findDependentTracePair(traceMap, paramType, paramID, paramPosition, trace);
                if (dependentPairParam != null) {
                    traceDependences.put(dependentPairParam.a, dependentPairParam.b);
                }
            }
        }
    }

    /**
   * Finding dependence from the original trace. Returns a pair of trace event and
   * its corresponding position, indicating the object in the position of key trace event
   * depends on the object in the position of the value trace event.
   * 
   * <em>Note: </em> we only consider the return value/this value in building
   * dependence between traces.
   * 
   * @return a pair of {@link TraceEventAndPosition} objects, indicating that the position
   * of the first trace event depends on the object produced by the second trace event of the
   * corresponding position
   * */
    private static Pair<TraceEventAndPosition, TraceEventAndPosition> findDependentTracePair(Map<Class<?>, Map<Instance, List<TraceEventAndPosition>>> traceMap, Class<?> objectType, int objectId, Position objectPosition, TraceEvent trace) {
        PalusUtil.checkNull(objectPosition, "The objectPosition argument could not be null!");
        PalusUtil.checkNull(trace, "The trace event argument could not be null!");
        if (objectId == 0) {
            return null;
        }
        if (!ClassesToModel.modelThisClass(objectType)) {
            return null;
        }
        int uniqueTraceSeqID = trace.getTraceEventSequenceID();
        Map<Instance, List<TraceEventAndPosition>> instanceMap = traceMap.get(objectType);
        if (instanceMap == null) {
            return null;
        }
        Instance desirable = new Instance(objectId, objectType);
        List<TraceEventAndPosition> tapList = instanceMap.get(desirable);
        if (tapList == null) {
            if (PalusOptions.avoid_serialize_dynamic_class) {
                return null;
            } else {
                throw new BugInPalusException("There is no event and position list for: " + desirable);
            }
        }
        TraceEvent dependentEvent = null;
        Position dependentPosition = null;
        for (TraceEventAndPosition tap : tapList) {
            TraceEvent event = tap.event;
            Position position = tap.position;
            if (!(event instanceof InitExitEvent) && !(event instanceof MethodExitEvent)) {
                continue;
            }
            int eventID = event.getTraceEventSequenceID();
            if (eventID > uniqueTraceSeqID) {
                break;
            } else {
                if (position.isRetPosition() || position.isThisPosition()) {
                    dependentEvent = event;
                    dependentPosition = position;
                    if (position.isRetPosition()) {
                        PalusUtil.checkTrue(event instanceof MethodExitEvent, "The event: " + event + " should be a MethodExitEvent type!");
                    }
                }
            }
        }
        if (dependentEvent != null && dependentPosition != null) {
            TraceEventAndPosition keyTAP = new TraceEventAndPosition(trace, objectPosition);
            TraceEventAndPosition valueTAP = new TraceEventAndPosition(dependentEvent, dependentPosition);
            return new Pair<TraceEventAndPosition, TraceEventAndPosition>(keyTAP, valueTAP);
        }
        return null;
    }

    /**
   * This method checks the TraceDependenceRepository to find the dependences between
   * transition and model node
   * @see TraceTransitionManager
   * @return Transition depends on a ModelNode
   * */
    public static Map<Pair<Transition, Position>, Pair<ModelNode, Position>> findModelDependence() {
        if (traceDependences.isEmpty()) {
            Log.log("There is no dependence information.");
            return null;
        }
        Map<Pair<Transition, Position>, Pair<ModelNode, Position>> transitionNodeMap = new LinkedHashMap<Pair<Transition, Position>, Pair<ModelNode, Position>>();
        Set<Entry<TraceEventAndPosition, TraceEventAndPosition>> entries = traceDependences.entrySet();
        for (Entry<TraceEventAndPosition, TraceEventAndPosition> entry : entries) {
            TraceEventAndPosition keyTAP = entry.getKey();
            List<Transition> dependentTransitions = TraceTransitionManager.findTransitionsByTraceEventAndPosition(keyTAP.event, keyTAP.position);
            if (dependentTransitions == null || dependentTransitions.isEmpty()) {
                continue;
            }
            TraceEventAndPosition valueTAP = entry.getValue();
            TraceEvent pairEvent = valueTAP.event.getPairEvent();
            List<Transition> dependentOnTransitions = TraceTransitionManager.findTransitionsByTraceEventAndPosition(pairEvent, valueTAP.position);
            if (dependentOnTransitions == null) {
                continue;
            }
            Log.log(Globals.lineSep + Globals.lineSep);
            Log.log(" size of dependent transitions: " + dependentTransitions.size());
            Log.log(" size of dependent on transitions: " + dependentOnTransitions.size());
            if (dependentTransitions.isEmpty() || dependentOnTransitions.isEmpty()) {
                continue;
            }
            PalusUtil.checkTrue(dependentTransitions.size() == 1, "The dependentTransitions' size: " + dependentTransitions.size() + " should == 1.");
            PalusUtil.checkTrue(dependentOnTransitions.size() == 1, "The dependentOnTransitions' size: " + dependentOnTransitions.size() + " should == 1.");
            Transition dependentTransition = dependentTransitions.get(0);
            Transition dependentOnTransition = dependentOnTransitions.get(0);
            PalusUtil.checkNull(dependentTransition, "The dependentTransition could not be null.");
            PalusUtil.checkNull(dependentOnTransition.getDestNode(), "The dest node of dependentOnTransition" + " could not be null!");
            transitionNodeMap.put(new Pair<Transition, Position>(dependentTransition, keyTAP.position), new Pair<ModelNode, Position>(dependentOnTransition.getDestNode(), valueTAP.position));
        }
        for (Entry<TraceEventAndPosition, TraceEventAndPosition> entry : traceDependences.entrySet()) {
            TraceEventAndPosition keyTAP = entry.getKey();
            TraceEvent keyTraceEvent = keyTAP.event;
            Position keyPosition = keyTAP.position;
            TraceEventAndPosition valueTAP = entry.getValue();
            TraceEvent valuePairTraceEvent = valueTAP.event.getPairEvent();
            Position valuePosition = valueTAP.position;
            List<Transition> allTransitions = TraceTransitionManager.findTransitionsByTraceEvent(keyTraceEvent);
            if (allTransitions == null) {
                continue;
            }
            List<Transition> dependentOnTransitions = TraceTransitionManager.findTransitionsByTraceEventAndPosition(valuePairTraceEvent, valuePosition);
            if (dependentOnTransitions == null || dependentOnTransitions.isEmpty()) {
                continue;
            }
            PalusUtil.checkTrue(dependentOnTransitions.size() == 1, "The dependentOnTransitions' size: " + dependentOnTransitions.size() + " should == 1.");
            Transition dependentOnTransition = dependentOnTransitions.get(0);
            PalusUtil.checkNull(dependentOnTransition, "The dependentOnTransition could not be null!");
            PalusUtil.checkNull(dependentOnTransition.getDestNode(), "The dest node of dependentOnTransition" + " could not be null!");
            for (Transition transition : allTransitions) {
                Pair<Transition, Position> keyPair = new Pair<Transition, Position>(transition, keyPosition);
                if (transitionNodeMap.containsKey(keyPair)) {
                    continue;
                }
                Log.log("@@@ New added pair: " + keyPair.a.getTransitionID() + ":" + keyPair.a.toSignature() + ",  position: " + keyPair.b);
                transitionNodeMap.put(keyPair, new Pair<ModelNode, Position>(dependentOnTransition.getDestNode(), valuePosition));
            }
        }
        return transitionNodeMap;
    }

    /**
   * Return the trace dependences
   * */
    public static Map<TraceEventAndPosition, TraceEventAndPosition> getTraceDependences() {
        return traceDependences;
    }

    /**
   * Get some basic information for the trace dependence map
   * */
    public static String getTraceDependenceInfo() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<TraceEventAndPosition, TraceEventAndPosition> entry : traceDependences.entrySet()) {
            TraceEventAndPosition key = entry.getKey();
            TraceEventAndPosition value = entry.getValue();
            sb.append(key.event.getTraceEventSequenceID() + " (position: " + key.position.toIntValue() + ") depends on " + value.event.getTraceEventSequenceID() + " (position: " + value.position.toIntValue() + ")");
            sb.append(Globals.lineSep);
        }
        return sb.toString();
    }

    /**
   * A private method to verify the precondition in build trace dependences
   * */
    private static void checkTraceSeqNumberValidity(List<TraceEvent> traces) {
        int id = 0;
        for (TraceEvent trace : traces) {
            PalusUtil.checkTrue(trace.getTraceEventSequenceID() == id, "The trace event sequence id: " + trace.getTraceEventSequenceID() + " should equal to id: " + id);
            id++;
        }
    }
}
