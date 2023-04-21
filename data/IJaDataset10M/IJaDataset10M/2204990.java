package es.eucm.eadventure.editor.gui.editdialogs.effectdialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.controllers.EffectsController;

public class TriggerBookEffectDialog extends EffectDialog {

    /**
     * Required.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Combo box with the books.
     */
    private JComboBox booksComboBox;

    /**
     * Constructor.
     * 
     * @param currentProperties
     *            Set of initial values
     */
    public TriggerBookEffectDialog(HashMap<Integer, Object> currentProperties) {
        super(TC.get("TriggerBookEffect.Title"), false);
        String[] booksArray = controller.getIdentifierSummary().getBookIds();
        if (booksArray.length > 0) {
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            mainPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5), BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), TC.get("TriggerBookEffect.Description"))));
            c.insets = new Insets(2, 4, 4, 4);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            booksComboBox = new JComboBox(booksArray);
            mainPanel.add(booksComboBox, c);
            add(mainPanel, BorderLayout.CENTER);
            if (currentProperties != null) {
                if (currentProperties.containsKey(EffectsController.EFFECT_PROPERTY_TARGET)) booksComboBox.setSelectedItem(currentProperties.get(EffectsController.EFFECT_PROPERTY_TARGET));
            }
            setResizable(false);
            setSize(240, 140);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
            setVisible(true);
        } else controller.showErrorDialog(getTitle(), TC.get("TriggerBookEffect.ErrorNoBooks"));
    }

    @Override
    protected void pressedOKButton() {
        properties = new HashMap<Integer, Object>();
        properties.put(EffectsController.EFFECT_PROPERTY_TARGET, booksComboBox.getSelectedItem().toString());
    }
}
