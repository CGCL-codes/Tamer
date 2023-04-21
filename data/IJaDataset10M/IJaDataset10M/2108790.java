package org.nakedobjects.embedded;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.embedded.dom.claim.ClaimRepositoryImpl;
import org.nakedobjects.embedded.dom.employee.EmployeeRepositoryImpl;

@RunWith(JMock.class)
public class GivenMetaModelWhenInitialized {

    private Mockery mockery = new JUnit4Mockery();

    private EmbeddedContext mockContext;

    private NakedObjectsMetaModel metaModel;

    @Before
    public void setUp() {
        mockContext = mockery.mock(EmbeddedContext.class);
        metaModel = new NakedObjectsMetaModel(mockContext, EmployeeRepositoryImpl.class, ClaimRepositoryImpl.class);
        metaModel.init();
    }

    @Test
    public void shouldBeAbleToGetViewer() {
        assertThat(metaModel.getViewer(), is(notNullValue()));
    }
}
