package com.oat.gui;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import com.oat.Domain;

/**
 * Description: Suitable for rendering a domain object using its full human readable name
 *  
 * Date: 21/08/2007<br/>
 * @author Jason Brownlee 
 *
 * <br/>
 * <pre>
 * Change History
 * ----------------------------------------------------------------------------
 * 
 * </pre>
 */
public class DomainListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof Domain) {
            Domain d = (Domain) value;
            return super.getListCellRendererComponent(list, d.getHumanReadableName(), index, isSelected, cellHasFocus);
        }
        throw new GUIException("Unexpected value, expected Domain: " + value);
    }
}
