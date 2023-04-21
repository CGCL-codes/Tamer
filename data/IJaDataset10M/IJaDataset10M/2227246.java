package net.sf.opendf.hades.des.components;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
    A ComponentDescriptor object provides information about a given DEC class. In this way,
    the I/O signature of component classes may be analyzed without instantiating them, provided
    that the DEC conforms to a standardized naming scheme ('design pattern').
    
    The I/O signature identifies the input and output connectors of a DEC and optionally their
    types (i.e. the types of the tokens flowing across these connectors). Each connector is
    defined by a ConnectorDescriptor.
    
    @see    SignatureDescriptor
    @see    net.sf.opendf.hades.des.DiscreteEventComponent
    
*/
public class ComponentDescriptor {

    private static final String componentRootName = "net.sf.opendf.hades.des.DiscreteEventComponent";

    private static final String interfaceRootName = "net.sf.opendf.hades.des.components.Interface";

    private static Class componentRootClass = net.sf.opendf.hades.des.DiscreteEventComponent.class;

    private static Class interfaceRootClass = net.sf.opendf.hades.des.components.Interface.class;

    private Class componentClass;

    private SignatureDescriptor signature;

    private ParameterDescriptor[] parameters;

    private Constructor defaultCtor = null;

    private Constructor parameterCtor = null;

    public boolean isDEC() {
        return this.isComponent();
    }

    public boolean isComponent() {
        return componentRootClass.isAssignableFrom(componentClass);
    }

    public boolean isInterface() {
        return interfaceRootClass.isAssignableFrom(componentClass);
    }

    public String getClassName() {
        return componentClass.getName();
    }

    public SignatureDescriptor getSignature() {
        return signature;
    }

    public ParameterDescriptor[] getParameters() {
        return parameters;
    }

    public Constructor getDefaultCtor() {
        return defaultCtor;
    }

    public Constructor getParameterCtor() {
        return parameterCtor;
    }

    public ComponentDescriptor(Class c) {
        this(c, "", false);
    }

    public ComponentDescriptor(Class c, String prefix, boolean inv) {
        componentClass = c;
        if (isDEC() || isInterface()) {
            signature = new SignatureDescriptor(componentClass, prefix, inv);
        }
        if (isDEC()) {
            try {
                Method parameterMethod = componentClass.getMethod(parameterMethodName, new Class[0]);
                parameters = (ParameterDescriptor[]) parameterMethod.invoke(null, new Object[0]);
            } catch (Exception e) {
                parameters = new ParameterDescriptor[0];
            }
            try {
                defaultCtor = componentClass.getConstructor(new Class[0]);
            } catch (Exception e) {
            }
            try {
                Class[] pars = new Class[] { Object.class };
                parameterCtor = componentClass.getConstructor(pars);
            } catch (Exception e) {
            }
        }
    }

    private static final String parameterMethodName = "getComponentParameters";

    private static void printUsage() {
        System.err.println("<java> net.sf.opendf.hades.des.components.ComponentDecsriptor <fullclassname>");
    }

    private String format() {
        if ((!this.isComponent()) && (!this.isInterface())) return this.getClassName() + ": No component or interface class.\n";
        String s = this.getClassName() + ": [" + (this.isInterface() ? "Interface" : "Component") + "]\n";
        if (this.isComponent()) {
            ParameterDescriptor[] pd = this.getParameters();
            if (pd != null) {
                s += "\tParameters:\n";
                for (int i = 0; i < pd.length; i++) {
                    s += "\t\t" + pd[i].getName() + ": " + ((pd[i].getType() == null) ? "<undef>" : pd[i].getType().getName()) + " [" + pd[i].getDefault() + "]\n";
                }
            } else s += "\tNo parameters.\n";
        }
        SignatureDescriptor sd = this.getSignature();
        s += "\tSignature [v" + sd.getSignatureVersion() + "]\n";
        s += "\t\tInputs:\n";
        for (Iterator i = sd.getAllAtomicConnectors().iterator(); i.hasNext(); ) {
            AtomicConnectorDescriptor acd = (AtomicConnectorDescriptor) i.next();
            if (acd.isInput()) s += "\t\t\t" + acd.getName() + ": " + acd.getType().getName() + "\n";
        }
        s += "\t\tOutputs:\n";
        for (Iterator i = sd.getAllAtomicConnectors().iterator(); i.hasNext(); ) {
            AtomicConnectorDescriptor acd = (AtomicConnectorDescriptor) i.next();
            if (acd.isOutput()) s += "\t\t\t" + acd.getName() + ": " + acd.getType().getName() + "\n";
        }
        return s;
    }

    public static void main(String[] args) {
        String s = "";
        if (args.length != 1) {
            printUsage();
            return;
        }
        String name = args[0];
        try {
            Class c = Class.forName(name);
            ComponentDescriptor cd = new ComponentDescriptor(c);
            s += cd.format();
        } catch (ClassNotFoundException e) {
            s += name + ": Cannot find class.\n";
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(s);
    }
}
