package com.zubarev.htmltable.vo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NoContent implements Serializable {

    protected String name;

    protected String nameKey;

    protected String styleClass;

    public void inherit(NoContent parent) {
        if (parent == null) {
            return;
        }
        if (name == null) {
            name = parent.name;
        }
        if (nameKey == null) {
            nameKey = parent.nameKey;
        }
        if (styleClass == null) {
            styleClass = parent.styleClass;
        }
    }

    /**
   * @return
   */
    public String getName() {
        return name;
    }

    /**
   * @return
   */
    public String getNameKey() {
        return nameKey;
    }

    /**
   * @return
   */
    public String getStyleClass() {
        return styleClass;
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
    public void setNameKey(String string) {
        nameKey = string;
    }

    /**
   * @param string
   */
    public void setStyleClass(String string) {
        styleClass = string;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
