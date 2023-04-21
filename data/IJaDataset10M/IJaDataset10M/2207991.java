package com.rapidminer.gui.tools.components;

import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.Action;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import com.rapidminer.gui.tools.ExtendedHTMLJEditorPane;
import com.rapidminer.gui.tools.ResourceAction;
import com.rapidminer.tools.Tools;

/** Can be used as a label that triggers an action event on every link activation click. 
 *  The {@link Action#NAME} property of the action will be the label text. Note that
 *  it must contain a &lt;a&gt;> tag for this class to do something useful. The
 *  icon property of the action is not interpreted. 
 * 
 * @author Simon Fischer
 *
 */
public class LinkButton extends ExtendedHTMLJEditorPane {

    private static final long serialVersionUID = 1L;

    public LinkButton(final Action action) {
        this(action, false);
    }

    public LinkButton(final Action action, boolean addLinkTag) {
        super("text/html", makeHTML(action, addLinkTag));
        setToolTipText((String) action.getValue(Action.SHORT_DESCRIPTION));
        installDefaultStylesheet();
        setEditable(false);
        setOpaque(false);
        addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == EventType.ACTIVATED) {
                    action.actionPerformed(new ActionEvent(LinkButton.this, ActionEvent.ACTION_PERFORMED, e.getDescription()));
                }
            }
        });
    }

    private static String makeHTML(final Action action, boolean addLinkTag) {
        String html = (String) action.getValue(Action.NAME);
        if (addLinkTag) {
            if (action instanceof ResourceAction) {
                String iconName = ((ResourceAction) action).getIconName();
                if (iconName != null) {
                    URL iconUrl = Tools.getResource("icons/16/" + iconName);
                    if (iconUrl != null) {
                        html = "<img src=\"" + iconUrl.toString() + "\" border=\"0\" style=\"border:none;vertical-align:middle;\"/>&nbsp;" + html;
                    }
                }
            }
            html = "<a href=\"#\">" + html + "</a>";
        }
        return html;
    }
}
