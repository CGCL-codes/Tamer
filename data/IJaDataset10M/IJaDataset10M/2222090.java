package com.submersion.jspshop.classpropertytag;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/** TEI Descriptor for CreateTag
 * 
 * @author Jeff Davey (jeffdavey@submersion.com)
 * @see com.submersion.jspshop.classpropertytag.CreateTag
 * @version $Revision: 1.1.1.1 $
 * @created: September 17, 2001  
 * @changed: $Date: 2001/10/03 05:14:01 $
 * @changedBy: $Author: jeffdavey $
*/
public class CreateTEI extends TagExtraInfo {

    public VariableInfo[] getVariableInfo(TagData data) {
        VariableInfo info = new VariableInfo(data.getId(), "com.submersion.jspshop.rae.ClassProperty", true, VariableInfo.AT_END);
        VariableInfo[] varInfo = { info };
        return varInfo;
    }
}
