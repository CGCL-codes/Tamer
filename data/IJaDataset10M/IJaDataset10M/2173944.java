package uk.ac.ebi.intact.plugins.dbupdate.targetspecies;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.intact.core.persister.PersisterHelper;
import uk.ac.ebi.intact.core.unit.IntactBasicTestCase;
import uk.ac.ebi.intact.core.util.SchemaUtils;
import uk.ac.ebi.intact.model.Annotation;
import uk.ac.ebi.intact.model.CvTopic;
import uk.ac.ebi.intact.model.Experiment;
import uk.ac.ebi.intact.plugins.dbupdate.experiments.UpdateExperiments;
import uk.ac.ebi.intact.plugins.dbupdate.experiments.UpdateSingleExperimentReport;
import uk.ac.ebi.intact.config.impl.SmallCvPrimer;
import uk.ac.ebi.intact.persistence.dao.DaoFactory;
import java.util.List;

/**
 * TODO comment this
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: UpdateExperimentsTest.java 10026 2007-10-17 15:00:33Z baranda $
 */
public class UpdateExperimentsTest extends IntactBasicTestCase {

    @Before
    public void before() throws Exception {
        SchemaUtils.createSchema();
    }

    @After
    public void after() throws Exception {
        commitTransaction();
    }

    @Test
    public void normal() throws Exception {
        beginTransaction();
        new MandatoryTopicsCvPrimer(getDaoFactory()).createCVs();
        commitTransaction();
        Experiment exp = getMockBuilder().createExperimentEmpty("rauramo-1975-1", "1234567");
        Annotation annot = getMockBuilder().createAnnotation("Rauramo L., Lagerspetz K., Engblom P., Punnonen R.", CvTopic.AUTHOR_LIST_MI_REF, CvTopic.AUTHOR_LIST);
        exp.addAnnotation(annot);
        PersisterHelper.saveOrUpdate(exp);
        List<UpdateSingleExperimentReport> reports = UpdateExperiments.startUpdate(System.out, false);
        Assert.assertEquals(1, reports.size());
        final UpdateSingleExperimentReport singleExperimentReport = reports.iterator().next();
        Assert.assertFalse(singleExperimentReport.isInvalid());
        Assert.assertFalse(singleExperimentReport.isAuthorListUpdated());
        Assert.assertTrue(singleExperimentReport.isFullNameUpdated());
        Assert.assertTrue(singleExperimentReport.isJournalUpdated());
        Assert.assertTrue(singleExperimentReport.isYearUpdated());
        Assert.assertFalse(singleExperimentReport.isShortLabelUpdated());
    }

    @Test
    public void normal_withPattern() throws Exception {
        beginTransaction();
        new MandatoryTopicsCvPrimer(getDaoFactory()).createCVs();
        commitTransaction();
        PersisterHelper.saveOrUpdate(getMockBuilder().createExperimentEmpty("rauramo-1975-1", "1234567"));
        PersisterHelper.saveOrUpdate(getMockBuilder().createExperimentEmpty("lala-2007-1", "1234568"));
        List<UpdateSingleExperimentReport> reports = UpdateExperiments.startUpdate(System.out, "lala%", false);
        Assert.assertEquals(1, reports.size());
    }

    @Test
    public void curatedComplex() throws Exception {
        Experiment exp = getMockBuilder().createExperimentEmpty();
        Annotation annot = getMockBuilder().createAnnotation("", "IA:0", CvTopic.CURATED_COMPLEX);
        exp.addAnnotation(annot);
        PersisterHelper.saveOrUpdate(exp);
        List<UpdateSingleExperimentReport> reports = UpdateExperiments.startUpdate(System.out, false);
        Assert.assertEquals(1, reports.size());
        Assert.assertTrue(reports.iterator().next().isInvalid());
    }

    private class MandatoryTopicsCvPrimer extends SmallCvPrimer {

        public MandatoryTopicsCvPrimer(DaoFactory daoFactory) {
            super(daoFactory);
        }

        @Override
        public void createCVs() {
            super.createCVs();
            getCvObject(CvTopic.class, CvTopic.AUTHOR_LIST, CvTopic.AUTHOR_LIST_MI_REF);
            getCvObject(CvTopic.class, CvTopic.JOURNAL, CvTopic.JOURNAL_MI_REF);
            getCvObject(CvTopic.class, CvTopic.PUBLICATION_YEAR, CvTopic.PUBLICATION_YEAR_MI_REF);
            getCvObject(CvTopic.class, CvTopic.CONTACT_EMAIL, CvTopic.CONTACT_EMAIL_MI_REF);
        }
    }
}
