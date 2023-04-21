package com.cron.job.utility;

import java.sql.SQLException;
import java.sql.Statement;
import org.quartz.*;
import com.utils.Default;

/**
 * Job che segue una generica query sul db principale
 * 
 *
 * @author Marco Berri marcoberri@gmail.com
 */
public class DBQuery extends com.cron.job.Base {

    /**
     *
     * @param context
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        super.execute(context);
        debug("Start execute job " + this.getClass().getName());
        try {
            Statement stat = conn_url.createStatement();
            stat.execute(Default.toString(properties.get("query")));
            stat.close();
        } catch (SQLException e) {
            fatal("SQLException", e);
        }
        debug("End execute job " + this.getClass().getName());
    }
}
