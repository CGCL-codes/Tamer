package org.apache.ibatis.submitted.column_prefix;

public class AddressWithCaution extends Address {

    private String caution;

    public AddressWithCaution(Integer id, String state) {
        super(id, state);
    }

    public String getCaution() {
        return caution;
    }

    public void setCaution(String caution) {
        this.caution = caution;
    }
}
