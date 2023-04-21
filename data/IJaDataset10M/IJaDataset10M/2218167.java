package edu.ucsd.ncmir.unys.listeners;

import edu.ucsd.ncmir.asynchronous_event.AbstractAsynchronousEventListener;
import edu.ucsd.ncmir.asynchronous_event.AsynchronousEvent;

/**
 *
 * @author spl
 */
public class QuitEventListener extends AbstractAsynchronousEventListener {

    @Override
    public void handler(AsynchronousEvent event, Object object) {
        System.exit(0);
    }
}
