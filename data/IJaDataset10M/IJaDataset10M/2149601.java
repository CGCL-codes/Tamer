package ghm.followgui;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
Action which clears the contents of all followed files.
@author <a href="mailto:greghmerrill@yahoo.com">Greg Merrill</a>
*/
class ClearAll extends FollowAppAction {

    ClearAll(final FollowApp app) throws IOException {
        super(app, app.resBundle_.getString("action.ClearAll.name"), app.resBundle_.getString("action.ClearAll.mnemonic"), app.resBundle_.getString("action.ClearAll.accelerator"), app.resBundle_.getString("action.ClearAll.icon"));
    }

    public void actionPerformed(final ActionEvent e) {
        final List allFileFollowingPanes = app_.getAllFileFollowingPanes();
        final Iterator i = allFileFollowingPanes.iterator();
        FileFollowingPane fileFollowingPane;
        while (i.hasNext()) {
            fileFollowingPane = (FileFollowingPane) i.next();
            fileFollowingPane.getTextArea().setText("");
        }
    }
}
