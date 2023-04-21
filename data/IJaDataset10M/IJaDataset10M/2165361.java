package it.diamonds.tests.engine.input;

import it.diamonds.engine.input.Event;
import it.diamonds.engine.input.InputDeviceInterface;
import it.diamonds.engine.input.InputListenerInterface;
import it.diamonds.engine.input.Event.Code;
import it.diamonds.engine.input.Event.State;
import it.diamonds.tests.mocks.MockKeyboard;
import junit.framework.TestCase;

public class TestListener extends TestCase implements InputListenerInterface {

    private Event notifiedEvent;

    public void testNotifyCorrectEventToListners() {
        InputDeviceInterface keyboard = MockKeyboard.create();
        keyboard.addListener(this);
        keyboard.notify(Event.create(Code.KEY_A, State.PRESSED));
        assertTrue(notifiedEvent.is(Code.KEY_A));
        assertTrue(notifiedEvent.isPressed());
    }

    public void notify(Event event) {
        notifiedEvent = event;
    }
}
