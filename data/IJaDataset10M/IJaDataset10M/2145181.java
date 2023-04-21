package org.cilogon.d2.storage;

import edu.uiuc.ncsa.security.storage.data.ConversionMap;
import edu.uiuc.ncsa.security.storage.data.MapConverter;
import edu.uiuc.ncsa.security.storage.data.SerializationKeys;
import org.cilogon.d2.storage.provider.UserProvider;
import javax.inject.Provider;

/**
 * <p>Created by Jeff Gaynor<br>
 * on 4/16/12 at  5:10 PM
 */
public class ArchivedUserMapConverter extends MapConverter<ArchivedUser> {

    public ArchivedUserMapConverter(Provider<ArchivedUser> vProvider, UserMapConverter umc) {
        super(new AUKeys(), vProvider);
        userMapConverter = umc;
    }

    UserMapConverter userMapConverter;

    public static class AUKeys extends SerializationKeys {

        public AUKeys() {
            identifier(archivedUserIDColumn);
        }

        String archivedUserIDColumn = "archived_user_id";

        String archivedTimestampColumn = "archive_time";

        public String archivedUserIDColumn(String... x) {
            if (0 < x.length) archivedUserIDColumn = x[0];
            return archivedUserIDColumn;
        }

        public String archivedTimestampColumn(String... x) {
            if (0 < x.length) archivedTimestampColumn = x[0];
            return archivedTimestampColumn;
        }
    }

    protected AUKeys getAUKeys() {
        return (AUKeys) keys;
    }

    @Override
    public ArchivedUser fromMap(ConversionMap<String, Object> map, ArchivedUser v) {
        ArchivedUser archivedUser = super.fromMap(map, v);
        User u = userMapConverter.fromMap(map, archivedUser.getUser());
        archivedUser.setArchivedDate(map.getDate(getAUKeys().archivedTimestampColumn()));
        archivedUser.setArchivedDate(map.getDate(getAUKeys().archivedTimestampColumn()));
        archivedUser.setUser(u);
        return archivedUser;
    }

    @Override
    public void toMap(ArchivedUser value, ConversionMap<String, Object> data) {
        super.toMap(value, data);
    }
}
