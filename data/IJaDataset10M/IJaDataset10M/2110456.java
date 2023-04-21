package net.sf.orcc.frontend;

import java.util.HashMap;
import java.util.Map;
import net.sf.orcc.cal.cal.AstActor;
import net.sf.orcc.cal.cal.AstEntity;
import net.sf.orcc.cal.cal.AstFunction;
import net.sf.orcc.cal.cal.AstPort;
import net.sf.orcc.cal.cal.AstProcedure;
import net.sf.orcc.cal.cal.AstSchedule;
import net.sf.orcc.cal.cal.AstScheduleRegExp;
import net.sf.orcc.cal.cal.AstVariable;
import net.sf.orcc.cal.services.AstExpressionEvaluator;
import net.sf.orcc.cal.util.Util;
import net.sf.orcc.frontend.schedule.ActionSorter;
import net.sf.orcc.frontend.schedule.FSMBuilder;
import net.sf.orcc.frontend.schedule.RegExpConverter;
import net.sf.orcc.ir.Actor;
import net.sf.orcc.ir.Expression;
import net.sf.orcc.ir.FSM;
import net.sf.orcc.ir.IrFactory;
import net.sf.orcc.ir.Port;
import net.sf.orcc.ir.Type;
import net.sf.orcc.ir.Var;
import net.sf.orcc.util.ActionList;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;

/**
 * This class transforms an AST actor to its IR equivalent.
 * 
 * @author Matthieu Wipliez
 * 
 */
public class ActorTransformer2 {

    /**
	 * Transforms the given AST Actor to an IR actor.
	 * 
	 * @param file
	 *            the .cal file where the actor is defined
	 * @param astActor
	 *            the AST of the actor
	 * @return the actor in IR form
	 */
    public Actor transform(IFile file, AstActor astActor) {
        Actor actor = IrFactory.eINSTANCE.createActor();
        actor.setFileName(file.getFullPath().toString());
        int lineNumber = Util.getLocation(astActor);
        actor.setLineNumber(lineNumber);
        Map<EObject, EObject> mapAstIr = new HashMap<EObject, EObject>();
        transformPorts(actor, mapAstIr, astActor);
        for (AstVariable astVariable : astActor.getParameters()) {
            transformParameter(actor, mapAstIr, astVariable);
        }
        for (AstVariable astVariable : astActor.getStateVariables()) {
            transformStateVariable(actor, mapAstIr, astVariable);
        }
        AstToIrTranslator translator = new AstToIrTranslator(mapAstIr);
        for (AstFunction function : astActor.getFunctions()) {
            translator.createProcedure(actor, function);
        }
        for (AstProcedure procedure : astActor.getProcedures()) {
            translator.createProcedure(actor, procedure);
        }
        AstToIrActionTransformer actionTransformer = new AstToIrActionTransformer(mapAstIr);
        ActionList actions = actionTransformer.transformActions(astActor.getActions());
        ActionList initializes = actionTransformer.transformActions(astActor.getInitializes());
        ActionSorter sorter = new ActionSorter(actions);
        ActionList sortedActions = sorter.applyPriority(astActor.getPriorities());
        AstSchedule schedule = astActor.getSchedule();
        AstScheduleRegExp scheduleRegExp = astActor.getScheduleRegExp();
        if (schedule == null && scheduleRegExp == null) {
            actor.getActionsOutsideFsm().addAll(sortedActions.getAllActions());
        } else {
            FSM fsm = null;
            if (schedule != null) {
                FSMBuilder builder = new FSMBuilder(astActor.getSchedule());
                fsm = builder.buildFSM(sortedActions);
            } else {
                RegExpConverter converter = new RegExpConverter(scheduleRegExp);
                fsm = converter.convert(sortedActions);
            }
            actor.getActionsOutsideFsm().addAll(sortedActions.getUntaggedActions());
            actor.setFsm(fsm);
        }
        AstEntity entity = (AstEntity) astActor.eContainer();
        actor.setName(net.sf.orcc.cal.util.Util.getQualifiedName(entity));
        actor.setNative(entity.isNative());
        actor.getActions().addAll(actions.getAllActions());
        actor.getInitializes().addAll(initializes.getAllActions());
        return actor;
    }

    /**
	 * Transforms actor parameter.
	 * 
	 * @param actor
	 * @param mapAstIr
	 * @param astVariable
	 */
    private void transformParameter(Actor actor, Map<EObject, EObject> mapAstIr, AstVariable astVariable) {
        int lineNumber = Util.getLocation(astVariable);
        Type type = Util.getType(astVariable);
        String name = astVariable.getName();
        Var var = IrFactory.eINSTANCE.createVar(lineNumber, type, name, false, null);
        actor.getParameters().add(var);
        mapAstIr.put(astVariable, var);
    }

    /**
	 * Transforms ports.
	 * 
	 * @param actor
	 * @param mapAstIr
	 * @param astActor
	 */
    private void transformPorts(Actor actor, Map<EObject, EObject> mapAstIr, AstActor astActor) {
        for (AstPort astPort : astActor.getInputs()) {
            Type type = Util.getType(astPort);
            Port port = IrFactory.eINSTANCE.createPort(type, astPort.getName(), astPort.isNative());
            mapAstIr.put(astPort, port);
            actor.getInputs().add(port);
        }
        for (AstPort astPort : astActor.getOutputs()) {
            Type type = Util.getType(astPort);
            Port port = IrFactory.eINSTANCE.createPort(type, astPort.getName(), astPort.isNative());
            mapAstIr.put(astPort, port);
            actor.getOutputs().add(port);
        }
    }

    /**
	 * Transforms a state variable.
	 * 
	 * @param actor
	 * @param mapAstIr
	 * @param astVariable
	 */
    private void transformStateVariable(Actor actor, Map<EObject, EObject> mapAstIr, AstVariable astVariable) {
        int lineNumber = Util.getLocation(astVariable);
        Type type = Util.getType(astVariable);
        String name = astVariable.getName();
        boolean assignable = !astVariable.isConstant();
        Expression initialValue = new AstExpressionEvaluator(null).evaluate(astVariable.getValue());
        Var var = IrFactory.eINSTANCE.createVar(lineNumber, type, name, assignable, initialValue);
        actor.getStateVars().add(var);
        mapAstIr.put(astVariable, var);
    }
}
