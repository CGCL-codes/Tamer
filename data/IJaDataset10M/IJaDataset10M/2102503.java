package com.rapidminer.ntext.document.annotation;

/**
 * Iterator over one single {@link AnnotationsIndex#}. Very basic and technical iterator only taking care of iterating over the own 
 * index. It presents some technical methods for giving convenience access.
 * 
 * @author Sebastian Land
 */
public interface AnnotationLayerIterator<T> {

    /**
	 * This method returns true if another annotation exists.
	 */
    public abstract boolean hasNext();

    /**
	 * This will return a reference to an singleton object that will change it's values
	 * depending on the currently visited annotation. This way heap pollution is avoided, but the 
	 * caller must take care that he doesn't use this object EVER for storing!
	 */
    public abstract Annotation<T> nextReference();
}
