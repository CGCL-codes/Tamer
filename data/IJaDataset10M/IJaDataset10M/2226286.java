package uk.ac.ebi.intact.application.dataConversion.psiUpload.checker;

import uk.ac.ebi.intact.application.dataConversion.psiUpload.gui.Monitor;
import uk.ac.ebi.intact.application.dataConversion.psiUpload.model.*;
import uk.ac.ebi.intact.application.dataConversion.psiUpload.util.report.Message;
import uk.ac.ebi.intact.application.dataConversion.psiUpload.util.report.MessageHolder;
import uk.ac.ebi.intact.model.CvInteractorType;
import uk.ac.ebi.intact.model.CvDatabase;
import uk.ac.ebi.intact.util.BioSourceFactory;
import uk.ac.ebi.intact.util.UpdateProteinsI;
import uk.ac.ebi.intact.persistence.dao.DaoFactory;
import java.util.Collection;
import java.util.Iterator;

/**
 * That class .
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id: InteractionChecker.java 5204 2006-07-05 01:00:41Z baranda $
 */
public final class InteractionChecker {

    private static CvInteractorType cvInteractionType = null;

    public static boolean interatorTypeChecked = false;

    public static void checkCvInteractorType() {
        if (false == interatorTypeChecked) {
            cvInteractionType = DaoFactory.getCvObjectDao(CvInteractorType.class).getByXref(CvInteractorType.getInteractionMI());
            if (cvInteractionType == null) {
                MessageHolder.getInstance().addCheckerMessage(new Message("Could not find CvInteractorType( interaction )."));
            }
            interatorTypeChecked = true;
        }
    }

    public static CvInteractorType getCvInteractionType() {
        return cvInteractionType;
    }

    public static void check(final InteractionTag interaction, final UpdateProteinsI proteinFactory, final BioSourceFactory bioSourceFactory, final Monitor monitor) {
        checkCvInteractorType();
        Collection experiments = interaction.getExperiments();
        for (Iterator iterator = experiments.iterator(); iterator.hasNext(); ) {
            ExperimentDescriptionTag experimentDescription = (ExperimentDescriptionTag) iterator.next();
            ExperimentDescriptionChecker.check(experimentDescription, bioSourceFactory);
        }
        final Collection participants = interaction.getParticipants();
        for (Iterator iterator = participants.iterator(); iterator.hasNext(); ) {
            ProteinParticipantTag proteinParticipant = (ProteinParticipantTag) iterator.next();
            if (monitor != null) {
                ProteinInteractorTag proteinInteractor = proteinParticipant.getProteinInteractor();
                String uniprotID = null;
                String taxid = null;
                if (proteinInteractor != null && proteinInteractor.getPrimaryXref() != null) {
                    uniprotID = proteinInteractor.getPrimaryXref().getId();
                }
                if (proteinInteractor != null && proteinInteractor.getOrganism() != null) {
                    taxid = proteinInteractor.getOrganism().getTaxId();
                } else {
                    taxid = "taxid not specified";
                }
                monitor.setStatus("Checking " + uniprotID + " (" + taxid + ")");
            }
            ProteinParticipantChecker.check(proteinParticipant, proteinFactory, bioSourceFactory);
            ExpressedInTag expressedIn = proteinParticipant.getExpressedIn();
            if (null != expressedIn) {
                ExpressedInChecker.check(expressedIn);
            }
        }
        final InteractionTypeTag interactionType = interaction.getInteractionType();
        InteractionTypeChecker.check(interactionType);
        Collection xrefs = interaction.getXrefs();
        for (Iterator iterator = xrefs.iterator(); iterator.hasNext(); ) {
            XrefTag xref = (XrefTag) iterator.next();
            XrefChecker.check(xref);
        }
        final Collection annotations = interaction.getAnnotations();
        int countKd = 0;
        for (Iterator iterator = annotations.iterator(); iterator.hasNext(); ) {
            AnnotationTag annotation = (AnnotationTag) iterator.next();
            if (annotation.isDissociationConstant()) {
                countKd++;
            }
            AnnotationChecker.check(annotation);
        }
        if (countKd > 1) {
            MessageHolder.getInstance().addCheckerMessage(new Message("More than one dissociation constant specified " + "in an Interaction (one at most is allowed)."));
        }
    }
}
