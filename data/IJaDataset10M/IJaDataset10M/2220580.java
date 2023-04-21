package br.inf.ufrgs.usixml4desktop.main;

import br.inf.ufrgs.usixml360.logicconector.csharp.CSharpLogicConector;
import br.inf.ufrgs.usixml4desktop.exceptions.MethodCallException;
import br.inf.ufrgs.usixml4desktop.exceptions.MethodConfigurationException;
import br.inf.ufrgs.usixml4desktop.logicconector.LogicConector;
import br.inf.ufrgs.usixml4desktop.logicconector.java.JavaLogicConector;

public class Main2 {

    private static Main2 _instance;

    public static Main2 getInstance() {
        if (_instance == null) {
            _instance = new Main2();
        }
        return _instance;
    }

    public static void main(String[] args) {
        try {
            LogicConector logicConector = new CSharpLogicConector();
            logicConector.initializeMethod();
            logicConector.prepareMethodClass("CSharpCalculator");
            logicConector.prepareMethodName("buttonPressed");
            logicConector.prepareMethodIntegerParameter(1, 0);
            logicConector.prepareMethodStringReturnParameter();
            logicConector.executeMethod();
            System.out.println(logicConector.getMethodReturnString());
            logicConector.initializeMethod();
            logicConector.prepareMethodClass("CSharpCalculator");
            logicConector.prepareMethodName("buttonPressed");
            logicConector.prepareMethodIntegerParameter(10, 0);
            logicConector.prepareMethodStringReturnParameter();
            logicConector.executeMethod();
            System.out.println(logicConector.getMethodReturnString());
            logicConector.initializeMethod();
            logicConector.prepareMethodClass("CSharpCalculator");
            logicConector.prepareMethodName("buttonPressed");
            logicConector.prepareMethodIntegerParameter(2, 0);
            logicConector.prepareMethodStringReturnParameter();
            logicConector.executeMethod();
            System.out.println(logicConector.getMethodReturnString());
            logicConector.initializeMethod();
            logicConector.prepareMethodClass("CSharpCalculator");
            logicConector.prepareMethodName("buttonPressed");
            logicConector.prepareMethodIntegerParameter(14, 0);
            logicConector.prepareMethodStringReturnParameter();
            logicConector.executeMethod();
            System.out.println(logicConector.getMethodReturnString());
        } catch (MethodConfigurationException e) {
            e.printStackTrace();
        } catch (MethodCallException e) {
            e.printStackTrace();
        }
    }
}
