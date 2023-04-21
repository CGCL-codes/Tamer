package net.sourceforge.jcv.controller.members.preferences;

import com.salmonllc.html.events.PageEvent;
import com.salmonllc.html.events.SubmitEvent;
import net.sourceforge.jcv.controller.members.MembersBaseController;
import net.sourceforge.jcv.model.UserModel;
import net.sourceforge.jcv.util.PropsManager;

public class ExportPreferencesController extends MembersBaseController {

    public com.salmonllc.html.HtmlSubmitButton _cmdSave;

    public com.salmonllc.jsp.JspDisplayBox _dboxPreferences;

    public void memberInitialize() throws Exception {
        addPageListener(this);
        _cmdSave.addSubmitListener(this);
        _cmdSave.setAccessKey(PropsManager.getLangValue(PROP_BUTTON_SAVE_ACCESSKEY, this));
    }

    public boolean memberSubmitPerformed(SubmitEvent e) throws Exception {
        if (e.getSource() == _cmdSave) {
            updatePreferences();
        }
        return true;
    }

    public void memberPageRequested(PageEvent p) throws Exception {
    }

    /**
     * Called after the save of the export preferences display box has been pressed.
     *
     * @throws Exception
     */
    private void updatePreferences() throws Exception {
        _dsResume.update();
    }
}
