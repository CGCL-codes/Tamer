package net.sourceforge.fluxion.pussycat.util;

import org.semanticweb.owl.model.*;
import org.semanticweb.owl.vocab.XSDVocabulary;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CannedQueries {

    private CannedQueries() {
    }

    public static final OWLDescription getFakeQuery(URI datasource, OWLDataFactory factory) throws OWLException {
        return factory.getOWLClass(datasource.resolve("#bogus_class"));
    }

    public static final OWLDescription getPigSpeciesQuery(URI datasource, OWLDataFactory factory) throws OWLException {
        OWLClass species = factory.getOWLClass(datasource.resolve("#Species"));
        OWLDataProperty species_has_id = factory.getOWLDataProperty(datasource.resolve("#Species_Record_has_Id"));
        OWLTypedConstant pig = factory.getOWLTypedConstant("ARKSPC00000001", factory.getOWLDataType(XSDVocabulary.STRING.getURI()));
        OWLRestriction pig_species_restriction = factory.getOWLDataValueRestriction(species_has_id, pig);
        OWLDescription pig_species_description = factory.getOWLObjectIntersectionOf(new HashSet<OWLDescription>(Arrays.asList(species, pig_species_restriction)));
        return pig_species_description;
    }

    public static final OWLDescription getAllSpeciesQuery(URI datasource, OWLDataFactory factory) throws OWLException {
        OWLClass species = factory.getOWLClass(datasource.resolve("#Species"));
        return species;
    }

    public static final OWLDescription getAllOrganismQuery(URI datasource, OWLDataFactory factory) throws OWLException {
        OWLClass organism = factory.getOWLClass(datasource.resolve("#Organism"));
        return organism;
    }

    public static final OWLDescription getAllMapsQuery(URI datasource, OWLDataFactory factory) throws OWLException {
        OWLClass sequenceMap = factory.getOWLClass(datasource.resolve("#Chromosomal_DNA_Representation"));
        OWLClass modelMap = factory.getOWLClass(datasource.resolve("#Genetic_Map"));
        OWLDescription map = factory.getOWLObjectUnionOf(new HashSet<OWLDescription>(Arrays.asList(sequenceMap, modelMap)));
        return map;
    }
}
