package com.siberhus.stars.test.action.spring;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import com.siberhus.stars.test.action.BaseAction;
import com.siberhus.stars.test.service.Calculator;

public class SpringCalculatorAction extends BaseAction implements InitializingBean, DisposableBean {

    private Double numberOne = new Double(0);

    private Double numberTwo = new Double(0);

    private Double result = new Double(0);

    @Autowired
    private Calculator calcService;

    private String message;

    @Override
    public void afterPropertiesSet() throws Exception {
        message = "Hello";
    }

    @Override
    public void destroy() throws Exception {
        message = "Goodbye";
    }

    @DefaultHandler
    public Resolution add() {
        result = calcService.add(numberOne, numberTwo);
        return new ForwardResolution("");
    }

    public Double getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public Double getNumberOne() {
        return numberOne;
    }

    public void setNumberOne(Double numberOne) {
        this.numberOne = numberOne;
    }

    public Double getNumberTwo() {
        return numberTwo;
    }

    public void setNumberTwo(Double numberTwo) {
        this.numberTwo = numberTwo;
    }
}
