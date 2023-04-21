package jacky.lanlan.song.reflection.proxy;

import jacky.lanlan.song.util.Assert;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 代理工厂，专门生成代理。
 * 
 * @author Jacky.Song
 */
public abstract class ProxyFactory {

    /**
	 * 创建一个 JDK 动态代理。
	 * 
	 * @param handler
	 *          调用处理器
	 * @param ifaces
	 *          需要代理的接口(可变参数列表)
	 * @return 生成的代理
	 */
    public static Object createProxy(InvocationHandler handler, Class<?>... ifaces) {
        Assert.isTrue(ifaces.length > 0, "没有指定接口");
        return Proxy.newProxyInstance(ifaces[0].getClassLoader(), ifaces, handler);
    }

    /**
	 * 根据给定对象类创建一个和该对象实现了相同接口的 JDK 动态代理。
	 * 
	 * @param o 要创建代理的对象
	 * @param handler 调用处理器
	 * @return 生成的代理
	 * @throws IllegalArgumentException 如果该对象没有接口
	 */
    public static Object createProxy(Object o, InvocationHandler handler) {
        Class<?> objClass = o.getClass();
        Class<?>[] ifaces = objClass.getInterfaces();
        Assert.isTrue(ifaces.length > 0, objClass.getSimpleName() + "没有接口，无法创建代理");
        return createProxy(handler, ifaces);
    }

    /**
	 * 根据给定对象类创建一个该对象的 JDK 动态代理，代理的接口由ifaces指定。
	 * @param o 要创建代理的对象
	 * @param handler 调用处理器
   * @param ifaces 需要代理的接口(可变参数列表)
	 * @return 生成的代理
	 * @throws IllegalArgumentException 如果该对象没有接口，或者ifaces中有该对象没有实现的接口
	 */
    @SuppressWarnings("unchecked")
    public static Object createProxy(Object o, InvocationHandler handler, Class... ifaces) {
        Class objClass = o.getClass();
        for (Class c : ifaces) {
            Assert.isTrue(c.isAssignableFrom(objClass), objClass.getSimpleName() + " 没有实现接口 " + c.getSimpleName());
        }
        return createProxy(handler, ifaces);
    }

    /**
   * 创建一个 JDK 动态代理。
   * 
   * @param handler
   *          调用处理器
   * @param iface
   *          需要代理的接口
   * @return 生成的代理
   */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(InvocationHandler handler, Class<T> iface) {
        return (T) Proxy.newProxyInstance(iface.getClassLoader(), new Class[] { iface }, handler);
    }
}
