package edu.ucdavis.genomics.metabolomics.binbase.quality.server.ejb;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import edu.ucdavis.genomics.metabolomics.exception.BinBaseException;

/**
 * simple file cache service to save the search times for files
 * 
 * @author wohlgemuth
 */
@javax.ejb.Remote
public interface FileCacheService extends Serializable {

    /**
	 * cache all files in the given directory
	 * 
	 * @param directory
	 * @throws BinBaseException
	 */
    public void cacheFiles(String directory) throws BinBaseException;

    /**
	 * returns all the files matching this date
	 * 
	 * @param date
	 * @return
	 */
    public Collection<File> getFiles(Date date) throws BinBaseException;

    /**
	 * returns all files matching this pattern
	 * 
	 * @param pattern
	 * @return
	 */
    public Collection<File> getFiles(String pattern) throws BinBaseException;

    /**
	 * returns all files matching this pattern and date
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
    public Collection<File> getFiles(Date date, String pattern) throws BinBaseException;

    /**
	 * returns the files by pattern, machine and date
	 * 
	 * @param date
	 * @param pattern
	 * @param machine
	 * @return
	 * @throws BinBaseException
	 */
    public Collection<File> getFiles(Date date, String pattern, String machine) throws BinBaseException;

    /**
	 * returns the files by machine name
	 * 
	 * @param machine
	 * @return
	 * @throws BinBaseException
	 */
    public Collection<File> getFilesByMachine(String machine) throws BinBaseException;

    /**
	 * clears the cache
	 */
    public void clearCache() throws BinBaseException;

    /**
	 * returns all the files for the given range
	 * 
	 * @param begin
	 * @param end
	 * @return
	 * @throws BinBaseException
	 */
    public Collection<File> getFilesForDateRange(Date begin, Date end) throws BinBaseException;

    /**
	 * returns all the files for the given range and machine
	 * 
	 * @param begin
	 * @param end
	 * @param machine
	 * @return
	 * @throws BinBaseException
	 */
    public Collection<File> getFilesForDateRange(Date begin, Date end, String machine) throws BinBaseException;

    /**
	 * returns all the files for the given range and machine and pattern
	 * 
	 * @param begin
	 * @param end
	 * @param machine
	 * @param pattern
	 * @return
	 * @throws BinBaseException
	 */
    public Collection<File> getFilesForDateRange(Date begin, Date end, String machine, String pattern) throws BinBaseException;

    /**
	 * do we have files
	 * 
	 * @param begin
	 * @param end
	 * @param machine
	 * @param pattern
	 * @return
	 * @throws BinBaseException
	 */
    public Map<Date, Boolean> hasFiles(Date begin, Date end, String machine, String pattern) throws BinBaseException;

    /**
	 * do we have files
	 * 
	 * @param begin
	 * @param end
	 * @param machine
	 * @return
	 * @throws BinBaseException
	 */
    public Map<Date, Boolean> hasFiles(Date begin, Date end, String machine) throws BinBaseException;

    /**
	 * do we have files
	 * 
	 * @param begin
	 * @param end
	 * @return
	 * @throws BinBaseException
	 */
    public Map<Date, Boolean> hasFiles(Date begin, Date end) throws BinBaseException;

    /**
	 * do we have files
	 * 
	 * @param begin
	 * @param end
	 * @return
	 * @throws BinBaseException
	 */
    public Map<Date, Boolean> hasFilesByPattern(Date begin, Date end, String pattern) throws BinBaseException;

    public Collection<File> getFilesForDateRangeByPAttern(Date begin, Date end, String pattern);
}
