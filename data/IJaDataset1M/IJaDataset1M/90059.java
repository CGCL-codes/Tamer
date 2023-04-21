package net.sourceforge.sandirc.events;

import jerklib.events.IRCEvent;

/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public interface IRCEventRunnable {

    public void run(IRCEvent e);
}
