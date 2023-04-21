package net.cygeek.tech.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import net.cygeek.tech.client.data.DbVersion;
import java.util.Date;

public interface DbVersionServiceAsync {

    void getDbVersions(AsyncCallback async);

    void addDbVersion(DbVersion mDbVersion, boolean isNew, AsyncCallback async);

    void deleteDbVersion(String id, AsyncCallback async);

    void getDbVersion(String id, AsyncCallback async);
}
