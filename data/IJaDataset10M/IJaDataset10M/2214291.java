package mirrormonkey.util.annotations.parsing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import mirrormonkey.util.annotations.control.AnnotationOverride;

/**
 * A <tt>MemberDataIR</tt> that will match methods with a given signature.<br>
 * 
 * This class will basically collect every method and its annotations that
 * overrides the method that it was initialized with.
 * 
 * @author Philipp Christian Loewner
 * 
 */
public class MethodIR extends MemberIR {

    /**
	 * The latest methods matching the contained signature in the class
	 * hierarchy.
	 */
    protected Method collectedMethod;

    /**
	 * Return type that is required for methods to match
	 */
    protected Class<?> returnType;

    /**
	 * Parameter list that methods must have to match this <tt>MethodIR</tt>
	 */
    protected Class<?>[] params;

    /**
	 * Parameter annotations
	 */
    @SuppressWarnings("rawtypes")
    protected Map[] paramAnnotations;

    /**
	 * Name that methods must have to match this <tt>MethodIR</tt>
	 */
    protected String name;

    /**
	 * This class will only collect methods.
	 */
    public static final Class<?>[] LISTEN_CLASSES = { Method.class };

    /**
	 * Creates a new <tt>MethodIR</tt>. If this constructor is called, then this
	 * <tt>MethodIR</tt> is the first <tt>MemberDataIR</tt> for a method.
	 * 
	 * @param parser
	 *            the <tt>AnnotationParser</tt> that parses the class hierarchy
	 *            and creates this <tt>MethodIR</tt>
	 * @param member
	 *            the <tt>Method</tt> that this <tt>MethodIR</tt> is created for
	 */
    public MethodIR(AnnotationParser parser, Method member) {
        this(parser, member.getReturnType(), member.getParameterTypes(), member.getName(), member);
        parseMethod(member);
    }

    /**
	 * Creates a new <tt>MethodIR</tt> that will collect methods matching the
	 * provided signature. This constructor can be called if some kind of method
	 * should be collected, but has not yet been encountered.
	 * 
	 * @param parser
	 *            the <tt>AnnotationParser</tt> that parses the class hierarchy
	 *            and creates this <tt>MethodIR</tt>
	 * @param returnType
	 *            the return type that <tt>Methods</tt> need to have in order to
	 *            match this <tt>MethodIR</tt>
	 * @param params
	 *            the parameter list that <tt>Methods</tt> need to have in order
	 *            to match this <tt>MethodIR</tt>
	 * @param name
	 *            the name that <tt>Methods</tt> need to have in order to match
	 *            this <tt>MethodIR</tt>
	 */
    public MethodIR(AnnotationParser parser, Class<?> returnType, Class<?>[] params, String name) {
        this(parser, returnType, params, name, null);
    }

    /**
	 * Creates a new <tt>MethodIR</tt>.
	 * 
	 * @param parser
	 *            the <tt>AnnotationParser</tt> that parses the class hierarchy
	 *            and creates this <tt>MethodIR</tt>
	 * @param returnType
	 *            the return type that <tt>Methods</tt> need to have in order to
	 *            match this <tt>MethodIR</tt>
	 * @param params
	 *            the parameter list that <tt>Methods</tt> need to have in order
	 *            to match this <tt>MethodIR</tt>
	 * @param name
	 *            the name that <tt>Methods</tt> need to have in order to match
	 *            this <tt>MethodIR</tt>
	 * @param member
	 *            the <tt>Method</tt> that this <tt>MethodIR</tt> should be
	 *            created for or <tt>null</tt> if the <tt>Method</tt> is not
	 *            known yet and has to be collected
	 */
    private MethodIR(AnnotationParser parser, Class<?> returnType, Class<?>[] params, String name, Method member) {
        super(parser, member);
        this.returnType = returnType;
        this.params = params;
        this.name = name;
        paramAnnotations = new HashMap<?, ?>[params.length];
        for (int i = 0; i < params.length; i++) {
            paramAnnotations[i] = new HashMap<Object, Object>();
        }
    }

