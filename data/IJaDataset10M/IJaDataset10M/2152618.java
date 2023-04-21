package fr.esrf.tangoatk.widget.util;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.awt.font.FontRenderContext;

public class ThreeStateSwitch extends JComponent implements MouseListener {

    public static final int UNKNOWN_STATE = 0;

    public static final int ON_STATE = 1;

    public static final int OFF_STATE = 2;

    ImageIcon on_state;

    ImageIcon off_state;

    ImageIcon unknown_state;

    Dimension iconSize;

    Dimension titleSize;

    int currentState;

    String title;

    Font titleFont;

    EventListenerList listenerList;

    /**
   * Construct a ThreeStateSwitch with no title and default icons.
   */
    public ThreeStateSwitch() {
        initComponents();
    }

    /**
   * Construct a ThreeStateSwitch with the specified title and font.
   */
    public ThreeStateSwitch(String title, Font tFont) {
        initComponents();
        if (tFont != null) titleFont = tFont;
        setTitle(title);
    }

    /**
   * Sets the title off this components
   * @param s Title to display or null
   */
    public void setTitle(String s) {
        title = s;
        computeTitleSize();
    }

    /**
   * Returns the title of this component
   * @return Component title
   */
    public String getTitle() {
        return title;
    }

    /**
   * Sets the font of this component
   * @param f Font
   */
    public void setFont(Font f) {
        if (f != null) {
            titleFont = f;
            computeTitleSize();
        }
    }

    /**
   * Returns current font
   * @return Font
   */
    public Font getFont() {
        return titleFont;
    }

    /**
   * Sets icons fot the switch.
   * @param on ON icon
   * @param off OFF icon
   * @param unknown UNKNOWN icon
   */
    public void setIcons(ImageIcon on, ImageIcon off, ImageIcon unknown) {
        on_state = on;
        off_state = off;
        unknown_state = unknown;
        iconSize = new Dimension(on_state.getIconWidth(), on_state.getIconHeight());
        computeTitleSize();
    }

    /**
   * Sets the state.
   * @param s One of the following
   * ThreeStateSwitch.UNKNOWN_STATE
   * ThreeStateSwitch.ON_STATE
   * ThreeStateSwitch.OFF_STATE
   */
    public void setState(int s) {
        currentState = s;
        repaint();
    }

    /**
   * Returns the state of this component
   * @return State
   */
    public int getState() {
        return currentState;
    }

    /** Adds the specified action listener 
   *  @param l Listener to add
   */
    public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }

    /** Removes the specified action listener 
   *  @param l Listener to remove
   */
    public void removActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            switch(currentState) {
                case ON_STATE:
                    currentState = OFF_STATE;
                    fireStateChange("OFF");
                    repaint();
                    break;
                case OFF_STATE:
                    currentState = ON_STATE;
                    fireStateChange("ON");
                    repaint();
                    break;
                default:
                    fireStateChange("UNKNOWN");
                    break;
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void paint(Graphics g) {
        Dimension d = getSize();
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, d.width, d.height);
        }
        if (title != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2.setFont(titleFont);
            FontRenderContext frc = g2.getFontRenderContext();
            int fa = (int) Math.ceil(g.getFont().getLineMetrics("ABC", frc).getAscent());
            g.setColor(getForeground());
            g.drawString(title, (d.width - (titleSize.width - 6)) / 2, fa + 2);
        }
        int xOrg = (d.width - iconSize.width) / 2;
        int yOrg = (d.height - (iconSize.height - titleSize.height)) / 2;
        switch(currentState) {
            case ON_STATE:
                g.drawImage(on_state.getImage(), xOrg, yOrg, null);
                break;
            case OFF_STATE:
                g.drawImage(off_state.getImage(), xOrg, yOrg, null);
                break;
            default:
                g.drawImage(unknown_state.getImage(), xOrg, yOrg, null);
                break;
        }
        paintBorder(g);
    }

    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    public Dimension getMinimumSize() {
        return new Dimension(Math.max(iconSize.width, titleSize.width), iconSize.height + titleSize.height);
    }

    private void computeTitleSize() {
        if (title == null) {
            titleSize = new Dimension(0, 0);
            return;
        }
        Dimension d = ATKGraphicsUtils.measureString(title, titleFont);
        d.width += 6;
        d.height += 4;
        titleSize = d;
    }

    private void initComponents() {
        setOpaque(true);
        setLayout(null);
        setBorder(null);
        setBackground(new Color(206, 206, 206));
        setForeground(Color.BLACK);
        on_state = new ImageIcon(getClass().getResource("/fr/esrf/tangoatk/widget/util/switch_on.gif"));
        off_state = new ImageIcon(getClass().getResource("/fr/esrf/tangoatk/widget/util/switch_off.gif"));
        unknown_state = new ImageIcon(getClass().getResource("/fr/esrf/tangoatk/widget/util/switch_unknown.gif"));
        iconSize = new Dimension(on_state.getIconWidth(), on_state.getIconHeight());
        currentState = ON_STATE;
        title = null;
        titleSize = new Dimension(0, 0);
        titleFont = new Font("Dialog", Font.BOLD, 12);
        listenerList = new EventListenerList();
        addMouseListener(this);
    }

    private void fireStateChange(String state) {
        ActionListener[] list = (ActionListener[]) (listenerList.getListeners(ActionListener.class));
        ActionEvent w = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, state);
        for (int i = 0; i < list.length; i++) list[i].actionPerformed(w);
    }
}
