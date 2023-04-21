package core.specification.operator;

import automata.State;
import automata.Transition;
import automata.fsa.FSATransition;
import automata.fsa.FiniteStateAutomaton;
import core.specification.compositionexpression.extensionpoint.ExtensionPoint;
import core.specification.compositionexpression.extensionpoint.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Siamak
 * Date: May 12, 2006
 * Time: 5:46:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class Include extends Operator {

    public Include(FiniteStateAutomaton baseUseCase, FiniteStateAutomaton referredUseCase, ExtensionPoint extensionPoint) {
        super(baseUseCase, referredUseCase);
        this.extensionPoints.add(extensionPoint);
    }

    public FiniteStateAutomaton getBaseBuilder(FiniteStateAutomaton baseUsecase, ExtensionPoint extensionPoint) {
        if (extensionPoint instanceof TransitionExtensionPoint) {
            TransitionExtensionPoint transExtPoint = (TransitionExtensionPoint) extensionPoint;
            FiniteStateAutomaton baseBuilder = baseUsecase;
            FSATransition extPoint = transExtPoint.getTransition();
            String place = transExtPoint.getPlace();
            State x1 = extPoint.getFromState();
            State x2 = extPoint.getToState();
            String label = extPoint.getLabel();
            State qb = baseBuilder.createColorState(new Point(x1.getPoint().x + 40, x1.getPoint().y - 40), baseUsecase.getColor());
            qb.setName("qb");
            State qe = baseBuilder.createColorState(new Point(x2.getPoint().x + 40, x2.getPoint().y - 40), baseUsecase.getColor());
            qe.setName("qe");
            Transition[] transitions = baseUsecase.getTransitions();
            for (int i = 0; i < transitions.length; i++) {
                FSATransition trans = (FSATransition) transitions[i];
                State y1 = trans.getFromState();
                String name1 = y1.getName();
                State y2 = trans.getToState();
                String name2 = y2.getName();
                String l = trans.getLabel();
                if ((l.equals(label)) && (name1.equals(x1.getName())) && (name2.equals(x2.getName()))) {
                    qb.setColor(y1.getColor());
                    qe.setColor(y2.getColor());
                    if (place.equals("AFTER")) {
                        baseBuilder.addTransition(getFSATransition(y1, qb, l, trans, baseBuilder));
                        baseBuilder.addTransition(getBaseBeginTransition(qb, qe, "begin_0"));
                        baseBuilder.addTransition(getBaseEndTransition(qe, y2, "end_0"));
                        baseBuilder.removeTransition(trans);
                    } else if (place.equals("BEFORE")) {
                        baseBuilder.addTransition(getBaseBeginTransition(y1, qb, "begin_0"));
                        baseBuilder.addTransition(getBaseEndTransition(qb, qe, "end_0"));
                        baseBuilder.addTransition(getFSATransition(qe, y2, l, trans, baseBuilder));
                        baseBuilder.removeTransition(trans);
                    }
                }
            }
            return baseBuilder;
        } else if (extensionPoint instanceof StateExtensionPoint) {
            FiniteStateAutomaton baseBuilder = baseUsecase;
            StateExtensionPoint stateExtPoint = (StateExtensionPoint) extensionPoint;
            State[] states = stateExtPoint.getStates();
            for (int j = 0; j < states.length; j++) {
                State state = states[j];
                State qb = baseBuilder.createColorState(new Point(state.getPoint().x + 40, state.getPoint().y - 40), baseUsecase.getColor());
                qb.setName("qb" + j);
                State qe = baseBuilder.createColorState(new Point(state.getPoint().x + 40, state.getPoint().y - 40), baseUsecase.getColor());
                qe.setName("qe" + j);
                state = baseUsecase.getStateWithName(state.getName());
                Transition[] fromStateTransitions = baseBuilder.getTransitionsFromState(state);
                for (Transition tran : fromStateTransitions) {
                    FSATransition fsaTran = (FSATransition) tran;
                    baseBuilder.addTransition(getFSATransition(qe, fsaTran.getToState(), fsaTran.getLabel(), fsaTran, baseBuilder));
                    baseBuilder.removeTransition(fsaTran);
                }
                qb.setColor(state.getColor());
                qe.setColor(state.getColor());
                baseBuilder.addTransition(getBaseBeginTransition(state, qb, "begin_" + j));
                baseBuilder.addTransition(getBaseEndTransition(qb, qe, "end_" + j));
            }
            return baseBuilder;
        } else if (extensionPoint instanceof LabelExtensionPoint) {
            FiniteStateAutomaton baseBuilder = baseUsecase;
            LabelExtensionPoint labelExtPoint = (LabelExtensionPoint) extensionPoint;
            String label = labelExtPoint.getLabel();
            String place = labelExtPoint.getPlace();
            List labelList = new LinkedList();
            Transition[] transitions = baseUsecase.getTransitions();
            for (Transition trans : transitions) {
                FSATransition fsaTrans = (FSATransition) trans;
                if (fsaTrans.getLabel().equals(label)) labelList.add(fsaTrans);
            }
            for (int j = 0; j < labelList.size(); j++) {
                FSATransition fsaTrans = (FSATransition) labelList.get(j);
                State x1 = fsaTrans.getFromState();
                State x2 = fsaTrans.getToState();
                State qb = baseBuilder.createColorState(new Point(x1.getPoint().x + 40, x1.getPoint().y - 40), baseUsecase.getColor());
                qb.setName("qb" + j);
                State qe = baseBuilder.createColorState(new Point(x2.getPoint().x + 40, x2.getPoint().y - 40), baseUsecase.getColor());
                qe.setName("qe" + j);
                State y1 = fsaTrans.getFromState();
                String name1 = y1.getName();
                State y2 = fsaTrans.getToState();
                String name2 = y2.getName();
                qb.setColor(y1.getColor());
                qe.setColor(y2.getColor());
                if (place.equals("AFTER")) {
                    baseBuilder.addTransition(getFSATransition(y1, qb, label, fsaTrans, baseBuilder));
                    baseBuilder.addTransition(getBaseBeginTransition(qb, qe, "begin_" + j));
                    baseBuilder.addTransition(getBaseEndTransition(qe, y2, "end_" + j));
                    baseBuilder.removeTransition(fsaTrans);
                } else if (place.equals("BEFORE")) {
                    baseBuilder.addTransition(getBaseBeginTransition(y1, qb, "begin_" + j));
                    baseBuilder.addTransition(getBaseEndTransition(qb, qe, "end_" + j));
                    baseBuilder.addTransition(getFSATransition(qe, y2, label, fsaTrans, baseBuilder));
                    baseBuilder.removeTransition(fsaTrans);
                }
            }
            return baseBuilder;
        }
        return null;
    }

    /**
     * The method to be overriden in the expression for the forward updating
     * of the extendion point during the recomposition process;
     */
    public void updateRelatedStates() {
        updateRelatedExtensionPoints((ExtensionPoint) extensionPoints.get(0));
    }

    /**
     * toString...
     */
    public String toString() {
        return "Include";
    }

    public String toStringExtensionPoints() {
        return getFirstExtensionPoint().toString();
    }
}
