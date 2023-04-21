package bagaturchess.search.impl.rootsearch.sequential;

import bagaturchess.search.api.internal.ISearchMediator;

public class FinishCallback_SendToMediator implements IFinishCallback {

    private ISearchMediator mediator;

    public FinishCallback_SendToMediator(ISearchMediator _mediator) {
        mediator = _mediator;
    }

    public void ready() {
        if (mediator.getBestMoveSender() != null) mediator.getBestMoveSender().sendBestMove();
    }
}
