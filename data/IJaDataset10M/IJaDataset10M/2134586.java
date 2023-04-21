package cx.ath.mancel01.dependencyshot.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Set;

/**
 *
 * @author Mathieu ANCELIN
 */
public interface InjectionPoint {

    /**
    * Get the required type of injection point.
    *
    * @return the required type
    */
    Type getType();

    /**
    * Get the required qualifiers of the injection point.
    *
    * @return the required qualifiers
    */
    Set<Annotation> getAnnotations();

    /**
    * Get the {@link java.lang.reflect.Field} object in the case of field
    * injection, the {@link java.lang.reflect.Method} object in
    * the case of method parameter injection or the
    * {@link java.lang.reflect.Constructor} object in the case of constructor
    * parameter injection.
    *
    * @return the member
    */
    Member getMember();

    /**
    * Class on witch injection is performed.
    *
    * @return class on witch injection is performed.
    */
    Class getBeanClass();
}
