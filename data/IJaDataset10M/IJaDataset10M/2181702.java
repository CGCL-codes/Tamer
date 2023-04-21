package fr.itris.glips.svgeditor.properties;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author ITRIS, Jordi SUC
 */
public class SVGPropertiesFontChooserWidget extends SVGPropertiesWidget {

    /**
     * the constructor of the class
     * @param propertyItem a property item
     */
    public SVGPropertiesFontChooserWidget(SVGPropertyItem propertyItem) {
        super(propertyItem);
        buildComponent();
    }

    /**
	 * builds the component that will be displayed
	 */
    protected void buildComponent() {
        String propertyValue = propertyItem.getGeneralPropertyValue();
        Integer[] obj = new Integer[fontList.size()];
        for (int i = 0; i < fontList.size(); i++) {
            obj[i] = new Integer(i);
        }
        final JComboBox combo = new JComboBox(obj);
        FontFamilyRenderer renderer = new FontFamilyRenderer();
        combo.setRenderer(renderer);
        combo.setSelectedItem(fontFamilyList.contains(propertyValue) ? new Integer(fontFamilyList.indexOf(propertyValue)) : new Integer(0));
        final ActionListener listener = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                String value = "";
                if (combo.getSelectedItem() != null) {
                    try {
                        value = (String) (fontFamilyList.get(((Integer) combo.getSelectedItem()).intValue()));
                    } catch (Exception ex) {
                        value = "";
                    }
                }
                if (value != null && !value.equals("")) {
                    propertyItem.changePropertyValue(value);
                }
            }
        };
        combo.addActionListener(listener);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(combo);
        component = panel;
        disposer = new Runnable() {

            public void run() {
                combo.removeActionListener(listener);
            }
        };
    }

    /**
	 *  the class allowing to display a formatted text in a combo box
	 * @author ITRIS, Jordi SUC
	 */
    protected class FontFamilyRenderer extends JLabel implements ListCellRenderer {

        /**
		 * the constructor of the class
		 */
        protected FontFamilyRenderer() {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
        }

        /**
		  * returns the component that will be displayed in a combo box
		  * @param list 
		  * @param value
		  * @param index
		  * @param isSelected
		  * @param cellHasFocus
		  * @return the component that will be displayed in a combo box
		  */
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            int ind = ((Integer) value).intValue();
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            setText((String) fontFamilyList.get(ind));
            setFont((Font) fontList.get(ind));
            return this;
        }
    }
}
