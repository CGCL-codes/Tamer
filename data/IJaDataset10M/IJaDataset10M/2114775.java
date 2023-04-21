package guijavacommander.actions;

import guijavacommander.JavaCommander;
import javax.swing.*;
import java.awt.event.ActionEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: Deady
 * Date: 17.07.2009
 * Time: 12:34:13
 */
public class SwapPanelsAction extends AbstractAction {

    private Log logger = LogFactory.getLog(SwapPanelsAction.class);

    public void actionPerformed(ActionEvent e) {
        logger.debug("Swaping panels");
        JavaCommander.instance.swapPanels();
    }
}
