package net.sourceforge.cridmanager.actions;

import org.apache.log4j.Logger;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import javax.swing.AbstractAction;
import javax.swing.Action;
import net.sourceforge.cridmanager.CridController;
import net.sourceforge.cridmanager.CridInfo;
import net.sourceforge.cridmanager.ISettings;
import net.sourceforge.cridmanager.Messages;
import net.sourceforge.cridmanager.Utils;
import net.sourceforge.cridmanager.error.CridmanagerException;
import net.sourceforge.cridmanager.services.ServiceProvider;

/**
 * F�gt das Datum dem Aufnahmetitel hinzu. Provisorium bis zu einer vern�nftigen Filterl�sung, siehe
 * RFE [ 1219284 ] Datum automatisch an Aufnahme-Titel anh�ngen
 */
public class AppendDateAction extends AbstractAction {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(AppendDateAction.class);

    /**
	 * 
	 */
    private final CridController controller;

    public AppendDateAction(CridController controller) {
        super(Messages.getString("CridController.AppendDateAction"));
        this.controller = controller;
        putValue(IActions.SIZABLE_ICON_NAME, "images/Ordner_Datum_hinzufuegen.gif");
        putValue(Action.SHORT_DESCRIPTION, Messages.getString("CridController.AppendDateActionD"));
    }

    public void actionPerformed(ActionEvent e) {
        if (logger.isDebugEnabled()) {
            logger.debug("actionPerformed(ActionEvent) - start");
        }
        ISettings settings = ServiceProvider.instance().getSettings();
        CridInfo[] crids = this.controller.getSelectedCrids();
        SimpleDateFormat sdf = new SimpleDateFormat(settings.read(ISettings.APPEND_DATE_FORMAT, "'('yyyyMMdd')'"));
        for (int i = 0; i < crids.length; i++) {
            CridInfo info = crids[i];
            String formatString = "%0% %1%";
            if (settings.isSet(ISettings.APPEND_DATE_PREFIX)) formatString = "%1% %0%";
            String newTitle = Utils.format(formatString, new Object[] { info.getCridTitel(), sdf.format(info.getCridStartDate()) });
            info.setCridTitel(newTitle);
            try {
                info.write();
            } catch (CridmanagerException e1) {
                logger.error("actionPerformed(ActionEvent)", e1);
                e1.printStackTrace();
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("actionPerformed(ActionEvent) - end");
        }
    }
}
