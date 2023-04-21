package com.ivis.xprocess.ui.processdesigner.print;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import com.ivis.xprocess.ui.UIPlugin;
import com.ivis.xprocess.ui.properties.ProcessDesignerMessages;
import com.ivis.xprocess.ui.util.FontAndColorManager;

public class PrintOptions {

    public static final int POINT_TO_INCH = 72;

    private static PrintOptions ourInstance;

    private Font myLabelFont = FontAndColorManager.getInstance().getFont("Tahoma:10:" + SWT.NONE);

    private Dimension[] myPaperSizes;

    private List<String> myPaperSizeIds;

    private String[] myLocalizedPaperSizes;

    private IPreferenceStore myPreferenceStore;

    private PrintOptions() {
        myPaperSizeIds = new ArrayList<String>();
        myPaperSizeIds.add(PrintPreferences.PaperSize.A);
        myPaperSizeIds.add(PrintPreferences.PaperSize.A0);
        myPaperSizeIds.add(PrintPreferences.PaperSize.A1);
        myPaperSizeIds.add(PrintPreferences.PaperSize.A2);
        myPaperSizeIds.add(PrintPreferences.PaperSize.A3);
        myPaperSizeIds.add(PrintPreferences.PaperSize.A4);
        myPaperSizeIds.add(PrintPreferences.PaperSize.A5);
        myPaperSizeIds.add(PrintPreferences.PaperSize.B);
        myPaperSizeIds.add(PrintPreferences.PaperSize.B5);
        myPaperSizeIds.add(PrintPreferences.PaperSize.C);
        myPaperSizeIds.add(PrintPreferences.PaperSize.D);
        myPaperSizeIds.add(PrintPreferences.PaperSize.E);
        myPaperSizeIds.add(PrintPreferences.PaperSize.ENV_DL);
        myPaperSizeIds.add(PrintPreferences.PaperSize.EXECUTIVE);
        myPaperSizeIds.add(PrintPreferences.PaperSize.LEGAL);
        myPaperSizeIds.add(PrintPreferences.PaperSize.LETTER);
        myPaperSizes = new Dimension[] { new Dimension(612, 792), new Dimension(2382, 3368), new Dimension(1684, 2382), new Dimension(1191, 1684), new Dimension(842, 1191), new Dimension(595, 842), new Dimension(421, 595), new Dimension(792, 1224), new Dimension(516, 726), new Dimension(1224, 1584), new Dimension(1584, 2448), new Dimension(2448, 3168), new Dimension(296, 684), new Dimension(522, 756), new Dimension(612, 1008), new Dimension(612, 792) };
        myLocalizedPaperSizes = new String[] { ProcessDesignerMessages.PrintOptions_A, ProcessDesignerMessages.PrintOptions_A0, ProcessDesignerMessages.PrintOptions_A1, ProcessDesignerMessages.PrintOptions_A2, ProcessDesignerMessages.PrintOptions_A3, ProcessDesignerMessages.PrintOptions_A4, ProcessDesignerMessages.PrintOptions_A5, ProcessDesignerMessages.PrintOptions_B, ProcessDesignerMessages.PrintOptions_B5, ProcessDesignerMessages.PrintOptions_C, ProcessDesignerMessages.PrintOptions_D, ProcessDesignerMessages.PrintOptions_E, ProcessDesignerMessages.PrintOptions_ENV_DL, ProcessDesignerMessages.PrintOptions_EXECUTIVE, ProcessDesignerMessages.PrintOptions_LEGAL, ProcessDesignerMessages.PrintOptions_LETTER };
        myPreferenceStore = UIPlugin.getDefault().getPreferenceStore();
    }

    public boolean fitToPage() {
        return myPreferenceStore.getBoolean(PrintPreferences.FIT_TO_PAGE);
    }

    public Dimension paperSize() {
        String paperSizeId = myPreferenceStore.getString(PrintPreferences.PAPER_SIZE);
        Dimension size = null;
        if (myPreferenceStore.getBoolean(PrintPreferences.CUSTOM_PAPER)) {
            size = customPaperSize();
        } else {
            int idx = myPaperSizeIds.indexOf(paperSizeId);
            size = (idx != -1) ? myPaperSizes[idx].getCopy() : null;
        }
        if ((size != null) && (orientation() == PrintPreferences.Orientation.LANDSCAPE)) {
            size.transpose();
        }
        return size;
    }

