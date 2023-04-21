package org.nakedobjects.corelib.events;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.nakedobjects.corelib.Identifier;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class InteractionEventTests {

    private final Mockery mockery = new JUnit4Mockery();

    private InteractionEvent interactionEvent;

    private Object source;

    private Identifier identifier;

    private Class<? extends InteractionEventTests> advisorClass;

    @Before
    public void setUp() {
        source = new Object();
        identifier = Identifier.actionIdentifier("CustomerOrder", "cancelOrder", new Class[] { String.class, boolean.class });
        advisorClass = this.getClass();
    }

    @Test
    public void getIdentifier() {
        interactionEvent = new InteractionEvent(source, identifier) {

            private static final long serialVersionUID = 1L;
        };
        assertThat(interactionEvent.getIdentifier(), is(identifier));
    }

    @Test
    public void getSource() {
        interactionEvent = new InteractionEvent(source, identifier) {

            private static final long serialVersionUID = 1L;
        };
        assertThat(interactionEvent.getSource(), is(source));
    }

    @Test
    public void getClassName() {
        interactionEvent = new InteractionEvent(source, identifier) {

            private static final long serialVersionUID = 1L;
        };
        assertThat(interactionEvent.getClassName(), equalTo("CustomerOrder"));
    }

    @Test
    public void getClassNaturalName() {
        interactionEvent = new InteractionEvent(source, identifier) {

            private static final long serialVersionUID = 1L;
        };
        assertThat(interactionEvent.getClassNaturalName(), equalTo("Customer Order"));
    }

    @Test
    public void getMember() {
        interactionEvent = new InteractionEvent(source, identifier) {

            private static final long serialVersionUID = 1L;
        };
        assertThat(interactionEvent.getMemberName(), equalTo("cancelOrder"));
    }

    @Test
    public void getMemberNaturalName() {
        interactionEvent = new InteractionEvent(source, identifier) {

            private static final long serialVersionUID = 1L;
        };
        assertThat(interactionEvent.getMemberNaturalName(), equalTo("Cancel Order"));
    }

    @Test
    public void shouldInitiallyNotVeto() {
        interactionEvent = new InteractionEvent(source, identifier) {

            private static final long serialVersionUID = 1L;
        };
        assertThat(interactionEvent.isVeto(), is(false));
    }

    @Test
    public void afterAdvisedShouldVeto() {
        interactionEvent = new InteractionEvent(source, identifier) {

            private static final long serialVersionUID = 1L;
        };
        interactionEvent.advised("some reason", this.getClass());
        assertThat(interactionEvent.isVeto(), is(true));
    }

    @Test
    public void afterAdvisedShouldReturnReason() {
        interactionEvent = new InteractionEvent(source, identifier) {

            private static final long serialVersionUID = 1L;
        };
        interactionEvent.advised("some reason", this.getClass());
        assertThat(interactionEvent.isVeto(), is(true));
    }

    @Test
    public void afterAdvisedShouldReturnAdvisorClass() {
        interactionEvent = new InteractionEvent(source, identifier) {

            private static final long serialVersionUID = 1L;
        };
        interactionEvent.advised("some reason", advisorClass);
        assertEquals(interactionEvent.getAdvisorClass(), advisorClass);
    }
}
