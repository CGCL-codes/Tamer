package tudresden.ocl.injection;

import java.io.*;
import java.util.*;
import java.lang.reflect.Modifier;

/**
   Represents a java feature.
   May be a class (even an inner class), an attribute or
   a method.
*/
public abstract class JavaFeature {

    /**
     The java file, which contains this feature.
     Must not be null.
  */
    private JavaFile file;

    /**
     The class, which contains this feature.
     Is null for top-level (not inner) classes.
  */
    private JavaClass parent;

    /**
     The modifiers of this feature.
     @see java.lang.reflect.Modifier
  */
    private int modifiers;

    /**
     The return type of the method.
     Is null, if it is a constructor, or a class.
  */
    protected String type;

    protected String name;

    public JavaFeature(JavaFile file, JavaClass parent, int modifiers, String type, String name) throws InjectorParseException {
        this.file = file;
        this.parent = parent;
        this.modifiers = modifiers;
        this.type = type;
        this.name = name;
        if (file == null) throw new RuntimeException();
        if (parent != null && file != parent.getFile()) throw new RuntimeException();
        int over = modifiers & ~getAllowedModifiers();
        if (over != 0) throw new InjectorParseException("modifier(s) " + java.lang.reflect.Modifier.toString(over) + " not allowed for class feature " + name + " of type " + getClass().getName() + '.');
    }

    /**
     Returns the java file, which contains this feature.
     Is never null.
  */
    public final JavaFile getFile() {
        return file;
    }

    /**
     Returns the package of the file containing this feature.
  */
    public final String getPackageName() {
        return file.getPackageName();
    }

    /**
     Returns the class, which contains this feature.
     Is null for top-level (not inner) classes.
  */
    public final JavaClass getParent() {
        return parent;
    }

    /**
     Returns the modifiers of this feature.
     @see java.lang.reflect.Modifier
  */
    public final int getModifiers() {
        return modifiers;
    }

    /**
     Subclasses use this method to specify,
     which modifiers are allowed for the specific kind
     of feature.
  */
    public abstract int getAllowedModifiers();

    public final boolean isStatic() {
        return (modifiers & Modifier.STATIC) > 0;
    }

    public final boolean isAbstract() {
        return (modifiers & Modifier.ABSTRACT) > 0;
    }

    /**
     The return type of the method.
     Is null, if it is a constructor, or a class.
  */
    public final String getType() {
        return type;
    }

    public final String getName() {
        return name;
    }

    public final void print(PrintStream o) {
        o.println("  " + JavaFile.extractClassName(getClass().getName()) + " (" + Modifier.toString(modifiers) + ") >" + type + "< >" + name + "< in >" + (parent == null ? "none" : parent.getName()) + "<");
        printMore(o);
    }

    public void printMore(PrintStream o) {
    }
}
