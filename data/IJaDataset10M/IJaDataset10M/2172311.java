package org.codehaus.groovy.grails.commons;

import javax.persistence.Entity;

/**
 * Detects annotated domain classes for EJB3 style mappings
 *
 * @author Graeme Rocher
 * @since 1.0
 *        <p/>
 *        Created: Dec 6, 2007
 */
public class AnnotationDomainClassArtefactHandler extends DomainClassArtefactHandler {

    private static final String JPA_MAPPING_STRATEGY = "JPA";

    public boolean isArtefactClass(Class clazz) {
        return super.isArtefactClass(clazz) || isJPADomainClass(clazz);
    }

    public static boolean isJPADomainClass(Class clazz) {
        return clazz != null && clazz.getAnnotation(Entity.class) != null;
    }

    public GrailsClass newArtefactClass(Class artefactClass) {
        GrailsDomainClass grailsClass = (GrailsDomainClass) super.newArtefactClass(artefactClass);
        grailsClass.setMappingStrategy(JPA_MAPPING_STRATEGY);
        return grailsClass;
    }
}
