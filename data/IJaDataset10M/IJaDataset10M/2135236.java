package com.nyandu.weboffice.common.tags;

import org.apache.jasper.runtime.JspRuntimeLibrary;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * 
 *  The contents of this file are subject to the Nandu Public License
 * Version 1.1 ("License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.nyandu.com
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 * 
 * The Initial Developer of the Original Code is User.
 * Portions created by User are Copyleft (C) www.nyandu.com. 
 * All Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * User: ern
 * Date: Jun 1, 2005
 * Time: 10:18:52 AM
 */
public class IncludeJSPTag extends TagSupport {

    private String jspName;

    public String getJspName() {
        return jspName;
    }

    public void setJspName(String jspName) {
        this.jspName = jspName;
    }

    public int doStartTag() throws JspException {
        try {
            pageContext.getOut().print("<h1>Antes del include" + jspName + "</h1>");
            JspRuntimeLibrary.include(pageContext.getRequest(), pageContext.getResponse(), jspName, pageContext.getOut(), true);
            pageContext.getOut().print("<h1>Despues del include</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }
}
