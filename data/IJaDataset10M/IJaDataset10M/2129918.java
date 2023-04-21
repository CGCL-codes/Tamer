package net.stickycode.metadata;

import java.lang.annotation.Annotation;

/**
 * Contract for algorithms that can detect marker annotations, and annotations with meta annotation markers.
 */
public interface MetadataResolver {

    /**
   * See if the subject of resolution is annotated directly with the given annotation
   * or if the given annotation annotates a marker annotation of the subject of resolution
   */
    boolean metaAnnotatedWith(Class<? extends Annotation> annotation);

    /**
   * See if the subject of resolution is annotated directly with the given annotation
   */
    boolean annotatedWith(Class<? extends Annotation> annotation);
}
