package tests.api.java.io;

import dalvik.annotation.AndroidOnly;
import dalvik.annotation.KnownFailure;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

@SuppressWarnings("serial")
@TestTargetClass(Serializable.class)
public class SerializationStressTest1 extends SerializationStressTest {

    static final int INIT_INT_VALUE = 7;

    private static class SerializationTest implements java.io.Serializable {

        int anInt = INIT_INT_VALUE;

        public SerializationTest() {
            super();
        }
    }

    static final String INIT_STR_VALUE = "a string that is blortz";

    private static class SerializationTestSubclass1 extends SerializationTest {

        String aString = INIT_STR_VALUE;

        public SerializationTestSubclass1() {
            super();
            anInt = INIT_INT_VALUE / 2;
        }
    }

    private static class SpecTestSuperClass implements Runnable {

        protected java.lang.String instVar;

        public void run() {
        }

        SpecTestSuperClass() {
        }
    }

    private static class SpecTest extends SpecTestSuperClass implements Cloneable, Serializable {

        public java.lang.String instVar1;

        public static java.lang.String staticVar1;

        public static java.lang.String staticVar2;

        {
            instVar1 = "NonStaticInitialValue";
        }

        static {
            staticVar1 = "StaticInitialValue";
            staticVar1 = new String(staticVar1);
        }

        public Object method(Object objParam, Object objParam2) {
            return new Object();
        }

        public boolean method(boolean bParam, Object objParam) {
            return true;
        }

        public boolean method(boolean bParam, Object objParam, Object objParam2) {
            return true;
        }
    }

    private static class SpecTestSubclass extends SpecTest {

        public transient java.lang.String transientInstVar = "transientValue";
    }

    private static class ReadWriteObject implements java.io.Serializable {

        public boolean calledWriteObject = false;

        public boolean calledReadObject = false;

        public ReadWriteObject() {
            super();
        }

        private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
            calledReadObject = true;
            in.readObject();
        }

