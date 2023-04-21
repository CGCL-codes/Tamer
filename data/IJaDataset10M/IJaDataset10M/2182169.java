package net.sf.josas.ui;

import java.util.Observable;
import java.util.Observer;
import javax.swing.ActionMap;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import net.sf.josas.Josas;
import net.sf.josas.model.AssociationManager;
import net.sf.josas.ui.swing.action.AssociationAction;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;

/**
 * Main application menu. In order to simplify unit testing all actions that are
 * linked to menu items are defined in this class leveraging on the Swing
 * Application Framework action mechanism and every action definition invokes
 * the actual action located in a different class. It will be then possible to
 * unit test all action without to invoke the complete SAF action mechanism.
 * @author frederic
 */
public class MainMenu extends JMenuBar implements Observer {

    /** Default serial version ID. */
    private static final long serialVersionUID = 1L;

    /** Help display. */
    private final HelpDisplay helpDisplay;

    /** Association manager. */
    private final AssociationManager aMngr;

    /** Selected association property name. */
    private static final String SELECTED_ASSOCIATION = "selectedAssociation";

    /** No selected association property name. */
    private static final String NO_SELECTED_ASSOCIATION = "noSelectedAssociation";

    /** Association defined property name. */
    private static final String ASSOCIATION_DEFINED = "associationDefined";

    /** Help menu item name. */
    private static final String HELP_MENU_ITEM = "helpContent";

    /** Association creation menu name. */
    public static final String NEW_ASSOCIATION = "createAssociation";

    /** Association opening menu name. */
    public static final String OPEN_ASSOCIATION = "openAssociation";

    /** Association closing menu name. */
    public static final String CLOSE_ASSOCIATION = "closeAssociation";

    /** Data import menu name. */
    public static final String IMPORT = "importData";

    /** Data export menu name. */
    public static final String EXPORT = "exportData";

    /** Database saving menu name. */
    public static final String SAVE = "saveDatabase";

    /** Database restore menu name. */
    public static final String RESTORE = "restoreDatabase";

    /** Print menu name. */
    public static final String PRINT = "print";

    /** Print preview menu name. */
    public static final String PRINT_PREVIEW = "printPreview";

    /** Application exit menu name. */
    public static final String QUIT = "quit";

    /** Member creation menu name. */
    public static final String NEW_MEMBER = "newMember";

    /** Members display menu name. */
    public static final String MEMBERS = "members";

    /** Closed membership display menu name. */
    public static final String CLOSED_MEMBERSHIPS = "closedMembership";

    /** Leads display menu name. */
    public static final String LEADS = "leads";

    /** Subscription management menu name. */
    public static final String SUBSCRIPTIONS = "subscriptions";

    /** Activities management menu name. */
    public static final String ACTIVITIES = "activities";

    /** Subscription payments management menu name. */
    public static final String SUBSCRPTION_PAYMENTS = "subscriptionPayments";

    /** Accounting categories display menu name. */
    public static final String CATEGORIES = "categories";

    /** Accounts display menu name. */
    public static final String ACCOUNTS = "accounts";

    /** Expenses management menu name. */
    public static final String EXPENSES = "expenses";

    /** Revenues management menu name. */
    public static final String REVENUES = "revenues";

    /** Remittances management menu name. */
    public static final String REMITTANCES = "remittances";

    /** Operations management menu name. */
    public static final String OPERATIONS = "operations";

    /** Configuation management menu name. */
    public static final String CONFIGURATION = "configuration";

    /** Processing menu name. */
    public static final String PROCESSING = "processing";

    /** About menu name. */
    public static final String ABOUT = "about";

    /** Separator. */
    private static final String SEPARATOR = "---";

    /** File menu action names. */
    private static final String[] FILE_MENU_ACTION_NAMES = { NEW_ASSOCIATION, OPEN_ASSOCIATION, CLOSE_ASSOCIATION, SEPARATOR, IMPORT, EXPORT, SEPARATOR, PRINT, PRINT_PREVIEW, SEPARATOR, SAVE, RESTORE, SEPARATOR, QUIT };

