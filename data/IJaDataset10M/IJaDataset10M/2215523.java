package com.submersion.jspshop.typenametag;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/** TEI Descriptor for CreateTag
 * 
 * @author Jeff Davey (jeffdavey@submersion.com)
 * @see com.submersion.jspshop.typenametag.CreateTag
 * @version $Revision: 1.1.1.1 $
 * @created: September 25, 2001  
 * @changed: $Date: 2001/10/03 05:14:01 $
 * @changedBy: $Author: jeffdavey $
*/
public class CreateTEI extends TagExtraInfo {

    public VariableInfo[] getVariableInfo(TagData data) {
        VariableInfo info = new VariableInfo(data.getId(), "com.submersion.jspshop.rae.TypeName", true, VariableInfo.AT_END);
        VariableInfo[] varInfo = { info };
        return varInfo;
    }
}
