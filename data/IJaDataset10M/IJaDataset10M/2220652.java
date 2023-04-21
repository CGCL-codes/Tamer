package net.sf.orcc.tools.classifier;

import java.util.HashMap;
import java.util.Map;
import net.sf.orcc.OrccRuntimeException;
import net.sf.orcc.df.Action;
import net.sf.orcc.df.Actor;
import net.sf.orcc.df.Pattern;
import net.sf.orcc.df.Port;
import net.sf.orcc.ir.InstPhi;
import net.sf.orcc.ir.NodeIf;
import net.sf.orcc.ir.NodeWhile;
import net.sf.orcc.ir.Type;
import net.sf.orcc.ir.TypeList;
import net.sf.orcc.ir.Var;
import net.sf.orcc.ir.util.ActorInterpreter;
import net.sf.orcc.ir.util.IrUtil;
import net.sf.orcc.ir.util.ValueUtil;
import net.sf.orcc.moc.CSDFMoC;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;

/**
 * This class defines an abstract interpreter of an actor. It refines the
 * concrete interpreter by not relying on anything that is data-dependent.
 * 
 * @author Matthieu Wipliez
 * 
 */
public class AbstractInterpreter extends ActorInterpreter {

    private Map<String, Object> configuration;

    private Copier copier;

    private Map<Action, Action> originalActions;

    private Actor originalActor;

    private Map<String, Boolean> portRead;

    private boolean schedulableMode;

    private Action scheduledAction;

    /**
	 * Creates a new abstract interpreter.
	 * 
	 * @param actor
	 *            an actor
	 */
    public AbstractInterpreter(Actor actor) {
        originalActor = actor;
        copier = new EcoreUtil.Copier();
        Actor copyOfActor = IrUtil.copy(copier, actor);
        originalActions = new HashMap<Action, Action>();
        for (Action action : originalActor.getActions()) {
            originalActions.put((Action) copier.get(action), action);
        }
        setActor(copyOfActor);
        exprInterpreter = new AbstractExpressionEvaluator();
        initialize();
    }

    @Override
    public Object caseInstPhi(InstPhi phi) {
        if (branch != -1) {
            return super.caseInstPhi(phi);
        }
        return null;
    }

    @Override
    public Object caseNodeIf(NodeIf node) {
        Object condition = exprInterpreter.doSwitch(node.getCondition());
        int oldBranch = branch;
        if (ValueUtil.isBool(condition)) {
            if (ValueUtil.isTrue(condition)) {
                doSwitch(node.getThenNodes());
                branch = 0;
            } else {
                doSwitch(node.getElseNodes());
                branch = 1;
            }
        } else {
            if (schedulableMode) {
                throw new OrccRuntimeException("null condition");
            }
            branch = -1;
        }
        doSwitch(node.getJoinNode());
        branch = oldBranch;
        return null;
    }

    @Override
    public Object caseNodeWhile(NodeWhile node) {
        int oldBranch = branch;
        branch = 0;
        doSwitch(node.getJoinNode());
        Object condition = exprInterpreter.doSwitch(node.getCondition());
        if (ValueUtil.isBool(condition)) {
            branch = 1;
            while (ValueUtil.isTrue(condition)) {
                doSwitch(node.getNodes());
                doSwitch(node.getJoinNode());
                condition = exprInterpreter.doSwitch(node.getCondition());
                if (schedulableMode && !ValueUtil.isBool(condition)) {
                    throw new OrccRuntimeException("Condition not boolean at line " + node.getLineNumber() + "\n");
                }
            }
        } else if (schedulableMode) {
            throw new OrccRuntimeException("condition is data-dependent");
        }
        branch = oldBranch;
        return null;
    }

    @Override
    public void execute(Action action) {
        scheduledAction = action;
        Pattern inputPattern = action.getInputPattern();
        for (Port port : inputPattern.getPorts()) {
            int numTokens = inputPattern.getNumTokens(port);
            String portName = port.getName();
            if (configuration != null && configuration.containsKey(portName) && !portRead.get(portName)) {
                portRead.put(portName, true);
            }
            port.increaseTokenConsumption(numTokens);
        }
        Pattern outputPattern = action.getOutputPattern();
        allocatePattern(outputPattern);
        doSwitch(action.getBody());
        for (Port port : outputPattern.getPorts()) {
            int numTokens = outputPattern.getNumTokens(port);
            port.increaseTokenProduction(numTokens);
        }
    }

    /**
	 * Returns the latest action that was scheduled by the latest call to
	 * {@link #schedule()}.
	 * 
	 * @return the latest scheduled action
	 */
    public Action getScheduledAction() {
        return originalActions.get(scheduledAction);
    }

    @Override
    protected boolean isSchedulable(Action action) {
        Pattern pattern = action.getPeekPattern();
        for (Port port : pattern.getPorts()) {
            Var peeked = pattern.getVariable(port);
            String portName = port.getName();
            if (configuration != null && configuration.containsKey(portName) && !portRead.get(portName)) {
                TypeList typeList = (TypeList) peeked.getType();
                Object array = ValueUtil.createArray(typeList);
                peeked.setValue(array);
                Type type = typeList.getType();
                Object value = configuration.get(portName);
                ValueUtil.set(type, array, value, 0);
            }
        }
        setSchedulableMode(true);
        try {
            Object result = doSwitch(action.getScheduler());
            if (result == null) {
                throw new OrccRuntimeException("could not determine if action " + action.toString() + " is schedulable");
            }
            return ValueUtil.isTrue(result);
        } finally {
            setSchedulableMode(false);
        }
    }

    /**
	 * Sets the configuration that should be used by the interpreter.
	 * 
	 * @param configuration
	 *            a configuration as a map of ports and values
	 */
    public void setConfiguration(Map<String, Object> configuration) {
        this.configuration = configuration;
        portRead = new HashMap<String, Boolean>(configuration.size());
        for (String port : configuration.keySet()) {
            portRead.put(port, false);
        }
    }

    /**
	 * Sets schedulable mode. When in schedulable mode, evaluations of null
	 * expressions is forbidden.
	 * 
	 * @param schedulableMode
	 */
    public void setSchedulableMode(boolean schedulableMode) {
        this.schedulableMode = schedulableMode;
        ((AbstractExpressionEvaluator) exprInterpreter).setSchedulableMode(schedulableMode);
    }

    /**
	 * Sets the token rates, i.e. token production/consumption rates, of the
	 * given CSDF MoC. Token rates are set using the ports of the original actor
	 * (the actor this interpreter was created with) and not the copy (the actor
	 * on which the interpreter is working).
	 * 
	 * @param csdfMoc
	 *            a CSDF MoC
	 */
    public void setTokenRates(CSDFMoC csdfMoc) {
        Pattern inputPattern = csdfMoc.getInputPattern();
        for (Port originalPort : originalActor.getInputs()) {
            Port port = (Port) copier.get(originalPort);
            int numTokens = port.getNumTokensConsumed();
            inputPattern.setNumTokens(originalPort, numTokens);
        }
        Pattern outputPattern = csdfMoc.getOutputPattern();
        for (Port originalPort : originalActor.getOutputs()) {
            Port port = (Port) copier.get(originalPort);
            int numTokens = port.getNumTokensProduced();
            outputPattern.setNumTokens(originalPort, numTokens);
        }
    }
}
