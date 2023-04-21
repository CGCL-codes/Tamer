package fr.itris.glips.svgeditor.properties;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * @author ITRIS, Jordi SUC
 */
public class SVGPropertiesValidatedEntryWidget extends SVGPropertiesWidget {

    /**
     * the constructor of the class
     * @param propertyItem a property item
     */
    public SVGPropertiesValidatedEntryWidget(SVGPropertyItem propertyItem) {
        super(propertyItem);
        buildComponent();
    }

    /**
	 * builds the component that will be displayed
	 */
    protected void buildComponent() {
        final ResourceBundle bundle = ResourcesManager.bundle;
        final JTextField textField = new JTextField(propertyItem.getGeneralPropertyValue(), 8);
        textField.moveCaretPosition(0);
        final KeyAdapter keyListener = new KeyAdapter() {

            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    propertyItem.changePropertyValue(textField.getText());
                }
            }
        };
        textField.addKeyListener(keyListener);
        final JButton okButton = new JButton();
        Insets buttonInsets = new Insets(1, 1, 1, 1);
        okButton.setMargin(buttonInsets);
        if (bundle != null) {
            try {
                okButton.setText(bundle.getString("labelok"));
            } catch (Exception ex) {
            }
        }
        final ActionListener listener = new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                propertyItem.changePropertyValue(textField.getText());
            }
        };
        okButton.addActionListener(listener);
        JPanel validatedPanel = new JPanel();
        validatedPanel.setLayout(new BorderLayout());
        validatedPanel.add(textField, BorderLayout.CENTER);
        validatedPanel.add(okButton, BorderLayout.EAST);
        component = validatedPanel;
        disposer = new Runnable() {

            public void run() {
                textField.removeKeyListener(keyListener);
                okButton.removeActionListener(listener);
            }
        };
    }
}
