package com.dcivision.dms.web;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import com.dcivision.dms.bean.DmsRoot;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.TextUtility;
import com.dcivision.framework.bean.AbstractBaseObject;

/**
  MaintDmsRootForm.java

  This class is the for web form purpose.

  @author          Rollo Chan
  @company         DCIVision Limited
  @creation date   13/08/2003
  @version         $Revision: 1.10 $
*/
public class MaintDmsRootForm extends com.dcivision.setup.web.AbstractActionPermissionForm {

    public static final String REVISION = "$Revision: 1.10 $";

    private String rootName = null;

    private String rootType = null;

    private String locID = null;

    private String ownerID = null;

    private String storageLimit = null;

    private String rootDesc = null;

    private String rootFolderID = null;

    private String copyMode = null;

    private String unlimit = null;

    private String locPath = null;

    private String fromWhom = null;

    private boolean ignoreDeleteShortcut = false;

    public MaintDmsRootForm() {
        super();
    }

    public String getRootName() {
        return (this.rootName);
    }

    public void setRootName(String rootName) {
        this.rootName = rootName;
    }

    public String getRootType() {
        return (this.rootType);
    }

    public void setRootType(String rootType) {
        this.rootType = rootType;
    }

    public String getLocID() {
        return (this.locID);
    }

    public void setLocID(String locID) {
        this.locID = locID;
    }

    public String getStorageLimit() {
        return (this.storageLimit);
    }

    public void setStorageLimit(String storageLimit) {
        this.storageLimit = storageLimit;
    }

    public String getOwnerID() {
        return (this.ownerID);
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getRootDesc() {
        return (this.rootDesc);
    }

    public void setRootDesc(String rootDesc) {
        this.rootDesc = rootDesc;
    }

    public String getRootFolderID() {
        return (this.rootFolderID);
    }

    public void setRootFolderID(String rootFolderID) {
        this.rootFolderID = rootFolderID;
    }

    public String getCopyMode() {
        return (this.copyMode);
    }

    public void setCopyMode(String copyMode) {
        this.copyMode = copyMode;
    }

    public String getUnlimit() {
        return (this.unlimit);
    }

    public void setLocPath(String locPath) {
        this.locPath = locPath;
    }

    public String getLocPath() {
        return (this.locPath);
    }

    public void setUnlimit(String unlimit) {
        this.unlimit = unlimit;
    }

    public String getFromWhom() {
        return (this.fromWhom);
    }

    public void setFromWhom(String fromWhom) {
        this.fromWhom = fromWhom;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        this.setID(null);
        this.setRootName(null);
        this.setRootDesc(null);
        this.setRootFolderID(null);
        this.setRootType(null);
        this.setLocID(null);
        this.setStorageLimit(null);
        this.setUnlimit(null);
        this.setOwnerID(null);
        this.setRecordStatus(null);
        this.setUpdateCount(null);
        this.setCreatorID(null);
        this.setCreateDate(null);
        this.setUpdaterID(null);
        this.setUpdateDate(null);
    }

    public AbstractBaseObject getFormData() throws ApplicationException {
        DmsRoot tmpDmsRoot = new DmsRoot();
        tmpDmsRoot.setID(TextUtility.parseIntegerObj(this.getID()));
        tmpDmsRoot.setRootName(this.getRootName());
        tmpDmsRoot.setRootType(this.getRootType());
        tmpDmsRoot.setRootDesc(this.getRootDesc());
        tmpDmsRoot.setRootFolderID(TextUtility.parseIntegerObj(this.getRootFolderID()));
        tmpDmsRoot.setLocID(TextUtility.parseIntegerObj(this.getLocID()));
        tmpDmsRoot.setStorageLimit(TextUtility.parseDoubleObj(this.getStorageLimit()));
        tmpDmsRoot.setUnlimit(this.getUnlimit());
        tmpDmsRoot.setOwnerID(TextUtility.parseIntegerObj(this.getOwnerID()));
        tmpDmsRoot.setRecordStatus(this.getRecordStatus());
        tmpDmsRoot.setUpdateCount(TextUtility.parseIntegerObj(this.getUpdateCount()));
        tmpDmsRoot.setCreatorID(TextUtility.parseIntegerObj(this.getCreatorID()));
        tmpDmsRoot.setCreateDate(parseTimestamp(this.getCreateDate()));
        tmpDmsRoot.setUpdaterID(TextUtility.parseIntegerObj(this.getUpdaterID()));
        tmpDmsRoot.setUpdateDate(parseTimestamp(this.getUpdateDate()));
        return tmpDmsRoot;
    }

    public void setFormData(AbstractBaseObject baseObj) throws ApplicationException {
        DmsRoot tmpDmsRoot = (DmsRoot) baseObj;
        this.setID(TextUtility.formatIntegerObj(tmpDmsRoot.getID()));
        this.setRootName(tmpDmsRoot.getRootName());
        this.setRootType(tmpDmsRoot.getRootType());
        this.setRootDesc(tmpDmsRoot.getRootDesc());
        this.setRootFolderID(TextUtility.formatIntegerObj(tmpDmsRoot.getRootFolderID()));
        this.setLocID(TextUtility.formatIntegerObj(tmpDmsRoot.getLocID()));
        this.setStorageLimit(TextUtility.formatDoubleObj(tmpDmsRoot.getStorageLimit()));
        this.setUnlimit(tmpDmsRoot.getUnlimit());
        this.setOwnerID(TextUtility.formatIntegerObj(tmpDmsRoot.getOwnerID()));
        this.setRecordStatus(tmpDmsRoot.getRecordStatus());
        this.setUpdateCount(TextUtility.formatIntegerObj(tmpDmsRoot.getUpdateCount()));
        this.setCreatorID(TextUtility.formatIntegerObj(tmpDmsRoot.getCreatorID()));
        this.setCreateDate(formatTimestamp(tmpDmsRoot.getCreateDate()));
        this.setUpdaterID(TextUtility.formatIntegerObj(tmpDmsRoot.getUpdaterID()));
        this.setUpdateDate(formatTimestamp(tmpDmsRoot.getUpdateDate()));
    }

    public boolean isIgnoreDeleteShortcut() {
        return ignoreDeleteShortcut;
    }

    public void setIgnoreDeleteShortcut(boolean ignoreDeleteShortcut) {
        this.ignoreDeleteShortcut = ignoreDeleteShortcut;
    }
}
