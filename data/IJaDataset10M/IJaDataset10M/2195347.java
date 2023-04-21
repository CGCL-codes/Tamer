package org.nakedobjects.nof.core.adapter.value;

public class BigIntAdapterWithNullTest extends AbstractValueAdapterWithNullTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        value = new BigIntegerAdapter();
    }
}
