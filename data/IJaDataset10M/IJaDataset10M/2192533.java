package org.dcm4chee.xero.wado.multi;

import static org.dcm4chee.xero.wado.WadoParams.OBJECT_UID;
import java.util.Iterator;
import java.util.Map;
import org.dcm4chee.xero.metadata.filter.Filter;
import org.dcm4chee.xero.metadata.filter.FilterItem;
import org.dcm4chee.xero.metadata.servlet.ServletResponseItem;
import org.dcm4chee.xero.util.FilterCombineIterator;
import org.dcm4chee.xero.util.StringUtil;

/**
 * The ObjectUidItFilter creates an iterator over all the specified UID's For
 * each UID, it calls the next filter, setting objectUID to the single, current
 * objectUID. It will restore objectUID when the calls are all done.
 * 
 * @author bwallace
 * 
 */
public class ObjectUidItFilter implements Filter<Iterator<ServletResponseItem>> {

    /** Split out the object UID if necessary */
    public Iterator<ServletResponseItem> filter(FilterItem<Iterator<ServletResponseItem>> filterItem, Map<String, Object> params) {
        Object objectUID = params.get(OBJECT_UID);
        if (objectUID == null) return null;
        String[] arrUID;
        if (objectUID instanceof String) {
            String sobjectUID = (String) objectUID;
            if (sobjectUID.indexOf('\\') == -1) return filterItem.callNextFilter(params);
            arrUID = StringUtil.split(sobjectUID, '\\', true);
        } else arrUID = (String[]) objectUID;
        Iterator<ServletResponseItem> it = new ObjectUidIterator(arrUID, filterItem, params);
        return it;
    }

    /** Iterate over the string UID's specified in the original parameters. */
    class ObjectUidIterator extends FilterCombineIterator<String, ServletResponseItem> {

        String origObjectUID;

        public ObjectUidIterator(String[] arrUid, FilterItem<Iterator<ServletResponseItem>> filterItem, Map<String, Object> params) {
            super(arrUid, filterItem, params);
            origObjectUID = (String) params.get(OBJECT_UID);
        }

        /** Sets the object UID to the current object UID */
        @Override
        protected void updateParams(String item, Map<String, Object> params) {
            params.put(OBJECT_UID, item);
        }

        /** Restore the original UID */
        @Override
        protected void restoreParams(String item, Map<String, Object> params) {
            params.put(OBJECT_UID, origObjectUID);
        }
    }
}
