package com.rapidminer.gui.new_plotter.gui.cellrenderer;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import com.rapidminer.gui.tools.SwingTools;
import com.rapidminer.tools.I18N;

/**
 * This class renders ComboBox items that contain enumeration values as a JLabel with label, tooltip 
 * and icon set by a resource identifier.
 * <p>
 * The label settings are taken from a .properties file being part of
 * the GUI Resource bundles of RapidMiner. These might be accessed using the I18N class.
 * <p>
 * A resource action needs a key specifier, which will be used to build the complete keys of
 * the form:
 * <ul><li>gui.label.-key-.ENUM_VALUE.label as label</li>
 * <li>gui.label.-key-.ENUM_VALUE.tip as tooltip</li>
 * <li>gui.label.-key-.ENUM_VALUE.icon as icon</li>
 * </ul>
 * 
 * @author Nils Woehler
 * 
 */
public class EnumComboBoxCellRenderer implements ListCellRenderer {

    private final String i18nKeyPrefix;

    private final Map<Object, String> textCache = new HashMap<Object, String>();

    private final Map<Object, ImageIcon> iconCache = new HashMap<Object, ImageIcon>();

    private final DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    /**
	 *	Creates a Enumeration ComboBox cell renderer.
	 * 
	 * @param key the gui resource key.  
	 * Example: gui.label.foo_plotter.series_type.LINES.label with key 'foo_plotter.series_type'
	 */
    public EnumComboBoxCellRenderer(String key) {
        this.i18nKeyPrefix = key;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel listCellRendererComponent = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        String text = textCache.get(value);
        ImageIcon icon = iconCache.get(value);
        if (text == null) {
            text = I18N.getMessageOrNull(I18N.getGUIBundle(), "gui.label." + i18nKeyPrefix + "." + value + ".label");
            if (text != null) {
                textCache.put(value, text);
            } else {
                text = i18nKeyPrefix + "." + value;
            }
            String iconId = I18N.getMessageOrNull(I18N.getGUIBundle(), "gui.label." + i18nKeyPrefix + "." + value + ".icon");
            if (iconId != null) {
                icon = SwingTools.createIcon("16/" + iconId);
                iconCache.put(value, icon);
            }
        }
        listCellRendererComponent.setText(text);
        listCellRendererComponent.setIcon(icon);
        return listCellRendererComponent;
    }
}
