package com.dcivision.framework.taglib.html;

import javax.servlet.jsp.JspException;

/**
  FileTag.java

  <p>This class is to to addin javascript checing during input file onchange. </p>
  <p>Purpose to Override struts taglib for tracing form modification.<p>
  <p>Modification in "struts-html.tld"<br>
  <pre>
     [FROM]
     &lt;tagclass&gt;org.apache.struts.taglib.html.FileTag&lt;/tagclass&gt;
     [TO]
     &lt;tagclass&gt;com.dcivision.framework.taglib.html.FileTag&lt;/tagclass&gt;

     [ADDED]
     No addition attribute is added
  </pre>
  </p>

    @author          Rollo Chan
    @company         DCIVision Limited
    @creation date   09/07/2003
    @version         $Revision: 1.7 $
*/
public class FileTag extends org.apache.struts.taglib.html.FileTag {

    public static final String REVISION = "$Revision: 1.7 $";

    protected boolean textOnly = false;

    public int doStartTag() throws JspException {
        switch(BaseHandler.commonRoutine(this, pageContext, this.getTextOnly())) {
            case BaseHandler.DIRECT_SKIP_BODY:
                return SKIP_BODY;
            case BaseHandler.DIRECT_EVAL_BODY_BUFFERED:
                return EVAL_BODY_BUFFERED;
            case BaseHandler.RETURN_SUPER_DOSTART:
                return super.doStartTag();
            default:
                return super.doStartTag();
        }
    }

    public void setTextOnly(boolean val) {
        textOnly = val;
    }

    public boolean getTextOnly() {
        return textOnly;
    }

    public void release() {
        super.release();
        textOnly = false;
    }
}
