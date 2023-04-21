package com.aimluck.eip.addressbook;

import com.aimluck.commons.field.ALNumberField;
import com.aimluck.commons.field.ALStringField;
import com.aimluck.eip.common.ALData;

/**
 * アドレス帳グループのリザルトデータです。
 */
public class AddressBookGroupResultData implements ALData {

    /** グループID */
    private ALNumberField group_id;

    /** グループ名 */
    private ALStringField group_name;

    /** オーナID */
    private ALNumberField owner_id;

    /** 公開フラグ */
    private ALStringField public_flag;

    /** 登録日 */
    private ALStringField create_date;

    /** 更新日 */
    private ALStringField update_date;

    /**
   *
   */
    public void initField() {
        group_id = new ALNumberField();
        group_name = new ALStringField();
        owner_id = new ALNumberField();
        public_flag = new ALStringField();
        create_date = new ALStringField();
        update_date = new ALStringField();
    }

    /**
   * @param i
   */
    public void setGroupId(long i) {
        group_id.setValue(i);
    }

    /**
   * @return
   */
    public ALNumberField getGroupId() {
        return group_id;
    }

    /**
   * @param string
   */
    public void setGroupName(String string) {
        group_name.setValue(string);
    }

    /**
   * @return
   */
    public ALStringField getGroupName() {
        return group_name;
    }

    public void setOwnerId(long i) {
        owner_id.setValue(i);
    }

    public ALNumberField getOwnerId() {
        return owner_id;
    }

    /**
   * @param string
   */
    public void setPublicFlag(String string) {
        public_flag.setValue(string);
    }

    /**
   * @return
   */
    public ALStringField getPublicFlag() {
        return public_flag;
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
   * @param string
   */
    public void setUpdateDate(String string) {
        update_date.setValue(string);
    }
}
