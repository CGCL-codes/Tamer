package uk.ac.ebi.intact.task;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.intact.core.persister.CorePersister;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Test a job which retrieves cluster scores from a clustered mitab and updates a non clustered mitab file with the computed score
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>07/02/12</pre>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/mitab-creation.spring.xml", "/META-INF/job-tests.spring.xml", "classpath*:/META-INF/intact.spring.xml", "classpath*:/META-INF/standalone/*-standalone.spring.xml" })
public class MitabClusterScoreUpdateJobTest {

    @Resource(name = "intactBatchJobLauncher")
    private JobLauncher jobLauncher;

    @Autowired
    private CorePersister corePersister;

    @Autowired
    private ApplicationContext applicationContext;

    @Before
    public void deleteGeneratedMitab() {
        File generatedMitab = new File("target/lala.txt");
        generatedMitab.delete();
    }

    @Test
    public void update_cluster_scores() throws JobInstanceAlreadyCompleteException, JobParametersInvalidException, JobRestartException, JobExecutionAlreadyRunningException, IOException {
        Job job = (Job) applicationContext.getBean("mitabScoreUpdateJob");
        Map<String, JobParameter> params = new HashMap<String, JobParameter>(1);
        params.put("date", new JobParameter(System.currentTimeMillis()));
        JobExecution jobExecution = jobLauncher.run(job, new JobParameters(params));
        Assert.assertTrue(jobExecution.getAllFailureExceptions().isEmpty());
        Assert.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
        File generatedMitab = new File("target/lala.txt");
        Assert.assertTrue(generatedMitab.exists());
        File expectedMitab = new File(MitabClusterScoreUpdateJobTest.class.getResource("/resulting_mitab_score.txt").getFile());
        Assert.assertEquals(FileUtils.checksumCRC32(generatedMitab), FileUtils.checksumCRC32(expectedMitab));
    }
}
