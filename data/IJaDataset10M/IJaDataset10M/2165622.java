package com.liferay.portal.model;

import java.io.Serializable;

/**
 * <a href="LayoutTemplateColumn.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Ivica Cardic
 * @version $Revision: 1.0 $
 *
 */
public class LayoutTemplateColumn implements Serializable {

    public LayoutTemplateColumn(String id) {
        _id = id;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    private String _id;
}
