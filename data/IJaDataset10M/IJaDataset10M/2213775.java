package org.ombu.participant.at;

import java.util.HashMap;
import java.util.Map;
import org.ombu.exceptions.InconsistentInternalStateException;
import org.ombu.exceptions.InvalidStateException;
import org.ombu.model.CoordinationContext;
import org.ombu.model.TwoPhaseCommitParticipant;
import org.ombu.model.TwoPhaseCommitParticipant.State;
import org.ombu.participant.ParticipantService;
import org.ombu.persistence.TwoPhaseCommitParticipantDAO;
import org.ombu.stubs.CoordinatorStub;
import org.ombu.util.Log;

public class TwoPhaseCommitParticipantService extends ParticipantService<TwoPhaseCommitParticipantManager> {

    private static TwoPhaseCommitParticipantService instance;

    public static TwoPhaseCommitParticipantService getInstance() {
        if (instance == null) {
            instance = new TwoPhaseCommitParticipantService();
        }
        return instance;
    }

    private TwoPhaseCommitParticipantService() {
    }

    private Map<String, TwoPhaseCommitParticipantManager> managers;

    @Override
    protected Map<String, TwoPhaseCommitParticipantManager> getManagers() {
        if (managers == null) {
            managers = new HashMap<String, TwoPhaseCommitParticipantManager>();
        }
        return managers;
    }

    private TwoPhaseCommitParticipant getParticipant(long participantId) {
        TwoPhaseCommitParticipantDAO dao = TwoPhaseCommitParticipantDAO.getInstance();
        TwoPhaseCommitParticipant participant = dao.find(participantId);
        return participant;
    }

    private TwoPhaseCommitParticipantManager getManager(String operation) {
        TwoPhaseCommitParticipantManager manager = getManagers().get(operation);
        if (manager == null) {
            String msg = "There's no manager registered to handle opeartion [%s]";
            throw new RuntimeException(String.format(msg, operation));
        }
        return manager;
    }

    public void processPrepare(long participantId) {
        TwoPhaseCommitParticipant participant = getParticipant(participantId);
        if (participant != null) {
            logResponse(participant, "prepare");
            CoordinatorStub stub = CoordinatorStub.getInstance();
            ;
            switch(participant.getState()) {
                case ACTIVE:
                    TwoPhaseCommitParticipantManager manager = getManager(participant.getOperation());
                    participant.setState(State.PREPARING);
                    TwoPhaseCommitParticipantDAO dao = TwoPhaseCommitParticipantDAO.getInstance();
                    dao.update(participant);
                    Vote result = manager.onPrepare(participant);
                    if (result == null) {
                        result = Vote.ABORTED;
                    }
                    switch(result) {
                        case PREPARED:
                            participant.setState(State.PREPARED);
                            dao.update(participant);
                            participant.setState(State.PREPARED_SUCCESS);
                            dao.update(participant);
                            stub.prepared(participant);
                            break;
                        case READONLY:
                            readOnlyDecision(participant);
                            break;
                        case ABORTED:
                            rollbackDecision(participant);
                            break;
                    }
                    break;
                case PREPARED_SUCCESS:
                    stub.prepared(participant);
                    break;
                case NONE:
                    stub.aborted(participant);
                    break;
                case PREPARED:
                case PREPARING:
                case COMMITTING:
                default:
                    break;
            }
        } else {
        }
    }

    public void processCommit(long participantId) {
        TwoPhaseCommitParticipant participant = getParticipant(participantId);
        if (participant != null) {
            logResponse(participant, "commit");
            CoordinatorStub stub = CoordinatorStub.getInstance();
            TwoPhaseCommitParticipantDAO dao = TwoPhaseCommitParticipantDAO.getInstance();
            switch(participant.getState()) {
                case PREPARED_SUCCESS:
                    participant.setState(State.COMMITTING);
                    dao.update(participant);
                    TwoPhaseCommitParticipantManager manager = getManager(participant.getOperation());
                    manager.onCommit(participant);
                    participant.setState(State.NONE);
                    dao.update(participant);
                    stub.commited(participant);
                    break;
                case COMMITTING:
                    break;
                case ACTIVE:
                case PREPARING:
                case PREPARED:
                    participant.setState(State.NONE);
                    dao.update(participant);
                    throw new InvalidStateException("Commit message received, but participant's state was " + participant.getState());
                case NONE:
                    stub.commited(participant);
                    break;
                default:
                    break;
            }
        } else {
        }
    }

    public void processRollback(long participantId) {
        TwoPhaseCommitParticipant participant = getParticipant(participantId);
        if (participant != null) {
            logResponse(participant, "rollback");
            switch(participant.getState()) {
                case ACTIVE:
                case PREPARING:
                case PREPARED:
                case PREPARED_SUCCESS:
                    initiateRollbackAndSendAborted(participant);
                    break;
                case COMMITTING:
                    throw new InconsistentInternalStateException("INCONSISTENCY: received 'rollback' message, but participant is " + participant.getState());
                case NONE:
                    CoordinatorStub stub = CoordinatorStub.getInstance();
                    stub.aborted(participant);
                    break;
                default:
                    break;
            }
        } else {
        }
    }

    private void initiateRollbackAndSendAborted(TwoPhaseCommitParticipant participant) {
        TwoPhaseCommitParticipantManager manager = getManager(participant.getOperation());
        if (!participant.getState().equals(State.ACTIVE)) manager.onRollback(participant);
        participant.setState(State.NONE);
        TwoPhaseCommitParticipantDAO dao = TwoPhaseCommitParticipantDAO.getInstance();
        dao = TwoPhaseCommitParticipantDAO.getInstance();
        dao.update(participant);
        CoordinatorStub stub = CoordinatorStub.getInstance();
        stub.aborted(participant);
    }

    public void rollbackDecision(long participantId) {
        TwoPhaseCommitParticipantDAO dao = TwoPhaseCommitParticipantDAO.getInstance();
        TwoPhaseCommitParticipant participant = dao.find(participantId);
        if (participant != null) {
            rollbackDecision(participant);
        }
    }

    public void rollbackDecision(TwoPhaseCommitParticipant participant) {
        TwoPhaseCommitParticipantDAO dao = TwoPhaseCommitParticipantDAO.getInstance();
        participant.setState(State.NONE);
        dao.update(participant);
        CoordinatorStub stub = CoordinatorStub.getInstance();
        stub.aborted(participant);
    }

    public void readOnlyDecision(long participantId) {
        TwoPhaseCommitParticipantDAO dao = TwoPhaseCommitParticipantDAO.getInstance();
        TwoPhaseCommitParticipant participant = dao.find(participantId);
        if (participant != null) {
            readOnlyDecision(participant);
        }
    }

    public void readOnlyDecision(TwoPhaseCommitParticipant participant) {
        TwoPhaseCommitParticipantDAO dao = TwoPhaseCommitParticipantDAO.getInstance();
        participant.setState(State.NONE);
        dao.update(participant);
        CoordinatorStub stub = CoordinatorStub.getInstance();
        stub.readonly(participant);
    }

    private void logResponse(TwoPhaseCommitParticipant part, String action) {
        long partId = part.getId();
        State partState = part.getState();
        String partStateStr = partState.toString();
        CoordinationContext ctx = part.getCoordinationContext();
        String ctxIdentifier = ctx.getIdentifier();
        String msg = String.format("Context %s: received '%s' for participant %d (with state %s)", ctxIdentifier, action, partId, partStateStr);
        Log.info(msg);
    }
}
