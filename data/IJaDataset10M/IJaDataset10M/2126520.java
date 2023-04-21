package es.randres.jdo.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Hyperlink;

/**
 * A composite that contains the shortcut stack panel on the left side. The
 * mailbox tree and shortcut lists don't actually do anything, but serve to show
 * how you can construct an interface using
 * {@link com.google.gwt.user.client.ui.StackPanel},
 * {@link com.google.gwt.user.client.ui.Tree}, and other custom widgets.
 */
public class Shortcuts extends ResizeComposite {

    interface Binder extends UiBinder<StackLayoutPanel, Shortcuts> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    /**
   * Constructs a new shortcuts widget using the specified images.
   * 
   * @param images a bundle that provides the images for this widget
   */
    public Shortcuts() {
        initWidget(binder.createAndBindUi(this));
    }
}
