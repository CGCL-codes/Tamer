package org.nakedobjects.runtime.authentication.standard;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.nakedobjects.metamodel.encoding.EncodabilityContractTest;

public abstract class SimpleSessionEncodabilityTestAbstract extends EncodabilityContractTest {

    @Override
    protected void assertRoundtripped(Object decodedEncodable, Object originalEncodable) {
        SimpleSession decoded = (SimpleSession) decodedEncodable;
        SimpleSession original = (SimpleSession) originalEncodable;
        assertThat(decoded.getUserName(), is(equalTo(original.getUserName())));
        assertThat(decoded.getRoles(), is(equalTo(original.getRoles())));
    }
}
