package com.siberhus.easyexecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.siberhus.commons.util.ElapsedTimeUtils;

public class BeanExecutor {

    private final Logger logger = LoggerFactory.getLogger(BeanExecutor.class);

    private String id;

    private Class<?> targetClass;

    private Object targetObject;

    private List<MethodInvoker> methodInvokerList = new ArrayList<MethodInvoker>();

    public void execute() throws Throwable {
        logger.info("Executing bean: {} ({})", id, targetObject);
        logger.info("Total methods of bean '{}' = {}", targetObject, methodInvokerList.size());
        ElapsedTimeUtils.start("bean");
        for (MethodInvoker mi : methodInvokerList) {
            logger.info("Invoking method: {}", mi.getTargetMethod());
            logger.info("Arguments: {}", Arrays.toString(mi.getArguments()));
            ElapsedTimeUtils.start("method");
            mi.invoke();
            logger.info("Method '{}' consumes : {}", mi.getTargetMethod(), ElapsedTimeUtils.showElapsedTime("method"));
        }
        logger.info("Bean: {} ({}) consumes : {}", new Object[] { id, targetObject, ElapsedTimeUtils.showElapsedTime("bean") });
    }

    public void addMethodInvoker(String methodName, Class<?> argTypes[], Object argValues[]) throws SecurityException, NoSuchMethodException {
        MethodInvoker mi = new MethodInvoker(getTargetObject());
        if (argTypes != null) {
            mi.setTargetMethod(getTargetClass().getMethod(methodName, argTypes));
        } else {
            mi.setTargetMethod(getTargetClass().getMethod(methodName));
        }
        mi.setArguments(argValues);
        methodInvokerList.add(mi);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString((String[]) null));
    }
}
