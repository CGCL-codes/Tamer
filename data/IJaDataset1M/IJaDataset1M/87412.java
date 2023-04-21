package org.ist.contract.DataStructures;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;
import org.ist.contract.MonitoringComponents.syncTime;
import org.ist.contract.MonitoringTools.CMonitorObject;
import org.ist.contract.interfaces.Agent;
import org.ist.contract.interfaces.Norm;
import org.ist.contract.interfaces.NormStatus;
import org.ist.contract.interfaces.NormType;

public class ClauseInterpretationBuilder extends CMonitorObject implements java.util.Observer, Serializable, Norm {

    private static final long serialVersionUID = 1L;

    private clauseState currentClauseState;

    private ClauseInterpretation currentClause;

    private Clause abstractNorm;

    private NormType modality;

    private syncTime[] deadline = new syncTime[2];

    private String cibName;

    private String modalityS = "";

    private String builderState = "";

    private boolean change = false;

    private long timeStamp = 0;

    private NormStatus clauseStatus;

    private NormTraceElementImpl stepHistory = new NormTraceElementImpl();

    private InstantiatedNormTraceImpl history = new InstantiatedNormTraceImpl();

    private String name = "";

    /**
	 * This class also contains the norm trace 
	 * @param abstractNorm : the associated abstract norm
	 * @param clause : interpretation corresponding to the ATN of the instantiated norm in some time
	 * @param timeStamp : represents the time of instantiation of the abstract norm
	 */
    public ClauseInterpretationBuilder(Clause abstractNorm, ClauseInterpretation clause, long timeStamp) {
        currentClause = clause;
        this.abstractNorm = abstractNorm;
        cibName = clause.getInterpretationName();
        currentClauseState = clause.getInitialState();
        modality = abstractNorm.getNormType();
        modalityS = abstractNorm.getClauseModality();
        builderState = "Activated";
        clauseStatus = NormStatus.ABSTRACT;
        clauseState critState = clause.getCriticalState();
        if (!(critState.getTempExp().equals(null))) {
            for (int k = 0; k < critState.getTempExp().size(); k++) {
                for (int k1 = 0; k1 < critState.getTempExp(k).size(); k1++) {
                    System.out.println("ADD DEADLINE");
                    deadline[k1] = new syncTime(critState.getTempExp(k).get(k1).intValue());
                    deadline[k1].addObserver(this);
                }
            }
        }
        stepHistory = new NormTraceElementImpl(this.getStatus(), timeStamp);
        history.addNormTraceElement(stepHistory);
    }

    /**
	 * This method checks if some conjunction i holds, given a reported event
	 * @param event
	 * @param i
	 * @return
	 */
    public boolean acceptSync(String event, int i) {
        boolean accept = false;
        change = false;
        if (currentClauseState.isTransition2ExistComment(event, i) & currentClauseState.currentTransitionMatched(i).size() == 1) {
            accept = true;
            change = true;
        }
        return accept;
    }

    /**
	 * This method checks if the one of the disjunction can be matched with the reported event 
	 * @param event
	 * @return
	 */
    public boolean accept(String event) {
        boolean accept = false;
        change = false;
        if (currentClauseState.isTransition2Exist(event)) {
            accept = true;
            change = true;
        }
        return accept;
    }

