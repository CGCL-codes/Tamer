package view.dialog;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import model.MMIngredient;
import model.MMShopPoint;
import model.MMUnit;
import view.MenuMakerGUI;
import view.editor.MMIngredientEditor;
import view.table.MMIngredientTable;

/**
 * @author cmaurice2
 * 
 */
public class MMIngredientDialog extends JDialog {

    /**
	 * Auto-generated SVUID
	 */
    private static final long serialVersionUID = -5241460721152660173L;

    public static final int DEFAULT_WIDTH = 400;

    public static final int DEFAULT_HEIGHT = 400;

    private MenuMakerGUI parent;

    private MMIngredientTable table;

    private MMIngredientEditor ingredientEditor;

    public MMIngredientDialog(MenuMakerGUI parent) {
        super(parent, "Manage ingredients");
        this.parent = parent;
        this.setModal(true);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        buildTable();
        buildButtons();
        this.getContentPane().setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        this.pack();
        this.setLocationRelativeTo(parent);
    }

    private void buildTable() {
        this.table = new MMIngredientTable(this);
        JScrollPane scrollPane = new JScrollPane(table);
        this.getContentPane().add(scrollPane);
        this.repaint();
    }

    private void buildButtons() {
        JButton buttonAdd = new JButton(MenuMakerGUI.ICON_PLUS);
        buttonAdd.setToolTipText("Add a ingredient");
        MouseAdapter addAdapter = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                ingredientEditor = new MMIngredientEditor(MMIngredientDialog.this, null);
                ingredientEditor.setVisible(true);
            }
        };
        buttonAdd.addMouseListener(addAdapter);
        JButton buttonEdit = new JButton(MenuMakerGUI.ICON_EDIT);
        buttonEdit.setToolTipText("Edit ingredient");
        MouseAdapter editAdapter = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                editIngredient();
            }
        };
        buttonEdit.addMouseListener(editAdapter);
        JButton buttonRemove = new JButton(MenuMakerGUI.ICON_MINUS);
        buttonRemove.setToolTipText("Remove selected ingredient(s)");
        MouseAdapter removeAdapter = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                ArrayList<MMIngredient> ingredients = table.getSelectedItems();
                if (!ingredients.isEmpty()) {
                    int retVal = JOptionPane.showConfirmDialog(MMIngredientDialog.this, "All selected ingredients will be deleted. Confirm ?", "Confirm deletion", JOptionPane.YES_NO_OPTION);
                    if (retVal == JOptionPane.OK_OPTION) {
                        for (MMIngredient ingredient : ingredients) {
                            if (parent.canDelete(ingredient)) {
                                parent.removeIngredient(ingredient);
                                table.removeRow(ingredient);
                            } else {
                                JOptionPane.showMessageDialog(MMIngredientDialog.this, "Ingredient \"" + ingredient.getName() + "\" is used for one or more ingredients.", "Can't delete", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        };
        buttonRemove.addMouseListener(removeAdapter);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(buttonAdd);
        buttonPanel.add(buttonEdit);
        buttonPanel.add(buttonRemove);
        this.getContentPane().add(buttonPanel);
    }

    public Collection<MMIngredient> getIngredientList() {
        return parent.getData().getIngredients().values();
    }

    public Collection<MMUnit> getUnitsList() {
        return parent.getData().getUnits().values();
    }

    public Collection<MMShopPoint> getShopPointsList() {
        return parent.getData().getShopPoints().values();
    }

    public void addIngredient(MMIngredient ingredient) {
        parent.addIngredient(ingredient);
        table.addRow(ingredient);
    }

    public void editIngredient() {
        ingredientEditor = new MMIngredientEditor(MMIngredientDialog.this, table.getFirstSelectedItem());
        ingredientEditor.setVisible(true);
    }

    public void removeIngredient(MMIngredient ingredient) {
        parent.removeIngredient(ingredient);
        table.removeRow(ingredient);
    }
}
