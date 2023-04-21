package demo;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/**
 * Extra information class to support the scripting variable created by the
 * ListMessagesTag class. The scope of the variable is limited to the body 
 * of the tag.
 */
public class ListMessagesTEI extends TagExtraInfo {

    public ListMessagesTEI() {
        super();
    }

    public VariableInfo[] getVariableInfo(TagData data) {
        VariableInfo info = new VariableInfo(data.getId(), "MessageInfo", true, VariableInfo.NESTED);
        VariableInfo[] varInfo = { info };
        return varInfo;
    }
}
