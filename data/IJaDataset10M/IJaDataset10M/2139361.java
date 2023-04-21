package com.dcivision.setup.web;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.TextUtility;
import com.dcivision.framework.bean.AbstractBaseObject;
import com.dcivision.framework.bean.SysFunction;

/**
  MaintFunctionAccessForm.java

  This class is the for web form purpose.

  @author      Phoebe Wong
  @company     DCIVision Limited
  @creation date   07/08/2003
  @version     $Revision: 1.4 $
*/
public class MaintFunctionAccessForm extends AbstractActionPermissionForm {

    public static final String REVISION = "$Revision: 1.4 $";

    private String functionCode = null;

    private String functionName = null;

    private String functionNameZhHk = null;

    private String functionNameZhCn = null;

    private String parentID = null;

    private String linkUrl = null;

    private String displaySeq = null;

    private String menuType = null;

    private String permissionType = null;

    private String status = null;

    public MaintFunctionAccessForm() {
        super();
    }

    public String getFunctionCode() {
        return (this.functionCode);
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getFunctionName() {
        return (this.functionName);
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionNameZhHk() {
        return (this.functionNameZhHk);
    }

    public void setFunctionNameZhHk(String functionNameZhHk) {
        this.functionNameZhHk = functionNameZhHk;
    }

    public String getFunctionNameZhCn() {
        return (this.functionNameZhCn);
    }

    public void setFunctionNameZhCn(String functionNameZhCn) {
        this.functionNameZhCn = functionNameZhCn;
    }

    public String getParentID() {
        return (this.parentID);
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getLinkUrl() {
        return (this.linkUrl);
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getDisplaySeq() {
        return (this.displaySeq);
    }

    public void setDisplaySeq(String displaySeq) {
        this.displaySeq = displaySeq;
    }

    public String getMenuType() {
        return (this.menuType);
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getPermissionType() {
        return (this.permissionType);
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public String getStatus() {
        return (this.status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        this.setID(null);
        this.setFunctionCode(null);
        this.setFunctionName(null);
        this.setFunctionNameZhHk(null);
        this.setFunctionNameZhCn(null);
        this.setParentID(null);
        this.setLinkUrl(null);
        this.setDisplaySeq(null);
        this.setMenuType(null);
        this.setPermissionType(null);
        this.setStatus(null);
        this.setRecordStatus(null);
        this.setUpdateCount(null);
        this.setCreatorID(null);
        this.setCreateDate(null);
        this.setUpdaterID(null);
        this.setUpdateDate(null);
    }

    public AbstractBaseObject getFormData() throws ApplicationException {
        SysFunction tmpSysFunction = new SysFunction();
        tmpSysFunction.setID(TextUtility.parseIntegerObj(this.getID()));
        tmpSysFunction.setFunctionCode(this.getFunctionCode());
        tmpSysFunction.setFunctionName(this.getFunctionName());
        tmpSysFunction.setFunctionNameZhHk(this.getFunctionNameZhHk());
        tmpSysFunction.setFunctionNameZhCn(this.getFunctionNameZhCn());
        tmpSysFunction.setParentID(TextUtility.parseIntegerObj(this.getParentID()));
        tmpSysFunction.setLinkUrl(this.getLinkUrl());
        tmpSysFunction.setDisplaySeq(TextUtility.parseIntegerObj(this.getDisplaySeq()));
        tmpSysFunction.setMenuType(this.getMenuType());
        tmpSysFunction.setPermissionType(this.getPermissionType());
        tmpSysFunction.setStatus(this.getStatus());
        tmpSysFunction.setRecordStatus(this.getRecordStatus());
        tmpSysFunction.setUpdateCount(TextUtility.parseIntegerObj(this.getUpdateCount()));
        tmpSysFunction.setCreatorID(TextUtility.parseIntegerObj(this.getCreatorID()));
        tmpSysFunction.setCreateDate(parseTimestamp(this.getCreateDate()));
        tmpSysFunction.setUpdaterID(TextUtility.parseIntegerObj(this.getUpdaterID()));
        tmpSysFunction.setUpdateDate(parseTimestamp(this.getUpdateDate()));
        return tmpSysFunction;
    }

    public void setFormData(AbstractBaseObject baseObj) throws ApplicationException {
        SysFunction tmpSysFunction = (SysFunction) baseObj;
        this.setID(TextUtility.formatIntegerObj(tmpSysFunction.getID()));
        this.setFunctionCode(tmpSysFunction.getFunctionCode());
        this.setFunctionName(tmpSysFunction.getFunctionName());
        this.setFunctionNameZhHk(tmpSysFunction.getFunctionNameZhHk());
        this.setFunctionNameZhCn(tmpSysFunction.getFunctionNameZhCn());
        this.setParentID(TextUtility.formatIntegerObj(tmpSysFunction.getParentID()));
        this.setLinkUrl(tmpSysFunction.getLinkUrl());
        this.setDisplaySeq(TextUtility.formatIntegerObj(tmpSysFunction.getDisplaySeq()));
        this.setMenuType(tmpSysFunction.getMenuType());
        this.setPermissionType(tmpSysFunction.getPermissionType());
        this.setStatus(tmpSysFunction.getStatus());
        this.setRecordStatus(tmpSysFunction.getRecordStatus());
        this.setUpdateCount(TextUtility.formatIntegerObj(tmpSysFunction.getUpdateCount()));
        this.setCreatorID(TextUtility.formatIntegerObj(tmpSysFunction.getCreatorID()));
        this.setCreateDate(formatTimestamp(tmpSysFunction.getCreateDate()));
        this.setUpdaterID(TextUtility.formatIntegerObj(tmpSysFunction.getUpdaterID()));
        this.setUpdateDate(formatTimestamp(tmpSysFunction.getUpdateDate()));
    }
}
