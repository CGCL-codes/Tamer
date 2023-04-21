package org.oclc.da.ndiipp.struts.analysis.util;

import java.util.ArrayList;
import org.oclc.da.ndiipp.series.SeriesConst;
import org.oclc.da.ndiipp.struts.core.util.NDIIPPUIBean;
import org.oclc.da.ndiipp.struts.core.util.ScheduleBean;
import org.oclc.da.ndiipp.struts.core.util.WebsiteListBean;
import org.oclc.da.ndiipp.system.SpiderSettingsConst;

/**
 * This class is used to model the series.
 * <P>
 * Created on May 26, 2005
 * @author Joseph Nelson
 */
public class SeriesBean extends NDIIPPUIBean implements Comparable {

    /**
     * Do a deep harvest on html files in an analysis based series.
     */
    public static final String DEEP = "Deep";

    /**
     * Do a shallow harvest on html files in an analysis based series.
     */
    public static final String SHALLOW = "Shallow";

    /**
     * Is the series active?
     */
    private boolean active = true;

    /**
     * Should we harvest all files?
     */
    private boolean allFiles = true;

    /**
     * What analysis is associated with this series?
     */
    private String associatedAnalysis = "";

    /**
     * This flag will let the user say if they want to extract metadata
     * automatically.
     */
    private String autoExtractMetadata = NO;

    /**
     * Should this series ingest a package via H&S as soon as it is harvested?
     */
    private boolean autoHSIngest = false;

    /**
     * Should this series ingest a package as soon as it is harvested?
     */
    private boolean autoIngest = false;

    /**
     * This flag will let the user say if they want the metadata automatically
     * harvested to OAI
     */
    private boolean autoMetadata = true;

    /**
     * Should this series create a package as soon as it is harvested?
     */
    private boolean autoPackage = false;

    /**
     * This is the date on which the series was created.
     */
    private String createDate = "";

    /**
     * When did the latest harvest run of this series last complete?
     */
    private String dateCompleted = "";

    /**
     * When is this series next scheduled to run?
     */
    private String dateScheduled = "";

    /**
     * When did the latest harvest run of this series last start?
     */
    private String dateStarted = "";

    /**
     * What is the guid for the associated DC metadata object?
     */
    private String dcMetadataGUID = "";

    /**
     * What is the name of the associated DC metadata object?
     */
    private String dcMetadataName = "";

    /**
     * What is this series all about?
     */
    private String description = "";

    /**
     * Should we harvest .doc files?
     */
    private boolean doc = false;

    /**
     * What entity did this series come from?
     */
    private String entity = "";

    /**
     * What is the GUID of the entity?
     */
    private String entityGUID = "";

    /**
     * Should an analysis based harvest be shallow or deep?
     */
    private String harvestDepth = SHALLOW;

    /**
     * This holds the scheduling information for this series
     */
    private ScheduleBean harvestSchedule = null;

    /**
     * Does this series plan to harvest html?
     */
    private boolean hasHTML = false;

    /**
     * Should we harvest content as individual objects?
     */
    private boolean individualObjects = false;

    /**
     * How is this series ranked among others?
     */
    private String macroAppraisalScore = "";

    /**
     * Is this series obsolete?
     */
    private boolean obsolete = false;

    /**
     * Should we harvest PDF files?
     */
    private boolean pdf = false;

    /**
     * What series is this series related to?
     */
    private ArrayList<ShortSeriesBean> relation = new ArrayList<ShortSeriesBean>();

    /**
     * What is the unique ID of the series?
     */
    private String seriesLocalID = "";

    /**
     * What is the OCLC Number for the series?
     */
    private String seriesOCLCNumber = "";

    /**
     * What is the unique ID of the series?
     */
    private String seriesUniqueID = "";

    /**
     * This is the sortWebsite for the analysis from which the series was
     * defined.
     */
    private String sortWebsite = "";

    /**
     * What is the guid for the associated spider settings object?
     */
    private String spiderSettingsGUID = "";

    /**
     * What is the name of the associated spider settings object?
     */
    private String spiderSettingsName = SpiderSettingsConst.DEFAULT;

    /**
     * What is the guid for the associated submission details object?
     */
    private String submissionDetailsGUID = "";

    /**
     * What is the name of the associated submission details object?
     */
    private String submissionDetailsName = "";

