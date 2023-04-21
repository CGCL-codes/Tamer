package grammarbrowser.browser.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 * <p>
 * A balloon tip which can be displayed
 * <ul>
 * <li>left-aligned above</li>
 * <li>left-aligned below</li>
 * <li>right-aligned above</li>
 * <li>right-aligned below</li>
 * </ul>
 * the attached component. See enumeration <code>BalloonTip.Alignment</code>.
 * </p>
 * <p>
 * The balloon tip uses a <code>JTextPane</code> to render its contents which allows use of HTML code.
 * </p>
 * <p>
 * Create a balloon tip by using the static <code>create</code> methods. You can choose between two looks:
 * </p>
 * 
 * @author Bernhard Pauler
 * @author Bernard Bou
 */
public class BalloonTip extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Alignment enumeration
     */
    public enum Alignment {

        LEFT_ALIGNED_ABOVE, RIGHT_ALIGNED_ABOVE, LEFT_ALIGNED_BELOW, RIGHT_ALIGNED_BELOW
    }

    ;

    /**
     * Popup window
     */
    private Popup thePopup;

    /**
     * Balloon's external attached component
     */
    private final Component theAttachedComponent;

    /**
     * Balloon's internal text component
     */
    private final JTextComponent theTextComponent;

    /**
     * Balloon's internal main component
     */
    private final JComponent theComponent;

    /**
     * Balloon's alignment
     */
    private final Alignment theAlignment;

    /**
     * Horizontal position offset
     */
    private final int theHorizontalOffset;

    /**
     * Vertical position offset
     */
    private final int theVerticalOffset;

    /**
     * Close icon
     */
    private static final Icon theDefaultCloseIcon = new ImageIcon(BalloonTip.class.getResource("/grammarbrowser/browser/images/close_default.png"));

    /**
     * Rollover close icon
     */
    private static final Icon theRolloverCloseIcon = new ImageIcon(BalloonTip.class.getResource("/grammarbrowser/browser/images/close_rollover.png"));

    /**
     * Pressed close icon
     */
    private static final Icon thePressedCloseIcon = new ImageIcon(BalloonTip.class.getResource("/grammarbrowser/browser/images/close_pressed.png"));

    /**
     * Constructor
     * 
     * @param thisTextComponent
     *                internal text component
     * @param thisAttachedComponent
     *                external attached component
     * @param thisAlignment
     *                alignment
     * @param thisHorizontalOffset
     *                horizontal offset
     * @param thisVerticalOffset
     *                vertical offset
     * @param useCloseButton
     *                whether to use close button
     */
    public BalloonTip(JTextPane thisTextComponent, Component thisAttachedComponent, Alignment thisAlignment, int thisHorizontalOffset, int thisVerticalOffset, boolean useCloseButton) {
        theTextComponent = thisTextComponent;
        theAttachedComponent = thisAttachedComponent;
        theAlignment = thisAlignment;
        theHorizontalOffset = thisHorizontalOffset;
        theVerticalOffset = thisVerticalOffset;
        setBorder(BorderFactory.createLineBorder(Color.black));
        setOpaque(false);
        setLayout(new GridBagLayout());
        theTextComponent.setEditable(false);
        theTextComponent.setBackground(new Color(200, 255, 200));
        theComponent = new JScrollPane(theTextComponent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(theComponent, new GridBagConstraints(0, 0, 1, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
        if (useCloseButton) {
            JButton thisCloseButton = new JButton();
            thisCloseButton.setBorder(null);
            thisCloseButton.setContentAreaFilled(false);
            thisCloseButton.setIcon(theDefaultCloseIcon);
            thisCloseButton.setRolloverIcon(theRolloverCloseIcon);
            thisCloseButton.setPressedIcon(thePressedCloseIcon);
            thisCloseButton.setFocusable(false);
            thisCloseButton.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 1));
            thisCloseButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                }
            });
            add(thisCloseButton, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        }
        Icon thisIcon = new ImageIcon(BalloonTip.class.getResource("/grammarbrowser/browser/images/relations-small.png"));
        JLabel thisLabel = new JLabel();
        thisLabel.setIcon(thisIcon);
        add(thisLabel, new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        thisAttachedComponent.addHierarchyBoundsListener(new HierarchyBoundsAdapter() {

            public void ancestorMoved(HierarchyEvent e) {
                if (isShowing()) {
                    setVisible(false);
                }
            }
        });
        theComponent.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setVisible(false);
    }

    /**
     * Compute location
     * 
     * @return location to place balloon
     */
    private Point getWhere() {
        Point theAttachedComponentLocation = SwingUtilities.convertPoint(theAttachedComponent, getLocation(), this);
        int x = 0, y = 0;
        switch(theAlignment) {
            case LEFT_ALIGNED_ABOVE:
                x = theAttachedComponentLocation.x;
                y = theAttachedComponentLocation.y - getPreferredSize().height;
                break;
            case LEFT_ALIGNED_BELOW:
                x = theAttachedComponentLocation.x;
                y = theAttachedComponentLocation.y + theAttachedComponent.getHeight();
                break;
            case RIGHT_ALIGNED_ABOVE:
                x = theAttachedComponentLocation.x + theAttachedComponent.getWidth() - getPreferredSize().width;
                y = theAttachedComponentLocation.y - getPreferredSize().height;
                break;
            case RIGHT_ALIGNED_BELOW:
                x = theAttachedComponentLocation.x + theAttachedComponent.getWidth() - getPreferredSize().width;
                y = theAttachedComponentLocation.y + theAttachedComponent.getHeight();
                break;
        }
        return new Point(x + theHorizontalOffset, y + theVerticalOffset);
    }

    /**
     * Calculate preferred size
     * 
     * @return text component's preferred size
     */
    private Dimension calcPreferredSize() {
        Dimension thisDimension = theAttachedComponent.getSize();
        thisDimension.width -= 18;
        thisDimension.height = Integer.MAX_VALUE;
        theTextComponent.setSize(thisDimension);
        thisDimension.height = theTextComponent.getPreferredSize().height + 10;
        return thisDimension;
    }

    /**
     * Set text
     * 
     * @param thisText
     *                text
     */
    public void setText(String thisText) {
        theTextComponent.setText(thisText);
    }

    public synchronized void setVisible(boolean show) {
        if (show) {
            theComponent.setPreferredSize(calcPreferredSize());
            Point where = getWhere();
            PopupFactory thisFactory = PopupFactory.getSharedInstance();
            thePopup = thisFactory.getPopup(theAttachedComponent, this, where.x, where.y);
            thePopup.show();
        } else {
            if (thePopup != null) thePopup.hide();
            thePopup = null;
            theTextComponent.setText("");
        }
    }
}
