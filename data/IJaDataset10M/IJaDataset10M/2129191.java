package net.sf.webnzb.services;

import java.util.List;
import net.sf.webnzb.domain.NNTPPost;

/**
 * Defines methods for scheduling and monitoring the state of downloads.
 * 
 * @author silvester
 * @since 0.1
 */
public interface TransferService {

    /**
     * Schedule a post for download.
     * 
     * @param post the post to schedule
     */
    public void scheduleForDownload(NNTPPost post);

    /**
     * List all downloads that are active (have not been deleted yet).
     * 
     * @return list containing all active downloads, or an empty list
     */
    public List<NNTPPost> listActiveDownloads();
}
