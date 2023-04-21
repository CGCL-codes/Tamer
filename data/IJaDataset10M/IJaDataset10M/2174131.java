package com.ehs.pm.tables;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.xml.bind.JAXBException;
import org.jdesktop.swingx.JXPanel;
import com.ehs.pm.beans.Visit;
import com.ehs.common.gui.HPanel;
import com.ehs.common.HSConstantsI;
import com.ehs.common.gui.HTable;
import com.ehs.pm.dblayer.DataManager;

/**
 *
 * @author E15567
 */
public class VisitsTable extends HPanel implements HSConstantsI {

    private static final long serialVersionUID = 6670699408265873269L;

    private HTable table;

    private JScrollPane scrollVisit;

    private VisitsTableModel visitsTableModel;

    private JXPanel pnlButtons;

    private JButton btnOk;

    private Visit sVisit = null;

    public VisitsTable() {
        try {
            initComponents();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setUpTable();
    }

    private void initComponents() throws Exception {
        pnlButtons = new JXPanel();
        btnOk = new JButton(BUTTON_OK);
        table = new HTable();
        scrollVisit = new JScrollPane(table);
        this.setLayout(new BorderLayout());
        this.add(scrollVisit, BorderLayout.CENTER);
        this.add(pnlButtons, BorderLayout.SOUTH);
    }

    private void setUpTable() {
        visitsTableModel = new VisitsTableModel();
        this.table.setModel(visitsTableModel);
        btnOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int sRow = table.getSelectedRow();
                if (sRow != -1) {
                }
            }
        });
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() > 1) {
                    int sRow = table.getSelectedRow();
                    if (sRow != -1) {
                        sVisit = visitsTableModel.getSelectedVisit(sRow);
                    }
                }
            }
        });
    }

    public Visit getSelectedVisit() {
        return sVisit;
    }

    public HTable getTable() {
        return table;
    }

    public void loadVisits(String medRecNo) {
        Visit visit = null;
        try {
            File[] pFiles = new File(VISIT_BASE_XML_PATH + medRecNo + "/").listFiles();
            for (File pFile : pFiles) {
                if (pFile != null && pFile.getName().endsWith(".xml")) {
                    visit = (Visit) DataManager.getInstance().getObject(pFile);
                    visitsTableModel.addRow(visit);
                }
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
