package tools;

import javax.swing.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.awt.*;

/**
 * Static convenience methods for GUIs which eliminate code duplication
 * This utility class has methods for:
 * equalizing sizes of components
 * creating pairs of components, both for user input (JLabel and JTextField) and simple display (two JLabel objects)
 *
 * @author http://www.javapractices.com/topic/TopicAction.do?Id=152
 * @since 2.0
 */
public final class SwingUtil {

    private static int TXT_INPUT_COLS = 20;

    private static int MAX_LABEL_LENGTH = 30;

    /**
   * This is a static utility class. It cannot be constructed.
   */
    private SwingUtil() {
    }

    /**
   * Create a pair of components, a <tt>JLabel</tt> and an associated
   * <tt>JTextField</tt>, as is typically used for user input.
   * <p/>
   * <P>The <tt>JLabel</tt> appears on the left, and the <tt>JTextField</tt>
   * appears on the same  row, just to the right of the <tt>JLabel</tt>.
   * The <tt>JLabel</tt> has a mnemonic which forwards focus to the
   * <tt>JTextField</tt> when activated.
   * <p/>
   * The <tt>JTextField</tt> will grow when extra space is available,
   * the <tt>JLabel</tt> will not grow.
   *
   * @param container    holds the pair of components.
   * @param name         text of the <tt>JLabel</tt> component.
   * @param initialValue possibly-null initial value to appear
   *                     in the <tt>JTextField</tt>; if <tt>null</tt>, then
   *                     <tt>JTextField</tt> will be blank.
   * @param mnemonic     <tt>KeyEvent</tt> field, used as the mnemonic for
   *                     the <tt>JLabel</tt>.
   * @param c            applied to the <tt>JLabel</tt>; the corresponding
   *                     c for the <tt>JTextField</tt> are the same as
   *                     <tt>c</tt>, except for <tt>gridx</tt> being incremented by one;
   *                     in addition, if <tt>c</tt> has <tt>weightx=0</tt> (the default),
   *                     then the entry field will receive <tt>weightx=1.0</tt> (entry field gets more
   *                     horizontal space upon resize).
   * @param tooltip      possibly-null text displayed as tool tip for the
   *                     <tt>JTextField</tt> ; if <tt>null</tt>, the tool tip is turned off.
   * @return the user input <tt>JTextField</tt>.
   */
    public static JTextField addSimpleEntryField(Container container, String name, String initialValue, int mnemonic, GridBagConstraints c, String tooltip) {
        Args.checkForNull(name, "name is null");
        JLabel label = new JLabel(name);
        label.setDisplayedMnemonic(mnemonic);
        container.add(label, c);
        JTextField textField = new JTextField(TXT_INPUT_COLS);
        label.setLabelFor(textField);
        textField.setToolTipText(tooltip);
        if (initialValue != null) {
            textField.setText(initialValue);
        }
        c.gridx = c.gridx++;
        if (c.fill != GridBagConstraints.HORIZONTAL) {
            c.fill = GridBagConstraints.HORIZONTAL;
        }
        if (c.weightx == 0.0) {
            c.weightx = 1.0;
        }
        container.add(textField, c);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.weighty = 0;
        return textField;
    }

    /**
   * Create a pair of <tt>JLabel</tt> components, as is typically needed
   * for display of a name-value pair.
   * <p/>
   * <P>The name appears on the left, and the value appears on the right,
   * all on the same row. A colon and an empty space are appended to the name.
   * <p/>
   * <P> If the the length of "value" label is greater than <tt>MAX_LABEL_LENGTH<tt>,
   * then the text is truncated, an ellipsis
   * is placed at its end, and the full text is placed in a tooltip.
   *
   * @param aContainer       holds the pair of components.
   * @param aName            text of the name <tt>JLabel</tt>.
   * @param aValue           possibly-null ; if null, then an empty <tt>String</tt>
   *                         is used for the value; otherwise <tt>Object.toString</tt> is used.
   * @param aConstraints     for the name <tt>JLabel</tt>; the corresponding
   *                         constraints for the value <tt>JLabel</tt> are mostly taken from
   *                         <tt>aConstraints</tt>, except for <tt>gridx</tt> being incremented by one
   *                         (<tt>weightx</tt> may differ as well - see <tt>aWeightOnDisplay</tt>.)
   * @param aWeightOnDisplay if true, then set <tt>weightx</tt> for the value
   *                         field to 1.0 (to give it more horizontal space upon resize).
   * @return the <tt>JLabel</tt> for the value (which is usually variable).
   */
    public static JLabel addSimpleDisplayField(Container aContainer, String aName, Object aValue, GridBagConstraints aConstraints, boolean aWeightOnDisplay) {
        StringBuilder formattedName = new StringBuilder(aName);
        formattedName.append(": ");
        JLabel name = new JLabel(formattedName.toString());
        aContainer.add(name, aConstraints);
        String valueText = (aValue != null ? aValue.toString() : "");
        JLabel value = new JLabel(valueText);
        truncateLabelIfLong(value);
        aConstraints.gridx = ++aConstraints.gridx;
        if (aWeightOnDisplay) {
            aConstraints.weightx = 1.0;
        }
        aContainer.add(value, aConstraints);
        return value;
    }

