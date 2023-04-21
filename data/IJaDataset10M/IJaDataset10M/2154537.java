package org.digitall.apps.projecttask.interfaces;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JDesktopPane;
import org.digitall.lib.components.basic.BasicInternalFrame;
import org.digitall.lib.components.basic.BasicPanel;

public class FrameContainer extends BasicInternalFrame {

    private BasicPanel panel;

    private int panelType;

    public static final int TASKTREE = 1;

    public static final int TASKMGMT = 2;

    public static final int USERLIST = 3;

    public static final int TASKBYEMPLOYEEMGMT = 4;

    public static final int PERSON = 5;

    public static final int BUDGETLIST = 6;

    public static final int EXPENDITUREACCOUNT = 7;

    public static final int BUDGETEXPENDITUREACCOUNTLIST = 8;

    public static final int BUDGETEXPENDITUREACCOUNT = 9;

    public static final int COSTSCENTRE = 10;

    public static final int BUDGETCOSTCENTRE = 11;

    public static final int BUDGETCOSTSCENTRELIST = 12;

    public static final int BUDGETCOSTSCENTREAMOUNT = 13;

    public static final int BUDGETEXPENDITUREACCOUNTMGMT = 14;

    public static final int ACCOUNTMGMT = 15;

    public static final int ACCOUNTSLIST = 16;

    public static final int ACCOUNTDETAILS = 17;

    private String title = "";

    private JDesktopPane parentDesktop;

    private Component parentList;

    private Object params;

    public FrameContainer(int _panelType, JDesktopPane _parentDesktop, Component _parentList, Object _params) {
        try {
            params = _params;
            parentList = _parentList;
            parentDesktop = _parentDesktop;
            panelType = _panelType;
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(null);
        this.setSize(new Dimension(416, 360));
        this.setClosable(true);
        this.setIconifiable(true);
        this.setTitle(title);
        this.setSize(panel.getWidth() + 10, panel.getHeight() + 40);
        this.getContentPane().add(panel, null);
    }
}
