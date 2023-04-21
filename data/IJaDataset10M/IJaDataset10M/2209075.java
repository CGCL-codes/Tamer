package com.zubarev.htmltable.vo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BeanDescription implements Serializable {

    protected String name;

    protected String value;

    /**
   * @return
   */
    public String getName() {
        return name;
    }

    /**
   * @return
   */
    public String getValue() {
        return value;
    }

    /**
   * @param string
   */
    public void setName(String string) {
        name = string;
    }

    /**
   * @param string
   */
    public void setValue(String string) {
        value = string;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this) + "\n";
    }
}
