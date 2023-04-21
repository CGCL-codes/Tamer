package com.dcivision.form.bean;

import java.io.InputStream;
import com.dcivision.framework.bean.AbstractBaseObject;

/**
  FormFileAttachment.java

  This class is the serializable bean reflecting business logic uses.

    @author           Vera Wang
    @company          DCIVision Limited
    @creation date    28/04/2005
    @version          $Revision: 1.3 $
*/
public class FormFileAttachment extends AbstractBaseObject {

    public static final String REVISION = "$Revision: 1.3 $";

    private Integer formDataElementID = null;

    private String attachmentName = null;

    private String attachmentType = null;

    private String externalID = null;

    private Integer contentSize = null;

    private String flowType = null;

    private InputStream dataStream = null;

    public FormFileAttachment() {
        super();
    }

    public Integer getFormDataElementID() {
        return (this.formDataElementID);
    }

    public void setFormDataElementID(Integer formDataElementID) {
        this.formDataElementID = formDataElementID;
    }

    public String getAttachmentName() {
        return (this.attachmentName);
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentType() {
        return (this.attachmentType);
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getExternalID() {
        return (this.externalID);
    }

    public void setExternalID(String externalID) {
        this.externalID = externalID;
    }

    public Integer getContentSize() {
        return (this.contentSize);
    }

    public void setContentSize(Integer contentSize) {
        this.contentSize = contentSize;
    }

    public String getFlowType() {
        return (this.flowType);
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public InputStream getDataStream() {
        return (this.dataStream);
    }

    public void setDataStream(InputStream dataStream) {
        this.dataStream = dataStream;
    }
}