    /** Membership menu action names. */
    private static final String[] MEMBER_MENU_ACTION_NAMES = { NEW_MEMBER, SEPARATOR, MEMBERS, CLOSED_MEMBERSHIPS, LEADS, SEPARATOR, SUBSCRIPTIONS, ACTIVITIES, SEPARATOR, SUBSCRPTION_PAYMENTS };

    /** Accounting menu action names. */
    private static final String[] ACCOUNTING_MENU_ACTION_NAMES = { CATEGORIES, ACCOUNTS, SEPARATOR, EXPENSES, REVENUES, REMITTANCES, SEPARATOR, OPERATIONS };

    /** Tools menu action names. */
    private static final String[] TOOLS_MENU_ACTION_NAMES = { CONFIGURATION, PROCESSING };

    /** Help menu action names. */
    private static final String[] HELP_MENU_ACTION_NAMES = { HELP_MENU_ITEM, ABOUT };

    /** Indicates if an association is selected. */
    private boolean selectedAssociation;

    /** Indicates if an association is selected. */
    private boolean noSelectedAssociation;

    /** Indicates if an other association can be opened. */
    private boolean associationDefined;

    /** Main application frame. */
    private final JFrame mainFrame;

    /**
     * Constructor.
     * @param aHelpDisplay
     *        help display
     */
    public MainMenu(final HelpDisplay aHelpDisplay) {
        Application app = Application.getInstance();
        if (app instanceof Josas) {
            aMngr = ((Josas) app).getAssociationManager();
            mainFrame = ((SingleFrameApplication) app).getMainFrame();
        } else {
            aMngr = new AssociationManager();
            mainFrame = new JFrame();
        }
        aMngr.addObserver(this);
        setSelectedAssociation(aMngr.getSelectedAssociation() != null);
        computeAssociationDefined();
        helpDisplay = aHelpDisplay;
        add(createMenu("fileMenu", FILE_MENU_ACTION_NAMES));
        add(createMenu("memberMenu", MEMBER_MENU_ACTION_NAMES));
        add(createMenu("accountingMenu", ACCOUNTING_MENU_ACTION_NAMES));
        add(createMenu("toolsMenu", TOOLS_MENU_ACTION_NAMES));
        add(createMenu("helpMenu", HELP_MENU_ACTION_NAMES));
        ApplicationContext ctx = Application.getInstance().getContext();
        ResourceMap rMap = ctx.getResourceMap(MainMenu.class);
        rMap.injectFields(this);
    }

    /**
     * Get an action.
     * @param actionName
     *        action name
     * @return action
     */
    private javax.swing.Action getAction(final String actionName) {
        Application app = Application.getInstance();
        ActionMap aMap = app.getContext().getActionMap(MainMenu.class, this);
        return aMap.get(actionName);
    }

    /**
     * Create a menu.
     * @param name
     *        menu name
     * @param actionNames
     *        name of actions for menu entries
     * @return created menu
     */
    private JMenu createMenu(final String name, final String[] actionNames) {
        JMenu menu = new JMenu();
        menu.setName(name);
        for (String actionName : actionNames) {
            if (actionName.equals(SEPARATOR)) {
                menu.add(new JSeparator());
            } else {
                JMenuItem menuItem = new JMenuItem();
                if (actionName.equals(HELP_MENU_ITEM)) {
                    menuItem.addActionListener(helpDisplay);
                }
                menuItem.setName(actionName);
                menuItem.setAction(getAction(actionName));
                menuItem.setIcon(null);
                menu.add(menuItem);
            }
        }
        return menu;
    }

    /**
     * Action for creating a new association.
     */
    @Action
    public final void createAssociation() {
        AssociationAction.createAssociation(aMngr, mainFrame, helpDisplay);
    }

    /**
     * Action for opening an existing association. Note that this action is
     * disabled if no association than the currently selected association has
     * been recorded yet.
     */
    @Action(enabledProperty = ASSOCIATION_DEFINED)
    public final void openAssociation() {
        AssociationAction.openAssociation(aMngr, mainFrame, helpDisplay);
    }

    /**
     * @return the associationDefined
     */
    public final boolean isAssociationDefined() {
        return associationDefined;
    }

    /**
     * Determines if an other association can be opened. Update the
     * corresponding property.
     */
    public final void computeAssociationDefined() {
        boolean value = ((aMngr.getSelectedAssociation() == null) && (aMngr.getAssociationList().size() > 0)) || ((aMngr.getSelectedAssociation() != null) && (aMngr.getAssociationList().size() > 1));
        setAssociationDefined(value);
    }