    /**
     * This is the pretty name for the series.
     */
    private String title = "";

    /**
     * What type of series is it?
     */
    private String type = SeriesConst.TYPE_SNAPSHOT;

    /**
     * This is the list of websites associated with the series.
     */
    private ArrayList<WebsiteListBean> websites = new ArrayList<WebsiteListBean>();

    /**
     * Should we harvest .xls files?
     */
    private boolean xls = false;

    /**
     * This is the default constructor for the class
     */
    public SeriesBean() {
        guid = "";
        active = true;
        allFiles = true;
        associatedAnalysis = "";
        autoExtractMetadata = NO;
        autoHSIngest = false;
        autoIngest = false;
        autoMetadata = true;
        autoPackage = false;
        createDate = "";
        dateCompleted = "";
        dateScheduled = "";
        dateStarted = "";
        dcMetadataGUID = "";
        dcMetadataName = "";
        description = "";
        doc = false;
        entity = "";
        entityGUID = "";
        individualObjects = false;
        harvestDepth = SHALLOW;
        hasHTML = false;
        macroAppraisalScore = "";
        obsolete = false;
        pdf = false;
        relation = new ArrayList<ShortSeriesBean>();
        harvestSchedule = null;
        seriesLocalID = "";
        seriesOCLCNumber = "";
        seriesUniqueID = "";
        spiderSettingsGUID = "";
        spiderSettingsName = SpiderSettingsConst.DEFAULT;
        submissionDetailsGUID = "";
        submissionDetailsName = "";
        title = "";
        type = SeriesConst.TYPE_SNAPSHOT;
        sortWebsite = "";
        websites = new ArrayList<WebsiteListBean>();
        xls = false;
    }

    /**
     * This is the default constructor for the class
     * <P>
     * @param guid The unique ID for the series
     * @param active Is the series active?
     * @param allFiles Should the series harvest all files?
     * @param associatedAnalysis The GUID of any analysis associated with the 
     * series
     * @param autoExtractMetadata Should the metadata be automatically extracted 
     * after the series is harvested?
     * @param autoHSIngest Should the series be ingested via H&S?
     * @param autoIngest Should the series be automatically ingested?
     * @param autoMetadata Should the metadata be sent to OAI?
     * @param autoPackage Should the series be automatically packaged?
     * @param createDate When was the series created?
     * @param dateCompleted When was the series competed?
     * @param dateScheduled When is the series next scheduled?
     * @param dateStarted When was the series harvest started?
     * @param dcMetadataGUID What is the GUID of the associated metadata object?
     * @param dcMetadataName What is the name of the associated metadata object?
     * @param description A useful description for the series
     * @param doc Should the series just harvest doc files?
     * @param entity The name of any associated entity
     * @param entityGUID The GUID of any associated entity
     * @param harvestDepth How deep should the harvest go?
     * @param hasHTML Is there any HTML in the content to be harvested?
     * @param individualObjects Should the site be harvested as individual 
     * objects? 
     * @param macroAppraisalScore A relative score given to the series by the 
     * user
     * @param obsolete Is the series obsolete?
     * @param pdf Should the series just harvest pdf files?
     * @param relation What other series are related to this one?
     * @param harvestSchedule On what schedule should the series be harvested?
     * @param seriesLocalID What is the local ID of the series
     * @param seriesOCLCNumber What OCLC number is assigned to the series?
     * @param seriesUniqueID What is the unique ID of the series
     * @param spiderSettingsGUID The GUID of the spider settings object used
     * @param spiderSettingsName The name of the spider settings object used
     * @param submissionDetailsGUID The GUID of the submission details object
     * used
     * @param submissionDetailsName The name of the submission details object 
     * used
     * @param title The series title
     * @param type The series type
     * @param sortWebsite What website URL will be used for sorting?
     * @param websites What websites are harvested in the series?
     * @param xls Should the series just harvest xls files?
     */
    public SeriesBean(String guid, boolean active, boolean allFiles, String associatedAnalysis, String autoExtractMetadata, boolean autoHSIngest, boolean autoIngest, boolean autoMetadata, boolean autoPackage, String createDate, String dateCompleted, String dateScheduled, String dateStarted, String dcMetadataGUID, String dcMetadataName, String description, boolean doc, String entity, String entityGUID, boolean individualObjects, String harvestDepth, boolean hasHTML, String macroAppraisalScore, boolean obsolete, boolean pdf, ArrayList<ShortSeriesBean> relation, ScheduleBean harvestSchedule, String seriesLocalID, String seriesOCLCNumber, String seriesUniqueID, String spiderSettingsGUID, String spiderSettingsName, String submissionDetailsGUID, String submissionDetailsName, String title, String sortWebsite, String type, ArrayList<WebsiteListBean> websites, boolean xls) {
        this.guid = guid;
        this.active = active;
        this.allFiles = allFiles;
        this.associatedAnalysis = associatedAnalysis;
        this.autoExtractMetadata = autoExtractMetadata;
        this.autoHSIngest = autoHSIngest;
        this.autoIngest = autoIngest;
        this.autoMetadata = autoMetadata;
        this.autoPackage = autoPackage;
        this.createDate = createDate;
        this.dateCompleted = dateCompleted;
        this.dateScheduled = dateScheduled;
        this.dateStarted = dateStarted;
        this.dcMetadataGUID = dcMetadataGUID;
        this.dcMetadataName = dcMetadataName;
        this.description = description;
        this.doc = doc;
        this.entity = entity;
        this.entityGUID = entityGUID;
        this.individualObjects = individualObjects;
        this.harvestDepth = harvestDepth;
        this.hasHTML = hasHTML;
        this.macroAppraisalScore = macroAppraisalScore;
        this.obsolete = obsolete;
        this.pdf = pdf;
        this.relation = relation;
        this.harvestSchedule = harvestSchedule;
        this.seriesLocalID = seriesLocalID;
        this.seriesOCLCNumber = seriesOCLCNumber;
        this.seriesUniqueID = seriesUniqueID;
        this.spiderSettingsGUID = spiderSettingsGUID;
        this.spiderSettingsName = spiderSettingsName;
        this.submissionDetailsGUID = submissionDetailsGUID;
        this.submissionDetailsName = submissionDetailsName;
        this.title = title;
        this.sortWebsite = sortWebsite;
        this.type = type;
        this.websites = websites;
        this.xls = xls;
    }

