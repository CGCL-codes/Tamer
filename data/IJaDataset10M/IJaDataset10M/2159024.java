package org.egavas.tags;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import org.egavas.base.*;

public class InitContextTag extends ContextTag {

    public int doStartTag() throws JspException {
        appContext = getAppContext();
        try {
            appContext.init();
        } catch (Exception e) {
            throw new JspException(e.toString());
        }
        putAppContext();
        return SKIP_BODY;
    }
}
