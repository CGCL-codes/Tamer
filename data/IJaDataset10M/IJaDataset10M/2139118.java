package foa.attributes;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Emiliano Grigis
 * @version 0.0.1
 */
public class NewVariantAttributeSetAction extends AbstractAction {

    private Container c;

    private String file;

    private String className;

    private String originalAttributeSet;

    public NewVariantAttributeSetAction(Container c, String file, String className, String attributeSet) {
        super("New Variant of Attribute Set...", null);
        this.c = c;
        this.file = file;
        this.className = className;
        this.originalAttributeSet = attributeSet;
    }

    public void actionPerformed(ActionEvent e) {
        ((AttributeFrame) c).getAttributeDirector().newAttributeSetVariant(file, className, originalAttributeSet);
    }
}
