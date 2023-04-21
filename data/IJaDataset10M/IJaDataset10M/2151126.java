package com.empower.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import com.empower.utils.ECSConstants;
import com.empower.utils.WidgetProperties;

public class DCViewPanel extends JPanel {

    public JButton cnclBtn;

    public JButton exportBtn;

    public JButton saveBtn;

    public JButton printBtn;

    public JTable srchRsltsTable;

    public JScrollPane scrollPane;

    public JLabel dcDateValueLbl;

    public JLabel dcDateLbl;

    public JLabel stnToLbl_Value;

    public JLabel stnToLbl;

    public JPanel srchResultsPnl;

    public JButton srchBtn;

    public JComboBox stnFromCombo;

    public JLabel stnFromLbl;

    public AutoComboBox dcNbrTxf;

    public JLabel dcNbrLbl;

    public JPanel srchCrtraPnl;

    public JLabel deliveredByLabel_Value;

    ResourceBundle resBorder = ResourceBundle.getBundle(ECSConstants.BORDER_TITLE);

    ResourceBundle resLabel = ResourceBundle.getBundle(ECSConstants.LABEL);

    ResourceBundle resButton = ResourceBundle.getBundle(ECSConstants.BUTTON);

    public DCViewPanel() {
        super();
        setLayout(new BorderLayout());
        setName("DC_VIEW_PNL");
        setPreferredSize(new Dimension(1010, 575));
        WidgetProperties.setPropToPanel(this, resBorder.getString("VIEW_DC"));
        add(getSrchCrtraPnl(), BorderLayout.NORTH);
        add(getSrchResultsPnl(), BorderLayout.CENTER);
    }

    public JPanel getSrchCrtraPnl() {
        if (srchCrtraPnl == null) {
            srchCrtraPnl = new JPanel();
            srchCrtraPnl.setPreferredSize(new Dimension(990, 75));
            final SpringLayout springLayout = new SpringLayout();
            srchCrtraPnl.setLayout(springLayout);
            WidgetProperties.setPropToPanel(srchCrtraPnl, "  " + resBorder.getString("SEARCHCRITERIA") + "  ");
            srchCrtraPnl.add(getStnFromLbl());
            springLayout.putConstraint(SpringLayout.SOUTH, getStnFromLbl(), 45, SpringLayout.NORTH, srchCrtraPnl);
            springLayout.putConstraint(SpringLayout.NORTH, getStnFromLbl(), 20, SpringLayout.NORTH, srchCrtraPnl);
            springLayout.putConstraint(SpringLayout.EAST, getStnFromLbl(), 150, SpringLayout.WEST, srchCrtraPnl);
            springLayout.putConstraint(SpringLayout.WEST, getStnFromLbl(), 15, SpringLayout.WEST, srchCrtraPnl);
            srchCrtraPnl.add(getStnFromCombo());
            springLayout.putConstraint(SpringLayout.SOUTH, getStnFromCombo(), 45, SpringLayout.NORTH, srchCrtraPnl);
            springLayout.putConstraint(SpringLayout.NORTH, getStnFromCombo(), 20, SpringLayout.NORTH, srchCrtraPnl);
            springLayout.putConstraint(SpringLayout.EAST, getStnFromCombo(), 335, SpringLayout.WEST, srchCrtraPnl);
            springLayout.putConstraint(SpringLayout.WEST, getStnFromCombo(), 130, SpringLayout.WEST, srchCrtraPnl);
            srchCrtraPnl.add(getDCNbrLbl());
            springLayout.putConstraint(SpringLayout.SOUTH, getDCNbrLbl(), 45, SpringLayout.NORTH, srchCrtraPnl);
            springLayout.putConstraint(SpringLayout.NORTH, getDCNbrLbl(), 20, SpringLayout.NORTH, srchCrtraPnl);
            springLayout.putConstraint(SpringLayout.EAST, getDCNbrLbl(), 455, SpringLayout.WEST, srchCrtraPnl);
            springLayout.putConstraint(SpringLayout.WEST, getDCNbrLbl(), 355, SpringLayout.WEST, srchCrtraPnl);
            srchCrtraPnl.add(getDCNbrTxf());
            springLayout.putConstraint(SpringLayout.SOUTH, getDCNbrTxf(), 45, SpringLayout.NORTH, srchCrtraPnl);
            springLayout.putConstraint(SpringLayout.NORTH, getDCNbrTxf(), 20, SpringLayout.NORTH, srchCrtraPnl);
            springLayout.putConstraint(SpringLayout.EAST, getDCNbrTxf(), 640, SpringLayout.WEST, srchCrtraPnl);
            springLayout.putConstraint(SpringLayout.WEST, getDCNbrTxf(), 455, SpringLayout.WEST, srchCrtraPnl);
            srchCrtraPnl.add(getSrchBtn());
            springLayout.putConstraint(SpringLayout.SOUTH, getSrchBtn(), 45, SpringLayout.NORTH, srchCrtraPnl);
            springLayout.putConstraint(SpringLayout.NORTH, getSrchBtn(), 20, SpringLayout.NORTH, srchCrtraPnl);
            springLayout.putConstraint(SpringLayout.EAST, getSrchBtn(), 785, SpringLayout.WEST, srchCrtraPnl);
            springLayout.putConstraint(SpringLayout.WEST, getSrchBtn(), 685, SpringLayout.WEST, srchCrtraPnl);
        }
        return srchCrtraPnl;
    }

