package com.c2b2.ipoint.presentation.taglib;

import com.c2b2.ipoint.presentation.PresentationException;
import com.c2b2.ipoint.processing.PortalRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;

public class RenderKeyWordsTag extends TagSupport {

    public RenderKeyWordsTag() {
    }

    public int doEndTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            PortalRequest portalRequest = PortalRequest.getCurrentRequest();
            HashSet keysSet = portalRequest.getKeyWords();
            Iterator words = keysSet.iterator();
            out.print("<meta name='keywords' content='");
            while (words.hasNext()) {
                String word = (String) words.next();
                out.print(word + ",");
            }
            out.println("'/>");
        } catch (IOException e) {
            throw new JspException("Unable to output information for tag RenderKeyWordsTag", e);
        }
        return EVAL_PAGE;
    }
}
