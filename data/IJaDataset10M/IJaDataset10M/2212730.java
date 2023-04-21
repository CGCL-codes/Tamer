package org.wcb.gui.component;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.plaf.ComponentUI;
import java.awt.Color;

/**
 * <small>
 * <p/>
 * Copyright (c)  2006  wbogaardt.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * <p/>
 * $File:  $ <br>
 * $Change:  $ submitted by $Author: wbogaardt $ at $DateTime: Sep 12, 2006 10:31:10 AM $ <br>
 * </small>
 *
 * @author wbogaardt
 *         Creates a pretty wavey button.
 */
public class WaveButton extends JButton {

    /**
     * Creates a wave button with text.
     * @param text text to display on button
     */
    public WaveButton(String text) {
        super(text);
        getUI().uninstallUI(this);
        ComponentUI ui = WaveButtonUI.createUI(this);
        setUI(ui);
        setBackground(new Color(0x522aec));
    }

    /**
     * wave button with icon image.
     * @param icon image to put on button.
     */
    public WaveButton(ImageIcon icon) {
        super(icon);
        getUI().uninstallUI(this);
        ComponentUI ui = WaveButtonUI.createUI(this);
        setUI(ui);
        setBackground(new Color(0x522aec));
    }
}
