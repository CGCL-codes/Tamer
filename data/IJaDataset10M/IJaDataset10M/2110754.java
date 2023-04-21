package org.gwanted.gwt.widget.disclosurepanel.client.ui;

import java.util.HashMap;
import java.util.Map;
import org.gwanted.gwt.core.client.behaviours.AbstractCommandAction;
import org.gwanted.gwt.core.client.behaviours.CommandAction;
import org.gwanted.gwt.core.client.behaviours.HasCommand;
import org.gwanted.gwt.core.client.behaviours.Hide;
import org.gwanted.gwt.core.client.behaviours.Show;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget that consists of a header and a content panel that discloses the
 * content when a user clicks on the header.
 *
 * @author Joshua Hewitt aka Sposh
 *
 */
public class DisclosurePanel extends Composite implements HasCommand {

    private final boolean isOpen;

    private com.google.gwt.user.client.ui.DisclosurePanel discPanel;

    private final Map commandActions = new HashMap();

    public DisclosurePanel() {
        this(false);
    }

    public DisclosurePanel(final boolean isOpen) {
        super();
        this.isOpen = isOpen;
        initWidget();
    }

    protected final void initWidget() {
        setupComposite();
        setupCommandActions();
    }

    private void setupComposite() {
        this.discPanel = new com.google.gwt.user.client.ui.DisclosurePanel((Widget) null, this.isOpen);
        initWidget(this.discPanel);
    }

    private void setupCommandActions() {
        setCurrentCommandAction(Show.class, new CommandAction(getMainWidget(), Show.getInstance(), null));
        setCurrentCommandAction(Hide.class, new CommandAction(getMainWidget(), Hide.getInstance(), null));
    }

    public final void setCurrentCommandAction(final Class type, final AbstractCommandAction commandAction) {
        this.commandActions.put(type, commandAction);
    }

    public final AbstractCommandAction getCurrentCommandAction(final Class type) {
        return (AbstractCommandAction) this.commandActions.get(type);
    }

    protected final Widget getMainWidget() {
        return this.discPanel;
    }

    /**
     * Show ("open") disclosure panel's content body and change header style.
     */
    public final void show() {
        final AbstractCommandAction commandAction = getCurrentCommandAction(Show.class);
        if (commandAction != null) {
            commandAction.execute();
        }
    }

    /**
     * Hide ("close") disclosure panel's content body and change header style.
     */
    public final void hide() {
        final AbstractCommandAction commandAction = getCurrentCommandAction(Hide.class);
        if (commandAction != null) {
            commandAction.execute();
        }
    }

    /**
     * @param content The panel's main content body
     */
    public final void setContent(final Widget content) {
        this.discPanel.setContent(content);
    }

    /**
     * @param header The panel's title header
     */
    public final void setHeader(final Widget header) {
        this.discPanel.setHeader(header);
    }

    /**
     * @return The panel's title header
     */
    public final Widget getHeader() {
        return this.discPanel.getHeader();
    }
}
