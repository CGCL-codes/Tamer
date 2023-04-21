package org.enerj.enhancer;

import java.util.*;
import java.io.*;
import org.enerj.core.*;

class TestClass implements Cloneable {

    private final String mTestNonPersistentFinal = "a";

    static String mTestNonPersistentStatic;

    transient int mTestNonPersistentTransient;

    private int mPrivateInt;

    public int mPublicInt;

    protected String mProtectedString;

    long mPackageLong;

    private char mChar;

    private byte mByte;

    private short mShort;

    private int mInt;

    private long mLong;

    private float mFloat;

    private double mDouble;

    private Character mCharObj;

    private Byte mByteObj;

    private Short mShortObj;

    private Integer mIntObj;

    private Long mLongObj;

    private Float mFloatObj;

    private Double mDoubleObj;

    private String mString;

    private TestClass mTestClassObj;

    private Object[] mObjectArray = new Object[10];

    private Properties mProperties;

    static {
        mTestNonPersistentStatic = "foo";
    }

    TestClass() {
        this(new String("test"), 5);
        System.out.println(mLong);
        mLong = 10239;
        System.out.println(mLong);
    }

    TestClass(String aString, int aValue) {
        if (aValue == 5) {
            mString = aString;
            mShortObj = new Short((short) 32);
        } else {
            mString = aString + "cc";
            mShortObj = new Short((short) 66);
        }
        mDoubleObj = new Double(55.);
    }

    void testFieldAccessMethod() {
        System.out.println(mLong);
        ++mLong;
        System.out.println(mLong);
    }

    static void testFieldAccessStaticMethod(TestClass aTestClass) {
        System.out.println(aTestClass.mLong);
        aTestClass.mLong = 10255;
        System.out.println(aTestClass.mLong);
    }

    public Object clone() throws CloneNotSupportedException {
        TestClass clone = (TestClass) super.clone();
        if (mLong == 44) {
            clone.mString = "4848";
        } else {
            clone.mString = "clone";
            return clone;
        }
        clone.mShortObj = new Short((short) 55);
        return clone;
    }

    private void enerjPostLoad() {
        System.out.println("enerjPostLoad called on " + this);
    }

    private void enerjPreStore() {
        System.out.println("enerjPreStore called on " + this);
    }

    private void enerjPostStore() {
        System.out.println("enerjPostStore called on " + this);
    }

    private void enerjPreHollow() {
        System.out.println("enerjPreHollow called on " + this);
    }

    public static void main(String[] args) throws Exception {
        TestClass test = new TestClass();
        org.enerj.core.Persistable persistable = (org.enerj.core.Persistable) test;
        persistable.enerj_SetAllowNonTransactionalRead(true);
        persistable.enerj_SetAllowNonTransactionalWrite(true);
        test.testFieldAccessMethod();
        testFieldAccessStaticMethod(test);
        test.mIntObj = new Integer(234);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        persistable.enerj_WriteObject(new ObjectSerializer(dos));
        byte[] bytes = baos.toByteArray();
        System.out.println("Wrote " + bytes.length + " bytes");
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bais);
        TestClass test2 = new TestClass();
        org.enerj.core.Persistable persistable2 = (org.enerj.core.Persistable) test2;
        persistable2.enerj_SetAllowNonTransactionalRead(true);
        persistable2.enerj_SetAllowNonTransactionalWrite(true);
        persistable2.enerj_ReadObject(new ObjectSerializer(dis));
        if (test2.mLong != test.mLong) {
            System.out.println("mLongs differ");
        }
        if (test2.mIntObj.intValue() != test.mIntObj.intValue()) {
            System.out.println("mIntObjs differ");
        }
        TestClass testclone = (TestClass) test.clone();
        System.out.println("Clone works");
        System.out.println("done");
    }
}
