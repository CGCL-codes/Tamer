package freetm.client.ui.users;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.*;
import gwtm.client.services.users.User;
import gwtm.client.services.users.UsersStatics;
import java.util.Date;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.widget.WidgetContainer;
import net.mygwt.ui.client.widget.TabItem;
import net.mygwt.ui.client.widget.layout.FillLayout;

/**
 *
 * @author dedalos
 */
public class TabItemLogin extends TabItem {

    private DlgLogin m_dlgLogin;

    private TextBox m_textUsername;

    private PasswordTextBox m_textPassword;

    private CheckBox m_checkAutoLogin;

    /**
   * Creates a new instance of TabItemLogin
   */
    public TabItemLogin(DlgLogin dlgLogin) {
        super(Style.NONE);
        m_dlgLogin = dlgLogin;
        setText("Log-In");
        WidgetContainer container = getContainer();
        DockPanel dockPanel = new DockPanel();
        dockPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        container.add(dockPanel);
        container.setLayout(new FillLayout(8));
        Grid grid = new Grid(3, 2);
        dockPanel.add(grid, DockPanel.CENTER);
        String sBool = Cookies.getCookie(UsersStatics.COOKIE_AUTOLOGIN);
        boolean bAutoLogin = Boolean.valueOf(sBool).booleanValue();
        grid.setWidget(0, 0, new Label("Username"));
        m_textUsername = new TextBox();
        m_textUsername.addKeyboardListener(new KeyboardListenerAdapter() {

            public void onKeyPress(Widget sender, char keyCode, int modifiers) {
                if (keyCode == KeyboardListener.KEY_ENTER) m_dlgLogin.login();
            }
        });
        grid.setWidget(0, 1, m_textUsername);
        if (bAutoLogin) m_textUsername.setText(Cookies.getCookie(UsersStatics.COOKIE_USERNAME));
        grid.setWidget(1, 0, new Label("Password"));
        m_textPassword = new PasswordTextBox();
        m_textPassword.addKeyboardListener(new KeyboardListenerAdapter() {

            public void onKeyPress(Widget sender, char keyCode, int modifiers) {
                if (keyCode == KeyboardListener.KEY_ENTER) m_dlgLogin.login();
            }
        });
        grid.setWidget(1, 1, m_textPassword);
        if (bAutoLogin) m_textPassword.setText(Cookies.getCookie(UsersStatics.COOKIE_PASSWORD));
        m_checkAutoLogin = new CheckBox();
        m_checkAutoLogin.setHTML("Remember username / password <br>and login automatically next time");
        m_checkAutoLogin.setChecked(bAutoLogin);
        grid.setWidget(2, 1, m_checkAutoLogin);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) m_dlgLogin.setOkText("Log-In");
    }

    public String getUsername() {
        return m_textUsername.getText();
    }

    public String getPassword() {
        return m_textPassword.getText();
    }

    public void saveCookies() {
        Date d = new Date();
        d.setYear(d.getYear() + 1);
        boolean bAutoLogin = m_checkAutoLogin.isChecked();
        Cookies.setCookie(UsersStatics.COOKIE_AUTOLOGIN, Boolean.toString(bAutoLogin), d);
        String username = "";
        String password = "";
        if (bAutoLogin) {
            username = m_textUsername.getText();
            password = m_textPassword.getText();
        }
        Cookies.setCookie(UsersStatics.COOKIE_USERNAME, username, d);
        Cookies.setCookie(UsersStatics.COOKIE_PASSWORD, password, d);
    }
}
