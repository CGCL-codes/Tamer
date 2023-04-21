package net.turingcomplete.phosphor.shared;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.datatransfer.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.net.*;
import java.beans.*;
import java.lang.reflect.*;
import java.util.*;
import java.io.*;
import java.text.*;

/**
 * Helper class for Swing and AWT components
 * <p>
 * REVISION HISTORY:
 * <p>
 */
public class GUIHelpers {

    public static final int MINUS_SIGN = new DecimalFormatSymbols().getMinusSign();

    public static void center(Container parent, Component comp) {
        int x, y;
        Rectangle parentBounds;
        Dimension compSize = comp.getSize();
        if ((parent == null) || (parent.getBounds().width < compSize.width) || (parent.getBounds().height < compSize.height)) {
            parentBounds = new Rectangle(comp.getToolkit().getScreenSize());
            parentBounds.setLocation(0, 0);
        } else {
            parentBounds = parent.getBounds();
        }
        x = parentBounds.x + ((parentBounds.width / 2) - (compSize.width / 2));
        y = parentBounds.y + ((parentBounds.height / 2) - (compSize.height / 2));
        comp.setLocation(x, y);
    }

    /** resizes the component to a certain percentage of the screen
	 * @param perWidth from 0.0 to 1.0
	 */
    public static void setSize(Component comp, float perWidth, float perHeight) {
        comp.setSize(new Dimension((int) (comp.getToolkit().getScreenSize().width * perWidth), (int) (comp.getToolkit().getScreenSize().height * perWidth)));
    }

