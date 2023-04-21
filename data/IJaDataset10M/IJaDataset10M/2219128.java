package com.javahispano.jfaqmatic.template;

import freemarker.template.*;
import java.util.ArrayList;

/**
 *  Description of the Class
 *
 *@author     Alberto Molpeceres
 *@created    13 January 2002
 *@version    0.1
 *@since      0.1
 */
public class TemplateList implements TemplateListModel {

    int index = 0;

    private ArrayList list;

    /**
   *  Constructor for the TemplateList object
   */
    public TemplateList() {
        list = new ArrayList();
    }

    /**
   *  Constructor for the get object
   *
   *@param  i  Description of Parameter
   *@return    Description of the Returned Value
   */
    public TemplateModel get(int i) {
        if (i < list.size()) {
            return (TemplateModel) list.get(i);
        }
        System.out.println("[TemplateList.get] returning null");
        return null;
    }

    /**
   *  Gets the empty attribute of the TemplateList object
   *
   *@return    The empty value
   */
    public boolean isEmpty() {
        return list.size() > 0;
    }

    /**
   *  Gets the rewound attribute of the TemplateList object
   *
   *@return    The rewound value
   */
    public boolean isRewound() {
        return index == 0;
    }

    /**
   *  Description of the Method
   */
    public void rewind() {
        index = 0;
    }

    /**
   *  Description of the Method
   *
   *@return    Description of the Returned Value
   */
    public TemplateModel next() {
        if (hasNext()) {
            return this.get(index++);
        }
        System.out.println("[TemplateList.next] returning null");
        return null;
    }

    /**
   *  Description of the Method
   *
   *@return    Description of the Returned Value
   */
    public boolean hasNext() {
        return (index < list.size());
    }

    /**
   *  Description of the Method
   *
   *@param  data  Description of Parameter
   */
    public void add(TemplateModel data) {
        list.add(data);
    }
}
