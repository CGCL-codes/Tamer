package sfeir.gwt.ergosoom.server.service;

import org.json.JSONArray;
import org.json.JSONException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class BackupAccess {

    public static boolean save(JSONArray contacts, String email) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity entity = null;
        try {
            entity = ds.prepare(new Query("Backup").addFilter("email", FilterOperator.EQUAL, email)).asSingleEntity();
        } catch (Exception e) {
        }
        if (null == entity) entity = new Entity("Backup");
        entity.setProperty("email", email);
        entity.setProperty("contacts", new Text(contacts.toString()));
        ds.put(entity);
        return true;
    }

    public static JSONArray get(String email) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity entity = ds.prepare(new Query("Backup").addFilter("email", FilterOperator.EQUAL, email)).asSingleEntity();
        Text pbytes = (Text) entity.getProperty("contacts");
        JSONArray contacts = null;
        try {
            contacts = new JSONArray(pbytes.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contacts;
    }
}
