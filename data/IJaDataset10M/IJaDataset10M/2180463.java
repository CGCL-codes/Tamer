package com.funambol.storage;

import com.funambol.storage.ObjectStore;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.microedition.rms.RecordStoreException;
import jmunit.framework.cldc10.AssertionFailedException;
import jmunit.framework.cldc10.TestCase;
import com.funambol.storage.ObjectStore;
import javax.microedition.rms.RecordStoreException;
import jmunit.framework.cldc10.TestSuite;

public class ObjectStoreTest extends TestCase {

    private ObjectStore os = null;

    private String storename1 = "Test1";

    private String storename2 = "Test2";

    int recordIndex = -1;

    public class TestHtClass implements Serializable {

        private Hashtable ht;

        public TestHtClass() {
            ht = new Hashtable();
            ht.put(new String("Funambol"), new String("Developers"));
            ht.put(new String("Patrick"), new String("Ohly"));
            ht.put(new String("Andrea"), new String("Toccalini"));
        }

        public void serialize(DataOutputStream out) throws IOException {
            ComplexSerializer.serializeHashTable(out, ht);
        }

        public void deserialize(DataInputStream in) throws IOException {
            ComplexSerializer.deserializeHashTable(in);
        }

        public String toString() {
            StringBuffer ret = new StringBuffer(this.getClass().getName());
            for (Enumeration e = ht.keys(); e.hasMoreElements(); ) {
                String k = (String) e.nextElement();
                ret.append(k);
                ret.append(": ");
                ret.append((String) ht.get(k));
                ret.append("\n");
            }
            return ret.toString();
        }
    }

    public ObjectStoreTest() {
        super(4, "ObjectStoreTest");
    }

    public void test(int testNumber) throws Throwable {
        os = new ObjectStore();
        switch(testNumber) {
            case 0:
                testCreate();
                break;
            case 1:
                testOpenClose();
                break;
            case 2:
                testStore();
                break;
            case 3:
                testRetrieve();
                break;
            default:
                break;
        }
        os = null;
    }

    public void testCreate() throws Exception {
        assertTrue(os.create(storename1));
        os.close();
        assertTrue(os.create(storename2));
        os.close();
        assertTrue(os.create(storename1));
        os.close();
        assertTrue(os.create(storename2));
        os.close();
    }

    public void testOpenClose() throws Exception {
        boolean ret = os.open(storename1);
        assertTrue(ret);
        ret = os.open(storename1);
        assertFalse(ret);
        ret = os.open(storename2);
        assertTrue(ret);
        os.close();
        ret = os.open(storename2);
        assertTrue(ret);
        os.close();
        try {
            os.open("NonExiststentStore");
            throw new Exception("Non existent RecordStore " + "has been opened: check for errors");
        } catch (RecordStoreException e) {
            System.out.println(new StringBuffer("Exception catched: ").append(e.getMessage()).toString());
        }
    }

    private int testStore() throws Exception {
        try {
            os.open(storename1);
            for (int i = 1; i <= 3; i++) {
                TestHtClass htc = new TestHtClass();
                recordIndex = os.store(htc);
                assertEquals(htc, os.retrieve(recordIndex, htc));
            }
            os.close();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private int testRetrieve() throws Exception {
        os.open(storename1);
        TestHtClass tc = new TestHtClass();
        assertEquals(os.retrieve(recordIndex - 2, tc).toString(), tc.toString());
        assertEquals(os.retrieve(recordIndex - 1, tc).toString(), tc.toString());
        assertEquals(os.retrieve(recordIndex, tc).toString(), tc.toString());
        os.close();
        return 0;
    }
}
