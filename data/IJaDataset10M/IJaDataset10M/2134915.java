package uk.ac.ebi.intact.dataexchange.psimi.xml.exchange;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import psidev.psi.mi.xml.model.*;
import psidev.psi.mi.xml.PsimiXmlReader;
import uk.ac.ebi.intact.core.persister.stats.PersisterStatistics;
import uk.ac.ebi.intact.model.*;
import uk.ac.ebi.intact.model.Feature;
import uk.ac.ebi.intact.model.Interactor;
import uk.ac.ebi.intact.model.Alias;
import uk.ac.ebi.intact.model.Interaction;
import uk.ac.ebi.intact.model.util.CvObjectUtils;
import uk.ac.ebi.intact.core.context.IntactContext;
import uk.ac.ebi.intact.core.context.DataContext;
import uk.ac.ebi.intact.core.persistence.dao.DaoFactory;
import uk.ac.ebi.intact.core.util.DebugUtil;
import uk.ac.ebi.intact.dataexchange.psimi.xml.converter.ConverterContext;
import uk.ac.ebi.intact.dataexchange.psimi.xml.converter.ExportProfile;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

/**
 * PsiExchange Tester.
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: PsiExchangeTest.java 13389 2009-07-09 11:22:44Z samuel.kerrien $
 */
public class PsiExchangeTest extends AbstractPsiExchangeTest {

    @Autowired
    private PsiExchange psiExchange;

    @Test
    public void importXml_intact2() throws Exception {
        EntrySet set = getIntactEntrySet();
        PersisterStatistics stats = psiExchange.importIntoIntact(set);
        Assert.assertEquals(6, stats.getPersistedCount(InteractionImpl.class, false));
        Assert.assertEquals(0, stats.getDuplicatesCount(InteractionImpl.class, false));
        Assert.assertEquals(6, getDaoFactory().getInteractionDao().countAll());
        Assert.assertEquals(6, getDaoFactory().getInteractionDao().countAll());
        IntactContext intactContext = IntactContext.getCurrentInstance();
        DataContext dataContext = intactContext.getDataContext();
        DaoFactory daoFactory = dataContext.getDaoFactory();
        final Collection<InteractionImpl> interactions = daoFactory.getInteractionDao().getAll();
        IntactEntry intactEntry = new IntactEntry(new ArrayList<Interaction>(interactions));
        Writer writer = new StringWriter();
        psiExchange.exportToPsiXml(writer, intactEntry);
    }

    @Test
    public void importXml_intact() throws Exception {
        EntrySet set = getIntactEntrySet();
        PersisterStatistics stats = psiExchange.importIntoIntact(set);
        Assert.assertEquals(6, stats.getPersistedCount(InteractionImpl.class, false));
        Assert.assertEquals(0, stats.getDuplicatesCount(InteractionImpl.class, false));
        Assert.assertEquals(6, getDaoFactory().getInteractionDao().countAll());
    }

    @Test
    public void importXml_mint() throws Exception {
        PersisterStatistics stats = psiExchange.importIntoIntact(getMintEntrySet());
        Assert.assertEquals(16, stats.getPersistedCount(InteractionImpl.class, false));
        Assert.assertEquals(1, stats.getDuplicatesCount(InteractionImpl.class, false));
        Assert.assertEquals(16, getDaoFactory().getInteractionDao().countAll());
    }

    @Test
    public void importXml_mint_simplified() throws Exception {
        PsimiXmlReader reader = new PsimiXmlReader();
        EntrySet entrySet = reader.read(PsiExchangeTest.class.getResourceAsStream("/xml/mint_2006-07-18_simplified.xml"));
        psiExchange.importIntoIntact(entrySet);
        int count = getDaoFactory().getInteractionDao().countAll();
        final List<String> labels = DebugUtil.labelList(getDaoFactory().getInteractionDao().getAll());
        Assert.assertEquals(2, count);
    }

    @Test
    public void importXml_dupes() throws Exception {
        PersisterStatistics stats = psiExchange.importIntoIntact(PsiExchangeTest.class.getResourceAsStream("/xml/dupes.xml"));
        Assert.assertEquals(7, getDaoFactory().getInteractionDao().countAll());
        Assert.assertEquals(7, stats.getPersistedCount(InteractionImpl.class, false));
        Assert.assertEquals(1, stats.getDuplicatesCount(InteractionImpl.class, false));
    }

    @Test
    public void importXml_dip() throws Exception {
        psiExchange.importIntoIntact(getDipEntrySet());
        int count = getDaoFactory().getInteractionDao().countAll();
        Assert.assertEquals(74, count);
    }

