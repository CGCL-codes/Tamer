package net.sf.doolin.gui.monitor;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * Progress dialog used by the {@link TaskMonitor}.
 * 
 * @author Damien Coraboeuf
 * 
 */
public class TaskMonitorDialog extends JOptionPane {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Constructor
	 * 
	 * @param messageList
	 *            Message list as specified in {@link JOptionPane}.
	 * @see JOptionPane#JOptionPane(Object, int, int, javax.swing.Icon,
	 *      Object[], Object)
	 */
    public TaskMonitorDialog(Object messageList) {
        super(messageList, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[] { getCancelOption() }, null);
    }

    private static Object getCancelOption() {
        return UIManager.getString("OptionPane.cancelButtonText");
    }
}
