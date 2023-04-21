package org.adempiere.webui.component;

import java.io.IOException;
import java.net.URI;
import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.EnvWeb;
import org.compiere.util.Msg;

/**
 *  Application Action.
 *      Creates Action with Button
 *      The ActionCommand is translated for display
 *      If translated text contains &, the next character is the Mnemonic
 *
 *  @author     Andrew Kimball
 */
public class WAppsAction {

    /**
    *
    */
    private static final long serialVersionUID = 1L;

    /**
    *  Application Action
    *
    *  @param   action base action command - used as AD_Message for Text and Icon name
    *  @param   accelerator optional keystroke for accelerator
    *  @param   toggle is toggle action (maintains state)
    */
    public WAppsAction(String action, String accelerator) throws IOException {
        this(action, accelerator, null);
    }

    /**
    *  Application Action.
    *
    *  @param   action base action command - used as AD_Message for Text and Icon name
    *  @param   accelerator optional keystroke for accelerator
    *  @param   toolTipText text, if null defered from action
    *  @param   toggle is toggle action (maintains state)
    */
    public WAppsAction(String action, String accelerator, String toolTipText) throws IOException {
        super();
        String newToolTipText = toolTipText;
        m_action = action;
        if (m_accelerator == null) {
            m_accelerator = "";
        } else {
            m_accelerator = accelerator;
        }
        if (newToolTipText == null) {
            newToolTipText = Msg.getMsg(EnvWeb.getCtx(), action);
        }
        int pos = newToolTipText.indexOf('&');
        if (pos != -1 && newToolTipText.length() > pos) {
            Character ch = new Character(newToolTipText.toLowerCase().charAt(pos + 1));
            if (ch != ' ') {
                newToolTipText = newToolTipText.substring(0, pos) + newToolTipText.substring(pos + 1);
                m_accelerator += "@" + ch;
            }
        }
        URI large = getImage(action, false);
        m_button = new Button();
        m_button.setTooltiptext(newToolTipText);
        m_button.setName("btn" + action);
        m_button.setId(action);
        if (large != null) {
            m_button.setImage(large.getPath());
            m_button.setLabel(null);
        } else {
            m_button.setLabel(newToolTipText);
        }
        LayoutUtils.addSclass("action-button", m_button);
    }

    private Button m_button;

    private String m_action = null;

    private String m_accelerator = null;

    /**
    *  Get Icon with name action
    *  @param name name
    *  @param small small
    *  @return Icon
    */
    private URI getImage(String name, boolean small) throws IOException {
        String fullName = name + (small ? "16" : "24");
        URI uri = AEnv.getImage(fullName + ".png");
        return uri;
    }

    /**
    *  Get Name/ActionCommand
    *  @return ActionName
    */
    public String getName() {
        return m_action;
    }

    /**
    *  Return Button
    *  @return Button
    */
    public Button getButton() {
        return m_button;
    }

    public String getCtrlKeys() {
        return this.m_accelerator;
    }
}
