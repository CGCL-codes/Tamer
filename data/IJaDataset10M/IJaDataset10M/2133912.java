package chrriis.dj.sweet.components;

import org.eclipse.swt.internal.SWTEventListener;

/**
 * @author Christopher Deckers
 */
public interface FlashPlayerListener extends SWTEventListener {

    /**
   * The Flash player can invoke special commands to the application simply by calling:<br>
   * <code>ExternalInterface.call("sendCommand", commandName, arg1, arg2, ...);</code>
   */
    public void commandReceived(FlashPlayerCommandEvent e);
}
