package com.submersion.jspshop.typetag;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/** TEI Descriptor for IterateTag
 * 
 * @author Jeff Davey (jeffdavey@submersion.com)
 * @see com.submersion.jspshop.typetag.IterateTag
 * @version $Revision: 1.1.1.1 $
 * @created: September 24, 2001  
 * @changed: $Date: 2001/10/03 05:13:54 $
 * @changedBy: $Author: jeffdavey $
*/
public class IterateTEI extends TagExtraInfo {

    public VariableInfo[] getVariableInfo(TagData data) {
        VariableInfo info = new VariableInfo(data.getId(), "com.submersion.jspshop.rae.Type", true, VariableInfo.NESTED);
        VariableInfo info2 = new VariableInfo(data.getId() + "HasNext", "java.lang.Boolean", true, VariableInfo.NESTED);
        VariableInfo[] varInfo = { info, info2 };
        return varInfo;
    }
}
