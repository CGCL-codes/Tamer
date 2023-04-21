package org.ensembl.compara.datamodel.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.ensembl.compara.datamodel.GenomeDB;
import org.ensembl.compara.datamodel.MethodLink;
import org.ensembl.compara.datamodel.MethodLinkSpeciesSet;
import org.ensembl.datamodel.Persistent;
import org.ensembl.datamodel.impl.PersistentImpl;

/**
 * I represent a type of analysis which could be applied between 
 * the elements of a set of genomes and the actual set which had it
 * applied.
**/
public class MethodLinkSpeciesSetImpl extends PersistentImpl implements MethodLinkSpeciesSet {

    private static final long serialVersionUID = 1L;

    private Set _speciesSet = new HashSet();

    public Set _speciesSetdbIDs = new HashSet();

    private MethodLink _methodLink;

    public Set getSpeciesSet() {
        return _speciesSet;
    }

    public void setSpeciesSet(Set speciesSet) {
        Iterator genomeDBs = speciesSet.iterator();
        HashMap uniqueIDsHash = new HashMap();
        while (genomeDBs.hasNext()) {
            Object thing = genomeDBs.next();
            if (thing == null) {
                throw new org.ensembl.compara.driver.NonFatalException("Attempt to initialise species set with a null element");
            }
            if (!(thing instanceof GenomeDB)) {
                throw new org.ensembl.compara.driver.NonFatalException("Attempt to initialise species set with an element which is not a GenomeDB");
            }
            if (uniqueIDsHash.get((new Long(((Persistent) thing).getInternalID())).toString()) != null) {
                throw new org.ensembl.compara.driver.NonFatalException("GenomeDB with ID :" + ((Persistent) thing).getInternalID() + " is passed in twice");
            }
            uniqueIDsHash.put((new Long(((Persistent) thing).getInternalID())).toString(), "1");
        }
        _speciesSet = speciesSet;
    }

    public MethodLink getMethodLink() {
        return _methodLink;
    }

    public void setMethodLink(MethodLink methodLink) {
        _methodLink = methodLink;
    }

    public Set getSpeciesSetdbIDs() {
        return _speciesSetdbIDs;
    }

    public void setSpeciesSetDBIDs(Set speciesSetdbIDs) {
        _speciesSetdbIDs = speciesSetdbIDs;
    }

    public String toString() {
        StringBuffer output = new StringBuffer().append("MethodLinkSpeciesSet:[").append(getInternalID()).append("](").append(getMethodLink().getType()).append(")[");
        Iterator genomes = getSpeciesSet().iterator();
        while (genomes.hasNext()) {
            GenomeDB genome = (GenomeDB) genomes.next();
            output.append(genome.getInternalID()).append("--").append(genome.getName()).append(", ");
        }
        output.append("]");
        return output.toString();
    }
}
