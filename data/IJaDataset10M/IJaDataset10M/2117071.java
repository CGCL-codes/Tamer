package ti.plato.logcontrol.profiles;

import ti.plato.logcontrol.profiles.views.ProfileContentProvider;
import ti.plato.logcontrol.views.LogControlView;

public class ImportProfileMenuAction extends ProfileMenuAction {

    public ImportProfileMenuAction(ProfilePageController pageController, String id, String text, String tooltip, String iconName) {
        super(pageController, id, text, tooltip, iconName);
    }

    public void run() {
        String profileName = LogControlView.getDefault().importProfile();
        if (profileName.length() > 0) {
            ((ProfileContentProvider) pageController.getContentProvider()).addProfile(profileName);
        }
    }

    @Override
    public void setEnabledState() {
        setEnabled(true);
    }
}
