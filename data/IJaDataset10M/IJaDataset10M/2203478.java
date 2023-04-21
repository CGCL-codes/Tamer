package org.codemode.bishop.entity;

import org.codemode.bishop.BishopEntity;
import org.codemode.bishop.Model;
import org.codemode.bishop.Scope;
import org.codemode.bishop.BishopException;

public class Object extends BishopEntity {

    protected static final String NAME = "name";

    protected static final String TYPE = "type";

    protected static final String STATIC = "static";

    protected static final String EXPORT = "export";

    protected java.util.Vector ins = new java.util.Vector();

    /**
       *
       */
    public void invoke(Scope locals, org.codemode.bishop.View view, Model model) throws Throwable {
        String _name = (String) attributes.get(NAME);
        String _class = (String) attributes.get(TYPE);
        Class c;
        boolean _static = new Boolean((String) attributes.get(STATIC)).booleanValue();
        boolean _export = new Boolean((String) attributes.get(EXPORT)).booleanValue();
        try {
            java.lang.Object o = locals.getObject(_name);
            if (o == null) {
                c = getObjectClass();
                o = createObjectFromClass(c, locals);
            } else if (o.getClass().equals(getObjectClass()) == false) {
                {
                    throw new BishopException(this, "Object " + _name + " is already defined as " + o.getClass() + ", not " + getObjectClass() + ".");
                }
            }
            locals.putObject(_name, o);
            if (_static) {
                model.setObject(_name, o);
            }
            if (_export) {
                view.put(_name, o);
            }
        } catch (Exception e) {
            throw new BishopException(e, this, "Failed to invoke object");
        }
    }

    /**
       * Returns the object's name
       */
    public String getName() {
        return (String) attributes.get(NAME);
    }

    /**
       * Returns true if the object is viewable
       */
    public boolean isExported() {
        return new Boolean((String) attributes.get(EXPORT)).booleanValue();
    }

    /**
       * Returns true if the object is in the model.
       */
    public boolean isStatic() {
        return new Boolean((String) attributes.get(STATIC)).booleanValue();
    }

    /**
       *
       */
    public String getType() {
        return (String) attributes.get(TYPE);
    }

    /** 
       * Returns the Class of the underlying object. If the object is a primitive type,
       * it's representing class is returned (Short.TYPE, Integer.TYPE, etc)
       */
    protected Class getObjectClass() throws BishopException {
        Class c = null;
        String type = (String) attributes.get(TYPE);
        try {
            c = Class.forName(type);
        } catch (java.lang.ClassNotFoundException e) {
            if (type.equals("int")) c = Integer.TYPE; else if (type.equals("boolean")) c = Boolean.TYPE; else if (type.equals("byte")) c = Byte.TYPE; else if (type.equals("char")) c = Character.TYPE; else if (type.equals("double")) c = Double.TYPE; else if (type.equals("float")) c = Float.TYPE; else if (type.equals("long")) c = Long.TYPE; else if (type.equals("short")) c = Short.TYPE; else throw new BishopException(e, this, "Class not found: " + e.getMessage());
        }
        return c;
    }

    /**
       *
       */
    protected java.lang.Object createObjectFromClass(Class c, Scope locals) throws BishopException {
        String classname = c.getName();
        java.lang.Object o = null;
        if (classname.equals("int")) o = new Integer(0); else if (classname.equals("boolean")) o = new Boolean("true"); else if (classname.equals("byte")) o = new Byte((byte) 0); else if (classname.equals("char")) o = new Character((char) 0); else if (classname.equals("double")) o = new Double(0); else if (classname.equals("float")) o = new Float(0); else if (classname.equals("long")) o = new Long(0); else if (classname.equals("short")) o = new Short((short) 0); else {
            try {
                java.lang.Class[] sig = new java.lang.Class[ins.size()];
                java.lang.Object[] arg = new java.lang.Object[ins.size()];
                for (int i = 0; i < ins.size(); i++) {
                    In in = (In) ins.get(i);
                    java.lang.Object obj = locals.getObject(in);
                    sig[i] = obj.getClass();
                    arg[i] = obj;
                }
                o = c.getConstructor(sig).newInstance(arg);
            } catch (InstantiationException e) {
                throw new BishopException(e, this, "Object element cant instantiate arrays or interfaces: " + c.getName());
            } catch (NoSuchMethodException e) {
                throw new BishopException(e, this, "Object could not be created, no matching constructor was found.");
            } catch (Exception e) {
                throw new BishopException(e, this, "Failed to create object: " + e);
            }
        }
        return o;
    }

    public void addChild(BishopEntity child) {
        if (child instanceof In) {
            ins.add(child);
        }
    }
}
