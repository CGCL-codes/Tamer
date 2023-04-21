package cn.vlabs.duckling.vwb.services.dlog.rdi.client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import cn.vlabs.dlog.barrels.BufferedEventList;
import cn.vlabs.dlog.client.Event;
import cn.vlabs.dlog.client.EventField;
import cn.vlabs.dlog.client.EventQuery;
import cn.vlabs.duckling.vwb.services.dlog.impl.DlogServiceImpl;
import cn.vlabs.duckling.vwb.services.dlog.rdi.data.EventCategoryEntry;
import cn.vlabs.duckling.vwb.services.dlog.rdi.data.PageRecord;
import cn.vlabs.duckling.vwb.services.dlog.rdi.data.QueryRecord;
import cn.vlabs.rest.ServiceException;

public class QueryGuest extends QueryMethod {

    private DlogServiceImpl m_dlogger;

    public QueryGuest(DlogServiceImpl m_dlogger) {
        this.m_dlogger = m_dlogger;
    }

    @Override
    public ArrayList<QueryRecord> query(String target, String object, Date fromTime, Date endTime) {
        if (target != null) {
            ArrayList<QueryRecord> result = null;
            ArrayList<EventQuery> queries = null;
            if (object != null) {
                queries = getEventQuery(target, object, fromTime, endTime);
                BufferedEventList list = m_dlogger.query(queries.toArray(new EventQuery[0]));
                result = handleResult(object, list);
            }
            return result;
        }
        return null;
    }

    private ArrayList<QueryRecord> handleResult(String object, BufferedEventList list) {
        ArrayList<QueryRecord> result = new ArrayList<QueryRecord>();
        HashMap<String, String> guests = new HashMap<String, String>();
        HashMap<String, ArrayList<Object>> access = new HashMap<String, ArrayList<Object>>();
        if (list != null) {
            try {
                if (!object.equals(Integer.toString(EventCategoryEntry.PAGE_EDIT))) {
                    while (list.hasNext()) {
                        Event event = list.next();
                        if (guests.get(event.getTarget()) != null) {
                            guests.put(event.getTarget(), Integer.toString(Integer.parseInt(guests.get(event.getTarget())) + 1));
                            ArrayList<Object> hasAccess = access.get(event.getTarget());
                            PageRecord pRecord = new PageRecord();
                            pRecord.setTime(event.getDate() + " " + event.getTime());
                            pRecord.setVersion(event.getDescription());
                            hasAccess.add(pRecord);
                            access.put(event.getTarget(), hasAccess);
                        } else {
                            guests.put(event.getTarget(), "1");
                            ArrayList<Object> hasAccess = new ArrayList<Object>();
                            PageRecord pRecord = new PageRecord();
                            pRecord.setTime(event.getDate() + " " + event.getTime());
                            hasAccess.add(pRecord);
                            access.put(event.getTarget(), hasAccess);
                        }
                    }
                    Iterator<String> iter = guests.keySet().iterator();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        QueryRecord record = new QueryRecord();
                        record.setObject(key);
                        record.setCount(Integer.parseInt(guests.get(key)));
                        record.setElements(access.get(key));
                        result.add(record);
                    }
                } else {
                    while (list.hasNext()) {
                        Event event = list.next();
                        if (guests.get(event.getTarget()) != null) {
                            guests.put(event.getTarget(), Integer.toString(Integer.parseInt(guests.get(event.getTarget())) + 1));
                            ArrayList<Object> hasAccess = access.get(event.getTarget());
                            PageRecord pRecord = new PageRecord();
                            pRecord.setPage(event.getTarget());
                            pRecord.setTime(event.getDate() + " " + event.getTime());
                            pRecord.setVersion(event.getDescription());
                            hasAccess.add(pRecord);
                            access.put(event.getTarget(), hasAccess);
                        } else {
                            guests.put(event.getTarget(), "1");
                            ArrayList<Object> hasAccess = new ArrayList<Object>();
                            PageRecord pRecord = new PageRecord();
                            pRecord.setPage(event.getTarget());
                            pRecord.setVersion(event.getDescription());
                            pRecord.setTime(event.getDate() + " " + event.getTime());
                            hasAccess.add(pRecord);
                            access.put(event.getTarget(), hasAccess);
                        }
                    }
                    Iterator<String> iter = guests.keySet().iterator();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        QueryRecord record = new QueryRecord();
                        record.setObject(key);
                        record.setCount(Integer.parseInt(guests.get(key)));
                        record.setElements(access.get(key));
                        result.add(record);
                    }
                }
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            return result;
        }
        return null;
    }

    private ArrayList<EventQuery> getEventQuery(String target, String object, Date fromTime, Date endTime) {
        ArrayList<EventQuery> queries = new ArrayList<EventQuery>();
        queries.add(EventQuery.equal(EventField.ATT_USER, target));
        queries.add(EventQuery.equal(EventField.ATT_EVENT_ID, object));
        if (fromTime != null) {
            SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = fmtDate.format(fromTime);
            SimpleDateFormat fmtTime = new SimpleDateFormat("HH:mm:ss");
            if (strDate != null) queries.add(EventQuery.greatOrEqual(EventField.ATT_DATE, strDate));
        }
        if (endTime != null) {
            SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = fmtDate.format(endTime);
            SimpleDateFormat fmtTime = new SimpleDateFormat("HH:mm:ss");
            if (strDate != null) queries.add(EventQuery.lessOrEqual(EventField.ATT_DATE, strDate));
        }
        return queries;
    }
}
