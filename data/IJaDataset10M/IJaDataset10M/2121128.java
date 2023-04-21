package com.phamola.examples.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The top panel, which contains the 'welcome' message and various links.
 */
public class TopPanel extends Composite implements ClickListener {

    /**
   * An image bundle for this widgets images.
   */
    public interface Images extends ImageBundle {

        AbstractImagePrototype logo();
    }

    private HTML signOutLink = new HTML("<a href='javascript:;'>Sign Out</a>");

    private HTML aboutLink = new HTML("<a href='javascript:;'>About</a>");

    public TopPanel(Images images) {
        HorizontalPanel outer = new HorizontalPanel();
        VerticalPanel inner = new VerticalPanel();
        outer.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
        inner.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
        HorizontalPanel links = new HorizontalPanel();
        links.setSpacing(4);
        links.add(signOutLink);
        links.add(aboutLink);
        final Image logo = images.logo().createImage();
        outer.add(logo);
        outer.setCellHorizontalAlignment(logo, HorizontalPanel.ALIGN_LEFT);
        outer.add(inner);
        inner.add(new HTML("<b>Welcome back, foo@example.com</b>"));
        inner.add(links);
        signOutLink.addClickListener(this);
        aboutLink.addClickListener(this);
        initWidget(outer);
        setStyleName("mail-TopPanel");
        links.setStyleName("mail-TopPanelLinks");
    }

    public void onClick(Widget sender) {
        if (sender == signOutLink) {
            Window.alert("If this were implemented, you would be signed out now.");
        } else if (sender == aboutLink) {
            AboutDialog dlg = new AboutDialog();
            dlg.show();
            dlg.center();
        }
    }
}
