package a03.swing.plaf;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.UIResource;

public class A03ArrowButton extends JButton implements UIResource {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3715017185739912842L;

    protected int direction;

    private A03ArrowButtonDelegate delegate;

    private Border border;

    public A03ArrowButton(int direction, A03ArrowButtonDelegate delegate) {
        this.direction = direction;
        this.delegate = delegate;
        this.border = A03BorderFactory.createArrowButtonDelegatedBorder(delegate);
    }

    @Override
    public void setUI(ButtonUI ui) {
        super.setUI(ui);
    }

    @Override
    public Border getBorder() {
        return border;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Dimension getPreferredSize() {
        return new Dimension(16, 16);
    }

    public Dimension getMinimumSize() {
        return new Dimension(5, 5);
    }

    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public boolean isFocusTraversable() {
        return false;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        Container parent = getParent();
        if (parent instanceof JComboBox) {
            A03ComboBoxDelegate comboBoxDelegate = (A03ComboBoxDelegate) A03SwingUtilities.getDelegate((JComponent) parent, UIManager.get("ComboBox.delegate"));
            comboBoxDelegate.paintArrowBackground(this, graphics);
        }
        paintBorder(graphics);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        delegate.paintArrow(this, graphics, direction);
        graphics.dispose();
    }
}
