package uk.ac.ebi.intact.application.imex.helpers;

import uk.ac.ebi.intact.business.IntactException;
import uk.ac.ebi.intact.business.IntactHelper;
import uk.ac.ebi.intact.model.CvDatabase;
import uk.ac.ebi.intact.model.CvInteractorType;
import uk.ac.ebi.intact.model.CvTopic;
import uk.ac.ebi.intact.model.CvXrefQualifier;

/**
 * Utility methods for CVs.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id: CvHelper.java 4903 2006-05-19 16:04:55Z skerrien $
 * @since <pre>15-May-2006</pre>
 */
public class CvHelper {

    public static CvDatabase getPubmed(IntactHelper helper) {
        CvDatabase pubmed = helper.getObjectByPrimaryId(CvDatabase.class, CvDatabase.PUBMED_MI_REF);
        if (pubmed == null) {
            throw new IllegalStateException("Could not find PubMed.");
        }
        return pubmed;
    }

    public static CvDatabase getImex(IntactHelper helper) {
        CvDatabase pubmed = helper.getObjectByPrimaryId(CvDatabase.class, CvDatabase.IMEX_MI_REF);
        if (pubmed == null) {
            throw new IllegalStateException("Could not find CvDatabase(imex) by MI: " + CvDatabase.IMEX_MI_REF);
        }
        return pubmed;
    }

    public static CvDatabase getIntact(IntactHelper helper) {
        CvDatabase intact = helper.getObjectByPrimaryId(CvDatabase.class, CvDatabase.INTACT_MI_REF);
        if (intact == null) {
            throw new IllegalStateException("Could not find CvDatabase(intact) by MI: " + CvDatabase.INTACT_MI_REF);
        }
        return intact;
    }

    public static CvDatabase getPsi(IntactHelper helper) {
        CvDatabase psi = helper.getObjectByPrimaryId(CvDatabase.class, CvDatabase.PSI_MI_MI_REF);
        if (psi == null) {
            throw new IllegalStateException("Could not find CvDatabase(psi) by MI: " + CvDatabase.PSI_MI_MI_REF);
        }
        return psi;
    }

    public static CvXrefQualifier getPrimaryReference(IntactHelper helper) {
        CvXrefQualifier primaryReference = helper.getObjectByPrimaryId(CvXrefQualifier.class, CvXrefQualifier.PRIMARY_REFERENCE_MI_REF);
        if (primaryReference == null) {
            throw new IllegalStateException("Could not find CvXrefQualifier(primary-reference) by MI: " + CvXrefQualifier.PRIMARY_REFERENCE_MI_REF);
        }
        return primaryReference;
    }

    public static CvXrefQualifier getImexPrimary(IntactHelper helper) {
        CvXrefQualifier imexPrimary = helper.getObjectByPrimaryId(CvXrefQualifier.class, CvXrefQualifier.IMEX_PRIMARY_MI_REF);
        if (imexPrimary == null) {
            throw new IllegalStateException("Could not find CvXrefQualifier(imex-primary) by MI: " + CvXrefQualifier.IMEX_PRIMARY_MI_REF);
        }
        return imexPrimary;
    }

    public static CvXrefQualifier getIdentity(IntactHelper helper) {
        CvXrefQualifier identity = helper.getObjectByPrimaryId(CvXrefQualifier.class, CvXrefQualifier.IDENTITY_MI_REF);
        if (identity == null) {
            throw new IllegalStateException("Could not find CvXrefQualifier(identity) by MI: " + CvXrefQualifier.IDENTITY_MI_REF);
        }
        return identity;
    }

    public static CvInteractorType getProteinType(IntactHelper helper) {
        CvInteractorType proteinType = helper.getObjectByPrimaryId(CvInteractorType.class, CvInteractorType.getProteinMI());
        if (proteinType == null) {
            throw new IllegalStateException("Could not find CvInteractorType by MI: " + CvInteractorType.getProteinMI());
        }
        return proteinType;
    }

    public static CvTopic getImexRangeRequested(IntactHelper helper) throws IntactException {
        CvTopic imexRangeRequested = helper.getObjectByLabel(CvTopic.class, CvTopic.IMEX_RANGE_REQUESTED);
        if (imexRangeRequested == null) {
            throw new IllegalStateException("Could not find CvTopic by name: " + CvTopic.IMEX_RANGE_REQUESTED);
        }
        return imexRangeRequested;
    }

    public static CvTopic getImexRangeAssigned(IntactHelper helper) throws IntactException {
        CvTopic imexRangeAssigned = helper.getObjectByLabel(CvTopic.class, CvTopic.IMEX_RANGE_ASSIGNED);
        if (imexRangeAssigned == null) {
            throw new IllegalStateException("Could not find CvTopic by name: " + CvTopic.IMEX_RANGE_ASSIGNED);
        }
        return imexRangeAssigned;
    }

    public static CvTopic getImexExported(IntactHelper helper) throws IntactException {
        CvTopic imexExported = helper.getObjectByLabel(CvTopic.class, CvTopic.IMEX_EXPORTED);
        if (imexExported == null) {
            throw new IllegalStateException("Could not find CvTopic by name: " + CvTopic.IMEX_EXPORTED);
        }
        return imexExported;
    }
}
