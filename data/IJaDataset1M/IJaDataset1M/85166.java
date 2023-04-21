package tico.components;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * List to choose a font face only showing the system available fonts as
 * options.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TFontFaceList extends JPanel {

    private static int LIST_MIN_HEIGHT = 120;

    private static int LIST_MIN_WIDTH = 100;

    private static String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

    private static String DEFAULT_FONT = fonts[0];

    private JScrollPane fontListScroll;

    private JList fontList;

    private JTextField fontField;

    /**
	 * Creates a new <code>TFontFaceList</code> with <code>fontFace</code>
	 * defaults to <i>default</i>.
	 */
    public TFontFaceList() {
        this(DEFAULT_FONT);
    }

    /**
	 * Creates a new <code>TFontFaceList</code> with  the specified initial
	 * <code>fontFace</code>.
	 * 
	 * @param fontFace The selected initial <code>fontFace</code>
	 */
    public TFontFaceList(String fontFace) {
        super();
        createFontList();
        createFontField();
        placeComponents();
        fontList.addListSelectionListener(new TFontFaceSelectionListener());
        setFontFace(fontFace);
    }

    private void createFontList() {
        fontListScroll = new JScrollPane();
        fontListScroll.setMinimumSize(new Dimension(LIST_MIN_WIDTH, LIST_MIN_HEIGHT));
        fontListScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        fontListScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        fontList = new JList();
        fontList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        fontList.setLayoutOrientation(JList.VERTICAL);
        fontList.setListData(fonts);
        fontListScroll.setViewportView(fontList);
    }

    private void createFontField() {
        fontField = new JTextField();
        fontField.setEditable(false);
        fontField.setFocusable(false);
    }

    private void placeComponents() {
        GridBagConstraints c = new GridBagConstraints();
        setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        add(fontField, c);
        c.insets = new Insets(5, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(fontListScroll, c);
    }

    /**
	 * Returns the selected <code>fontFace</code>.
	 * 
	 * @return The selected <code>fontFace</code>
	 */
    public String getFontFace() {
        return (String) fontList.getSelectedValue();
    }

    /**
	 * Set the <code>fontFace</code>.
	 * 
	 * @param fontFace The <code>fontFace</code> to set
	 */
    public void setFontFace(String fontFace) {
        if (fontFace == null) return;
        fontList.setSelectedValue(fontFace, true);
        fontField.setText((String) fontFace);
        fontField.setCaretPosition(0);
    }

    /**
	 * Adds an <code>ListSelectionListener</code>.
	 * 
	 * The <code>ListSelectionListener</code> will receive an
	 * <code>ListSelectionEvent</code> when the <code>fontFace</code> has been
	 * changed.
	 * 
	 * @param listener The <code>ListSelectionListener</code> that is to be notified
	 */
    public void addListSelectionListener(ListSelectionListener listener) {
        fontList.addListSelectionListener(listener);
    }

    /**
	 * Removes an <code>ListSelectionListener</code>.
	 * 
	 * @param listener The <code>ListSelectionListener</code> to remove
	 */
    public void removeListSelectionListener(ListSelectionListener listener) {
        fontList.removeListSelectionListener(listener);
    }

    private class TFontFaceSelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent event) {
            if (!event.getValueIsAdjusting()) {
                fontField.setText((String) fontList.getSelectedValue());
                fontField.setCaretPosition(0);
            }
        }
    }
}
