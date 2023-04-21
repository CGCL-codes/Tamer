package org.pixory.pxmodel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pixory.pxfoundation.SZQualifier;

/**
 */
public class PXEmailAddress extends PXPersistentObject {

    private static final Log LOG = LogFactory.getLog(PXEmailAddress.class);

    static final SZQualifier PRIMARY_QUALIFIER = new SZQualifier.KeyValue("isPrimary", SZQualifier.Equal, Boolean.TRUE);

    private String _address;

    private Boolean _isPrimary;

    private PXPerson _person;

    public PXEmailAddress() {
        this(null);
    }

    public PXEmailAddress(String address) {
        super(null);
        this.setAddress(address);
        this.setIsPrimary(Boolean.FALSE);
    }

    public String getAddress() {
        return _address;
    }

    public void setAddress(String address) {
        _address = address;
    }

    public Boolean getIsPrimary() {
        return _isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        _isPrimary = isPrimary;
    }

    public PXPerson getPerson() {
        return _person;
    }

    public void setPerson(PXPerson person) {
        _person = person;
    }
}
