package org.gerhardb.jibs.viewer.contact;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.gerhardb.jibs.Jibs;
import org.gerhardb.lib.print.*;
import org.gerhardb.lib.print.contactSheet.*;
import org.gerhardb.lib.swing.JPanelRows;

/**
 * OptionsPanel
 */
public class OptionsPanel extends JPanelRows {

    private final String DEFAULT_LEFT_HEADER = "Showing " + ListToDraw.PIC_FIRST_INDEX + "-" + ListToDraw.PIC_LAST_INDEX;

    final String DEFAULT_RIGHT_HEADER = ListToDraw.PIC_COUNT + " " + Jibs.getString("ContactSheetOptions.126");

    ContactSheetDisplay display;

    JTextField myHeader = new JTextField(50);

    JTextField myHeaderLeft = new JTextField(30);

    JTextField myHeaderRight = new JTextField(30);

    JTextField myFooter = new JTextField(50);

    JTextField myFooterLeft = new JTextField(30);

    JTextField myFooterRight = new JTextField(30);

    JCheckBox myShowLines = new JCheckBox(Jibs.getString("ContactSheetOptions.17"));

    OptionsPanel(ContactSheetDisplay csd) {
        this.display = csd;
        JButton defaultBtn = new JButton(Jibs.getString("ContactSheetOptions.48"));
        defaultBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                OptionsPanel.this.myHeaderLeft.setText(OptionsPanel.this.DEFAULT_LEFT_HEADER);
                OptionsPanel.this.myHeader.setText(ListToDraw.CURRENT_DIRECTORY);
                OptionsPanel.this.myHeaderRight.setText(OptionsPanel.this.DEFAULT_RIGHT_HEADER);
                OptionsPanel.this.myFooterLeft.setText(HeaderFooterInfo.PRINTED_BY_JIBS);
                OptionsPanel.this.myFooter.setText(HeaderFooterInfo.DEFAULT_FOOTER);
                OptionsPanel.this.myFooterRight.setText(HeaderFooterInfo.JIBS_WEB_SITE);
            }
        });
        JPanelRows topWest = new JPanelRows();
        JPanel row = topWest.topRow();
        row.add(this.myShowLines);
        row = topWest.nextRow();
        row.add(defaultBtn);
        JPanelRows topEast = new JPanelRows();
        row = topEast.topRow();
        row.add(new JLabel(Jibs.getString("ContactSheetOptions.99") + " " + HeaderFooterInfo.PAGE_INDEX + " " + Jibs.getString("ContactSheetOptions.102")));
        row = topEast.nextRow();
        row.add(new JLabel(Jibs.getString("ContactSheetOptions.103") + " " + HeaderFooterInfo.PAGE_COUNT + " " + Jibs.getString("ContactSheetOptions.106")));
        row = topEast.nextRow();
        row.add(new JLabel(Jibs.getString("ContactSheetOptions.107") + " " + ListToDraw.CURRENT_DIRECTORY + " " + Jibs.getString("ContactSheetOptions.110")));
        row = topEast.nextRow();
        row.add(new JLabel(Jibs.getString("ContactSheetOptions.107") + " " + ListToDraw.PIC_FIRST_INDEX + " " + Jibs.getString("ContactSheetOptions.120")));
        row = topEast.nextRow();
        row.add(new JLabel(Jibs.getString("ContactSheetOptions.107") + " " + ListToDraw.PIC_LAST_INDEX + " " + Jibs.getString("ContactSheetOptions.121")));
        row = topEast.nextRow();
        row.add(new JLabel(Jibs.getString("ContactSheetOptions.107") + " " + ListToDraw.PIC_COUNT + " " + Jibs.getString("ContactSheetOptions.122")));
        JPanel top = new JPanel(new BorderLayout());
        top.add(topWest, BorderLayout.WEST);
        top.add(topEast, BorderLayout.EAST);
        row = this.topRow();
        row.add(top);
        row = this.nextRow();
        row.add(new JLabel(Jibs.getString("ContactSheetOptions.58")));
        row = this.nextRow();
        row.add(new JLabel(Jibs.getString("ContactSheetOptions.111") + Jibs.getString("colon") + "   "));
        row.add(this.myHeaderLeft);
        row = this.nextRow();
        row.add(new JLabel(Jibs.getString("ContactSheetOptions.60") + Jibs.getString("colon") + " "));
        row.add(this.myHeader);
        row = this.nextRow();
        row.add(new JLabel(Jibs.getString("ContactSheetOptions.63") + Jibs.getString("colon") + " "));
        row.add(this.myHeaderRight);
        row = this.nextRow();
        row.add(new JLabel(Jibs.getString("ContactSheetOptions.66")));
        row = this.nextRow();
        row.add(new JLabel(Jibs.getString("ContactSheetOptions.114") + Jibs.getString("colon") + "     "));
        row.add(this.myFooterLeft);
        row = this.nextRow();
        row.add(new JLabel(Jibs.getString("ContactSheetOptions.68") + Jibs.getString("colon") + " "));
        row.add(this.myFooter);
        row = this.nextRow();
        row.add(new JLabel(Jibs.getString("ContactSheetOptions.117") + Jibs.getString("colon") + "   "));
        row.add(this.myFooterRight);
    }

    private static final String CT_SHOW_LINES = "ct_show_lines";

    private static final String CT_HEADER = "ct_header";

    private static final String CT_FOOTER = "ct_footer";

    private static final String CT_RIGHT_FOOTER = "ct_right_footer";

    private static final String CT_LEFT_FOOTER = "ct_left_footer";

    private static final String CT_RIGHT_HEADER = "ct_right_header";

    private static final String CT_LEFT_HEADER = "ct_left_header";

    void savePreferences() {
        this.display.myPrefs.putBoolean(CT_SHOW_LINES, this.myShowLines.isSelected());
        this.display.myPrefs.put(CT_HEADER, this.myHeader.getText());
        this.display.myPrefs.put(CT_LEFT_HEADER, this.myHeaderLeft.getText());
        this.display.myPrefs.put(CT_RIGHT_HEADER, this.myHeaderRight.getText());
        this.display.myPrefs.put(CT_FOOTER, this.myFooter.getText());
        this.display.myPrefs.put(CT_LEFT_FOOTER, this.myFooterLeft.getText());
        this.display.myPrefs.put(CT_RIGHT_FOOTER, this.myFooterRight.getText());
    }

    void lookupPrefences() {
        this.myShowLines.setSelected(this.display.myPrefs.getBoolean(CT_SHOW_LINES, true));
        this.myHeader.setText(this.display.myPrefs.get(CT_HEADER, ListToDraw.CURRENT_DIRECTORY));
        this.myHeaderLeft.setText(this.display.myPrefs.get(CT_LEFT_HEADER, ""));
        this.myHeaderRight.setText(this.display.myPrefs.get(CT_RIGHT_HEADER, ""));
        this.myFooter.setText(this.display.myPrefs.get(CT_FOOTER, HeaderFooterInfo.DEFAULT_FOOTER));
        this.myFooterLeft.setText(this.display.myPrefs.get(CT_LEFT_FOOTER, HeaderFooterInfo.PRINTED_BY_JIBS));
        this.myFooterRight.setText(this.display.myPrefs.get(CT_RIGHT_FOOTER, HeaderFooterInfo.JIBS_WEB_SITE));
    }
}
