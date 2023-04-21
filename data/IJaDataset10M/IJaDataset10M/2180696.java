package org.vqwiki.tags;

import org.apache.log4j.Logger;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;
import org.vqwiki.utils.JSPUtils;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

/**
 * Copyright 2006 - Martijn van der Kleijn.
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, version 2.1, dated February 1999.
 * <p/>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the latest version of the GNU Lesser General
 * Public License as published by the Free Software Foundation;
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program (gpl.txt); if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
public class UrlTag extends TagSupport {

    private static final Logger logger = Logger.getLogger(EncodeTag.class);

    private String action;

    private String topic;

    private String var;

    private String expandedValue;

    /**
     *
     */
    public int doEndTag() throws JspException {
        evaluateExpressions();
        if (var == null) {
            JspWriter out = pageContext.getOut();
            try {
                out.print(JSPUtils.encodeURL(expandedValue));
            } catch (IOException e) {
                logger.warn(e);
            }
        } else {
            pageContext.setAttribute(var, JSPUtils.encodeURL(expandedValue));
        }
        return EVAL_PAGE;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    private void evaluateExpressions() throws JspException {
        expandedValue = (String) ExpressionUtil.evalNotNull("url", "action", action, String.class, this, pageContext);
    }
}