        private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
            calledWriteObject = true;
            out.writeObject(FOO);
        }
    }

    private static class PublicReadWriteObject implements java.io.Serializable {

        public boolean calledWriteObject = false;

        public boolean calledReadObject = false;

        public PublicReadWriteObject() {
            super();
        }

        public void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
            calledReadObject = true;
            in.readObject();
        }

        public void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
            calledWriteObject = true;
            out.writeObject(FOO);
        }
    }

    private static class FieldOrder implements Serializable {

        String aaa1NonPrimitive = "aaa1";

        int bbb1PrimitiveInt = 5;

        boolean aaa2PrimitiveBoolean = true;

        String bbb2NonPrimitive = "bbb2";
    }

    private static class JustReadObject implements java.io.Serializable {

        public boolean calledReadObject = false;

        public JustReadObject() {
            super();
        }

        private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
            calledReadObject = true;
            in.defaultReadObject();
        }
    }

    private static class JustWriteObject implements java.io.Serializable {

        public boolean calledWriteObject = false;

        public JustWriteObject() {
            super();
        }

        private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException, ClassNotFoundException {
            calledWriteObject = true;
            out.defaultWriteObject();
        }
    }

    private static class ClassBasedReplacementWhenDumping implements java.io.Serializable {

        public boolean calledReplacement = false;

        public ClassBasedReplacementWhenDumping() {
            super();
        }

        private Object writeReplace() {
            calledReplacement = true;
            return FOO;
        }
    }

    private static class MultipleClassBasedReplacementWhenDumping implements java.io.Serializable {

        private static class C1 implements java.io.Serializable {

            private Object writeReplace() {
                return new C2();
            }
        }

        private static class C2 implements java.io.Serializable {

            private Object writeReplace() {
                return new C3();
            }
        }

        private static class C3 implements java.io.Serializable {

            private Object writeReplace() {
                return FOO;
            }
        }

        public MultipleClassBasedReplacementWhenDumping() {
            super();
        }

        private Object writeReplace() {
            return new C1();
        }
    }

    private static class ClassBasedReplacementWhenLoading implements java.io.Serializable {

        public ClassBasedReplacementWhenLoading() {
            super();
        }

        private Object readResolve() {
            return FOO;
        }
    }

    private static class ClassBasedReplacementWhenLoadingViolatesFieldType implements java.io.Serializable {

        public ClassBasedReplacementWhenLoading classBasedReplacementWhenLoading = new ClassBasedReplacementWhenLoading();

        public ClassBasedReplacementWhenLoadingViolatesFieldType() {
            super();
        }
    }

    private static class MyExceptionWhenDumping1 implements java.io.Serializable {

        private static class MyException extends java.io.IOException {
        }

        ;

        public boolean anInstanceVar = false;

        public MyExceptionWhenDumping1() {
            super();
        }

        private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
            in.defaultReadObject();
        }

        private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException, ClassNotFoundException {
            throw new MyException();
        }
    }

    private static class MyExceptionWhenDumping2 implements java.io.Serializable {

        private static class MyException extends java.io.IOException {
        }

        ;

        public Integer anInstanceVar = new Integer(0xA1);

        public MyExceptionWhenDumping2() {
            super();
        }

        private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
            in.defaultReadObject();
        }

        private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException, ClassNotFoundException {
            throw new MyException();
        }
    }

    private static class NonSerializableExceptionWhenDumping implements java.io.Serializable {

        public Object anInstanceVar = new Object();

        public NonSerializableExceptionWhenDumping() {
            super();
        }
    }

    private static class MyUnserializableExceptionWhenDumping implements java.io.Serializable {

        private static class MyException extends java.io.IOException {

            @SuppressWarnings("unused")
            private Object notSerializable = new Object();
        }

        ;

        public boolean anInstanceVar = false;

        public MyUnserializableExceptionWhenDumping() {
            super();
        }

        private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
            in.defaultReadObject();
        }

        private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException, ClassNotFoundException {
            throw new MyException();
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_1_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            objToSave = "HelloWorld";
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, (((String) objLoaded).equals((String) objToSave)));
        } catch (IOException e) {
            fail("IOException serializing data : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type: " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_2_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            objToSave = null;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, objLoaded == objToSave);
        } catch (IOException e) {
            fail("IOException serializing data : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_3_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            byte[] bytes = { 0, 1, 2, 3 };
            objToSave = bytes;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, Arrays.equals((byte[]) objLoaded, (byte[]) objToSave));
        } catch (IOException e) {
            fail("IOException serializing data : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_4_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            int[] ints = { 0, 1, 2, 3 };
            objToSave = ints;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, Arrays.equals((int[]) objLoaded, (int[]) objToSave));
        } catch (IOException e) {
            fail("IOException serializing data : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_5_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            short[] shorts = { 0, 1, 2, 3 };
            objToSave = shorts;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, Arrays.equals((short[]) objLoaded, (short[]) objToSave));
        } catch (IOException e) {
            fail("IOException serializing data : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_6_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            long[] longs = { 0, 1, 2, 3 };
            objToSave = longs;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, Arrays.equals((long[]) objLoaded, (long[]) objToSave));
        } catch (IOException e) {
            fail("IOException serializing data : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_7_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            float[] floats = { 0.0f, 1.1f, 2.2f, 3.3f };
            objToSave = floats;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, Arrays.equals((float[]) objLoaded, (float[]) objToSave));
        } catch (IOException e) {
            fail("IOException serializing data: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_8_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            double[] doubles = { 0.0, 1.1, 2.2, 3.3 };
            objToSave = doubles;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, Arrays.equals((double[]) objLoaded, (double[]) objToSave));
        } catch (IOException e) {
            fail("IOException serializing data : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_9_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            boolean[] booleans = { true, false, false, true };
            objToSave = booleans;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, Arrays.equals((boolean[]) objLoaded, (boolean[]) objToSave));
        } catch (IOException e) {
            fail("IOException serializing data : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_10_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            String[] strings = { "foo", "bar", "java" };
            objToSave = strings;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, Arrays.equals((Object[]) objLoaded, (Object[]) objToSave));
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("Unable to read Object type: " + e.toString());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_11_writeObject() {
        Object objToSave = null;
        try {
            objToSave = new Object();
            if (DEBUG) System.out.println("Obj = " + objToSave);
            boolean passed = false;
            Throwable t = null;
            try {
                dumpAndReload(objToSave);
            } catch (NotSerializableException ns) {
                passed = true;
                t = ns;
            } catch (Exception wrongExc) {
                passed = false;
                t = wrongExc;
            }
            assertTrue("Failed to throw NotSerializableException when serializing " + objToSave + " Threw(if non-null) this: " + t, passed);
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_12_writeObject() {
        try {
            if (DEBUG) System.out.println("Obj = <mixed>");
            t_MixPrimitivesAndObjects();
        } catch (IOException e) {
            fail("IOException serializing data : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when dumping mixed types");
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_13_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            SerializationTestSubclass1 st = new SerializationTestSubclass1();
            st.anInt = Integer.MAX_VALUE;
            st.aString = FOO;
            objToSave = st;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, ((SerializationTestSubclass1) objLoaded).anInt == Integer.MAX_VALUE);
            assertTrue(MSG_TEST_FAILED + objToSave, ((SerializationTestSubclass1) objLoaded).aString.equals(FOO));
        } catch (IOException e) {
            fail("Exception serializing " + objToSave + "\t->" + e.toString());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            err.printStackTrace();
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_14_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            SpecTest specTest = new SpecTest();
            specTest.instVar = FOO;
            specTest.instVar1 = specTest.instVar;
            objToSave = specTest;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertNull(MSG_TEST_FAILED + objToSave, ((SpecTest) objLoaded).instVar);
            assertTrue(MSG_TEST_FAILED + objToSave, ((SpecTest) objLoaded).instVar1.equals(FOO));
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception serializing " + objToSave + "\t->" + e.toString());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_15_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            SpecTestSubclass specTestSubclass = new SpecTestSubclass();
            specTestSubclass.transientInstVar = FOO;
            objToSave = specTestSubclass;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertNull(MSG_TEST_FAILED + objToSave, ((SpecTestSubclass) objLoaded).transientInstVar);
        } catch (IOException e) {
            fail("Exception serializing " + objToSave + "\t->" + e.toString());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_16_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            String[] strings = new String[2];
            strings[0] = FOO;
            strings[1] = (" " + FOO + " ").trim();
            objToSave = strings;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            String[] stringsLoaded = (String[]) objLoaded;
            assertTrue(MSG_TEST_FAILED + objToSave, !(stringsLoaded[0] == stringsLoaded[1]));
        } catch (IOException e) {
            fail("Exception serializing " + objToSave + "\t->" + e.toString());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_17_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            ReadWriteObject readWrite = new ReadWriteObject();
            objToSave = readWrite;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, readWrite.calledWriteObject);
            assertTrue(MSG_TEST_FAILED + objToSave, ((ReadWriteObject) objLoaded).calledReadObject);
        } catch (IOException e) {
            fail("Exception serializing " + objToSave + "\t->" + e.toString());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_18_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            PublicReadWriteObject publicReadWrite = new PublicReadWriteObject();
            objToSave = publicReadWrite;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, !publicReadWrite.calledWriteObject);
            assertTrue(MSG_TEST_FAILED + objToSave, !((PublicReadWriteObject) objLoaded).calledReadObject);
        } catch (IOException e) {
            fail("Exception serializing " + objToSave + "\t->" + e.toString());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_19_writeObject() {
        Object objToSave = null;
        try {
            FieldOrder fieldOrder = new FieldOrder();
            objToSave = fieldOrder;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, true);
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_20_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            objToSave = Class.forName("java.lang.Integer");
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, objLoaded == objToSave);
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_21_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            objToSave = Class.forName("java.lang.Object");
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, objLoaded == objToSave);
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_22_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            java.net.URL url = new java.net.URL("http://localhost/a.txt");
            objToSave = url;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue("URLs are not the same: " + url + "\t,\t" + objLoaded, url.equals(objLoaded));
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_23_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            JustReadObject justReadObject = new JustReadObject();
            objToSave = justReadObject;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue("Called readObject on an object without a writeObject", !((JustReadObject) objLoaded).calledReadObject);
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_24_writeObject() {
        Object objToSave = null;
        try {
            JustWriteObject justWriteObject = new JustWriteObject();
            objToSave = justWriteObject;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, justWriteObject.calledWriteObject);
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type: " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_25_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            Vector<String> vector = new Vector<String>(1);
            vector.add(FOO);
            objToSave = vector;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            @SuppressWarnings("unchecked") Vector<String> obj = (Vector<String>) objLoaded;
            assertTrue(MSG_TEST_FAILED + objToSave, FOO.equals(obj.elementAt(0)));
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_26_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            Hashtable<String, String> hashTable = new Hashtable<String, String>(5);
            hashTable.put(FOO, FOO);
            objToSave = hashTable;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            @SuppressWarnings("unchecked") Hashtable<String, String> loadedHashTable = (Hashtable<String, String>) objLoaded;
            assertTrue(MSG_TEST_FAILED + objToSave, FOO.equals(loadedHashTable.get(FOO)));
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_27_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            ClassBasedReplacementWhenDumping classBasedReplacementWhenDumping = new ClassBasedReplacementWhenDumping();
            objToSave = classBasedReplacementWhenDumping;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue("Did not run writeReplace", classBasedReplacementWhenDumping.calledReplacement);
            assertTrue("Did not replace properly", FOO.equals(objLoaded));
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    @KnownFailure("nested writeReplace is not handled")
    public void test_18_28_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            MultipleClassBasedReplacementWhenDumping multipleClassBasedReplacementWhenDumping = new MultipleClassBasedReplacementWhenDumping();
            objToSave = multipleClassBasedReplacementWhenDumping;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue("Executed multiple levels of replacement (see PR 1F9RNT1), loaded= " + objLoaded, objLoaded instanceof String);
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.toString());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_29_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            ClassBasedReplacementWhenLoading classBasedReplacementWhenLoading = new ClassBasedReplacementWhenLoading();
            objToSave = classBasedReplacementWhenLoading;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue("Did not run readResolve", FOO.equals(objLoaded));
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_30_writeObject() {
        Object objToSave = null;
        try {
            ClassBasedReplacementWhenLoadingViolatesFieldType classBasedReplacementWhenLoadingViolatesFieldType = new ClassBasedReplacementWhenLoadingViolatesFieldType();
            objToSave = classBasedReplacementWhenLoadingViolatesFieldType;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            dumpAndReload(objToSave);
            fail("Loading replacements can cause field type violation in this implementation");
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (ClassCastException e) {
            assertTrue("Loading replacements can NOT cause field type violation in this implementation", true);
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_31_writeObject() {
        Object objToSave = null;
        try {
            MyExceptionWhenDumping1 exceptionWhenDumping = new MyExceptionWhenDumping1();
            objToSave = exceptionWhenDumping;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            boolean causedException = false;
            try {
                dump(objToSave);
            } catch (MyExceptionWhenDumping1.MyException e) {
                causedException = true;
            }
            ;
            assertTrue("Should have caused an exception when dumping", causedException);
            causedException = false;
            try {
                reload();
            } catch (ClassCastException e) {
                causedException = true;
            }
            ;
            assertTrue("Should have caused a ClassCastException when loading", causedException);
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_32_writeObject() {
        Object objToSave = null;
        try {
            MyExceptionWhenDumping2 exceptionWhenDumping = new MyExceptionWhenDumping2();
            objToSave = exceptionWhenDumping;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            boolean causedException = false;
            try {
                dump(objToSave);
            } catch (MyExceptionWhenDumping2.MyException e) {
                causedException = true;
            }
            ;
            assertTrue("Should have caused an exception when dumping", causedException);
            causedException = false;
            try {
                reload();
            } catch (java.io.WriteAbortedException e) {
                causedException = true;
            }
            ;
            assertTrue("Should have caused a java.io.WriteAbortedException when loading", causedException);
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (ClassCastException e) {
            fail("ClassCastException : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_NonSerializableExceptionWhenDumping() {
        Object objToSave = null;
        try {
            NonSerializableExceptionWhenDumping nonSerializableExceptionWhenDumping = new NonSerializableExceptionWhenDumping();
            objToSave = nonSerializableExceptionWhenDumping;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            boolean causedException = false;
            try {
                dump(objToSave);
            } catch (java.io.NotSerializableException e) {
                causedException = true;
            }
            ;
            assertTrue("Should have caused an exception when dumping", causedException);
            causedException = false;
            try {
                reload();
            } catch (java.io.WriteAbortedException e) {
                causedException = true;
            }
            ;
            assertTrue("Should have caused a java.io.WriteAbortedException when loading", causedException);
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_33_writeObject() {
        Object objToSave = null;
        try {
            MyUnserializableExceptionWhenDumping exceptionWhenDumping = new MyUnserializableExceptionWhenDumping();
            objToSave = exceptionWhenDumping;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            boolean causedException = false;
            try {
                dump(objToSave);
            } catch (java.io.ObjectStreamException e) {
                causedException = true;
            }
            ;
            assertTrue("Should have caused an exception when dumping", causedException);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_34_writeObject() {
        Object objToSave = null;
        try {
            java.io.IOException ioe = new java.io.IOException();
            objToSave = ioe;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, true);
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_35_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            objToSave = Class.forName("java.util.Hashtable");
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, objLoaded == objToSave);
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_36_writeObject() {
        Object objToSave = null;
        try {
            java.io.IOException ex = new java.io.InvalidClassException(FOO);
            objToSave = ex;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, true);
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_37_writeObject() {
        Object objToSave = null;
        try {
            java.io.IOException ex = new java.io.InvalidObjectException(FOO);
            objToSave = ex;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, true);
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_38_writeObject() {
        Object objToSave = null;
        try {
            java.io.IOException ex = new java.io.NotActiveException(FOO);
            objToSave = ex;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, true);
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_39_writeObject() {
        Object objToSave = null;
        try {
            java.io.IOException ex = new java.io.NotSerializableException(FOO);
            objToSave = ex;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, true);
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_40_writeObject() {
        Object objToSave = null;
        try {
            java.io.IOException ex = new java.io.StreamCorruptedException(FOO);
            objToSave = ex;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, true);
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }
}
