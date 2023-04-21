package com.dcivision.framework.bean;

/**
  SysUserDefinedIndex.java

  This class is the serializable bean reflecting business logic uses.

    @author           Zoe Shum
    @company          DCIVision Limited
    @creation date    15/10/2003
    @version          $Revision: 1.9 $
*/
public class SysUserDefinedIndex extends AbstractBaseObject {

    public static final String REVISION = "$Revision: 1.9 $";

    static final long serialVersionUID = -5225656946492920077L;

    private Integer parentID = null;

    private String objectType = null;

    private String userDefinedType = null;

    private String description = null;

    private String genKeyTemplate = null;

    private Integer genKeyNumMax = null;

    private String importType = null;

    private Integer workflowRecordID = null;

    private Integer dmsParentID = null;

    private Integer dmsRootID = null;

    private String dmsScanFolderPath = null;

    private String dmsScanFolderCreationType = null;

    /** indicate this is a imported type of field as prefix
   *  A - ACME
   * */
    public static final String IMPORT_TYPE = "A";

    public SysUserDefinedIndex() {
        super();
    }

    public Integer getParentID() {
        return (this.parentID);
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public String getObjectType() {
        return (this.objectType);
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getUserDefinedType() {
        return (this.userDefinedType);
    }

    public void setUserDefinedType(String userDefinedType) {
        this.userDefinedType = userDefinedType;
    }

    public String getDescription() {
        return (this.description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenKeyTemplate() {
        return (this.genKeyTemplate);
    }

    public void setGenKeyTemplate(String genKeyTemplate) {
        this.genKeyTemplate = genKeyTemplate;
    }

    public Integer getGenKeyNumMax() {
        return (this.genKeyNumMax);
    }

    public void setGenKeyNumMax(Integer genKeyNumMax) {
        this.genKeyNumMax = genKeyNumMax;
    }

    public String getImportType() {
        return (this.importType);
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    public Integer getWorkflowRecordID() {
        return (this.workflowRecordID);
    }

    public void setWorkflowRecordID(Integer workflowRecordID) {
        this.workflowRecordID = workflowRecordID;
    }

    public Integer getDmsParentID() {
        return (this.dmsParentID);
    }

    public void setDmsParentID(Integer dmsParentID) {
        this.dmsParentID = dmsParentID;
    }

    public Integer getDmsRootID() {
        return (this.dmsRootID);
    }

    public void setDmsRootID(Integer dmsRootID) {
        this.dmsRootID = dmsRootID;
    }

    public String getDmsScanFolderPath() {
        return (this.dmsScanFolderPath);
    }

    public void setDmsScanFolderPath(String dmsScanFolderPath) {
        this.dmsScanFolderPath = dmsScanFolderPath;
    }

    public String getDmsScanFolderCreationType() {
        return (this.dmsScanFolderCreationType);
    }

    public void setDmsScanFolderCreationType(String dmsScanFolderCreationType) {
        this.dmsScanFolderCreationType = dmsScanFolderCreationType;
    }
}