    public JLabel getDCNbrLbl() {
        if (dcNbrLbl == null) {
            dcNbrLbl = new JLabel();
            dcNbrLbl.setText(resLabel.getString("CHLN_NBR"));
            WidgetProperties.setPropToLabel(dcNbrLbl);
        }
        return dcNbrLbl;
    }

    public AutoComboBox getDCNbrTxf() {
        if (dcNbrTxf == null) {
            dcNbrTxf = new AutoComboBox();
        }
        return dcNbrTxf;
    }

    public JLabel getStnFromLbl() {
        if (stnFromLbl == null) {
            stnFromLbl = new JLabel();
            stnFromLbl.setText(resLabel.getString("DSTN_STN"));
            WidgetProperties.setPropToLabel(stnFromLbl);
        }
        return stnFromLbl;
    }

    public JComboBox getStnFromCombo() {
        if (stnFromCombo == null) {
            stnFromCombo = new JComboBox();
        }
        return stnFromCombo;
    }

    public JButton getSrchBtn() {
        if (srchBtn == null) {
            srchBtn = new JButton();
            WidgetProperties.setPropToButton(srchBtn, resButton.getString("SEARCH"));
        }
        return srchBtn;
    }

    public JPanel getSrchResultsPnl() {
        if (srchResultsPnl == null) {
            srchResultsPnl = new JPanel();
            final SpringLayout springLayout = new SpringLayout();
            srchResultsPnl.setLayout(springLayout);
            WidgetProperties.setPropToPanel(srchResultsPnl, "  " + resBorder.getString("SEARCHRESULTS") + "  ");
            srchResultsPnl.add(getStnToLbl());
            springLayout.putConstraint(SpringLayout.SOUTH, getStnToLbl(), 40, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.NORTH, getStnToLbl(), 15, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.EAST, getStnToLbl(), 120, SpringLayout.WEST, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.WEST, getStnToLbl(), 15, SpringLayout.WEST, srchResultsPnl);
            srchResultsPnl.add(getStnToLbl_Value());
            springLayout.putConstraint(SpringLayout.SOUTH, getStnToLbl_Value(), 40, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.NORTH, getStnToLbl_Value(), 15, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.EAST, getStnToLbl_Value(), 335, SpringLayout.WEST, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.WEST, getStnToLbl_Value(), 120, SpringLayout.WEST, srchResultsPnl);
            srchResultsPnl.add(getDCDateLbl());
            springLayout.putConstraint(SpringLayout.SOUTH, getDCDateLbl(), 40, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.NORTH, getDCDateLbl(), 15, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.EAST, getDCDateLbl(), 450, SpringLayout.WEST, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.WEST, getDCDateLbl(), 355, SpringLayout.WEST, srchResultsPnl);
            srchResultsPnl.add(getDCDateValueLbl());
            springLayout.putConstraint(SpringLayout.SOUTH, getDCDateValueLbl(), 40, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.NORTH, getDCDateValueLbl(), 15, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.EAST, getDCDateValueLbl(), 550, SpringLayout.WEST, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.WEST, getDCDateValueLbl(), 455, SpringLayout.WEST, srchResultsPnl);
            srchResultsPnl.add(getScrollPane());
            springLayout.putConstraint(SpringLayout.SOUTH, getScrollPane(), 430, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.NORTH, getScrollPane(), 50, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.EAST, getScrollPane(), 985, SpringLayout.WEST, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.WEST, getScrollPane(), 5, SpringLayout.WEST, srchResultsPnl);
            srchResultsPnl.add(getExportBtn());
            springLayout.putConstraint(SpringLayout.SOUTH, getExportBtn(), 465, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.NORTH, getExportBtn(), 440, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.EAST, getExportBtn(), 115, SpringLayout.WEST, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.WEST, getExportBtn(), 20, SpringLayout.WEST, srchResultsPnl);
            srchResultsPnl.add(getSaveBtn());
            springLayout.putConstraint(SpringLayout.SOUTH, getSaveBtn(), 465, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.NORTH, getSaveBtn(), 440, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.EAST, getSaveBtn(), 315, SpringLayout.WEST, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.WEST, getSaveBtn(), 200, SpringLayout.WEST, srchResultsPnl);
            srchResultsPnl.add(getPrintBtn());
            springLayout.putConstraint(SpringLayout.SOUTH, getPrintBtn(), 465, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.NORTH, getPrintBtn(), 440, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.EAST, getPrintBtn(), 515, SpringLayout.WEST, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.WEST, getPrintBtn(), 400, SpringLayout.WEST, srchResultsPnl);
            srchResultsPnl.add(getCnclBtn());
            springLayout.putConstraint(SpringLayout.SOUTH, getCnclBtn(), 465, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.NORTH, getCnclBtn(), 440, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.EAST, getCnclBtn(), 970, SpringLayout.WEST, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.WEST, getCnclBtn(), 885, SpringLayout.WEST, srchResultsPnl);
            final JLabel deliveredByLabel = new JLabel();
            deliveredByLabel.setText("Delivered By");
            WidgetProperties.setPropToLabel(deliveredByLabel);
            srchResultsPnl.add(deliveredByLabel);
            springLayout.putConstraint(SpringLayout.SOUTH, deliveredByLabel, 40, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.NORTH, deliveredByLabel, 15, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.EAST, deliveredByLabel, 685, SpringLayout.WEST, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.WEST, deliveredByLabel, 575, SpringLayout.WEST, srchResultsPnl);
            srchResultsPnl.add(getDeliveredByValueLbl());
            springLayout.putConstraint(SpringLayout.SOUTH, getDeliveredByValueLbl(), 40, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.NORTH, getDeliveredByValueLbl(), 15, SpringLayout.NORTH, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.EAST, getDeliveredByValueLbl(), 900, SpringLayout.WEST, srchResultsPnl);
            springLayout.putConstraint(SpringLayout.WEST, getDeliveredByValueLbl(), 685, SpringLayout.WEST, srchResultsPnl);
        }
        return srchResultsPnl;
    }

