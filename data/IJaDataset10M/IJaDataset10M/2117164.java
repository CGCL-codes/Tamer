package org.cycads.dbExternal;

import org.biojavax.ontology.ComparableTerm;
import org.cycads.general.biojava.TermsAndOntologies;

public class COGBJ implements COG {

    ComparableTerm term;

    public COGBJ(String id) {
        term = TermsAndOntologies.getOntologyCOG().getOrCreateTerm(id);
    }

    public COGBJ(ComparableTerm term) {
        this.term = term;
    }

    public String getId() {
        return term.getName();
    }

    public ComparableTerm getTerm() {
        return term;
    }

    public int compareTo(COG cog) {
        if (cog instanceof COGBJ) {
            return term.compareTo(((COGBJ) cog).getTerm());
        }
        return getId().compareTo(cog.getId());
    }
}
