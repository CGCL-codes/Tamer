package com.spartez.ezdelegator;

import com.spartez.ezdelegator.annotation.Delegate;

/**
 * User: kalamon
 * Date: 2009-05-01
 * Time: 19:57:42
 */
public abstract class TestComposite2 implements TestIfc {

    @Delegate(ifc = TestIfc.class)
    private TestImpl testImpl;

    public TestComposite2() {
    }
}
