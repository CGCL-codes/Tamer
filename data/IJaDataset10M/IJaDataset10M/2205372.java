package org.mobicents.servlet.sip.restcomm.interpreter.tagstrategies;

import org.mobicents.servlet.sip.restcomm.callmanager.Call;
import org.mobicents.servlet.sip.restcomm.callmanager.CallException;
import org.mobicents.servlet.sip.restcomm.callmanager.events.CallEvent;
import org.mobicents.servlet.sip.restcomm.callmanager.events.CallEventType;
import org.mobicents.servlet.sip.restcomm.callmanager.events.EventListener;
import org.mobicents.servlet.sip.restcomm.callmanager.events.EventType;
import org.mobicents.servlet.sip.restcomm.interpreter.TagStrategy;
import org.mobicents.servlet.sip.restcomm.interpreter.TagStrategyException;
import org.mobicents.servlet.sip.restcomm.interpreter.TwiMLInterpreter;
import org.mobicents.servlet.sip.restcomm.interpreter.TwiMLInterpreterContext;
import org.mobicents.servlet.sip.restcomm.xml.Tag;

public abstract class TwiMLTagStrategy implements TagStrategy {

    private final EventListener<CallEvent> listener;

    public TwiMLTagStrategy() {
        super();
        this.listener = new CallEventListener(this);
    }

    protected void answer(final Call call) throws TagStrategyException {
        if (call.getState().equals(Call.RINGING)) {
            try {
                call.addListener(listener);
                call.answer();
                synchronized (this) {
                    wait();
                }
                call.removeListener(listener);
            } catch (final CallException exception) {
                throw new TagStrategyException(exception);
            } catch (final InterruptedException exception) {
                throw new TagStrategyException(exception);
            }
        }
    }

    public abstract void execute(final TwiMLInterpreter interpreter, final TwiMLInterpreterContext context, final Tag tag) throws TagStrategyException;

    private final class CallEventListener implements EventListener<CallEvent> {

        private final Object sleeper;

        private CallEventListener(final Object sleeper) {
            super();
            this.sleeper = sleeper;
        }

        public void onEvent(CallEvent event) {
            final EventType type = event.getType();
            if (type.equals(CallEventType.IN_CALL)) {
                synchronized (sleeper) {
                    sleeper.notify();
                }
            }
        }
    }
}
