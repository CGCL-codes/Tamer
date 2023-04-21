package org.enerj.enhancer;

import org.enerj.annotations.Persist;

/**
 * Test class for TopLevelPersistableEnhancementTest.
 * Note super-class doesn't have an exposed no-arg constructor.
 */
@Persist
class TLPTestClass3 extends TLPTestClassNP {

    private static int sSomeValue = 5;

    private int mValue;

    TLPTestClass3(int aValue) {
        super(true);
        mValue = aValue;
    }

    TLPTestClass3() {
        this(4);
    }

    public void someMethod() {
        mValue = 22;
        sSomeValue = 23;
    }

    public boolean equals(Object anObject) {
        if (!(anObject instanceof TLPTestClass3)) {
            return false;
        }
        TLPTestClass3 obj = (TLPTestClass3) anObject;
        return this.mValue == obj.mValue;
    }
}