    /**
	 * This method builds the next state in the interpretation
	 * @param event
	 */
    public synchronized void nextState(String event) {
        currentClauseState = currentClauseState.getChild(event, currentClause);
        if (currentClauseState.getType().isFinal() | currentClauseState.getType().isNormal()) {
            if (modalityS.equals("Obligation") & !builderState.equals("obligationViolation")) {
                builderState = "obligationFulFillment";
                clauseStatus = NormStatus.COMPLETED;
                stepHistory = new NormTraceElementImpl(clauseStatus, timeStamp, abstractNorm.getNormCondition());
                history.addNormTraceElement(stepHistory);
            } else if (modalityS.equals("Prohibition") & !builderState.equals("prohibitionInhibited")) {
                builderState = "prohibitionViolation";
                clauseStatus = NormStatus.VIOLATED;
                stepHistory = new NormTraceElementImpl(clauseStatus, timeStamp, abstractNorm.getNormCondition() + "is occured");
                history.addNormTraceElement(stepHistory);
            } else {
                if (modalityS.equals("Permission") & !builderState.equals("permissionNotAllowed")) {
                    builderState = "permissionAllowed";
                    clauseStatus = NormStatus.COMPLETED;
                    stepHistory = new NormTraceElementImpl(clauseStatus, timeStamp, abstractNorm.getNormCondition());
                    history.addNormTraceElement(stepHistory);
                }
            }
        }
        if (currentClauseState.getType().isInitial()) {
            builderState = "newActiveClause";
            clauseStatus = NormStatus.ACTIVE;
            stepHistory = new NormTraceElementImpl(clauseStatus, timeStamp, abstractNorm.getActivationCondition());
            history.addNormTraceElement(stepHistory);
            for (int k = 0; k < deadline.length; k++) {
                if (!(deadline[k] == null)) {
                    System.out.println("TRIGGER THE CLOCK ASSOCIATED TO DEADLINE ");
                    deadline[k].run();
                }
            }
        }
    }

    /**
		 * This method notifies when the temporal expression, associated to the instantiated norm, is not still valid
		 * It also updates the trace of the norm
		 */
    public synchronized void update(Observable syncTimer, Object arg) {
        change = true;
        if (modalityS.equals("Obligation") & !builderState.equals("obligationFulFillment")) {
            builderState = "obligationViolation";
            clauseStatus = NormStatus.VIOLATED;
            timeStamp = deadline[0].getValue();
            stepHistory = new NormTraceElementImpl(clauseStatus, timeStamp, abstractNorm.getNormCondition() + "not matched");
            history.addNormTraceElement(stepHistory);
        } else if (modalityS.equals("Prohibition") & !builderState.equals("prohibitionViolation")) {
            builderState = "prohibitionInhibited";
            clauseStatus = NormStatus.COMPLETED;
            stepHistory = new NormTraceElementImpl(clauseStatus, timeStamp, abstractNorm.getNormCondition());
            history.addNormTraceElement(stepHistory);
        } else {
            if (modalityS.equals("Permission")) {
                builderState = "permissionNotAllowed";
                clauseStatus = NormStatus.COMPLETED;
                stepHistory = new NormTraceElementImpl(clauseStatus, timeStamp, abstractNorm.getNormCondition());
                history.addNormTraceElement(stepHistory);
            }
        }
    }

    /**   Usual set and get Methods ********/
    public String getName() {
        return name;
    }

    ;

    public void setName(String s) {
        name = s;
    }

    ;

    public NormStatus getStatus() {
        return clauseStatus;
    }

    public NormType getType() {
        return modality;
    }

    public InstantiatedNormTraceImpl getInstanciatedNormTrace() {
        return history;
    }

    public String getActivationCondition() {
        return abstractNorm.getActivationCondition();
    }

    public String getExpirationCondition() {
        return abstractNorm.getExpirationCondition();
    }

    public String getNormCondition() {
        return abstractNorm.getNormCondition();
    }

    public NormType getNormType() {
        return abstractNorm.getNormType();
    }

    public List<Agent> getTargets() {
        return abstractNorm.getTargets();
    }

    public void setActivationCondition(String activationCondition) {
        abstractNorm.setActivationCondition(activationCondition);
    }

    public void setExpirationCondition(String expirationCondition) {
    }

    public void setNormCondition(String normCondition) {
        abstractNorm.setNormCondition(normCondition);
    }

    public void setNormType(NormType normType) {
    }

    public void setTargets(List<Agent> Target) {
    }

    public ClauseInterpretation getInterpretation() {
        return currentClause;
    }

    public void setAbstractNorm(Clause instNorm) {
        abstractNorm = instNorm;
    }

    public void setCurrentState(clauseState state) {
        currentClauseState = state;
    }

    public synchronized void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public clauseState getCurrentClauseState() {
        return this.currentClauseState;
    }

    public String getNameCibName() {
        return cibName;
    }

    public NormType getModality() {
        return modality;
    }

    public String getBuilderState() {
        return builderState;
    }

    public boolean getChange() {
        return change;
    }
}
