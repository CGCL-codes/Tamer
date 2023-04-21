package org.mbari.vars.knowledgebase.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import org.mbari.swing.SearchableComboBoxModel;

/**
 * <p>
 * A ComboBox coupled with a TextField to allow searching the contents of
 * the ComboBox model for specified Strings.
 * </p>
 *
 * @author $Author: hohonuuli $
 * @version $Revision: 94 $
 */
public class SearchableComboBoxPanel extends JPanel {

    /**
     * Default size of the text box.
     */
    private static final int DEFAULT_TEXT_SIZE = 8;

    /**
     * This panel's combo box.
     */
    private JComboBox comboBox;

    /**
     * The model backing this panel's combo box.
     */
    private SearchableComboBoxModel model;

    /**
     * This panel's search button.
     */
    private JButton searchButton;

    /**
     * This panel's text box.
     */
    private JTextField searchText;

    /**
     * Constructs ...
     *
     */
    public SearchableComboBoxPanel() {
        super();
        model = new SearchableComboBoxModel();
        comboBox = new JComboBox(model);
        searchText = new JTextField(DEFAULT_TEXT_SIZE);
        searchButton = new JButton("Search");
        comboBox.setEditable(false);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets.right = 3;
        add(comboBox, gbc);
        gbc.weightx = 0.0;
        add(searchText, gbc);
        gbc.insets.right = 0;
        add(searchButton, gbc);
        searchText.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                performModelSearch();
            }
        });
        searchButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                performModelSearch();
            }
        });
    }

    /**
     * Constructs a <code>ConceptNameComboBox</code> initially populated by the
     * specified array of <code>ConceptName</code> items.
     *
     * @param items
     */
    public SearchableComboBoxPanel(String[] items) {
        this();
        updateModel(items);
    }

    /**
     * Adds the specified <code>ConceptName</code> item to the model for this
     * <code>ConceptNameComboBox</code>.
     *
     * @param item
     *  The item to add to the model.
     */
    public void addItem(Object item) {
        model.addElement(item);
    }

    /**
     * Adds an item listener to this panel's combo box.
     *
     * @param listener
     */
    public void addItemListener(ItemListener listener) {
        comboBox.addItemListener(listener);
    }

    /**
     * Gets the current selected combo box item.
     *
     * @return
     */
    public Object getSelectedItem() {
        return model.getSelectedItem();
    }

    /**
     * Performs a model search using the current contents of the text box. If a
     * new match is found, that item is selected; o/w the user gets a beep.
     */
    protected void performModelSearch() {
        String text = searchText.getText().trim();
        Object selectedItem = model.getSelectedItem();
        int index = model.searchForItemContaining(text, true);
        if (-1 < index) {
            Object foundItem = model.getElementAt(index);
            if (!foundItem.equals(selectedItem)) {
                model.setSelectedItem(foundItem);
            } else {
                getToolkit().beep();
            }
        } else {
            getToolkit().beep();
        }
    }

    /**
     * Clears the combo box model.
     */
    public void removeAllItems() {
        model.clear();
    }

    /**
     * Removes the specified <code>ConceptName</code> item from the model.
     *
     * @param item
     *  The item to remove from the model.
     */
    public void removeItem(Object item) {
        model.removeElement(item);
    }

    /**
     * Sets the renderer of this panel's combo box.
     *
     * @param renderer
     */
    public void setRenderer(ListCellRenderer renderer) {
        comboBox.setRenderer(renderer);
    }

    /**
     * Sets the current selected combo box item.
     *
     * @param item
     */
    public void setSelectedItem(Object item) {
        model.setSelectedItem(item);
    }

    /**
     * Updates the model to contain the specified items.
     *
     * @param items
     *  An array of <code>ConceptName</code> items for this
     *  <code>ConceptNameComboBox</code>.
     */
    public void updateModel(String[] items) {
        model.setItems(Arrays.asList(items));
    }
}
