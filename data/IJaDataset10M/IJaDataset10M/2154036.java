package jaxlib.reflect;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ReflectPermission;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import javax.annotation.Nonnull;
import jaxlib.lang.AccessLevel;
import jaxlib.lang.Referencing;
import jaxlib.lang.StackTraces;
import jaxlib.util.Strings;

/**
 *
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: JavaElement.java 3039 2012-01-13 09:45:24Z joerg_wassmer $
 */
@SuppressWarnings("unchecked")
public abstract class JavaElement extends Object implements Serializable {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    static final Permission ACCESS_PERMISSION = new ReflectPermission("suppressAccessChecks");

    static final Annotation[] NO_ANNOTATIONS = new Annotation[0];

    static final int PUBLIC_STATIC_FINAL = Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL;

    /**
   * Resolve the Java element referenced by the named field declared in the class calling this method.
   * If the referenced element is a field, constructor or method then it will be
   * {@link AccessibleObject#setAccessible(boolean) set accessible}.
   * <p>
   * The referenced element either has to be accessible from the class calling this method, or the caller
   * must have the security privileges to access it via reflection.
   * </p><p>
   * <h3>Example</h3><br/>
   * <pre>
   * class Foo
   * {
   *   &#64;jaxlib.lang.Referencing(declaring=Object.class, method="hashCode")
   *   private static final Method objectHashCode = init("objectHashCode");
   *
   *   &#64;jaxlib.lang.Referencing(declaring=Object.class, method="equals", parameters=Object.class)
   *   private static final JavaMethod&lt;Boolean> objectEquals = init("objectEquals");
   * }
   * </pre>
   *
   * @param annotatedFieldName
   *  the name of a field declared in the caller class, annotated with {@link Referencing}.
   *
   * @return
   *  depending on the referenced element and the type of the field annotated with {@link Referencing} an instance of
   *  one of the following classes:
   *  <ul>
   *  <li>{@link java.lang.Package}</li>
   *  <li>{@link java.lang.Class}</li>
   *  <li>{@link java.lang.reflect.Field}</li>
   *  <li>{@link java.lang.reflect.Constructor}</li>
   *  <li>{@link java.lang.reflect.Method}</li>
   *  <li>{@link JavaPackage}</li>
   *  <li>{@link JavaClass}</li>
   *  <li>{@link JavaField}</li>
   *  <li>{@link JavaConstructor}</li>
   *  <li>{@link JavaMethod}</li>
   *  <li>
   *    or {@code null}, in case the referenced element is not existing, and
   *    {@link Referencing#optional() Referencing.optional} is {@code true}.
   * </li>
   *  </ul>
   *
   * @throws IllegalArgumentException
   *   <ul>
   *   <li>if the class calling this method is not declaring the named field.</li>
   *   <li>if the named field is not annotated with {@link Referencing}.</li>
   *   <li>if the referenced Java element is not existing and {@link Referencing#optional()} is {@code false}.</li>
   *   </ul>
   * @throws SecurityException
   *   if the referenced element is a field, constructor or method and the caller has not the privileges to access it.
   *
   * @since JaXLib 1.0
   */
    public static <E> E init(final String annotatedFieldName) {
        return (E) init(JavaClass.of(StackTraces.getCallerClass()), annotatedFieldName, true);
    }

    static <E> E init(final JavaClass<?> caller, final String annotatedFieldName, final boolean unwrap) {
        if (caller == null) throw new IllegalStateException("unable to determine caller class");
        final InitReferencing init = new InitReferencing(caller, annotatedFieldName, true, unwrap);
        final SecurityManager sm = System.getSecurityManager();
        if (sm == null) init.run(); else {
            AccessController.doPrivileged(init, new AccessControlContext(new ProtectionDomain[] { caller.type.getProtectionDomain() }));
        }
        final JavaElement e = init.wrapper;
        if (e == null) return null;
        if ((sm != null) && (e instanceof JavaMember)) ((JavaMember) e).asNonAccessible().access();
        if (unwrap) {
            final JavaClass<?> annotatedFieldType = init.annotatedField.getType();
            if (init.referenced.getClass().isAssignableFrom(annotatedFieldType.type)) return (E) init.referenced;
        }
        return (E) init.wrapper;
    }

