package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.tigris.gef.util.ResourceLoader;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 *   This class implements a panel that adds a navigation button
 *   to the right of the combo box
 *
 *   @author Curt Arnold
 *   @since 0.9
 */
public class UMLComboBoxNavigator extends JPanel implements ActionListener {

    private static ImageIcon _icon = ResourceLoader.lookupIconResource("ComboNav");

    private UMLUserInterfaceContainer _container;

    private JComboBox _box;

    /**
    *  Constructor
    *  @param container Container, typically a PropPanel
    *  @param tooltip Tooltip key for button
    *  @param box Associated combo box
    */
    public UMLComboBoxNavigator(UMLUserInterfaceContainer container, String tooltip, JComboBox box) {
        super(new BorderLayout());
        JButton button = new JButton(_icon);
        _container = container;
        _box = box;
        button.setPreferredSize(new Dimension(_icon.getIconWidth() + 6, _icon.getIconHeight() + 6));
        button.setToolTipText(container.localize(tooltip));
        button.addActionListener(this);
        add(box, BorderLayout.CENTER);
        add(button, BorderLayout.EAST);
    }

    /**
     *  Fired when the button is pushed.  Navigates to the currently
     *  selected item in the combo box
     */
    public void actionPerformed(final java.awt.event.ActionEvent event) {
        Object item = _box.getSelectedItem();
        if (item instanceof UMLComboBoxEntry) {
            UMLComboBoxEntry entry = (UMLComboBoxEntry) item;
            if (!entry.isPhantom()) {
                MModelElement target = entry.getElement(null);
                if (target != null) {
                    _container.navigateTo(target);
                }
            }
        }
    }
}
