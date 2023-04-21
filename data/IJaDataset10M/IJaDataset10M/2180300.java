package emast.algorithm.planner;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import emast.algorithm.PPFERG;
import emast.model.action.Action;
import emast.model.communication.message.MessageHistory;
import emast.model.communication.message.MessageListener;
import emast.model.communication.message.MessageListenerSupport;
import emast.model.communication.message.StateRewardMessage;
import emast.model.function.PropositionFunctionItem;
import emast.model.model.ERGMDPModel;
import emast.model.problem.ERGProblem;
import emast.model.propositional.Expression;
import emast.model.propositional.Proposition;
import emast.model.propositional.operator.BinaryOperator;
import emast.model.solution.Plan;
import emast.model.solution.Policy;
import emast.model.state.State;

/**
 *
 * @author Anderson
 */
public class CommAgentPlanner<M extends ERGMDPModel, P extends ERGProblem<M>> extends DefaultAgentPlanner<M, P> implements MessageListener<StateRewardMessage> {

    private State currentState;

    private final MessageHistory history;

    private final MessageListenerSupport messageListenerSupport;

    private final Map<Proposition, Double> propositionLocalReputation;

    private final Map<Proposition, Double> propositionMessageReputation;

    public CommAgentPlanner(final P pProblem, final Policy pInitialPolicy, final int pAgent) {
        super(pProblem, pInitialPolicy, pAgent);
        history = new MessageHistory();
        messageListenerSupport = new MessageListenerSupport();
        propositionLocalReputation = new HashMap<Proposition, Double>();
        propositionMessageReputation = new HashMap<Proposition, Double>();
    }

    @Override
    protected void doRun() {
        currentState = getProblem().getInitialStates().get(getAgent());
        setPlan(new Plan());
        if (getPolicy().get(currentState) != null) {
            do {
                final Action action = getPolicy().get(currentState).get(0);
                final State nextState = getModel().getTransitionFunction().getBestFinalState(currentState, action);
                if (nextState != null) {
                    final double reward = getModel().getRewardFunction().getValue(currentState, nextState, action);
                    boolean preservationChanged = false;
                    if (isBadRewardState(nextState, reward)) {
                        if (mustSendMessage(nextState, reward)) {
                            final StateRewardMessage msg = new StateRewardMessage(nextState, reward, getModel().getAgents().get(getAgent()));
                            sendBroadcast(msg);
                        }
                        if (mustChangePreservationGoal(nextState, reward)) {
                            final Policy p = changePreservationGoal(nextState);
                            preservationChanged = p != null;
                            if (preservationChanged) {
                                setPolicy(p);
                            }
                        }
                        savePropositionReputation(nextState, reward, propositionLocalReputation);
                    }
                    if (!preservationChanged) {
                        changeState(nextState, reward);
                        getPlan().add(action);
                    }
                } else {
                    currentState = null;
                }
            } while (currentState != null);
        }
    }

