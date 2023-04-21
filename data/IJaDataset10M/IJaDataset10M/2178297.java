package org.opencms.publish;

import org.opencms.util.CmsUUID;

/**
 * Default implementation for the {@link I_CmsPublishEventListener}.<p>
 * 
 * @author Michael Moossen
 * 
 * @version $Revision: 1.4 $
 * 
 * @since 6.5.5
 */
public class CmsPublishEventAdapter implements I_CmsPublishEventListener {

    /**
     * @see org.opencms.publish.I_CmsPublishEventListener#onAbort(CmsUUID, org.opencms.publish.CmsPublishJobEnqueued)
     */
    public void onAbort(CmsUUID userId, CmsPublishJobEnqueued publishJob) {
    }

    /**
     * @see org.opencms.publish.I_CmsPublishEventListener#onEnqueue(org.opencms.publish.CmsPublishJobBase)
     */
    public void onEnqueue(CmsPublishJobBase publishJob) {
    }

    /**
     * @see org.opencms.publish.I_CmsPublishEventListener#onFinish(org.opencms.publish.CmsPublishJobRunning)
     */
    public void onFinish(CmsPublishJobRunning publishJob) {
    }

    /**
     * @see org.opencms.publish.I_CmsPublishEventListener#onRemove(org.opencms.publish.CmsPublishJobFinished)
     */
    public void onRemove(CmsPublishJobFinished publishJob) {
    }

    /**
     * @see org.opencms.publish.I_CmsPublishEventListener#onStart(org.opencms.publish.CmsPublishJobEnqueued)
     */
    public void onStart(CmsPublishJobEnqueued publishJob) {
    }
}
