package com.manning.sbia.sandbox.partition;

import javax.sql.DataSource;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("partition-import-job.xml")
public class PartitionTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private DataSource dataSource;

    @Test
    public void partition() throws Exception {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        int initialCount = template.queryForInt("select count(1) from product");
        jobLauncher.run(job, new JobParameters());
        int currentCount = template.queryForInt("select count(1) from product");
        Assert.assertEquals(initialCount + 3 * 8, currentCount);
    }
}
