package sf.qof.customizer;

import static sf.qof.codegen.Constants.SIG_getConnection;
import static sf.qof.codegen.Constants.SIG_setConnection;
import static sf.qof.codegen.Constants.SIG_ungetConnection;
import static sf.qof.codegen.Constants.TYPE_Connection;
import static sf.qof.codegen.Constants.TYPE_RuntimeException;
import static sf.qof.codegen.Constants.TYPE_SQLException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import org.objectweb.asm.Label;

/**
 * Provides the default implementation of a ConnectionFactoryCustomizer.
 * 
 * This customizer implements getConnection() and setConnection() methods and
 * a private field to store it if the super class does not have a <code>getConnection()</code>
 * method defined. 
 * 
 * @see ConnectionFactoryCustomizer
 */
public class DefaultConnectionFactoryCustomizer implements ConnectionFactoryCustomizer {

    private static final Class<?>[] CONNECTION_PARAMETER_TYPES = new Class<?>[] { java.sql.Connection.class };

    private static final String FIELD_NAME_CONNECTION = "$connection";

    public void emitFields(Class<?> queryDefinitionClass, Class<?> superClass, ClassEmitter ce) {
        if (!methodExists(superClass, "getConnection", null, java.sql.Connection.class)) {
            ce.declare_field(Constants.ACC_PRIVATE, FIELD_NAME_CONNECTION, TYPE_Connection, null, null);
        }
    }

    public void emitGetConnection(Class<?> queryDefinitionClass, Class<?> superClass, ClassEmitter ce) {
        if (!methodExists(superClass, "getConnection", null, java.sql.Connection.class)) {
            CodeEmitter co = ce.begin_method(Constants.ACC_PUBLIC, SIG_getConnection, null, null);
            co.load_this();
            co.getfield(FIELD_NAME_CONNECTION);
            Label labelNonNull = co.make_label();
            co.ifnonnull(labelNonNull);
            co.throw_exception(TYPE_SQLException, "Connection was not set");
            co.mark(labelNonNull);
            co.load_this();
            co.getfield(FIELD_NAME_CONNECTION);
            co.return_value();
            co.end_method();
        }
    }

    public void emitUngetConnection(Class<?> queryDefinitionClass, Class<?> superClass, ClassEmitter ce) {
        if (!methodExists(superClass, "ungetConnection", CONNECTION_PARAMETER_TYPES, null)) {
            CodeEmitter co = ce.begin_method(Constants.ACC_PUBLIC, SIG_ungetConnection, null, null);
            co.return_value();
            co.end_method();
        }
    }

    public void emitSetConnection(Class<?> queryDefinitionClass, Class<?> superClass, ClassEmitter ce) {
        if (!methodExists(superClass, "getConnection", null, java.sql.Connection.class)) {
            CodeEmitter co = ce.begin_method(Constants.ACC_PUBLIC, SIG_setConnection, null, null);
            co.load_this();
            co.load_arg(0);
            co.putfield(FIELD_NAME_CONNECTION);
            co.return_value();
            co.end_method();
        } else if (!methodExists(superClass, "setConnection", CONNECTION_PARAMETER_TYPES, null)) {
            CodeEmitter co = ce.begin_method(Constants.ACC_PUBLIC, SIG_setConnection, null, null);
            co.throw_exception(TYPE_RuntimeException, "Connection cannot be set");
            co.end_method();
        }
    }

    private boolean methodExists(Class<?> superClass, String methodName, Class<?>[] parameterTypes, Class<?> returnType) {
        try {
            Method method = superClass.getMethod(methodName, parameterTypes);
            int modifiers = method.getModifiers();
            if (Modifier.isPrivate(modifiers) || Modifier.isAbstract(modifiers)) {
                return false;
            }
            if (returnType != null && method.getReturnType() != returnType) {
                return false;
            } else if (returnType == null && method.getReturnType() != void.class) {
                return false;
            }
            return true;
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
        }
        return false;
    }
}
