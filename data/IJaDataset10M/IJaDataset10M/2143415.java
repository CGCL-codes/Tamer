package org.sourceforge.jemm.database.components.types;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sourceforge.jemm.database.ClassId;
import org.sourceforge.jemm.database.ClassInfo;
import org.sourceforge.jemm.database.FieldInfo;
import org.sourceforge.jemm.database.FieldType;
import org.sourceforge.jemm.database.ObjectState;
import org.sourceforge.jemm.database.ObjectSyncResponse;
import org.sourceforge.jemm.types.ID;

/**
 * Test cases for the StoredSetObject.
 * 
 * @author Rory Graves
 */
public class StoredUserObjectTest {

    public static final String DUMMY_CLASS = "com.foo.Bar";

    ID id = new ID(1);

    ClassId classId = new ClassId(1234);

    StoredUserObject uo;

    FieldInfo field1 = new FieldInfo("a", DUMMY_CLASS, FieldType.OBJECT);

    FieldInfo field2 = new FieldInfo("b", DUMMY_CLASS, FieldType.INT);

    @Before
    public void setup() {
        Set<FieldInfo> fields = new HashSet<FieldInfo>();
        fields.add(field1);
        fields.add(field2);
        ClassInfo info = new ClassInfo(DUMMY_CLASS, fields);
        uo = new StoredUserObject(id, classId, info);
        Map<FieldInfo, Object> initialValues = new HashMap<FieldInfo, Object>();
        initialValues.put(field1, id);
        initialValues.put(field2, Integer.valueOf(7));
        ObjectState syncData = new ObjectState(id, 0, initialValues);
        uo.synchronizeObject(syncData);
        uo.clearModified();
    }

    @Test
    public void nullUpdateTest() {
        ObjectState syncData = new ObjectState(id, 0, Collections.<FieldInfo, Object>emptyMap());
        ObjectSyncResponse resp = uo.synchronizeObject(syncData);
        Assert.assertFalse(uo.getModified());
        Assert.assertEquals(0, resp.getNewVersionNo());
    }

    @Test
    public void updateTest() {
        Map<FieldInfo, Object> newValues = new HashMap<FieldInfo, Object>();
        newValues.put(field1, new ID(2));
        ObjectState syncData = new ObjectState(id, 0, newValues);
        ObjectSyncResponse resp = uo.synchronizeObject(syncData);
        Assert.assertTrue(uo.getModified());
        Assert.assertEquals(1, resp.getNewVersionNo());
    }
}
