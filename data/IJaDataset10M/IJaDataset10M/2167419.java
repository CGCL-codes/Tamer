package org.nexopenframework.scheduling.quartz.jdbcjobstore;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.impl.jdbcjobstore.StdJDBCConstants;
import org.quartz.spi.ClassLoadHelper;
import org.slf4j.Logger;
import org.springframework.scheduling.quartz.JobDetailBean;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @see org.quartz.impl.jdbcjobstore.StdJDBCDelegate
 * @author Francesc Xavier Magdaleno
 * @version $Revision 3333$,$Date: 2009-04-15 22:02:44 +0100 $
 * @since 2.0.0.GA
 */
public class StdJDBCDelegate extends org.quartz.impl.jdbcjobstore.StdJDBCDelegate implements StdJDBCConstants {

    /**
	 * @param logger
	 * @param tablePrefix
	 * @param instanceId
	 */
    public StdJDBCDelegate(final Logger logger, final String tablePrefix, final String instanceId) {
        super(logger, tablePrefix, instanceId);
    }

    /**
	 * @param logger
	 * @param tablePrefix
	 * @param instanceId
	 * @param useProperties
	 */
    public StdJDBCDelegate(final Logger logger, final String tablePrefix, final String instanceId, final Boolean useProperties) {
        super(logger, tablePrefix, instanceId, useProperties);
    }

    public JobDetail selectJobDetail(final Connection conn, final String jobName, final String groupName, final ClassLoadHelper loadHelper) throws ClassNotFoundException, IOException, SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(rtp(SELECT_JOB_DETAIL));
            ps.setString(1, jobName);
            ps.setString(2, groupName);
            rs = ps.executeQuery();
            JobDetail job = null;
            if (rs.next()) {
                job = new JobDetailBean();
                job.setName(rs.getString(COL_JOB_NAME));
                job.setGroup(rs.getString(COL_JOB_GROUP));
                job.setDescription(rs.getString(COL_DESCRIPTION));
                job.setJobClass(loadHelper.loadClass(rs.getString(COL_JOB_CLASS)));
                job.setDurability(getBoolean(rs, COL_IS_DURABLE));
                job.setVolatility(getBoolean(rs, COL_IS_VOLATILE));
                job.setRequestsRecovery(getBoolean(rs, COL_REQUESTS_RECOVERY));
                Map<?, ?> map = null;
                if (canUseProperties()) {
                    map = getMapFromProperties(rs);
                } else {
                    map = (Map<?, ?>) getObjectFromBlob(rs, COL_JOB_DATAMAP);
                }
                if (null != map) {
                    job.setJobDataMap(new JobDataMap(map));
                }
            }
            return job;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
        }
    }

    /**
	 * <p></p>
	 * 
	 * @see org.quartz.impl.jdbcjobstore.StdJDBCDelegate#selectJobForTrigger(java.sql.Connection, java.lang.String, java.lang.String, org.quartz.spi.ClassLoadHelper)
	 */
    public JobDetail selectJobForTrigger(final Connection conn, final String triggerName, final String groupName, final ClassLoadHelper loadHelper) throws ClassNotFoundException, SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(rtp(SELECT_JOB_FOR_TRIGGER));
            ps.setString(1, triggerName);
            ps.setString(2, groupName);
            rs = ps.executeQuery();
            if (rs.next()) {
                final JobDetail job = new JobDetailBean();
                job.setName(rs.getString(1));
                job.setGroup(rs.getString(2));
                job.setDurability(getBoolean(rs, 3));
                job.setJobClass(loadHelper.loadClass(rs.getString(4)));
                job.setRequestsRecovery(getBoolean(rs, 5));
                return job;
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("No job for trigger '" + groupName + "." + triggerName + "'.");
                }
                return null;
            }
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
        }
    }

    Map<?, ?> getMapFromProperties(final ResultSet rs) throws ClassNotFoundException, IOException, SQLException {
        final InputStream is = (InputStream) getJobDetailFromBlob(rs, COL_JOB_DATAMAP);
        if (is == null) {
            return null;
        }
        final Properties properties = new Properties();
        if (is != null) {
            try {
                properties.load(is);
            } finally {
                is.close();
            }
        }
        return convertFromProperty(properties);
    }
}
