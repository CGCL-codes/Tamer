package net.sourceforge.htmlunit.corejs.javascript.tools.shell;

import java.io.InputStream;
import java.util.List;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationTargetException;
import net.sourceforge.htmlunit.corejs.javascript.Kit;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Function;

/**
 * Provides a specialized input stream for consoles to handle line
 * editing, history and completion. Relies on the JLine library (see
 * <http://jline.sourceforge.net>).
 */
public class ShellLine {

    public static InputStream getStream(Scriptable scope) {
        ClassLoader classLoader = ShellLine.class.getClassLoader();
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        if (classLoader == null) {
            return null;
        }
        Class<?> readerClass = Kit.classOrNull(classLoader, "jline.ConsoleReader");
        if (readerClass == null) return null;
        try {
            Constructor<?> c = readerClass.getConstructor();
            Object reader = c.newInstance();
            Method m = readerClass.getMethod("setBellEnabled", Boolean.TYPE);
            m.invoke(reader, Boolean.FALSE);
            Class<?> completorClass = Kit.classOrNull(classLoader, "jline.Completor");
            m = readerClass.getMethod("addCompletor", completorClass);
            Object completor = Proxy.newProxyInstance(classLoader, new Class[] { completorClass }, new FlexibleCompletor(completorClass, scope));
            m.invoke(reader, completor);
            Class<?> inputStreamClass = Kit.classOrNull(classLoader, "jline.ConsoleReaderInputStream");
            c = inputStreamClass.getConstructor(readerClass);
            return (InputStream) c.newInstance(reader);
        } catch (NoSuchMethodException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return null;
    }
}

/**
 * The completors provided with JLine are pretty uptight, they only
 * complete on a line that it can fully recognize (only composed of
 * completed strings). This one completes whatever came before.
 */
class FlexibleCompletor implements java.lang.reflect.InvocationHandler {

    private Method completeMethod;

    private Scriptable global;

    FlexibleCompletor(Class<?> completorClass, Scriptable global) throws NoSuchMethodException {
        this.global = global;
        this.completeMethod = completorClass.getMethod("complete", String.class, Integer.TYPE, List.class);
    }

    @SuppressWarnings({ "unchecked" })
    public Object invoke(Object proxy, Method method, Object[] args) {
        if (method.equals(this.completeMethod)) {
            int result = complete((String) args[0], ((Integer) args[1]).intValue(), (List<String>) args[2]);
            return Integer.valueOf(result);
        }
        throw new NoSuchMethodError(method.toString());
    }

    public int complete(String buffer, int cursor, List<String> candidates) {
        int m = cursor - 1;
        while (m >= 0) {
            char c = buffer.charAt(m);
            if (!Character.isJavaIdentifierPart(c) && c != '.') break;
            m--;
        }
        String namesAndDots = buffer.substring(m + 1, cursor);
        String[] names = namesAndDots.split("\\.", -1);
        Scriptable obj = this.global;
        for (int i = 0; i < names.length - 1; i++) {
            Object val = obj.get(names[i], global);
            if (val instanceof Scriptable) obj = (Scriptable) val; else {
                return buffer.length();
            }
        }
        Object[] ids = (obj instanceof ScriptableObject) ? ((ScriptableObject) obj).getAllIds() : obj.getIds();
        String lastPart = names[names.length - 1];
        for (int i = 0; i < ids.length; i++) {
            if (!(ids[i] instanceof String)) continue;
            String id = (String) ids[i];
            if (id.startsWith(lastPart)) {
                if (obj.get(id, obj) instanceof Function) id += "(";
                candidates.add(id);
            }
        }
        return buffer.length() - lastPart.length();
    }
}
