package tudresden.ocl.check.types;

import tudresden.ocl.check.*;
import java.util.HashMap;
import java.lang.reflect.*;
import tudresden.ocl.lib.NameAdapter;

/** <code>ReflectionFacade</code> implements <code>ModelFacade</code>
 *  by extracting the required
 *  information from compiled Java classes via Java Reflection.
 *  The package name where classes are to be found is given as constructor
 *  parameter, along with a <code>ReflectionAdapter</code>
 *  object that is used
 *  to map Java classes to OCL types and vice versa.
 *  Classes are then loaded using the
 *  standard Java class loader.
 *
 *  <p>While this approach is very flexible, it has some serious drawbacks. Association
 *  ends with a multiplicity greater than one are usually represented in Java
 *  classes as some Java collection type, e.g.
 *  <code>java.util.Vector</code>.
 *  Through static examination of the class it is not possible to find out the
 *  element type of the OCL collection type that is the result of navigation
 *  along this association in an OCL constraint. Hence the iterator type can not be
 *  determined whenever the expression contains a call to one of the iterating
 *  methods, and type checking will fail unless the iterator type is specifies
 *  explicitly.
 *
 *  <p>For the same reason it is not possible to type-check the properties
 *  <code>first</code> and <code>last</code> defined for the OCL type
 *  Sequence.
 *
 *  <p>Another problem with getting model information through reflection is that
 *  the security manager might deny access to non-public fields.
 *
 *  <p>In addition, the mapping of OCL types to Java types is usually not a
 *  homomorphism. For further explanation, see the description of the method
 *  <code>navigate(String name, Type[] params)</code>.
 *
 *  <p>As the result of these restrictions, the class <code>ReflectionFacade</code> can
 *  only be used if all iterators and their types are declared explicitly.
 *  Additional problems might arise from security restrictions.
 * 
 *  Additionally, ReflectionFacade provides a default implementation
 *  for ReflectionExtender, which simply does nothing.
 *
 *  @see ClassAny#navigateParameterized(String name, Type[] params)
 *  @see ReflectionAdapter
 *
 *  @author Frank Finger
 *
 */
public class ReflectionFacade implements ModelFacade {

    String[] packageNames;

    NameAdapter nameAdapter;

    ReflectionAdapter reflAdapter;

    /** this map is not static since different ReflectionFacades should
   *  use their own type representations as these are inner class objects,
   *  therefore dependent on their outer objects, which are the
   *  ReflectionFacades
   */
    protected HashMap classAnyInstances = new HashMap();

    private ReflectionExtender extender;

    /** @param packageNames the names of the Java packages that contain the
   *              classes that will be queried for model information, without
   *              trailing dot character (&quot;.&quot;);
   *              <code>null</code> or the empty string denote the root
   *              package (containing all classes not assign to a package
   *              through Java's package statement); when a class is searched
   *              for the package names are tried in their order in
   *              the array
   *  @param reflAdapter maps OCL types to Java types and vice versa
   *  @param nameAdapter maps OCL names to Java names and vice versa; see explanation
   *              in NameAdapter documentation
   */
    public ReflectionFacade(String[] packageNames, ReflectionAdapter reflAdapter, NameAdapter nameAdapter, ReflectionExtender extender) {
        for (int i = 0; i < packageNames.length; i++) {
            if (packageNames[i] == null || packageNames[i].equals("")) packageNames[i] = null;
        }
        this.packageNames = packageNames;
        this.reflAdapter = reflAdapter;
        this.nameAdapter = nameAdapter;
        this.extender = extender;
    }

    public ReflectionFacade(String[] packageNames, ReflectionAdapter reflAdapter, NameAdapter nameAdapter) {
        this(packageNames, reflAdapter, nameAdapter, null);
    }

    public Any getClassifier(String name) {
        ClassAny ret = null;
        int doublecolonindex = name.indexOf("::");
        if (doublecolonindex >= 0) {
            System.out.println("dirty hack in reflectionfacade: " + name);
            name = name.substring(doublecolonindex + 2, name.length()).trim();
            System.out.println("dirty hack in reflectionfacade: " + name);
        }
        for (int i = 0; i < packageNames.length && ret == null; i++) {
            try {
                String className;
                if (packageNames[i] == null) {
                    className = name;
                } else {
                    className = packageNames[i] + "." + name;
                }
                Class c = Class.forName(className, false, getClass().getClassLoader());
                ret = getClassAny(c);
            } catch (ClassNotFoundException cnf) {
            }
        }
        if (ret == null) {
            StringBuffer sb = new StringBuffer();
            sb.append("ReflectionFacade could not find class " + name + " in packages ");
            for (int i = 0; i < packageNames.length; i++) {
                if (i != 0) sb.append(", ");
                sb.append(packageNames[i]);
            }
            throw new OclTypeException(sb.toString());
        }
        return ret;
    }

    protected ClassAny getClassAny(Class c) {
        ClassAny ret = (ClassAny) classAnyInstances.get(c);
        if (ret == null) {
            ret = new ClassAny(c, this);
            classAnyInstances.put(c, ret);
        }
        return ret;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(getClass().getName());
        buf.append('(');
        buf.append('[');
        for (int i = 0; i < packageNames.length; i++) {
            if (i > 0) buf.append(',');
            buf.append(packageNames[i]);
        }
        buf.append(']');
        buf.append(',');
        buf.append(reflAdapter.toString());
        buf.append(',');
        buf.append(nameAdapter.toString());
        buf.append(',');
        buf.append(extender);
        buf.append(')');
        return buf.toString();
    }

    protected Class getElementType(Field f) {
        Class fieldtype = f.getType();
        if (fieldtype.isArray()) return fieldtype.getComponentType();
        return (extender != null) ? extender.getElementType(f) : null;
    }

    protected Class getKeyType(Field f) {
        return (extender != null) ? extender.getKeyType(f) : null;
    }
}
