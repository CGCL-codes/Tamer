package alice.tuprologx.pj.lib;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Finds methods and constructors that can be invoked by reflection.
 * Attempts to address some of the limitations of the JDK's
 * Class.getMethod() and Class.getConstructor(), and other JDK
 * reflective facilities.
 */
public final class MethodFinder {

    /** The target class to look for methods and constructors in. */
    private final Class clazz;

    /** Mapping from method name to the Methods in the target class with that name. */
    private final Map methodMap = new HashMap();

    /** List of the Constructors in the target class. */
    private final List ctorList = new ArrayList();

    /** Mapping from a Constructor or Method object to the Class objects representing its formal parameters. */
    private final Map paramMap = new HashMap();

    /**
     * @param  clazz  Class in which I will look for methods and constructors
     * throws  IllegalArgumentException if clazz is null, or represents a primitive, or represents an array type
     */
    public MethodFinder(final Class clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("null Class parameter");
        }
        if (clazz.isPrimitive()) {
            throw new IllegalArgumentException("primitive Class parameter");
        }
        if (clazz.isArray()) {
            throw new IllegalArgumentException("array Class parameter");
        }
        this.clazz = clazz;
        loadMethods();
        loadConstructors();
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        } else {
            MethodFinder other = (MethodFinder) o;
            return clazz.equals(other.clazz);
        }
    }

    /**
     * Returns the most specific public constructor in my target class that
     * accepts the number and type of parameters in the given Class array in a
     * reflective invocation. <p> A null value or Void.TYPE in parameterTypes
     * matches a corresponding Object or array reference in a constructor's
     * formal parameter list, but not a primitive formal parameter.
     *
     * @param parameterTypes array representing the number and types of
     *            parameters to look for in the constructor's signature. A null
     *            array is treated as a zero-length array.
     * @return Constructor object satisfying the conditions
     * @throws NoSuchMethodException if no constructors match the criteria,
     *                or if the reflective call is ambiguous based on the
     *                parameter types
     */
    public Constructor findConstructor(final Class[] parameterTypes) throws NoSuchMethodException {
        return (Constructor) findMemberIn(ctorList, parameterTypes == null ? new Class[0] : parameterTypes);
    }

    /**
     * Basis of findConstructor() and findMethod(). The member list fed to this
     * method will be either all Constructor objects or all Method objects.
     */
    private Member findMemberIn(final List memberList, final Class[] parameterTypes) throws NoSuchMethodException {
        List matchingMembers = new ArrayList();
        for (Iterator it = memberList.iterator(); it.hasNext(); ) {
            Member member = (Member) it.next();
            Class[] methodParamTypes = (Class[]) paramMap.get(member);
            if (Arrays.equals(methodParamTypes, parameterTypes)) {
                return member;
            }
            if (ClassUtilities.compatibleClasses(methodParamTypes, parameterTypes)) {
                matchingMembers.add(member);
            }
        }
        if (matchingMembers.isEmpty()) {
            throw new NoSuchMethodException("no member in " + clazz.getName() + " matching given args");
        }
        if (matchingMembers.size() == 1) {
            return (Member) matchingMembers.get(0);
        }
        return findMostSpecificMemberIn(matchingMembers);
    }

    /**
     * Returns the most specific public method in my target class that has the
     * given name and accepts the number and type of parameters in the given
     * Class array in a reflective invocation. <p> A null value or Void.TYPE in
     * parameterTypes will match a corresponding Object or array reference in a
     * method's formal parameter list, but not a primitive formal parameter.
     *
     * @param methodName name of the method to search for
     * @param parameterTypes array representing the number and types of
     *            parameters to look for in the method's signature. A null array
     *            is treated as a zero-length array.
     * @return Method object satisfying the conditions
     * @throws NoSuchMethodException if no methods match the criteria, or if
     *                the reflective call is ambiguous based on the parameter
     *                types, or if methodName is null
     */
    public Method findMethod(final String methodName, final Class[] parameterTypes) throws NoSuchMethodException {
        List methodList = (List) methodMap.get(methodName);
        if (methodList == null) {
            throw new NoSuchMethodException("no method named " + clazz.getName() + "." + methodName);
        }
        return (Method) findMemberIn(methodList, parameterTypes == null ? new Class[0] : parameterTypes);
    }

    /**
     * @param a List of Members (either all Constructors or all Methods)
     * @return the most specific of all Members in the list
     * @throws NoSuchMethodException if there is an ambiguity as to which is
     *                most specific
     */
    private Member findMostSpecificMemberIn(final List memberList) throws NoSuchMethodException {
        List mostSpecificMembers = new ArrayList();
        for (Iterator memberIt = memberList.iterator(); memberIt.hasNext(); ) {
            Member member = (Member) memberIt.next();
            if (mostSpecificMembers.isEmpty()) {
                mostSpecificMembers.add(member);
            } else {
                boolean moreSpecific = true;
                boolean lessSpecific = false;
                for (Iterator specificIt = mostSpecificMembers.iterator(); specificIt.hasNext(); ) {
                    Member moreSpecificMember = (Member) specificIt.next();
                    if (!memberIsMoreSpecific(member, moreSpecificMember)) {
                        moreSpecific = false;
                        lessSpecific = memberIsMoreSpecific(moreSpecificMember, member);
                        break;
                    }
                }
                if (moreSpecific) {
                    mostSpecificMembers.clear();
                    mostSpecificMembers.add(member);
                } else if (!lessSpecific) {
                    mostSpecificMembers.add(member);
                }
            }
        }
        if (mostSpecificMembers.size() > 1) {
            throw new NoSuchMethodException("Ambiguous request for member in " + this.clazz.getName() + " matching given args");
        }
        return (Member) mostSpecificMembers.get(0);
    }

    /**
     * @param args an Object array
     * @return an array of Class objects representing the classes of the objects
     *         in the given Object array. If args is null, a zero-length Class
     *         array is returned. If an element in args is null, then Void.TYPE
     *         is the corresponding Class in the return array.
     */
    public static Class[] getParameterTypesFrom(final Object[] args) {
        if (args == null) {
            return new Class[0];
        }
        Class[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; ++i) {
            argTypes[i] = (args[i] == null) ? Void.TYPE : args[i].getClass();
        }
        return argTypes;
    }

    /**
     * @param classNames String array of fully qualified names (FQNs) of classes
     *            or primitives. Represent an array type by using its JVM type
     *            descriptor, with dots instead of slashes (e.g. represent the
     *            type int[] with "[I", and Object[][] with
     *            "[[Ljava.lang.Object;").
     * @return an array of Class objects representing the classes or primitives
     *         named by the FQNs in the given String array. If the String array
     *         is null, a zero-length Class array is returned. If an element in
     *         classNames is null, the empty string, "void", or "null", then
     *         Void.TYPE is the corresponding Class in the return array. If any
     *         classes require loading because of this operation, the loading is
     *         done by the ClassLoader that loaded this class. Such classes are
     *         not initialized, however.
     * @throws ClassNotFoundException if any of the FQNs name an unknown
     *                class
     */
    public static Class[] getParameterTypesFrom(final String[] classNames) throws ClassNotFoundException {
        return getParameterTypesFrom(classNames, MethodFinder.class.getClassLoader());
    }

    /**
     * @param classNames String array of fully qualified names (FQNs) of classes
     *            or primitives. Represent an array type by using its JVM type
     *            descriptor, with dots instead of slashes (e.g. represent the
     *            type int[] with "[I", and Object[][] with
     *            "[[Ljava.lang.Object;").
     * @param loader a ClassLoader
     * @return an array of Class objects representing the classes or primitives
     *         named by the FQNs in the given String array. If the String array
     *         is null, a zero-length Class array is returned. If an element in
     *         classNames is null, the empty string, "void", or "null", then
     *         Void.TYPE is the corresponding Class in the return array. If any
     *         classes require loading because of this operation, the loading is
     *         done by the given ClassLoader. Such classes are not initialized,
     *         however.
     * @throws ClassNotFoundException if any of the FQNs name an unknown
     *                class
     */
    public static Class[] getParameterTypesFrom(final String[] classNames, final java.lang.ClassLoader loader) throws ClassNotFoundException {
        if (classNames == null) {
            return new Class[0];
        }
        Class[] types = new Class[classNames.length];
        for (int i = 0; i < classNames.length; ++i) {
            types[i] = ClassUtilities.classForNameOrPrimitive(classNames[i], loader);
        }
        return types;
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return clazz.hashCode();
    }

    /**
     * Loads up the data structures for my target class's constructors.
     */
    private void loadConstructors() {
        Constructor[] ctors = clazz.getConstructors();
        for (int i = 0; i < ctors.length; ++i) {
            ctorList.add(ctors[i]);
            paramMap.put(ctors[i], ctors[i].getParameterTypes());
        }
    }

    /**
     * Loads up the data structures for my target class's methods.
     */
    private void loadMethods() {
        List allMethods = getAllMethods();
        Method[] methods = (Method[]) allMethods.toArray(new Method[allMethods.size()]);
        for (int i = 0; i < methods.length; ++i) {
            Method m = methods[i];
            String methodName = m.getName();
            Class[] paramTypes = m.getParameterTypes();
            List list = (List) methodMap.get(methodName);
            if (list == null) {
                list = new ArrayList();
                methodMap.put(methodName, list);
            }
            if (!ClassUtilities.classIsAccessible(clazz)) {
                m = ClassUtilities.getAccessibleMethodFrom(clazz, methodName, paramTypes);
            }
            if (m != null) {
                list.add(m);
                paramMap.put(m, paramTypes);
            }
        }
    }

    private List getAllMethods() {
        List allMethods = new ArrayList();
        Class c = clazz;
        while ((c != null)) {
            Method[] methods = c.getDeclaredMethods();
            List list = null;
            if (methods != null) {
                list = Arrays.asList(methods);
            }
            if (list != null) {
                allMethods.addAll(list);
            }
            c = c.getSuperclass();
        }
        return allMethods;
    }

    /**
     * @param  first  a Member
     * @param  second  a Member
     * @return  true if the first Member is more specific than the second,
     * false otherwise.  Specificity is determined according to the
     * procedure in the Java Language Specification, section 15.12.2.
     */
    private boolean memberIsMoreSpecific(final Member first, final Member second) {
        Class[] firstParamTypes = (Class[]) paramMap.get(first);
        Class[] secondParamTypes = (Class[]) paramMap.get(second);
        return ClassUtilities.compatibleClasses(secondParamTypes, firstParamTypes);
    }
}
