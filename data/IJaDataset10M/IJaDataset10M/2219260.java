package org.ombu.stubs;

import org.ombu.model.TwoPhaseCommitParticipant;
import org.ombu.stubs.CoordinatorStub.ICoordinatorStubStrategy;

public class DummyCoordinationStubStrategy implements ICoordinatorStubStrategy {

    @Override
    public void aborted(TwoPhaseCommitParticipant participant) {
    }

    @Override
    public void commited(TwoPhaseCommitParticipant participant) {
    }

    @Override
    public void prepared(TwoPhaseCommitParticipant participant) {
    }

    @Override
    public void readonly(TwoPhaseCommitParticipant participant) {
    }
}
