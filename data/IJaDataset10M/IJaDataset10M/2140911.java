package org.amlfilter.service;

import java.io.File;
import java.util.Properties;
import org.amlfilter.model.EntitySource;
import org.amlfilter.model.Process;
import org.amlfilter.service.GenericService;
import org.amlfilter.util.GeneralConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

public class DesignationService extends GenericService implements InitializingBean {

    public static final String DESIGNATION_TYPE_PARAMETER = "designation_type";

    public static final String DESIGNATION_MANDATORY = "M";

    public static final String DESIGNATION_NONMANDATORY = "N";

    public static final String DESIGNATION_PEP = "P";

    public static final String DESIGNATION_NOT_APPLICABLE = "N/A";

    private ProcessService mProcessService;

    private EntitySourcesService mEntitySourcesService;

    private String mDefaultMandatorySubListsFilePath;

    private String mDefaultNonMandatorySubListsFilePath;

    private String mDefaultPepSubListsFilePath;

    private Properties mMasterListToDefaultDesignationMap = new Properties();

    /**
     * Get the process service
     * @return The process service
     */
    public ProcessService getProcessService() {
        return mProcessService;
    }

    public void setProcessService(ProcessService pProcessService) {
        mProcessService = pProcessService;
    }

    /**
     * Get the entity sources service
     * @return The entity sources service
     */
    public EntitySourcesService getEntitySourcesService() {
        return mEntitySourcesService;
    }

    /**
     * Get the entity sources service
     * @return The entity sources service
     */
    public void setEntitySourcesService(EntitySourcesService pEntitySourcesService) {
        mEntitySourcesService = pEntitySourcesService;
    }

    /**
	 * Get the default mandatory sublists file path
	 * @return The default mandatory sublists file path
	 */
    public String getDefaultMandatorySubListsFilePath() {
        return mDefaultMandatorySubListsFilePath;
    }

    /**
	 * Set the default mandatory sublists file path
	 * @param pDefaultMandatorySubListsFilePath The default mandatory sublists file path
	 */
    public void setDefaultMandatorySubListsFilePath(String pDefaultMandatorySubListsFilePath) {
        mDefaultMandatorySubListsFilePath = pDefaultMandatorySubListsFilePath;
    }

    /**
	 * Get the default non-mandatory sublists file path
	 * @return The default non-mandatory sublists file path
	 */
    public String getDefaultNonMandatorySubListsFilePath() {
        return mDefaultNonMandatorySubListsFilePath;
    }

    /**
	 * Set the default non-mandatory sublists file path
	 * @param pDefaultNonMandatorySubListsFilePath The default non-mandatory sublists file path
	 */
    public void setDefaultNonMandatorySubListsFilePath(String pDefaultNonMandatorySubListsFilePath) {
        mDefaultNonMandatorySubListsFilePath = pDefaultNonMandatorySubListsFilePath;
    }

    /**
	 * Get the default pep sublists file path
	 * @return The default pep sublists file path
	 */
    public String getDefaultPepSubListsFilePath() {
        return mDefaultPepSubListsFilePath;
    }

    /**
	 * Set the default pep sublists file path
	 * @param pDefaultPepSubListsFilePath The default pep sublists file path
	 */
    public void setDefaultPepSubListsFilePath(String pDefaultPepSubListsFilePath) {
        mDefaultPepSubListsFilePath = pDefaultPepSubListsFilePath;
    }

    /**
	 * Get the master list to default designation map
	 * @return The master list to default designation map
	 */
    public Properties getMasterListToDefaultDesignationMap() {
        return mMasterListToDefaultDesignationMap;
    }

    /**
	 * Set the master list to default designation map
	 * @param pMasterListToDefaultDesignationMap The master list to default designation map
	 */
    public void setMasterListToDefaultDesignationMap(Properties pMasterListToDefaultDesignationMap) {
        mMasterListToDefaultDesignationMap = pMasterListToDefaultDesignationMap;
    }

    /**
	 * Get the default designation for the master list
	 * @return The default designation
	 */
    public String getDefaultDesignationForMasterList(String pMapListName) {
        return getMasterListToDefaultDesignationMap().getProperty(pMapListName);
    }

    /**
	 * Get the list designation from the process of interest
	 * @param pProcessId The process id
	 * @return The list designation
	 */
    public String getProcessListDesignation(Long pProcessId) {
        Process process = (Process) getProcessService().getProcessMap().get(pProcessId);
        String designationKey = (String) process.getConfiguration().getConfigurationEntryMap().get(DESIGNATION_TYPE_PARAMETER);
        if (null == designationKey) {
            throw new IllegalStateException("Could not find designation key (" + designationKey + ") for process id: " + pProcessId);
        }
        return designationKey;
    }

    /**
	 * Gets the designation by entity sources
	 * Note: Here there is some hierarchical logic where
	 * Mandatory takes precedence, the non-mandatory then
	 * finally All
	 * @param pEntitySourceCodes The entity sources string arrays
	 * @return The designation
	 */
    public String getDesignationByEntitySources(String[] pEntitySourceCodes) {
        String strongestDesignation = DESIGNATION_NOT_APPLICABLE;
        for (int i = 0; i < pEntitySourceCodes.length; i++) {
            String entitySourceCode = pEntitySourceCodes[i];
            EntitySource entitySource = getEntitySourcesService().getEntitySourceByKey(entitySourceCode);
            if (null != entitySource) {
                strongestDesignation = compareAndReturnMostPrevalentDesignation(strongestDesignation, entitySource.getDesignation());
            } else {
                throw new IllegalArgumentException("The entity source (" + entitySourceCode + ") is unknown");
            }
        }
        return strongestDesignation;
    }

