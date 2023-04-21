package er.corebusinesslogic.audittrail;

import er.extensions.foundation.*;
import er.extensions.eof.*;
import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class _ERCAuditBlob extends ERXGenericRecord {

    public static final String ENTITY_NAME = "ERCAuditBlob";

    public interface Key {

        public static final String BLOB_VALUE = "blobValue";
    }

    public static class _ERCAuditBlobClazz extends ERXGenericRecord.ERXGenericRecordClazz<ERCAuditBlob> {
    }

    public NSData blobValue() {
        return (NSData) storedValueForKey(Key.BLOB_VALUE);
    }

    public void setBlobValue(NSData value) {
        takeStoredValueForKey(value, Key.BLOB_VALUE);
    }
}
