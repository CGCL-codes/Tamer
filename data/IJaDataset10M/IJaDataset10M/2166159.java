package cn.edu.pku.dr.requirement.elicitation.business.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import cn.edu.pku.dr.requirement.elicitation.action.ProblemSolutionIntercepter;
import easyJ.common.EasyJException;

public class ProblemSolutionProxy implements InvocationHandler {

    public ProblemSolutionProxy() {
    }

    private Object target;

    ProblemSolutionIntercepter psi = new ProblemSolutionIntercepter();

    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalArgumentException, IllegalAccessException, EasyJException {
        Object result = null;
        if (method.getName().equals("getProblemsolution")) {
            result = method.invoke(target, args);
        }
        if (method.getName().equals("problemsolutionUpdate")) {
            result = method.invoke(target, args);
        }
        if (method.getName().equals("viewDetailedSolution")) {
            result = method.invoke(target, args);
        }
        if (method.getName().equals("problemsolutionReplyUpdate")) {
            result = method.invoke(target, args);
        }
        if (method.getName().equals("creatingReply")) {
            result = method.invoke(target, args);
        }
        if (method.getName().equals("createSolution")) {
            result = method.invoke(target, args);
        }
        if (method.getName().equals("creatingSolution")) {
            result = method.invoke(target, args);
        }
        return result;
    }

    public void setTarget(Object o) {
        this.target = o;
    }
}
