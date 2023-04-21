package com.ibm.realtime.analysis.generic;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import com.ibm.realtime.analysis.AnalysisException;
import com.ibm.realtime.analysis.Bytecode;
import com.ibm.realtime.analysis.MethodWrapper;

/**
 * An implementation of MethodWrapper functionality that uses a generic class inspection package
 *  (BCEL at present) instead of J9-specific native methods to obtain bytecodes.  This is suitable
 *  for development-time testing of Eventron validity but not for runtime validation.
 */
public class GenericMethodWrapper extends MethodWrapper {

    private org.apache.bcel.classfile.Method bcelMethod;

    private Member jlMethod;

    private byte[] bytecodes;

    private Class[] exceptionTypes;

    /** The ClassLoader used to load classes */
    private ClassLoader cl;

    /**
   * Create a GenericMethodWrapper from a java.lang.reflect.Member object representing a
   *   method or constructor (not a field!)
   * @param fromMember the Member object representing the method or constructor to be wrapped
   * @throws AnalysisException if the wrapper could not be created for any reason
   */
    public GenericMethodWrapper(Member fromMember) throws AnalysisException {
        jlMethod = fromMember;
        cl = fromMember.getDeclaringClass().getClassLoader();
        JavaClass defcls;
        try {
            defcls = Repository.lookupClass(fromMember.getDeclaringClass());
        } catch (ClassNotFoundException e) {
            defcls = null;
        }
        if (defcls == null) {
            throw new AnalysisException("Classfile could not be resolved for " + fromMember.getDeclaringClass().getName());
        }
        bcelMethod = getMethodFromMember(defcls, fromMember);
        if (bcelMethod == null) {
            throw new AnalysisException("Method " + fromMember.getName() + " not found in classfile for " + defcls.getClassName());
        }
        Code bcode = bcelMethod.getCode();
        if (bcode == null) {
            bytecodes = new byte[0];
        } else {
            bytecodes = bcode.getCode();
        }
        if (jlMethod instanceof Method) {
            exceptionTypes = ((Method) jlMethod).getExceptionTypes();
        } else if (jlMethod instanceof Constructor) {
            exceptionTypes = ((Constructor) jlMethod).getExceptionTypes();
        }
    }

    /**
   * Create a GenericMethodWrapper from parts
   * @param definingClass the Class in which the method is defined
   * @param name the name of the method
   * @param signature the (JVM-format) signature of the method
   * @param isStatic true if the method is expected to be static, false
   *    otherwise
   * @throws AnalysisException if the method is not found or does not match expectations
   */
    public GenericMethodWrapper(Class definingClass, String name, String signature, boolean isStatic) throws AnalysisException {
        this(getJLMember(definingClass, name, signature, isStatic));
    }

    /**
   * Turn a parts description of a method into its java.lang.reflect.Method or
   *   java.lang.reflect.Constructor, if possible
   * @param definingClass the Class in which the method is defined
   * @param name the name of the method (&lt;init&gt; for a constructor)
   * @param signature the (JVM-format) signature of the method or constructor
   * @param isStatic true if the method is expected to be static, false
   *   otherwise
   * @return the corresponding jlrMethod or jlrConstructor
   * @throws AnalysisException if the method can't be found or violates expectations
   */
    private static Member getJLMember(Class definingClass, String name, String signature, boolean isStatic) throws AnalysisException {
        Member[] members;
        if ("<init>".equals(name)) {
            members = definingClass.getDeclaredConstructors();
        } else {
            List memberList = new ArrayList();
            getAllDeclaredMethods(definingClass, memberList);
            members = (Member[]) memberList.toArray(new Member[0]);
        }
        for (int i = 0; i < members.length; i++) {
            Member m = members[i];
            String sig;
            if (m instanceof Constructor) {
                sig = toSignature((Constructor) m);
            } else {
                sig = toSignature((Method) m);
                if (!m.getName().equals(name)) {
                    continue;
                }
            }
            if (sig.equals(signature)) {
                boolean reallyIsStatic = (m.getModifiers() & Modifier.STATIC) != 0;
                if (isStatic == reallyIsStatic) {
                    return m;
                } else {
                    throw new AnalysisException("Method " + name + (isStatic ? " should be static but isn't" : " shouldn't be static but is"));
                }
            }
        }
        throw new AnalysisException("Method " + name + " could not be resolved");
    }