    /**
     * (non-Javadoc)
     * @see org.oclc.da.ndiipp.struts.core.util.NDIIPPUIBean#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object arg0) {
        SeriesBean sb0 = (SeriesBean) arg0;
        int titleSort = title.compareTo(sb0.getTitle());
        if (titleSort == 0) {
            return (sortWebsite.compareTo(sb0.getSortWebsite()));
        }
        return (titleSort);
    }

    /**
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object arg0) {
        SeriesBean sb0 = (SeriesBean) arg0;
        return (guid.equals(sb0.getGuid()));
    }

    /**
     * Gets the associatedAnalysis
     * <P>
     * @return Returns the associatedAnalysis.
     */
    public String getAssociatedAnalysis() {
        return associatedAnalysis;
    }

    /**
     * Gets the autoExtractMetadata
     * <P>
     * @return Returns the autoExtractMetadata.
     */
    public String getAutoExtractMetadata() {
        return autoExtractMetadata;
    }

    /**
     * Gets the createDate
     * <P>
     * @return Returns the createDate.
     */
    public String getCreateDate() {
        return (createDate);
    }

    /**
     * Gets the dateCompleted
     * <P>
     * @return Returns the dateCompleted.
     */
    public String getDateCompleted() {
        return dateCompleted;
    }

    /**
     * Gets the dateScheduled
     * <P>
     * @return Returns the dateScheduled.
     */
    public String getDateScheduled() {
        return dateScheduled;
    }

    /**
     * Gets the dateStarted
     * <P>
     * @return Returns the dateStarted.
     */
    public String getDateStarted() {
        return dateStarted;
    }

    /**
     * Gets the dcMetadataGUID
     * <P>
     * @return Returns the dcMetadataGUID.
     */
    public String getDcMetadataGUID() {
        return (dcMetadataGUID);
    }

    /**
     * Gets the dcMetadataName
     * <P>
     * @return Returns the dcMetadataName.
     */
    public String getDcMetadataName() {
        return (dcMetadataName);
    }

    /**
     * Gets the description
     * <P>
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the entity
     * <P>
     * @return Returns the entity.
     */
    public String getEntity() {
        return entity;
    }

