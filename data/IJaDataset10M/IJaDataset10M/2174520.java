package net.datao.datamodel.helpers.jena;

import com.hp.hpl.jena.ontology.OntClass;
import java.util.HashSet;

public class EquivalentClasses extends HashSet {

    public EquivalentClasses(OntClass c) {
        super();
        this.c = c;
    }

    private OntClass c;

    public OntClass getOriginalClass() {
        return c;
    }
}
