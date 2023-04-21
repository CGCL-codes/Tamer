package org.mbari.vars.annotation.ui.actions;

import org.mbari.awt.event.ActionAdapter;
import org.mbari.vars.annotation.model.VideoArchiveSet;
import org.mbari.vars.annotation.model.VideoFrame;
import org.mbari.vars.annotation.model.dao.ExpdVideoArchiveSetDAO;
import org.mbari.vars.annotation.model.dao.ExpdVideoFrameDAO;
import java.util.Collection;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Looks up information form the expedition (EXPD) database and and
 * updates the VideoArchiveSet and all it's child VideoFrames</p>
 *
 * @author <a href="http://www.mbari.org">MBARI</a>
 * @version $Id: MergeExpdAction.java,v 1.1 2006/06/27 21:32:32 brian Exp $
 */
public class MergeExpdAction extends ActionAdapter {

    private static final Logger log = LoggerFactory.getLogger(MergeExpdAction.class);

    private VideoArchiveSet videoArchiveSet;

    /**
     * <p><!-- Method description --></p>
     *
     */
    public void doAction() {
        try {
            ExpdVideoArchiveSetDAO.update(videoArchiveSet);
        } catch (Exception e) {
            log.error("failed to update " + videoArchiveSet + "with information from the expd database.", e);
        }
        Collection videoFrames = videoArchiveSet.getVideoFrames();
        for (Iterator i = videoFrames.iterator(); i.hasNext(); ) {
            VideoFrame vf = (VideoFrame) i.next();
            try {
                ExpdVideoFrameDAO.update(vf);
            } catch (Exception e) {
                log.error("Failed to update " + vf + " with information from the expd database. All subsequent " + "VideoFrames will not be updated.", e);
            }
        }
    }

    /**
     * @return Returns the videoArchiveSet.
     */
    public final VideoArchiveSet getVideoArchiveSet() {
        return videoArchiveSet;
    }

    /**
     * @param videoArchiveSet The videoArchiveSet to set.
     */
    public final void setVideoArchiveSet(VideoArchiveSet videoArchiveSet) {
        this.videoArchiveSet = videoArchiveSet;
    }
}
