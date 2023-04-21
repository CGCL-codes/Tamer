package cn.edu.pku.dr.requirement.elicitation.business.proxy;

import java.lang.reflect.Proxy;

public class AmbiguityProxyFactory {

    public AmbiguityProxyFactory() {
    }

    public static Object getProxy(Object object) {
        AmbiguityProxy ap = new AmbiguityProxy();
        ap.setTarget(object);
        return Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), ap);
    }
}