    public static JavaElement resolve(final JavaElement annotatedElement) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException {
        final Referencing ref = annotatedElement.getAnnotation(Referencing.class);
        if (ref == null) {
            throw new IllegalArgumentException("missing annotation " + Referencing.class.getName() + " on " + annotatedElement);
        }
        return resolve(annotatedElement, ref, null);
    }

    public static JavaElement resolve(final JavaElement annotatedElement, final Referencing ref) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException {
        return resolve(annotatedElement, ref, null);
    }

    static JavaElement resolve(final JavaElement annotated, final Referencing ref, final JavaClass<?> caller) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException {
        if (annotated == null) throw new NullPointerException("annotated element");
        if (ref == null) throw new NullPointerException("ref");
        Class<?> c = ref.declaring();
        if ((c == Void.TYPE) || (c == Void.class)) {
            c = null;
            final String cn = ref.classname();
            if (cn.length() > 0) {
                c = Class.forName(cn, false, annotated.getClassLoader().unsafeGetClassLoader());
                if (c == Void.TYPE) c = null;
            }
            if ((c == null) && (annotated instanceof JavaMember)) c = ((JavaMember) annotated).getDeclaringClass().type;
        }
        final String f = ref.field();
        final String m = ref.method();
        final String p = ref.pkg();
        final int parameterIndex = ref.parameterIndex();
        if (parameterIndex < -1) throw new IllegalArgumentException("parameterIndex < -1: " + annotated);
        if ((parameterIndex != -1) && (m.length() == 0)) throw new IllegalArgumentException("parameterIndex but no method specified: " + annotated);
        if (p.length() > 0) {
            if ((c != null) || (f.length() > 0) || (m.length() > 0)) {
                throw new IllegalArgumentException("for package reference nothing else than the name is allowed to be specified: " + annotated);
            }
            return resolvePackage(annotated, p, ref.optional());
        } else if ((f.length() == 0) && (m.length() == 0)) return (c == null) ? null : JavaClass.of(c);
        if (c == null) {
            if (annotated instanceof JavaMember) c = ((JavaMember) annotated).getDeclaringClass().type; else if (annotated instanceof JavaClass) c = annotated.getType().type; else throw new IllegalArgumentException("element neither is a package nor a class nor a member: " + annotated);
        }
        if (!f.isEmpty()) {
            if (!m.isEmpty()) throw new IllegalArgumentException("only one of fieldName and methodName may be specified: " + annotated);
            if (c == null) throw new IllegalArgumentException("class required to resolve field '" + f + "' referenced by " + annotated);
            final JavaField field = resolveField(c, f, ref.optional());
            if (field == null) return null;
            final Class<?> t = ref.type();
            if ((t != Void.TYPE) && !t.isAssignableFrom(field.getType().getElement())) {
                if (ref.optional()) return null;
                throw new NoSuchFieldException("type of field is not assignable from " + t + ":\n" + field);
            }
            return field;
        } else {
            if (c == null) throw new IllegalArgumentException("class required to resolve method '" + m + "' referenced by " + annotated);
            final Class<?>[] parameterTypes = ref.parameters();
            if (parameterIndex >= parameterTypes.length) throw new IllegalArgumentException("parameterIndex >= parameterTypes.length: " + annotated);
            if (m.equals(Referencing.CONSTRUCTOR) || (m.isEmpty() && ((annotated.getTypeClass() == Constructor.class) || (annotated.getTypeClass() == JavaConstructor.class)))) {
                return resolveConstructor(c, parameterTypes, ref.optional());
            } else {
                final JavaMethod method = resolveMethod(c, m, parameterTypes, ref.optional());
                if (method == null) return null;
                final Class<?> t = ref.type();
                if ((t != Void.TYPE) && !t.isAssignableFrom(method.getType().getElement())) {
                    if (ref.optional()) return null;
                    throw new NoSuchMethodException("return type of method is not assignable from " + t + ":\n" + method);
                }
                return method;
            }
        }
    }

