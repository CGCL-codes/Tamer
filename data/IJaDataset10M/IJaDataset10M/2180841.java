package com.fitso.web.service;

import com.fitso.model.bean.Body;
import com.fitso.model.bean.measure.mass.Mass;
import com.fitso.model.bean.measure.mass.Pound;
import com.fitso.model.calculator.IdealWeightCalculator;

public class IdealBodyWeightCalculatorService extends DWRService {

    private IdealWeightCalculator _calculator;

    public void setIdealWeightCalculator(IdealWeightCalculator calculator) {
        _calculator = calculator;
    }

    public Mass calculate(Body body) {
        return new Pound(_calculator.calculate(body));
    }
}
