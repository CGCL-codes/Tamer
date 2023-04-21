package org.wcb.gui.forms;

import org.wcb.model.vo.LogImportVO;
import org.wcb.gui.component.WaveButton;
import javax.swing.JPanel;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
 * $Change:  $ submitted by $Author: wbogaardt $ at $DateTime: Apr 13, 2006 9:28:50 AM $ <br>
 * </small>
 *
 * @author wbogaardt
 * @version 1
 *          Date: Apr 13, 2006
 *          Time: 9:28:50 AM
 */
public class CSVDataImportSelectForm extends JPanel implements ActionListener {

    private JButton jButtonExport;

    private JButton jButtonExportAll;

    private JButton jButtonRevertAll;

    private JButton jButtonRevert;

    private JList jListLeftside;

    private JList jListRightside;

    private DefaultListModel leftsideOptionModel;

    private DefaultListModel rightsideOptionModel;

    /**
     * Default constructor.
     */
    public CSVDataImportSelectForm() {
        this.initComponents();
    }

    /**
     * Initialize components.
     */
    private void initComponents() {
        GridBagConstraints gridBagConstraints;
        JScrollPane jScrollPane1 = new JScrollPane();
        leftsideOptionModel = new DefaultListModel();
        leftsideOptionModel.addElement("ENTRY");
        leftsideOptionModel.addElement("REGISTRATION");
        leftsideOptionModel.addElement("FROM");
        leftsideOptionModel.addElement("ROUTE");
        leftsideOptionModel.addElement("TO");
        leftsideOptionModel.addElement("DURATION");
        leftsideOptionModel.addElement("PIC");
        leftsideOptionModel.addElement("SIC");
        leftsideOptionModel.addElement("XCOUNTRY");
        leftsideOptionModel.addElement("INSTRUCTING");
        leftsideOptionModel.addElement("SAFETY PILOT");
        leftsideOptionModel.addElement("DUAL RECEIVED");
        leftsideOptionModel.addElement("SOLO");
        leftsideOptionModel.addElement("#TAKEOFFS");
        leftsideOptionModel.addElement("#LANDINGS");
        leftsideOptionModel.addElement("#NIGHT TAKEOFFS");
        leftsideOptionModel.addElement("#NIGHT LANDINGS");
        leftsideOptionModel.addElement("APPROACHES");
        leftsideOptionModel.addElement("NIGHT DURATION");
        leftsideOptionModel.addElement("ACTUAL IMC");
        leftsideOptionModel.addElement("SIMULATED IMC");
        leftsideOptionModel.addElement("FLIGHT SIMULATOR");
        jListLeftside = new JList(leftsideOptionModel);
        jListLeftside.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane jScrollPane2 = new JScrollPane();
        rightsideOptionModel = new DefaultListModel();
        jListRightside = new JList(rightsideOptionModel);
        jListRightside.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jButtonExport = new WaveButton(">");
        jButtonExportAll = new WaveButton(">>");
        jButtonRevert = new WaveButton("<");
        jButtonRevertAll = new WaveButton("<<");
        jButtonExport.addActionListener(this);
        jButtonRevert.addActionListener(this);
        jButtonExportAll.addActionListener(this);
        jButtonRevertAll.addActionListener(this);
        setLayout(new GridBagLayout());
        jScrollPane1.setViewportView(jListLeftside);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        add(jScrollPane1, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.SOUTH;
        add(jButtonExport, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        add(jButtonExportAll, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        add(jButtonRevertAll, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        add(jButtonRevert, gridBagConstraints);
        jScrollPane2.setViewportView(jListRightside);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        add(jScrollPane2, gridBagConstraints);
    }

    /**
     * Handle action events.
     * @param evt action event to handle.
     */
    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src.equals(jButtonExport)) {
            int idx = jListLeftside.getSelectedIndex();
            rightsideOptionModel.addElement(jListLeftside.getSelectedValue());
            leftsideOptionModel.remove(idx);
        }
        if (src.equals(jButtonExportAll)) {
            DefaultListModel model = (DefaultListModel) jListLeftside.getModel();
            int size = model.getSize();
            for (int idx = 0; idx < size; idx++) {
                rightsideOptionModel.addElement(model.getElementAt(0));
                leftsideOptionModel.remove(0);
            }
        }
        if (src.equals(jButtonRevertAll)) {
            DefaultListModel model = (DefaultListModel) jListRightside.getModel();
            int size = model.getSize();
            for (int idx = 0; idx < size; idx++) {
                leftsideOptionModel.addElement(model.getElementAt(0));
                rightsideOptionModel.remove(0);
            }
        }
        if (src.equals(jButtonRevert)) {
            int idx = jListRightside.getSelectedIndex();
            leftsideOptionModel.addElement(jListRightside.getSelectedValue());
            rightsideOptionModel.remove(idx);
        }
    }

    /**
     * Get the value object from the that the person is going to import in order of.
     * @return Log import position place holders.
     */
    public LogImportVO getValueObject() {
        LogImportVO returnValue = new LogImportVO();
        for (int i = 0; i < rightsideOptionModel.getSize(); i++) {
            String value = (String) rightsideOptionModel.get(i);
            if (value.equalsIgnoreCase("ENTRY")) {
                returnValue.setEntryDatePosition(i);
            }
            if (value.equalsIgnoreCase("REGISTRATION")) {
                returnValue.setRegistrationPosition(i);
            }
            if (value.equalsIgnoreCase("FROM")) {
                returnValue.setFAAFromPosition(i);
            }
            if (value.equalsIgnoreCase("ROUTE")) {
                returnValue.setViaPosition(i);
            }
            if (value.equalsIgnoreCase("TO")) {
                returnValue.setFAAToPosition(i);
            }
            if (value.equalsIgnoreCase("DURATION")) {
                returnValue.setFlightDurationPosition(i);
            }
            if (value.equalsIgnoreCase("PIC")) {
                returnValue.setPICPosition(i);
            }
            if (value.equalsIgnoreCase("SIC")) {
                returnValue.setSICPosition(i);
            }
            if (value.equalsIgnoreCase("XCOUNTRY")) {
                returnValue.setCrossCountryPosition(i);
            }
            if (value.equalsIgnoreCase("INSTRUCTING")) {
                returnValue.setFlightInstructingPosition(i);
            }
            if (value.equalsIgnoreCase("SAFETY PILOT")) {
                returnValue.setSafetyPilotPosition(i);
            }
            if (value.equalsIgnoreCase("DUAL RECEIVED")) {
                returnValue.setDualReceivedPosition(i);
            }
            if (value.equalsIgnoreCase("SOLO")) {
                returnValue.setSoloPosition(i);
            }
            if (value.equalsIgnoreCase("#TAKEOFFS")) {
                returnValue.setDayTakoffPosition(i);
            }
            if (value.equalsIgnoreCase("#LANDINGS")) {
                returnValue.setDayLandingPosition(i);
            }
            if (value.equalsIgnoreCase("#NIGHT TAKEOFFS")) {
                returnValue.setNightTakeoffPosition(i);
            }
            if (value.equalsIgnoreCase("#NIGHT LANDINGS")) {
                returnValue.setNightLandingPosition(i);
            }
            if (value.equalsIgnoreCase("APPROACHES")) {
                returnValue.setInstrumentApproachesPosition(i);
            }
            if (value.equalsIgnoreCase("NIGHT DURATION")) {
                returnValue.setConditionNightPosition(i);
            }
            if (value.equalsIgnoreCase("ACTUAL IMC")) {
                returnValue.setActualIMCPosition(i);
            }
            if (value.equalsIgnoreCase("SIMULATED IMC")) {
                returnValue.setSimulatedIMCPosition(i);
            }
            if (value.equalsIgnoreCase("FLIGHT SIMULATOR")) {
                returnValue.setFlightSimPosition(i);
            }
        }
        return returnValue;
    }
}
