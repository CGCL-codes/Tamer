package com.ivis.xprocess.ui.processdesigner.print;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.ScaledGraphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import com.ivis.xprocess.ui.UIPlugin;
import com.ivis.xprocess.ui.processdesigner.print.PrintDocument.PrintDocumentListener;
import com.ivis.xprocess.ui.processdesigner.print.PrintDocument.PrintDriver;
import com.ivis.xprocess.ui.properties.ProcessDesignerMessages;

public class PreviewDialog extends Dialog implements PrintDocumentListener {

    private static final int OPTIONS_ID = IDialogConstants.CLIENT_ID + 1;

    private final IFigure myFigure;

    private final PrintDocument myPreviewDocument;

    private final String myDiagramLabel;

    private Composite myPreviewPane;

    private Combo myPreviewZoomBox;

    private String[] zoomIds = new String[] { ProcessDesignerMessages.PreviewDialog_zoom400, ProcessDesignerMessages.PreviewDialog_zoom200, ProcessDesignerMessages.PreviewDialog_zoom100, ProcessDesignerMessages.PreviewDialog_zoom75, ProcessDesignerMessages.PreviewDialog_zoom50, ProcessDesignerMessages.PreviewDialog_zoom25, ProcessDesignerMessages.PreviewDialog_zoom12 };

    private float[] zoomValues = new float[] { 4.0f, 2.0f, 1.0f, .75f, .5f, .25f, .125f };

    protected PreviewDialog(IFigure figure, String diagramLabel, Shell parentShell) {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
        myFigure = figure;
        myDiagramLabel = diagramLabel;
        myPreviewDocument = new PrintDocument(myFigure);
        myPreviewDocument.addListener(this);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(ProcessDesignerMessages.PreviewDialog_title);
    }

    @Override
    public boolean close() {
        myPreviewDocument.removeListener(this);
        return super.close();
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        area.setLayout(new GridLayout(2, false));
        ScrolledComposite scrollPane = new ScrolledComposite(area, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        scrollPane.setBackground(ColorConstants.buttonDarker);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.horizontalSpan = 2;
        scrollPane.setLayoutData(gd);
        myPreviewPane = new Composite(scrollPane, SWT.NONE);
        myPreviewPane.setBackground(ColorConstants.buttonDarker);
        myPreviewPane.setLayout(new GridLayout(1, true));
        scrollPane.setContent(myPreviewPane);
        Label previewZoomLabel = new Label(area, SWT.NONE);
        previewZoomLabel.setText(ProcessDesignerMessages.PreviewDialog_zoomLabelText);
        previewZoomLabel.setLayoutData(new GridData(SWT.NONE, SWT.CENTER, false, false));
        myPreviewZoomBox = new Combo(area, SWT.READ_ONLY);
        myPreviewZoomBox.setLayoutData(new GridData(SWT.NONE, SWT.CENTER, false, false));
        myPreviewZoomBox.setItems(zoomIds);
        myPreviewZoomBox.select(4);
        myPreviewZoomBox.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                updatePreview();
            }
        });
        updatePreview();
        return area;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, OPTIONS_ID, ProcessDesignerMessages.PreviewDialog_optionButtonText, false);
        createButton(parent, IDialogConstants.OK_ID, ProcessDesignerMessages.PreviewDialog_printButtonText, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (OPTIONS_ID == buttonId) {
            PreferencePage pp = new PrintPreferencePage();
            pp.setTitle(ProcessDesignerMessages.PreviewDialog_optionsTitle);
            PreferenceManager pm = new PreferenceManager();
            pm.addToRoot(new PreferenceNode(PrintPreferencePage.ID, pp));
            PreferenceDialog d = new PreferenceDialog(getShell(), pm);
            d.create();
            d.open();
        }
        super.buttonPressed(buttonId);
    }

    @Override
    protected void okPressed() {
        super.okPressed();
        PrintDialog dialog = new PrintDialog(getParentShell(), SWT.NONE);
        PrinterData printerData = dialog.open();
        if (printerData != null) {
            Printer printer = new Printer(printerData);
            try {
                PrintDocument pd = new PrintDocument(myFigure);
                pd.print(new PrinterPrintDriver(printer, myDiagramLabel), printerData.startPage, printerData.endPage);
            } catch (PrintException e) {
                UIPlugin.log("", IStatus.ERROR, e);
            } finally {
                printer.dispose();
            }
        }
    }

    public void documentChanged(PrintDocument document) {
        updatePreview();
    }

    private void updatePreview() {
        for (Control child : myPreviewPane.getChildren()) {
            child.dispose();
        }
        Dimension pageSize = PrintOptions.getInstance().paperSize();
        int colCount = myPreviewDocument.getColCount();
        int rowCount = myPreviewDocument.getRowCount();
        GridLayout gd = (GridLayout) myPreviewPane.getLayout();
        gd.numColumns = colCount;
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                Control pageControl = new PageControl(myPreviewPane, row, col);
                pageControl.setBackground(ColorConstants.white);
                Dimension size = zoom(pageSize);
                pageControl.setSize(size.width, size.height);
            }
        }
        myPreviewPane.layout(true);
        myPreviewPane.setSize(myPreviewPane.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    private Dimension zoom(Dimension origSize) {
        Dimension size = origSize.getCopy();
        float currentZoom = zoomValues[myPreviewZoomBox.getSelectionIndex()];
        size.scale(currentZoom, currentZoom);
        return size;
    }

    private class PageControl extends Canvas {

        private final int myRow;

        private final int myCol;

        private final PagePrintDriver myPrintDriver;

        private Point mySize;

        public PageControl(Composite parent, int row, int col) {
            super(parent, SWT.BORDER);
            myRow = row;
            myCol = col;
            myPrintDriver = new PagePrintDriver();
            addPaintListener(new PaintListener() {

                public void paintControl(PaintEvent e) {
                    try {
                        myPrintDriver.setGraphics(new ScaledGraphics(new SWTGraphics(e.gc)));
                        myPreviewDocument.printPage(myPrintDriver, myRow, myCol);
                    } catch (PrintException ex) {
                        UIPlugin.log("", IStatus.ERROR, ex);
                    }
                }
            });
        }

        @Override
        public void setSize(int x, int y) {
            mySize = new Point(x, y);
            super.setSize(x, y);
        }

        @Override
        public Point computeSize(int wHint, int hHint, boolean changed) {
            return mySize;
        }

        private class PagePrintDriver extends PrintDriver.Lazy {

            private Graphics myGraphics;

            public String getPageLabel(int row, int col) {
                return myDiagramLabel;
            }

            public Graphics getGraphics(int row, int col) {
                return myGraphics;
            }

            public Dimension getVisiblePageSize(int row, int col) {
                return new Dimension(PageControl.this.getSize());
            }

            public void setGraphics(Graphics graphics) {
                myGraphics = graphics;
            }
        }
    }
}
