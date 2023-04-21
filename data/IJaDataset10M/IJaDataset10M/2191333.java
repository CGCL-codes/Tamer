package org.nakedobjects.runtime.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nakedobjects.metamodel.adapter.Instance;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.facets.When;
import org.nakedobjects.metamodel.facets.disable.DisableForSessionFacetAbstract;
import org.nakedobjects.metamodel.facets.hide.HiddenFacetAbstract;
import org.nakedobjects.metamodel.facets.hide.HiddenFacetAlways;
import org.nakedobjects.metamodel.facets.hide.HiddenFacetImpl;
import org.nakedobjects.metamodel.facets.hide.HiddenFacetNever;
import org.nakedobjects.metamodel.facets.hide.HideForContextFacetNone;
import org.nakedobjects.metamodel.facets.hide.HideForSessionFacetAbstract;
import org.nakedobjects.metamodel.facets.naming.describedas.DescribedAsFacetAbstract;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacetAbstract;
import org.nakedobjects.metamodel.interactions.PropertyUsabilityContext;
import org.nakedobjects.metamodel.interactions.PropertyVisibilityContext;
import org.nakedobjects.metamodel.interactions.UsabilityContext;
import org.nakedobjects.metamodel.interactions.VisibilityContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.noruntime.RuntimeContextNoRuntime;
import org.nakedobjects.metamodel.runtimecontext.spec.feature.NakedObjectMemberAbstract;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.identifier.IdentifiedImpl;
import org.nakedobjects.runtime.testsystem.TestProxySystem;

public class NakedObjectMemberAbstractTest {

    private TestProxySystem system;

    private NakedObjectMemberAbstractImpl testMember;

    private NakedObject testAdapter;

    @Before
    public void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);
        system = new TestProxySystem();
        system.init();
        testAdapter = system.createPersistentTestObject();
        testMember = new NakedObjectMemberAbstractImpl("id");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testToString() throws Exception {
        testMember.addFacet(new NamedFacetAbstract("", testMember) {
        });
        assertTrue(testMember.toString().length() > 0);
    }

    @Test
    public void testAvailableForUser() throws Exception {
        testMember.addFacet(new DisableForSessionFacetAbstract(testMember) {

            public String disabledReason(final AuthenticationSession session) {
                return null;
            }
        });
        final Consent usable = testMember.isUsable(null, testAdapter);
        boolean allowed = usable.isAllowed();
        assertTrue(allowed);
    }

    @Test
    public void testVisibleWhenHiddenFacetSetToAlways() {
        testMember.addFacet(new HideForContextFacetNone(testMember));
        testMember.addFacet(new HiddenFacetAbstract(When.ALWAYS, testMember) {

            public String hiddenReason(final NakedObject target) {
                return null;
            }
        });
        final Consent visible = testMember.isVisible(null, testAdapter);
        assertTrue(visible.isAllowed());
    }

    @Test
    public void testVisibleWhenTargetPersistentAndHiddenFacetSetToOncePersisted() {
        testMember.addFacet(new HideForContextFacetNone(testMember));
        testMember.addFacet(new HiddenFacetImpl(When.ONCE_PERSISTED, testMember));
        assertFalse(testMember.isVisible(null, testAdapter).isAllowed());
    }

    @Test
    public void testVisibleWhenTargetPersistentAndHiddenFacetSetToUntilPersisted() {
        testMember.addFacet(new HideForContextFacetNone(testMember));
        testMember.addFacet(new HiddenFacetImpl(When.UNTIL_PERSISTED, testMember));
        final Consent visible = testMember.isVisible(null, testAdapter);
        assertTrue(visible.isAllowed());
    }

    @Test
    public void testVisibleWhenTargetTransientAndHiddenFacetSetToUntilPersisted() {
        testMember.addFacet(new HideForContextFacetNone(testMember));
        testMember.addFacet(new HiddenFacetImpl(When.UNTIL_PERSISTED, testMember));
        final NakedObject transientTestAdapter = system.createTransientTestObject();
        assertFalse(testMember.isVisible(null, transientTestAdapter).isAllowed());
    }

    @Test
    public void testVisibleDeclarativelyByDefault() {
        testMember.addFacet(new HiddenFacetNever(testMember) {
        });
        assertTrue(testMember.isVisible(null, testAdapter).isAllowed());
    }

    @Test
    public void testVisibleDeclaratively() {
        testMember.addFacet(new HiddenFacetAlways(testMember) {
        });
        assertFalse(testMember.isVisible(null, testAdapter).isAllowed());
    }

    @Test
    public void testVisibleForSessionByDefault() {
        final Consent visible = testMember.isVisible(null, testAdapter);
        assertTrue(visible.isAllowed());
    }

    @Test
    public void testVisibleForSession() {
        testMember.addFacet(new HideForSessionFacetAbstract(testMember) {

            public String hiddenReason(final AuthenticationSession session) {
                return "Hidden";
            }
        });
        assertFalse(testMember.isVisible(null, testAdapter).isAllowed());
    }

    @Test
    public void testVisibleForSessionFails() {
        testMember.addFacet(new HideForSessionFacetAbstract(testMember) {

            public String hiddenReason(final AuthenticationSession session) {
                return "hidden";
            }
        });
        assertFalse(testMember.isVisible(null, testAdapter).isAllowed());
    }

    @Test
    public void testName() throws Exception {
        final String name = "action name";
        testMember.addFacet(new NamedFacetAbstract(name, testMember) {
        });
        assertEquals(name, testMember.getName());
    }

    @Test
    public void testDescription() throws Exception {
        final String name = "description text";
        testMember.addFacet(new DescribedAsFacetAbstract(name, testMember) {
        });
        assertEquals(name, testMember.getDescription());
    }
}

class NakedObjectMemberAbstractImpl extends NakedObjectMemberAbstract {

    protected NakedObjectMemberAbstractImpl(final String id) {
        super(id, new IdentifiedImpl(), MemberType.ONE_TO_ONE_ASSOCIATION, new RuntimeContextNoRuntime());
    }

    protected NakedObjectMemberAbstractImpl(final String id, final RuntimeContext runtimeContext) {
        super(id, new IdentifiedImpl(), MemberType.ONE_TO_ONE_ASSOCIATION, runtimeContext);
    }

    public String debugData() {
        return null;
    }

    public Consent isUsable(final NakedObject target) {
        return null;
    }

    public NakedObjectSpecification getSpecification() {
        return null;
    }

    public UsabilityContext<?> createUsableInteractionContext(final AuthenticationSession session, final InteractionInvocationMethod invocationMethod, final NakedObject target) {
        return new PropertyUsabilityContext(session, invocationMethod, target, getIdentifier());
    }

    public VisibilityContext<?> createVisibleInteractionContext(final AuthenticationSession session, final InteractionInvocationMethod invocationMethod, final NakedObject targetNakedObject) {
        return new PropertyVisibilityContext(session, invocationMethod, targetNakedObject, getIdentifier());
    }

    public Instance getInstance(NakedObject nakedObject) {
        return null;
    }
}
