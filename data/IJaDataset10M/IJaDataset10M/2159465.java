package org.semanticweb.skos;

import java.net.URI;
import java.util.Set;

/**
 * Author: Simon Jupp<br>
 * Date: Apr 28, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public interface SKOSProperty extends SKOSObject {

    URI getURI();

    Set<SKOSEntity> getReferencingEntities(SKOSDataset dataset);

    void accept(SKOSPropertyVisitor visitor);
}
