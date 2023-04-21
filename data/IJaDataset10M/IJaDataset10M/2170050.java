package be.kuleuven.cs.mop.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * The global menu for a <code>{@link UserInterface}</code>
 */
public class MenuBar extends JMenuBar {

    private static final long serialVersionUID = 1L;

    private static final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 14);

    protected MenuBar(final ActionListener listener) {
        setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 15, 0, 15);
        add(new Menu("Projects", new Item("Create Project", listener, "CreateProject"), new Item("Remove Project", listener, "RemoveProject")), gbc);
        add(new Menu("Resources", new Item("Create Resource", listener, "CreateResource"), new Item("Make Resource Reservation for Task", listener, "MakeResourceReservationForTask")), gbc);
        add(new Menu("System", new Item("Adjust Clock Value", listener, "AdjustClockValue")));
        add(new Menu("Tasks", new Item("Create Task", listener, "CreateTask"), new Item("Focus Work", listener, "FocusWork"), new Item("Modify Task Details", listener, "ModifyTaskDetails"), new Item("Remove Task", listener, "RemoveTask"), new Item("Update Task Status", listener, "UpdateTaskStatus")), gbc);
        add(new Menu("Users", new Item("Accept/Decline Helper Invitation", listener, "AcceptDeclineHelperInvitation"), new Item("Create User", listener, "CreateUser"), new Item("Log In", listener, "LogIn"), new Item("Manage Helper Invitations", listener, "ManageHelperInvitations")), gbc);
    }

    @Override
    public Dimension getPreferredSize() {
        final Dimension dim = super.getPreferredSize();
        dim.height = 32;
        return dim;
    }

    /**
	 * Represents a menu option in a <code>Menu</code>
	 */
    private static final class Item extends JMenuItem {

        private static final long serialVersionUID = 1L;

        private Item(final String label, final ActionListener listener, final String command) {
            super(label);
            addActionListener(listener);
            setActionCommand(command);
            setFocusable(false);
            setFont(FONT);
        }
    }

    /**
	 * Represents a sub-menu in the <code>MenuBar</code>
	 */
    private static final class Menu extends JMenu {

        private static final long serialVersionUID = 1L;

        private Menu(final String title, final Item... items) {
            super(title);
            setFocusable(false);
            setFont(FONT);
            for (final Item item : items) add(item);
        }
    }
}
