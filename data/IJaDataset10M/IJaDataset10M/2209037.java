package com.butnet.myframe.template.impl;

import com.butnet.myframe.template.TemplateNode;
import com.butnet.myframe.template.TemplateNodeList;
import java.util.LinkedList;

/**
 *
 * @author Administrator
 */
public class TemplateNodeListImpl extends LinkedList<TemplateNode> implements TemplateNodeList {

    private static final long serialVersionUID = 1L;

    @Override
    public int getLength() {
        return this.size();
    }

    @Override
    public TemplateNode item(int index) {
        if (index < size()) return this.get(index);
        return null;
    }
}
