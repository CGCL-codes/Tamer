package com.aimluck.eip.account;

import com.aimluck.commons.field.ALNumberField;
import com.aimluck.commons.field.ALStringField;
import com.aimluck.eip.common.ALData;

/**
 * 『役職』のResultDataです。 <br />
 */
public class AccountPositionResultData implements ALData {

    /** 役職ID */
    private ALNumberField position_id;

    /** 役職名 */
    private ALStringField position_name;

    /** 登録日 */
    private ALStringField create_date;

    /** 更新日 */
    private ALStringField update_date;

    /**
   * 
   * 
   */
    public void initField() {
        position_id = new ALNumberField();
        position_name = new ALStringField();
        create_date = new ALStringField();
        update_date = new ALStringField();
    }

    /**
   * @return
   */
    public ALStringField getCreateDate() {
        return create_date;
    }

    /**
   * @return
   */
    public ALNumberField getPositionId() {
        return position_id;
    }

    /**
   * @return
   */
    public ALStringField getPositionName() {
        return position_name;
    }

    /**
   * @return
   */
    public ALStringField getUpdateDate() {
        return update_date;
    }

    /**
   * @param string
   */
    public void setCreateDate(String string) {
        create_date.setValue(string);
    }

    /**
   * @param id
   */
    public void setPositionId(int id) {
        position_id.setValue(id);
    }

    /**
   * @param string
   */
    public void setPositionName(String string) {
        position_name.setValue(string);
    }

    /**
   * @param string
   */
    public void setUpdate_date(String string) {
        update_date.setValue(string);
    }
}
