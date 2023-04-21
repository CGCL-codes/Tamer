package com.wpl.ui.factory.impl.components.xinfo;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * 
 * @since 1.0
 */
public class ComponentInfo {

    private String mId;

    @XmlAttribute
    public String getId() {
        return mId;
    }

    public void setId(final String id) {
        mId = id;
    }
}
