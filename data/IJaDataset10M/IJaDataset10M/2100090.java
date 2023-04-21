package edu.ucdavis.genomics.metabolomics.binbase.quality.server.ejb;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.TransactionTimeout;
import edu.ucdavis.genomics.metabolomics.binbase.algorythm.date.DateUtil;
import edu.ucdavis.genomics.metabolomics.binbase.algorythm.date.SampleDate;
import edu.ucdavis.genomics.metabolomics.exception.BinBaseException;
import edu.ucdavis.genomics.metabolomics.util.io.FileUtil;

/**
 * implementation of the service
 * 
 * @author wohlgemuth
 */
@Stateless
public class FileCacheServiceBean implements FileCacheService {

    private Logger logger = Logger.getLogger(getClass());

    @PersistenceContext
    EntityManager manager;

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @TransactionTimeout(value = 90000)
    public void cacheFiles(String directory) throws BinBaseException {
        logger.debug("caching files");
        logger.debug("remove outdated files from cache");
        for (FileCacheEntityBean bean : getAllBeans()) {
            if (bean.getFilePath().exists() == false) {
                logger.debug("removing not existing file from cache: " + bean.getName());
                manager.remove(bean);
            }
        }
        logger.debug("adding new files");
        File myDir = new File(directory);
        if (myDir.isDirectory()) {
            File[] files = myDir.listFiles();
            logger.debug("directory contains " + files.length + " files");
            for (File file : files) {
                if (file.isFile()) {
                    String name = FileUtil.cleanFileName(file.getName());
                    Date date = new Date(file.lastModified());
                    if (hasBean(name, date) == false) {
                        logger.debug("trying to store bean in cache");
                        try {
                            storeBean(file, name, date);
                            logger.debug("success!");
                        } catch (RuntimeException e) {
                            logger.debug("didn't match registered sample pattern! - " + name, e);
                        }
                    } else {
                        logger.debug("already in cache: " + name);
                    }
                }
            }
        } else {
            logger.warn("not a directory!");
        }
    }

    /**
	 * checks if the bean already exists.
	 * 
	 * @param name
	 * @return
	 */
    public boolean hasBean(String name, Date date) {
        FileCacheEntityBean bean = manager.find(FileCacheEntityBean.class, name);
        logger.debug("checked bean: " + bean);
        if (bean == null) {
            return false;
        } else {
            if (bean.getFileDate().equals(date) == false) {
                bean.setFileDate(date);
                manager.persist(bean);
            }
            return true;
        }
    }

    /**
	 * stores a bean
	 * 
	 * @param file
	 * @param name
	 * @param date
	 */
    public void storeBean(File file, String name, Date date) {
        FileCacheEntityBean bean;
        bean = new FileCacheEntityBean();
        bean.setFileDate(date);
        bean.setName(name);
        bean.setFilePath(file);
        SampleDate sd = SampleDate.createInstance(name);
        bean.setMachine(sd.getMachine());
        manager.persist(bean);
        manager.flush();
        logger.debug("file cached with name: " + name + " and date: " + date);
    }

    public void clearCache() {
        logger.info("deleting all objects from cache");
        for (FileCacheEntityBean bean : getAllBeans()) {
            manager.remove(bean);
        }
    }

    @SuppressWarnings("unchecked")
    public Collection<File> getFiles(Date date) {
        logger.debug("searching for files of date: " + date);
        Query query = manager.createQuery("SELECT e FROM FileCacheEntityBean e WHERE e.fileDate = ?1");
        query.setParameter(1, date);
        @SuppressWarnings("unused") Collection<FileCacheEntityBean> result = query.getResultList();
        Collection<File> files = new Vector<File>();
        for (FileCacheEntityBean bean : result) {
            files.add(bean.getFilePath());
        }
        return files;
    }

    @SuppressWarnings("unchecked")
    public Collection<File> getFiles(String pattern) {
        logger.debug("searching for files of pattern: " + pattern);
        Query query = manager.createQuery("SELECT e FROM FileCacheEntityBean e");
        Collection<FileCacheEntityBean> result = query.getResultList();
        Collection<File> files = new Vector<File>();
        logger.debug("fetched: " + files.size());
        for (FileCacheEntityBean bean : result) {
            this.filterByPattern(pattern, files, bean.getFilePath());
        }
        return files;
    }

