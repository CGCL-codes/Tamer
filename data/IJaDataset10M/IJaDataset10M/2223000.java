package mn.more.wits.client;

import com.google.gwt.user.client.ui.*;
import mn.more.wits.client.dto.UserDTO;
import mn.more.wits.client.gui.WitsButton;
import mn.more.wits.client.util.FormatUtil;
import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.widget.ContentPanel;
import net.mygwt.ui.client.widget.layout.CenterLayout;
import net.mygwt.ui.client.widget.layout.TableLayout;
import net.mygwt.ui.client.widget.layout.TableLayoutData;

/**
 * @author $Author: mikeliucc $
 * @version $Revision: 119 $
 */
public class UserProfile extends ContentPanel {

    private static final AppMsg APP_MSG = AppMsg.Self.get();

    private static final String STYLE_USER_PROFILE_VALUE = "user-profile-value";

    private Label lblUserName = new Label(APP_MSG.username(), false);

    private Label lblFullName = new Label(APP_MSG.fullname(), false);

    private Label lblLoginDate = new Label(APP_MSG.logindate(), false);

    private Label lblRole = new Label(APP_MSG.role(), false);

    private Label userName;

    private Label fullName;

    private Label loginDate;

    private Label role;

    private WitsButton lnkChangePass = new WitsButton(APP_MSG.changePasswordText());

    UserProfile up;

    public UserProfile() {
        super(Style.HEADER | Style.COLLAPSE);
        setFrame(false);
        setText("User Profile");
        setStyleName("pn-user-profile");
        up = this;
        TableLayout layout = new TableLayout(6);
        layout.setCellSpacing(0);
        layout.setCellPadding(5);
        setLayout(layout);
        initData();
        TableLayoutData layoutData = new TableLayoutData();
        layoutData.setAlign("left");
        layoutData.setVAlign("middle");
        layoutData.setHeight("35px");
        TableLayoutData layoutData1 = new TableLayoutData();
        layoutData1.setWidth("20px");
        layoutData1.setHeight("35px");
        add(lblUserName, layoutData);
        add(userName, layoutData);
        add(AppContext.createSpacer(), layoutData1);
        add(lblFullName, layoutData);
        add(fullName, layoutData);
        add(lnkChangePass, layoutData);
        add(lblLoginDate, layoutData);
        add(loginDate, layoutData);
        add(AppContext.createSpacer(), layoutData1);
        add(lblRole, layoutData);
        add(role, layoutData);
        add(AppContext.createSpacer(), layoutData1);
        lnkChangePass.addListener(Events.Click, new Listener() {

            public void handleEvent(BaseEvent be) {
                AppContext.showLoading();
                MainPanel main = AppContext.getMainPanel();
                main.removeAll();
                main.setLayout(new CenterLayout());
                ChangePasswordPanel cpp = new ChangePasswordPanel();
                cpp.setWidth(300);
                cpp.setHeight(150);
                main.add(cpp);
                main.layout(true);
                AppContext.hideLoading();
            }
        });
    }

    private void initData() {
        UserDTO user = AppContext.getUser();
        userName = new Label(user.getUserName(), false);
        fullName = new Label(user.getDisplayName(), false);
        loginDate = new Label(FormatUtil.formatLongDate(user.getLoginDate()), false);
        role = new Label(user.getMainRole(), false);
        lblUserName.setStyleName(AppContext.DEFAULT_STYLE_LABEL);
        lblFullName.setStyleName(AppContext.DEFAULT_STYLE_LABEL);
        lblRole.setStyleName(AppContext.DEFAULT_STYLE_LABEL);
        lblLoginDate.setStyleName(AppContext.DEFAULT_STYLE_LABEL);
        userName.setStyleName(AppContext.DEFAULT_STYLE_RO_VALUE);
        fullName.setStyleName(AppContext.DEFAULT_STYLE_RO_VALUE);
        loginDate.setStyleName(AppContext.DEFAULT_STYLE_RO_VALUE);
        role.setStyleName(AppContext.DEFAULT_STYLE_RO_VALUE);
        userName.addStyleName(STYLE_USER_PROFILE_VALUE);
        fullName.addStyleName(STYLE_USER_PROFILE_VALUE);
        loginDate.addStyleName(STYLE_USER_PROFILE_VALUE);
        role.addStyleName(STYLE_USER_PROFILE_VALUE);
    }
}
