package org.aspectme.weaver.debug;

import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.objectweb.asm.MethodVisitor;

public class TraceMethodVisitor implements InvocationHandler {

    private MethodVisitor mv;

    private PrintWriter out;

    public TraceMethodVisitor(MethodVisitor mv, Writer out) {
        this.mv = mv;
        this.out = new PrintWriter(out);
    }

    public TraceMethodVisitor(MethodVisitor mv) {
        this.mv = mv;
        this.out = new PrintWriter(System.out);
    }

    /**
	 * Creates a proxy that records all method instructions for later replay.
	 * @return A proxy for MethodVisitor.
	 */
    public MethodVisitor getProxy() {
        return (MethodVisitor) Proxy.newProxyInstance(MethodVisitor.class.getClassLoader(), new Class[] { MethodVisitor.class }, this);
    }

    /**
	 * Stores the information about each method invocation.
	 */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        out.print(method.getName());
        out.print("(");
        boolean first = true;
        if (args != null) {
            for (Object arg : args) {
                if (first) {
                    first = false;
                } else {
                    out.print(", ");
                }
                out.print(arg);
            }
        }
        out.println(")");
        out.flush();
        return method.invoke(mv, args);
    }
}
