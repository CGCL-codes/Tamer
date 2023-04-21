package uk.ac.ebi.intact.dbupdate.prot;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.core.context.DataContext;
import uk.ac.ebi.intact.core.context.IntactContext;
import uk.ac.ebi.intact.core.unit.IntactBasicTestCase;
import uk.ac.ebi.intact.dbupdate.prot.event.ProteinEvent;
import uk.ac.ebi.intact.dbupdate.prot.listener.AbstractProteinUpdateProcessorListener;
import uk.ac.ebi.intact.model.*;
import uk.ac.ebi.intact.model.util.ProteinUtils;
import uk.ac.ebi.intact.uniprot.model.UniprotProtein;
import uk.ac.ebi.intact.util.protein.ComprehensiveCvPrimer;
import uk.ac.ebi.intact.util.protein.mock.MockUniprotProtein;
import uk.ac.ebi.intact.util.protein.mock.MockUniprotService;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * Second Tester of ProteinProcessor
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>19-Nov-2010</pre>
 */
@ContextConfiguration(locations = { "classpath*:/META-INF/dbupdate.spring.xml" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProteinProcessor2Test extends IntactBasicTestCase {

    ProteinProcessor processor;

    @Before
    public void before() throws Exception {
        TransactionStatus status = getDataContext().beginTransaction();
        ComprehensiveCvPrimer primer = new ComprehensiveCvPrimer(getDaoFactory());
        primer.createCVs();
        ProteinUpdateProcessorConfig config = ProteinUpdateContext.getInstance().getConfig();
        config.setUniprotService(new MockUniprotService());
        processor = new ProteinUpdateProcessor();
        getDataContext().commitTransaction(status);
    }

    @After
    public void after() throws Exception {
        processor = null;
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void update_protein_and_fix_duplicates() throws Exception {
        ProteinUpdateProcessorConfig config = ProteinUpdateContext.getInstance().getConfig();
        config.setFixDuplicates(true);
        DataContext context = getDataContext();
        TransactionStatus status = context.beginTransaction();
        Protein protein = getMockBuilder().createProtein("P60953", "protein");
        protein.getBioSource().setTaxId("9606");
        getCorePersister().saveOrUpdate(protein);
        Protein secondary = getMockBuilder().createProtein("P21181", "secondary");
        secondary.getBioSource().setTaxId("9606");
        getCorePersister().saveOrUpdate(secondary);
        Protein random = getMockBuilder().createProteinRandom();
        getCorePersister().saveOrUpdate(random);
        Interaction interaction = getMockBuilder().createInteraction(protein, random);
        getCorePersister().saveOrUpdate(interaction);
        Interaction interaction2 = getMockBuilder().createInteraction(secondary, random);
        getCorePersister().saveOrUpdate(interaction2);
        Assert.assertEquals(3, getDaoFactory().getProteinDao().countAll());
        Assert.assertEquals(1, protein.getActiveInstances().size());
        Assert.assertEquals(1, secondary.getActiveInstances().size());
        Set<String> updatedProteins = processor.update(protein, context);
        Assert.assertEquals(2, updatedProteins.size());
        Assert.assertNotNull(getDaoFactory().getProteinDao().getByAc(protein.getAc()));
        Assert.assertNull(getDaoFactory().getProteinDao().getByAc(secondary.getAc()));
        Assert.assertEquals(2, protein.getActiveInstances().size());
        UniprotProtein uniprot = MockUniprotProtein.build_CDC42_HUMAN();
        Assert.assertEquals(uniprot.getOrganism().getTaxid(), Integer.parseInt(protein.getBioSource().getTaxId()));
        Assert.assertEquals(uniprot.getId().toLowerCase(), protein.getShortLabel());
        Assert.assertEquals(uniprot.getDescription(), protein.getFullName());
        Assert.assertEquals(uniprot.getSequence(), protein.getSequence());
        Assert.assertEquals(uniprot.getCrc64(), protein.getCrc64());
        Assert.assertEquals(uniprot.getPrimaryAc(), ProteinUtils.getUniprotXref(protein).getPrimaryId());
        for (String secAc : uniprot.getSecondaryAcs()) {
            Assert.assertTrue(hasXRef(protein, secAc, CvDatabase.UNIPROT, CvXrefQualifier.SECONDARY_AC));
        }
        for (String geneName : uniprot.getGenes()) {
            Assert.assertTrue(hasAlias(protein, CvAliasType.GENE_NAME, geneName));
        }
        for (String syn : uniprot.getSynomyms()) {
            Assert.assertTrue(hasAlias(protein, CvAliasType.GENE_NAME_SYNONYM, syn));
        }
        for (String orf : uniprot.getOrfs()) {
            Assert.assertTrue(hasAlias(protein, CvAliasType.ORF_NAME, orf));
        }
        for (String locus : uniprot.getLocuses()) {
            Assert.assertTrue(hasAlias(protein, CvAliasType.LOCUS_NAME, locus));
        }
        Assert.assertEquals(15, protein.getXrefs().size());
        Assert.assertTrue(hasXRef(protein, secondary.getAc(), CvDatabase.INTACT, "intact-secondary"));
        context.commitTransaction(status);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void update_protein_and_fix_duplicates_no() throws Exception {
        ProteinUpdateProcessorConfig config = ProteinUpdateContext.getInstance().getConfig();
        config.setFixDuplicates(false);
        DataContext context = getDataContext();
        TransactionStatus status = context.beginTransaction();
        Protein protein = getMockBuilder().createProtein("P60953", "protein");
        protein.getBioSource().setTaxId("9606");
        getCorePersister().saveOrUpdate(protein);
        Protein secondary = getMockBuilder().createProtein("P21181", "secondary");
        secondary.getBioSource().setTaxId("9606");
        getCorePersister().saveOrUpdate(secondary);
        Protein random = getMockBuilder().createProteinRandom();
        getCorePersister().saveOrUpdate(random);
        Interaction interaction = getMockBuilder().createInteraction(protein, random);
        getCorePersister().saveOrUpdate(interaction);
        Interaction interaction2 = getMockBuilder().createInteraction(secondary, random);
        getCorePersister().saveOrUpdate(interaction2);
        Assert.assertEquals(3, getDaoFactory().getProteinDao().countAll());
        Assert.assertEquals(1, protein.getActiveInstances().size());
        Assert.assertEquals(1, secondary.getActiveInstances().size());
        Set<String> updatedProteins = processor.update(protein, context);
        Assert.assertEquals(2, updatedProteins.size());
        Assert.assertNotNull(getDaoFactory().getProteinDao().getByAc(protein.getAc()));
        Assert.assertNotNull(getDaoFactory().getProteinDao().getByAc(secondary.getAc()));
        Assert.assertEquals(1, protein.getActiveInstances().size());
        Assert.assertEquals(1, secondary.getActiveInstances().size());
        UniprotProtein uniprot = MockUniprotProtein.build_CDC42_HUMAN();
        Assert.assertEquals(uniprot.getOrganism().getTaxid(), Integer.parseInt(protein.getBioSource().getTaxId()));
        Assert.assertEquals(uniprot.getId().toLowerCase(), protein.getShortLabel());
        Assert.assertEquals(uniprot.getDescription(), protein.getFullName());
        Assert.assertEquals(uniprot.getSequence(), protein.getSequence());
        Assert.assertEquals(uniprot.getCrc64(), protein.getCrc64());
        Assert.assertEquals(uniprot.getPrimaryAc(), ProteinUtils.getUniprotXref(protein).getPrimaryId());
        for (String secAc : uniprot.getSecondaryAcs()) {
            Assert.assertTrue(hasXRef(protein, secAc, CvDatabase.UNIPROT, CvXrefQualifier.SECONDARY_AC));
        }
        for (String geneName : uniprot.getGenes()) {
            Assert.assertTrue(hasAlias(protein, CvAliasType.GENE_NAME, geneName));
        }
        for (String syn : uniprot.getSynomyms()) {
            Assert.assertTrue(hasAlias(protein, CvAliasType.GENE_NAME_SYNONYM, syn));
        }
        for (String orf : uniprot.getOrfs()) {
            Assert.assertTrue(hasAlias(protein, CvAliasType.ORF_NAME, orf));
        }
        for (String locus : uniprot.getLocuses()) {
            Assert.assertTrue(hasAlias(protein, CvAliasType.LOCUS_NAME, locus));
        }
        Assert.assertEquals(14, protein.getXrefs().size());
        Assert.assertFalse(hasXRef(protein, secondary.getAc(), CvDatabase.INTACT, "intact-secondary"));
        config.setFixDuplicates(true);
        context.commitTransaction(status);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void update_protein_and_fix_duplicates_isoform() throws Exception {
        ProteinUpdateProcessorConfig config = ProteinUpdateContext.getInstance().getConfig();
        config.setFixDuplicates(true);
        DataContext context = getDataContext();
        TransactionStatus status = context.beginTransaction();
        Protein protein = getMockBuilder().createProtein("P60953", "protein");
        protein.getBioSource().setTaxId("9606");
        getCorePersister().saveOrUpdate(protein);
        Protein secondary = getMockBuilder().createProtein("P21181", "secondary");
        secondary.getBioSource().setTaxId("9606");
        getCorePersister().saveOrUpdate(secondary);
        Protein isoform = getMockBuilder().createProteinSpliceVariant(protein, "P60953-1", "isoform1");
        isoform.getBioSource().setTaxId("9606");
        getCorePersister().saveOrUpdate(isoform);
        Protein isoform2 = getMockBuilder().createProteinSpliceVariant(secondary, "P21181-1", "isoform2");
        isoform2.getBioSource().setTaxId("9606");
        getCorePersister().saveOrUpdate(isoform2);
        InteractorXref ref = ProteinUtils.getUniprotXref(isoform2);
        ref.setPrimaryId("P60953-1");
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(isoform2);
        Protein random = getMockBuilder().createProteinRandom();
        getCorePersister().saveOrUpdate(random);
        Interaction interaction = getMockBuilder().createInteraction(protein, random);
        getCorePersister().saveOrUpdate(interaction);
        Interaction interaction2 = getMockBuilder().createInteraction(secondary, random);
        getCorePersister().saveOrUpdate(interaction2);
        Interaction interaction3 = getMockBuilder().createInteraction(isoform, random);
        getCorePersister().saveOrUpdate(interaction3);
        Interaction interaction4 = getMockBuilder().createInteraction(isoform2, random);
        getCorePersister().saveOrUpdate(interaction4);
        Assert.assertEquals(5, getDaoFactory().getProteinDao().countAll());
        Assert.assertEquals(1, protein.getActiveInstances().size());
        Assert.assertEquals(1, secondary.getActiveInstances().size());
        Assert.assertEquals(1, isoform.getActiveInstances().size());
        Assert.assertEquals(1, isoform2.getActiveInstances().size());
        Set<String> updatedProteins = processor.update(protein, context);
        Assert.assertEquals(4, updatedProteins.size());
        Assert.assertNotNull(getDaoFactory().getProteinDao().getByAc(protein.getAc()));
        Assert.assertNull(getDaoFactory().getProteinDao().getByAc(secondary.getAc()));
        Assert.assertNotNull(getDaoFactory().getProteinDao().getByAc(isoform.getAc()));
        Assert.assertNull(getDaoFactory().getProteinDao().getByAc(isoform2.getAc()));
        Assert.assertEquals(2, protein.getActiveInstances().size());
        Assert.assertEquals(2, isoform.getActiveInstances().size());
        Assert.assertTrue(hasXRef(protein, secondary.getAc(), CvDatabase.INTACT, "intact-secondary"));
        Assert.assertTrue(hasXRef(isoform, isoform2.getAc(), CvDatabase.INTACT, "intact-secondary"));
        Assert.assertTrue(hasXRef(isoform, protein.getAc(), CvDatabase.INTACT, CvXrefQualifier.ISOFORM_PARENT));
        Assert.assertTrue(hasXRef(isoform2, protein.getAc(), CvDatabase.INTACT, CvXrefQualifier.ISOFORM_PARENT));
        context.commitTransaction(status);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void update_protein_and_fix_duplicates_isoforms_no() throws Exception {
        ProteinUpdateProcessorConfig config = ProteinUpdateContext.getInstance().getConfig();
        config.setFixDuplicates(false);
        DataContext context = getDataContext();
        TransactionStatus status = context.beginTransaction();
        Protein protein = getMockBuilder().createProtein("P60953", "protein");
        protein.getBioSource().setTaxId("9606");
        getCorePersister().saveOrUpdate(protein);
        Protein secondary = getMockBuilder().createProtein("P21181", "secondary");
        secondary.getBioSource().setTaxId("9606");
        getCorePersister().saveOrUpdate(secondary);
        Protein isoform = getMockBuilder().createProteinSpliceVariant(protein, "P60953-1", "isoform1");
        isoform.getBioSource().setTaxId("9606");
        getCorePersister().saveOrUpdate(isoform);
        Protein isoform2 = getMockBuilder().createProteinSpliceVariant(secondary, "P21181-1", "isoform2");
        isoform2.getBioSource().setTaxId("9606");
        getCorePersister().saveOrUpdate(isoform2);
        InteractorXref ref = ProteinUtils.getUniprotXref(isoform2);
        ref.setPrimaryId("P60953-1");
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(isoform2);
        Protein random = getMockBuilder().createProteinRandom();
        getCorePersister().saveOrUpdate(random);
        Interaction interaction = getMockBuilder().createInteraction(protein, random);
        getCorePersister().saveOrUpdate(interaction);
        Interaction interaction2 = getMockBuilder().createInteraction(secondary, random);
        getCorePersister().saveOrUpdate(interaction2);
        Interaction interaction3 = getMockBuilder().createInteraction(isoform, random);
        getCorePersister().saveOrUpdate(interaction3);
        Interaction interaction4 = getMockBuilder().createInteraction(isoform2, random);
        getCorePersister().saveOrUpdate(interaction4);
        Assert.assertEquals(5, getDaoFactory().getProteinDao().countAll());
        Assert.assertEquals(1, protein.getActiveInstances().size());
        Assert.assertEquals(1, secondary.getActiveInstances().size());
        Assert.assertEquals(1, isoform.getActiveInstances().size());
        Assert.assertEquals(1, isoform2.getActiveInstances().size());
        Set<String> updatedProteins = processor.update(protein, context);
        Assert.assertEquals(4, updatedProteins.size());
        Assert.assertNotNull(getDaoFactory().getProteinDao().getByAc(protein.getAc()));
        Assert.assertNotNull(getDaoFactory().getProteinDao().getByAc(secondary.getAc()));
        Assert.assertNotNull(getDaoFactory().getProteinDao().getByAc(isoform.getAc()));
        Assert.assertNotNull(getDaoFactory().getProteinDao().getByAc(isoform2.getAc()));
        Assert.assertEquals(1, protein.getActiveInstances().size());
        Assert.assertEquals(1, secondary.getActiveInstances().size());
        Assert.assertEquals(1, isoform.getActiveInstances().size());
        Assert.assertEquals(1, isoform2.getActiveInstances().size());
        Assert.assertFalse(hasXRef(protein, secondary.getAc(), CvDatabase.INTACT, "intact-secondary"));
        Assert.assertFalse(hasXRef(isoform, isoform2.getAc(), CvDatabase.INTACT, "intact-secondary"));
        Assert.assertTrue(hasXRef(isoform, protein.getAc(), CvDatabase.INTACT, CvXrefQualifier.ISOFORM_PARENT));
        Assert.assertFalse(hasXRef(isoform2, protein.getAc(), CvDatabase.INTACT, CvXrefQualifier.ISOFORM_PARENT));
        context.commitTransaction(status);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void update_protein_and_fix_duplicates_conflicts_no_transcripts() throws Exception {
        ProteinUpdateProcessorConfig config = ProteinUpdateContext.getInstance().getConfig();
        config.setFixDuplicates(true);
        config.setGlobalProteinUpdate(false);
        config.setDeleteProteinTranscriptWithoutInteractions(false);
        DataContext context = getDataContext();
        TransactionStatus status = context.beginTransaction();
        UniprotProtein uniprot = MockUniprotProtein.build_CDC42_HUMAN();
        Protein secondary = getMockBuilder().createProtein("P21181", "secondary");
        secondary.getBioSource().setTaxId("9606");
        secondary.setCreated(new Date(1));
        getCorePersister().saveOrUpdate(secondary);
        Protein protein = getMockBuilder().createProtein(uniprot.getPrimaryAc(), "intact");
        protein.getBioSource().setTaxId("9606");
        protein.setSequence("AAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        protein.getAnnotations().clear();
        protein.getAliases().clear();
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(protein);
        Protein random = getMockBuilder().createProteinRandom();
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(random);
        Range range = getMockBuilder().createRange(2, 2, 5, 5);
        Feature feature = getMockBuilder().createFeatureRandom();
        feature.getRanges().clear();
        feature.addRange(range);
        Interaction interaction = getMockBuilder().createInteraction(protein, random);
        Component componentWithFeatureConflicts = null;
        for (Component c : interaction.getComponents()) {
            c.getBindingDomains().clear();
            if (c.getInteractor().getAc().equals(protein.getAc())) {
                c.addBindingDomain(feature);
                componentWithFeatureConflicts = c;
            }
        }
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(interaction);
        Interaction interaction2 = getMockBuilder().createInteraction(secondary, random);
        for (Component c : interaction2.getComponents()) {
            c.getBindingDomains().clear();
        }
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(interaction2);
        Assert.assertEquals(3, getDaoFactory().getProteinDao().countAll());
        Assert.assertEquals(1, protein.getActiveInstances().size());
        Assert.assertEquals(1, secondary.getActiveInstances().size());
        Set<String> updatedProteins = processor.update(protein, context);
        Assert.assertEquals(4, updatedProteins.size());
        Assert.assertNotNull(getDaoFactory().getProteinDao().getByAc(protein.getAc()));
        Assert.assertNotNull(getDaoFactory().getProteinDao().getByAc(secondary.getAc()));
        Assert.assertEquals(1, protein.getActiveInstances().size());
        Assert.assertEquals(1, secondary.getActiveInstances().size());
        Assert.assertFalse(hasXRef(secondary, protein.getAc(), CvDatabase.INTACT, "intact-secondary"));
        Assert.assertEquals(2, range.getFromIntervalStart());
        Assert.assertEquals(2, range.getFromIntervalEnd());
        Assert.assertEquals(5, range.getToIntervalStart());
        Assert.assertEquals(5, range.getToIntervalEnd());
        Protein noUniprotUpdate = (Protein) componentWithFeatureConflicts.getInteractor();
        Assert.assertFalse(ProteinUtils.isFromUniprot(noUniprotUpdate));
        Assert.assertEquals(protein.getAc(), noUniprotUpdate.getAc());
        Assert.assertEquals(5, getDaoFactory().getProteinDao().countAll());
        config.setDeleteProteinTranscriptWithoutInteractions(true);
        context.commitTransaction(status);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void update_protein_and_fix_duplicates_conflicts_transcripts_yes() throws Exception {
        ProteinUpdateProcessorConfig config = ProteinUpdateContext.getInstance().getConfig();
        config.setFixDuplicates(true);
        config.setGlobalProteinUpdate(false);
        config.setDeleteProteinTranscriptWithoutInteractions(false);
        DataContext context = getDataContext();
        TransactionStatus status = context.beginTransaction();
        UniprotProtein uniprot = MockUniprotProtein.build_CDC42_HUMAN();
        Protein secondary = getMockBuilder().createProtein("P21181", "secondary");
        secondary.getBioSource().setTaxId("9606");
        secondary.setCreated(new Date(1));
        getCorePersister().saveOrUpdate(secondary);
        Protein protein = getMockBuilder().createProtein(uniprot.getPrimaryAc(), "intact");
        protein.getBioSource().setTaxId("9606");
        protein.setSequence("SYPQTDVFLVCFSVVSPSSFENVKEKWVPEITHHHH");
        protein.getAnnotations().clear();
        protein.getAliases().clear();
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(protein);
        Protein random = getMockBuilder().createProteinRandom();
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(random);
        Range range = getMockBuilder().createRange(30, 30, 36, 36);
        Feature feature = getMockBuilder().createFeatureRandom();
        feature.getRanges().clear();
        feature.addRange(range);
        Interaction interaction = getMockBuilder().createInteraction(protein, random);
        for (Component c : interaction.getComponents()) {
            c.getBindingDomains().clear();
            if (c.getInteractor().getAc().equals(protein.getAc())) {
                c.addBindingDomain(feature);
            }
        }
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(interaction);
        Interaction interaction2 = getMockBuilder().createInteraction(secondary, random);
        for (Component c : interaction2.getComponents()) {
            c.getBindingDomains().clear();
        }
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(interaction2);
        Assert.assertEquals(3, getDaoFactory().getProteinDao().countAll());
        Assert.assertEquals(1, protein.getActiveInstances().size());
        Assert.assertEquals(1, secondary.getActiveInstances().size());
        Set<String> updatedProteins = processor.update(protein, context);
        Assert.assertEquals(4, updatedProteins.size());
        Assert.assertNull(getDaoFactory().getProteinDao().getByAc(protein.getAc()));
        Assert.assertNotNull(getDaoFactory().getProteinDao().getByAc(secondary.getAc()));
        Assert.assertEquals(1, secondary.getActiveInstances().size());
        Protein isoform2 = getDaoFactory().getProteinDao().getByUniprotId("P60953-2").iterator().next();
        Assert.assertEquals(1, isoform2.getActiveInstances().size());
        Assert.assertTrue(hasXRef(isoform2, protein.getAc(), CvDatabase.INTACT, "intact-secondary"));
        Assert.assertTrue(hasXRef(isoform2, secondary.getAc(), CvDatabase.INTACT, CvXrefQualifier.ISOFORM_PARENT));
        Assert.assertEquals(30, range.getFromIntervalStart());
        Assert.assertEquals(30, range.getFromIntervalEnd());
        Assert.assertEquals(36, range.getToIntervalStart());
        Assert.assertEquals(36, range.getToIntervalEnd());
        Assert.assertEquals(4, getDaoFactory().getProteinDao().countAll());
        config.setDeleteProteinTranscriptWithoutInteractions(true);
        context.commitTransaction(status);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void update_protein_and_fix_isoforms_duplicates_conflicts_no_transcripts() throws Exception {
        ProteinUpdateProcessorConfig config = ProteinUpdateContext.getInstance().getConfig();
        config.setFixDuplicates(true);
        config.setGlobalProteinUpdate(false);
        config.setDeleteProteinTranscriptWithoutInteractions(false);
        DataContext context = getDataContext();
        TransactionStatus status = context.beginTransaction();
        Protein secondary = getMockBuilder().createProtein("P21181", "secondary");
        secondary.getBioSource().setTaxId("9606");
        getCorePersister().saveOrUpdate(secondary);
        Protein isoform = getMockBuilder().createProteinSpliceVariant(secondary, "P60953-1", "isoformValid");
        isoform.getBioSource().setTaxId("9606");
        getCorePersister().saveOrUpdate(isoform);
        Protein isoform2 = getMockBuilder().createProteinSpliceVariant(secondary, "P21181-1", "duplicate");
        isoform2.getBioSource().setTaxId("9606");
        isoform2.setSequence("AAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        isoform2.getAnnotations().clear();
        isoform2.getAliases().clear();
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(isoform2);
        InteractorXref ref = ProteinUtils.getUniprotXref(isoform2);
        ref.setPrimaryId("P60953-1");
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(isoform2);
        Protein random = getMockBuilder().createProteinRandom();
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(random);
        Range range = getMockBuilder().createRange(2, 2, 5, 5);
        Feature feature = getMockBuilder().createFeatureRandom();
        feature.getRanges().clear();
        feature.addRange(range);
        Interaction interaction = getMockBuilder().createInteraction(isoform2, random);
        Component componentWithFeatureConflicts = null;
        for (Component c : interaction.getComponents()) {
            c.getBindingDomains().clear();
            if (c.getInteractor().getAc().equals(isoform2.getAc())) {
                c.addBindingDomain(feature);
                componentWithFeatureConflicts = c;
            }
        }
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(interaction);
        Interaction interaction2 = getMockBuilder().createInteraction(secondary, random);
        for (Component c : interaction2.getComponents()) {
            c.getBindingDomains().clear();
        }
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(interaction2);
        Interaction interaction3 = getMockBuilder().createInteraction(isoform, random);
        for (Component c : interaction3.getComponents()) {
            c.getBindingDomains().clear();
        }
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(interaction3);
        Assert.assertEquals(4, getDaoFactory().getProteinDao().countAll());
        Assert.assertEquals(1, isoform2.getActiveInstances().size());
        Assert.assertEquals(1, secondary.getActiveInstances().size());
        Assert.assertEquals(1, isoform.getActiveInstances().size());
        Set<String> updatedProteins = processor.update(isoform2, context);
        Assert.assertEquals(4, updatedProteins.size());
        Assert.assertNotNull(getDaoFactory().getProteinDao().getByAc(isoform2.getAc()));
        Assert.assertNotNull(getDaoFactory().getProteinDao().getByAc(secondary.getAc()));
        Assert.assertEquals(1, isoform2.getActiveInstances().size());
        Assert.assertEquals(1, secondary.getActiveInstances().size());
        Assert.assertEquals(1, isoform.getActiveInstances().size());
        Assert.assertFalse(hasXRef(isoform, isoform2.getAc(), CvDatabase.INTACT, "intact-secondary"));
        Assert.assertEquals(2, range.getFromIntervalStart());
        Assert.assertEquals(2, range.getFromIntervalEnd());
        Assert.assertEquals(5, range.getToIntervalStart());
        Assert.assertEquals(5, range.getToIntervalEnd());
        Protein noUniprotUpdate = (Protein) componentWithFeatureConflicts.getInteractor();
        Assert.assertFalse(ProteinUtils.isFromUniprot(noUniprotUpdate));
        Assert.assertEquals(isoform2.getAc(), noUniprotUpdate.getAc());
        Assert.assertEquals(5, getDaoFactory().getProteinDao().countAll());
        config.setDeleteProteinTranscriptWithoutInteractions(true);
        context.commitTransaction(status);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void update_protein_and_fix_isoform_duplicates_conflicts_transcripts_yes() throws Exception {
        ProteinUpdateProcessorConfig config = ProteinUpdateContext.getInstance().getConfig();
        config.setFixDuplicates(true);
        config.setGlobalProteinUpdate(false);
        config.setDeleteProteinTranscriptWithoutInteractions(false);
        DataContext context = getDataContext();
        TransactionStatus status = context.beginTransaction();
        Protein secondary = getMockBuilder().createProtein("P21181", "secondary");
        secondary.getBioSource().setTaxId("9606");
        getCorePersister().saveOrUpdate(secondary);
        Protein isoform = getMockBuilder().createProteinSpliceVariant(secondary, "P60953-1", "isoformValid");
        isoform.getBioSource().setTaxId("9606");
        isoform.setCreated(new Date(1));
        getCorePersister().saveOrUpdate(isoform);
        Protein isoform2 = getMockBuilder().createProteinSpliceVariant(secondary, "P21181-1", "duplicate");
        isoform2.getBioSource().setTaxId("9606");
        isoform2.setSequence("SYPQTDVFLVCFSVVSPSSFENVKEKWVPEITHHHH");
        isoform2.getAnnotations().clear();
        isoform2.getAliases().clear();
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(isoform2);
        InteractorXref ref = ProteinUtils.getUniprotXref(isoform2);
        ref.setPrimaryId("P60953-1");
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(isoform2);
        Protein random = getMockBuilder().createProteinRandom();
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(random);
        Range range = getMockBuilder().createRange(30, 30, 36, 36);
        Feature feature = getMockBuilder().createFeatureRandom();
        feature.getRanges().clear();
        feature.addRange(range);
        Interaction interaction = getMockBuilder().createInteraction(isoform2, random);
        for (Component c : interaction.getComponents()) {
            c.getBindingDomains().clear();
            if (c.getInteractor().getAc().equals(isoform2.getAc())) {
                c.addBindingDomain(feature);
            }
        }
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(interaction);
        Interaction interaction2 = getMockBuilder().createInteraction(secondary, random);
        for (Component c : interaction2.getComponents()) {
            c.getBindingDomains().clear();
        }
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(interaction2);
        Interaction interaction3 = getMockBuilder().createInteraction(isoform, random);
        for (Component c : interaction3.getComponents()) {
            c.getBindingDomains().clear();
        }
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(interaction3);
        Assert.assertEquals(4, getDaoFactory().getProteinDao().countAll());
        Assert.assertEquals(1, isoform2.getActiveInstances().size());
        Assert.assertEquals(1, secondary.getActiveInstances().size());
        Assert.assertEquals(1, isoform.getActiveInstances().size());
        Set<String> updatedProteins = processor.update(isoform2, context);
        Assert.assertEquals(4, updatedProteins.size());
        Assert.assertNull(getDaoFactory().getProteinDao().getByAc(isoform2.getAc()));
        Assert.assertNotNull(getDaoFactory().getProteinDao().getByAc(secondary.getAc()));
        Assert.assertEquals(0, isoform2.getActiveInstances().size());
        Assert.assertEquals(1, secondary.getActiveInstances().size());
        Assert.assertEquals(1, isoform.getActiveInstances().size());
        Protein isoformLoaded = getDaoFactory().getProteinDao().getByUniprotId("P60953-2").iterator().next();
        Assert.assertEquals(1, isoformLoaded.getActiveInstances().size());
        Assert.assertTrue(hasXRef(isoformLoaded, isoform2.getAc(), CvDatabase.INTACT, "intact-secondary"));
        Assert.assertEquals(30, range.getFromIntervalStart());
        Assert.assertEquals(30, range.getFromIntervalEnd());
        Assert.assertEquals(36, range.getToIntervalStart());
        Assert.assertEquals(36, range.getToIntervalEnd());
        Assert.assertEquals(4, getDaoFactory().getProteinDao().countAll());
        config.setDeleteProteinTranscriptWithoutInteractions(true);
        context.commitTransaction(status);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void update_protein_bad_ranges_from_beginning() throws Exception {
        ProteinUpdateProcessorConfig config = ProteinUpdateContext.getInstance().getConfig();
        config.setFixDuplicates(true);
        config.setGlobalProteinUpdate(false);
        config.setDeleteProteinTranscriptWithoutInteractions(false);
        DataContext context = getDataContext();
        TransactionStatus status = context.beginTransaction();
        Protein secondary = getMockBuilder().createProtein("P21181", "secondary");
        secondary.getBioSource().setTaxId("9606");
        getCorePersister().saveOrUpdate(secondary);
        Protein random = getMockBuilder().createProteinRandom();
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(random);
        Range range = getMockBuilder().createRange(0, 0, 5, 5);
        Feature feature = getMockBuilder().createFeatureRandom();
        feature.getRanges().clear();
        feature.addRange(range);
        Interaction interaction = getMockBuilder().createInteraction(secondary, random);
        Component componentWithFeatureConflicts = null;
        for (Component c : interaction.getComponents()) {
            c.getBindingDomains().clear();
            if (c.getInteractor().getAc().equals(secondary.getAc())) {
                c.addBindingDomain(feature);
                componentWithFeatureConflicts = c;
            }
        }
        IntactContext.getCurrentInstance().getCorePersister().saveOrUpdate(interaction);
        Assert.assertEquals(2, getDaoFactory().getProteinDao().countAll());
        Assert.assertEquals(1, secondary.getActiveInstances().size());
        Assert.assertEquals(1, secondary.getActiveInstances().size());
        Set<String> updatedProteins = processor.update(secondary, context);
        Assert.assertEquals(3, updatedProteins.size());
        Assert.assertNotNull(getDaoFactory().getProteinDao().getByAc(secondary.getAc()));
        Assert.assertEquals(4, getDaoFactory().getProteinDao().countAll());
        Assert.assertEquals(1, secondary.getActiveInstances().size());
        Assert.assertEquals(0, range.getFromIntervalStart());
        Assert.assertEquals(0, range.getFromIntervalEnd());
        Assert.assertEquals(0, range.getToIntervalStart());
        Assert.assertEquals(0, range.getToIntervalEnd());
        Assert.assertTrue(hasAnnotation(feature, null, "invalid-range"));
        Assert.assertTrue(hasAnnotation(feature, "[" + range.getAc() + "]0-5", "invalid-positions"));
        Protein noUniprotUpdate = (Protein) componentWithFeatureConflicts.getInteractor();
        Assert.assertTrue(ProteinUtils.isFromUniprot(noUniprotUpdate));
        Assert.assertEquals(secondary.getAc(), noUniprotUpdate.getAc());
        config.setDeleteProteinTranscriptWithoutInteractions(true);
        context.commitTransaction(status);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void updateAll_oneElementToBeProcessedRemoved() throws Exception {
        DataContext context = getDataContext();
        TransactionStatus status = context.beginTransaction();
        final Protein protMain = getMockBuilder().createProtein("P12345", "main");
        protMain.setCreated(new Date(1));
        final Protein protRemoved = getMockBuilder().createProtein("Q01010", "removed");
        final Protein prot1 = getMockBuilder().createProteinRandom();
        final Protein prot2 = getMockBuilder().createProteinRandom();
        final Protein prot3 = getMockBuilder().createProteinRandom();
        final Protein prot4 = getMockBuilder().createProteinRandom();
        getCorePersister().saveOrUpdate(prot1, protMain, prot2, prot3, protRemoved, prot4);
        Assert.assertNotNull(protRemoved.getAc());
        context.commitTransaction(status);
        ProteinProcessor processor = new ProteinProcessor(3, 2) {

            protected void registerListeners() {
                addListener(new AbstractProteinUpdateProcessorListener() {

                    public void onPreProcess(ProteinEvent evt) throws ProcessorException {
                        if ("main".equals(evt.getProtein().getShortLabel())) {
                            getDaoFactory().getProteinDao().deleteByAc(protRemoved.getAc());
                        }
                    }
                });
            }
        };
        processor.updateAll();
    }

    private boolean hasXRef(Protein p, String primaryAc, String databaseName, String qualifierName) {
        final Collection<InteractorXref> refs = p.getXrefs();
        boolean hasXRef = false;
        for (InteractorXref ref : refs) {
            if (databaseName.equalsIgnoreCase(ref.getCvDatabase().getShortLabel())) {
                if (qualifierName.equalsIgnoreCase(ref.getCvXrefQualifier().getShortLabel())) {
                    if (primaryAc.equalsIgnoreCase(ref.getPrimaryId())) {
                        hasXRef = true;
                    }
                }
            }
        }
        return hasXRef;
    }

    private boolean hasAlias(Protein p, String aliasLabel, String aliasName) {
        final Collection<InteractorAlias> aliases = p.getAliases();
        boolean hasFoundAlias = false;
        for (InteractorAlias alias : aliases) {
            if (alias.getCvAliasType().getShortLabel().equals(aliasLabel)) {
                if (aliasName.equals(alias.getName())) {
                    hasFoundAlias = true;
                }
            }
        }
        return hasFoundAlias;
    }

    private boolean hasAnnotation(Feature p, String text, String cvTopic) {
        final Collection<Annotation> annotations = p.getAnnotations();
        boolean hasAnnotation = false;
        for (Annotation a : annotations) {
            if (cvTopic.equalsIgnoreCase(a.getCvTopic().getShortLabel())) {
                if (text == null) {
                    hasAnnotation = true;
                } else if (text != null && a.getAnnotationText() != null) {
                    if (text.equalsIgnoreCase(a.getAnnotationText())) {
                        hasAnnotation = true;
                    }
                }
            }
        }
        return hasAnnotation;
    }
}
