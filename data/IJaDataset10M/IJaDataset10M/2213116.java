package com.aimluck.eip.blog;

import com.aimluck.commons.field.ALNumberField;
import com.aimluck.commons.field.ALStringField;
import com.aimluck.eip.common.ALData;

/**
 * ブログエントリーのあしあとのResultDataです。 <BR>
 * 
 */
public class BlogFootmarkResultData implements ALData {

    /** Owner ID */
    private ALNumberField user_id;

    /** Owner 名 */
    private ALStringField user_name;

    /** 更新日 */
    private ALStringField update_date;

    /**
   * 
   * 
   */
    public void initField() {
        user_id = new ALNumberField();
        user_name = new ALStringField();
        update_date = new ALStringField();
    }

    /**
   * @return
   */
    public ALNumberField getUserId() {
        return user_id;
    }

    /**
   * @return
   */
    public ALStringField getUserName() {
        return user_name;
    }

    /**
   * @param i
   */
    public void setUserId(long i) {
        user_id.setValue(i);
    }

    /**
   * @param string
   */
    public void setUserName(String string) {
        user_name.setValue(string);
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
    public void setUpdateDate(String string) {
        update_date.setValue(string);
    }
}
