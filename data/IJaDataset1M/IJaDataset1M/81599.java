package net.rptools.inittool.component;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.SpinnerNumberModel;
import net.rptools.inittool.component.RPIconFactory.ImageStorageType;
import com.jeta.forms.components.panel.FormPanel;

/**
 * This is a controller class used to edit a font. The components are
 * contained on <code>FormPanel</code>s and identified by name.
 * 
 * @author jgorrell
 * @version $Revision$ $Date$ $Author$
 */
public class FontController {

    /**
   * The list of component names.
   * <ol start="0">
   * <li>Font combo box name
   * <li>Bold button
   * <li>Italics button
   * <li>Font size spinner
   * </ol>
   */
    private String[] componentNames;

    /**
   * The panel containing the components
   */
    private FormPanel panel;

    /**
   * Combo box containing all of the font names. 
   */
    public static final int FONTS = 0;

    /**
   * The toggle button used to turn bold on and off
   */
    public static final int BOLD = 1;

    /**
   * The toggle button used to turn italics on and off
   */
    public static final int ITALICS = 2;

    /**
   * The spinner used to change the size. 
   */
    public static final int SIZE = 3;

    /**
   * Resource containing the bold icon
   */
    public static final String BOLD_RESOURCE = ImageStorageType.createDescriptor("text_bold.png");

    /**
   * The icon for the bold toggle button.
   */
    public static final Icon BOLD_ICON = RPIconFactory.getInstance().get(BOLD_RESOURCE);

    /**
   * Resource containing the italics icon
   */
    public static final String ITALICS_RESOURCE = ImageStorageType.createDescriptor("text_italic.png");

    /**
   * The icon for the italics toggle button.
   */
    public static final Icon ITALICS_ICON = RPIconFactory.getInstance().get(ITALICS_RESOURCE);

    /**
   * Build the item editor controller for the passed panel and components
   * 
   * @param aPanel Panel containint the components.
   * @param theComponentNames The components that are used by this controller.  
   */
    public FontController(FormPanel aPanel, String[] theComponentNames) {
        assert aPanel != null : "A panel is required but none were passed";
        assert theComponentNames != null && theComponentNames.length == 4 : "The component names array is null or does not have the proper number of elements in it";
        panel = aPanel;
        componentNames = theComponentNames;
        String[] names = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Arrays.sort(names);
        DefaultComboBoxModel model = new DefaultComboBoxModel(names);
        panel.getComboBox(componentNames[FONTS]).setModel(model);
        AbstractButton button = panel.getButton(componentNames[BOLD]);
        button.setIcon(BOLD_ICON);
        Icon grey = button.getDisabledIcon();
        button.setIcon(grey);
        button.setSelectedIcon(BOLD_ICON);
        button.setText(null);
        button = panel.getButton(componentNames[ITALICS]);
        button.setIcon(ITALICS_ICON);
        grey = button.getDisabledIcon();
        button.setIcon(grey);
        button.setSelectedIcon(ITALICS_ICON);
        button.setText(null);
        panel.getSpinner(componentNames[SIZE]).setModel(new SpinnerNumberModel(11, 8, 24, 1));
    }

    /**
   * Get the font from this controller.
   * 
   * @return The font for the currently selected info.
   */
    public Font getFont() {
        StringBuilder sb = new StringBuilder((String) panel.getComboBox(componentNames[FONTS]).getSelectedItem());
        sb.append('-');
        if (panel.getButton(componentNames[BOLD]).isSelected()) sb.append("bold");
        if (panel.getButton(componentNames[ITALICS]).isSelected()) sb.append("italic");
        if (sb.charAt(sb.length() - 1) == '-') sb.append("plain");
        sb.append('-');
        sb.append(panel.getSpinner(componentNames[SIZE]).getValue());
        return Font.decode(sb.toString());
    }

    /**
   * Set the components to match a font
   * 
   * @param font Make the components match this font
   */
    public void setFont(Font font) {
        if (font == null) font = new Font("SansSerif", Font.PLAIN, 11);
        panel.getComboBox(componentNames[FONTS]).setSelectedItem(font.getName());
        panel.getButton(componentNames[BOLD]).setSelected(font.isBold());
        panel.getButton(componentNames[ITALICS]).setSelected(font.isItalic());
        panel.getSpinner(componentNames[SIZE]).setValue(font.getSize());
    }
}
