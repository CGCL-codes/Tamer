package com.aimluck.eip.accessctl.bean;

import com.aimluck.commons.field.ALStringField;
import com.aimluck.eip.common.ALData;

/**
 *
 *
 */
public class AccessControlAclBean implements ALData {

    /** ACL ID */
    private ALStringField acl_id;

    /** ACL 名 */
    private ALStringField acl_name;

    /** 選択フラグ */
    private ALStringField checked;

    /**
   *
   */
    public void initField() {
        acl_id = new ALStringField();
        acl_name = new ALStringField();
        checked = new ALStringField();
    }

    /**
   * @return
   */
    public String getAclId() {
        return acl_id.getValue();
    }

    /**
   * @return
   */
    public String getAclName() {
        return acl_name.getValue();
    }

    /**
   * @return
   */
    public String getChecked() {
        return checked.getValue();
    }

    /**
   * @param i
   */
    public void setAclId(String string) {
        acl_id.setValue(string);
    }

    /**
   * @param string
   */
    public void setAclName(String string) {
        acl_name.setValue(string);
    }

    /**
   * @param string
   */
    public void setChecked(String string) {
        checked.setValue(string);
    }
}
