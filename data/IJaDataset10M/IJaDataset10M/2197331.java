package com.gever.goa.dailyoffice.reportmgr.action;

import com.gever.struts.form.BaseForm;

/**
 *
 * <p>Title:Ŀ�����ActionForm </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: �������</p>
 * @author Hu.Walker
 * @version 1.0
 */
public class TargetForm extends BaseForm {

    private String username;

    private String deptname;

    private String cellcontent;

    private String cellname;

    private String searchYear;

    private String searchMonth;

    private String searchWeek;

    private String auditorname;

    private String auditorid;

    private String checkername;

    private String checkerid;

    private String auditFlag;

    private String checkFlag;

    private String editFlag;

    private String delFlag;

    private String unitname;

    private String creatorFlag;

    private String isReadOnlyTemplateFlag;

    private String reportTemplateName;

    private String modifyFlag;

    private String isCancel;

    private String targetTypeName;

    private String targetType;

    private String typeFlag;

    private String typeFlagName;

    private String saveExitFlag;

    public TargetForm() {
    }

    public String getAuditFlag() {
        return auditFlag;
    }

    public String getAuditorid() {
        return auditorid;
    }

    public String getAuditorname() {
        return auditorname;
    }

    public String getCellcontent() {
        return cellcontent;
    }

    public String getCellname() {
        return cellname;
    }

    public String getCheckerid() {
        return checkerid;
    }

    public String getCheckername() {
        return checkername;
    }

    public String getCheckFlag() {
        return checkFlag;
    }

    public String getCreatorFlag() {
        return creatorFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public String getDeptname() {
        return deptname;
    }

    public String getEditFlag() {
        return editFlag;
    }

    public String getIsCancel() {
        return isCancel;
    }

    public String getIsReadOnlyTemplateFlag() {
        return isReadOnlyTemplateFlag;
    }

    public String getModifyFlag() {
        return modifyFlag;
    }

    public String getReportTemplateName() {
        return reportTemplateName;
    }

    public String getSearchYear() {
        return searchYear;
    }

    public void setSearchYear(String searchYear) {
        this.searchYear = searchYear;
    }

    public void setReportTemplateName(String reportTemplateName) {
        this.reportTemplateName = reportTemplateName;
    }

    public String getUnitname() {
        return unitname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public void setModifyFlag(String modifyFlag) {
        this.modifyFlag = modifyFlag;
    }

    public void setIsReadOnlyTemplateFlag(String isReadOnlyTemplateFlag) {
        this.isReadOnlyTemplateFlag = isReadOnlyTemplateFlag;
    }

    public void setIsCancel(String isCancel) {
        this.isCancel = isCancel;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public void setCreatorFlag(String creatorFlag) {
        this.creatorFlag = creatorFlag;
    }

    public void setCheckFlag(String checkFlag) {
        this.checkFlag = checkFlag;
    }

    public void setCheckername(String checkername) {
        this.checkername = checkername;
    }

    public void setCheckerid(String checkerid) {
        this.checkerid = checkerid;
    }

    public void setCellname(String cellname) {
        this.cellname = cellname;
    }

    public void setCellcontent(String cellcontent) {
        this.cellcontent = cellcontent;
    }

    public void setEditFlag(String editFlag) {
        this.editFlag = editFlag;
    }

    public void setAuditorname(String auditorname) {
        this.auditorname = auditorname;
    }

    public void setAuditorid(String auditorid) {
        this.auditorid = auditorid;
    }

    public void setAuditFlag(String auditFlag) {
        this.auditFlag = auditFlag;
    }

    public String getTargetTypeName() {
        return targetTypeName;
    }

    public void setTargetTypeName(String targetType) {
        this.targetTypeName = targetType;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getSearchWeek() {
        return searchWeek;
    }

    public String getSearchMonth() {
        return searchMonth;
    }

    public void setSearchWeek(String searchWeek) {
        this.searchWeek = searchWeek;
    }

    public void setSearchMonth(String searchMonth) {
        this.searchMonth = searchMonth;
    }

    public String getTypeFlag() {
        return typeFlag;
    }

    public void setTypeFlag(String typeFlag) {
        this.typeFlag = typeFlag;
    }

    public String getSaveExitFlag() {
        return saveExitFlag;
    }

    public void setSaveExitFlag(String saveExitFlag) {
        this.saveExitFlag = saveExitFlag;
    }

    public String getTypeFlagName() {
        return typeFlagName;
    }

    public void setTypeFlagName(String typeFlagName) {
        this.typeFlagName = typeFlagName;
    }
}
