package witspirit.transactional.client.fsm;

import witspirit.transactional.client.Configuration;

public class IdleState<REQUEST> extends BaseState<REQUEST> {

    public IdleState(Configuration<REQUEST> configuration, REQUEST request) {
        super(configuration, request);
    }

    @Override
    public TransactionState abort() {
        return this;
    }

    @Override
    public TransactionState activate() {
        return this;
    }
}
