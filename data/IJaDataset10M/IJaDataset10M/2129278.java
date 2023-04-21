package net.sf.japi.progs.jeduca.swing.font;

import java.awt.Component;
import java.awt.Font;
import static java.awt.Font.BOLD;
import static java.awt.Font.ITALIC;
import static java.awt.Font.PLAIN;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.REMAINDER;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;
import static javax.swing.BorderFactory.createCompoundBorder;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createTitledBorder;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.showConfirmDialog;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/** Class for letting the user choose a font.
 * There are two possibilities to use this class.
 * The first one is to use an instance of FontChooser as Pane and simply add it to the desired Container.
 * The second one is to use one of this class' static methods to display a Dialog which lets the user choose a font.
 * @author $Author: chris $
 * @version $Id: FontChooser.java,v 1.5 2005/01/24 15:10:16 chris Exp $
 */
public class FontChooser extends JComponent implements ListSelectionListener, ChangeListener {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** Resource Bundle. */
    private static final ResourceBundle res = ResourceBundle.getBundle("net.sf.japi.progs.jeduca.swing.font.action");

    /** JList for Font Family.
     * @serial include
     */
    private JList familyList;

    /** JList for Font Style.
     * @serial include
     */
    private JList styleList;

    /** JList for Font Size.
     * @serial include
     */
    private JList sizeList;

    /** JSpinner for Font Size.
     * @serial include
     */
    private JSpinner sizeSpinner;

    /** FontPreview for Font.
     * @serial include
     */
    private FontPreview preview;

    /** Selected Font.
     * @serial include
     */
    private Font selectedFont;

    /** Create a new FontChooser. */
    public FontChooser() {
        setBorder(createCompoundBorder(createCompoundBorder(createEmptyBorder(8, 8, 8, 8), createTitledBorder(res.getString("desiredFont.borderTitle"))), createEmptyBorder(8, 4, 4, 4)));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        JLabel familyLabel, styleLabel, sizeLabel;
        familyLabel = new JLabel(res.getString("family.label"));
        styleLabel = new JLabel(res.getString("style.label"));
        sizeLabel = new JLabel(res.getString("size.label"));
        familyList = new JList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        styleList = new JList(new Integer[] { PLAIN, ITALIC, BOLD, BOLD | ITALIC });
        styleList.setCellRenderer(new FontStyleListCellRenderer());
        sizeList = new JList(new Integer[] { 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 22, 24, 26, 28, 32, 48, 64 });
        preview = new FontPreview();
        sizeSpinner = new JSpinner(new SpinnerNumberModel(12, 4, 100, 1));
        gbc.weightx = 1.0;
        gbc.fill = BOTH;
        add(familyLabel, gbc);
        add(styleLabel, gbc);
        gbc.gridwidth = REMAINDER;
        add(sizeLabel, gbc);
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.weighty = 1.0;
        add(new JScrollPane(familyList), gbc);
        add(new JScrollPane(styleList), gbc);
        gbc.gridheight = 1;
        gbc.gridwidth = REMAINDER;
        gbc.weighty = 0.0;
        add(sizeSpinner, gbc);
        gbc.weighty = 1.0;
        add(new JScrollPane(sizeList), gbc);
        gbc.gridwidth = 3;
        add(preview, gbc);
        familyList.addListSelectionListener(this);
        styleList.addListSelectionListener(this);
        sizeList.addListSelectionListener(this);
        sizeSpinner.addChangeListener(this);
        familyList.setSelectionMode(SINGLE_SELECTION);
        styleList.setSelectionMode(SINGLE_SELECTION);
        sizeList.setSelectionMode(SINGLE_SELECTION);
    }

    /** Set the selected font. */
    public void setSelectedFont(final Font selectedFont) {
        this.selectedFont = selectedFont;
        preview.setFont(selectedFont);
        sizeSpinner.setValue(selectedFont.getSize());
        sizeList.setSelectedValue(selectedFont.getSize(), true);
        styleList.setSelectedValue(selectedFont.getStyle(), true);
        familyList.setSelectedValue(selectedFont.getFamily(), true);
    }

    /** Set the selected family. */
    private void updateFont() {
        String family;
        int style;
        int size;
        family = familyList.getSelectedValue() == null ? selectedFont.getFamily() : (String) familyList.getSelectedValue();
        style = styleList.getSelectedValue() == null ? selectedFont.getStyle() : (Integer) styleList.getSelectedValue();
        size = sizeList.getSelectedValue() == null ? selectedFont.getSize() : (Integer) sizeSpinner.getValue();
        selectedFont = new Font(family, style, size);
        preview.setFont(selectedFont);
    }

    /** {@inheritDoc} */
    public void valueChanged(final ListSelectionEvent e) {
        Object source = e.getSource();
        if (source == familyList) {
        } else if (source == styleList) {
        } else if (source == sizeList) {
            Object size = sizeList.getSelectedValue();
            if (!sizeSpinner.getValue().equals(size) && size != null) {
                sizeSpinner.setValue(size);
            }
        } else {
            assert false;
        }
        updateFont();
    }

    /** {@inheritDoc} */
    public void stateChanged(final ChangeEvent e) {
        Object source = e.getSource();
        if (source == sizeSpinner) {
            Object size = sizeSpinner.getValue();
            if (!size.equals(sizeList.getSelectedValue())) {
                sizeList.setSelectedValue(size, true);
            }
        } else {
            assert false;
        }
        updateFont();
    }

    /** Show a dialog.
     * @param parent Parent component
     * @return seleced font or null
     */
    public static Font showChooseFontDialog(final Component parent) {
        return showChooseFontDialog(parent, Font.decode(null));
    }

    /** Show a dialog.
     * @param parent Parent compnent
     * @param font Font to modify
     * @return selected font or null
     */
    public static Font showChooseFontDialog(final Component parent, final Font font) {
        FontChooser chooser = new FontChooser();
        chooser.setSelectedFont(font);
        if (showConfirmDialog(parent, chooser, res.getString("chooser.title"), OK_CANCEL_OPTION, PLAIN_MESSAGE) == OK_OPTION) {
            return chooser.selectedFont;
        } else {
            return null;
        }
    }

    /** Main program.
     * Only for testing purposes
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        Font f = showChooseFontDialog(null);
        System.out.println(f);
    }
}
