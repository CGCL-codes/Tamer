package aurora.hwc.control;

import javax.swing.*;

/**
 * Base class for queue controller editing panels.
 * @author Alex Kurzhanskiy
 * @version $Id: AbstractPanelQController.java 38 2010-02-08 22:59:00Z akurzhan $
 */
public abstract class AbstractPanelQController extends JPanel {

    private static final long serialVersionUID = 1048949988339374993L;

    protected AbstractQueueController qcontroller = null;

    protected WindowQControllerEditor winQCE;

    /**
	 * Fills the panel with controller specific fields.
	 */
    protected abstract void fillPanel();

    /**
	 * Returns window header with event description.
	 */
    public final String getHeader() {
        return qcontroller.toString();
    }

    /**
	 * Initializes controller editing panel.
	 * @param qctrl queue controller.
	 */
    public void initialize(AbstractQueueController qctrl) {
        qcontroller = qctrl;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        fillPanel();
        winQCE = new WindowQControllerEditor(this, null);
        winQCE.setVisible(true);
        return;
    }

    /**
	 * Saves queue controller properties.
	 */
    public abstract void save();
}
