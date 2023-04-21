package net.sourceforge.pmd.swingui.event;

import java.util.EventListener;

/**
 *
 * @author Donald A. Leckie
 * @since December 13, 2002
 * @version $Revision: 2869 $, $Date: 2004-08-07 01:55:44 -0400 (Sat, 07 Aug 2004) $
 */
public interface StatusBarEventListener extends EventListener {

    /**
     **************************************************************************
     *
     * @param event
     */
    void startAnimation(StatusBarEvent event);

    /**
     **************************************************************************
     *
     * @param event
     */
    void showMessage(StatusBarEvent event);

    /**
     **************************************************************************
     *
     * @param event
     */
    void stopAnimation(StatusBarEvent event);
}