    /**
     * Gets the entityGUID
     * <P>
     * @return Returns the entityGUID.
     */
    public String getEntityGUID() {
        return entityGUID;
    }

    /**
     * Gets the harvestDepth
     * <P>
     * @return Returns the harvestDepth.
     */
    public String getHarvestDepth() {
        return harvestDepth;
    }

    /**
     * Gets the harvestSchedule
     * <P>
     * @return Returns the harvestSchedule.
     */
    public ScheduleBean getHarvestSchedule() {
        return harvestSchedule;
    }

    /**
     * Gets the macroAppraisalScore
     * <P>
     * @return Returns the macroAppraisalScore.
     */
    public String getMacroAppraisalScore() {
        return macroAppraisalScore;
    }

    /**
     * Gets the relation
     * <P>
     * @return Returns the relation.
     */
    public ArrayList<ShortSeriesBean> getRelation() {
        return relation;
    }

    /**
     * Gets the seriesLocalID
     * <P>
     * @return Returns the seriesLocalID.
     */
    public String getSeriesLocalID() {
        return seriesLocalID;
    }

    /**
     * Gets the seriesOCLCNumber
     * <P>
     * @return Returns the seriesOCLCNumber.
     */
    public String getSeriesOCLCNumber() {
        return seriesOCLCNumber;
    }

    /**
     * Gets the seriesUniqueID
     * <P>
     * @return Returns the seriesUniqueID.
     */
    public String getSeriesUniqueID() {
        return seriesUniqueID;
    }

    /**
     * Gets the sortWebsite
     * <P>
     * @return Returns the sortWebsite.
     */
    public String getSortWebsite() {
        return (sortWebsite);
    }

    /**
     * Gets the spiderSettingsGUID
     * <P>
     * @return Returns the spiderSettingsGUID.
     */
    public String getSpiderSettingsGUID() {
        return (spiderSettingsGUID);
    }

    /**
     * Gets the spiderSettingsName
     * <P>
     * @return Returns the spiderSettingsName.
     */
    public String getSpiderSettingsName() {
        return (spiderSettingsName);
    }

    /**
     * Gets the submissionDetailsGUID
     * <P>
     * @return Returns the submissionDetailsGUID.
     */
    public String getSubmissionDetailsGUID() {
        return (submissionDetailsGUID);
    }

    /**
     * Gets the submissionDetailsName
     * <P>
     * @return Returns the submissionDetailsName.
     */
    public String getSubmissionDetailsName() {
        return (submissionDetailsName);
    }

    /**
     * Gets the title
     * <P>
     * @return Returns the title.
     */
    public String getTitle() {
        return (title);
    }

    /**
     * Gets the type
     * <P>
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets a website
     * <P>
     * @param index The index into the list for this websites
     * @return Returns the website.
     */
    public WebsiteListBean getWebsite(int index) {
        while (websites.size() <= index) {
            websites.add(new WebsiteListBean());
        }
        return (websites.get(index));
    }

    /**
     * Gets the websites
     * <P>
     * @return Returns the websites.
     */
    public ArrayList<WebsiteListBean> getWebsites() {
        return websites;
    }

    /**
     * Gets the number of websites
     * <P>
     * @return Returns the number of websites.
     */
    public String getWebsiteStatus() {
        String populated = FALSE;
        if (websites.size() > 0) {
            populated = TRUE;
        }
        return (populated);
    }

    /**
     * Gets the active
     * <P>
     * @return Returns the active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Gets the allFiles
     * <P>
     * @return Returns the allFiles.
     */
    public boolean isAllFiles() {
        return allFiles;
    }

    /**
     * Gets the autoHSIngest
     * <P>
     * @return Returns the autoHSIngest.
     */
    public boolean isAutoHSIngest() {
        return autoHSIngest;
    }

    /**
     * Gets the autoIngest
     * <P>
     * @return Returns the autoIngest.
     */
    public boolean isAutoIngest() {
        return autoIngest;
    }

    /**
     * Gets the autoMetadata
     * <P>
     * @return Returns the autoMetadata.
     */
    public boolean isAutoMetadata() {
        return autoMetadata;
    }

    /**
     * Gets the autoPackage
     * <P>
     * @return Returns the autoPackage.
     */
    public boolean isAutoPackage() {
        return autoPackage;
    }

