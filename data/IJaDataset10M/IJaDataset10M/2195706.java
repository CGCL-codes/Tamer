package jk.wrapper.filters;

import java.util.List;
import jk.wrapper.common.util.StringUtil;

public class CleanCharFilter extends AbstractNodeFilter {

    private String cleanStr;

    private String[] cleanStrings;

    public CleanCharFilter(Object obj) {
        cleanStr = obj.toString();
        if (!StringUtil.isStringNull(cleanStr)) {
            cleanStrings = cleanStr.split(";");
        }
    }

    public String filter(String obj) {
        String ret = obj;
        if (cleanStrings == null || cleanStrings.length == 0) return "";
        for (String cStr : cleanStrings) {
            ret = ret.replaceAll(cStr, "");
        }
        return ret;
    }

    public String filter(List<String> obj) {
        if (obj == null || obj.size() == 0) return "";
        return this.filter(obj.get(0));
    }
}
