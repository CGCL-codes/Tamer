package foa.properties.flowElements;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import foa.attributes.*;

/**
 * @author Emiliano Grigis
 * @version 0.0.1
 */
public class UpdateAction extends AbstractAction {

    private AttributeDirector attributeDirector;

    private Container c;

    private JDialog getName;

    public UpdateAction(Container frame, AttributeDirector attributeDirector) {
        super("Update", null);
        this.attributeDirector = attributeDirector;
        c = frame;
    }

    public void actionPerformed(ActionEvent e) {
        if (attributeDirector.isANewSet()) {
            getName = new GetNameDialog(c, attributeDirector);
            if (attributeDirector.isAValidName()) {
                JOptionPane optionPane = new JOptionPane();
                while (attributeDirector.existsAnAttributeSetWithCurrentName() || (attributeDirector.getAttributeSetName()).length() == 0) {
                    optionPane.showMessageDialog(c, "An Attribute Set Name must be different from all the others !", "Invalid Attribute Set Name", JOptionPane.ERROR_MESSAGE);
                    getName = new GetNameDialog(c, attributeDirector);
                }
                attributeDirector.createAttributeSet();
                ((Window) c).dispose();
            }
        } else if (attributeDirector.isANewVariant()) {
            getName = new GetNameDialog(c, attributeDirector);
            if (attributeDirector.isAValidName()) {
                JOptionPane optionPane = new JOptionPane();
                while (attributeDirector.existsAnAttributeSetWithCurrentName() || (attributeDirector.getAttributeSetName()).length() == 0) {
                    optionPane.showMessageDialog(c, "An Attribute Set Name must be different from all the others !", "Invalid Attribute Set Name", JOptionPane.ERROR_MESSAGE);
                    getName = new GetNameDialog(c, attributeDirector);
                }
                attributeDirector.createAttributeSetVariant();
                ((Window) c).dispose();
            }
        } else {
            attributeDirector.updateAttributesValues();
            ((Window) c).dispose();
        }
    }
}
