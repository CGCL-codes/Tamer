package ru.aslanov.schedule.client.ds;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceDateTimeField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Created: Mar 4, 2010 1:23:14 PM
 *
 * @author Sergey Aslanov
 */
public class GoogleCalendarSyncDS extends ChildDS {

    private static Map<String, DataSource> instances = new HashMap<String, DataSource>();

    public static DataSource getInstance(String schduleKey) {
        DataSource dataSource = instances.get(schduleKey);
        if (dataSource == null) {
            dataSource = new GoogleCalendarSyncDS(schduleKey, null);
            instances.put(schduleKey, dataSource);
        }
        return dataSource;
    }

    private GoogleCalendarSyncDS(String parentId, InputLangProvider inputLangProvider) {
        super("GoogleCalendarSync", parentId, "schedule", inputLangProvider);
        DataSourceField hasSessionToken = new DataSourceBooleanField("hasSessionToken");
        DataSourceField authorizedByUser = new DataSourceTextField("authorizedByUser");
        DataSourceField lastPublishDate = new DataSourceDateTimeField("lastPublishDate");
        DataSourceField lastPublishErrors = new DataSourceTextField("lastPublishErrors");
        DataSourceField syncStatus = new DataSourceTextField("syncStatus");
        setFields(encodedKey, hasSessionToken, authorizedByUser, lastPublishDate, lastPublishErrors, syncStatus);
    }
}
