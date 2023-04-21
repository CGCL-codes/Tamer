package org.berlin.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.berlin.model.RequestActivityModel;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * Data access object for the activitiy log.
 * 
 * @author bbrown
 *
 */
public class RequestActivityLog {

    private static final SimpleDateFormat DF_POSIX = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    public void log(final HttpServletRequest request) {
        final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        final Entity log = new Entity("RequestActivityLog");
        final String date = DF_POSIX.format(new Date());
        log.setProperty("timestamp", date);
        log.setProperty("requestURL", request.getRequestURL().toString());
        log.setProperty("ipAddress", request.getRemoteAddr());
        log.setProperty("method", request.getMethod());
        datastore.put(log);
    }

    public List<RequestActivityModel> list() {
        final List<RequestActivityModel> list = new ArrayList<RequestActivityModel>();
        final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        final Query query = new Query("RequestActivityLog").addSort("timestamp", Query.SortDirection.DESCENDING);
        final PreparedQuery pq = datastore.prepare(query);
        final List<Entity> lst = pq.asList(FetchOptions.Builder.withLimit(40));
        for (final Entity log : lst) {
            final RequestActivityModel model = new RequestActivityModel(log.getProperty("timestamp"), log.getProperty("requestURL"), log.getProperty("ipAddress"), log.getProperty("method"));
            model.marshal();
            list.add(model);
        }
        Collections.sort(list);
        final int maxItems = 30;
        final List<RequestActivityModel> listRes = new ArrayList<RequestActivityModel>();
        for (final RequestActivityModel n : list) {
            listRes.add(n);
            if (listRes.size() >= maxItems) {
                break;
            }
        }
        return listRes;
    }
}
