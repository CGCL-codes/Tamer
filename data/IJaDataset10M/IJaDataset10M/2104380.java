package proper.gui.core.event;

import java.util.EventListener;

/**
* This Listener is for reacting to CurrentDirChangeEvents fired by a 
* <code>FileTextField</code>, if the current dir has been changed. With this
* it is possible to synchronize several TextFields, s.t. the user doesn't have
* to change to the same directory for every TextField.
*
*
* @see         proper.gui.core.text.FileTextField
* @see         proper.gui.core.event.CurrentDirChangeEvent
* @author      FracPete
* @version $Revision: 1.2 $
*/
public interface CurrentDirChangeListener extends EventListener {

    /**
   * invoked by a FileTextField if a file was selected and the current dir
   * changed.
   */
    public void currentDirChanged(CurrentDirChangeEvent e);
}
