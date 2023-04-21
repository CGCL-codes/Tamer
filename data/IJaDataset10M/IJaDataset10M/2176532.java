package org.nakedobjects.remoting.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.testspec.TestProxySpecification;
import org.nakedobjects.remoting.data.DummyEncodeableObjectData;
import org.nakedobjects.remoting.data.DummyReferenceData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.exchange.ClearValueRequest;
import org.nakedobjects.remoting.exchange.ClearValueResponse;
import org.nakedobjects.remoting.exchange.SetValueRequest;
import org.nakedobjects.remoting.exchange.SetValueResponse;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.facade.impl.ServerFacadeImpl;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.ConcurrencyException;
import org.nakedobjects.runtime.testsystem.ProxyJunit4TestCase;
import org.nakedobjects.runtime.testsystem.TestProxyAssociation;
import org.nakedobjects.runtime.testsystem.TestProxyVersion;

@RunWith(JMock.class)
public class ServerFacadeImpl_ParseableAssociationsTest extends ProxyJunit4TestCase {

    private Mockery mockery = new JUnit4Mockery();

    private ServerFacadeImpl server;

    private AuthenticationSession session;

    private DummyReferenceData movieData;

    private NakedObject object;

    private TestProxyAssociation nameField;

    private AuthenticationManager mockAuthenticationManager;

    private ObjectEncoderDecoder mockObjectEncoder;

    /**
     * Testing the {@link ServerFacadeImpl} implementation of {@link ServerFacade}.
     *
     * <p>
     * This uses the encoder to unmarshall objects
     * and then calls the persistor and reflector; all of which should be mocked.
     */
    @Before
    public void setUp() throws Exception {
        mockAuthenticationManager = mockery.mock(AuthenticationManager.class);
        mockObjectEncoder = mockery.mock(ObjectEncoderDecoder.class);
        server = new ServerFacadeImpl(mockAuthenticationManager);
        server.setEncoder(mockObjectEncoder);
        server.init();
        object = system.createPersistentTestObject();
        final TestProxySpecification spec = (TestProxySpecification) object.getSpecification();
        nameField = new TestProxyAssociation("name", system.getSpecification(String.class));
        spec.setupFields(new NakedObjectAssociation[] { nameField });
        movieData = new DummyReferenceData(object.getOid(), "none", new TestProxyVersion(1));
    }

    @After
    public void tearDown() throws Exception {
        system.shutdown();
    }

    /**
     * TODO: other tests for clear: - clear collection element - fails if unauthorised - fails if unavailable
     *
     * <p>
     * could place all these clear test in one class; test other methods in other classes
     */
    @Test
    public void testClearAssociation() {
        NakedObjectsContext.getTransactionManager().startTransaction();
        ClearValueRequest request = new ClearValueRequest(session, "name", movieData);
        ClearValueResponse response = server.clearValue(request);
        final ObjectData[] updatesData = response.getUpdates();
        NakedObjectsContext.getTransactionManager().endTransaction();
        nameField.assertFieldEmpty(object);
        assertEquals(0, updatesData.length);
    }

    @Test
    public void testSetValue() {
        final TestProxySpecification specf = system.getSpecification(String.class);
        specf.addFacet(new EncodableFacet() {

            public String toEncodedString(final NakedObject object) {
                return null;
            }

            public NakedObject fromEncodedString(final String encodedData) {
                return getAdapterManager().adapterFor(encodedData);
            }

            public Class facetType() {
                return EncodableFacet.class;
            }

            public FacetHolder getFacetHolder() {
                return null;
            }

            public void setFacetHolder(final FacetHolder facetHolder) {
            }

            public boolean alwaysReplace() {
                return false;
            }

            public boolean isDerived() {
                return false;
            }

            public boolean isNoop() {
                return false;
            }

            public Facet getUnderlyingFacet() {
                return null;
            }

            public void setUnderlyingFacet(Facet underlyingFacet) {
                throw new UnsupportedOperationException();
            }
        });
        NakedObjectsContext.getTransactionManager().startTransaction();
        SetValueRequest request = new SetValueRequest(session, "name", movieData, new DummyEncodeableObjectData("name of movie"));
        SetValueResponse response = server.setValue(request);
        final ObjectData[] updates = response.getUpdates();
        NakedObjectsContext.getTransactionManager().endTransaction();
        nameField.assertField(object, "name of movie");
        assertEquals(0, updates.length);
    }

    @Test
    public void testSetAssociationFailsWithNonCurrentTarget() {
        try {
            object.setOptimisticLock(new TestProxyVersion(2));
            SetValueRequest request = new SetValueRequest(session, "name", movieData, new DummyEncodeableObjectData("name of movie"));
            server.setValue(request);
            fail();
        } catch (final ConcurrencyException expected) {
        }
    }

    @Test
    public void testSetAssociationFailsWhenInvisible() {
        nameField.setUpIsVisible(false);
        try {
            SetValueRequest request = new SetValueRequest(session, "name", movieData, new DummyEncodeableObjectData("name of movie"));
            server.setValue(request);
            fail();
        } catch (final NakedObjectException expected) {
            assertEquals("can't modify field as not visible or editable", expected.getMessage());
        }
    }

    @Test
    public void testSetAssociationFailsWhenUnavailable() {
        nameField.setUpIsUnusableFor(object);
        try {
            SetValueRequest request = new SetValueRequest(session, "name", movieData, new DummyEncodeableObjectData("test data"));
            server.setValue(request);
            fail();
        } catch (final NakedObjectException expected) {
            assertEquals("can't modify field as not visible or editable", expected.getMessage());
        }
    }
}
