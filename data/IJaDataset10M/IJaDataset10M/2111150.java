package org.jcvi.vics.server.access;

import org.jcvi.vics.model.download.*;
import org.jcvi.vics.model.metadata.Sample;
import org.jcvi.vics.server.access.hibernate.DaoException;
import org.springframework.dao.DataAccessException;
import java.util.List;
import java.util.Set;

/**
 * Database Access Object for getting data about downloadable files.
 * <p/>
 * User: lfoster
 * Date: Oct 24, 2006
 * Time: 11:31:01 AM
 */
public interface DownloadDAO extends DAO {

    /**
     * Finder to get the project the given accession number
     *
     * @param publicationAccessionNo publication's accession number
     * @return the publication with the given accession number
     * @throws DataAccessException from methods called.
     * @throws DaoException        from methods called.
     */
    Publication findPublicationByAccessionNo(String publicationAccessionNo) throws DataAccessException, DaoException;

    /**
     * Finder to get all downloadable samples for Project.
     *
     * @return list of all samples for a project.
     * @throws DataAccessException from methods called.
     * @throws DaoException        from methods called.
     */
    List<Sample> findProjectSamples(String projectSymbol) throws DataAccessException, DaoException;

    /**
     * Get downloadable samples for Project, organized by Project.
     *
     * @return List<Object[]> where Object[0] is Sample and Object[1] is project symbol as String
     */
    List<Object[]> findProjectSamplesByProject() throws DataAccessException, DaoException;

    /**
     * Finder to get the project the given accession number
     *
     * @param projectSymbol project's symbol
     * @return the project with the given symbol
     * @throws DataAccessException from methods called.
     * @throws DaoException        from methods called.
     */
    Project findReleasedProjectBySymbol(String projectSymbol) throws DataAccessException, DaoException;

    /**
     * Retrieve data files for a given sample
     *
     * @param sampleAcc sample's accession
     * @return a list of data files
     */
    List<DataFile> findDataFilesBySampleAcc(String sampleAcc) throws DaoException;

    /**
     * Finder to get all instances of Project, from the database.
     *
     * @return list of all known projects.
     * @throws DataAccessException from methods called.
     * @throws DaoException        from methods called.
     */
    List<Project> findAllProjects() throws DataAccessException, DaoException;

    /**
     * Finder to get all instances of Publication, from the database.
     *
     * @return list of all known publications.
     * @throws DataAccessException from methods called.
     * @throws DaoException        from methods called.
     */
    List<Publication> findAllPublications() throws DataAccessException, DaoException;

    void saveOrUpdateProject(Project project);

    /**
     * Return a new author model object.
     *
     * @param name of auth
     * @return the created author
     * @throws DataAccessException from called methods
     * @throws DaoException        from called methods
     */
    Author createAuthor(String name) throws DataAccessException, DaoException;

    /**
     * Return a new data file model object.
     *
     * @param path             to the file
     * @param infoLocation     where is its format description
     * @param description      what is the file
     * @param size             how big is the file
     * @param multifileArchive is it tar.gz
     * @param samples          what samples referred-to
     * @return the created author
     * @throws DataAccessException from called methods
     * @throws DaoException        from called methods
     */
    DataFile createDataFile(String path, String infoLocation, String description, long size, boolean multifileArchive, Set samples) throws DataAccessException, DaoException;

    /**
     * Return a new publication model object.
     *
     * @param abstractOfPublication its abstract
     * @param summary               its summary
     * @param title                 its title
     * @param subjectDocument       pointer to document itself.
     * @param authors               who wrote it
     * @param rolledUpArchives      combined data if exists
     * @param hierarchyRootNodes    hierarchies of data files to which the document pertains.
     * @return the constructed object
     * @throws DataAccessException from called methods
     * @throws DaoException        from called methods
     */
    Publication createPublication(String abstractOfPublication, String summary, String title, String subjectDocument, List authors, List rolledUpArchives, List hierarchyRootNodes) throws DataAccessException, DaoException;

    /**
     * Retun a new project model object.
     *
     * @param symbol       short name for project.
     * @param description  long description for project.
     * @param publications list of pubs under project.
     * @return the created object
     * @throws DataAccessException from called methods
     * @throws DaoException        from called methods
     */
    Project createProject(String symbol, String description, List publications) throws DataAccessException, DaoException;

    /**
     * Return a new hierarchy node object that points to the datafiles given, and has the
     * subnodes given.
     *
     * @param name        name of node
     * @param description description of node.
     * @param dataFiles   files under this node
     * @param children    sub-nodes.
     * @return the created node.
     * @throws DataAccessException from called methods
     * @throws DaoException        from called methods
     */
    HierarchyNode createHierarchyNode(String name, String description, List dataFiles, List children) throws DataAccessException, DaoException;
}
