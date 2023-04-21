package com.aimluck.eip.common;

import com.aimluck.commons.field.ALNumberField;
import com.aimluck.commons.field.ALStringField;

/**
 * セッションへ格納する会社情報を表すクラスです。 <br />
 * 
 */
public class ALEipCompany implements ALData {

    /** 会社ID */
    private ALNumberField company_id;

    /** 会社名 */
    private ALStringField company_name;

    /**
   *
   */
    public void initField() {
        company_id = new ALNumberField();
        company_name = new ALStringField();
    }

    /**
   * @return
   */
    public ALNumberField getCompanyId() {
        return company_id;
    }

    /**
   * @return
   */
    public ALStringField getCompanyName() {
        return company_name;
    }

    /**
   * @param id
   */
    public void setCompanyId(int id) {
        company_id.setValue(id);
    }

    /**
   * @param string
   */
    public void setCompanyName(String string) {
        company_name.setValue(string);
    }
}
