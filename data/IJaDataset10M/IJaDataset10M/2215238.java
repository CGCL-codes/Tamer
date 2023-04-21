package uk.ac.ebi.intact.plugin.psigenerator;

import uk.ac.ebi.intact.config.impl.SmallCvPrimer;
import uk.ac.ebi.intact.model.CvDatabase;
import uk.ac.ebi.intact.model.CvXrefQualifier;
import uk.ac.ebi.intact.persistence.dao.DaoFactory;

/**
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: PsiXmlCvPrimer.java 11330 2008-04-16 15:45:22Z baranda $
 */
public class PsiXmlCvPrimer extends SmallCvPrimer {

    public PsiXmlCvPrimer(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public void createCVs() {
        super.createCVs();
        getCvObject(CvXrefQualifier.class, CvXrefQualifier.TARGET_SPECIES);
        getCvObject(CvDatabase.class, CvDatabase.NEWT, CvDatabase.NEWT_MI_REF);
    }
}
