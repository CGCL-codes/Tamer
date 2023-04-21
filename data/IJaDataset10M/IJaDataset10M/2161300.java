package org.shalma.presentation.client.service.security;

import org.shalma.presentation.client.ui.image.ShalmaImageBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.shalma-LoginPanel</li>
 * </ul>
 */
public class LoginUI extends PopupPanel implements KeyboardListener, AsyncCallback {

    private TextBox name;

    private PasswordTextBox password;

    private FlexTable table;

    public LoginUI() {
        super(true);
        table = new FlexTable();
        table.setCellSpacing(2);
        setWidget(table);
        setStyleName("shalma-LoginPanel");
        table.setWidget(0, 0, new HTML("Usuario"));
        name = new TextBox();
        name.addKeyboardListener(this);
        table.setWidget(0, 1, name);
        name.setFocus(true);
        table.setWidget(1, 0, new HTML("Contraseña"));
        password = new PasswordTextBox();
        password.addKeyboardListener(this);
        table.setWidget(1, 1, password);
        table.getFlexCellFormatter().setColSpan(2, 0, 2);
        table.getFlexCellFormatter().setAlignment(2, 0, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);
    }

    void setInfoWidget(Widget widget) {
        table.setWidget(2, 0, widget);
    }

    public void onSuccess(Object result) {
        if (result == null) setInfoWidget(new HTML("Usuario desconocido")); else {
            Security.setUser((User) result);
            hide();
        }
    }

    public void onFailure(Throwable caught) {
        setInfoWidget(new HTML(caught.getMessage()));
    }

    public void onKeyDown(Widget sender, char keyCode, int modifiers) {
    }

    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        if (keyCode != KEY_ENTER) return;
        setInfoWidget(ShalmaImageBundle.INSTANCE.Wait().createImage());
        security.login(name.getText(), password.getName(), this);
    }

    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
    }

    private static SecurityServiceAsync security;

    {
        security = (SecurityServiceAsync) GWT.create(SecurityService.class);
        ((ServiceDefTarget) security).setServiceEntryPoint(GWT.getModuleBaseURL() + "os/Security");
    }
}
