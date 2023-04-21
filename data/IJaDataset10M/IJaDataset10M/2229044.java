package lv.odylab.evemanage.client.rpc.action.quickcalculator;

import lv.odylab.evemanage.client.rpc.action.Response;
import lv.odylab.evemanage.client.rpc.dto.calculation.CalculationDto;

public class QuickCalculatorSetActionResponse implements Response {

    private CalculationDto calculation;

    public CalculationDto getCalculation() {
        return calculation;
    }

    public void setCalculation(CalculationDto calculation) {
        this.calculation = calculation;
    }
}
