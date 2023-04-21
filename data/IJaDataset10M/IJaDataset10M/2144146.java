package uk.ac.ebi.intact.model;

import java.util.Collection;

/**
 * TODO comment this
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: Annotated.java 9231 2007-07-31 09:42:32Z baranda $
 */
public interface Annotated {

    Collection<Annotation> getAnnotations();
}
