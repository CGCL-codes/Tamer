package net.pleso.demo.client.dal.bank;

import net.pleso.framework.client.dal.db.types.DBInteger;
import net.pleso.framework.client.dal.db.types.DBString;
import com.google.gwt.user.client.rpc.IsSerializable;

public class BankInfo implements IsSerializable {

    public BankInfo() {
    }

    private Integer bank_id = DBInteger.nullValue;

    private String bank_name = DBString.nullValue;

    private Integer bank_mfo = DBInteger.nullValue;

    public Integer getBank_id() {
        return bank_id;
    }

    public void setBank_id(Integer bank_id) {
        this.bank_id = bank_id;
    }

    public Integer getBank_mfo() {
        return bank_mfo;
    }

    public void setBank_mfo(Integer bank_mfo) {
        this.bank_mfo = bank_mfo;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }
}