    public Dimension paperSize(String paperSizeId) {
        int idx = myPaperSizeIds.indexOf(paperSizeId);
        return (idx != -1) ? myPaperSizes[idx].getCopy() : null;
    }

    public String getCurrentPaperSizeId() {
        return myPreferenceStore.getString(PrintPreferences.PAPER_SIZE);
    }

    public String[] getLocalizedPaperSizes() {
        return myLocalizedPaperSizes;
    }

    public String getCurrentLocalizedPaperSize() {
        String paperSizeId = myPreferenceStore.getString(PrintPreferences.PAPER_SIZE);
        int idx = myPaperSizeIds.indexOf(paperSizeId);
        return (idx >= 0) ? myLocalizedPaperSizes[idx] : "";
    }

    public String getDefaultLocalizedPaperSize() {
        String paperSizeId = myPreferenceStore.getDefaultString(PrintPreferences.PAPER_SIZE);
        int idx = myPaperSizeIds.indexOf(paperSizeId);
        return (idx >= 0) ? myLocalizedPaperSizes[idx] : "";
    }

    public String getPaperId(int idx) {
        return myPaperSizeIds.get(idx);
    }

    public boolean isCustomPaper() {
        return myPreferenceStore.getBoolean(PrintPreferences.CUSTOM_PAPER);
    }

    public float getCustomPaperWidth() {
        return myPreferenceStore.getFloat(PrintPreferences.CUSTOM_PAPER_SIZE_WIDTH);
    }

    public float getCustomPaperHeight() {
        return myPreferenceStore.getFloat(PrintPreferences.CUSTOM_PAPER_SIZE_HEIGHT);
    }

    public boolean printBorder() {
        return myPreferenceStore.getBoolean(PrintPreferences.PRINT_BORDER);
    }

    public boolean printPageNumbers() {
        return myPreferenceStore.getBoolean(PrintPreferences.PRINT_PAGE_NUMBERS);
    }

    public boolean printDiagramName() {
        return myPreferenceStore.getBoolean(PrintPreferences.PRINT_DIAGRAM_NAME);
    }

    public boolean printDiagramLabel() {
        return printPageNumbers() || printDiagramName();
    }

    public int orientation() {
        return myPreferenceStore.getInt(PrintPreferences.ORIENTATION);
    }

    public float zoom() {
        return myPreferenceStore.getFloat(PrintPreferences.ZOOM);
    }

    private Dimension customPaperSize() {
        float customWidth = myPreferenceStore.getFloat(PrintPreferences.CUSTOM_PAPER_SIZE_WIDTH);
        float customHeight = myPreferenceStore.getFloat(PrintPreferences.CUSTOM_PAPER_SIZE_HEIGHT);
        return new Dimension((int) (customWidth * POINT_TO_INCH), (int) (customHeight * POINT_TO_INCH));
    }

    public Insets getMargins() {
        int left = (int) (myPreferenceStore.getFloat(PrintPreferences.LEFT_MARGIN) * POINT_TO_INCH);
        int top = (int) (myPreferenceStore.getFloat(PrintPreferences.TOP_MARGIN) * POINT_TO_INCH);
        int right = (int) (myPreferenceStore.getFloat(PrintPreferences.RIGHT_MARGIN) * POINT_TO_INCH);
        int bottom = (int) (myPreferenceStore.getFloat(PrintPreferences.BOTTOM_MARGIN) * POINT_TO_INCH);
        return new Insets(top, left, bottom, right);
    }

    public float getTopMargin() {
        return myPreferenceStore.getFloat(PrintPreferences.TOP_MARGIN);
    }

    public float getLeftMargin() {
        return myPreferenceStore.getFloat(PrintPreferences.LEFT_MARGIN);
    }

    public float getBottomMargin() {
        return myPreferenceStore.getFloat(PrintPreferences.BOTTOM_MARGIN);
    }

    public float getRightMargin() {
        return myPreferenceStore.getFloat(PrintPreferences.RIGHT_MARGIN);
    }

    public Font getDiagramLabelFont() {
        return myLabelFont;
    }

    public static PrintOptions getInstance() {
        if (ourInstance == null) {
            ourInstance = new PrintOptions();
        }
        return ourInstance;
    }
}
