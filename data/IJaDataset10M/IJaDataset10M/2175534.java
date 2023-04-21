package org.nakedobjects.metamodel.examples.facets.jsr303;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.examples.facets.jsr303.Jsr303PropertyValidationFacet;
import org.nakedobjects.metamodel.interactions.PropertyModifyContext;
import org.nakedobjects.metamodel.spec.identifier.Identified;

@RunWith(JMock.class)
public class Jsr303FacetValidatingInteraction {

    private final Mockery mockery = new JUnit4Mockery() {

        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    private Jsr303PropertyValidationFacet facet;

    private Identified mockHolder;

    private PropertyModifyContext mockContext;

    private NakedObject mockTargetNakedObject;

    private NakedObject mockProposedNakedObject;

    private DomainObjectWithCustomValidation domainObjectWithCustomValidation;

    private DomainObjectWithBuiltInValidation domainObjectWithBuiltInValidation;

    @Before
    public void setUp() throws Exception {
        mockHolder = mockery.mock(Identified.class);
        facet = new Jsr303PropertyValidationFacet(mockHolder);
        mockContext = mockery.mock(PropertyModifyContext.class);
        mockTargetNakedObject = mockery.mock(NakedObject.class, "target");
        mockProposedNakedObject = mockery.mock(NakedObject.class, "proposed");
        domainObjectWithBuiltInValidation = new DomainObjectWithBuiltInValidation();
        domainObjectWithCustomValidation = new DomainObjectWithCustomValidation();
        mockery.checking(new Expectations() {

            {
                one(mockHolder).getIdentifier();
                will(returnValue(Identifier.propertyOrCollectionIdentifier(DomainObjectWithBuiltInValidation.class, "serialNumber")));
                one(mockContext).getTarget();
                will(returnValue(mockTargetNakedObject));
                one(mockContext).getProposed();
                will(returnValue(mockProposedNakedObject));
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        mockHolder = null;
        facet = null;
        mockContext = null;
    }

    @Test
    public void invalidatesWhenBuiltInConstraintVetoes() {
        mockery.checking(new Expectations() {

            {
                one(mockTargetNakedObject).getObject();
                will(returnValue(domainObjectWithBuiltInValidation));
                one(mockProposedNakedObject).getObject();
                will(returnValue("NONSENSE"));
            }
        });
        final String reason = facet.invalidates(mockContext);
        assertThat(reason, is(not(nullValue())));
        assertThat(reason, is("serialNumber is invalid: must match ....-....-...."));
    }

    @Test
    public void validatesWhenBuiltInConstraintIsMet() {
        mockery.checking(new Expectations() {

            {
                one(mockTargetNakedObject).getObject();
                will(returnValue(domainObjectWithBuiltInValidation));
                one(mockProposedNakedObject).getObject();
                will(returnValue("1234-5678-9012"));
            }
        });
        final String reason = facet.invalidates(mockContext);
        assertThat(reason, is(nullValue()));
    }

    @Test
    public void invalidatesWhenFailsCustomConstraint() {
        mockery.checking(new Expectations() {

            {
                one(mockTargetNakedObject).getObject();
                will(returnValue(domainObjectWithCustomValidation));
                one(mockProposedNakedObject).getObject();
                will(returnValue("NONSENSE"));
            }
        });
        final String reason = facet.invalidates(mockContext);
        assertThat(reason, is(not(nullValue())));
        assertThat(reason, is("serialNumber is invalid: must match ....-....-...."));
    }

    @Test
    public void validatesWhenFailsCustomConstraint() {
        mockery.checking(new Expectations() {

            {
                one(mockTargetNakedObject).getObject();
                will(returnValue(domainObjectWithCustomValidation));
                one(mockProposedNakedObject).getObject();
                will(returnValue("1234-5678-9012"));
            }
        });
        final String reason = facet.invalidates(mockContext);
        assertThat(reason, is(nullValue()));
    }
}
