package org.openamf.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openamf.config.FilterConfig;
import org.openamf.recordset.ASRecordSet;

/**
 * @author Jason Calabrese <jasonc@missionvi.com>
 * @version $Revision: 1.6 $, $Date: 2003/08/15 18:52:08 $
 */
public class MapListToRecordSet implements ResultFilter {

    private static Log log = LogFactory.getLog(MapListToRecordSet.class);

    public Object filter(Object value, FilterConfig config) throws FilterException {
        log.info("Translating MapListToRecordSet");
        ASRecordSet recordSet = null;
        List list = null;
        if (value instanceof List) {
            list = (List) value;
        } else if (value instanceof Iterator) {
            Iterator iterator = (Iterator) value;
            list = new ArrayList();
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
        }
        if (list != null) {
            recordSet = processList(list);
        } else {
            throw new FilterException("value is not an instance of List or Iterator: " + ((value != null) ? value.getClass().getName() : "null"));
        }
        return recordSet;
    }

    private ASRecordSet processList(List list) throws FilterException {
        ASRecordSet recordSet = new ASRecordSet();
        if (list.size() > 0) {
            String[] columnNames = getColumnNames(list);
            list = convertList(list);
            try {
                recordSet.populate(columnNames, list);
            } catch (Exception e) {
                throw new FilterException(e);
            }
        }
        return recordSet;
    }

    private String[] getColumnNames(List list) {
        List names = new ArrayList();
        Map map = (Map) list.get(0);
        for (Iterator i = map.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            names.add(entry.getKey());
        }
        String[] columnNames = new String[names.size()];
        int index = 0;
        for (Iterator i = names.iterator(); i.hasNext(); ) {
            String name = (String) i.next();
            columnNames[index] = name;
            index++;
        }
        return columnNames;
    }

    private List convertList(List list) {
        List rows = new ArrayList();
        for (Iterator i = list.iterator(); i.hasNext(); ) {
            Map map = (Map) i.next();
            rows.add(new ArrayList(map.values()));
        }
        return rows;
    }
}