    /**
     * This static method queries the system to find out what Pluggable
     * Look-and-Feel (PLAF) implementations are available.  Then it creates a
     * JMenu component that lists each of the implementations by name and
     * allows the user to select one of them using JRadioButtonMenuItem
     * components.  When the user selects one, the selected menu item
     * traverses the component hierarchy and tells all components to use the
     * new PLAF.
	 * Also sets the L&F to the system default
     **/
    public static JMenu createPlafMenu(final JFrame frame, final LimitOptions options) {
        JMenu plafmenu = new JMenu("Look and Feel");
        ButtonGroup radiogroup = new ButtonGroup();
        UIManager.LookAndFeelInfo[] plafs = UIManager.getInstalledLookAndFeels();
        for (int i = 0; i < plafs.length; i++) {
            try {
                final LookAndFeel plaf = (LookAndFeel) Class.forName(plafs[i].getClassName()).newInstance();
                if (plaf.isSupportedLookAndFeel()) {
                    JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(plafs[i].getName());
                    JMenuItem item = plafmenu.add(menuItem);
                    if (options.getLookAndFeelClassName().equals(plafs[i].getClassName())) {
                        setLookAndFeel(frame, plaf, options);
                        menuItem.setSelected(true);
                    }
                    item.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            setLookAndFeel(frame, plaf, options);
                        }
                    });
                    radiogroup.add(item);
                }
            } catch (Exception e) {
                Trace.display(e);
            }
        }
        SwingUtilities.updateComponentTreeUI(plafmenu);
        return plafmenu;
    }

    public static void setLookAndFeel(Frame frame, LookAndFeel plaf, final LimitOptions options) {
        try {
            Dimension oldSize = frame.getSize();
            UIManager.setLookAndFeel(plaf);
            SwingUtilities.updateComponentTreeUI(frame);
            redraw(frame, oldSize);
            options.setLookAndFeelClassName(plaf.getClass().getName());
        } catch (UnsupportedLookAndFeelException e) {
            Trace.display(e);
        }
    }

    public static void redraw(Window frame, Dimension size) {
        frame.pack();
        frame.setSize(size);
        frame.doLayout();
        frame.repaint();
    }

    public static void redraw(JInternalFrame frame, Dimension size) {
        frame.pack();
        frame.setSize(size);
        frame.doLayout();
        frame.repaint();
    }

    public static void setLookAndFeel(Frame frame, LimitOptions options) {
        try {
            setLookAndFeel(frame, (LookAndFeel) Class.forName(options.getLookAndFeelClassName()).newInstance(), options);
        } catch (Exception e) {
            Trace.display(e);
        }
    }

    /** places internal frame, and adds it to the pane, so they don't hide one another, and that don't go off screen*/
    public static void placeFrame(JDesktopPane pane, JInternalFrame frame) {
        final int offsetInc = 50;
        final int numFrames = 5;
        int maxWidthOffset = Math.max(0, pane.getWidth() - offsetInc);
        int maxHeightOffset = Math.max(0, pane.getHeight() - offsetInc - 50);
        int widthOffset = Math.min((pane.getAllFrames().length % numFrames) * offsetInc, maxWidthOffset);
        int heightOffset = Math.min((pane.getAllFrames().length % numFrames) * offsetInc, maxHeightOffset);
        frame.setLocation(widthOffset, heightOffset);
        frame.setSize(pane.getWidth() - widthOffset, pane.getHeight() - heightOffset);
        frame.setVisible(true);
        pane.add(frame);
    }

    /** places all internal frames so they don't hide one another, and that don't go off screen */
    public static void placeFrames(JDesktopPane pane) {
        int paneWidth = Math.max(pane.getWidth(), 50);
        int paneHeight = Math.max(pane.getHeight(), 40);
        final int offsetInc = 35;
        final int numFrames = 5;
        int maxWidthOffset = Math.max(0, paneWidth - offsetInc);
        int maxHeightOffset = Math.max(0, paneHeight - offsetInc);
        JInternalFrame[] frames = pane.getAllFrames();
        Arrays.sort(frames, new Comparator() {

            public int compare(Object o1, Object o2) {
                FrameOutOfDateIndicator a = (FrameOutOfDateIndicator) o1;
                FrameOutOfDateIndicator b = (FrameOutOfDateIndicator) o2;
                if (a.lastUpdated() < b.lastUpdated()) return -1;
                if (a.lastUpdated() > b.lastUpdated()) return 1;
                return 0;
            }
        });
        for (int i = 0; i < frames.length; ++i) {
            int widthOffset = Math.min((i % numFrames) * offsetInc, maxWidthOffset);
            int heightOffset = Math.min((i % numFrames) * offsetInc, maxHeightOffset);
            frames[i].setLocation(widthOffset, heightOffset);
            frames[i].setSize(paneWidth - widthOffset, paneHeight - heightOffset);
            try {
                frames[i].setIcon(false);
                frames[i].setSelected(true);
            } catch (PropertyVetoException e) {
                Trace.display(e, "Cannot select/uniconify frame.");
            }
        }
    }

    /** values are inclusive 
	 * isEmptyAllowed true means that if the string is empty it will be left alone, as opposed to being set to min
	 */
    public static void makeNumericInput(final JTextComponent txtNumber, final long min, final long max, boolean isEmptyAllowed) {
        makeNumericInput(txtNumber, null, false, min, max, isEmptyAllowed);
    }

    public static void makeNumericInput(final JTextComponent txtNumber, final long min, final long max) {
        makeNumericInput(txtNumber, null, false, min, max, false);
    }

    /** isBigger=true will switch values if txtNumber  >= txtCompare */
    public static void makeNumericInput(final JTextComponent txtNumber, final JTextComponent txtCompare, final boolean isBigger, final long min, final long max, final boolean isEmptyAllowed) {
        txtNumber.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNumber.setText(txtNumber.getText().trim());
                if (!isEmptyAllowed || txtNumber.getText().length() > 0) {
                    long val;
                    try {
                        val = Math.max(Long.parseLong(txtNumber.getText()), min);
                        val = Math.min(val, max);
                    } catch (NumberFormatException e) {
                        val = min;
                    }
                    txtNumber.setText(String.valueOf(val));
                    if (txtCompare != null) try {
                        long compare = Long.parseLong(txtCompare.getText());
                        if (isBigger && val > compare || !isBigger && val < compare) {
                            String temp = txtCompare.getText();
                            txtCompare.setText(txtNumber.getText());
                            txtNumber.setText(temp);
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        });
        txtNumber.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_UNDEFINED && evt.getKeyChar() != KeyEvent.VK_BACK_SPACE && evt.getKeyChar() != KeyEvent.VK_DELETE) if (!Character.isDigit(evt.getKeyChar())) {
                    if (min >= 0) evt.setKeyChar(KeyEvent.CHAR_UNDEFINED); else if (evt.getKeyChar() != MINUS_SIGN) evt.setKeyChar(KeyEvent.CHAR_UNDEFINED);
                }
            }
        });
    }

    /** it was necessary to add this because makeNumericInput does not gaurantee what focusLost listener will be called first, so could still get a number format exception */
    public static int getInt(JTextComponent txtNumber) {
        String val = txtNumber.getText();
        try {
            return Integer.parseInt(txtNumber.getText());
        } catch (NumberFormatException e) {
            if (val.indexOf(MINUS_SIGN) == 0) return Integer.MIN_VALUE; else return Integer.MAX_VALUE;
        }
    }

    /** returns the specified icon or a default icon if it isn't found 
	 * XXX added parent because webstart was screwing up when we used the string as the source for the url
	 */
    public static ImageIcon getIcon(Object parent, String url) {
        URL icon = parent.getClass().getResource(url);
        if (icon == null) {
            icon = parent.getClass().getResource("/net/turingcomplete/phosphor/shared/default.gif");
            if (icon == null) throw new RuntimeException("Default icon not found.");
        }
        return new ImageIcon(icon);
    }

    /** copies the string to the system clipboard */
    public static void clipboardCopy(String s) {
        StringSelection ss = new StringSelection(s);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
    }

    /** wraps the SwingUtilities of the same name and catches it's checked exceptions 
	 *	This method is prone to deadlock under rare circumstances.
	 */
    public static void invokeAndWait(Runnable doRun) {
        if (EventQueue.isDispatchThread()) doRun.run(); else try {
            EventQueue.invokeAndWait(doRun);
        } catch (InterruptedException e) {
            Trace.display(e);
        } catch (InvocationTargetException e) {
            Trace.display(e);
        }
    }

    /** wraps the SwingUtilities of the same name, gaurds against already being in dispatch thread */
    public static void invokeLater(Runnable doRun) {
        if (EventQueue.isDispatchThread()) doRun.run(); else EventQueue.invokeLater(doRun);
    }

    /** replaces all 'before' with 'after', case insensitive */
    public static String replaceAll(String original, String before, String after) {
        StringBuffer replaced = new StringBuffer(original);
        before = before.toUpperCase();
        int i = 0;
        for (; ; ) {
            i = replaced.toString().toUpperCase().indexOf(before, i);
            if (i >= 0) {
                replaced.replace(i, i + before.length(), after);
                i += after.length();
            } else break;
        }
        return replaced.toString();
    }

    public static String getItem(JComboBox cbox) {
        Object item = cbox.isEditable() ? cbox.getEditor().getItem() : cbox.getSelectedItem();
        addItem((String) item, cbox);
        return item == null ? "" : item.toString().trim();
    }

    public static void addItem(String[] items, JComboBox cbox) {
        for (int i = 0; i < items.length; ++i) addItem(items[i], cbox);
    }

    public static void addItem(Collection items, JComboBox cbox) {
        Iterator i = items.iterator();
        while (i.hasNext()) {
            addItem((String) i.next(), cbox);
        }
    }

    public static void addItem(String item, JComboBox cbox) {
        if (item != null && item.length() > 0) {
            if (cbox.getItemCount() > 0) {
                int i;
                for (i = 0; i < cbox.getItemCount(); ++i) {
                    String curItem = (String) cbox.getItemAt(i);
                    if (curItem.compareTo(item) > 0) {
                        cbox.insertItemAt(item, i);
                        break;
                    } else if (curItem.equals(item)) {
                        break;
                    }
                }
                if (i == cbox.getItemCount()) {
                    cbox.insertItemAt(item, i);
                }
            } else {
                cbox.insertItemAt(item, 0);
            }
        }
    }

    public static void setItem(String item, JComboBox cbox) {
        if (item != null) {
            cbox.setSelectedItem(item);
            addItem(item, cbox);
        }
    }

    /** panel must be in a grid bag layout with fill->both, and the panel has boxlayout*/
    public static void addToPanel(JPanel panel, Component component) {
        panel.setMaximumSize(new Dimension(0, 0));
        panel.add(component);
    }

    public static Font toFont(MutableAttributeSet att) {
        return new Font(StyleConstants.getFontFamily(att), (StyleConstants.isBold(att) ? Font.BOLD : Font.PLAIN) | (StyleConstants.isItalic(att) ? Font.ITALIC : 0), StyleConstants.getFontSize(att));
    }

    /** @param percentWidths contains the percent widths that each column should be 0..numColumns - 1*/
    public static void setColumnWidths(JTable table, float[] percentWidths) {
        Assert.that(percentWidths.length == table.getColumnCount() - 1);
        int totalWidth = table.getPreferredSize().width;
        int accumWidth = totalWidth;
        for (int i = 0; i < percentWidths.length; ++i) {
            TableColumn tblCol = table.getColumnModel().getColumn(i);
            int width = (int) (percentWidths[i] * totalWidth);
            accumWidth -= width;
            tblCol.setPreferredWidth(width);
            tblCol.setWidth(width);
        }
        TableColumn tblCol = table.getColumnModel().getColumn(percentWidths.length);
        tblCol.setPreferredWidth(accumWidth);
        tblCol.setWidth(accumWidth);
    }

    public static void removeElements(Collection c, int[] indexes) {
        Iterator i = c.iterator();
        int j = 0;
        int numRemoved = 0;
        while (i.hasNext() && numRemoved < indexes.length) {
            i.next();
            if (j++ == indexes[numRemoved]) {
                i.remove();
                ++numRemoved;
            }
        }
    }
}
