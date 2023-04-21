package be.kuleuven.cs.mop.gui.dialogs;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import be.kuleuven.cs.mop.app.controllers.Controllers;
import be.kuleuven.cs.mop.domain.model.User;
import be.kuleuven.cs.mop.gui.components.ActionButton;
import be.kuleuven.cs.mop.gui.components.SelectionList;
import be.kuleuven.cs.mop.gui.util.SwingUtils;

/**
 * {@link ActionDialog} for the "Log In" use case
 */
public class LogInDialog extends ActionDialog {

    private static final long serialVersionUID = 1L;

    private SelectionList<Entry> list;

    public LogInDialog(final Frame parent, final Controllers controllers) {
        super(parent, "Log In", controllers);
        final GridBagConstraints gbc = SwingUtils.getGBC();
        final List<Entry> entries = new ArrayList<Entry>();
        for (final User user : getControllers().getControllerUsers().getUsers()) entries.add(new Entry(user));
        list = new SelectionList<Entry>(entries, false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(list.wrap(new Dimension(500, 150)), gbc);
        gbc.gridy = 1;
        add(new ActionButton("Log in", new Dimension(150, 32), this, "login"), gbc);
        display();
    }

    @Override
    protected void handle(final ActionEvent event) throws Exception {
        try {
            final User user = list.getSelection().get(0).getUser();
            getControllers().getControllerUsers().setUser(user);
            log("Logged in as '" + user.getName() + "'");
            dispose();
        } catch (final Exception e) {
            log("Please select a (valid) user account");
        }
    }

    private static class Entry {

        private User user;

        private Entry(final User user) {
            setUser(user);
        }

        public User getUser() {
            return user;
        }

        private void setUser(final User user) {
            if (user == null) throw new NullPointerException("User == NULL");
            this.user = user;
        }

        @Override
        public String toString() {
            return String.format("%-24s  (%s)", getUser().getName(), getUser().getType().getName());
        }
    }
}
