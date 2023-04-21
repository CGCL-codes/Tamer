package org.colimas.web.forms;

import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;
import org.colimas.utils.GenerateSerialUtil;

/**
 * <h3>ComponentInfoForm.java</h3>
 *
 * <P>
 * Function:<BR />
 * Saving the fields value of 
 * pc001_e.jsp  Component info edit page
 * </P>
 * @author zhao lei
 * @version 1.11
 *
 * Modification History:
 * <PRE>
 * SEQ DATE       ORDER DEVELOPER      DESCRIPTION
 * --- ---------- ----- -------------- -----------------------------
 * 001 2005/12/11          zhao lei       INIT
 * 002 2006/01/14          zhao lei       coding
 * 003 2006/02/13          zhao lei       add serialno
 * 004 2006/02/22          zhao lei       change int->String
 * 005 2006/02/28          zhao lei       ActionForm->ValidatorForm
 * 006 2006/03/26          zhao lei       add projsn
 * </PRE>
 */
public class ComponentInfoForm extends ValidatorForm {

    public static final long serialVersionUID = 1;

    private String environmentDescription = "";

    private String functionDescription = "";

    private String isPublic = "Y";

    private String admins = "";

    private String devlopers = "";

    private String developmentPlatforms = "";

    private String developmentLanguages = "";

    private String authors = "";

    private String alias = "";

    private String name = "";

    private String version = "1.0";

    private String status = "Unconfirmed";

    private String serialno = "";

    private String confirmedDate = "N/A";

    private String confirmedBy = "N/A";

    private String createdDate = "";

    private String createdBy = "";

    private String updatedDate = "";

    private String updatedBy = "";

    private String extractedFunction = "";

    private FormFile file = null;

    private String projsn = "";

    /**
	 *<p>get confirmedBy</p>
	 * @return Returns the confirmedBy.
	 */
    public String getConfirmedBy() {
        return confirmedBy;
    }

