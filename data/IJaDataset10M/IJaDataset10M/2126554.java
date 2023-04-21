package net.lshift.jsp.taglib.yatl;

import javax.servlet.jsp.tagext.BodyTagSupport;

/**
*
* @author Sam Jones (sam@lshift.net)
*/
public class BodyTagSupportBase extends BodyTagSupport {

    protected String subst(String name) {
        return (null != name) ? YATLUtil.substituteName(name, this, pageContext) : null;
    }
}
