package ru.adv.util.console;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import ru.adv.util.ClassCreator;

public class ReadLine {

    private static Class _readline;

    private static Class _readlineLibrary;

    private static Class _readlineCompleter;

    public static void load() throws Exception {
        _readline = ClassCreator.forName("org.gnu.readline.Readline");
        _readlineLibrary = ClassCreator.forName("org.gnu.readline.ReadlineLibrary");
        _readlineCompleter = ClassCreator.forName("org.gnu.readline.ReadlineCompleter");
        Method load = _readline.getMethod("load", new Class[] { _readlineLibrary });
        load.invoke(null, new Object[] { _readlineLibrary.getField("GnuReadline").get(null) });
    }

    public static void initReadline() throws Exception {
        Method initReadline = _readline.getMethod("initReadline", new Class[] { String.class });
        initReadline.invoke(null, new Object[] { "shelladmin" });
    }

    public static void cleanup() throws Exception {
        Method method = _readline.getMethod("cleanup", new Class[] {});
        method.invoke(null, new Object[] {});
    }

    public static void writeHistoryFile(String fileName) throws Exception {
        Method method = _readline.getMethod("writeHistoryFile", new Class[] { String.class });
        method.invoke(null, new Object[] { fileName });
    }

    public static void readHistoryFile(String fileName) throws Exception {
        Method method = _readline.getMethod("readHistoryFile", new Class[] { String.class });
        method.invoke(null, new Object[] { fileName });
    }

    public static void setCompleter(InvocationHandler handler) throws Exception {
        Method method = _readline.getMethod("setCompleter", new Class[] { _readlineCompleter });
        method.invoke(null, new Object[] { createCompleterProxy(handler) });
    }

    public static Object createCompleterProxy(InvocationHandler handler) {
        return Proxy.newProxyInstance(_readlineCompleter.getClassLoader(), new Class[] { _readlineCompleter }, handler);
    }

    public static String getLineBuffer() {
        String result = "";
        try {
            Method method = _readline.getMethod("getLineBuffer", new Class[] {});
            result = (String) method.invoke(null, new Object[] {});
        } catch (Exception e) {
        }
        return result;
    }

    public static String readline(String prompt) {
        String result = null;
        try {
            Method method = _readline.getMethod("readline", new Class[] { String.class });
            result = (String) method.invoke(null, new Object[] { prompt });
        } catch (Exception e) {
        }
        return result;
    }
}
