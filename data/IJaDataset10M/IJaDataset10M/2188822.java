package uk.ac.ebi.intact.application.search3.struts.view.beans;

import uk.ac.ebi.intact.model.AnnotatedObject;
import uk.ac.ebi.intact.model.Annotation;

/**
 * Content of information to display for an Annotation object.
 *
 * @author Michael Kleen
 * @version AnnotationViewBean.java Date: Nov 23, 2004 Time: 4:24:13 PM
 */
public class AnnotationViewBean {

    /**
     * An annotation.
     */
    private final Annotation anAnnotation;

    /**
     * A search URL.
     */
    private final String searchURL;

    /**
     * Constructs an AnnotationViewBean object.
     *
     * @param anAnnotation an Annotation object
     * @param searchURL    a String ...
     */
    public AnnotationViewBean(final Annotation anAnnotation, final String searchURL) {
        this.anAnnotation = anAnnotation;
        this.searchURL = searchURL;
    }

    /**
     * Gets this object's text.
     *
     * @return annotation current text
     */
    public String getText() {
        return this.anAnnotation.getAnnotationText();
    }

    /**
     * Gets this annotation's CvTopic's name.
     *
     * @return a String representing this annotation's CvTopic's name.
     */
    public String getName() {
        return this.anAnnotation.getCvTopic().getShortLabel();
    }

    /**
     * Returns the search url value.
     *
     * @return a String representing the search url value
     */
    public String getSearchUrl() {
        return this.searchURL;
    }

    /**
     * Returns the object value.
     *
     * @return an Annotation object representing the object value
     */
    public Annotation getObject() {
        return this.anAnnotation;
    }

    /**
     * Returns the type of the given object.
     *
     * @param anAnnotatedObject the object of which we want to know the name.
     *
     * @return a nice name for the given object.
     */
    private String getIntactType(final AnnotatedObject anAnnotatedObject) {
        final String objectIntactType;
        final String className = anAnnotatedObject.getClass().getName();
        final String basicType = className.substring(className.lastIndexOf(".") + 1);
        objectIntactType = ((basicType.indexOf("Impl") == -1) ? basicType : basicType.substring(0, basicType.indexOf("Impl")));
        return objectIntactType;
    }
}
