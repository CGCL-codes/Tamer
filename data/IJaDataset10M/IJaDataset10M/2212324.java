package br.inf.ufrgs.usixml4desktop.logicconector;

import br.inf.ufrgs.usixml4desktop.exceptions.MethodCallException;
import br.inf.ufrgs.usixml4desktop.exceptions.MethodConfigurationException;

public interface LogicConector {

    public void initializeMethod();

    public void prepareMethodName(String methodName) throws MethodConfigurationException;

    public void prepareMethodClass(String methodName) throws MethodConfigurationException;

    public void prepareMethodIntegerParameter(int parameter, int position) throws MethodConfigurationException;

    public void prepareMethodDoubleParameter(double parameter, int position) throws MethodConfigurationException;

    public void prepareMethodStringParameter(String parameter, int position) throws MethodConfigurationException;

    public void prepareMethodIntegerReturnParameter() throws MethodConfigurationException;

    public void prepareMethodDoubleReturnParameter() throws MethodConfigurationException;

    public void prepareMethodStringReturnParameter() throws MethodConfigurationException;

    public void executeMethod() throws MethodCallException;

    public String getMethodReturnString() throws MethodConfigurationException;

    public int getMethodReturnInteger() throws MethodConfigurationException;

    public double getMethodReturnDouble() throws MethodConfigurationException;
}
