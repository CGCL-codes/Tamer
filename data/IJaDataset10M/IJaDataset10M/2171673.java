package org.opencms.scheduler.jobs;

import org.opencms.file.CmsObject;
import org.opencms.loader.CmsImageLoader;
import org.opencms.main.CmsLog;
import org.opencms.scheduler.I_CmsScheduledJob;
import java.io.File;
import java.util.Map;
import org.apache.commons.logging.Log;

/**
 * A schedulable OpenCms job that clear the image cache for the scaled images created by the <code>{@link org.opencms.loader.CmsImageLoader}</code>.<p>
 * 
 * Job parameters:<p>
 * <dl>
 * <dt><code>maxage={time in hours}</code></dt>
 * <dd>Specifies the maximum age (in hours) images can be unused before they are removed from the cache.
 * Any image in the image cache folder that has a RFS date of last modification older than this time is considered
 * expired and is therefore deleted.</dd>
 * </dl>
 * 
 * @author Alexander Kandzior
 * 
 * @version $Revision: 1.4 $ 
 * 
 * @since 6.2.0 
 */
public class CmsImageCacheCleanupJob implements I_CmsScheduledJob {

    /** Unlock parameter. */
    public static final String PARAM_MAXAGE = "maxage";

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsImageCacheCleanupJob.class);

    /**
     * Removes all expired image cache entries from the RFS cache.<p>
     * 
     * Empty directories are removed as well.<p>
     * 
     * @param maxAge the maximum age of the image cache files in hours (or fractions of hours)
     * 
     * @return the total number of deleted resources
     */
    public static int cleanImageCache(float maxAge) {
        long expireDate = System.currentTimeMillis() - (long) (maxAge * 60f * 60f * 1000f);
        File basedir = new File(CmsImageLoader.getImageRepositoryPath());
        return cleanImageCache(expireDate, basedir);
    }

    /**
     * Removes all expired image cache entries from the given RFS directory, including recursion to subdirectories.<p>
     * 
     * @param maxAge the maximum age of the image cache files
     * @param directory the directory to remove the cache files in
     * 
     * @return the total number of deleted resources
     */
    private static int cleanImageCache(long maxAge, File directory) {
        int count = 0;
        if (directory.canRead() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                if (f.isDirectory()) {
                    count += cleanImageCache(maxAge, f);
                }
                if (f.canWrite()) {
                    if (f.lastModified() < maxAge) {
                        try {
                            f.delete();
                            count++;
                        } catch (Exception e) {
                            LOG.error(Messages.get().getBundle().key(Messages.LOG_IMAGE_CACHE_UNABLE_TO_DELETE_1, f.getAbsolutePath()));
                        }
                    }
                }
            }
            if (directory.listFiles().length <= 0) {
                try {
                    directory.delete();
                    count++;
                } catch (Exception e) {
                    LOG.error(Messages.get().getBundle().key(Messages.LOG_IMAGE_CACHE_UNABLE_TO_DELETE_1, directory.getAbsolutePath()));
                }
            }
        }
        return count;
    }

    /**
     * @see org.opencms.scheduler.I_CmsScheduledJob#launch(CmsObject, Map)
     */
    public String launch(CmsObject cms, Map parameters) throws Exception {
        if (!CmsImageLoader.isEnabled() || (CmsImageLoader.getImageRepositoryPath() == null)) {
            return Messages.get().getBundle().key(Messages.LOG_IMAGE_SCALING_DISABLED_0);
        }
        String maxAgeStr = (String) parameters.get(PARAM_MAXAGE);
        float maxAge;
        try {
            maxAge = Float.parseFloat(maxAgeStr);
        } catch (NumberFormatException e) {
            maxAge = 24f * 7f;
            LOG.error(Messages.get().getBundle().key(Messages.LOG_IMAGE_CACHE_BAD_MAXAGE_2, maxAgeStr, new Float(maxAge)));
        }
        int count = cleanImageCache(maxAge);
        return Messages.get().getBundle().key(Messages.LOG_IMAGE_CACHE_CLEANUP_COUNT_1, new Integer(count));
    }
}
