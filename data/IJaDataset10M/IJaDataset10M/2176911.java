package com.ohioedge.j2ee.api.doc.ejb;

/**
 * @(#)MimeTypeEJB.java 1.350 01/12/03 Specifies MimeType EJB
 * @author Sandeep Dixit
 * @version 1.350, 01/12/03
 * @see org.j2eebuilder.view.ValueObjectFactory#getDataVO(java.lang.Object,
 *      java.lang.Class)
 * @since OEC1.2
 */
public class MimeTypeBean extends org.j2eebuilder.model.ejb.SignatureImpl {

    public MimeTypeBean() {
    }

    public Integer mimeTypeID;

    public String name;

    public String description;

    public Integer getMimeTypeID() {
        return mimeTypeID;
    }

    public void setMimeTypeID(Integer mimeTypeID) {
        this.mimeTypeID = mimeTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