    /**
     * Gets the doc
     * <P>
     * @return Returns the doc.
     */
    public boolean isDoc() {
        return doc;
    }

    /**
     * Gets the hasHTML
     * <P>
     * @return Returns the hasHTML.
     */
    public boolean isHasHTML() {
        return hasHTML;
    }

    /**
     * Gets the individualObjects
     * <P>
     * @return Returns the individualObjects.
     */
    public boolean isIndividualObjects() {
        return individualObjects;
    }

    /**
     * Gets the obsolete
     * <P>
     * @return Returns the obsolete.
     */
    public boolean isObsolete() {
        return (obsolete);
    }

    /**
     * Gets the pdf
     * <P>
     * @return Returns the pdf.
     */
    public boolean isPdf() {
        return pdf;
    }

    /**
     * Gets the xls
     * <P>
     * @return Returns the xls.
     */
    public boolean isXls() {
        return xls;
    }

    /**
     * Sets the active
     * <P>
     * @param active The active to set.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Sets the allFiles
     * <P>
     * @param allFiles The allFiles to set.
     */
    public void setAllFiles(boolean allFiles) {
        this.allFiles = allFiles;
    }

    /**
     * Sets the associatedAnalysis
     * <P>
     * @param associatedAnalysis The associatedAnalysis to set.
     */
    public void setAssociatedAnalysis(String associatedAnalysis) {
        this.associatedAnalysis = associatedAnalysis;
    }

    /**
     * Sets the autoExtractMetadata
     * <P>
     * @param autoExtractMetadata The autoExtractMetadata to set.
     */
    public void setAutoExtractMetadata(String autoExtractMetadata) {
        this.autoExtractMetadata = autoExtractMetadata;
    }

    /**
     * Sets the autoHSIngest
     * <P>
     * @param autoHSIngest The autoHSIngest to set.
     */
    public void setAutoHSIngest(boolean autoHSIngest) {
        this.autoHSIngest = autoHSIngest;
    }

    /**
     * Sets the autoIngest
     * <P>
     * @param autoIngest The autoIngest to set.
     */
    public void setAutoIngest(boolean autoIngest) {
        this.autoIngest = autoIngest;
    }

    /**
     * Sets the autoMetadata
     * <P>
     * @param autoMetadata The autoMetadata to set.
     */
    public void setAutoMetadata(boolean autoMetadata) {
        this.autoMetadata = autoMetadata;
    }

    /**
     * Sets the autoPackage
     * <P>
     * @param autoPackage The autoPackage to set.
     */
    public void setAutoPackage(boolean autoPackage) {
        this.autoPackage = autoPackage;
    }

    /**
     * Sets the createDate
     * <P>
     * @param createDate The createDate to set.
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * Sets the dateCompleted
     * <P>
     * @param dateCompleted The dateCompleted to set.
     */
    public void setDateCompleted(String dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    /**
     * Sets the dateScheduled
     * <P>
     * @param dateScheduled The dateScheduled to set.
     */
    public void setDateScheduled(String dateScheduled) {
        this.dateScheduled = dateScheduled;
    }

    /**
     * Sets the dateStarted
     * <P>
     * @param dateStarted The dateStarted to set.
     */
    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }

    /**
     * Sets the dcMetadataGUID
     * <P>
     * @param dcMetadataGUID The dcMetadataGUID to set.
     */
    public void setDcMetadataGUID(String dcMetadataGUID) {
        this.dcMetadataGUID = dcMetadataGUID;
    }

    /**
     * Sets the dcMetadataName
     * <P>
     * @param dcMetadataName The dcMetadataName to set.
     */
    public void setDcMetadataName(String dcMetadataName) {
        this.dcMetadataName = dcMetadataName;
    }

    /**
     * Sets the description
     * <P>
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the doc
     * <P>
     * @param doc The doc to set.
     */
    public void setDoc(boolean doc) {
        this.doc = doc;
    }

    /**
     * Sets the entity
     * <P>
     * @param entity The entity to set.
     */
    public void setEntity(String entity) {
        this.entity = entity;
    }

    /**
     * Sets the entityGUID
     * <P>
     * @param entityGUID The entityGUID to set.
     */
    public void setEntityGUID(String entityGUID) {
        this.entityGUID = entityGUID;
    }

