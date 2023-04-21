package com.ixora.common.ui.filter;

import com.ixora.common.xml.XMLExternalizable;

/**
 * @author Daniel Moraru
 */
public interface Filter extends XMLExternalizable {

    public static final String XML_NODE = "filter";

    /**
	 * @param obj
	 * @return
	 */
    boolean accept(Object obj);
}
