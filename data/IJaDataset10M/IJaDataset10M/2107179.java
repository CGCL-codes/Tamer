package org.apache.isis.extensions.jpa.runtime.persistence.oid;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.apache.isis.extensions.jpa.runtime.persistence.oid.JpaOid;
import org.junit.After;
import org.junit.Test;

public class GivenJpaOidWhenEnstring {

    @Test
    public void enstringTransient() {
        final JpaOid oid = JpaOid.createTransient("com.mycompany.Customer", 123, "CUS");
        assertThat(oid.enString(), is("CUS|123~"));
    }

    @After
    public void tearDown() throws Exception {
        JpaOid.testReset();
    }

    @Test
    public void enstringPersistent() {
        final JpaOid oid = JpaOid.createPersistent("com.mycompany.Customer", 123, "CUS");
        assertThat(oid.enString(), is("CUS|123"));
    }
}