    /**
     * Sets the harvestDepth
     * <P>
     * @param harvestDepth The harvestDepth to set.
     */
    public void setHarvestDepth(String harvestDepth) {
        this.harvestDepth = harvestDepth;
    }

    /**
     * Sets the harvestSchedule
     * <P>
     * @param harvestSchedule The harvestSchedule to set.
     */
    public void setHarvestSchedule(ScheduleBean harvestSchedule) {
        this.harvestSchedule = harvestSchedule;
    }

    /**
     * Sets the hasHTML
     * <P>
     * @param hasHTML The hasHTML to set.
     */
    public void setHasHTML(boolean hasHTML) {
        this.hasHTML = hasHTML;
    }

    /**
     * Sets the individualObjects
     * <P>
     * @param individualObjects The individualObjects to set.
     */
    public void setIndividualObjects(boolean individualObjects) {
        this.individualObjects = individualObjects;
    }

    /**
     * Sets the macroAppraisalScore
     * <P>
     * @param macroAppraisalScore The macroAppraisalScore to set.
     */
    public void setMacroAppraisalScore(String macroAppraisalScore) {
        this.macroAppraisalScore = macroAppraisalScore;
    }

    /**
     * Sets the obsolete
     * <P>
     * @param obsolete The obsolete to set.
     */
    public void setObsolete(boolean obsolete) {
        this.obsolete = obsolete;
    }

    /**
     * Sets the pdf
     * <P>
     * @param pdf The pdf to set.
     */
    public void setPdf(boolean pdf) {
        this.pdf = pdf;
    }

    /**
     * Sets the relation
     * <P>
     * @param relation The relation to set.
     */
    public void setRelation(ArrayList<ShortSeriesBean> relation) {
        this.relation = relation;
    }

    /**
     * Sets the seriesLocalID
     * <P>
     * @param seriesLocalID The seriesLocalID to set.
     */
    public void setSeriesLocalID(String seriesLocalID) {
        this.seriesLocalID = seriesLocalID;
    }

    /**
     * Sets the seriesOCLCNumber
     * <P>
     * @param seriesOCLCNumber The seriesOCLCNumber to set.
     */
    public void setSeriesOCLCNumber(String seriesOCLCNumber) {
        this.seriesOCLCNumber = seriesOCLCNumber;
    }

    /**
     * Sets the seriesUniqueID
     * <P>
     * @param seriesUniqueID The seriesUniqueID to set.
     */
    public void setSeriesUniqueID(String seriesUniqueID) {
        this.seriesUniqueID = seriesUniqueID;
    }

    /**
     * Sets the sortWebsite
     * <P>
     * @param sortWebsite The sortWebsite to set.
     */
    public void setSortWebsite(String sortWebsite) {
        this.sortWebsite = sortWebsite;
    }

    /**
     * Sets the spiderSettingsGUID
     * <P>
     * @param spiderSettingsGUID The spiderSettingsGUID to set.
     */
    public void setSpiderSettingsGUID(String spiderSettingsGUID) {
        this.spiderSettingsGUID = spiderSettingsGUID;
    }

    /**
     * Sets the spiderSettingsName
     * <P>
     * @param spiderSettingsName The spiderSettingsName to set.
     */
    public void setSpiderSettingsName(String spiderSettingsName) {
        this.spiderSettingsName = spiderSettingsName;
    }

    /**
     * Sets the submissionDetailsGUID
     * <P>
     * @param submissionDetailsGUID The submissionDetailsGUID to set.
     */
    public void setSubmissionDetailsGUID(String submissionDetailsGUID) {
        this.submissionDetailsGUID = submissionDetailsGUID;
    }

    /**
     * Sets the submissionDetailsName
     * <P>
     * @param submissionDetailsName The submissionDetailsName to set.
     */
    public void setSubmissionDetailsName(String submissionDetailsName) {
        this.submissionDetailsName = submissionDetailsName;
    }

    /**
     * Sets the title
     * <P>
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the type
     * <P>
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the websites
     * <P>
     * @param websites The websites to set.
     */
    public void setWebsites(ArrayList<WebsiteListBean> websites) {
        this.websites = websites;
    }

    /**
     * Sets the xls
     * <P>
     * @param xls The xls to set.
     */
    public void setXls(boolean xls) {
        this.xls = xls;
    }

    /**
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String seriesString = "Title = " + title;
        return (seriesString);
    }
}
