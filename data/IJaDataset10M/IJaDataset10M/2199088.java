package com.mycila.inject.cyclic;

import com.google.inject.AbstractModule;
import com.google.inject.ProvisionException;
import com.mycila.inject.jsr250.Jsr250;
import com.mycila.inject.jsr250.Jsr250Injector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PostConstructOnCyclicDependenciesTest {

    Jsr250Injector inj;

    @Before
    public void setUp() throws Exception {
        inj = Jsr250.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                bind(A.class).to(AImpl.class);
                bind(B.class).to(BImpl.class);
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        Jsr250Injector dying = inj;
        inj = null;
        dying.destroy();
    }

    @Test
    public void testPostConstructOnCyclicDependency() {
        A a = null;
        try {
            a = inj.getInstance(A.class);
        } catch (ProvisionException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
        assertNotNull(a);
        assertTrue(a.hasBeenCalled());
    }
}
