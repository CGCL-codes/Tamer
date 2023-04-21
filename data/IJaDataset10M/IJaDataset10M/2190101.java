package com.panemu.trensoft.core.ui.controller;

import com.panemu.trensoft.core.TrensoftUtil;
import com.panemu.trensoft.core.pojo.Student;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.client.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import org.openswing.swing.message.send.java.FilterWhereClause;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Filter panel.</p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class StudentFilterPanel extends CustomFilterPanel {

    LabelControl labelCod = new LabelControl();

    TextControl controlCod = new TextControl();

    FlowLayout flowLayout1 = new FlowLayout();

    CheckBoxControl cbxActive = new CheckBoxControl();

    ArrayList<String> lstActive = new ArrayList<String>();

    public StudentFilterPanel() {
        try {
            jbInit();
            lstActive.add(TrensoftUtil.Status.Menetap.toString());
            lstActive.add(TrensoftUtil.Status.Laju.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        labelCod.setLabel("Name");
        controlCod.setMaxCharacters(20);
        controlCod.setTrimText(true);
        controlCod.setUpperCase(true);
        controlCod.addKeyListener(new StudentKeyListener());
        cbxActive.setText("Only Active Student");
        cbxActive.setSelected(true);
        cbxActive.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                loadData();
            }
        });
        this.setLayout(flowLayout1);
        flowLayout1.setAlignment(FlowLayout.LEFT);
        this.add(labelCod, null);
        this.add(controlCod, null);
        this.add(cbxActive, null);
    }

    void loadData() {
        if (controlCod.getValue() != null && !controlCod.getValue().equals("")) {
            FilterWhereClause[] f = new FilterWhereClause[2];
            f[0] = new FilterWhereClause("name", "like", "%" + controlCod.getValue() + "%");
            getQuickFilterValues().put("name", f);
        } else {
            getQuickFilterValues().remove("name");
        }
        if (cbxActive.isSelected()) {
            FilterWhereClause[] f = new FilterWhereClause[2];
            f[0] = new FilterWhereClause(Student.FieldName.status.toString(), "in", lstActive);
            getQuickFilterValues().put("status", f);
        } else {
            getQuickFilterValues().remove("status");
        }
        grid.reloadDataFromStart();
    }

    private class StudentKeyListener implements KeyListener {

        public void keyTyped(KeyEvent ke) {
        }

        public void keyPressed(KeyEvent ke) {
        }

        public void keyReleased(KeyEvent ke) {
            if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                loadData();
            } else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                grid.getGrid().requestFocus();
            }
        }
    }
}
