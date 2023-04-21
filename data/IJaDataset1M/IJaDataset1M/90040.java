package view.editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import model.MMExtra;
import model.MMIngredient;
import view.MenuMakerGUI;
import view.combobox.MMAutoCompleteComboBox;

/**
 * @author cmaurice2
 * 
 */
public class MMExtraEditor extends JDialog {

    /**
	 * Auto-generated SVUID
	 */
    private static final long serialVersionUID = 4070524773506940562L;

    public static final int DEFAULT_WIDTH = 270;

    public static final int DEFAULT_HEIGHT = 125;

    public static final int DEFAULT_UNIT_LABEL_WIDTH = 50;

    public static final int DEFAULT_UNIT_LABEL_HEIGHT = MenuMakerGUI.DEFAULT_FIELD_HEIGHT;

    private MenuMakerGUI parent;

    private MMAutoCompleteComboBox ingredientComboBox;

    private JSpinner quantitySpinner;

    public MMExtraEditor(MenuMakerGUI parent) {
        super(parent, "Edit extra");
        this.parent = parent;
        this.setModal(true);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        buildInputs();
        buildButtons();
        this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        this.setModal(true);
        this.pack();
        this.setLocationRelativeTo(parent);
    }

    private void buildInputs() {
        Dimension fieldsDim = new Dimension(MenuMakerGUI.DEFAULT_FIELD_WIDTH, MenuMakerGUI.DEFAULT_FIELD_HEIGHT);
        JLabel ingredientLabel = new JLabel("Ingredient");
        ingredientLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        MMIngredient[] ingredients = new MMIngredient[parent.getData().getIngredients().size()];
        parent.getData().getIngredients().values().toArray(ingredients);
        Arrays.sort(ingredients);
        ingredientComboBox = new MMAutoCompleteComboBox(ingredients);
        ingredientComboBox.getComboBox().setAlignmentX(Component.RIGHT_ALIGNMENT);
        ingredientComboBox.getComboBox().setPreferredSize(fieldsDim);
        ingredientComboBox.getComboBox().setMaximumSize(fieldsDim);
        JPanel ingredientPanel = new JPanel();
        ingredientPanel.setLayout(new BoxLayout(ingredientPanel, BoxLayout.LINE_AXIS));
        ingredientPanel.add(ingredientLabel);
        ingredientPanel.add(Box.createHorizontalGlue());
        ingredientPanel.add(ingredientComboBox.getComboBox());
        JLabel quantityLabel = new JLabel("Quantity");
        quantityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        quantitySpinner = new JSpinner(new SpinnerNumberModel(0.0d, 0.0d, Double.MAX_VALUE, 1.0d));
        quantitySpinner.setAlignmentX(Component.RIGHT_ALIGNMENT);
        Dimension spinnerDim = new Dimension(MenuMakerGUI.DEFAULT_FIELD_WIDTH - DEFAULT_UNIT_LABEL_WIDTH, MenuMakerGUI.DEFAULT_FIELD_HEIGHT);
        quantitySpinner.setPreferredSize(spinnerDim);
        quantitySpinner.setMaximumSize(spinnerDim);
        final JLabel unitLabel = new JLabel("  ");
        if (ingredientComboBox.getComboBox().getSelectedItem() != null) {
            MMIngredient ingredient = (MMIngredient) ingredientComboBox.getComboBox().getSelectedItem();
            unitLabel.setText(ingredient.getUnit().toString());
        }
        unitLabel.setHorizontalAlignment(JLabel.CENTER);
        ItemListener ingredientListener = new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (ingredientComboBox.getComboBox().getSelectedItem() != null) {
                    MMIngredient ingredient = (MMIngredient) ingredientComboBox.getComboBox().getSelectedItem();
                    unitLabel.setText(ingredient.getUnit().toString());
                }
            }
        };
        ingredientComboBox.getComboBox().addItemListener(ingredientListener);
        Dimension unitDim = new Dimension(DEFAULT_UNIT_LABEL_WIDTH, DEFAULT_UNIT_LABEL_HEIGHT);
        unitLabel.setPreferredSize(unitDim);
        unitLabel.setMaximumSize(unitDim);
        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new BoxLayout(quantityPanel, BoxLayout.LINE_AXIS));
        quantityPanel.add(quantityLabel);
        quantityPanel.add(Box.createHorizontalGlue());
        quantityPanel.add(quantitySpinner);
        quantityPanel.add(unitLabel);
        JPanel inputsPanel = new JPanel();
        inputsPanel.setLayout(new BoxLayout(inputsPanel, BoxLayout.PAGE_AXIS));
        inputsPanel.add(ingredientPanel);
        inputsPanel.add(quantityPanel);
        this.getContentPane().add(inputsPanel);
    }

    private void buildButtons() {
        JButton buttonOK = new JButton();
        buttonOK.setIcon(MenuMakerGUI.ICON_OK);
        MouseAdapter okAdapter = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (areInputsValid()) {
                    MMExtra element = new MMExtra((MMIngredient) ingredientComboBox.getComboBox().getSelectedItem(), (Double) quantitySpinner.getValue(), new String(""));
                    parent.addExtra(element);
                    setVisible(false);
                }
            }
        };
        buttonOK.addMouseListener(okAdapter);
        JButton buttonCancel = new JButton();
        buttonCancel.setIcon(MenuMakerGUI.ICON_CANCEL);
        MouseAdapter cancelAdapter = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                setVisible(false);
            }
        };
        buttonCancel.addMouseListener(cancelAdapter);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(buttonOK);
        buttonPanel.add(buttonCancel);
        this.getContentPane().add(buttonPanel);
    }

    public boolean areInputsValid() {
        if (ingredientComboBox.getComboBox().getSelectedItem() == null) {
            JOptionPane.showMessageDialog(MMExtraEditor.this, "Please select an ingredient", "Inputs invalid", JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (((Double) quantitySpinner.getValue()) <= 0) {
            JOptionPane.showMessageDialog(MMExtraEditor.this, "Please set a quantity", "Inputs invalid", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            return true;
        }
    }
}
