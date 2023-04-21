package net.sf.jdpa;

import net.sf.jdpa.lang.Element;
import java.lang.annotation.Annotation;

/**
 * A <code>DeclarationHandler</code> is responsible for responding to the
 * precense of an annotation on some element in a class.
 *
 * Example: Create a custom annotation that you wish to process with a
 * <code>DeclarationHandler</code>:
 * <code><pre>
 * &at;Declaration
 * &at;(RUNTIME)
 * &at;(METHOD)
 * public @interface MyAnnotation { }
 * </pre></code>
 *
 * Next we define a class that uses the annotation:
 * <code><pre>
 * public class MyClass
 * {
 *    &at;MyAnnotation
 *    public void doSomething() {}
 * }
 * </pre></code>
 *
 * When the code is instrumented, a <code>Realizer</code> will go through all
 * the elements in a class, looking for all JDPA declarations available in the
 * class. Since <code>&at;MyAnnotation</code> has a <code>&at;Declaration</code>
 * annotation, the annotation will be discovered by the realizer. However, the
 * realizer won't be able to generate any code from the annotation - we need
 * to define a declaration handler capable of responding to the precense of an
 * annotation and register it with the realizer:
 *
 * <code><pre>
 * public class MyDeclarationHandler implements DeclarationHandler
 * {
 *     public boolean supports(Element target, Annotation annotation)
 *     {
 *         return (annotation instanceof MyAnnotation);
 *     }
 *
 *     public void instrument(Emitter emitter, Element target, Annotation annotation)
 *           throws RealizationException
 *     {
 *         emitter.emitBefore($call("println").of($("out").of(System.class)).with("Hello World!"));
 *     }
 * }
 * </pre></code>
 * @author Andreas Nilsson
 */
public interface DeclarationHandler {

    /**
     * Returns whether or not the declaration handler supports the provided annotation on
     * the provided element. If it does support it, this method should return true. This
     * will cause the <code>instrument</code> method to be called. If false is returned,
     * the <code>instrument</code> method will not be called.
     * @param target The target element on which the annotation was found.
     * @param annotation The annotation that was found.
     * @return Whether or not the provided annotation is supported on the provided element.
     * If true, <code>instrument</code> will subsequently be called. 
     */
    public boolean supports(Element target, Annotation annotation);

    /**
     * Called when a declaration has been found that is supported by this declaration
     * handler. This method should realize the declaration by injecting the appropriate
     * code into the class using the provided emitter.
     * @param emitter The emitter that allows injection of code into the class containing
     * the supported annotation.
     * @param target The target element that is being instrumented, e.g. a class, parameter,
     * method etc.
     * @param annotation The JDPA annotation that was found.
     * @throws RealizationException Thrown if the declaration could not be realized, e.g. if
     * the code could not be generated, the annotation is not setup properly etc.
     */
    public void instrument(Element target, Annotation annotation) throws RealizationException;
}
