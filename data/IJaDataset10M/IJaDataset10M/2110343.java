package com.cubusmail.gwtui.client.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.menu.Menu;
import com.cubusmail.gwtui.client.actions.contact.AddContactFromEmailAddressAction;
import com.cubusmail.gwtui.client.actions.contact.NewMessageToEmailAddressAction;
import com.cubusmail.gwtui.client.model.GWTAddress;
import com.cubusmail.gwtui.client.util.UIFactory;

/**
 * Hyperlink for email addresses.
 * 
 * @author Juergen Schlierf
 */
public class EmailAddressLink extends Composite {

    private Menu contextMenu;

    public EmailAddressLink(GWTAddress address, boolean withSeparator) {
        super();
        FlowPanel panel = new FlowPanel();
        initWidget(panel);
        DOM.setStyleAttribute(panel.getElement(), "whiteSpace", "nowrap");
        DOM.setStyleAttribute(getElement(), "whiteSpace", "nowrap");
        ImageHyperlink link = new ImageHyperlink();
        link.setText(address.getInternetAddress());
        AddContactFromEmailAddressAction addContactAction = new AddContactFromEmailAddressAction();
        addContactAction.setAddress(address);
        NewMessageToEmailAddressAction newMessageAction = new NewMessageToEmailAddressAction();
        newMessageAction.setAddress(address);
        this.contextMenu = new Menu();
        this.contextMenu.addItem(UIFactory.createMenuItem(addContactAction));
        this.contextMenu.addItem(UIFactory.createMenuItem(newMessageAction));
        MouseListenerAdapter listener = new MouseListenerAdapter() {

            @Override
            public void onMouseDown(Widget sender, int x, int y) {
                contextMenu.showAt(sender.getAbsoluteLeft() + x + 10, sender.getAbsoluteTop() + y);
            }
        };
        link.addLeftButtonListener(listener);
        link.addRightButtonListener(listener);
        panel.add(link);
        if (withSeparator) {
            panel.add(new HTML(",&nbsp;&nbsp;"));
        }
    }
}
