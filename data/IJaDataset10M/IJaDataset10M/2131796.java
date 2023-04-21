package org.mcisb.sbml;

import java.util.*;
import org.mcisb.util.*;
import org.sbml.jsbml.*;

/**
 *
 * @author Neil Swainston
 */
public class SbmlSubDocumentGenerator {

    /**
	 * 
	 * @param model
	 * @param enzymeNames
	 * @return SBMLDocument
	 * @throws SbmlException 
	 */
    public SBMLDocument getSubDocumentFromEnzymeNames(final Model model, final Collection<String> enzymeNames) throws SbmlException {
        final Collection<String> enzymeIds = new HashSet<String>();
        for (int l = 0; l < model.getNumSpecies(); l++) {
            final Species species = model.getSpecies(l);
            if (enzymeNames.contains(species.getName())) {
                enzymeIds.add(species.getId());
            }
        }
        final Collection<String> reactionIds = new TreeSet<String>();
        outer: for (int l = 0; l < model.getNumReactions(); l++) {
            final Reaction reaction = model.getReaction(l);
            for (int m = 0; m < reaction.getNumModifiers(); ) {
                if (enzymeIds.contains(reaction.getModifier(m).getSpecies())) {
                    reactionIds.add(reaction.getId());
                }
                continue outer;
            }
        }
        return getSubDocumentFromReactionIds(model, reactionIds);
    }

    /**
	 * @param model 
	 * @param reactionIds 
	 * @return SBMLDocument
	 * @throws SbmlException 
	 */
    public SBMLDocument getSubDocumentFromReactionIds(final Model model, final Collection<String> reactionIds) throws SbmlException {
        final Set<String> compartmentIds = new TreeSet<String>();
        final Collection<String> speciesIds = new TreeSet<String>();
        for (final String reactionId : reactionIds) {
            final Reaction reaction = model.getReaction(reactionId);
            for (int s = 0; s < reaction.getNumReactants(); s++) {
                final Species species = model.getSpecies(reaction.getReactant(s).getSpecies());
                speciesIds.add(species.getId());
                compartmentIds.add(species.getCompartment());
            }
            for (int s = 0; s < reaction.getNumProducts(); s++) {
                final Species species = model.getSpecies(reaction.getProduct(s).getSpecies());
                speciesIds.add(species.getId());
                compartmentIds.add(species.getCompartment());
            }
            for (int s = 0; s < reaction.getNumModifiers(); s++) {
                final Species species = model.getSpecies(reaction.getModifier(s).getSpecies());
                speciesIds.add(species.getId());
                compartmentIds.add(species.getCompartment());
            }
        }
        return getSubDocument(model, compartmentIds, speciesIds, reactionIds);
    }

    /**
	 * @param model 
	 * @param compartmentIds 
	 * @param speciesIds 
	 * @param reactionIds 
	 * @return SBMLDocument
	 * @throws SbmlException 
	 */
    private SBMLDocument getSubDocument(final Model model, final Set<String> compartmentIds, final Collection<String> speciesIds, final Collection<String> reactionIds) throws SbmlException {
        final SBMLDocument document = new SBMLDocument();
        document.addNamespace(SbmlUtils.getDefaultSBMLNamespace());
        final Model subModel = document.createModel(StringUtils.getUniqueId());
        getCompartmentIds(model, compartmentIds);
        for (Iterator<String> iterator = compartmentIds.iterator(); iterator.hasNext(); ) {
            SbmlUtils.add(subModel, model.getCompartment(iterator.next()));
        }
        for (Iterator<String> iterator = speciesIds.iterator(); iterator.hasNext(); ) {
            SbmlUtils.add(subModel, model.getSpecies(iterator.next()));
        }
        for (Iterator<String> iterator = reactionIds.iterator(); iterator.hasNext(); ) {
            SbmlUtils.add(subModel, model.getReaction(iterator.next()));
        }
        return document;
    }

    /**
	 * Recursive method to get all of the compartments that outside refer to.
	 * 
	 * @param model
	 * @param compartmentIds
	 */
    private void getCompartmentIds(final Model model, final Set<String> compartmentIds) {
        boolean complete = true;
        for (Iterator<String> iterator = compartmentIds.iterator(); iterator.hasNext(); ) {
            final Compartment compartment = model.getCompartment(iterator.next());
            if (compartment.isSetOutside() && compartmentIds.add(compartment.getOutside())) {
                complete = false;
                break;
            }
        }
        if (!complete) {
            getCompartmentIds(model, compartmentIds);
        }
    }
}