    private Policy changePreservationGoal(final State pState) {
        final Expression finalGoal = getProblem().getGoal();
        final Expression originalPreservGoal = getProblem().getPreservationGoal();
        final Expression newPropsExp = getNewPreservationGoal(originalPreservGoal, pState);
        final Expression newPreservGoal = new Expression(originalPreservGoal.toString());
        newPreservGoal.add(newPropsExp, BinaryOperator.AND);
        try {
            if (!newPreservGoal.equals(originalPreservGoal) && !originalPreservGoal.contains(newPropsExp) && !originalPreservGoal.contains(newPropsExp.negate()) && !getModel().getPropositionFunction().satisfies(pState, finalGoal, getModel().getPropositions())) {
                final P newProblem = cloneProblem(getProblem(), newPreservGoal);
                final Policy p = getAlgorithm().execute(newProblem);
                if (canReachFinalGoal(p, newProblem)) {
                    getProblem().setPreservationGoal(newPreservGoal);
                    print("changed preservation goal from {" + originalPreservGoal + "} to {" + newPreservGoal + "}");
                    return p;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private boolean canReachFinalGoal(final Policy pPolicy, final P pNewProblem) {
        final DefaultAgentPlanner planner = new DefaultAgentPlanner(pNewProblem, pPolicy, getAgent());
        try {
            planner.run();
            final Plan plan = planner.getPlan();
            return plan != null && !plan.isEmpty();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private Expression getNewPreservationGoal(final Expression originalPreservGoal, final State pState) {
        final PropositionFunctionItem pfi = getModel().getPropositionFunction().getItemForState(pState);
        Expression newPropsExp = pfi != null ? pfi.getExpression() : new Expression("");
        newPropsExp = newPropsExp.negate();
        return newPropsExp;
    }

    @Override
    public void messageReceived(final StateRewardMessage pMsg) {
        print("received message: " + pMsg + " from agent: " + pMsg.getSender());
        history.add(pMsg);
        savePropositionReputation(pMsg.getValue(), pMsg.getReward(), propositionMessageReputation);
        if (mustChangePreservationGoal(pMsg.getValue(), pMsg.getReward())) {
            final Policy p = changePreservationGoal(pMsg.getValue());
            if (p != null) {
                setPolicy(p);
            }
        }
    }

    protected void sendBroadcast(final StateRewardMessage pMsg) {
        print("sent broadcast: " + pMsg);
        addReward(getProblem().getMessageCost());
        messageListenerSupport.fireMessageSent(pMsg);
    }

    private void changeState(final State pFinalState, final double pReward) {
        addReward(pReward);
        currentState = pFinalState;
    }

    private boolean isBadRewardState(final State state, final double pReward) {
        return pReward < 0;
    }

    private Collection<Proposition> getPropositionsForState(final State state) {
        return getModel().getPropositionFunction().getPropositionsForState(state);
    }

    protected boolean mustSendMessage(final State state, final double pReward) {
        boolean somePropChanged = false;
        final Collection<Proposition> props = getPropositionsForState(state);
        final Double proportionalReward = pReward / props.size();
        if (props != null) {
            for (final Proposition proposition : props) {
                final Double rep = getPropositionReputation(proposition);
                final Double value = rep != null ? ((rep + proportionalReward) / 2) : proportionalReward;
                if (value < getProblem().getSendMessageThreshold()) {
                    somePropChanged = true;
                    break;
                }
            }
        }
        return somePropChanged;
    }

    private boolean mustChangePreservationGoal(final State state, final double pReward) {
        boolean somePropChanged = false;
        final Collection<Proposition> props = getPropositionsForState(state);
        final Double proportionalReward = pReward / props.size();
        if (props != null) {
            for (final Proposition proposition : props) {
                final Double rep = getPropositionReputation(proposition);
                final Double value = rep != null ? ((rep + proportionalReward) / 2) : proportionalReward;
                if (value < getProblem().getChangePreserveGoalThreshold()) {
                    somePropChanged = true;
                    break;
                }
            }
        }
        return somePropChanged;
    }

    public State getCurrentState() {
        return currentState;
    }

    private PPFERG<M, P> getAlgorithm() {
        return new PPFERG<M, P>();
    }

    public MessageListenerSupport getMessageListenerSupport() {
        return messageListenerSupport;
    }

    private void savePropositionReputation(final State pNextState, final double pReward, final Map<Proposition, Double> pPropositionReputationMap) {
        if (pPropositionReputationMap != null) {
            final Collection<Proposition> props = getPropositionsForState(pNextState);
            final double propReward = pReward / props.size();
            for (final Proposition proposition : props) {
                pPropositionReputationMap.put(proposition, propReward);
            }
        }
    }

    private void addReward(final double pReward) {
        if (pReward != 0) {
            totalReward += pReward;
            print("received reward of: " + pReward);
        }
    }

    private Double getPropositionReputation(final Proposition pProposition) {
        final Double localValue = propositionLocalReputation.get(pProposition);
        final Double externalValue = propositionMessageReputation.get(pProposition);
        if (localValue != null && externalValue != null) {
            return (localValue + externalValue) / 2;
        } else if (localValue != null) {
            return localValue;
        } else if (externalValue != null) {
            return externalValue;
        }
        return null;
    }

    private P cloneProblem(P problem, Expression newPreservGoal) throws CloneNotSupportedException {
        P newProblem = (P) getProblem().clone();
        newProblem.setPreservationGoal(newPreservGoal);
        final Map<Integer, State> is = new HashMap<Integer, State>();
        is.put(getAgent(), getProblem().getInitialStates().get(getAgent()));
        newProblem.setInitialStates(is);
        return newProblem;
    }
}