    private String compareAndReturnMostPrevalentDesignation(String pDesig1, String pDesig2) {
        String retVal = DESIGNATION_NOT_APPLICABLE;
        if (pDesig1.equals(DESIGNATION_MANDATORY) || pDesig2.equals(DESIGNATION_MANDATORY)) {
            return DESIGNATION_MANDATORY;
        }
        if (pDesig1.equals(DESIGNATION_NONMANDATORY) || pDesig2.equals(DESIGNATION_NONMANDATORY)) {
            return DESIGNATION_NONMANDATORY;
        }
        if (pDesig1.equals(DESIGNATION_PEP) || pDesig2.equals(DESIGNATION_PEP)) {
            return DESIGNATION_PEP;
        }
        return retVal;
    }

    /**
	 * Sets the default configurations for the sublists mandatory, non-mandatory and
	 * It essentially read the default mandatory configuration and tokenizes
	 * it and then updates the entity source with the mandatory designation.
	 * Note: This is essentially done only once during the installation, after the designation UI takes over
	 * @throws Exception
	 */
    public void setDefaultDesignationsForSubLists() throws Exception {
        File defaultMandatorySubListsFile = new File(getDefaultMandatorySubListsFilePath());
        if (defaultMandatorySubListsFile.exists()) {
            LineIterator lineIterator = FileUtils.lineIterator(defaultMandatorySubListsFile, GeneralConstants.UTF8);
            while (lineIterator.hasNext()) {
                String entitySourceKey = (String) lineIterator.next();
                EntitySource entitySource = getEntitySourcesService().getEntitySourceByKey(entitySourceKey);
                if (null == entitySource) {
                    logWarning("No entity source found for entity source key: " + entitySourceKey + " adding entity source (" + entitySourceKey + ") with DESIGNATION_MANDATORY");
                    entitySource = new EntitySource();
                    getEntitySourcesService().putEntitySource(entitySourceKey, entitySource);
                }
                entitySource.setDesignation(DESIGNATION_MANDATORY);
            }
        }
        File defaultNonMandatorySubListsFile = new File(getDefaultPepSubListsFilePath());
        if (defaultNonMandatorySubListsFile.exists()) {
            LineIterator lineIterator = FileUtils.lineIterator(defaultNonMandatorySubListsFile, GeneralConstants.UTF8);
            while (lineIterator.hasNext()) {
                String entitySourceKey = (String) lineIterator.next();
                EntitySource entitySource = getEntitySourcesService().getEntitySourceByKey(entitySourceKey);
                if (null == entitySource) {
                    logWarning("No entity source found for entity source key: " + entitySourceKey + " adding entity source (" + entitySourceKey + ") with designation DESIGNATION_NONMANDATORY");
                    entitySource = new EntitySource();
                    getEntitySourcesService().putEntitySource(entitySourceKey, entitySource);
                }
                entitySource.setDesignation(DESIGNATION_NONMANDATORY);
            }
        }
        File defaultPepSubListsFile = new File(getDefaultPepSubListsFilePath());
        if (defaultPepSubListsFile.exists()) {
            LineIterator lineIterator = FileUtils.lineIterator(defaultPepSubListsFile, GeneralConstants.UTF8);
            while (lineIterator.hasNext()) {
                String entitySourceKey = (String) lineIterator.next();
                EntitySource entitySource = getEntitySourcesService().getEntitySourceByKey(entitySourceKey);
                if (null == entitySource) {
                    logWarning("No entity source found for entity source key: " + entitySourceKey + " adding entity source (" + entitySourceKey + ") with designation DESIGNATION_PEP");
                    entitySource = new EntitySource();
                    getEntitySourcesService().putEntitySource(entitySourceKey, entitySource);
                }
                entitySource.setDesignation(DESIGNATION_PEP);
            }
        }
        getEntitySourcesService().serializeEntitySourcesMap();
        logInfo("entitySourceMap: " + getEntitySourcesService().getEntitySourceMap());
    }

    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (isLoggingInfo()) {
            logInfo("masterListToDefaultDesignationMap: " + getMasterListToDefaultDesignationMap());
        }
    }

    public static void main(String[] args) throws Exception {
        String basePath = "/Projects/AMLFilter/workspace/amlf-loader/";
        PropertyConfigurator.configure(basePath + "src/amlf-loader_log4j.properties");
        XmlBeanFactory beanFactory = new XmlBeanFactory(new FileSystemResource(basePath + "/src/amlf-loader_applicationContext.xml"));
        PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
        cfg.setLocation(new FileSystemResource(basePath + "src/amlf-loader_admin-config.properties"));
        cfg.postProcessBeanFactory(beanFactory);
        DesignationService designationService = (DesignationService) beanFactory.getBean("designationService");
        designationService.setDefaultDesignationsForSubLists();
    }
}
