package uk.ac.ebi.intact.dataexchange.psimi.xml.converter.shared;

import org.junit.Assert;
import org.junit.Test;
import psidev.psi.mi.xml.model.Interactor;
import uk.ac.ebi.intact.model.CvObjectXref;
import uk.ac.ebi.intact.model.CvXrefQualifier;
import uk.ac.ebi.intact.model.Institution;
import uk.ac.ebi.intact.model.util.CvObjectUtils;

/**
 * TODO comment this
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: InteractorConverterTest.java 9846 2007-10-05 13:57:49Z baranda $
 */
public class InteractorConverterTest {

    @Test
    public void psiToIntact_default() throws Exception {
        Interactor psiInteractor = PsiMockFactory.createMockInteractor();
        InteractorConverter interactorConverter = new InteractorConverter(new Institution("testInstitution"));
        uk.ac.ebi.intact.model.Interactor interactor = interactorConverter.psiToIntact(psiInteractor);
        CvObjectXref identityXref = CvObjectUtils.getPsiMiIdentityXref(interactor.getCvInteractorType());
        Assert.assertEquals(CvXrefQualifier.IDENTITY, identityXref.getCvXrefQualifier().getShortLabel());
        Assert.assertEquals(psiInteractor.getNames().getAliases().size(), interactor.getAliases().size());
        Assert.assertNotNull(interactor.getOwner());
    }
}
