package org.mcisb.sbml;

import java.io.*;
import java.util.*;
import org.mcisb.ontology.*;
import org.mcisb.ontology.go.*;
import org.mcisb.ontology.sbo.*;
import org.sbml.jsbml.*;

/**
 * 
 * @author Neil Swainston
 */
public class FbaUtils {

    /**
	 * 
	 */
    private final SbmlUtils sbmlUtils = new SbmlUtils();

    /**
	 * 
	 * @param fileIn 
	 * @param fileOut 
	 * @throws Exception 
	 */
    public void blockUptakeReactions(final File fileIn, final File fileOut) throws Exception {
        final SBMLDocument document = sbmlUtils.readSBML(fileIn).getDocument();
        final Model model = document.getModel();
        for (int l = 0; l < model.getNumReactions(); l++) {
            final Reaction reaction = model.getReaction(l);
            blockUptakeReaction(model, reaction);
        }
        final File temp = File.createTempFile("temp", ".xml");
        sbmlUtils.writeSBML(new SbmlDocument(document), temp, true, false);
        sbmlUtils.format(temp, fileOut, true, false);
    }

    /**
	 * 
	 * @param model
	 * @param reaction
	 * @throws Exception 
	 */
    private void blockUptakeReaction(final Model model, final Reaction reaction) throws Exception {
        final String MITOCHONDRION_GO_TERM = "GO:0005739";
        if (reaction.getSBOTerm() == SboUtils.TRANSPORT_REACTION) {
            final Collection<String> reactantCompartments = new LinkedHashSet<String>();
            final Collection<String> productCompartments = new LinkedHashSet<String>();
            for (int l = 0; l < reaction.getNumReactants(); l++) {
                final Species species = sbmlUtils.getReactantSpecies(model, reaction.getId(), l);
                final Compartment compartment = model.getCompartment(species.getCompartment());
                reactantCompartments.add(species.getBoundaryCondition() || compartment.getId().equals("b") || compartment.getId().equals("s") ? GoUtils.EXTRACELLULAR_GO_TERM_ID : sbmlUtils.getOntologyTerm(compartment, Ontology.GO).getId().replaceAll(OntologyTerm.ENCODED_COLON, OntologyTerm.COLON));
            }
            for (int l = 0; l < reaction.getNumProducts(); l++) {
                final Species species = sbmlUtils.getProductSpecies(model, reaction.getId(), l);
                final Compartment compartment = model.getCompartment(species.getCompartment());
                productCompartments.add(species.getBoundaryCondition() || compartment.getId().equals("b") || compartment.getId().equals("s") ? GoUtils.EXTRACELLULAR_GO_TERM_ID : sbmlUtils.getOntologyTerm(compartment, Ontology.GO).getId().replaceAll(OntologyTerm.ENCODED_COLON, OntologyTerm.COLON));
            }
            if ((reactantCompartments.contains(GoUtils.EXTRACELLULAR_GO_TERM_ID) && (productCompartments.contains(GoUtils.CYTOPLASM_GO_TERM_ID) || productCompartments.contains(GoUtils.CYTOSOL_GO_TERM_ID) || productCompartments.contains(MITOCHONDRION_GO_TERM)))) {
                final KineticLaw kineticLaw = reaction.getKineticLaw();
                kineticLaw.getLocalParameter("UPPER_BOUND").setValue(0.0);
            }
            if (productCompartments.contains(GoUtils.EXTRACELLULAR_GO_TERM_ID) && (reactantCompartments.contains(GoUtils.CYTOPLASM_GO_TERM_ID) || reactantCompartments.contains(GoUtils.CYTOSOL_GO_TERM_ID) || reactantCompartments.contains(MITOCHONDRION_GO_TERM))) {
                final KineticLaw kineticLaw = reaction.getKineticLaw();
                kineticLaw.getLocalParameter("LOWER_BOUND").setValue(0.0);
            }
        }
    }

    /**
	 * @param args
	 * @throws Exception 
	 * @throws IOException 
	 */
    public static void main(String[] args) throws IOException, Exception {
        new FbaUtils().blockUptakeReactions(new File(args[0]), new File(args[1]));
    }
}
