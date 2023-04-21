package org.semanticweb.skos;

import java.net.URI;

/**
 * Author: Simon Jupp<br>
 * Date: Apr 23, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * <br>
 * Represent a SKOS data property
 */
public interface SKOSDataProperty {

    URI getURI();

    SKOSDataProperty getSuperProperty();

    SKOSDataProperty getSubProperty();
}
