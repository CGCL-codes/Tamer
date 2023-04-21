package org.codehaus.jam.mutable;

import org.codehaus.jam.JAnnotatedElement;

/**
 * <p>Mutable version of JAnnotatedElement.</p>
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface MAnnotatedElement extends MElement, JAnnotatedElement {

    /**
   * Returns the annotation having the given name, creating it if it doesn't
   * exist.
   */
    public MAnnotation findOrCreateAnnotation(String annotationName);

    public MAnnotation[] getMutableAnnotations();

    public MAnnotation getMutableAnnotation(String named);

    public MAnnotation addLiteralAnnotation(String annotationName);

    public MComment getMutableComment();

    public MComment createComment();

    public void removeComment();
}
