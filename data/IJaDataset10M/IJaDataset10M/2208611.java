package org.gerhardb.jibs.viewer.contact;

import java.awt.BorderLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.gerhardb.jibs.Jibs;
import org.gerhardb.lib.print.contactSheet.ContactSheetInfo;
import org.gerhardb.lib.print.contactSheet.ContactSheetInfo.ResizeDPI;
import org.gerhardb.lib.print.contactSheet.ContactSheetInfo.ResizeType;
import org.gerhardb.lib.swing.JPanelRows;

/**
 * FilePanel
 */
public class RowColPanel extends JPanelRows {

    ContactSheetDisplay display;

    JSlider myRowSlider = new JSlider(SwingConstants.HORIZONTAL, 1, 8, 4);

    JSlider myColSlider = new JSlider(SwingConstants.HORIZONTAL, 1, 6, 3);

    JSlider myFrameSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 20, 1);

    JSlider myShadowSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 20, 0);

    JCheckBox myShowPicCount = new JCheckBox(Jibs.getString("ContactSheetOptions.16"));

    JCheckBox myShowFileName = new JCheckBox(Jibs.getString("ContactSheetOptions.18"));

    JCheckBox myShowFileSize = new JCheckBox(Jibs.getString("ContactSheetOptions.19"));

    @SuppressWarnings({ "rawtypes", "unchecked" })
    JComboBox<?> myResizeType = new JComboBox(ContactSheetInfo.ResizeType.values());

    @SuppressWarnings({ "rawtypes", "unchecked" })
    JComboBox<?> myResizeDPI = new JComboBox(ContactSheetInfo.ResizeDPI.values());

    RowColPanel(ContactSheetDisplay csd) {
        this.display = csd;
        this.myShowFileName.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (RowColPanel.this.myShowFileName.isSelected()) {
                    RowColPanel.this.myShowPicCount.setEnabled(true);
                } else {
                    RowColPanel.this.myShowPicCount.setEnabled(false);
                }
            }
        });
        this.myShowPicCount.setToolTipText(Jibs.getString("ContactSheetOptions.33"));
        this.myRowSlider.setSnapToTicks(true);
        this.myRowSlider.setPaintLabels(true);
        this.myRowSlider.setPaintTicks(true);
        this.myRowSlider.setMajorTickSpacing(1);
        this.myColSlider.setSnapToTicks(true);
        this.myColSlider.setPaintLabels(true);
        this.myColSlider.setPaintTicks(true);
        this.myColSlider.setMajorTickSpacing(1);
        this.myFrameSlider.setSnapToTicks(true);
        this.myFrameSlider.setPaintLabels(true);
        this.myFrameSlider.setPaintTicks(true);
        this.myFrameSlider.setMajorTickSpacing(1);
        this.myShadowSlider.setSnapToTicks(true);
        this.myShadowSlider.setPaintLabels(true);
        this.myShadowSlider.setPaintTicks(true);
        this.myShadowSlider.setMajorTickSpacing(1);
        JPanel row = this.topRow();
        row.add(new JLabel(Jibs.getString("ContactSheetOptions.6")));
        row.add(this.myRowSlider);
        row = this.nextRow();
        row.add(new JLabel(Jibs.getString("ContactSheetOptions.7")));
        row.add(this.myColSlider);
        row = this.nextRow();
        row.add(this.myShowFileName);
        row.add(this.myShowPicCount);
        row = this.nextRow();
        row.add(this.myShowFileSize);
        row = this.nextRow(new BorderLayout());
        row.add(new JLabel("Size of Frame    "), BorderLayout.WEST);
        row.add(this.myFrameSlider, BorderLayout.CENTER);
        row = this.nextRow(new BorderLayout());
        row.add(new JLabel("Size of Shadow"), BorderLayout.WEST);
        row.add(this.myShadowSlider, BorderLayout.CENTER);
        row = this.nextRow();
        row.add(new JLabel("Image Quality: "));
        row.add(this.myResizeType);
        row = this.nextRow();
        row.add(new JLabel("Image DPI: "));
        row.add(this.myResizeDPI);
    }

    private static final String ROW_COUNT = "row_count";

    private static final String COLUMN_COUNT = "column_count";

    private static final String CT_FRAME_SIZE = "ct_frame_size";

    private static final String CT_SHADOW_SIZE = "ct_shadow_size";

    private static final String CT_SHOW_PIC_NAME = "ct_show_pic_name";

    private static final String CT_SHOW_PIC_COUNT = "ct_show_pic_count";

    private static final String CT_SHOW_PIC_SIZE = "ct_show_pic_size";

    private static final String CT_TYPE = "ct_resize_type";

    private static final String CT_DPI = "ct_resize_dpi";

    void savePreferences() {
        this.display.myPrefs.putInt(ROW_COUNT, this.myRowSlider.getValue());
        this.display.myPrefs.putInt(COLUMN_COUNT, this.myColSlider.getValue());
        this.display.myPrefs.putInt(CT_FRAME_SIZE, this.myFrameSlider.getValue());
        this.display.myPrefs.putInt(CT_SHADOW_SIZE, this.myShadowSlider.getValue());
        this.display.myPrefs.putBoolean(CT_SHOW_PIC_NAME, this.myShowFileName.isSelected());
        this.display.myPrefs.putBoolean(CT_SHOW_PIC_COUNT, this.myShowPicCount.isSelected());
        this.display.myPrefs.putBoolean(CT_SHOW_PIC_SIZE, this.myShowFileSize.isSelected());
        this.display.myPrefs.put(CT_TYPE, this.myResizeType.getSelectedItem().toString());
        this.display.myPrefs.put(CT_DPI, this.myResizeDPI.getSelectedItem().toString());
    }

    void lookupPrefences() {
        this.myRowSlider.setValue(this.display.myPrefs.getInt(ROW_COUNT, 4));
        this.myColSlider.setValue(this.display.myPrefs.getInt(COLUMN_COUNT, 3));
        this.myFrameSlider.setValue(this.display.myPrefs.getInt(CT_FRAME_SIZE, 1));
        this.myShadowSlider.setValue(this.display.myPrefs.getInt(CT_SHADOW_SIZE, 0));
        this.myShowFileName.setSelected(this.display.myPrefs.getBoolean(CT_SHOW_PIC_NAME, false));
        this.myShowPicCount.setSelected(this.display.myPrefs.getBoolean(CT_SHOW_PIC_COUNT, false));
        this.myShowFileSize.setSelected(this.display.myPrefs.getBoolean(CT_SHOW_PIC_SIZE, false));
        ResizeType type = ResizeType.fromString(this.display.myPrefs.get(CT_TYPE, ResizeType.EXSHARP.toString()));
        this.myResizeType.setSelectedItem(type);
        ResizeDPI dpi = ResizeDPI.fromString(this.display.myPrefs.get(CT_DPI, ResizeDPI.DPI_300.toString()));
        this.myResizeDPI.setSelectedItem(dpi);
    }
}
