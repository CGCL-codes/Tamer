package com.dcivision.framework.taglib.html;

import javax.servlet.jsp.JspException;

/**
  TextareaTag.java

  <p>This class is to extend the TextareaTag provided by Apache Struts.<p>
  <p>Modification in "struts-html.tld"<br>
  <pre>
     [FROM]
     &lt;tagclass&gt;org.apache.struts.taglib.html.TextareaTag&lt;/tagclass&gt;
     [TO]
     &lt;tagclass&gt;com.dcivision.framework.taglib.html.TextareaTag&lt;/tagclass&gt;

     [ADDED]
     &lt;attribute&gt;
     &lt;name&gt;textlimit&lt;/name&gt;
     &lt;required&gt;false&lt;/required&gt;
     &lt;rtexprvalue&gt;true&lt;/rtexprvalue&gt;
     &lt;/attribute&gt;
  </pre>
  </p>

  <p>USAGE:</p>
  <p>The following example shows that the textarea are limited to input 500 letters ONLY.</p>
  <p><code>&lt;html:textarea cols='30' rows='5' <strong>textlimit='500'</strong>/&gt;</code></p>

    @author          Scott Tong
    @company         DCIVision Limited
    @creation date   08/07/2003
    @version         $Revision: 1.11.30.1 $
*/
public class TextareaTag extends org.apache.struts.taglib.html.TextareaTag {

    protected String textlimit = null;

    protected boolean textOnly = false;

    public String getTextlimit() {
        return (this.textlimit);
    }

    public void setTextlimit(String textlimit) {
        this.textlimit = textlimit;
    }

    public void setTextOnly(boolean val) {
        textOnly = val;
    }

    public boolean getTextOnly() {
        return textOnly;
    }

    public int doStartTag() throws JspException {
        if (this.textlimit != null) {
            String originalOnkeydown = super.getOnkeydown();
            originalOnkeydown = (originalOnkeydown == null) ? "" : originalOnkeydown;
            super.setOnkeydown("CheckTextArea(this, " + this.textlimit + ");" + originalOnkeydown);
            String originalOnkeyup = super.getOnkeyup();
            originalOnkeyup = (originalOnkeyup == null) ? "" : originalOnkeyup;
            super.setOnkeyup("CheckTextArea(this, " + this.textlimit + ");" + originalOnkeyup);
        }
        switch(BaseHandler.commonRoutine(this, pageContext, this.getTextOnly())) {
            case BaseHandler.DIRECT_SKIP_BODY:
                this.release();
                return SKIP_BODY;
            case BaseHandler.DIRECT_EVAL_BODY_BUFFERED:
                return EVAL_BODY_BUFFERED;
            case BaseHandler.RETURN_SUPER_DOSTART:
                return super.doStartTag();
            default:
                return super.doStartTag();
        }
    }

    public int doEndTag() throws JspException {
        int result = super.doEndTag();
        this.release();
        return (result);
    }

    public void release() {
        super.release();
        textlimit = null;
        textOnly = false;
        super.setOnkeyup(null);
        super.setOnkeydown(null);
        super.setRows(null);
        super.setCols(null);
    }
}
