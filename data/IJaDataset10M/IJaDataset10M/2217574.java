package com.koylu.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Rule {

    private String name;

    private String classname;

    private String method;

    private String methodParams;

    private String msg;

    private List parameters;

    private static Map typeMap = null;

    static {
        typeMap = new HashMap();
        typeMap.put("java.lang.String", new String());
        typeMap.put("java.lang.Integer", new Integer(0));
        typeMap.put("java.lang.Double", new Double(0.0));
        typeMap.put("java.lang.Float", new Float(0.0));
        typeMap.put("java.lang.Boolean", new Boolean(false));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethodParams() {
        return methodParams;
    }

    public void setMethodParams(String methodParams) {
        this.methodParams = methodParams;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List getParameters() {
        return parameters;
    }

    public void setParameters(List parameters) {
        this.parameters = parameters;
    }

    public void addParameter(Parameter parameter) {
        if (parameters == null) {
            parameters = new ArrayList();
        }
        parameters.add(parameter);
    }

    private List parameterList = null;

    public List getParameterList() {
        if (parameterList == null) {
            parameterList = new ArrayList();
            for (Iterator iterator = parameters.iterator(); iterator.hasNext(); ) {
                Parameter parameter = (Parameter) iterator.next();
                if ("parameter".equals(parameter.getType())) {
                    parameterList.add(parameter);
                }
            }
        }
        return parameterList;
    }

    private List messageList = null;

    public List getMessageList() {
        if (messageList == null) {
            messageList = new ArrayList();
            for (Iterator iterator = parameters.iterator(); iterator.hasNext(); ) {
                Parameter parameter = (Parameter) iterator.next();
                if ("message".equals(parameter.getType())) {
                    messageList.add(parameter);
                }
            }
        }
        return messageList;
    }

    public Boolean validate(Map map) throws Exception {
        Object obj = Class.forName(getClassname()).newInstance();
        List parameterList = getParameterList();
        List parameterValueList = new ArrayList();
        StringTokenizer token = new StringTokenizer(methodParams, ",");
        for (Iterator iterator = parameterList.iterator(); iterator.hasNext(); ) {
            Parameter parameter = (Parameter) iterator.next();
            Object value = map.get(parameter.getKey());
            if (value == null) {
                throw new RuntimeException("Invalid Parameter Key:" + parameter.getKey());
            }
            if (token.hasMoreTokens()) {
                String methodParameterType = token.nextToken();
                Object methodParameterTypeObj = typeMap.get(methodParameterType);
                if (methodParameterTypeObj == null) {
                    throw new RuntimeException("Invalid Method Parameter Type:" + methodParameterType);
                }
                String str = (String) MethodUtils.invokeMethod(new String(), "valueOf", new Object[] { value });
                value = MethodUtils.invokeMethod(methodParameterTypeObj, "valueOf", new Object[] { str });
            }
            parameterValueList.add(value);
        }
        return (Boolean) MethodUtils.invokeMethod(obj, getMethod(), parameterValueList.toArray());
    }

    public String getErrorMessage(String rowMessage, Map map) {
        List messageList = getMessageList();
        List messageValueList = new ArrayList();
        for (Iterator iterator = messageList.iterator(); iterator.hasNext(); ) {
            Parameter parameter = (Parameter) iterator.next();
            messageValueList.add(map.get(parameter.getKey()));
        }
        for (int k = 0; k < messageValueList.size(); k++) {
            String str = messageValueList.get(k) == null ? "" : messageValueList.get(k).toString();
            rowMessage = replace(rowMessage, "{" + k + "}", str);
        }
        return rowMessage;
    }

    public static String replace(String str, String target, String replacement) {
        if (str == null || target == null || replacement == null) {
            return null;
        }
        int k = str.indexOf(target);
        if (k > -1) {
            return str.substring(0, k) + replacement + replace(str.substring(k + target.length(), str.length()), target, replacement);
        }
        return str;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
