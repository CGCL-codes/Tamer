package com.dcivision.dms.web;

public class ListDmsSelectArchiveFolderForm extends ListDmsDocumentForm {

    public static final String REVISION = "$Revision: 1.1 $";

    private String parentID = null;

    private String rootID = null;

    private String actionType = null;

    private String allowCompoundDoc = null;

    private String allowPaperDoc = null;

    private String allowEmailDoc = null;

    private String allowFormDoc = null;

    private String allowFlowDoc = null;

    public String getAllowCompoundDoc() {
        return allowCompoundDoc;
    }

    public void setAllowCompoundDoc(String allowCompoundDoc) {
        this.allowCompoundDoc = allowCompoundDoc;
    }

    public String getAllowEmailDoc() {
        return allowEmailDoc;
    }

    public void setAllowEmailDoc(String allowEmailDoc) {
        this.allowEmailDoc = allowEmailDoc;
    }

    public String getAllowFlowDoc() {
        return allowFlowDoc;
    }

    public void setAllowFlowDoc(String allowFlowDoc) {
        this.allowFlowDoc = allowFlowDoc;
    }

    public String getAllowFormDoc() {
        return allowFormDoc;
    }

    public void setAllowFormDoc(String allowFormDoc) {
        this.allowFormDoc = allowFormDoc;
    }

    public String getAllowPaperDoc() {
        return allowPaperDoc;
    }

    public void setAllowPaperDoc(String allowPaperDoc) {
        this.allowPaperDoc = allowPaperDoc;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getRootID() {
        return rootID;
    }

    public void setRootID(String rootID) {
        this.rootID = rootID;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
}
