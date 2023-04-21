package de.excrawler.server.Statistics;

import de.excrawler.server.Logging.Log;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Job Class for creating daily statistic
 * @author Yves Hoppe <info at yves-hoppe.de>
 * @author Karpouzas George <www.webnetsoft.gr>
 */
public class DayStatistics implements Job {

    public static int DAY_CRAWLEDWEBSITES;

    public static int DAY_CRAWLEDIMAGES;

    public static int DAY_DOWNLOADEDWEBSITES;

    public static int DAY_DOWNLOADEDIMAGES;

    private static int TMP_CRAWLEDWEBSITES;

    private static int TMP_CRAWLEDIMAGES;

    private static int TMP_DOWNLOADEDWEBSITES;

    private static int TMP_DOWNLOADEDIMAGES;

    public DayStatistics() {
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        Log.logger.info("Creating daily statistic");
        DAY_CRAWLEDWEBSITES = Statistics.CRAWLED_WEBSITES - TMP_CRAWLEDWEBSITES;
        DAY_CRAWLEDIMAGES = Statistics.CRAWLED_IMAGES - TMP_CRAWLEDIMAGES;
        DAY_DOWNLOADEDWEBSITES = Statistics.DOWNLOADED_WEBSITES - TMP_DOWNLOADEDWEBSITES;
        DAY_DOWNLOADEDIMAGES = Statistics.DOWNLOADED_IMAGES - TMP_DOWNLOADEDIMAGES;
        TMP_CRAWLEDWEBSITES = Statistics.CRAWLED_WEBSITES;
        TMP_CRAWLEDIMAGES = Statistics.CRAWLED_IMAGES;
        TMP_DOWNLOADEDWEBSITES = Statistics.DOWNLOADED_WEBSITES;
        TMP_DOWNLOADEDIMAGES = Statistics.DOWNLOADED_IMAGES;
    }
}
