package net.sourceforge.taverna.publish;

import org.apache.commons.vfs.FileObject;

/**
 * This interface defines the methods needed by a Repository implementation. The
 * Repository represents anywhere that workflows, or data files may be stored. A
 * repository can be a network file share, a WebDAV repository, or any other
 * file system.
 * 
 * Last edited by $Author: wassinki $
 * 
 * @author Mark
 * @version $Revision: 1.1 $
 */
public interface Repository {

    /**
	 * This method sets the base URL for the repository.
	 * 
	 * @return
	 */
    public String getBaseURL();

    /**
	 * This method sets the base URL for the repository
	 * 
	 * @param baseURL
	 */
    public void setBaseURL(String baseURL);

    /**
	 * This method returns the FileObject corresponding to the baseURL.
	 * 
	 * @return
	 */
    public FileObject getRoot();

    /**
	 * This method gets the name of the Repository.
	 * 
	 * @return
	 */
    public String getName();

    /**
	 * This method sets the name of the Repository. This name will appear in a
	 * list box of repository names, so keep it short.
	 * 
	 * @param name
	 */
    public void setName(String name);

    /**
	 * This method searches the repository for a file whose
	 * 
	 * @param startingDir
	 *            The directory from which to start the search. This should
	 *            default to "/" the root directory.
	 * @param regex
	 *            A regular expression that can be applied to each file.
	 * @param srchRecursively
	 *            Indicates whether subdirectories should also be searched for
	 *            files that match the regular expression
	 * @return A list of files that match the regular expression
	 */
    public FileObject[] searchByFileName(FileObject startingDir, String regex, boolean srchRecursively);

    /**
	 * 
	 * @param startingDir
	 *            The directory from which to start the search. This should
	 *            default to "/" the root directory.
	 * @param regex
	 *            A regular expression that can be applied to each file.
	 * @param srchRecursively
	 *            Indicates whether subdirectories should also be searched for
	 *            files that match the regular expression
	 * @return A list of files that match the regular expression
	 */
    public FileObject[] searchByWorkFlowDescription(FileObject startingDir, String regex, boolean srchRecursively);

    /**
	 * This method searches for workflows whose author is specified by a
	 * 
	 * @param startingDir
	 *            The directory from which to start the search. This should
	 *            default to "/" the root directory.
	 * @param regex
	 *            A regular expression that can be applied to each file.
	 * @param srchRecursively
	 *            Indicates whether subdirectories should also be searched for
	 *            files that match the regular expression
	 * @return A list of files that match the regular expression
	 */
    public FileObject[] searchByWorkFlowAuthor(FileObject startingDir, String authorName, boolean srchRecursively);

    /**
	 * This method publishes a list of files, to selected directory. If the
	 * directory is null, the filelist will be published to the root of the
	 * repository.
	 * 
	 * @param filelist
	 *            The list of files to be written.
	 * @param startingDir
	 *            The directory in which to deposit the files. This directory is
	 *            relative to the repository root directory.
	 * @throws PublicationException
	 */
    public void publish(FileObject[] filelist, FileObject startingDir) throws PublicationException;

    /**
	 * This method deletes a list of files from the repository.
	 * 
	 * @param filelist
	 * @throws PublicationException
	 */
    public void delete(FileObject[] filelist) throws PublicationException;
}
