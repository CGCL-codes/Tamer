package org.bluprint.app.mapper.adapter.uml2;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.Iterator;
import java.lang.RuntimeException;
import org.bluprint.app.mapper.JavaSourceJavaModelMapper;
import org.bluprint.app.mapper.adapter.ClassAdapter;
import org.bluprint.app.mapper.adapter.InterfaceAdapter;
import org.bluprint.app.mapper.adapter.PropertyAdapter;
import org.bluprint.app.mapper.adapter.AdapterException;
import org.eclipse.uml2.Class;
import org.eclipse.uml2.Interface;
import org.eclipse.uml2.Property;
import org.eclipse.uml2.Type;
import org.eclipse.emf.common.util.EList;
import org.apache.log4j.Logger;

/**
 * Implementation of the ClassAdapter.
 * 
 * @author Peter Long
 */
public class UML2ClassAdapter extends UML2ClassifierAdapter implements ClassAdapter {

    static Logger logger = Logger.getLogger(UML2ClassAdapter.class);

    public Logger getLogger() {
        return logger;
    }

    private Class uml2Class;

    public Class getUML2Class() {
        return uml2Class;
    }

    public void setUML2Class(Class aClass) {
        uml2Class = aClass;
    }

    public boolean isAbstract() {
        return getUML2Class().isAbstract();
    }

    /**
	 * Determine all the types that are referenced by this Class.
	 * Extend the implementation so that the types of properties are also included.
	 */
    public Type[] getReferencedTypes() {
        Type[] types = super.getReferencedTypes();
        HashSet<Type> typeSet = new HashSet<Type>();
        for (int i = 0; i < types.length; i++) {
            typeSet.add(types[i]);
        }
        PropertyAdapter[] properties = this.getProperties();
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].getType() != null) {
                typeSet.add(properties[i].getType());
            } else {
                this.getLogger().warn("Ignoring null property type encountered for property " + this.getUML2Class().getName() + "." + properties[i].getName());
            }
            if (properties[i].isQualified()) {
                try {
                    if (properties[i].getQualifyingType() != null) {
                        typeSet.add(properties[i].getQualifyingType());
                    } else {
                        this.getLogger().warn("Ignoring null qualifying type encountered for qualified property " + this.getUML2Class().getName() + "." + properties[i].getName());
                    }
                } catch (AdapterException exception) {
                    throw new RuntimeException("Unexpected exception encountered: " + exception.toString());
                }
            }
        }
        Set<Interface> interfaces = getUML2Class().getImplementedInterfaces();
        Iterator<Interface> iterator = interfaces.iterator();
        while (iterator.hasNext()) {
            Interface theInterface = iterator.next();
            if (theInterface != null) {
                typeSet.add(theInterface);
            }
        }
        return typeSet.toArray(types);
    }

    /**
	 * Return the fully qualified names of all the interfaces implemented by this class.
	 */
    public String[] getInterfaces() {
        Set<Interface> interfaces = getUML2Class().getImplementedInterfaces();
        Vector<String> interfaceNames = new Vector<String>();
        Iterator<Interface> iterator = interfaces.iterator();
        while (iterator.hasNext()) {
            interfaceNames.add(iterator.next().getQualifiedName());
        }
        return interfaceNames.toArray(new String[0]);
    }

    /**
	 * Return the set of properties defined for this class.  Wrap each property in its own adapter.
	 */
    public PropertyAdapter[] getProperties() {
        Vector<UML2PropertyAdapter> properties = new Vector<UML2PropertyAdapter>();
        EList attributes = getUML2Class().getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            UML2PropertyAdapter property = new UML2PropertyAdapter((Property) attributes.get(i));
            properties.add(property);
        }
        return properties.toArray(new PropertyAdapter[0]);
    }

    public void setIsAbstract(boolean flag) {
    }

    public void setInterfaces(InterfaceAdapter[] interfaces) {
    }

    public void setProperties(PropertyAdapter[] properties) {
    }

    public void wrap(Class aClass) {
        setUML2Class(aClass);
        super.wrap(aClass);
    }

    public UML2ClassAdapter(Class aClass) {
        super(aClass);
        wrap(aClass);
    }
}
