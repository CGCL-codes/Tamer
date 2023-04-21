package com.csbubbles.mamont.core;

import com.csbubbles.mamont.App;
import com.csbubbles.mamont.msg.Msg;
import com.csbubbles.mamont.msg.MsgStop;

/**
 * Abstract class for subscribing and handling inner application messages.
 */
public abstract class AbstractSubscriber extends AbstractComponent {

    /**
	 * Message types to handle. Subscriber has to return
	 * array of message types which he wants to manage.
	 */
    public abstract Class<? extends Msg>[] getMsgTypes();

    @Override
    protected void onInterrupted(InterruptedException ex) {
        super.onInterrupted(ex);
        App.dispatch(new MsgStop(this));
    }
}
