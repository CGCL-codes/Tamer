package org.wcb.gui.renderer;

import org.wcb.model.vo.hibernate.AirportBO;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.Component;

/**
 * <small>
 * <p/>
 * Copyright (C)  2006  wbogaardt.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * <p/>
 * $File:  $ <br>
 * $Change:  $ submitted by $Author: wbogaardt $ at $DateTime: Dec 1, 2006 11:17:07 AM $ <br>
 * </small>
 *
 * @author wbogaardt
 *         This render is used for the E6b calculator so the user
 * can see and prefill a list of aiports lat and lon data.
 */
public class AirportLocationListCellRender extends JLabel implements ListCellRenderer {

    public AirportLocationListCellRender() {
        setOpaque(true);
        setVerticalAlignment(CENTER);
    }

    public Component getListCellRendererComponent(JList list, Object val, int index, boolean isSelected, boolean cellHasFocus) {
        AirportBO displayInfo = (AirportBO) val;
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setText(displayInfo.getIcao() + " - " + displayInfo.getName());
        setFont(list.getFont());
        return this;
    }
}
