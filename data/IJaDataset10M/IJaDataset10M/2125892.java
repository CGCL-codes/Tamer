package com.dcivision.dms.web;

/**
  MaintDmsFullTextViewerForm.java

  This class is the for Dms Full Text Viewer.

  @author      jerry zhou
  @company     DCIVision Limited
  @creation date   12/29/2004
  @version     $Revision: 1.2.2.1 $
*/
public class MaintDmsIndexViewerForm extends MaintDmsDocumentForm {

    public static final String REVISION = "$Revision: 1.2.2.1 $";

    private String opMode = null;

    private String documentID = null;

    private String content = null;

    private int histLength = 0;

    private String versionParentID = null;

    private String versionID = null;

    private String versionNumber = null;

    private String versionLabel = null;

    private String udfID = null;

    private String udf_1 = null;

    private String udf_2 = null;

    private String udf_3 = null;

    private String udf_4 = null;

    private String udf_5 = null;

    private String udf_6 = null;

    private String udf_7 = null;

    private String udf_8 = null;

    private String udf_9 = null;

    private String udf_10 = null;

    private String dciUniqueID = null;

    private String updater = null;

    private String creator = null;

    public void setOpMode(String opMode) {
        this.opMode = opMode;
    }

    public String getOpMode() {
        return (this.opMode);
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getDocumentID() {
        return (this.documentID);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return (this.content);
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    public String getVersionID() {
        return (this.versionID);
    }

    public void setVersionParentID(String versionParentID) {
        this.versionParentID = versionParentID;
    }

    public String getVersionParentID() {
        return (this.versionParentID);
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getVersionNumber() {
        return (this.versionNumber);
    }

    public void setVersionLabel(String versionLabel) {
        this.versionLabel = versionLabel;
    }

    public String getVersionLabel() {
        return (this.versionLabel);
    }

    public void setUdfID(String udfID) {
        this.udfID = udfID;
    }

    public String getUdfID() {
        return (this.udfID);
    }

    public void setUdf_1(String udf_1) {
        this.udf_1 = udf_1;
    }

    public String getUdf_1() {
        return (this.udf_1);
    }

    public void setUdf_2(String udf_2) {
        this.udf_2 = udf_2;
    }

    public String getUdf_2() {
        return (this.udf_2);
    }

    public void setUdf_3(String udf_3) {
        this.udf_3 = udf_3;
    }

    public String getUdf_3() {
        return (this.udf_3);
    }

    public void setUdf_4(String udf_4) {
        this.udf_4 = udf_4;
    }

    public String getUdf_4() {
        return (this.udf_4);
    }

    public void setUdf_5(String udf_5) {
        this.udf_5 = udf_5;
    }

    public String getUdf_5() {
        return (this.udf_5);
    }

    public void setUdf_6(String udf_6) {
        this.udf_6 = udf_6;
    }

    public String getUdf_6() {
        return (this.udf_6);
    }

    public void setUdf_7(String udf_7) {
        this.udf_7 = udf_7;
    }

    public String getUdf_7() {
        return (this.udf_7);
    }

    public void setUdf_8(String udf_8) {
        this.udf_8 = udf_8;
    }

    public String getUdf_8() {
        return (this.udf_8);
    }

    public void setUdf_9(String udf_9) {
        this.udf_9 = udf_9;
    }

    public String getUdf_9() {
        return (this.udf_9);
    }

    public void setUdf_10(String udf_10) {
        this.udf_10 = udf_10;
    }

    public String getUdf_10() {
        return (this.udf_10);
    }

    public void setDciUniqueID(String dciUniqueID) {
        this.dciUniqueID = dciUniqueID;
    }

    public String getDciUniqueID() {
        return (this.dciUniqueID);
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getUpdater() {
        return (this.updater);
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return (this.creator);
    }

    public void setHistLength(int histLength) {
        this.histLength = histLength;
    }

    public int getHistLength() {
        return (this.histLength);
    }
}
