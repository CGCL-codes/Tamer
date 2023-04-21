package gov.lanl.GUITools;

import javax.swing.JComponent;

/**Defines an external updater interface for widgets from an InfoListPanel
 * @author Jim George
 * @version $Id: UpdaterInterface.java 1411 2002-05-01 22:55:45Z dwforslund $
 */
public interface UpdaterInterface {

    /**Update the values in the widet bases on USE and current val
     * @param widget is the widget which needs updating
     * @param whichUse is the USE which caused the update
     * @param whatReason is the current value of the USE item
     */
    public void updateWidget(JComponent widget, String whichUse, String whatReason);
}
