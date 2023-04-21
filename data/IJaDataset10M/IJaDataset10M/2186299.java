package com.google.gwt.user.client.ui;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;

/**
 * Tests for {@link Composite}.
 */
public class CompositeTest extends GWTTestCase {

    static int orderIndex;

    @Override
    public String getModuleName() {
        return "com.google.gwt.user.User";
    }

    private static class EventTestComposite extends Composite {

        TextBox tb = new TextBox();

        boolean widgetFocusFired;

        boolean widgetLostFocusFired;

        boolean widgetFocusHandlerFired;

        boolean widgetBlurHandlerFired;

        boolean domFocusFired;

        boolean domBlurFired;

        @SuppressWarnings("deprecation")
        public EventTestComposite() {
            initWidget(tb);
            sinkEvents(Event.FOCUSEVENTS);
            tb.addFocusListener(new FocusListener() {

                public void onLostFocus(Widget sender) {
                    widgetLostFocusFired = true;
                }

                public void onFocus(Widget sender) {
                    widgetFocusFired = true;
                }
            });
            tb.addFocusHandler(new FocusHandler() {

                public void onFocus(FocusEvent event) {
                    widgetFocusHandlerFired = true;
                }
            });
            tb.addBlurHandler(new BlurHandler() {

                public void onBlur(BlurEvent event) {
                    widgetBlurHandlerFired = true;
                }
            });
        }

        @Override
        public void onBrowserEvent(Event event) {
            switch(DOM.eventGetType(event)) {
                case Event.ONFOCUS:
                    domFocusFired = true;
                    return;
                case Event.ONBLUR:
                    domBlurFired = true;
                    break;
            }
            super.onBrowserEvent(event);
        }
    }

    public void disabledTestBrowserEvents() {
        final EventTestComposite c = new EventTestComposite();
        RootPanel.get().add(c);
        this.delayTestFinish(1000);
        DeferredCommand.addCommand(new Command() {

            public void execute() {
                DeferredCommand.addCommand(new Command() {

                    public void execute() {
                        assertTrue(c.domFocusFired);
                        assertTrue(c.domBlurFired);
                        assertTrue(c.widgetLostFocusFired);
                        assertTrue(c.widgetBlurHandlerFired);
                        assertFalse(c.widgetFocusFired);
                        assertFalse(c.widgetFocusHandlerFired);
                        finishTest();
                    }
                });
                c.tb.setFocus(false);
            }
        });
        c.tb.setFocus(true);
    }

    /**
   * This test is here to prevent a "No tests found" warning in Junit.
   * 
   * TODO: Remove this when testBrowserEvents is enabled
   */
    public void testNothing() {
    }

    public void testAttachAndDetachOrder() {
        class TestAttachHandler implements AttachEvent.Handler {

            int delegateAttachOrder;

            int delegateDetachOrder;

            public void onAttachOrDetach(AttachEvent event) {
                if (event.isAttached()) {
                    delegateAttachOrder = ++orderIndex;
                } else {
                    delegateDetachOrder = ++orderIndex;
                }
            }
        }
        class TestComposite extends Composite {

            TextBox tb = new TextBox();

            public TestComposite() {
                initWidget(tb);
            }
        }
        TestComposite c = new TestComposite();
        TestAttachHandler ca = new TestAttachHandler();
        TestAttachHandler wa = new TestAttachHandler();
        c.addAttachHandler(ca);
        c.tb.addAttachHandler(wa);
        RootPanel.get().add(c);
        RootPanel.get().remove(c);
        assertTrue(ca.delegateAttachOrder > 0);
        assertTrue(ca.delegateDetachOrder > 0);
        assertTrue(ca.delegateAttachOrder > wa.delegateAttachOrder);
        assertTrue(ca.delegateDetachOrder < wa.delegateDetachOrder);
    }
}
