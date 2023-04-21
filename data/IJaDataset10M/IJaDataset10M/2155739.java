package com.submersion.jspshop.typetag;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/** TEI Descriptor for CreateTag
 * 
 * @author Jeff Davey (jeffdavey@submersion.com)
 * @see com.submersion.jspshop.typetag.CreateTag
 * @version $Revision: 1.1.1.1 $
 * @created: September 26, 2001  
 * @changed: $Date: 2001/10/03 05:13:54 $
 * @changedBy: $Author: jeffdavey $
*/
public class CreateTEI extends TagExtraInfo {

    public VariableInfo[] getVariableInfo(TagData data) {
        VariableInfo info = new VariableInfo(data.getId(), "com.submersion.jspshop.rae.Type", true, VariableInfo.AT_END);
        VariableInfo[] varInfo = { info };
        return varInfo;
    }
}