    private static JavaConstructor resolveConstructor(final Class c, final Class[] parameterTypes, final boolean optional) throws NoSuchMethodException {
        final JavaConstructor ctor = JavaClass.of(c).getDeclaredConstructor(parameterTypes);
        if ((ctor == null) && !optional) {
            throw new NoSuchMethodException(Strings.concat(c.getName(), ".<init>(", Strings.chain(',', (Object[]) parameterTypes), ")"));
        }
        return ctor;
    }

    private static JavaField resolveField(final Class c, final String f, final boolean optional) throws NoSuchFieldException {
        final JavaField field = JavaClass.of(c).getDeclaredField(f);
        if ((field == null) && !optional) throw new NoSuchFieldException(c.getName() + "." + f);
        return field;
    }

    private static JavaMethod resolveMethod(final Class c, final String name, final Class[] parameterTypes, final boolean optional) throws NoSuchMethodException {
        final JavaMethod m = JavaClass.of(c).getDeclaredMethod(name, parameterTypes);
        if ((m == null) && !optional) {
            throw new NoSuchMethodException(Strings.concat(c.getName(), ".", name, "(", Strings.chain(',', (Object[]) parameterTypes), ")"));
        }
        return m;
    }

    private static JavaPackage resolvePackage(final JavaElement annotated, final String name, final boolean optional) {
        final JavaPackage p = annotated.getClassLoader().getPackage(name);
        if ((p == null) && !optional) {
            throw new IllegalStateException("package not found: '" + name + "' referenced by " + annotated + "\nclassloader = " + annotated.getClassLoader());
        }
        return p;
    }

    final transient AccessLevel accessLevel;

    final transient ElementType elementType;

    final transient int modifiers;

    JavaElement(final AnnotatedElement element, final ElementType elementType, final int modifiers) {
        super();
        this.elementType = elementType;
        this.modifiers = modifiers;
        this.accessLevel = AccessLevel.valueOfModifiers(modifiers);
    }

    public abstract JavaClassLoader getClassLoader();

    /**
   * Get the parent element containing the declaration of this one, {@code null} if not applicable.
   *
   * @since JaXLib 1.0
   */
    public abstract JavaElement getDeclaring();

    /**
   * Get the package containing the declaration of this element, {@code null} only for anonymous
   * {@link JavaPackage packages}.
   *
   * @since JaXLib 1.0
   */
    public abstract JavaPackage getPackage();

    /**
   * Get the name of the package containing the declaration of this element, {@code null} if and only if the element
   * is declared inside the anonymous package.
   *
   * @since JaXLib 1.0
   */
    public abstract String getPackageName();

    /**
   * Get the effective Java language access level of this element, taking access levels of enclosing elements into
   * account.
   *
   * @since JaXLib 1.0
   */
    @Nonnull
    public abstract AccessLevel getEffectiveAccessLevel();

    /**
   * Get the element wrapped by this {@code JavaElement} instance.
   *
   * @since JaXLib 1.0
   */
    public abstract AnnotatedElement getElement();

    public abstract String getName();

    public abstract JavaClass<?> getPropertyType();

    /**
   * The type of this element, {@code null} if and only if this element is a {@link JavaPackage package}.
   *
   * @since JaXLib 1.0
   */
    public abstract JavaClass<?> getType();

    /**
   * The type of this element, {@code null} if and only if this element is a {@link JavaPackage package}.
   *
   * @since JaXLib 1.0
   */
    public abstract Class<?> getTypeClass();