    @Test
    public void importXml_all() throws Exception {
        PersisterStatistics intactStatistics = psiExchange.importIntoIntact(getIntactStream());
        Assert.assertEquals(6, intactStatistics.getPersistedCount(InteractionImpl.class, false));
        Assert.assertEquals(6, getDaoFactory().getInteractionDao().countAll());
        PersisterStatistics mintStats = psiExchange.importIntoIntact(getMintStream());
        Assert.assertEquals(16, mintStats.getPersistedCount(InteractionImpl.class, false));
        Assert.assertEquals(1, mintStats.getDuplicatesCount(InteractionImpl.class, false));
        Assert.assertEquals(22, getDaoFactory().getInteractionDao().countAll());
        PersisterStatistics dipStats = psiExchange.importIntoIntact(getDipStream());
        Assert.assertEquals(74, dipStats.getPersistedCount(InteractionImpl.class, false));
        Assert.assertEquals(96, getDaoFactory().getInteractionDao().countAll());
    }

    @Test
    public void checkPsiMiIdentities() throws Exception {
        psiExchange.importIntoIntact(getIntactEntrySet());
        CvExperimentalRole expRole = getDaoFactory().getCvObjectDao(CvExperimentalRole.class).getAll(0, 1).iterator().next();
        Assert.assertNotNull(expRole);
        CvObjectXref identityXref = CvObjectUtils.getPsiMiIdentityXref(expRole);
        Assert.assertNotNull(identityXref);
    }

    @Test
    public void checkAliases() throws Exception {
        psiExchange.importIntoIntact(getIntactEntrySet());
        Interactor interactor = getDaoFactory().getInteractorDao().getByShortLabel("fadd_mouse");
        Alias alias = interactor.getAliases().iterator().next();
        Assert.assertEquals("Fadd", alias.getName());
        CvObjectXref aliasTypeIdentXref = CvObjectUtils.getPsiMiIdentityXref(alias.getCvAliasType());
        Assert.assertNotNull(aliasTypeIdentXref);
        Assert.assertEquals(CvAliasType.GENE_NAME_MI_REF, aliasTypeIdentXref.getPrimaryId());
    }

    @Test
    public void export() throws Exception {
        Interaction mockInteraction = getMockBuilder().createInteractionRandomBinary();
        Experiment exp = mockInteraction.getExperiments().iterator().next();
        exp.addXref(getMockBuilder().createPrimaryReferenceXref(exp, "1234567"));
        IntactEntry entry = new IntactEntry(Arrays.asList(mockInteraction));
        StringWriter writer = new StringWriter();
        psiExchange.exportToPsiXml(writer, entry);
        System.out.println(writer.getBuffer().toString());
    }

    @Test
    public void getReleaseDates() throws Exception {
        PsiExchangeImpl psiExchangeImpl = new PsiExchangeImpl(getIntactContext());
        final List<DateTime> releaseDates = psiExchangeImpl.getReleaseDates(PsiExchangeTest.class.getResourceAsStream("/xml/dip_2006-11-01.xml"));
        Assert.assertEquals(26, releaseDates.size());
        for (DateTime releaseDate : releaseDates) {
            Assert.assertEquals(releaseDate, new DateTime("2006-11-01"));
        }
    }

    @Test
    public void toDateTime() throws Exception {
        String date1 = "Wed Sep 20 11:54:49 PDT 2006";
        String date2 = "2006-09-20";
        PsiExchangeImpl psiExchangeImpl = new PsiExchangeImpl(getIntactContext());
        Assert.assertEquals(new DateTime("2006-09-20T11:54:49.000"), psiExchangeImpl.toDateTime(date1));
        Assert.assertEquals(new DateTime("2006-09-20"), psiExchangeImpl.toDateTime(date2));
    }

    @Test
    public void importIntoIntact_participantBaitPreySameInteractior() throws Exception {
        PsimiXmlReader reader = new PsimiXmlReader();
        EntrySet set = reader.read(PsiExchangeTest.class.getResourceAsStream("/xml/2participants_sameInteractor.xml"));
        psiExchange.importIntoIntact(set);
        Assert.assertEquals(1, getDaoFactory().getInteractionDao().countAll());
        Assert.assertEquals(1, getDaoFactory().getProteinDao().countAll());
        Assert.assertEquals(2, getDaoFactory().getComponentDao().countAll());
        Assert.assertEquals(2, getDaoFactory().getInteractionDao().getByShortLabel("sft2-sft2").getComponents().size());
    }

