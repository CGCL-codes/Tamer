package uk.ac.ebi.intact.sanity.rules.smallmolecule;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.intact.core.unit.IntactMockBuilder;
import uk.ac.ebi.intact.model.CvDatabase;
import uk.ac.ebi.intact.model.InteractorXref;
import uk.ac.ebi.intact.model.SmallMolecule;
import uk.ac.ebi.intact.model.CvObjectXref;
import uk.ac.ebi.intact.model.util.CvObjectUtils;
import uk.ac.ebi.intact.sanity.commons.rules.GeneralMessage;
import uk.ac.ebi.intact.sanity.commons.rules.MessageDefinition;
import uk.ac.ebi.intact.sanity.commons.rules.Rule;
import java.util.Collection;

/**
 * SmallMoleculeIdentity Tester.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @since 2.0.0
 * @version $Id$
 */
public class SmallMoleculeIdentityTest {

    @Test
    public void check_identity_is_not_chebi() throws Exception {
        IntactMockBuilder mockBuilder = new IntactMockBuilder();
        final SmallMolecule sm = mockBuilder.createSmallMoleculeRandom();
        Assert.assertEquals(1, sm.getXrefs().size());
        final InteractorXref xref = sm.getXrefs().iterator().next();
        final CvObjectXref dbXref = CvObjectUtils.getPsiMiIdentityXref(xref.getCvDatabase());
        Assert.assertEquals(CvDatabase.CHEBI_MI_REF, dbXref.getPrimaryId());
        CvDatabase cabri = mockBuilder.createCvObject(CvDatabase.class, CvDatabase.CABRI_MI_REF, CvDatabase.CABRI);
        xref.setCvDatabase(cabri);
        Rule rule = new SmallMoleculeIdentity();
        final Collection<GeneralMessage> messages = rule.check(sm);
        Assert.assertNotNull(messages);
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals(MessageDefinition.SMALL_MOLECULE_IDENTITY_INVALID_DB, messages.iterator().next().getMessageDefinition());
    }

    @Test
    public void check_many_identities() throws Exception {
        IntactMockBuilder mockBuilder = new IntactMockBuilder();
        final SmallMolecule sm = mockBuilder.createSmallMoleculeRandom();
        sm.addXref(mockBuilder.createIdentityXrefChebi(sm, "chebi:xxxxx"));
        Rule rule = new SmallMoleculeIdentity();
        final Collection<GeneralMessage> messages = rule.check(sm);
        Assert.assertNotNull(messages);
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals(MessageDefinition.SMALL_MOLECULE_IDENTITY_MULTIPLE, messages.iterator().next().getMessageDefinition());
    }

    @Test
    public void check_no_identities() throws Exception {
        IntactMockBuilder mockBuilder = new IntactMockBuilder();
        final SmallMolecule sm = mockBuilder.createSmallMoleculeRandom();
        sm.getXrefs().clear();
        Rule rule = new SmallMoleculeIdentity();
        final Collection<GeneralMessage> messages = rule.check(sm);
        Assert.assertNotNull(messages);
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals(MessageDefinition.SMALL_MOLECULE_IDENTITY_MISSING, messages.iterator().next().getMessageDefinition());
    }
}
