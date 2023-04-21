package uk.ac.ebi.intact.application.dataConversion.psiUpload.persister;

import uk.ac.ebi.intact.application.dataConversion.psiUpload.checker.*;
import uk.ac.ebi.intact.application.dataConversion.psiUpload.model.AnnotationTag;
import uk.ac.ebi.intact.application.dataConversion.psiUpload.model.ExperimentDescriptionTag;
import uk.ac.ebi.intact.application.dataConversion.psiUpload.model.XrefTag;
import uk.ac.ebi.intact.business.IntactException;
import uk.ac.ebi.intact.model.*;
import uk.ac.ebi.intact.util.cdb.UpdateExperimentAnnotationsFromPudmed;
import uk.ac.ebi.intact.persistence.dao.ExperimentDao;
import uk.ac.ebi.intact.persistence.dao.DaoFactory;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * That class make the data persitent in the Intact database. <br> That class takes care of an Experiments object. <br>
 * It assumes that the data are already parsed and passed the validity check successfully.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id: ExperimentDescriptionPersister.java 5209 2006-07-05 07:36:22Z baranda $
 */
public class ExperimentDescriptionPersister {

    private static Map cache = new HashMap();

    /**
     * Make an ExperimentTag persistent as an Intact Experiemnt. <br> (1) check if the experiment is not existing in
     * IntAct, if so, reuse it <br> (2) check if it has been made paersistent already, if so, reuse it. <br> (3) else,
     * make it persistent.
     *
     * @param experimentDescription the data from which we want to make an Experiment persistent
     *
     * @return either an already existing Experiment in IntAct or a brand new one created out of the data present in the
     *         PSI file
     *
     * @throws IntactException
     */
    public static Experiment persist(final ExperimentDescriptionTag experimentDescription) throws IntactException {
        ExperimentDao expDao = DaoFactory.getExperimentDao();
        Institution institution = DaoFactory.getInstitutionDao().getInstitution();
        Experiment experiment;
        final String shortlabel = experimentDescription.getShortlabel();
        experiment = ExperimentDescriptionChecker.getIntactExperiment(shortlabel);
        if (experiment != null) {
            return experiment;
        }
        experiment = (Experiment) cache.get(shortlabel);
        if (experiment != null) {
            return experiment;
        }
        final BioSource biosource = HostOrganismChecker.getBioSource(experimentDescription.getHostOrganism());
        experiment = new Experiment(institution, shortlabel, biosource);
        expDao.persist(experiment);
        experiment.setFullName(experimentDescription.getFullname());
        final String participantDetectionId = experimentDescription.getParticipantDetection().getPsiDefinition().getId();
        final CvIdentification cvIdentification = ParticipantDetectionChecker.getCvIdentification(participantDetectionId);
        experiment.setCvIdentification(cvIdentification);
        final String interactionDetectionId = experimentDescription.getInteractionDetection().getPsiDefinition().getId();
        final CvInteraction cvInteraction = InteractionDetectionChecker.getCvInteraction(interactionDetectionId);
        experiment.setCvInteraction(cvInteraction);
        final XrefTag bibRef = experimentDescription.getBibRef();
        final Xref primaryXref = new Xref(institution, XrefChecker.getCvDatabase(bibRef.getDb()), bibRef.getId(), bibRef.getSecondary(), bibRef.getVersion(), ControlledVocabularyRepository.getPrimaryXrefQualifier());
        experiment.addXref(primaryXref);
        DaoFactory.getXrefDao().persist(primaryXref);
        String pubmedId = primaryXref.getPrimaryId();
        System.out.print("Updating experiment details from CitExplore...");
        System.out.flush();
        UpdateExperimentAnnotationsFromPudmed.update(experiment, pubmedId);
        System.out.println("done.");
        final Collection secondaryPubmedXrefs = experimentDescription.getAdditionalBibRef();
        for (Iterator iterator = secondaryPubmedXrefs.iterator(); iterator.hasNext(); ) {
            XrefTag xrefTag = (XrefTag) iterator.next();
            Xref seeAlsoXref = new Xref(institution, XrefChecker.getCvDatabase(xrefTag.getDb()), xrefTag.getId(), xrefTag.getSecondary(), xrefTag.getVersion(), ControlledVocabularyRepository.getSeeAlsoXrefQualifier());
            experiment.addXref(seeAlsoXref);
            DaoFactory.getXrefDao().persist(seeAlsoXref);
        }
        final Collection annotations = experimentDescription.getAnnotations();
        for (Iterator iterator = annotations.iterator(); iterator.hasNext(); ) {
            final AnnotationTag annotationTag = (AnnotationTag) iterator.next();
            final CvTopic cvTopic = AnnotationChecker.getCvTopic(annotationTag.getType());
            Annotation annotation = searchIntactAnnotation(annotationTag);
            if (annotation == null) {
                annotation = new Annotation(institution, cvTopic);
                annotation.setAnnotationText(annotationTag.getText());
                DaoFactory.getAnnotationDao().persist(annotation);
            }
            experiment.addAnnotation(annotation);
        }
        final Collection xrefs = experimentDescription.getXrefs();
        for (Iterator iterator = xrefs.iterator(); iterator.hasNext(); ) {
            XrefTag xrefTag = (XrefTag) iterator.next();
            Xref xref = new Xref(DaoFactory.getInstitutionDao().getInstitution(), XrefChecker.getCvDatabase(xrefTag.getDb()), xrefTag.getId(), xrefTag.getSecondary(), xrefTag.getVersion(), null);
            experiment.addXref(xref);
            DaoFactory.getXrefDao().persist(xref);
        }
        expDao.update(experiment);
        cache.put(experiment.getShortLabel(), experiment);
        return experiment;
    }

    /**
     * Search in IntAct for an Annotation having the a specific type and annotationText.
     *
     * @param annotationTag the description of the Annotation we are looking for.
     *
     * @return the found Annotation or null if not found.
     *
     * @throws IntactException
     */
    private static Annotation searchIntactAnnotation(final AnnotationTag annotationTag) throws IntactException {
        final String text = annotationTag.getText();
        Collection annotations = DaoFactory.getAnnotationDao().getByTextLike(text);
        Annotation annotation = null;
        if (annotations != null) {
            for (Iterator iterator = annotations.iterator(); iterator.hasNext() && annotation == null; ) {
                Annotation anAnnotation = (Annotation) iterator.next();
                if (annotationTag.getType().equals(anAnnotation.getCvTopic().getShortLabel())) {
                    annotation = anAnnotation;
                }
            }
        }
        return annotation;
    }
}
