package uk.ac.ebi.intact.application.editor.util;

import uk.ac.ebi.intact.business.IntactException;
import uk.ac.ebi.intact.context.IntactContext;
import uk.ac.ebi.intact.model.*;
import uk.ac.ebi.intact.persistence.dao.AnnotatedObjectDao;
import uk.ac.ebi.intact.persistence.dao.DaoFactory;

/**
 * TODO comment it.
 *
 * @author Catherine Leroy (cleroy@ebi.ac.uk)
 * @version $Id$
 */
public class DaoProvider {

    public static AnnotatedObjectDao getDaoFactory(Class clazz) {
        if (Feature.class.isAssignableFrom(clazz)) {
            return DaoProvider.getDaoFactory().getFeatureDao();
        } else if (Protein.class.isAssignableFrom(clazz)) {
            return DaoProvider.getDaoFactory().getProteinDao();
        } else if (BioSource.class.isAssignableFrom(clazz)) {
            return DaoProvider.getDaoFactory().getBioSourceDao();
        } else if (Experiment.class.isAssignableFrom(clazz)) {
            return DaoProvider.getDaoFactory().getExperimentDao();
        } else if (CvObject.class.isAssignableFrom(clazz)) {
            if (CvAliasType.class.isAssignableFrom(clazz)) {
                return DaoProvider.getDaoFactory().getCvObjectDao(CvAliasType.class);
            } else if (CvCellType.class.isAssignableFrom(clazz)) {
                return DaoProvider.getDaoFactory().getCvObjectDao(CvCellType.class);
            } else if (CvExperimentalRole.class.isAssignableFrom(clazz)) {
                return DaoProvider.getDaoFactory().getCvObjectDao(CvExperimentalRole.class);
            } else if (CvDatabase.class.isAssignableFrom(clazz)) {
                return DaoProvider.getDaoFactory().getCvObjectDao(CvDatabase.class);
            } else if (CvFeatureIdentification.class.isAssignableFrom(clazz)) {
                return DaoProvider.getDaoFactory().getCvObjectDao(CvFeatureIdentification.class);
            } else if (CvFeatureType.class.isAssignableFrom(clazz)) {
                return DaoProvider.getDaoFactory().getCvObjectDao(CvFeatureType.class);
            } else if (CvFuzzyType.class.isAssignableFrom(clazz)) {
                return DaoProvider.getDaoFactory().getCvObjectDao(CvFuzzyType.class);
            } else if (CvIdentification.class.isAssignableFrom(clazz)) {
                return DaoProvider.getDaoFactory().getCvObjectDao(CvIdentification.class);
            } else if (CvInteraction.class.isAssignableFrom(clazz)) {
                return DaoProvider.getDaoFactory().getCvObjectDao(CvInteraction.class);
            } else if (CvInteractionType.class.isAssignableFrom(clazz)) {
                return DaoProvider.getDaoFactory().getCvObjectDao(CvInteractionType.class);
            } else if (CvInteractorType.class.isAssignableFrom(clazz)) {
                return DaoProvider.getDaoFactory().getCvObjectDao(CvInteractorType.class);
            } else if (CvTissue.class.isAssignableFrom(clazz)) {
                return DaoProvider.getDaoFactory().getCvObjectDao(CvTissue.class);
            } else if (CvTopic.class.isAssignableFrom(clazz)) {
                return DaoProvider.getDaoFactory().getCvObjectDao(CvTopic.class);
            } else if (CvXrefQualifier.class.isAssignableFrom(clazz)) {
                return DaoProvider.getDaoFactory().getCvObjectDao(CvXrefQualifier.class);
            } else if (CvBiologicalRole.class.isAssignableFrom(clazz)) {
                return DaoProvider.getDaoFactory().getCvObjectDao(CvBiologicalRole.class);
            } else {
                return DaoProvider.getDaoFactory().getCvObjectDao(CvObject.class);
            }
        } else if (Interaction.class.isAssignableFrom(clazz)) {
            return DaoProvider.getDaoFactory().getInteractionDao();
        } else if (NucleicAcid.class.isAssignableFrom(clazz)) {
            return DaoProvider.getDaoFactory().getInteractorDao(NucleicAcidImpl.class);
        } else if (Protein.class.isAssignableFrom(clazz)) {
            return DaoProvider.getDaoFactory().getProteinDao();
        } else if (SmallMolecule.class.isAssignableFrom(clazz)) {
            return DaoProvider.getDaoFactory().getInteractorDao(SmallMoleculeImpl.class);
        } else throw new IntactException("Class " + clazz.getName() + " is not a searchable object");
    }

    public static DaoFactory getDaoFactory() {
        return IntactContext.getCurrentInstance().getDataContext().getDaoFactory();
    }
}
