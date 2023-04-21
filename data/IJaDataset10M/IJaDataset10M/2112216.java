package org.smartcc;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * Tracing method invocations.
 *
 * @author <a href="mailto:hengels@innovidata.com">Holger Engels</a>.
 * @version $Revision: 1.2 $
 */
public class TracingInterceptor extends AbstractInterceptor {

    boolean writeArgumentSize = true;

    public void setWriteArgumentSize(boolean writeArgumentSize) {
        this.writeArgumentSize = writeArgumentSize;
    }

    public Object invoke(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArguments();
        StringBuffer buffer = new StringBuffer();
        buffer.append("TRACE: ");
        buffer.append(invocation.getMethod().getDeclaringClass().getName());
        buffer.append(".");
        buffer.append(invocation.getMethod().getName());
        int length = buffer.length();
        buffer.append("(");
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                buffer.append(i == 0 ? "" : ", ");
                buffer.append(args[i]);
            }
        }
        buffer.append(")");
        if (args != null && args.length > 0) {
            buffer.append("[");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(args);
            oos.flush();
            buffer.append(baos.size());
            buffer.append("B]");
        }
        System.out.println(buffer.toString());
        buffer.setLength(length);
        long millis = System.currentTimeMillis();
        try {
            Object ret = getNext().invoke(invocation);
            millis = System.currentTimeMillis() - millis;
            buffer.append(": [");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(ret);
            oos.flush();
            buffer.append(baos.size());
            oos = null;
            baos = null;
            buffer.append("B]");
            buffer.append(" OK after " + millis + "ms");
            return ret;
        } catch (Throwable e) {
            millis = System.currentTimeMillis() - millis;
            buffer.append(": FAILED after " + millis + "ms\n");
            buffer.append(e.getClass().getName() + ": " + e.getMessage());
            throw e;
        } finally {
            System.out.println(buffer.toString());
        }
    }
}
