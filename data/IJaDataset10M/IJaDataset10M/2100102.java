package ch.skyguide.tools.requirement.hmi.openoffice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XIndexReplace;

@Deprecated
class TocProcessing {

    private static final Logger logger = Logger.getLogger(TocProcessing.class.getName());

    private static final String TOKEN_TYPE = "TokenType";

    private static final String TYPE_PAGE_NUMBER = "TokenPageNumber";

    private static final String TYPE_HYPERLINK_START = "TokenHyperlinkStart";

    private static final String TYPE_HYPERLINK_END = "TokenHyperlinkEnd";

    public static void addHyperlinks(XPropertySet _xPropertySet) throws OpenOfficeException {
        Object oLevelFormat;
        try {
            oLevelFormat = _xPropertySet.getPropertyValue("LevelFormat");
        } catch (Exception e) {
            throw OpenOfficeException.wrap(e);
        }
        XIndexReplace xIndexReplace = UnoHelper.queryInterface(XIndexReplace.class, oLevelFormat);
        for (int i = 0; i < xIndexReplace.getCount(); i++) {
            Object e;
            try {
                e = xIndexReplace.getByIndex(i);
            } catch (Exception _e) {
                throw OpenOfficeException.wrap("Failed to get element #" + i, _e);
            }
            logger.finer("### " + i + " ###");
            PropertyValue[][] pvss = (PropertyValue[][]) e;
            if (pvss.length == 0) {
                continue;
            }
            logger.finer("BEFORE:\n" + ToStringHelper.toString(pvss, ""));
            pvss = TocProcessing.addHyperlink(pvss);
            logger.finer("AFTER:\n" + ToStringHelper.toString(pvss, ""));
            try {
                xIndexReplace.replaceByIndex(i, pvss);
            } catch (Exception _e) {
                throw OpenOfficeException.wrap("Failed to replace element #" + i, _e);
            }
        }
    }

    private static PropertyValue[][] addHyperlink(PropertyValue[][] _pvss) {
        List<PropertyValue[]> tokenList = new ArrayList<PropertyValue[]>(Arrays.asList(_pvss));
        int index = findTokenWithType(tokenList, TYPE_PAGE_NUMBER);
        if (index == -1) {
            throw new IllegalArgumentException("No token with type " + TYPE_PAGE_NUMBER + " was found");
        }
        PropertyValue[] hlEnd = { UnoHelper.makePropertyValue(TOKEN_TYPE, TYPE_HYPERLINK_END) };
        tokenList.add(index + 1, hlEnd);
        PropertyValue[] hlStart = { UnoHelper.makePropertyValue(TOKEN_TYPE, TYPE_HYPERLINK_START) };
        tokenList.add(index, hlStart);
        return tokenList.toArray(new PropertyValue[tokenList.size()][]);
    }

    private static int findTokenWithType(List<PropertyValue[]> _tokenList, String _tokenType) {
        for (int i = 0; i < _tokenList.size(); i++) {
            PropertyValue[] pvs = _tokenList.get(i);
            String tokenType = (String) UnoHelper.getPropertyValue(pvs, TOKEN_TYPE);
            if (tokenType == null) {
                throw new RuntimeException("Property not found: " + TOKEN_TYPE);
            }
            if (_tokenType.equals(tokenType)) {
                return i;
            }
        }
        return -1;
    }
}