    /**
   * Whether this element is reachable through some public declaration.
   * For all kind of elements except methods this call returns {@code getEffectiveAccessLevel() == AccessLevel.PUBLIC}.
   * For methods this call returns true if either the effective access level is public, or the method is public and
   * is the implementation of a superclass or superinterface method of a class or interface that itself is completely
   * public.
   *
   * @since JaXLib 1.0
   */
    public abstract boolean isPublicReachable();

    public final AccessLevel getAccessLevel() {
        return this.accessLevel;
    }

    public <A extends Annotation> A getAnnotation(final Class<A> type) {
        return getElement().getAnnotation(type);
    }

    public Annotation[] getAnnotations() {
        return getElement().getAnnotations();
    }

    public final ElementType getElementType() {
        return this.elementType;
    }

    public final int getModifiers() {
        return this.modifiers;
    }

    public final Class<?> getPropertyTypeClass() {
        final JavaClass<?> c = getPropertyType();
        return (c == null) ? null : c.type;
    }

    public boolean isAnnotationPresent(final Class<? extends Annotation> type) {
        return getElement().isAnnotationPresent(type);
    }

    public final boolean isAbstract() {
        return (this.modifiers & Modifier.ABSTRACT) != 0;
    }

    public final boolean isFinal() {
        return (this.modifiers & Modifier.FINAL) != 0;
    }

    public final boolean isPrivate() {
        return this.accessLevel == AccessLevel.PRIVATE;
    }

    public final boolean isProtected() {
        return this.accessLevel == AccessLevel.PROTECTED;
    }

    public final boolean isPublic() {
        return this.accessLevel == AccessLevel.PUBLIC;
    }

    public final boolean isStatic() {
        return (this.modifiers & Modifier.STATIC) != 0;
    }

    public final boolean isStrictFp() {
        return (this.modifiers & Modifier.STRICT) != 0;
    }

    public final boolean isSynthetic() {
        return (this.modifiers & 0x00001000) != 0;
    }

    @Override
    public String toString() {
        return getElement().toString();
    }

    private static final class InitReferencing extends Object implements PrivilegedAction {

        final String annotatedFieldName;

        final JavaClass<?> caller;

        final boolean setAccessible;

        final boolean unwrap;

        JavaField annotatedField;

        JavaElement wrapper;

        Object referenced;

        InitReferencing(final JavaClass<?> caller, final String annotatedFieldName, final boolean setAccessible, final boolean unwrap) {
            super();
            if (caller == null) throw new IllegalStateException("unable to determine caller class");
            this.annotatedFieldName = annotatedFieldName;
            this.caller = caller;
            this.setAccessible = setAccessible;
            this.unwrap = unwrap;
        }

        @Override
        public final Object run() {
            this.annotatedField = this.caller.getDeclaredField(this.annotatedFieldName);
            if (this.annotatedField == null) throw new IllegalArgumentException(this.caller + " declares no field named " + this.annotatedFieldName);
            final Referencing referencing = this.annotatedField.getAnnotation(Referencing.class);
            if (referencing == null) {
                throw new IllegalArgumentException("missing annotation " + Referencing.class.getName() + " on " + this.annotatedField);
            }
            try {
                this.wrapper = JavaElement.resolve(this.annotatedField, referencing, this.caller);
            } catch (final RuntimeException ex) {
                throw ex;
            } catch (final Exception ex) {
                throw new IllegalArgumentException(ex);
            }
            if (this.wrapper == null) return null;
            if (this.setAccessible && (this.wrapper instanceof JavaMember)) this.wrapper = ((JavaMember) this.wrapper).accessUnsafe();
            if (this.unwrap) {
                this.referenced = this.wrapper.getElement();
                if (this.setAccessible && (this.referenced instanceof AccessibleObject)) ((AccessibleObject) this.referenced).setAccessible(true);
            }
            return null;
        }
    }
}
