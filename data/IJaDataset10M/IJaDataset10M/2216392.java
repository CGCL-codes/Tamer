package webirc.client.gui.dialogs.panels;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import webirc.client.WebIRC;
import webirc.client.gui.Popups;

/**
 * @author Ayzen
 * @version 1.0 30.07.2006 23:37:48
 */
public class NicknamePanel extends HorizontalPanel {

    private TextBox tbNick = new TextBox();

    public NicknamePanel() {
        setWidth("100%");
        tbNick.setVisibleLength(15);
        Label label = new Label(WebIRC.dialogMessages.nickname());
        add(label);
        add(tbNick);
        setCellHorizontalAlignment(label, VerticalPanel.ALIGN_LEFT);
        setCellHorizontalAlignment(tbNick, VerticalPanel.ALIGN_RIGHT);
    }

    public boolean validate() {
        if (tbNick.getText().trim().length() == 0) {
            Popups.showErrorMessage(WebIRC.dialogMessages.emptyNickname(), tbNick.getAbsoluteLeft(), tbNick.getAbsoluteTop() + tbNick.getOffsetHeight());
            return false;
        }
        return true;
    }

    public TextBox getTbNick() {
        return tbNick;
    }

    public String getNick() {
        return tbNick.getText();
    }

    public void setNick(String nick) {
        tbNick.setText(nick);
    }
}