    public JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setViewportView(getSrchRsltsTable());
        }
        return scrollPane;
    }

    public JTable getSrchRsltsTable() {
        if (srchRsltsTable == null) {
            srchRsltsTable = new JTable();
            srchRsltsTable.setBackground(Color.WHITE);
            srchRsltsTable.getTableHeader().setReorderingAllowed(false);
        }
        return srchRsltsTable;
    }

    public JButton getExportBtn() {
        if (exportBtn == null) {
            exportBtn = new JButton();
            WidgetProperties.setPropToButton(exportBtn, resButton.getString("EXPORT"));
        }
        return exportBtn;
    }

    public JButton getSaveBtn() {
        if (saveBtn == null) {
            saveBtn = new JButton();
            WidgetProperties.setPropToButton(saveBtn, resButton.getString("SAVE"));
        }
        return saveBtn;
    }

    public JButton getPrintBtn() {
        if (printBtn == null) {
            printBtn = new JButton();
            WidgetProperties.setPropToButton(printBtn, resButton.getString("PRINT"));
        }
        return printBtn;
    }

    public JButton getCnclBtn() {
        if (cnclBtn == null) {
            cnclBtn = new JButton();
            WidgetProperties.setPropToButton(cnclBtn, resButton.getString("CANCEL"));
        }
        return cnclBtn;
    }

    public JLabel getStnToLbl() {
        if (stnToLbl == null) {
            stnToLbl = new JLabel();
            stnToLbl.setText(resLabel.getString("STATION_FROM"));
            WidgetProperties.setPropToLabel(stnToLbl);
        }
        return stnToLbl;
    }

    public JLabel getStnToLbl_Value() {
        if (stnToLbl_Value == null) {
            stnToLbl_Value = new JLabel();
            stnToLbl_Value.setHorizontalAlignment(SwingConstants.CENTER);
            stnToLbl_Value.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, false));
            WidgetProperties.setPropToLabel(stnToLbl_Value);
        }
        return stnToLbl_Value;
    }

    public JLabel getDeliveredByValueLbl() {
        if (deliveredByLabel_Value == null) {
            deliveredByLabel_Value = new JLabel();
            deliveredByLabel_Value.setHorizontalAlignment(SwingConstants.CENTER);
            deliveredByLabel_Value.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, false));
            WidgetProperties.setPropToLabel(deliveredByLabel_Value);
        }
        return deliveredByLabel_Value;
    }

    public JLabel getDCDateLbl() {
        if (dcDateLbl == null) {
            dcDateLbl = new JLabel();
            dcDateLbl.setText(resLabel.getString("CHLN_DATE"));
            WidgetProperties.setPropToLabel(dcDateLbl);
        }
        return dcDateLbl;
    }

    public JLabel getDCDateValueLbl() {
        if (dcDateValueLbl == null) {
            dcDateValueLbl = new JLabel();
            dcDateValueLbl.setHorizontalAlignment(SwingConstants.CENTER);
            dcDateValueLbl.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, false));
            WidgetProperties.setPropToLabel(dcDateValueLbl);
        }
        return dcDateValueLbl;
    }

    public static void main(java.lang.String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            javax.swing.JFrame frame = new javax.swing.JFrame();
            DCViewPanel aServiceTabPanel;
            aServiceTabPanel = new DCViewPanel();
            frame.setContentPane(aServiceTabPanel);
            frame.setSize(1010, 575);
            frame.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }

                ;
            });
            frame.show();
            java.awt.Insets insets = frame.getInsets();
            frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
            frame.setVisible(true);
        } catch (Throwable exception) {
            System.err.println("Exception occurred in main() of javax.swing.JPanel");
            exception.printStackTrace(System.out);
        }
    }
}
