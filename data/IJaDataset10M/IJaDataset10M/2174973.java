package com.submersion.jspshop.logintag;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/** TEI Descriptor for LoginTag
 * 
 * @author Jeff Davey (jeffdavey@submersion.com)
 * @see com.submersion.jspshop.logintag.LoginTag
 * @version $Revision: 1.1.1.1 $
 * @created: September 28, 2001  
 * @changed: $Date: 2001/10/03 05:13:59 $
 * @changedBy: $Author: jeffdavey $
*/
public class LoginTEI extends TagExtraInfo {

    public VariableInfo[] getVariableInfo(TagData data) {
        VariableInfo info = new VariableInfo("validLogin", "java.lang.Boolean", true, VariableInfo.AT_END);
        VariableInfo[] varInfo = { info };
        return varInfo;
    }
}
