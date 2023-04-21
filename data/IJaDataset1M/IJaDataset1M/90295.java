package javab.bling;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javab.ootil.Ootil;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * A BImageButton that maintains an idea of 
 * being one of many possible selections choices
 *  much like a JRadioButton.
 * 
 * @author Brett Geren
 *
 */
public class BImageRadioButton extends JButton implements ActionListener, ItemListener {

    private boolean selected;

    private Border selectedBorder;

    private static final int BACKGROUND_DARKENING_AMOUNT = 15;

    private static final int BORDER_DARKENING_AMOUNT = 50;

    private static final Border DEFAULT_BORDER = BorderFactory.createEmptyBorder(5, 5, 5, 5);

    /**
     * There is a known bug where changing the L&F/UI messes with this button.
     * Just don't change the L&F/UI :P.
     * @param text - the name, actionCommand, and name of the image for the button
     * @param tooltip - the tooltip text for the button
     * @param listener - the actionlistener to use
     */
    public BImageRadioButton(String text, String tooltip, ActionListener listener) {
        this(Ootil.createImageIcon(Ootil.textToVariable(text) + ".png"), text, tooltip, listener);
    }

    /**
     * There is a known bug where changing the L&F/UI messes with this button.
     * Just don't change the L&F/UI :P.
     * @param icon - The icon to use
     * @param text - the name, actionCommand, and name of the button
     * @param tooltip - the tooltip text for the button
     * @param listener - the actionlistener to use
     */
    public BImageRadioButton(ImageIcon icon, String text, String tooltip, ActionListener listener) {
        setUI(new BasicButtonUI());
        setContentAreaFilled(false);
        setRolloverEnabled(new JButton().isRolloverEnabled());
        setName(Ootil.textToVariable(text));
        setActionCommand(getName());
        addActionListener(listener);
        addActionListener(this);
        setToolTipText(tooltip);
        setBorder(DEFAULT_BORDER);
        new BDefaultButtonPressOnEnterAction(this);
        Color background = getBackground();
        setBackground(new Color(Math.max(background.getRed() - BACKGROUND_DARKENING_AMOUNT, 0), Math.max(background.getGreen() - BACKGROUND_DARKENING_AMOUNT, 0), Math.max(background.getBlue() - BACKGROUND_DARKENING_AMOUNT, 0)));
        this.selected = false;
        this.selectedBorder = getSelectedBorder2(this, DEFAULT_BORDER, background);
        getModel().addItemListener(this);
        if (icon != null) {
            setIcon(icon);
            setRolloverIcon(new ImageIcon(Ootil.createRolloverImage((ImageIcon) getIcon())));
            setSelectedIcon(getRolloverIcon());
        }
    }

    private static Border getSelectedBorder(Component c, Border regularBorder) {
        if (regularBorder == null) {
            System.err.println("Border is NULL!");
            return null;
        }
        Insets i = regularBorder.getBorderInsets(c);
        Color shadow = UIManager.getLookAndFeelDefaults().getColor("Button.shadow");
        Color highlight = UIManager.getLookAndFeelDefaults().getColor("Button.highlight");
        return BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(highlight, shadow), BorderFactory.createEmptyBorder(i.top - 2, i.left - 2, i.bottom - 2, i.right - 2));
    }

    private static Border getSelectedBorder2(Component c, Border regularBorder, Color originalBackground) {
        if (regularBorder == null) {
            System.err.println("Border is NULL!");
            return null;
        }
        Insets i = regularBorder.getBorderInsets(c);
        Color borderColor = new Color(Math.max(originalBackground.getRed() - BORDER_DARKENING_AMOUNT, 0), Math.max(originalBackground.getGreen() - BORDER_DARKENING_AMOUNT, 0), Math.max(originalBackground.getBlue() - BORDER_DARKENING_AMOUNT, 0));
        return BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(borderColor), BorderFactory.createEmptyBorder(i.top - 1, i.left - 1, i.bottom - 1, i.right - 1));
    }

    public void setSelected(boolean b) {
        selected = b;
        if (selected) {
            setOpaque(true);
            setBorder(selectedBorder);
        } else {
            setOpaque(false);
            setBorder(DEFAULT_BORDER);
        }
        ButtonGroup g = ((DefaultButtonModel) getModel()).getGroup();
        if (g != null) {
            if (selected) {
                g.setSelected(getModel(), selected);
            } else {
                g.clearSelection();
            }
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void updateUI() {
    }

    public void actionPerformed(ActionEvent e) {
        setSelected(!selected);
    }

    public void itemStateChanged(ItemEvent e) {
        setSelected(((ButtonModel) e.getItem()).isSelected());
    }
}