    /**
   * Present a number of read-only items to the user as a vertical listing
   * of <tt>JLabel</tt> name-value pairs.
   * <p/>
   * <P>Each pair is added in the style of {@link #addSimpleDisplayField},
   * and its <tt>aWeightOnDisplay</tt> param is set to <tt>true</tt>).
   * <p/>
   * <P>The order of presentation is determined by the iteration order of
   * <tt>nameValuePairs</tt>.
   * <p/>
   * <P>The number of items which should be presented using this method is limited, since
   * no scrolling mechanism is given to the user.
   *
   * @param container      holds the display fields.
   * @param nameValuePairs has <tt>String</tt> keys for the names,
   *                       and values are possibly null <tt>Object</tt>s;
   *                       if null, then an empty <tt>String</tt> is displayed, otherwise
   *                       <tt>Object.toString</tt> is called on the value and displayed.
   */
    public static void addSimpleDisplayFields(Container container, Map<String, String> nameValuePairs) {
        Set<String> keys = nameValuePairs.keySet();
        int rowIdx = 0;
        for (String name : keys) {
            String value = nameValuePairs.get(name);
            if (value == null) {
                value = "";
            }
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = rowIdx;
            c.gridy = 0;
            addSimpleDisplayField(container, name, value, c, true);
            ++rowIdx;
        }
    }

    /**
   * If aLabel has text which is longer than MAX_LABEL_LENGTH, then truncate
   * the label text and place an ellipsis at the end; the original text is placed
   * in a tooltip.
   * <p/>
   * This is particularly useful for displaying file names, whose length
   * can vary widely between deployments.
   */
    private static void truncateLabelIfLong(JLabel aLabel) {
        String originalText = aLabel.getText();
        if (originalText.length() > MAX_LABEL_LENGTH) {
            aLabel.setToolTipText(originalText);
            String truncatedText = originalText.substring(0, MAX_LABEL_LENGTH) + "...";
            aLabel.setText(truncatedText);
        }
    }

    /**
   * Make a horizontal row of buttons of equal size, whch are equally spaced,
   * and aligned on the right.
   * <p/>
   * <P>The returned component has border spacing only on the top (of the size
   * recommended by the Look and Feel Design Guidelines).
   *
   * @param buttons contains the buttons to be placed in a row.
   */
    public static JComponent getCommandRow(List<JComponent> buttons) {
        final int TOP_MARGIN = 2;
        final int WIDTH_BETWEEN_BUTTONS = 5;
        equalizeSizes(buttons);
        JPanel panel = new JPanel();
        LayoutManager layout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createEmptyBorder(TOP_MARGIN, 0, 0, 0));
        panel.add(Box.createHorizontalGlue());
        Iterator<JComponent> buttonsIter = buttons.iterator();
        while (buttonsIter.hasNext()) {
            panel.add(buttonsIter.next());
            if (buttonsIter.hasNext()) {
                panel.add(Box.createHorizontalStrut(WIDTH_BETWEEN_BUTTONS));
            }
        }
        return panel;
    }

    /**
   * Sets the items in <tt>components</tt> to the same size.
   * <p/>
   * <P>Sets each component's preferred and maximum sizes.
   * The actual size is determined by the layout manager, which adjusts
   * for locale-specific strings and customized fonts. (See this
   * <a href="http://java.sun.com/products/jlf/ed2/samcode/prefere.html">Sun doc</a>
   * for more information.)
   *
   * @param components items whose sizes are to be equalized
   */
    public static void equalizeSizes(List<JComponent> components) {
        Dimension targetSize = new Dimension(0, 0);
        for (JComponent comp : components) {
            Dimension compSize = comp.getPreferredSize();
            double width = Math.max(targetSize.getWidth(), compSize.getWidth());
            double height = Math.max(targetSize.getHeight(), compSize.getHeight());
            targetSize.setSize(width, height);
        }
        setSizes(components, targetSize);
    }

    private static void setSizes(List components, Dimension dimension) {
        for (Object aComponent : components) {
            JComponent comp = (JComponent) aComponent;
            comp.setPreferredSize((Dimension) dimension.clone());
            comp.setMaximumSize((Dimension) dimension.clone());
        }
    }
}
