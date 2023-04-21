package org.nakedobjects.application.office;

import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.applib.annotation.Optional;

public class Phone extends PimObject implements Address {

    private String type;

    private String number;

    private String extension;

    protected String explicitToString() {
        return number;
    }

    @Optional
    @MemberOrder(sequence = "110")
    public String getExtension() {
        return extension;
    }

    @MemberOrder(sequence = "100")
    public String getNumber() {
        return number;
    }

    @Optional
    @MemberOrder(sequence = "120")
    public String getType() {
        return type;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setType(String type) {
        this.type = type;
        objectChanged();
    }
}
