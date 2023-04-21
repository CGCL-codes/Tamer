package org.plazmaforge.framework.client.swing.forms;

import javax.swing.JPanel;
import org.plazmaforge.framework.core.exception.ApplicationException;

public interface PanelController extends EditFormController {

    /** Create panel */
    JPanel createPanel() throws ApplicationException;

    /** Initialize panel */
    void initPanel() throws ApplicationException;

    /** Initialize labels of panel */
    void initLabel(JPanel panel);

    /** Initialize fields of panel */
    void initField(JPanel panel);
}
