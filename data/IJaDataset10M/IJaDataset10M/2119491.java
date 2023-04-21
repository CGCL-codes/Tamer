package com.hsbc.hbfr.ccf.at.logreader.ui.swt;

import org.eclipse.jface.action.Action;
import org.apache.log4j.Logger;
import com.hsbc.hbfr.ccf.at.logreader.ui.UIConstants;

/**
 * Created by IntelliJ IDEA.
 * User: Administrateur
 * Date: 10 juin 2005
 * Time: 18:34:39
 * To change this template use File | Settings | File Templates.
 */
public class ActionConfigSave extends Action {

    private static final Logger logger = Logger.getLogger("INFRA." + ActionConfigSave.class.getName());

    private final LogViewerFormSWT form;

    public ActionConfigSave(LogViewerFormSWT formSWT) {
        super(UIConstants.MENU_FILE_CONFIGURATION_SAVE);
        form = formSWT;
    }

    public void run() {
        logger.debug("run");
        if (form.currentConfig == null) {
            String aConfigName = "todo";
            form.currentConfig = aConfigName;
        }
        if (form.currentConfig != null) {
            form.saveConfiguration(form.currentConfig);
        }
    }
}
