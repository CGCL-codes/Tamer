package lv.odylab.evemanage.client.rpc.action.quickcalculator;

import com.google.inject.Inject;
import lv.odylab.evemanage.application.EveManageClientFacade;
import lv.odylab.evemanage.client.rpc.dto.calculation.CalculationDto;

public class QuickCalculatorUseBlueprintActionRunnerImpl implements QuickCalculatorUseBlueprintActionRunner {

    private EveManageClientFacade clientFacade;

    @Inject
    public QuickCalculatorUseBlueprintActionRunnerImpl(EveManageClientFacade clientFacade) {
        this.clientFacade = clientFacade;
    }

    @Override
    public QuickCalculatorUseBlueprintActionResponse execute(QuickCalculatorUseBlueprintAction action) throws Exception {
        CalculationDto calculationDto = clientFacade.getQuickCalculation(action.getPathNodes(), action.getBlueprintName());
        QuickCalculatorUseBlueprintActionResponse response = new QuickCalculatorUseBlueprintActionResponse();
        response.setPathNodes(action.getPathNodes());
        response.setCalculation(calculationDto);
        return response;
    }
}
