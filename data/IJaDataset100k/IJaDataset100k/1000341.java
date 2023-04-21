package au.org.tpac.portal.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PostFilter;
import au.org.tpac.portal.domain.AnzsrcCode;
import au.org.tpac.portal.domain.BlockedUrl;
import au.org.tpac.portal.domain.Category;
import au.org.tpac.portal.domain.Dataset;
import au.org.tpac.portal.domain.DatasetAttribute;
import au.org.tpac.portal.domain.DatasetCoverage;
import au.org.tpac.portal.domain.DatasetMeta;
import au.org.tpac.portal.domain.PartyIndividual;
import au.org.tpac.portal.domain.PartyOrganisation;
import au.org.tpac.portal.domain.RelatedDataset;
import au.org.tpac.portal.domain.RelatedInformation;
import au.org.tpac.portal.domain.RelatedParty;
import au.org.tpac.portal.domain.Subject;

/**
 * The Interface DatasetManager.
 */
public interface DatasetManager {

    /**
     * Gets the datasets.
     * 
     * @return the datasets
     */
    @PostFilter("hasPermission(filterObject, 'read') or hasRole('ROLE_ADMINISTRATOR')")
    List<Dataset> findDatasets();

    List<Dataset> findDatasets(List<Integer> ids);

    /**
     * Gets the ids and names.
     * 
     * @param CategoryId the category id
     * 
     * @return the ids and names
     */
    HashMap<Integer, String> findIdsAndNames(int CategoryId);

    /**
     * Gets the dataset.
     * 
     * @param id the id
     * 
     * @return the dataset
     */
    Dataset findDataset(int id);

    /**
     * Gets the dataset meta.
     * 
     * @param id the id
     * 
     * @return the dataset meta
     */
    Map<String, String> findDatasetMeta(int id);

    /**
     * Find dataset meta.
     *
     * @param key the key
     * @param value the value
     * @return the list of datasets
     */
    List<Dataset> findDatasets(String key, String value);

    /**
     * Gets the dataset attribute variables.
     * 
     * @param id the id
     * 
     * @return the dataset attribute variables
     */
    List<String> findDatasetAttributeVariables(int id);

    /**
     * Fetch datasets with tag.
     * 
     * @param tagid the tagid
     * @return the list
     */
    List<Dataset> findDatasetsWithTag(int tagid);

    /**
     * Fetch dataset tags.
     * 
     * @param string the string
     * @param datasetId the dataset id
     * @return the hash map
     */
    HashMap<String, Boolean> findDatasetTags(String string, int datasetId);

    /**
     * Gets the number of files.
     * 
     * @param datasetId the dataset id
     * @return the number of files in the dataset
     */
    int findNumberOfFiles(int datasetId);

    /**
     * Update dataset tags.
     * 
     * @param datasetId the dataset id
     * @param classification the classification
     * @param dataTypes the data types
     */
    void updateDatasetTags(int datasetId, String classification, HashMap<String, Boolean> dataTypes);

    /**
     * Insert dataset.
     * 
     * @param dataset the dataset
     * @return the int
     */
    int insertDataset(Dataset dataset);

    /**
     * Insert dataset metas.
     * 
     * @param datasetMetas the dataset metas
     */
    void insertDatasetMetas(List<DatasetMeta> datasetMetas);

    /**
     * Gets the dataset attributes.
     * 
     * @param datasetId the dataset id
     * @return the dataset attributes
     */
    List<DatasetAttribute> findDatasetAttributes(int datasetId);

    /**
     * Insert dataset attributes.
     * 
     * @param datasetAttributes the dataset attributes
     * @return the int
     */
    int insertDatasetAttributes(List<DatasetAttribute> datasetAttributes);

    /**
     * Creates the dataset attributes table.
     * 
     * @param datasetId the dataset id
     */
    void createDatasetAttributesTable(int datasetId);

    /**
     * Creates the dataset directories table.
     * 
     * @param datasetId the dataset id
     */
    void createDatasetDirectoriesTable(int datasetId);

    /**
	 * Creates the coverages table.
	 *
	 * @param datasetId the dataset id
	 */
    void createCoveragesTable(int datasetId);

