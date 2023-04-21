package com.bitmovers.maui.engine.messagedispatcher;

/**
* MessageThreadFactory <p>
*
* A simple MessageThreadFactory.  This just creates a simple MessageThread which
* only displays the ".toString ()" result of the message object
*/
public class MessageThreadFactory {

    class MessageThread extends A_MessageThread {

        public void processMessage(Object aMessage) {
            System.out.println("Empty message handler got message " + aMessage);
        }
    }

    /**
	* Create a MessageThread
	*
	* @return A new MessageThread object
	*/
    public A_MessageThread createMessageThread() {
        return new MessageThread();
    }
}