    @Test
    public void importIntoIntact_withMultipleFeatures() throws Exception {
        PsimiXmlReader reader = new PsimiXmlReader();
        EntrySet set = reader.read(PsiExchangeTest.class.getResourceAsStream("/xml/multiplefeature.xml"));
        psiExchange.importIntoIntact(set);
        Assert.assertEquals(1, getDaoFactory().getInteractionDao().countAll());
        Assert.assertEquals(2, getDaoFactory().getProteinDao().countAll());
        Assert.assertEquals(2, getDaoFactory().getComponentDao().countAll());
        final InteractionImpl interaction = getDaoFactory().getInteractionDao().getAll().iterator().next();
        Component baitComponent = null;
        Component preyComponent = null;
        for (Component component : interaction.getComponents()) {
            final CvExperimentalRole cvExperimentalRole = component.getExperimentalRoles().iterator().next();
            Assert.assertNotNull(cvExperimentalRole);
            if (cvExperimentalRole.getShortLabel().equals("bait")) {
                baitComponent = component;
            }
            if (cvExperimentalRole.getShortLabel().equals("prey")) {
                preyComponent = component;
            }
        }
        Assert.assertNotNull(baitComponent);
        Assert.assertNotNull(preyComponent);
        final Collection<Feature> preyFeatures = baitComponent.getBindingDomains();
        Assert.assertEquals(2, preyFeatures.size());
        String mycFeatureShortLabel = null;
        String t7FeatureShortLabel = null;
        String mycFeatureType = null;
        String t7FeatureType = null;
        for (Feature feature : preyFeatures) {
            final String featureShortLabel = feature.getShortLabel();
            final CvFeatureType featureType = feature.getCvFeatureType();
            if (featureShortLabel.equals("region1")) {
                mycFeatureShortLabel = featureShortLabel;
                mycFeatureType = featureType.getShortLabel();
            }
            if (featureShortLabel.equals("region2")) {
                t7FeatureShortLabel = featureShortLabel;
                t7FeatureType = featureType.getShortLabel();
            }
        }
        Assert.assertEquals(mycFeatureShortLabel, "region1");
        Assert.assertEquals(t7FeatureShortLabel, "region2");
        Assert.assertEquals(mycFeatureType, "myc tag");
        Assert.assertEquals(t7FeatureType, "t7 tag");
    }

    @Test
    public void exportCompact() throws Exception {
        Interaction mockInteraction = getMockBuilder().createInteractionRandomBinary();
        Experiment exp = mockInteraction.getExperiments().iterator().next();
        exp.addXref(getMockBuilder().createPrimaryReferenceXref(exp, "1234567"));
        IntactEntry entry = new IntactEntry(Arrays.asList(mockInteraction));
        final ConverterContext context = ConverterContext.getInstance();
        context.configure(new ExportProfile() {

            public void configure(ConverterContext context) {
                context.setGenerateExpandedXml(false);
            }
        });
        final EntrySet es = psiExchange.exportToEntrySet(entry);
        final Entry e = es.getEntries().iterator().next();
        Assert.assertEquals(1, e.getExperiments().size());
        Assert.assertEquals(2, e.getInteractors().size());
        final psidev.psi.mi.xml.model.Interaction i = e.getInteractions().iterator().next();
        Assert.assertEquals(0, i.getExperiments().size());
        Assert.assertEquals(1, i.getExperimentRefs().size());
        for (Participant p : i.getParticipants()) {
            Assert.assertNull(p.getInteractor());
            Assert.assertNotNull(p.getInteractorRef());
        }
    }

    @Test
    public void exportExpanded() throws Exception {
        Interaction mockInteraction = getMockBuilder().createInteractionRandomBinary();
        Experiment exp = mockInteraction.getExperiments().iterator().next();
        exp.addXref(getMockBuilder().createPrimaryReferenceXref(exp, "1234567"));
        IntactEntry entry = new IntactEntry(Arrays.asList(mockInteraction));
        final ConverterContext context = ConverterContext.getInstance();
        context.configure(new ExportProfile() {

            public void configure(ConverterContext context) {
                context.setGenerateExpandedXml(true);
            }
        });
        final EntrySet es = psiExchange.exportToEntrySet(entry);
        final Entry e = es.getEntries().iterator().next();
        Assert.assertEquals(0, e.getExperiments().size());
        Assert.assertEquals(0, e.getInteractors().size());
        final psidev.psi.mi.xml.model.Interaction i = e.getInteractions().iterator().next();
        Assert.assertEquals(1, i.getExperiments().size());
        Assert.assertEquals(0, i.getExperimentRefs().size());
        for (Participant p : i.getParticipants()) {
            Assert.assertNotNull(p.getInteractor());
            Assert.assertNull(p.getInteractorRef());
        }
    }
}