    /**
	 * <p>set confirmedBy</p>
	 * @param confirmedBy The confirmedBy to set.
	 */
    public void setConfirmedBy(String confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    /**
	 *<p>get confirmedDate</p>
	 * @return Returns the confirmedDate.
	 */
    public String getConfirmedDate() {
        return confirmedDate;
    }

    /**
	 * <p>set confirmedDate</p>
	 * @param confirmedDate The confirmedDate to set.
	 */
    public void setConfirmedDate(String confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    /**
	 *<p>get createdBy</p>
	 * @return Returns the createdBy.
	 */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
	 * <p>set createdBy</p>
	 * @param createdBy The createdBy to set.
	 */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
	 *<p>get createdDate</p>
	 * @return Returns the createdDate.
	 */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
	 * <p>set createdDate</p>
	 * @param createdDate The createdDate to set.
	 */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    /**
	 *<p>get updatedBy</p>
	 * @return Returns the updatedBy.
	 */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
	 * <p>set updatedBy</p>
	 * @param updatedBy The updatedBy to set.
	 */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
	 *<p>get updatedDate</p>
	 * @return Returns the updatedDate.
	 */
    public String getUpdatedDate() {
        return updatedDate;
    }

    /**
	 * <p>set updatedDate</p>
	 * @param updatedDate The updatedDate to set.
	 */
    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
	 * <p>get admins</p>
	 * @return Returns the admins.
	 */
    public String getAdmins() {
        return admins;
    }

    /**
	 * <p>set admins</p>
	 * @param admins The admins to set.
	 */
    public void setAdmins(String admins) {
        this.admins = admins;
    }

    /**
	 * <p>get alias</p>
	 * @return Returns the alias.
	 */
    public String getAlias() {
        return alias;
    }

    /**
	 * <p>set alias</p>
	 * @param alias The alias to set.
	 */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
	 * <p>get authors</p>
	 * @return Returns the authors.
	 */
    public String getAuthors() {
        return authors;
    }

    /**
	 * <p>set authors</p>
	 * @param authors The authors to set.
	 */
    public void setAuthors(String authors) {
        this.authors = authors;
    }

    /**
	 * <p>get developmentLanguages</p>
	 * @return Returns the developmentLanguages.
	 */
    public String getDevelopmentLanguages() {
        return developmentLanguages;
    }

    /**
	 * <p>set developmentLanguages</p>
	 * @param developmentLanguages The developmentLanguages to set.
	 */
    public void setDevelopmentLanguages(String developmentLanguages) {
        this.developmentLanguages = developmentLanguages;
    }

    /**
	 * <p>get developmentPlatforms</p>
	 * @return Returns the developmentPlatforms.
	 */
    public String getDevelopmentPlatforms() {
        return developmentPlatforms;
    }

    /**
	 * <p>set developmentPlatforms</p>
	 * @param developmentPlatforms The developmentPlatforms to set.
	 */
    public void setDevelopmentPlatforms(String developmentPlatforms) {
        this.developmentPlatforms = developmentPlatforms;
    }

    /**
	 * <p>get devlopers</p>
	 * @return Returns the devlopers.
	 */
    public String getDevlopers() {
        return devlopers;
    }

    /**
	 * <p>set devlopers</p>
	 * @param devlopers The devlopers to set.
	 */
    public void setDevlopers(String devlopers) {
        this.devlopers = devlopers;
    }

    /**
	 * <p>get environmentDescription</p>
	 * @return Returns the environmentDescription.
	 */
    public String getEnvironmentDescription() {
        return environmentDescription;
    }

    /**
	 * <p>set environmentDescription</p>
	 * @param environmentDescription The environmentDescription to set.
	 */
    public void setEnvironmentDescription(String environmentDescription) {
        this.environmentDescription = environmentDescription;
    }

    /**
	 * <p>get functionDescription</p>
	 * @return Returns the functionDescription.
	 */
    public String getFunctionDescription() {
        return functionDescription;
    }

    /**
	 * <p>set functionDescription</p>
	 * @param functionDescription The functionDescription to set.
	 */
    public void setFunctionDescription(String functionDescription) {
        this.functionDescription = functionDescription;
    }

    /**
	 * <p>get isPublic
	 * Y | N
	 * </p>
	 * @return Returns the isPublic.
	 */
    public String getIsPublic() {
        return isPublic;
    }

    /**
	 * <p>set isPublic
	 * Y | N
	 * </p>
	 * @param isPublic The isPublic to set.
	 */
    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    /**
	 * <p>get name</p>
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * <p>set name</p>
	 * @param name The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 *Unconfirmed
	 *Confirmed   
	 *Deleted     
	 * @return Returns the status.
	 */
    public String getStatus() {
        return status;
    }

    /**
	 * <p>set status
	 * Unconfirmed
	 * Confirmed   
	 * Deleted 
	 * </p>
	 * @param status The status to set.
	 */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
	 * <p>get version</p>
	 * @return Returns the version.
	 */
    public String getVersion() {
        return version;
    }

    /**
	 * <p>set version</p>
	 * @param version The version to set.
	 */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
	 *<p>get serialno
	 *0 not checked
	 *1 checked
	 *</p>
	 * @return Returns the serialno.
	 */
    public String getSerialno() {
        if (this.serialno.equals("")) {
            this.serialno = GenerateSerialUtil.getComsn();
        }
        return this.serialno;
    }

    /**
	 * <p>set serialno
	 *0 not checked
	 *1 checked
	 * </p>
	 * @param serialno The serialno to set.
	 */
    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    /**
	 *<p>get extractedFunction</p>
	 * @return Returns the extractedFunction.
	 */
    public String getExtractedFunction() {
        if (this.functionDescription == null) {
            return extractedFunction;
        }
        if (this.functionDescription.length() <= 50) this.extractedFunction = this.functionDescription; else {
            this.extractedFunction = this.functionDescription.substring(0, 50);
            this.extractedFunction += "...";
        }
        return extractedFunction;
    }

    /**
	 *<p>get file</p>
	 * @return Returns the file.
	 */
    public FormFile getFile() {
        return file;
    }

    /**
	 * <p>set file</p>
	 * @param file The file to set.
	 */
    public void setFile(FormFile file) {
        this.file = file;
    }

    /**
	 *<p>get projsn</p>
	 * @return Returns the projsn.
	 */
    public String getProjsn() {
        return projsn;
    }

    /**
	 * <p>set projsn</p>
	 * @param projsn The projsn to set.
	 */
    public void setProjsn(String projsn) {
        this.projsn = projsn;
    }
}
