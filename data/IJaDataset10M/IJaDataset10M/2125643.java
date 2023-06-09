package org.fest.reflect.field;

import org.fest.reflect.exception.ReflectionError;
import org.fest.reflect.reference.TypeRef;

/**
 * Understands the type of a static field to access using Java Reflection.
 * <p>
 * The following is an example of proper usage of this class:
 * <pre>
 *   // Retrieves the value of the static field "commonPowers"
 *   List&lt;String&gt; commmonPowers = {@link org.fest.reflect.core.Reflection#staticField(String) staticField}("commonPowers").{@link StaticFieldName#ofType(TypeRef) ofType}(new {@link TypeRef TypeRef}&lt;List&lt;String&gt;&gt;() {}).{@link StaticFieldTypeRef#in(Class) in}(Jedi.class).{@link Invoker#get() get}();
 *   
 *   // Sets the value of the static field "commonPowers"
 *   List&lt;String&gt; commonPowers = new ArrayList&lt;String&gt;();
 *   commonPowers.add("jump");
 *   {@link org.fest.reflect.core.Reflection#staticField(String) staticField}("commonPowers").{@link StaticFieldName#ofType(TypeRef) ofType}(new {@link TypeRef TypeRef}&lt;List&lt;String&gt;&gt;() {}).{@link StaticFieldTypeRef#in(Class) in}(Jedi.class).{@link Invoker#set(Object) set}(commonPowers);
 * </pre>
 * </p>
 *
 * @param <T> the generic type of the field. 
 *
 * @author Alex Ruiz
 * 
 * @since 1.1
 */
public class StaticFieldTypeRef<T> extends TypeRefTemplate<T> {

    StaticFieldTypeRef(TypeRef<T> type, StaticFieldName fieldName) {
        super(type, fieldName);
    }

    /**
   * Returns a new field invoker. A field invoker is capable of accessing (read/write) the underlying field.
   * @param target the type containing the static field of interest.
   * @return the created field invoker.
   * @throws NullPointerException if the given target is <code>null</code>.
   * @throws ReflectionError if a static field with a matching name and type cannot be found.
   */
    public Invoker<T> in(Class<?> target) {
        return fieldInvoker(target, target);
    }
}