    public Collection<File> getFiles(Date date, String pattern) {
        logger.debug("searching for files of pattern: " + pattern);
        Collection<File> files = getFiles(date);
        Collection<File> result = new Vector<File>();
        logger.debug("fetched: " + files.size());
        for (File f : files) {
            filterByPattern(pattern, result, f);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public Collection<File> getFiles(Date date, String pattern, String machine) throws BinBaseException {
        logger.debug("searching for files of pattern: " + pattern);
        logger.debug("searching for files of machine: " + machine);
        logger.debug("searching for files of date: " + date);
        Query query = manager.createQuery("SELECT e FROM FileCacheEntityBean e WHERE e.machine = ?1 and e.fileDate = ?2");
        query.setParameter(1, machine);
        query.setParameter(2, date);
        @SuppressWarnings("unused") Collection<FileCacheEntityBean> result = query.getResultList();
        Collection<File> files = new Vector<File>();
        for (FileCacheEntityBean bean : result) {
            File f = bean.getFilePath();
            filterByPattern(pattern, files, f);
        }
        return files;
    }

    private void filterByPattern(String pattern, Collection<File> files, File f) {
        String name = FileUtil.cleanFileName(f.getName());
        if (name.matches(pattern)) {
            files.add(f);
            logger.debug("checking if matches: " + name + " vs " + pattern + " = true");
        } else {
            logger.debug("checking if matches: " + name + " vs " + pattern + " = false");
        }
    }

    @SuppressWarnings("unchecked")
    public Collection<File> getFilesByMachine(String machine) throws BinBaseException {
        logger.debug("searching for files of machine: " + machine);
        Query query = manager.createQuery("SELECT e FROM FileCacheEntityBean e WHERE e.machine = ?1");
        query.setParameter(1, machine);
        @SuppressWarnings("unused") Collection<FileCacheEntityBean> result = query.getResultList();
        Collection<File> files = new Vector<File>();
        for (FileCacheEntityBean bean : result) {
            files.add(bean.getFilePath());
        }
        return files;
    }

    @SuppressWarnings("unchecked")
    protected Collection<FileCacheEntityBean> getAllBeans() {
        Query query = manager.createQuery("SELECT e FROM FileCacheEntityBean e");
        return (Collection<FileCacheEntityBean>) query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public Collection<File> getFilesForDateRange(Date begin, Date end) throws BinBaseException {
        logger.debug("searching for files fors dates between : " + begin + " and " + end);
        Query query = manager.createQuery("SELECT e FROM FileCacheEntityBean e WHERE e.fileDate between ?1 and ?2");
        query.setParameter(1, begin);
        query.setParameter(2, end);
        @SuppressWarnings("unused") Collection<FileCacheEntityBean> result = query.getResultList();
        Collection<File> files = new Vector<File>();
        for (FileCacheEntityBean bean : result) {
            files.add(bean.getFilePath());
        }
        return files;
    }

    @SuppressWarnings("unchecked")
    public Collection<File> getFilesForDateRange(Date begin, Date end, String machine) throws BinBaseException {
        logger.debug("searching for files fors dates between : " + begin + " and " + end);
        Query query = manager.createQuery("SELECT e FROM FileCacheEntityBean e WHERE e.fileDate between ?1 and ?2 and e.machine = ?3");
        query.setParameter(1, begin);
        query.setParameter(2, end);
        query.setParameter(3, machine);
        @SuppressWarnings("unused") Collection<FileCacheEntityBean> result = query.getResultList();
        Collection<File> files = new Vector<File>();
        for (FileCacheEntityBean bean : result) {
            files.add(bean.getFilePath());
        }
        return files;
    }

    @SuppressWarnings("unchecked")
    public Collection<File> getFilesForDateRange(Date begin, Date end, String machine, String pattern) throws BinBaseException {
        logger.debug("searching for files fors dates between : " + begin + " and " + end);
        Query query = manager.createQuery("SELECT e FROM FileCacheEntityBean e WHERE e.fileDate between ?1 and ?2 and e.machine = ?3");
        query.setParameter(1, begin);
        query.setParameter(2, end);
        query.setParameter(3, machine);
        @SuppressWarnings("unused") Collection<FileCacheEntityBean> result = query.getResultList();
        Collection<File> files = new Vector<File>();
        logger.debug("fetched: " + files.size());
        for (FileCacheEntityBean bean : result) {
            if (bean.getFilePath().getName().matches(pattern)) {
                files.add(bean.getFilePath());
            }
        }
        return files;
    }

    @SuppressWarnings("unchecked")
    public Map<Date, Boolean> hasFiles(Date begin, Date end, String machine, String pattern) throws BinBaseException {
        logger.debug("searching for files fors dates between : " + begin + " and " + end);
        Query query = manager.createQuery("SELECT e FROM FileCacheEntityBean e WHERE e.fileDate between ?1 and ?2 and e.machine = ?3");
        query.setParameter(1, begin);
        query.setParameter(2, end);
        query.setParameter(3, machine);
        @SuppressWarnings("unused") Collection<FileCacheEntityBean> result = query.getResultList();
        logger.debug("fetched: " + result.size());
        Map<Date, Boolean> res = new HashMap<Date, Boolean>();
        for (FileCacheEntityBean bean : result) {
            if (bean.getFilePath().getName().matches(pattern)) {
                Date date = DateUtil.stripTime(bean.getFileDate());
                res.put(date, true);
            }
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    public Map<Date, Boolean> hasFiles(Date begin, Date end, String machine) throws BinBaseException {
        logger.debug("searching for files fors dates between : " + begin + " and " + end);
        Query query = manager.createQuery("SELECT e FROM FileCacheEntityBean e WHERE e.fileDate between ?1 and ?2 and e.machine = ?3");
        query.setParameter(1, begin);
        query.setParameter(2, end);
        query.setParameter(3, machine);
        @SuppressWarnings("unused") Collection<FileCacheEntityBean> result = query.getResultList();
        Map<Date, Boolean> res = new HashMap<Date, Boolean>();
        for (FileCacheEntityBean bean : result) {
            res.put(DateUtil.stripTime(bean.getFileDate()), true);
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    public Map<Date, Boolean> hasFiles(Date begin, Date end) throws BinBaseException {
        logger.debug("searching for files fors dates between : " + begin + " and " + end);
        Query query = manager.createQuery("SELECT e FROM FileCacheEntityBean e WHERE e.fileDate between ?1 and ?2");
        query.setParameter(1, begin);
        query.setParameter(2, end);
        @SuppressWarnings("unused") Collection<FileCacheEntityBean> result = query.getResultList();
        Map<Date, Boolean> res = new HashMap<Date, Boolean>();
        for (FileCacheEntityBean bean : result) {
            Date date = DateUtil.stripTime(bean.getFileDate());
            res.put(date, true);
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    public Map<Date, Boolean> hasFilesByPattern(Date begin, Date end, String pattern) throws BinBaseException {
        logger.debug("searching for files fors dates between : " + begin + " and " + end);
        Query query = manager.createQuery("SELECT e FROM FileCacheEntityBean e WHERE e.fileDate between ?1 and ?2");
        query.setParameter(1, begin);
        query.setParameter(2, end);
        @SuppressWarnings("unused") Collection<FileCacheEntityBean> result = query.getResultList();
        logger.debug("fetched: " + result.size());
        Map<Date, Boolean> res = new HashMap<Date, Boolean>();
        for (FileCacheEntityBean bean : result) {
            if (bean.getFilePath().getName().matches(pattern)) {
                Date date = DateUtil.stripTime(bean.getFileDate());
                res.put(date, true);
            }
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    public Collection<File> getFilesForDateRangeByPAttern(Date begin, Date end, String pattern) {
        logger.debug("searching for files fors dates between : " + begin + " and " + end);
        Query query = manager.createQuery("SELECT e FROM FileCacheEntityBean e WHERE e.fileDate between ?1 and ?2");
        query.setParameter(1, begin);
        query.setParameter(2, end);
        @SuppressWarnings("unused") Collection<FileCacheEntityBean> result = query.getResultList();
        logger.debug("fetched: " + result.size());
        Collection<File> res = new Vector<File>();
        for (FileCacheEntityBean bean : result) {
            if (bean.getFilePath().getName().matches(pattern)) {
                res.add(bean.getFilePath());
            }
        }
        return res;
    }
}
