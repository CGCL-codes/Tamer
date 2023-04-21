package org.ocd.desktop;

import org.ocd.cmdmgr.*;

/**
 * Listeners can be added to a LaunchPanel that receive notifications when the launchPanel
 * is launching a Cmd and when the launchPanel is done launching the Cmd
 *
 * @author  drichan
 * @version
 */
public interface ILaunchListener {

    /**
   * Launch Panel as triggered an ICmdAction
   * @param action that has been Launched
   */
    public void launchStarted(ICmdAction action);

    /**
   * Launch Panel as finished launching an Action
   * @param action that has finished
   */
    public void launchCompleted(ICmdAction action);
}
