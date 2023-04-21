package ch.skyguide.tools.requirement.hmi.action;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import ch.skyguide.tools.requirement.hmi.RepositoryManager;
import ch.skyguide.tools.requirement.hmi.RequirementTool;
import ch.skyguide.tools.requirement.hmi.model.BeanManagerAndTableModelFactory;

@SuppressWarnings("serial")
public class BrowseToLogFileAction extends AbstractAction {

    public BrowseToLogFileAction() {
        super(BeanManagerAndTableModelFactory.getInstance().getTranslatedText("menu.browseToLogFile"));
        putValue(SHORT_DESCRIPTION, BeanManagerAndTableModelFactory.getInstance().getTranslatedText("hint.browseToLogFile"));
    }

    public void actionPerformed(ActionEvent _e) {
        RepositoryManager.openExplorer(new File(RequirementTool.getLogFilePath()));
    }
}