    /**
     * Gets the datasets.
     * 
     * @param categoryId the category id
     * @return the datasets
     */
    @PostFilter("hasPermission(filterObject, 'read') or hasRole('ROLE_ADMINISTRATOR')")
    List<Dataset> getDatasets(int categoryId);

    /**
     * Update dataset meta.
     * 
     * @param datasetMeta the dataset meta
     */
    void updateDatasetMeta(DatasetMeta datasetMeta);

    /**
     * Delete dataset.
     * 
     * @param datasetId the dataset id
     * @return true, if successful
     */
    boolean deleteDataset(int datasetId);

    /**
     * Fetch ANZSRC Codes.
     * 
     * @param datasetId the dataset id
     * @return the list
     */
    List<AnzsrcCode> findAnzsrcCodes(int datasetId);

    /**
     * Fetch blocked urls.
     * 
     * @param datasetId the dataset id
     * @return the list
     */
    List<BlockedUrl> findBlockedUrls(int datasetId);

    /**
     * Fetch related parties.
     * 
     * @param datasetId the dataset id
     * @return the list
     */
    List<RelatedParty> findRelatedParties(int datasetId);

    /**
     * Fetch related information
     * 
     * @param datasetId the dataset id
     * @return the list
     */
    List<RelatedInformation> findRelatedInformation(int datasetId);

    /**
     * Fetch related dataset
     * 
     * @param datasetId the dataset id
     * @return the list
     */
    List<RelatedDataset> findRelatedDataset(int datasetId);

    /**
     * Fetch subjects
     * 
     * @param id dataset id
     * @return list of subjects for dataset
     */
    List<Subject> findDatasetSubject(int id);

    /**
     * Replace subjects. Delete any that may exist, add new set.
     * 
     * @param subject New set of subjects
     * @param datasetId dataset id
     */
    void replaceDatasetSubject(List<Subject> subject, int datasetId);

    /**
     * Creates the dataset.
     * 
     * @param datasetName the dataset name
     * @param categoryId the category id
     * @return the string
     */
    int createDataset(String datasetName, int categoryId);

    /**
     * Update ANZSRC codes.
     * 
     * @param codes the ANZSRC codes
     * @param datasetId the dataset id
     */
    void replaceAnzsrcCodes(List<AnzsrcCode> codes, int datasetId);

    /**
     * Update blocked urls.
     * 
     * @param blockedUrls the blocked urls
     * @param datasetId the dataset id
     */
    void replaceBlockedUrls(List<BlockedUrl> blockedUrls, int datasetId);

    /**
     * Update related parties.
     * 
     * @param relatedParties the related parties
     * @param datasetId the dataset id
     */
    void replaceRelatedParties(List<RelatedParty> relatedParties, int datasetId);

    /**
     * Update related information items.
     * 
     * @param relatedInformation the related info
     * @param datasetId the dataset id
     */
    void replaceRelatedInformation(List<RelatedInformation> relatedInformation, int datasetId);

    /**
     * Update related dataset items.
     * 
     * @param related the related datasets
     * @param datasetId the dataset id
     */
    void replaceRelatedDataset(List<RelatedDataset> related, int datasetId);

    /**
     * List related parties using a selected party
     * @return list of individual parties
     */
    List<RelatedParty> findRelatedPartiesUsingParty(PartyOrganisation party);

    /**
     * List related parties using a selected party
     * @return list of individual parties
     */
    List<RelatedParty> findRelatedPartiesUsingParty(PartyIndividual party);

    /**
     * Update dataset.
     * 
     * @param dataset the dataset
     * @param datasetMetas the dataset metas
     */
    void updateDataset(Dataset dataset, List<DatasetMeta> datasetMetas);

    /**
	 * Creates the file attributes table.
	 *
	 * @param datasetId the dataset id
	 */
    void createFileAttributesTable(int datasetId);

    /**
	 * Creates the attribute data table.
	 *
	 * @param datasetId the dataset id
	 */
    void createAttributeDataTable(int datasetId);

    /**
	 * Update dataset coverage.
	 *
	 * @param datasetCoverage the dataset coverage
	 */
    void updateDatasetCoverage(DatasetCoverage datasetCoverage);

    /**
	 * Insert dataset coverage.
	 *
	 * @param datasetCoverage the dataset coverage
	 */
    void insertDatasetCoverage(DatasetCoverage datasetCoverage);
}
