package org.mov.ui;

import java.awt.Point;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.mov.main.CommandManager;
import org.mov.prefs.PreferencesManager;
import org.mov.prefs.PreferencesModule;
import org.mov.prefs.StoredEquation;
import org.mov.util.Locale;

/**
 * Extension of JComboBox used for displaying an editable equation field
 * in the venice application. This ComboBox allows the user to select
 * equations entered in the Functions preferences page. This way the user
 * does not have to keep typing in the same equations multiple times.
 *
 * <pre>
 *	EquationComboBox comboBox = new EquationComboBox();
 *	panel.add(comboBox);
 * </pre>
 */
public class EquationComboBox extends JComboBox implements PopupMenuListener {

    static List storedEquations;

    private boolean isDialogUp = false;

    private JTextField textField;

    /**
     * Create a new equation combo box.
     */
    public EquationComboBox() {
        this("");
    }

    /**
     * Create a new equation combo box with the given default equation
     * text.
     *
     * @param	equationText	equation text to display
     */
    public EquationComboBox(String equationText) {
        super();
        setEditable(true);
        if (storedEquations == null) updateEquations();
        setEquationText(equationText);
        addPopupMenuListener(this);
        for (int i = 0; i < getComponentCount(); i++) {
            Component component = getComponent(i);
            if (component instanceof JTextField) textField = (JTextField) component;
        }
        textField.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent event) {
                handleMouseClicked(event);
            }
        });
        updateItems();
    }

    /**
     * Return the equation string in the ComboBox. If a name of an
     * equation is in the box then its equation will be returned.
     *
     * @return	equation string
     */
    public String getEquationText() {
        String text = getText();
        StoredEquation storedEquation = findStoredEquationByName(text);
        if (storedEquation != null) return storedEquation.equation; else return text;
    }

    /**
     * Return whether the current displayed equation is a stored equation.
     * A stored equation is one the user has entered and can refer
     * to by using a keyword.
     *
     * @return <code>true</code> if it is a stored equation.
     */
    public boolean isStoredEquation() {
        return findStoredEquationByName(getText()) != null;
    }

    /**
     * Set the equation string in the ComboBox. If the given equation
     * has a name, then the name will be displayed in the comboBox
     * instead of the equation.
     *
     * @param	equationText	equation text to display
     */
    public void setEquationText(String equationText) {
        StoredEquation storedEquation = findStoredEquationByEquation(equationText);
        if (storedEquation != null) setSelectedItem(storedEquation.name); else setSelectedItem(equationText);
    }

    /**
     * Tell all equation ComboBoxes that the stored equations have
     * been modified by the user and that their popup menus need to be
     * changed.
     */
    public static void updateEquations() {
        storedEquations = PreferencesManager.loadStoredEquations();
    }

    private StoredEquation findStoredEquationByName(String name) {
        for (Iterator iterator = storedEquations.iterator(); iterator.hasNext(); ) {
            StoredEquation storedEquation = (StoredEquation) iterator.next();
            if (storedEquation.name.equals(name)) return storedEquation;
        }
        return null;
    }

    private StoredEquation findStoredEquationByEquation(String equation) {
        for (Iterator iterator = storedEquations.iterator(); iterator.hasNext(); ) {
            StoredEquation storedEquation = (StoredEquation) iterator.next();
            if (storedEquation.equation.equals(equation)) return storedEquation;
        }
        return null;
    }

    private void updateItems() {
        List menuItems = new ArrayList();
        String current = getText();
        menuItems.add(current);
        List stored = new ArrayList();
        for (Iterator iterator = storedEquations.iterator(); iterator.hasNext(); ) {
            StoredEquation storedEquation = (StoredEquation) iterator.next();
            if (!storedEquation.name.equals(current)) stored.add(storedEquation.name);
        }
        Collections.sort(stored);
        menuItems.addAll(stored);
        removeAllItems();
        for (Iterator iterator = menuItems.iterator(); iterator.hasNext(); ) addItem((String) iterator.next());
    }

    public void popupMenuCanceled(PopupMenuEvent e) {
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        updateItems();
    }

    private void showAddDialog() {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                if (!isDialogUp) {
                    isDialogUp = true;
                    StoredEquation storedEquation = ExpressionEditorDialog.showAddDialog(storedEquations, Locale.getString("ADD_EQUATION"), getText());
                    if (storedEquation != null) {
                        setEquationText(storedEquation.name);
                        storedEquations.add(storedEquation);
                        PreferencesManager.saveStoredEquations(storedEquations);
                    }
                    isDialogUp = false;
                }
            }
        });
        thread.start();
    }

    private void showDeleteDialog() {
        if (!isDialogUp) {
            isDialogUp = true;
            StoredEquation storedEquation = findStoredEquationByName(getText());
            if (storedEquation != null) {
                int option = JOptionPane.showInternalConfirmDialog(DesktopManager.getDesktop(), Locale.getString("SURE_DELETE_EQUATION", getText()), Locale.getString("DELETE_EQUATION"), JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    storedEquations.remove(storedEquation);
                    PreferencesManager.saveStoredEquations(storedEquations);
                    setEquationText("");
                }
            }
            isDialogUp = false;
        }
    }

    private void showEditDialog() {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                if (!isDialogUp) {
                    isDialogUp = true;
                    if (isStoredEquation()) {
                        StoredEquation storedEquation = findStoredEquationByName(getText());
                        if (storedEquation != null) {
                            storedEquation = ExpressionEditorDialog.showEditDialog(storedEquations, Locale.getString("EDIT_EQUATION"), storedEquation);
                            setEquationText(storedEquation.equation);
                            PreferencesManager.saveStoredEquations(storedEquations);
                        }
                    } else {
                        String equationText = getEquationText();
                        String newEquationText = ExpressionEditorDialog.showEditDialog(Locale.getString("EDIT_EQUATION"), equationText);
                        setEquationText(newEquationText);
                    }
                    isDialogUp = false;
                }
            }
        });
        thread.start();
    }

    private String getText() {
        return textField.getText();
    }

    private void handleMouseClicked(final MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON3) {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem editMenuItem = new JMenuItem(Locale.getString("EDIT"));
            editMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    showEditDialog();
                }
            });
            menu.add(editMenuItem);
            boolean isStoredEquation = isStoredEquation();
            JMenuItem addMenuItem = new JMenuItem(Locale.getString("ADD"));
            addMenuItem.setEnabled(!isStoredEquation);
            addMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    showAddDialog();
                }
            });
            menu.add(addMenuItem);
            JMenuItem deleteMenuItem = new JMenuItem(Locale.getString("DELETE"));
            deleteMenuItem.setEnabled(isStoredEquation);
            deleteMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    showDeleteDialog();
                }
            });
            menu.add(deleteMenuItem);
            menu.addSeparator();
            JMenuItem manageMenuItem = new JMenuItem(Locale.getString("MANAGE"));
            manageMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    CommandManager commandManager = CommandManager.getInstance();
                    commandManager.openPreferences(PreferencesModule.EQUATION_PAGE);
                }
            });
            menu.add(manageMenuItem);
            Point point = event.getPoint();
            menu.show(this, point.x, point.y);
        }
    }
}
