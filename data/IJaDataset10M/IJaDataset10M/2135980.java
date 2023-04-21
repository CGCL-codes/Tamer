package org.nakedobjects.embedded;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.util.List;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.embedded.dom.claim.ClaimRepositoryImpl;
import org.nakedobjects.embedded.dom.employee.EmployeeRepositoryImpl;

@RunWith(JMock.class)
public class GivenMetaModelWhenInstantiate {

    private Mockery mockery = new JUnit4Mockery();

    private EmbeddedContext mockContext;

    private NakedObjectsMetaModel metaModel;

    @Before
    public void setUp() {
        mockContext = mockery.mock(EmbeddedContext.class);
    }

    @Test
    public void shouldSucceedWithoutThrowingAnyExceptions() {
        metaModel = new NakedObjectsMetaModel(mockContext);
    }

    @Test
    public void shouldBeAbleToRegisterServices() {
        metaModel = new NakedObjectsMetaModel(mockContext, EmployeeRepositoryImpl.class, ClaimRepositoryImpl.class);
        List<Class<?>> serviceTypes = metaModel.getServiceTypes();
        assertThat(serviceTypes.size(), is(2));
        assertThat(serviceTypes.contains(EmployeeRepositoryImpl.class), is(true));
        assertThat(serviceTypes.contains(ClaimRepositoryImpl.class), is(true));
    }
}
