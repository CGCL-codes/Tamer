package com.roxes.win32;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import junit.framework.TestCase;

public class RegistryTest extends TestCase {

    static final String ROXES_TEST_SUB_KEY = "RoxesTests_Delete_Me";

    static final String ROXES_TEST_SUB_KEY_PATH = "HKEY_CURRENT_USER\\" + ROXES_TEST_SUB_KEY;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(RegistryTest.class);
    }

    Registry reg = null;

    protected void setUp() throws Exception {
        reg = new Registry(Registry.HKEY_CURRENT_USER, ROXES_TEST_SUB_KEY);
        reg.create();
        super.setUp();
    }

    protected void tearDown() throws Exception {
        reg.delete();
        super.tearDown();
    }

    public void testCreateAndDelete() {
        Registry reg1 = new Registry(reg.getHKey(), reg.getSubKey() + "\\SÖFTWARE");
        reg1.create();
        assertTrue("No Key " + ROXES_TEST_SUB_KEY_PATH + "\\SÖFTWARE", reg1.exists());
        reg1.delete();
        assertFalse("Has Key " + ROXES_TEST_SUB_KEY_PATH + "\\SÖFTWARE", reg1.exists());
    }

    public void testCreateDeleteSubKey() {
        assertFalse("hasSubkey", reg.hasSubKey("TestSubKey"));
        Registry reg1 = reg.createSubKey("TestSubKey");
        assertTrue("hasSubkey", reg.hasSubKey("TestSubKey"));
        assertEquals("TestSubKey equals", reg1, reg.openSubKey("TestSubKey"));
        reg.deleteSubKey("TestSubKey");
        assertFalse("hasSubkey", reg.hasSubKey("TestSubKey"));
    }

    private static String dumpByteArray(byte[] ba) {
        StringBuffer sb = new StringBuffer("[");
        for (int i = 0; i < ba.length; i++) {
            byte b = ba[i];
            sb.append(b);
            if (i < ba.length - 1) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    public void testOldMain() {
        {
            System.out.println("################ test exists ################");
            Registry reg = new Registry(Registry.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\AntiVir");
            System.out.println(reg + " exists()=" + reg.exists());
            reg = new Registry(Registry.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\AntiVird");
            System.out.println(reg + " exists()=" + reg.exists());
            System.out.println("################ test create ################");
            reg = new Registry(Registry.HKEY_LOCAL_MACHINE, "SOFTWARE\\Roxes Technologies\\myapp");
            System.out.println(reg + " exists()=" + reg.exists());
            reg.create();
            System.out.println(reg + " exists()=" + reg.exists());
            try {
                reg.create();
            } catch (Win32Exception ex) {
                ex.printStackTrace();
                System.out.println("### OK ###");
            }
            System.out.println("################ test delete ################");
            reg.setValue("alpha", "beta");
            System.out.println(reg.getValue("alpha").equals("beta"));
            reg.deleteValue("alpha");
            System.out.println(reg.hasValue("alpha"));
            reg.delete();
            System.out.println(reg + " exists()=" + reg.exists());
            try {
                reg.delete();
            } catch (Win32Exception ex) {
                ex.printStackTrace();
                System.out.println("### OK ###");
            }
            System.out.println("################ test hasSubkeys ################");
            try {
                reg.hasSubKeys();
            } catch (Win32Exception ex) {
                ex.printStackTrace();
                System.out.println("### OK ###");
            }
            reg = new Registry(Registry.HKEY_LOCAL_MACHINE, "SOFTWARE\\Roxes Technologies");
            System.out.println(reg + " hasSubKeys()=" + reg.hasSubKeys());
            reg = new Registry(Registry.HKEY_LOCAL_MACHINE, "SOFTWARE");
            System.out.println(reg + " hasSubKeys()=" + reg.hasSubKeys());
            System.out.println("################ test hasValues ################");
            reg = new Registry(Registry.HKEY_LOCAL_MACHINE, "SOFTWARE\\Roxes Technologies\\myapp");
            try {
                reg.hasValues();
            } catch (Win32Exception ex) {
                ex.printStackTrace();
                System.out.println("### OK ###");
            }
            reg = new Registry(Registry.HKEY_LOCAL_MACHINE, "SOFTWARE\\Roxes Technologies");
            System.out.println(reg + " hasValues()=" + reg.hasValues());
            reg = new Registry(Registry.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion");
            System.out.println(reg + " hasValues()=" + reg.hasValues());
            System.out.println("################ test hasValue ################");
            reg = new Registry(Registry.HKEY_LOCAL_MACHINE, "SOFTWARE\\Roxes Technologies\\myapp");
            try {
                reg.hasValue("dummy");
            } catch (Win32Exception ex) {
                ex.printStackTrace();
                System.out.println("### OK ###");
            }
            reg = new Registry(Registry.HKEY_LOCAL_MACHINE, "SOFTWARE\\Roxes Technologies");
            System.out.println(reg + " hasValue( 'dummy')=" + reg.hasValue("dummy"));
            reg = new Registry(Registry.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion");
            System.out.println(reg + " hasValue( 'ProgramFilesDir')=" + reg.hasValue("ProgramFilesDir"));
            reg = new Registry(Registry.HKEY_LOCAL_MACHINE, "SOFTWARE\\Roxes Technologies");
            reg.setValue("test", "huhu");
            reg.setValue("int", new Integer(Integer.MAX_VALUE));
            reg.setValue("int", Integer.MAX_VALUE);
            reg.setValue("long", new Long(2000));
            reg.setValue("long", 2000);
            reg.setValue("true", Boolean.TRUE);
            reg.setValue("true", true);
            reg.setValue("false", Boolean.FALSE);
            reg.setValue("short", new Short((short) Short.MAX_VALUE));
            reg.setValue("short", Short.MAX_VALUE);
            reg.setValue("bigdecimal", new BigDecimal(333));
            reg.setValue("bigint", new BigInteger("300"));
            reg.setValue("byte", new Byte((byte) Byte.MAX_VALUE));
            reg.setValue("byte", (byte) Byte.MAX_VALUE);
            reg.setValue("bytearray", new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
            System.out.println("value test=" + reg.getValue("test"));
            System.out.println("value int=" + reg.getIntValue("int"));
            System.out.println("value long=" + reg.getLongValue("long"));
            System.out.println("value true=" + reg.getBooleanValue("true"));
            System.out.println("value false=" + reg.getBooleanValue("false"));
            System.out.println("value short=" + reg.getShortValue("short"));
            System.out.println("value bigdecimal=" + reg.getBigDecimalValue("bigdecimal"));
            System.out.println("value bigint=" + reg.getBigIntegerValue("bigint"));
            System.out.println("value byte=" + reg.getByteValue("byte"));
            System.out.println("value bytearray=" + dumpByteArray(reg.getByteArrayValue("bytearray")));
            Iterator iter = reg.valueNames();
            while (iter.hasNext()) {
                System.out.println(iter.next());
            }
            reg = new Registry(Registry.HKEY_LOCAL_MACHINE, "SOFTWARE");
            iter = reg.subKeyNames();
            while (iter.hasNext()) {
                System.out.println((String) iter.next());
            }
        }
    }

    public void testDumpGetDeleteByteArray() {
        byte[] values = { 0, 1, 2, 3, 4, 5, 6, 7 };
        assertFalse("Value exists", reg.hasValue("TestBytes"));
        reg.setValue("TestBytes", values);
        byte[] retValues = reg.getByteArrayValue("TestBytes");
        assertEquals("dumpByteArray length", values.length, retValues.length);
        for (int i = 0; i < values.length; i++) {
            assertEquals("dumpByteArray[" + i + "]", values[i], retValues[i]);
        }
        assertEquals("ByteArrayType", Registry.REG_BINARY, reg.getValueType("TestBytes"));
        reg.deleteValue("TestBytes");
        assertFalse("Value exists", reg.hasValue("TestBytes"));
    }

    public void testExists() {
        assertTrue("Has Key: " + ROXES_TEST_SUB_KEY_PATH, reg.exists());
    }

    public void testGetHKey() {
        assertEquals("HKey", Registry.HKEY_CURRENT_USER, reg.getHKey());
    }

    public void testGetHKeyName() {
        assertEquals("HKey", "HKEY_CURRENT_USER", Registry.getHKeyName(Registry.HKEY_CURRENT_USER));
    }

    public void testCreateHasDeleteSubKey() {
        assertFalse("hasSubkey", reg.hasSubKey("TestSubKey"));
        reg.setValue("TestSubKey", "TestSubKey");
        Registry reg1 = reg.createSubKey("TestSubKey");
        assertTrue("hasSubkey", reg.hasSubKey("TestSubKey"));
        reg1.delete();
        assertFalse("hasSubkey", reg.hasSubKey("TestSubKey"));
    }

    public void testSetGetDeleteBigDecimalValue() {
        assertFalse("Value exists", reg.hasValue("TestBigDecimal"));
        reg.setValue("TestBigDecimal", new BigDecimal(42.0));
        assertEquals("setBigDecimalValue", new BigDecimal(42.0), reg.getBigDecimalValue("TestBigDecimal"));
        assertEquals("BigDecimalValueType", Registry.REG_SZ, reg.getValueType("TestBigDecimal"));
        reg.deleteValue("TestBigDecimal");
        assertFalse("Value exists", reg.hasValue("TestBigDecimal"));
    }

    public void testSetGetDeleteBigIntegerValue() {
        assertFalse("Value exists", reg.hasValue("TestBigInteger"));
        reg.setValue("TestBigInteger", new BigInteger("42"));
        assertEquals("setBigIntegerValue", new BigInteger("42"), reg.getBigIntegerValue("TestBigInteger"));
        assertEquals("BigDecimalValueType", Registry.REG_SZ, reg.getValueType("TestBigInteger"));
        reg.deleteValue("TestBigInteger");
        assertFalse("Value exists", reg.hasValue("TestBigInteger"));
    }

    public void testSetGetDeleteBooleanValue() {
        assertFalse("Value exists", reg.hasValue("TestBoolean"));
        reg.setValue("TestBoolean", true);
        assertEquals("setBoolValue", true, reg.getBooleanValue("TestBoolean"));
        assertEquals("BoolValueType", Registry.REG_DWORD, reg.getValueType("TestBoolean"));
        reg.deleteValue("TestBoolean");
        assertFalse("Value exists", reg.hasValue("TestBoolean"));
    }

    public void testSetGetDeleteByteValue() {
        assertFalse("Value exists", reg.hasValue("TestByte"));
        reg.setValue("TestByte", (byte) 1);
        assertEquals("setByteValue", (byte) 1, reg.getByteValue("TestByte").byteValue());
        assertEquals("ByteValueType", Registry.REG_DWORD, reg.getValueType("TestByte"));
        reg.deleteValue("TestByte");
        assertFalse("Value exists", reg.hasValue("TestByte"));
    }

    public void testSetGetDeleteDoubleValue() {
        assertFalse("Value exists", reg.hasValue("TestDouble"));
        reg.setValue("TestDouble", (double) 42.0);
        assertEquals("setDoubleValue", (double) 42.0, reg.getDoubleValue("TestDouble"), Double.MIN_VALUE);
        assertEquals("DoubleValueType", Registry.REG_BINARY, reg.getValueType("TestDouble"));
        reg.deleteValue("TestDouble");
        assertFalse("Value exists", reg.hasValue("TestDouble"));
    }

    public void testSetGetDeleteFloatValue() {
        assertFalse("Value exists", reg.hasValue("TestFloat"));
        reg.setValue("TestFloat", (float) 42.0);
        assertEquals("setFloatValue", (float) 42.0, reg.getFloatValue("TestFloat"), Float.MIN_VALUE);
        assertEquals("FloatValueType", Registry.REG_BINARY, reg.getValueType("TestFloat"));
        reg.deleteValue("TestFloat");
        assertFalse("Value exists", reg.hasValue("TestFloat"));
    }

    public void testSetGetDeleteIntValue() {
        assertFalse("Value exists", reg.hasValue("TestInt"));
        reg.setValue("TestInt", (int) 42);
        assertEquals("setIntValue", (int) 42, reg.getIntValue("TestInt"));
        assertEquals("IntValueType", Registry.REG_DWORD, reg.getValueType("TestInt"));
        reg.deleteValue("TestInt");
        assertFalse("Value exists", reg.hasValue("TestInt"));
    }

    public void testSetGetDeleteLongValue() {
        assertFalse("Value exists", reg.hasValue("TestLong"));
        reg.setValue("TestLong", (long) 42);
        assertEquals("setLongValue", (long) 42, reg.getLongValue("TestLong"));
        assertEquals("longValueType", Registry.REG_DWORD, reg.getValueType("TestLong"));
        reg.deleteValue("TestLong");
        assertFalse("Value exists", reg.hasValue("TestLong"));
    }

    public void testSetGetDeleteShortValue() {
        assertFalse("Value exists", reg.hasValue("TestShort"));
        reg.setValue("TestShort", (short) 1);
        assertEquals("setShortValue", (short) 1, reg.getShortValue("TestShort"));
        assertEquals("ShortValueType", Registry.REG_DWORD, reg.getValueType("TestShort"));
        reg.deleteValue("TestShort");
        assertFalse("Value exists", reg.hasValue("TestShort"));
    }

    public void testSetGetDeleteStringValue() {
        assertFalse("Value exists", reg.hasValue("TestString"));
        reg.setValue("TestString", "TestString");
        assertEquals("setStringValue", "TestString", reg.getStringValue("TestString"));
        assertEquals("StringValueType", Registry.REG_SZ, reg.getValueType("TestString"));
        reg.deleteValue("TestString");
        assertFalse("Value exists", reg.hasValue("TestString"));
    }

    public void testSubKey() {
        Iterator iterSubKeyNames = reg.subKeyNames();
        Iterator iterSubKeys = reg.subKeys();
        assertFalse("SubKeyNames", iterSubKeyNames.hasNext());
        assertFalse("SubKeys", iterSubKeys.hasNext());
        Registry reg0 = reg.createSubKey("TestSubKey0");
        Registry reg1 = reg.createSubKey("TestSubKey1");
        Registry reg2 = reg.createSubKey("TestSubKey2");
        iterSubKeyNames = reg.subKeyNames();
        iterSubKeys = reg.subKeys();
        assertTrue("SubKeyNames", iterSubKeyNames.hasNext());
        assertTrue("SubKeys", iterSubKeys.hasNext());
        assertEquals("SubKeyNames TestSubKey0", "TestSubKey0", (String) iterSubKeyNames.next());
        assertEquals("SubKeys TestSubKey0", reg0, (Registry) iterSubKeys.next());
        assertTrue("SubKeyNames", iterSubKeyNames.hasNext());
        assertTrue("SubKeys", iterSubKeys.hasNext());
        assertEquals("SubKeyNames TestSubKey1", "TestSubKey1", (String) iterSubKeyNames.next());
        assertEquals("SubKeys TestSubKey1", reg1, (Registry) iterSubKeys.next());
        assertTrue("SubKeyNames", iterSubKeyNames.hasNext());
        assertTrue("SubKeys", iterSubKeys.hasNext());
        assertEquals("SubKeyNames TestSubKey2", "TestSubKey2", (String) iterSubKeyNames.next());
        assertEquals("SubKeys TestSubKey2", reg2, (Registry) iterSubKeys.next());
        assertFalse("SubKeyNames", iterSubKeyNames.hasNext());
        assertFalse("SubKeys", iterSubKeys.hasNext());
    }

    public void testToString() {
        assertEquals("toString", ROXES_TEST_SUB_KEY_PATH, reg.toString());
    }

    public void testValueNames() {
        Iterator iter = reg.valueNames();
        assertFalse("ValueNames", iter.hasNext());
        reg.setValue("TestValue0", "TestValue0");
        reg.setValue("TestValue1", "TestValue1");
        reg.setValue("TestValue2", "TestValue2");
        iter = reg.valueNames();
        assertTrue("ValueNames", iter.hasNext());
        assertEquals("ValueName TestValue0", "TestValue0", (String) iter.next());
        assertTrue("ValueNames", iter.hasNext());
        assertEquals("ValueName TestValue1", "TestValue1", (String) iter.next());
        assertTrue("ValueNames", iter.hasNext());
        assertEquals("ValueName TestValue2", "TestValue2", (String) iter.next());
        assertFalse("ValueNames", iter.hasNext());
    }
}
