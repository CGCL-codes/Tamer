package uk.ac.ebi.intact.mocks.cvXrefQualifiers;

import uk.ac.ebi.intact.model.CvXrefQualifier;
import uk.ac.ebi.intact.model.CvObjectXref;
import uk.ac.ebi.intact.model.Institution;
import uk.ac.ebi.intact.model.util.CvObjectBuilder;
import uk.ac.ebi.intact.mocks.CvObjectMock;
import uk.ac.ebi.intact.mocks.XrefMock;
import uk.ac.ebi.intact.mocks.IntactObjectSetter;
import uk.ac.ebi.intact.mocks.cvDatabases.PsiMiMock;

/**
 * TODO comment this
 *
 * @author Catherine Leroy (cleroy@ebi.ac.uk)
 * @version $Id$
 * @since TODO
 */
public class IdentityMock {

    public static CvXrefQualifier getMock() {
        return new CvObjectBuilder().createIdentityCvXrefQualifier(new Institution("lala"));
    }
}
