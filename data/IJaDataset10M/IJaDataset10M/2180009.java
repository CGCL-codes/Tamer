package org.com.cnc.common.android22.contact;

public class Phone {

    private String numberPhone;

    private String type;

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Phone(String numberPhone, String type) {
        super();
        this.numberPhone = numberPhone;
        this.type = type;
    }

    public String toString() {
        return numberPhone + "   " + type;
    }
}
