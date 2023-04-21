package saadadb.admintool.panels.editors;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import saadadb.admintool.AdminTool;
import saadadb.admintool.components.SaveButton;
import saadadb.admintool.components.ToolBarPanel;
import saadadb.admintool.components.XMLButton;
import saadadb.admintool.components.voresources.VOServiceItemSelector;
import saadadb.admintool.panels.EditPanel;
import saadadb.admintool.utils.DataTreePath;
import saadadb.admintool.utils.MyGBC;
import saadadb.admintool.windows.TextSaver;
import saadadb.command.ArgsParser;
import saadadb.database.Database;
import saadadb.exceptions.QueryException;
import saadadb.exceptions.SaadaException;
import saadadb.sqltable.SQLTable;
import saadadb.sqltable.Table_Saada_VO_Capabilities;
import saadadb.util.Messenger;
import saadadb.vo.registry.Authority;
import saadadb.vo.registry.Capability;
import saadadb.vo.registry.Record;
import saadadb.vo.tap.TapServiceManager;

/**
 * @author laurentmichel
 *
 */
public class SIAServicePanel extends EditPanel {

    private SaveButton saveButton = new SaveButton(this);

    private VOServiceItemSelector itemSelector;

    private Authority authority;

    /**
	 * @param rootFrame
	 * @param ancestor
	 */
    public SIAServicePanel(AdminTool rootFrame, String ancestor) {
        super(rootFrame, SIA_PUBLISH, null, ancestor);
    }

    @Override
    protected void setToolBar() {
        this.initTreePathLabel();
        this.initSelectResourceLabel();
        this.add(new ToolBarPanel(this, false, false, false));
    }

    public void setDataTreePath(DataTreePath dataTreePath) {
        if (itemSelector != null) {
            try {
                itemSelector.loadCapabilities();
            } catch (Exception e) {
                showFatalError(rootFrame, e);
            }
        }
    }

    @Override
    protected void setActivePanel() {
        try {
            itemSelector = new VOServiceItemSelector(this, Capability.SIA);
        } catch (Exception e) {
            showFatalError(rootFrame, e);
        }
        JPanel tPanel = this.addSubPanel("Published SIA tables");
        MyGBC imcep = new MyGBC(0, 0, 0, 0);
        imcep.reset(5, 5, 5, 5);
        imcep.weightx = 1;
        imcep.weighty = 1;
        imcep.fill = GridBagConstraints.BOTH;
        tPanel.add(new JScrollPane(itemSelector), imcep);
        JButton b = new JButton("Empty SIA service");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (showConfirmDialog(rootFrame, "Do you want to empty your SIA service (no data is removed from the DB)?")) {
                    itemSelector.reset();
                    try {
                        SQLTable.beginTransaction();
                        Table_Saada_VO_Capabilities.emptyTable(Capability.SIA);
                        SQLTable.commitTransaction();
                        showInfo(rootFrame, "SIA service empty");
                    } catch (SaadaException e) {
                        SQLTable.abortTransaction();
                        showFatalError(rootFrame, e);
                    }
                }
            }
        });
        imcep.weightx = 0;
        imcep.weighty = 0;
        imcep.fill = GridBagConstraints.NONE;
        imcep.newRow();
        tPanel.add(b, imcep);
        this.setActionBar();
    }

    @Override
    public void active() {
    }

    protected void setActionBar() {
        this.saveButton = new SaveButton(this);
        this.saveButton.setEnabled(true);
        this.saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                TapServiceManager tsm = new TapServiceManager();
                try {
                    SQLTable.beginTransaction();
                    Table_Saada_VO_Capabilities.emptyTable(Capability.SIA);
                    itemSelector.saveCapabilities();
                    SQLTable.commitTransaction();
                    itemSelector.loadCapabilities();
                    showSuccess(SIAServicePanel.this.rootFrame, "SIA capabilities saved");
                } catch (Exception e) {
                    SQLTable.abortTransaction();
                    showFatalError(rootFrame, e);
                }
            }
        });
        JPanel tPanel = new JPanel();
        tPanel.setLayout(new GridBagLayout());
        tPanel.setBackground(LIGHTBACKGROUND);
        tPanel.setPreferredSize(new Dimension(1000, 48));
        tPanel.setMaximumSize(new Dimension(1000, 48));
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.gridx = 0;
        c.anchor = GridBagConstraints.PAGE_END;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        tPanel.add(saveButton, c);
        c.gridx++;
        c.gridx++;
        c.weightx = 1;
        tPanel.add(new JLabel(" "), c);
        this.add(tPanel);
    }
}
