package er.corebusinesslogic.audittrail;

import er.extensions.foundation.*;
import er.extensions.eof.*;
import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class _ERCAuditTrailEntry extends ERXGenericRecord {

    public static final String ENTITY_NAME = "ERCAuditTrailEntry";

    public interface Key {

        public static final String CREATED = "created";

        public static final String KEY_PATH = "keyPath";

        public static final String NEW_VALUES = "newValues";

        public static final String OLD_VALUES = "oldValues";

        public static final String TYPE = "type";

        public static final String USER_GLOBAL_ID = "userGlobalID";

        public static final String USER_INFO = "userInfo";

        public static final String NEW_BLOB_VALUE = "newBlobValue";

        public static final String OLD_BLOB_VALUE = "oldBlobValue";

        public static final String TRAIL = "trail";
    }

    public static class _ERCAuditTrailEntryClazz extends ERXGenericRecord.ERXGenericRecordClazz<ERCAuditTrailEntry> {
    }

    public NSTimestamp created() {
        return (NSTimestamp) storedValueForKey(Key.CREATED);
    }

    public void setCreated(NSTimestamp value) {
        takeStoredValueForKey(value, Key.CREATED);
    }

    public String keyPath() {
        return (String) storedValueForKey(Key.KEY_PATH);
    }

    public void setKeyPath(String value) {
        takeStoredValueForKey(value, Key.KEY_PATH);
    }

    public String newValues() {
        return (String) storedValueForKey(Key.NEW_VALUES);
    }

    public void setNewValues(String value) {
        takeStoredValueForKey(value, Key.NEW_VALUES);
    }

    public String oldValues() {
        return (String) storedValueForKey(Key.OLD_VALUES);
    }

    public void setOldValues(String value) {
        takeStoredValueForKey(value, Key.OLD_VALUES);
    }

    public er.corebusinesslogic.audittrail.ERCAuditTrailType type() {
        return (er.corebusinesslogic.audittrail.ERCAuditTrailType) storedValueForKey(Key.TYPE);
    }

    public void setType(er.corebusinesslogic.audittrail.ERCAuditTrailType value) {
        takeStoredValueForKey(value, Key.TYPE);
    }

    public ERXKeyGlobalID userGlobalID() {
        return (ERXKeyGlobalID) storedValueForKey(Key.USER_GLOBAL_ID);
    }

    public void setUserGlobalID(ERXKeyGlobalID value) {
        takeStoredValueForKey(value, Key.USER_GLOBAL_ID);
    }

    public ERXMutableDictionary userInfo() {
        return (ERXMutableDictionary) storedValueForKey(Key.USER_INFO);
    }

    public void setUserInfo(ERXMutableDictionary value) {
        takeStoredValueForKey(value, Key.USER_INFO);
    }

    public er.corebusinesslogic.audittrail.ERCAuditBlob newBlobValue() {
        return (er.corebusinesslogic.audittrail.ERCAuditBlob) storedValueForKey(Key.NEW_BLOB_VALUE);
    }

    public void setNewBlobValue(er.corebusinesslogic.audittrail.ERCAuditBlob value) {
        takeStoredValueForKey(value, Key.NEW_BLOB_VALUE);
    }

    public er.corebusinesslogic.audittrail.ERCAuditBlob oldBlobValue() {
        return (er.corebusinesslogic.audittrail.ERCAuditBlob) storedValueForKey(Key.OLD_BLOB_VALUE);
    }

    public void setOldBlobValue(er.corebusinesslogic.audittrail.ERCAuditBlob value) {
        takeStoredValueForKey(value, Key.OLD_BLOB_VALUE);
    }

    public er.corebusinesslogic.audittrail.ERCAuditTrail trail() {
        return (er.corebusinesslogic.audittrail.ERCAuditTrail) storedValueForKey(Key.TRAIL);
    }

    public void setTrail(er.corebusinesslogic.audittrail.ERCAuditTrail value) {
        takeStoredValueForKey(value, Key.TRAIL);
    }
}