    /**
   * Subroutine to obtain all the members of a class, including inherited members and abstract members
   * @param definingClass the defining class
   * @param members a List in which to accumulate the members (starts out empty but is passed to
   *    recursive invocations of this method)
   */
    private static void getAllDeclaredMethods(Class definingClass, List members) {
        Member[] newMembers = definingClass.getDeclaredMethods();
        for (int i = 0; i < newMembers.length; i++) {
            members.add(newMembers[i]);
        }
        Class superclass = definingClass.getSuperclass();
        if (superclass != null) {
            getAllDeclaredMethods(superclass, members);
        }
        Class[] superInterfaces = definingClass.getInterfaces();
        for (int i = 0; i < superInterfaces.length; i++) {
            getAllDeclaredMethods(superInterfaces[i], members);
        }
    }

    /**
   * Returns the BCEL method wrapped by this instance.
   * @return the BCEL method wrapped by this instance.
   */
    public org.apache.bcel.classfile.Method getBCELMethod() {
        return bcelMethod;
    }

    /**
   * Get a BCEL method from a java.lang.reflect.Member (Constructor or Method)
   * @param defcls the JavaClass object from which the BCEL method will be drawn
   * @param fromMember the Member describing what is wanted at the java.lang.reflect level
   * @return a value BCEL method or null if cannot be resolved
   */
    private org.apache.bcel.classfile.Method getMethodFromMember(JavaClass defcls, Member fromMember) {
        if (fromMember instanceof Method) {
            return defcls.getMethod((Method) fromMember);
        }
        org.apache.bcel.classfile.Method[] allMethods = defcls.getMethods();
        String sig = toSignature((Constructor) fromMember);
        for (int i = 0; i < allMethods.length; i++) {
            org.apache.bcel.classfile.Method method = allMethods[i];
            if (method.getName().equals("<init>") && method.getSignature().equals(sig)) {
                return method;
            }
        }
        return null;
    }

    public Collection getBytecodes() throws AnalysisException {
        return new GenericBytecodes(bytecodes, bcelMethod.getConstantPool(), cl);
    }

    public Class getMethodClass() {
        return jlMethod.getDeclaringClass();
    }

    public MethodWrapper getImplementation(Class clazz) {
        if (clazz == getMethodClass()) return this;
        GenericMethodWrapper ans = null;
        if (jlMethod instanceof Constructor) {
            return ans;
        }
        if (getMethodClass().isAssignableFrom(clazz)) {
            try {
                ans = new GenericMethodWrapper(clazz.getDeclaredMethod(jlMethod.getName(), (Class[]) ((Method) jlMethod).getParameterTypes()));
            } catch (AnalysisException e) {
            } catch (SecurityException e) {
            } catch (NoSuchMethodException e) {
            }
        }
        return ans;
    }

    public boolean isSynchronized() {
        return ((jlMethod.getModifiers() & Modifier.SYNCHRONIZED) != 0);
    }

    public boolean isNative() {
        return ((jlMethod.getModifiers() & Modifier.NATIVE) != 0);
    }

    public boolean isStatic() {
        return ((jlMethod.getModifiers() & Modifier.STATIC) != 0);
    }

    public String getMethodName() {
        return jlMethod.getName();
    }

    public String getMethodSignature() {
        if (jlMethod instanceof Method) {
            return toSignature((Method) jlMethod);
        } else {
            return toSignature((Constructor) jlMethod);
        }
    }

    public int getLineNumber(Bytecode bc) {
        return bcelMethod.getCode().getLineNumberTable().getSourceLine(bc.getPosition());
    }

    public Class[] getParameterTypes() throws AnalysisException {
        if (jlMethod instanceof Constructor) {
            Constructor c = (Constructor) jlMethod;
            return c.getParameterTypes();
        } else {
            Method m = (Method) jlMethod;
            return m.getParameterTypes();
        }
    }

    /**
   * Returns an array of <code>Class</code> objects representing
   * the exception types of the method represented by the instance
   * of this wrapper, or an empty array, if method throws no 
   * exceptions.
   * @return
   */
    public Class[] getExceptionTypes() {
        return exceptionTypes;
    }

    public Class getReturnType() throws AnalysisException {
        if (jlMethod instanceof Constructor) {
            return jlMethod.getDeclaringClass();
        } else {
            Method m = (Method) jlMethod;
            return m.getReturnType();
        }
    }
}
