package com.c2b2.ipoint.presentation.portlets.jsr168;

import javax.servlet.jsp.tagext.*;

/**
  * $Id: ParamTag.java,v 1.1 2005/12/26 21:10:18 steve Exp $
  * 
  * Copyright 2005 C2B2 Consulting Limited. All rights reserved.
  * 
  * This tag implements the required JSR 168 param tag
  * 
  * @author $Author: steve $
  * @version $Revision: 1.1 $
  * $Date: 2005/12/26 21:10:18 $
  * 
  */
public class ParamTag extends SimpleTagSupport {

    private URLTag URLTag_Var;

    private String name = "";

    private String value = "";

    public void doTag() {
        URLTag tag = getURLTag_Var();
        if (tag != null) {
            tag.addParameter(name, value);
        }
    }

    /**
   * get the parent tag of type com.c2b2.ipoint.presentation.portlets.jsr168.URLTag
   * @return parent tag of type com.c2b2.ipoint.presentation.portlets.jsr168.URLTag
   */
    public URLTag getURLTag_Var() {
        if (URLTag_Var == null) {
            URLTag_Var = (URLTag) findAncestorWithClass(this, com.c2b2.ipoint.presentation.portlets.jsr168.URLTag.class);
        }
        return URLTag_Var;
    }

    public void setName(String value) {
        if (value != null && value.length() > 0) {
            name = value;
        }
    }

    public String getName() {
        return name;
    }

    public void setValue(String value) {
        if (value == null) {
            value = "";
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
