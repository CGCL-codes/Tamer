package com.wpl.ui.factory.impl.components.xinfo.combobox;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ComboBoxInfo {

    private String mId;

    private final List<ComboBoxItemInfo> mItems = new ArrayList<ComboBoxItemInfo>();

    @XmlAttribute
    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    @XmlElement
    public List<ComboBoxItemInfo> getItem() {
        return mItems;
    }
}
