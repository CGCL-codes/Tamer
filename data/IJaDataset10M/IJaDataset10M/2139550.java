package org.dcm4chex.archive.ejb.session;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import org.apache.log4j.Logger;
import org.dcm4chex.archive.ejb.interfaces.FileLocal;
import org.dcm4chex.archive.ejb.interfaces.InstanceLocal;
import org.dcm4chex.archive.ejb.interfaces.SeriesLocal;
import org.dcm4chex.archive.ejb.interfaces.StudyLocal;
import org.dcm4chex.archive.ejb.interfaces.StudyOnFileSystemLocal;

/**
 * Used by FileSystemMgtBean
 * 
 * @author gunter.zeilinger@tiani.com
 * @version Revision $Date: 2005-09-20 10:37:20 -0400 (Tue, 20 Sep 2005) $
 * @since 05.02.2005
 * 
 * @ejb.bean name="FileSystemMgtSupport" type="Stateless" view-type="local"
 *           jndi-name="ejb/FileSystemMgtSupport"
 * @ejb.transaction-type type="Container"
 * @ejb.transaction type="RequiresNew"
 */
public abstract class FileSystemMgtSupportBean implements SessionBean {

    private static Logger log = Logger.getLogger(FileSystemMgtSupportBean.class);

    /**    
     * @ejb.interface-method
     */
    public long releaseStudy(StudyOnFileSystemLocal studyOnFs, boolean deleteUncommited, boolean flushOnMedia, boolean flushExternal) throws EJBException, RemoveException, FinderException {
        long size = 0L;
        if (Thread.interrupted()) {
            log.warn("Interrupted state cleared for current thread!");
        }
        StudyLocal study = studyOnFs.getStudy();
        if (flushExternal && study.isStudyExternalRetrievable() || flushOnMedia && study.isStudyAvailableOnMedia()) {
            Collection c = studyOnFs.getFiles();
            if (log.isDebugEnabled()) log.debug("Release " + c.size() + " files from " + studyOnFs.asString());
            FileLocal fileLocal;
            InstanceLocal il;
            Set series = new HashSet();
            for (Iterator iter = c.iterator(); iter.hasNext(); ) {
                fileLocal = (FileLocal) iter.next();
                if (log.isDebugEnabled()) log.debug("Release File:" + fileLocal.asString());
                size += fileLocal.getFileSize();
                il = fileLocal.getInstance();
                series.add(il.getSeries());
                fileLocal.setInstance(null);
                il.updateDerivedFields(true, true);
            }
            for (Iterator iter = series.iterator(); iter.hasNext(); ) {
                final SeriesLocal ser = (SeriesLocal) iter.next();
                ser.updateDerivedFields(false, true, false, false, true);
            }
            study.updateDerivedFields(false, true, false, false, true, false);
            log.info("Release Files of " + studyOnFs.asString() + " - " + (size / 1000000.f) + "MB");
            studyOnFs.remove();
        } else if (deleteUncommited && study.getNumberOfCommitedInstances() == 0) {
            Collection files = studyOnFs.getFiles();
            for (Iterator it = files.iterator(); it.hasNext(); ) {
                size += ((FileLocal) it.next()).getFileSize();
            }
            log.info("Delete " + studyOnFs.asString() + " - " + (size / 1000000.f) + "MB");
            study.remove();
        }
        return size;
    }
}
