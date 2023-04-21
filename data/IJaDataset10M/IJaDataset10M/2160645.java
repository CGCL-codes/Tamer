package com.jetigy.magicbus.event.bus;

import com.jetigy.magicbus.event.EventVetoException;
import com.jetigy.magicbus.event.bus.queue.Task;

/**
 * 
 * @author Tim
 * 
 */
public class AsyncEventTopicDispatcher extends AbstractEventTopicDispatcher {

    public AsyncEventTopicDispatcher() {
        super();
    }

    public void fireCommandEvent(ChanneledEvent e) throws EventVetoException {
        fire(e);
    }

    /**
   * 
   * @param e
   */
    protected void fire(final ChanneledEvent e) {
        EventBus.instance().getEventDispatcher().dispatchLater(new Task() {

            public void execute() throws Exception {
                try {
                    fireEvent(e);
                } catch (EventVetoException e) {
                }
            }
        });
    }

    public void fireStateEvent(ChanneledEvent e) {
        fire(e);
    }
}
