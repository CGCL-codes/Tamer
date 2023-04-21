package lv.odylab.evemanage.client.rpc.action.quickcalculator;

import com.google.inject.Inject;
import lv.odylab.evemanage.application.EveManageClientFacade;

public class QuickCalculatorTabFirstLoadActionRunnerImpl implements QuickCalculatorTabFirstLoadActionRunner {

    private EveManageClientFacade clientFacade;

    @Inject
    public QuickCalculatorTabFirstLoadActionRunnerImpl(EveManageClientFacade clientFacade) {
        this.clientFacade = clientFacade;
    }

    @Override
    public QuickCalculatorTabFirstLoadActionResponse execute(QuickCalculatorTabFirstLoadAction action) throws Exception {
        return new QuickCalculatorTabFirstLoadActionResponse();
    }
}
