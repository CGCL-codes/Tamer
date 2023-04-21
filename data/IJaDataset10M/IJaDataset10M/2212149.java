package org.freeorion.api;

import org.freeorion.Orion;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/** The CommandProcesser executes OrionCommands
 */
public class CommandProcessor {

    private Controller controller;

    private Object executor;

    private Hashtable methodHash;

    public CommandProcessor(Controller controller, Object executor, Class theInterface) {
        this.controller = controller;
        setExecutor(executor, theInterface);
    }

    public void setExecutor(Object executor, Class theInterface) {
        try {
            this.executor = executor;
            this.methodHash = new Hashtable();
            Method[] methods = theInterface.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                methodHash.put(methods[i].getName(), methods[i]);
            }
        } catch (SecurityException e) {
            this.executor = null;
            this.methodHash = null;
            System.err.println("SecurityException: " + e);
            throw e;
        }
    }

    protected Object[] parseParameters(String line, Class[] parameterTypes) throws IllegalArgumentException {
        int start = 0;
        int length = line.length();
        boolean quote = false;
        boolean space = true;
        Vector parameters = new Vector();
        for (int i = 0; i < length; i++) {
            char c = line.charAt(i);
            if (space && (c == ' ' || c == ',')) continue;
            if (quote && c != '"') continue;
            if (space) {
                space = false;
                quote = c == '"';
                start = quote ? i + 1 : i;
                continue;
            }
            if (c == ' ' || c == ',' || (quote && c == '"')) {
                quote = false;
                space = true;
                parameters.addElement(line.substring(start, i));
            }
        }
        if (!space) {
            parameters.addElement(line.substring(start));
        }
        if (parameters.size() != parameterTypes.length) throw new IllegalArgumentException("Wrong number of parameters: Expected " + parameterTypes.length + " but found " + parameters.size() + ".");
        Object[] result = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            String parameter = parameters.elementAt(i).toString();
            if (parameterTypes[i].equals(String.class)) {
                result[i] = parameter;
            } else if (parameterTypes[i].equals(Coordinate.class)) {
                result[i] = new Coordinate(parameter);
            } else if (parameterTypes[i].equals(Integer.TYPE)) {
                result[i] = new Integer(parameter);
            } else if (parameterTypes[i].equals(Building.class)) {
                result[i] = Building.getObject(parameter);
                if (result[i] == null) {
                    throw new IllegalArgumentException("Unknown Building: '" + parameter + "'!");
                }
            } else if (Construction.class.isAssignableFrom(parameterTypes[i])) {
                result[i] = null;
                if (parameter.startsWith("Fixed_")) {
                    result[i] = Building.getObject(parameter);
                }
                if (result[i] == null) {
                    result[i] = controller.getObject(parameter);
                }
                if (result[i] == null) throw new IllegalArgumentException("Unknown Construction: '" + parameter + "'!");
            } else if (Technology.class.isAssignableFrom(parameterTypes[i])) {
                result[i] = Building.getObject(parameter);
                if (result[i] == null) {
                    throw new IllegalArgumentException("Unknown Technology: '" + parameter + "'!");
                }
            } else if (OrionObject.class.isAssignableFrom(parameterTypes[i])) {
                result[i] = controller.getObject(parameter);
                if (result[i] == null) throw new IllegalArgumentException("Could not resolve Object: '" + parameter + "'!");
            } else {
                throw new IllegalArgumentException("Don't know how to parse type '" + parameterTypes[i].getName() + "', sorry!");
            }
        }
        return result;
    }

    void process(String command) {
        int p1 = command.indexOf('(');
        int p2 = command.lastIndexOf(')');
        String methodName = command.substring(0, p1);
        String parameterString = command.substring(p1 + 1, p2);
        Method method = (Method) methodHash.get(methodName);
        Object[] parameter = null;
        String className = executor.getClass().getName();
        className = className.substring(className.lastIndexOf(".") + 1);
        Orion.DEBUG("CommandProcessor[" + className + "]: " + "Evaluating command '" + command + "'", Orion.DEBUG_FLAG_DUMP_COMMANDS);
        try {
            if (method == null) throw new IllegalArgumentException("No such method '" + methodName + "'!");
            parameter = parseParameters(parameterString, method.getParameterTypes());
            method.invoke(executor, parameter);
        } catch (Exception e) {
            Orion.DEBUG("Exception while parsing command '" + command + "': " + e);
            if (e instanceof InvocationTargetException) {
                Throwable exception = ((InvocationTargetException) e).getTargetException();
                Orion.DEBUG("Target Exception: " + exception);
                exception.printStackTrace(System.err);
            }
            Orion.DEBUG("Method: " + methodName);
            if (parameter != null) {
                for (int i = 0; i < parameter.length; i++) {
                    Orion.DEBUG(i + ". parameter: desired type: " + ((method == null) ? "?" : method.getParameterTypes()[i].toString()) + ", actual type: " + (parameter[i] != null ? parameter[i].getClass().toString() : "null") + ", value: '" + (parameter[i] != null ? parameter[i] : "null") + "'");
                }
            } else {
                Orion.DEBUG("Parameter-Array: null!");
                Orion.DEBUG("paramterString: \"" + parameterString + "\"");
                Orion.DEBUG("method: " + method);
            }
        }
    }

    /** This is only set during the execution of Commands (i.e. when
	 * process(commands) is called.).
	 */
    private Enumeration commands;

    /** Evaluate a sequence of command strings.
	 */
    void process(Enumeration commands) {
        int count = 0;
        this.commands = commands;
        while (commands.hasMoreElements()) {
            String command = commands.nextElement().toString();
            if (command.startsWith("ID=")) {
                Orion.DEBUG("CommandProcessor: Discovered unused ID: " + command);
                continue;
            }
            process(command);
            count++;
        }
        this.commands = null;
        Orion.DEBUG("CommandProcessor: " + "Executed " + count + " commands.");
    }

    String getNextId() {
        if (commands == null) {
            Orion.DEBUG("CommandProcessor: No commands are set!");
            return "Error-id";
        }
        if (!commands.hasMoreElements()) {
            Orion.DEBUG("CommandProcessor: No entries on commands stack!");
            return "Error-id";
        }
        String id = commands.nextElement().toString();
        if (id.startsWith("ID=")) {
            id = id.substring(3);
        } else {
            Orion.DEBUG("CommandProcessor: Next entry is not an ID: " + id);
        }
        Orion.DEBUG("CommandProcessor: Got next ID: " + id);
        return id;
    }

    /**
	 */
    void process(CommandList commands) {
        process(commands.elements());
    }
}
