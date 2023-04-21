package org.rascalli.framework.event;

/**
 * <p>
 * 
 * </p>
 * 
 * <p>
 * <b>Company:&nbsp;</b> SAT, Research Studios Austria
 * </p>
 * 
 * <p>
 * <b>Copyright:&nbsp;</b> (c) 2007
 * </p>
 * 
 * <p>
 * <b>last modified:</b><br/> $Author: christian $<br/> $Date: 2008-02-07
 * 17:43:47 +0100 (Do, 07 Feb 2008) $<br/> $Revision: 2446 $
 * </p>
 * 
 * @author Christian Schollum
 */
public interface EventListener {

    /**
     * @param createUserContextChangeEvent
     */
    void handleEvent(Event event);
}
