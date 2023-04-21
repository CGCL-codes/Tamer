package net.sf.mavenizer.analyser;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:cedric-vidal@users.sourceforge.net">C&eacute;dric Vidal</a>
 *
 */
public class LoggerInvocationHandler implements InvocationHandler {

    private static final String RIGHT_BRACKET = "]";

    private static final String LEFT_BRACKET = "[";

    private static final String RIGHT_CURLY = "}";

    private static final String COMMA_SEPARATOR = ", ";

    private static final String LEFT_CURLY = "{";

    private static final String NULL = "null";

    private static final String NAME_SEPARATOR = ": \t";

    private static final String SHARP = "#";

    private static Log log = LogFactory.getLog(LoggerInvocationHandler.class);

    private Object delegate;

    public LoggerInvocationHandler(Object delegate) {
        super();
        this.delegate = delegate;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        StringBuffer buffer = new StringBuffer();
        buffer.append(method.getDeclaringClass());
        buffer.append(SHARP);
        buffer.append(method.getName());
        buffer.append(NAME_SEPARATOR);
        append(buffer, args);
        log.debug(buffer.toString());
        Object res = method.invoke(getDelegate(), args);
        return res;
    }

    protected void append(StringBuffer buffer, Object object) {
        if (object == null) {
            buffer.append(NULL);
        } else if (object instanceof Iterable) {
            buffer.append(LEFT_CURLY);
            for (Iterator i = ((Iterable) object).iterator(); i.hasNext(); ) {
                append(buffer, i.next());
                if (i.hasNext()) {
                    buffer.append(COMMA_SEPARATOR);
                }
            }
            buffer.append(RIGHT_CURLY);
        } else if (object instanceof Object[]) {
            Object[] array = (Object[]) object;
            buffer.append(LEFT_BRACKET);
            if (array.length > 0) {
                append(buffer, array[0]);
            }
            for (int i = 1; i < array.length; i++) {
                buffer.append(COMMA_SEPARATOR);
                append(buffer, array[i]);
            }
            buffer.append(RIGHT_BRACKET);
        } else if (object.getClass().isArray()) {
            append(buffer, object.getClass());
        } else {
            buffer.append(object.toString());
        }
    }

    public Object getDelegate() {
        return delegate;
    }

    public void setDelegate(Object delegate) {
        this.delegate = delegate;
    }
}
