package uk.ac.ebi.intact.dataexchange.psimi.xml.converter.shared;

import org.junit.Assert;
import org.junit.Test;
import psidev.psi.mi.xml.model.Alias;
import psidev.psi.mi.xml.model.Entry;
import psidev.psi.mi.xml.model.EntrySet;
import psidev.psi.mi.xml.model.Interactor;
import uk.ac.ebi.intact.model.CvAliasType;
import uk.ac.ebi.intact.model.InteractorAlias;
import uk.ac.ebi.intact.model.Protein;
import uk.ac.ebi.intact.model.util.CvObjectUtils;

/**
 * TODO comment this
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: AliasConverterTest.java 9236 2007-07-31 14:50:49Z baranda $
 */
public class AliasConverterTest extends AbstractConverterTest {

    @Test
    public void psiToIntact_cvTypeNotNull() throws Exception {
        EntrySet entrySet = getIntactEntrySet();
        Entry entry = entrySet.getEntries().iterator().next();
        Interactor interactor = null;
        for (Interactor candidateInteractor : entry.getInteractors()) {
            if (candidateInteractor.getId() == 6) {
                interactor = candidateInteractor;
            }
        }
        Assert.assertNotNull("Interactor with id 6 must exist", interactor);
        Alias alias = null;
        for (Alias candidateAlias : interactor.getNames().getAliases()) {
            if (candidateAlias.getTypeAc().equals(CvAliasType.GENE_NAME_MI_REF)) {
                alias = candidateAlias;
            }
        }
        Assert.assertNotNull("Alias with gene name must exist", alias);
        AliasConverter aliasConverter = new AliasConverter(getMockInstitution(), InteractorAlias.class);
        uk.ac.ebi.intact.model.Alias intactAlias = aliasConverter.psiToIntact(alias);
        Assert.assertNotNull(intactAlias);
        Assert.assertEquals(alias.getValue(), intactAlias.getName());
        Assert.assertNotNull(intactAlias.getCvAliasType());
        Assert.assertEquals(CvAliasType.GENE_NAME_MI_REF, CvObjectUtils.getPsiMiIdentityXref(intactAlias.getCvAliasType()).getPrimaryId());
    }

    @Test
    public void psiToIntact_cvTypeNull() throws Exception {
        Alias alias = PsiMockFactory.createAlias("alias1", null, null);
        AliasConverter<InteractorAlias> aliasConverter = new AliasConverter<InteractorAlias>(getMockInstitution(), InteractorAlias.class);
        uk.ac.ebi.intact.model.Alias intactAlias = aliasConverter.psiToIntact(alias);
        Assert.assertNotNull(intactAlias);
        Assert.assertNull(intactAlias.getCvAliasType());
        Assert.assertEquals("alias1", intactAlias.getName());
    }

    @Test
    public void intactToPsi_cvTypeNotNull() throws Exception {
        Protein protein = getIntactMockBuilder().createProteinRandom();
        Assert.assertFalse(protein.getAliases().isEmpty());
        InteractorAlias interactorAlias = protein.getAliases().iterator().next();
        AliasConverter<InteractorAlias> aliasConverter = new AliasConverter<InteractorAlias>(getMockInstitution(), InteractorAlias.class);
        Alias alias = aliasConverter.intactToPsi(interactorAlias);
        Assert.assertNotNull(alias);
        Assert.assertEquals(interactorAlias.getName(), alias.getValue());
        Assert.assertEquals(CvAliasType.GENE_NAME_MI_REF, alias.getTypeAc());
        Assert.assertEquals(CvAliasType.GENE_NAME, alias.getType());
    }

    @Test
    public void intactToPsi_cvTypeNull() throws Exception {
        Protein protein = getIntactMockBuilder().createProteinRandom();
        Assert.assertFalse(protein.getAliases().isEmpty());
        InteractorAlias interactorAlias = protein.getAliases().iterator().next();
        interactorAlias.setCvAliasType(null);
        AliasConverter<InteractorAlias> aliasConverter = new AliasConverter<InteractorAlias>(getMockInstitution(), InteractorAlias.class);
        Alias alias = aliasConverter.intactToPsi(interactorAlias);
        Assert.assertNotNull(alias);
        Assert.assertEquals(interactorAlias.getName(), alias.getValue());
        Assert.assertNull(alias.getTypeAc());
        Assert.assertNull(alias.getType());
    }
}
