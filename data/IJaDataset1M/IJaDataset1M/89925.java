package com.manning.sbia.sandbox.chunk;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("local-chunk-import-job.xml")
public class LocalChunkTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job jobSimple;

    @Autowired
    private Job jobChunk;

    @Autowired
    private JdbcTemplate template;

    @Test
    public void localChunking() throws Exception {
        executeJob(100, jobSimple);
        executeJob(100, jobChunk);
        int max = 100;
        int nbIterations = 5;
        for (int i = 0; i < nbIterations; i++) {
            long simpleTime = executeJob(max, jobSimple);
            long remoteTime = executeJob(max, jobChunk);
            System.out.println("simple: " + simpleTime + ", local chunk: " + remoteTime);
        }
    }

    private long executeJob(int count, Job job) throws Exception {
        stageProducts(count);
        int initialCount = countProducts();
        long start = System.currentTimeMillis();
        jobLauncher.run(job, new JobParametersBuilder().addLong("time", start).toJobParameters());
        long end = System.currentTimeMillis();
        int currentCount = countProducts();
        Assert.assertEquals(initialCount + count, currentCount);
        return end - start;
    }

    private int countProducts() {
        return template.queryForInt("select count(1) from product");
    }

    private void stageProducts(final int count) {
        template.update("delete from staging_product");
        template.update("delete from product");
        template.batchUpdate("insert into staging_product (id,processed) values (?,?)", new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, String.valueOf(i));
                ps.setBoolean(2, false);
            }

            @Override
            public int getBatchSize() {
                return count;
            }
        });
    }
}
