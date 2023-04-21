package org.campware.cream.modules.screens;

import java.util.List;
import org.apache.torque.util.Criteria;
import org.campware.cream.om.LanguagePeer;

/**
 * To read comments for this class, please see
 * the ancestor class
 */
public class LanguageList extends CreamLookupList {

    protected void initScreen() {
        setModuleType(LOOKUP);
        setModuleName("LANGUAGE");
        setIdName(LanguagePeer.LANGUAGE_ID);
        setDefOrderColumn(LanguagePeer.LANGUAGE_NAME);
    }

    protected String getSortColumn(int sortNo) {
        if (sortNo == 1) {
            return LanguagePeer.LANGUAGE_NAME;
        }
        return "";
    }

    protected List getEntries(Criteria criteria) {
        try {
            return LanguagePeer.doSelect(criteria);
        } catch (Exception e) {
            return null;
        }
    }
}
