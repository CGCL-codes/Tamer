package org.mcisb.subliminal.compartment;

import java.io.*;
import java.util.*;
import org.mcisb.ontology.*;
import org.mcisb.ontology.uniprot.*;
import org.mcisb.subliminal.compartment.PsortClient.OrganismType;

/**
 * 
 * @author Neil Swainston
 */
class MixedSbmlCompartmentaliser extends SbmlCompartmentaliser {

    /**
	 * 
	 */
    final UniProtSbmlCompartmentaliser uniProtSbmlCompartmentaliser;

    /**
	 * 
	 */
    final PsortSbmlCompartmentaliser psortSbmlCompartmentaliser;

    /**
	 * @param inFile
	 * @param outFile
	 * @throws Exception
	 */
    public MixedSbmlCompartmentaliser(final File inFile, final File outFile, final OrganismType organismType) throws Exception {
        super(inFile, outFile);
        uniProtSbmlCompartmentaliser = new UniProtSbmlCompartmentaliser(inFile, outFile);
        psortSbmlCompartmentaliser = new PsortSbmlCompartmentaliser(inFile, outFile, organismType);
    }

    /**
	 * @param inFile
	 * @param outFile
	 * @throws Exception
	 */
    public MixedSbmlCompartmentaliser(final File inFile, final File outFile) throws Exception {
        this(inFile, outFile, OrganismType.fungi);
    }

    @Override
    protected Collection<OntologyTerm> getGoTerms(final UniProtTerm uniProtTerm) throws Exception {
        final Collection<OntologyTerm> goTerms = new LinkedHashSet<OntologyTerm>();
        goTerms.addAll(uniProtSbmlCompartmentaliser.getGoTerms(uniProtTerm));
        goTerms.addAll(psortSbmlCompartmentaliser.getGoTerms(uniProtTerm));
        return goTerms;
    }

    /**
	 * @param args
	 * @throws Exception 
	 */
    public static void main(String[] args) throws Exception {
        new MixedSbmlCompartmentaliser(new File(args[0]), new File(args[1]), OrganismType.valueOf(args[2])).run();
    }
}