    /**
     * @param value
     *        the associationDefined to set
     */
    public final void setAssociationDefined(final boolean value) {
        boolean old = associationDefined;
        this.associationDefined = value;
        firePropertyChange(ASSOCIATION_DEFINED, old, value);
    }

    /**
     * Action for closing the selected association. Note that this action is
     * disabled if no association is currently selected.
     */
    @Action(enabledProperty = SELECTED_ASSOCIATION)
    public final void closeAssociation() {
        AssociationAction.closeAssociation(aMngr, mainFrame, helpDisplay);
    }

    /**
     * Action for importing data into the selected association. Note that this
     * action is disabled if no association is currently selected.
     */
    @Action(enabledProperty = SELECTED_ASSOCIATION)
    public final void importData() {
        AssociationAction.importData(aMngr, mainFrame, helpDisplay);
    }

    /**
     * Action for exporting data from the selected association. Note that this
     * action is disabled if no association is currently selected.
     */
    @Action(enabledProperty = SELECTED_ASSOCIATION)
    public final void exportData() {
        AssociationAction.exportData(aMngr, mainFrame, helpDisplay);
    }

    /**
     * Action for saving the database content.
     */
    @Action
    public final void saveDatabase() {
        AssociationAction.saveDatabase(mainFrame, helpDisplay);
    }

    /**
     * Action for restoring the database content. In order to ensure data
     * consistency, this action is disabled if an association is currently
     * selected.
     */
    @Action(enabledProperty = NO_SELECTED_ASSOCIATION)
    public final void restoreDatabase() {
        AssociationAction.restoreDatabase(mainFrame, helpDisplay);
    }

    /**
     * Action for printing data from the selected association. Note that this
     * action is disabled if no association is currently selected.
     */
    @Action(enabledProperty = SELECTED_ASSOCIATION)
    public final void print() {
        AssociationAction.print(aMngr, mainFrame, helpDisplay);
    }

    /**
     * Action for opening the print preview dialog box.
     */
    @Action(enabledProperty = SELECTED_ASSOCIATION)
    public final void printPreview() {
        AssociationAction.printPreview(aMngr, mainFrame, helpDisplay);
    }

    /**
     * New member action.
     */
    @Action(enabledProperty = SELECTED_ASSOCIATION)
    public final void newMember() {
        AssociationAction.newMember(aMngr.getMembershipManager(), mainFrame, helpDisplay);
    }

    /**
     * Members action.
     */
    @Action(enabledProperty = SELECTED_ASSOCIATION)
    public final void members() {
        AssociationAction.members(mainFrame, helpDisplay);
    }

    /**
     * Categories action.
     */
    @Action(enabledProperty = SELECTED_ASSOCIATION)
    public final void categories() {
    }

    /**
     * Configuration action.
     */
    @Action
    public final void configuration() {
        AssociationAction.configuration(mainFrame, helpDisplay);
    }

    /**
     * Help content action.
     */
    @Action
    public final void helpContent() {
        AssociationAction.members(mainFrame, helpDisplay);
    }

    /**
     * Checks if an association is currently selected.
     * @return true if an association is selected.
     */
    public final boolean isSelectedAssociation() {
        return selectedAssociation;
    }

    /**
     * Checks if no association is currently selected.
     * @return true if no association is selected.
     */
    public final boolean isNoSelectedAssociation() {
        return noSelectedAssociation;
    }

    /**
     * @param value
     *        value to set
     */
    private void setSelectedAssociation(final boolean value) {
        boolean old = selectedAssociation;
        selectedAssociation = value;
        noSelectedAssociation = !value;
        firePropertyChange(SELECTED_ASSOCIATION, old, value);
        firePropertyChange(NO_SELECTED_ASSOCIATION, !old, !value);
    }

    /**
     * @param name
     *        association to select
     */
    public final void selectAssociation(final String name) {
        aMngr.selectAssociation(name);
        setSelectedAssociation(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void update(final Observable o, final Object arg) {
        computeAssociationDefined();
        setSelectedAssociation(aMngr.getSelectedAssociation() != null);
    }
}
