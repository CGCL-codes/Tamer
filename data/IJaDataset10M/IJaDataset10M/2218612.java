package net.sf.opendf.cal.interpreter.environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.sf.opendf.cal.interpreter.Context;
import net.sf.opendf.cal.interpreter.InterpreterException;

/**
 * A package environment represents an imported Java package. It maps all class names in that package to the
 * corresponding class object. The class object is constructed by the context.
 *
 *
 *  @see Context
 *
 *  @author Jorn W. Janneck <janneck@eecs.berkeley.edu>
 */
public class PackageEnvironment extends AbstractEnvironment implements Environment {

    public Object get(Object variable) {
        if (classes.containsKey(variable)) return classes.get(variable);
        String className = packagePrefix + "." + variable;
        try {
            Class c = classLoader.loadClass(className);
            Object classObject = context.createClass(c);
            classes.put(variable, classObject);
            return classObject;
        } catch (ClassNotFoundException e) {
            if (parent == null) throw new InterpreterException("Undefined variable '" + variable + "'.");
            return parent.get(variable);
        }
    }

    public void bind(Object variable, Object value) {
        throw new InterpreterException("Cannot create binding in package environment.");
    }

    public void set(Object variable, Object value) {
        if (parent == null) throw new InterpreterException("Undefined variable '" + variable + "'.");
        parent.set(variable, value);
    }

    public void freezeLocal() {
    }

    public Set localVars() {
        throw new InterpreterException("Cannot compute local variable set of package environment.");
    }

    public boolean isLocalVar(Object variable) {
        if (classes.containsKey(variable)) return true;
        String className = packagePrefix + "." + variable;
        try {
            Class c = classLoader.loadClass(className);
            Object classObject = context.createClass(c);
            classes.put(variable, classObject);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public Environment newFrame(Environment parent) {
        throw new InterpreterException("Cannot create new frame of package environment.");
    }

    public PackageEnvironment(Environment parent, ClassLoader classLoader, Context context, String packagePrefix) {
        super(parent, context);
        this.classLoader = classLoader;
        this.context = context;
        this.packagePrefix = packagePrefix;
    }

    private Map classes = new HashMap();

    private ClassLoader classLoader;

    private Context context;

    private String packagePrefix;
}
