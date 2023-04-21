package ghm.follow.gui;

import ghm.follow.FollowApp;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

/**
 * Action which clears the contents of all followed files.
 * 
 * @author <a href="mailto:greghmerrill@yahoo.com">Greg Merrill</a>
 */
public class ClearAll extends FollowAppAction {

    public static final String NAME = "clearAll";

    public ClearAll(FollowApp app) throws IOException {
        super(app, FollowApp.getResourceString("action.ClearAll.name"), FollowApp.getResourceString("action.ClearAll.mnemonic"), FollowApp.getResourceString("action.ClearAll.accelerator"), FollowApp.getIcon(ClearAll.class, "action.ClearAll.icon"), ActionContext.MULTI_FILE);
    }

    public void actionPerformed(ActionEvent e) {
        List<FileFollowingPane> allFileFollowingPanes = getApp().getAllFileFollowingPanes();
        for (FileFollowingPane fileFollowingPane : allFileFollowingPanes) {
            fileFollowingPane.getTextPane().setText("");
        }
    }
}
