package net.techwatch.fsindex.dao.jdbc;

import java.util.List;
import net.techwatch.fsindex.FileSystemObject;
import net.techwatch.fsindex.dao.FileSystemDao;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Proxy used for performance tracking.
 *  
 * @author wiv
 *
 */
public class FileSystemDaoProxy implements FileSystemDao {

    private Log log = LogFactory.getLog(FileSystemDaoProxy.class);

    /**
	 * 
	 */
    private FileSystemDao fileSystemDao;

    /**
	 * 
	 */
    public FileSystemDaoProxy() {
    }

    /**
	 * @return
	 */
    public FileSystemDao getFileSystemDao() {
        return this.fileSystemDao;
    }

    /**
	 * @param fileSystemDao
	 */
    public void setFileSystemDao(FileSystemDao fileSystemDao) {
        this.fileSystemDao = fileSystemDao;
    }

    public int create(FileSystemObject fsObj) {
        StopWatch watch = new StopWatch();
        watch.start();
        int result = fileSystemDao.create(fsObj);
        watch.stop();
        if (result > 0) if (log.isInfoEnabled()) log.info("Creation of " + result + " files complete in " + watch.toString());
        return result;
    }

    @SuppressWarnings("unchecked")
    public List getFiles() {
        StopWatch watch = new StopWatch();
        watch.start();
        List result = fileSystemDao.getFiles();
        watch.stop();
        if (log.isInfoEnabled()) log.info("Get list of " + result.size() + " files in " + watch.toString());
        return result;
    }

    public int remove(long id) {
        StopWatch watch = new StopWatch();
        watch.start();
        int result = fileSystemDao.remove(id);
        watch.stop();
        if (result > 0) if (log.isInfoEnabled()) log.info("Deletion of " + result + " files complete in " + watch.toString());
        return result;
    }

    public int update(long id, long lastUpdate) {
        StopWatch watch = new StopWatch();
        watch.start();
        int result = fileSystemDao.update(id, lastUpdate);
        watch.stop();
        if (result > 0) if (log.isInfoEnabled()) log.info("Update of " + result + " files complete in " + watch.toString());
        return result;
    }

    @SuppressWarnings("unchecked")
    public List getDirLevel0() {
        StopWatch watch = new StopWatch();
        watch.start();
        List result = fileSystemDao.getDirLevel0();
        watch.stop();
        if (log.isInfoEnabled()) log.info("Get level 0 directory list in " + watch.toString());
        return result;
    }

    @SuppressWarnings("unchecked")
    public List getDirLevel1() {
        StopWatch watch = new StopWatch();
        watch.start();
        List result = fileSystemDao.getDirLevel1();
        watch.stop();
        if (log.isInfoEnabled()) log.info("Get level 1 directory list in " + watch.toString());
        return result;
    }

    @SuppressWarnings("unchecked")
    public List getDirLevel2() {
        StopWatch watch = new StopWatch();
        watch.start();
        List result = fileSystemDao.getDirLevel2();
        watch.stop();
        if (log.isInfoEnabled()) log.info("Get level 2 directory list in " + watch.toString());
        return result;
    }

    @SuppressWarnings("unchecked")
    public List getDirLevel3() {
        StopWatch watch = new StopWatch();
        watch.start();
        List result = fileSystemDao.getDirLevel3();
        watch.stop();
        if (log.isInfoEnabled()) log.info("Get level 3 directory list in " + watch.toString());
        return result;
    }

    @SuppressWarnings("unchecked")
    public List getChildren(long parentId) {
        StopWatch watch = new StopWatch();
        watch.start();
        List result = fileSystemDao.getChildren(parentId);
        watch.stop();
        if (log.isDebugEnabled()) log.debug("Get children in " + watch.toString());
        return result;
    }

    public int clean() {
        StopWatch watch = new StopWatch();
        watch.start();
        int result = fileSystemDao.clean();
        watch.stop();
        if (log.isInfoEnabled()) log.info("Update/Insert/Deletion of " + result + " files complete in " + watch.toString());
        return result;
    }
}
