package uk.ac.ebi.intact.mocks.cvexperimentalroles;

import uk.ac.ebi.intact.mocks.CvObjectMock;
import uk.ac.ebi.intact.mocks.IntactObjectSetter;
import uk.ac.ebi.intact.mocks.XrefMock;
import uk.ac.ebi.intact.mocks.cvDatabases.PsiMiMock;
import uk.ac.ebi.intact.mocks.cvXrefQualifiers.IdentityMock;
import uk.ac.ebi.intact.model.CvComponentRole;
import uk.ac.ebi.intact.model.CvObjectXref;

/**
 * TODO comment this
 *
 * @author Catherine Leroy (cleroy@ebi.ac.uk)
 * @version $Id$
 * @since TODO
 */
public class InhibitedMock {

    public static CvComponentRole getMock() {
        CvComponentRole inhibited = CvObjectMock.getMock(CvComponentRole.class, CvComponentRole.INHIBITED, CvComponentRole.INHIBITED);
        inhibited = (CvComponentRole) IntactObjectSetter.setBasicObject(inhibited);
        CvObjectXref xref = XrefMock.getMock(CvObjectXref.class, PsiMiMock.getMock(), IdentityMock.getMock(), CvComponentRole.INHIBITED_PSI_REF);
        inhibited.addXref(xref);
        return inhibited;
    }
}
