package com.aimluck.eip.schedule_ctl;

import com.aimluck.commons.field.ALNumberField;
import com.aimluck.commons.field.ALStringField;
import com.aimluck.eip.common.ALData;

/**
 * スケジュールアクセス権限の詳細ResultDataです。
 * 
 */
public class ScheduleAccessControlDetailResultData implements ALData {

    /** <code>to_id</code> アクセス側ID */
    private ALNumberField to_id;

    /** <code>to_id_type</code> アクセス側ID タイプ */
    private ALStringField to_id_type;

    /** <code>acl_type</code> アクセス権限 */
    private ALStringField acl_type;

    /** <code>to_id_name</code> アクセス側ID名 */
    private ALStringField to_id_name;

    /**
   *
   */
    @Override
    public void initField() {
        to_id = new ALNumberField();
        to_id_type = new ALStringField();
        acl_type = new ALStringField();
    }

    public String getToId() {
        return to_id.toString();
    }

    public void setToId(int number) {
        to_id.setValue(number);
    }

    public String getToIdType() {
        return to_id_type.toString();
    }

    public void setToIdType(String string) {
        to_id_type.setValue(string);
    }

    public String getAclType() {
        return acl_type.toString();
    }

    public void setAclType(String string) {
        acl_type.setValue(string);
    }

    public String getToIdName() {
        return to_id_name.toString();
    }

    public void setToIdName(String string) {
        to_id_name.setValue(string);
    }
}
