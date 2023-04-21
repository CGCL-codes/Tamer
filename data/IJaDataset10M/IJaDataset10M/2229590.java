package ces.platform.infoplat.taglib.ds.other;

import javax.servlet.jsp.tagext.*;

public class CounterTagExtraInfo extends TagExtraInfo {

    public CounterTagExtraInfo() {
    }

    /**
	 * �õ�������Ϣ
	 */
    public VariableInfo[] getVariableInfo(TagData tagdata) {
        String s = tagdata.getAttributeString("id");
        if (s == null) {
            return null;
        } else {
            VariableInfo variableinfo = new VariableInfo(s, "java.lang.String", true, VariableInfo.AT_END);
            VariableInfo avariableinfo[] = { variableinfo };
            return avariableinfo;
        }
    }
}
