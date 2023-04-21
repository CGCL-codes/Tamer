package org.semanticweb.skos;

/**
 * Author: Simon Jupp<br>
 * Date: Apr 28, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public interface SKOSObject {

    void accept(SKOSObjectVisitor visitor);
}