    /**
	 * Creates a new <tt>MethodIR</tt> that copies previously collected data
	 * from the previous <tt>MethodIR</tt>. This constructor should be called by
	 * inheriting classes to convert IR classes into one another.
	 * 
	 * @param previous
	 *            the previous <tt>MethodIR</tt> that matched <tt>member</tt>
	 * @param member
	 *            the <tt>Member</tt> for which this <tt>MethodIR</tt> is
	 *            created
	 */
    public MethodIR(MethodIR previous, Method member) {
        super(previous, member);
        this.paramAnnotations = previous.paramAnnotations;
        parseMethod(member);
    }

    /**
	 * Returns the <tt>Method</tt> that this <tt>MethodIR</tt> has collected.
	 * This will be the last method in the class hierarchy that matched the
	 * contained signature.
	 * 
	 * @return the last method that matched this <tt>MethodIR</tt>
	 */
    public Method getCollectedMethod() {
        return collectedMethod;
    }

    /**
	 * Sets the collected method.
	 * 
	 * @param collectedMethod
	 *            <tt>Method</tt> to collect
	 */
    public void setCollectedMethod(Method collectedMethod) {
        this.collectedMethod = collectedMethod;
        returnType = collectedMethod.getReturnType();
        params = collectedMethod.getParameterTypes();
        name = collectedMethod.getName();
    }

    /**
	 * Parses <tt>m's</tt> parameter annotations. Basically, all parameter
	 * annotations will be collected. If an <tt>AnnotationOverride</tt> is
	 * detected, every previously collected annotation is lost.
	 * 
	 * @param m
	 *            <tt>Method</tt> to parse parameters of
	 */
    @SuppressWarnings("unchecked")
    public void parseParameterAnnotations(Method m) {
        Annotation[][] annotations = m.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            for (Annotation a : annotations[i]) {
                if (a.annotationType().equals(AnnotationOverride.class)) {
                    paramAnnotations[i].clear();
                }
            }
            for (Annotation a : annotations[i]) {
                if (!a.annotationType().equals(AnnotationOverride.class)) {
                    paramAnnotations[i].put(a.annotationType(), a);
                }
            }
        }
    }

    /**
	 * Returns an array of <tt>Maps</tt> reflecting the collected annotations
	 * for each parameter.
	 * 
	 * @return <tt>Map</tt> of all collected parameter annotations. This will be
	 *         a <tt>Map<Class<? extends Annotation>, Annotation></tt>, but java
	 *         does not permit returning a generic <tt>Map</tt> array due to
	 *         type consistency issues with generic arrays.
	 */
    @SuppressWarnings("rawtypes")
    public Map[] getParameterAnnotations() {
        return paramAnnotations;
    }

    @Override
    public boolean matches(Member member) {
        if (!Method.class.isInstance(member)) {
            return false;
        }
        Method method = (Method) member;
        return returnType.equals(method.getReturnType()) && Arrays.deepEquals(params, method.getParameterTypes()) && name.equals(method.getName());
    }

    @Override
    public void parseField(Field f) {
        throw new RuntimeException(getClass() + " cannot parse Fields.");
    }

    @Override
    public void parseMethod(Method m) {
        if (collectedMethod == null) {
            setCollectedMethod(m);
        } else {
            collectedMethod = m;
        }
        parseParameterAnnotations(m);
        parseAnnotatedElement(m);
    }

    @Override
    public void parseConstructor(Constructor<?> constr) {
        throw new RuntimeException(getClass() + " cannot parse Constructors.");
    }

    @Override
    public String toString() {
        return "\n[" + getClass() + ": \n  collectedMethod: " + collectedMethod + "\n  collectedAnnotations: " + collectedAnnotations + "]";
    }

    @Override
    public Class<?>[] getListenMemberClasses() {
        return LISTEN_CLASSES;
    }
}
