package com.cron.job.stats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.quartz.*;

/**
 * statistiche: esegue il conteggio della view totali per singolo url
 * 
 * @author marco
 */
public class CounterView extends com.cron.job.Base {

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
            Statement statement_stats = this.conn_stats.createStatement();
            statement_stats.execute(properties.get("query_delete"));
            Statement statement = this.conn_url.createStatement();
            ResultSet rsstats = statement.executeQuery(properties.get("query_select_url"));
            while (rsstats.next()) {
                String id = rsstats.getString("id");
                ResultSet rsstats_stats = statement_stats.executeQuery("select count(*) as c from view where fk_url_id='" + id + "'");
                while (rsstats_stats.next()) {
                    statement_stats.execute("insert into counter_view (fk_url_id,n_view) values('" + id + "'," + rsstats_stats.getString("c") + ")");
                }
            }
            statement.close();
            statement_stats.close();
        } catch (SQLException ex) {
            fatal("SQLException", ex);
        }
        debug("End execute job " + this.getClass().getName());
    }
}
