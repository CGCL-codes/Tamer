package chapter3.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 1.ģ����г���һ����ע�㷽�棺��־��¼
 *   aspect����������ͨ���ǰ�point cut��advice
 *
 * 2.ʹ����J2SE��̬���?��ģ���˸÷������
 *   װ��Ŀ�����(target object)��Ч����bind()����
 *
 */
public class LoggingProxyAspect implements InvocationHandler {

    private Object proxyobj;

    public LoggingProxyAspect(Object obj) {
        proxyobj = obj;
    }

    /**
	 * ͨ��̬������ɶ������
	 */
    public static Object bind(Object obj) {
        Class cls = obj.getClass();
        return Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), new LoggingProxyAspect(obj));
    }

    /**
	 * point cut: ����һ��join point�����ػ���Ƕ����ж���ķ������ã�
	 *            ���ҵ���beforeAdvice��afterAdvice
	 */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        beforeAdvice(method);
        Object object = method.invoke(proxyobj, args);
        afterAdvice(method);
        return object;
    }

    /**
	 * before advice������ǰ����
	 *
	 */
    private void beforeAdvice(Method method) {
        logging("before calling " + method.getName());
    }

    /**
	 * after advice: ���ú���
	 */
    private void afterAdvice(Method method) {
        logging("after calling " + method.getName());
    }

    /**
	 * ���й�ע�㣺��¼��־
	 */
    private void logging(String msg) {
        System.out.println(msg);
    }
}
