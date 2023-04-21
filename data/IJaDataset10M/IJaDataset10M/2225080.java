package luxor.swing.widget;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *  a collapse button.
 *  modified for verinec
 */
class CollapseButton extends JButton implements ActionListener {

    private CollapsiblePanel _collapsible;

    /** Create a collapse button. 
    * 
    * @param collapsible
    * @param orientation A swing constant SwingConstant.VERTICAL or HORIZONTAL
    */
    public CollapseButton(CollapsiblePanel collapsible, int orientation) {
        super();
        _collapsible = collapsible;
        setFocusPainted(false);
        setDefaultCapable(false);
        setBorder(null);
        setBorderPainted(false);
        setMargin(new Insets(0, 0, 0, 0));
        setMinimumSize(new Dimension(40, 40));
        setSize(new Dimension(40, 40));
        if (orientation == SwingConstants.VERTICAL) {
            setText("-");
            setToolTipText("Collapses Panel");
        } else {
            setText("+");
            setToolTipText("Expands Panel");
        }
        validate();
        addActionListener(this);
    }

    /** Collapse or expand.
    * @param evt ignored
    */
    public void actionPerformed(ActionEvent evt) {
        if (_collapsible.isCollapsed() == true) _collapsible.expand(); else _collapsible.collapse();
    }
}
