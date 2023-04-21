package audictiv.viewProfile.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Audictiv_viewProfile implements EntryPoint {

    public void onModuleLoad() {
        ViewProfilePanel viewProfilePanel = new ViewProfilePanel(8);
        RootLayoutPanel.get().add(viewProfilePanel);
    }
}
