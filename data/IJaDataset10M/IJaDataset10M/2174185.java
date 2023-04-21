package userInterface.Settings;

import javax.servlet.http.HttpSession;
import utils.Constant;
import utils.MessageConstant;
import com.itmill.toolkit.service.ApplicationContext;
import com.itmill.toolkit.terminal.gwt.server.WebApplicationContext;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.CheckBox;
import com.itmill.toolkit.ui.CustomComponent;
import com.itmill.toolkit.ui.CustomLayout;
import com.itmill.toolkit.ui.Label;
import com.itmill.toolkit.ui.Panel;
import com.itmill.toolkit.ui.Window;
import com.itmill.toolkit.ui.Button.ClickEvent;
import database.UserControl;

public class GeneralSettings extends CustomComponent implements Button.ClickListener, Constant, MessageConstant {

    private Window main;

    private CustomLayout generalSettingsLayout = new CustomLayout("generalSettingsLayout");

    private Panel generalSettingsPanel = new Panel("General Settings");

    private Button btApply = new Button("Apply", this);

    private Button btCancel = new Button("Cancel", this);

    private Button btDefault = new Button("Defaults", this);

    private Label lbEmailForwading = new Label("When people send me messages");

    private Label lbMsgsPerPage = new Label("");

    private CheckBox btEmailForwading = new CheckBox("Also send to my private email", this);

    public ApplicationContext ctx;

    public WebApplicationContext webCtx;

    public HttpSession session;

    public String username;

    public GeneralSettings(Window mainWindow) {
        main = mainWindow;
        ctx = main.getApplication().getContext();
        webCtx = (WebApplicationContext) ctx;
        session = webCtx.getHttpSession();
        username = (String) session.getAttribute("UserName");
        btApply.setEnabled(false);
        btEmailForwading.setImmediate(true);
        lbMsgsPerPage.setValue("Messages shown per page");
        generalSettingsLayout.addComponent(btApply, "btApply");
        generalSettingsLayout.addComponent(btCancel, "btCancel");
        generalSettingsLayout.addComponent(btDefault, "btDefault");
        btEmailForwading.setValue(UserControl.getForwadEmailStatus(username));
        generalSettingsLayout.addComponent(lbEmailForwading, "lbEmailForwading");
        generalSettingsLayout.addComponent(btEmailForwading, "btEmailForwading");
        generalSettingsPanel.addComponent(generalSettingsLayout);
        setCompositionRoot(generalSettingsPanel);
    }

    public void buttonClick(ClickEvent event) {
        if (event.getSource() == btApply) {
            UserControl.setAllowEmailForwading(username, (Boolean) btEmailForwading.getValue());
            btApply.setEnabled(false);
        } else if (event.getSource() == btEmailForwading) {
            btApply.setEnabled(true);
        }
    }
}
