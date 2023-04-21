package org.wcb.gui.forms;

import org.wcb.model.service.impl.RecencyExperianceValidationService;
import org.wcb.model.service.IServicesConstants;
import org.wcb.model.service.ILogbookService;
import org.wcb.model.util.SpringUtil;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import java.awt.Color;

/**
 * <small>
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
 * $Change:  $ submitted by $Author: wbogaardt $ at $DateTime: Apr 7, 2006 10:16:01 AM $ <br>
 * </small>
 *
 * @author wbogaardt
 * @version 1
 *          Date: Apr 7, 2006
 *          Time: 10:16:01 AM
 */
public class RecentFlightExperiencePanel extends JPanel {

    private JTextArea area;

    /**
     * Default Constructor of panel. This panel displays
     * a message about the recent flight experience being met for FAA standards.
     */
    public RecentFlightExperiencePanel() {
        init();
    }

    /**
     * Init components.
     */
    private void init() {
        setLayout(new FlowLayout());
        area = new JTextArea(10, 50);
        area.setEditable(false);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setBackground(new Color(0xefd67a));
        add(new JScrollPane(area));
    }

    /**
     * Refreshes the message information in the dialog.
     */
    public void refresh() {
        RecencyExperianceValidationService recencyValidator = (RecencyExperianceValidationService) SpringUtil.getApplicationContext().getBean(IServicesConstants.LOGBOOK_RECENCY_EXPERIANCE);
        ILogbookService delegateService = (ILogbookService) SpringUtil.getApplicationContext().getBean(IServicesConstants.LOGBOOK_SERVICE);
        recencyValidator.setObject(delegateService);
        recencyValidator.validate();
        String messages = recencyValidator.getMessage();
        area.setText(messages);
    }
}
