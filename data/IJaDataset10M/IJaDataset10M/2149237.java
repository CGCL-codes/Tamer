package br.inf.ufrgs.usixml4cdc.logicconector.java;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import br.inf.ufrgs.usixml4cdc.exceptions.MethodCallException;
import br.inf.ufrgs.usixml4cdc.exceptions.MethodConfigurationException;

public class MethodDefinition {

    private String className;

    private String methodName;

    private Map parametersDefinition = new HashMap();

    private int returnType;

    private Map integerParameter = new HashMap();

    private Map doubleParameter = new HashMap();

    private Map stringParameter = new HashMap();

    private String returnString = null;

    private Integer returnInteger = null;

    private Double returnDouble = null;

    public MethodDefinition() {
    }

    public void setClassName(String name) {
        this.className = name;
    }

    public void setMethodName(String name) {
        this.methodName = name;
    }

    public void setMethodReturnType(int returnType) {
        this.returnType = returnType;
    }

    public void addParameter(String parameter, int position) {
        this.parametersDefinition.put(new Integer(position), new Integer(2));
        this.stringParameter.put(new Integer(position), new Integer(parameter));
    }

    public void addParameter(Integer parameter, int position) {
        this.parametersDefinition.put(new Integer(position), new Integer(1));
        this.integerParameter.put(new Integer(position), parameter);
    }

    public void addParameter(Double parameter, int position) {
        this.parametersDefinition.put(new Integer(position), new Integer(0));
        this.doubleParameter.put(new Integer(position), parameter);
    }

    public void callMethod() throws MethodCallException {
        try {
            if (this.className == null || this.methodName == null) {
                throw new MethodCallException("Class or method name not configured");
            } else {
                int numParameters = this.parametersDefinition.size();
                final Class[] parameterTypeArray = new Class[numParameters];
                final Object[] parameterArray = new Object[numParameters];
                for (int i = 0; i < numParameters; i++) {
                    int parType = ((Integer) this.parametersDefinition.get(new Integer(i))).intValue();
                    switch(parType) {
                        case 0:
                            parameterTypeArray[i] = double.class;
                            parameterArray[i] = this.doubleParameter.get(new Integer(i));
                            break;
                        case 1:
                            parameterTypeArray[i] = int.class;
                            parameterArray[i] = this.integerParameter.get(new Integer(i));
                            break;
                        case 2:
                            parameterTypeArray[i] = String.class;
                            parameterArray[i] = this.stringParameter.get(new Integer(i));
                            break;
                        default:
                            throw new MethodCallException("Invalid parameter type");
                    }
                }
                Object obj = DomainModel.getInstance().getDomainModelObject(this.className);
                Method method = obj.getClass().getMethod(this.methodName, parameterTypeArray);
                Object returnValue = method.invoke(obj, parameterArray);
                switch(this.returnType) {
                    case 0:
                        this.returnDouble = (Double) returnValue;
                        break;
                    case 1:
                        this.returnInteger = (Integer) returnValue;
                        break;
                    case 2:
                        this.returnString = (String) returnValue;
                        break;
                    default:
                        throw new MethodCallException("Invalid return type");
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Integer getIntegerReturnValue() throws MethodConfigurationException {
        if (this.returnType == 1) {
            return this.returnInteger;
        } else {
            throw new MethodConfigurationException("Invalid method return type");
        }
    }

    public String getStringReturnValue() throws MethodConfigurationException {
        if (this.returnType == 2) {
            return this.returnString;
        } else {
            throw new MethodConfigurationException("Invalid method return type");
        }
    }

    public Double getDoubleReturnValue() throws MethodConfigurationException {
        if (this.returnType == 0) {
            return this.returnDouble;
        } else {
            throw new MethodConfigurationException("Invalid method return type");
        }
    }
}
