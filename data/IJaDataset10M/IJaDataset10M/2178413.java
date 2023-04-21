package com.prolix.editor.main.workspace.export.check.treemodel;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import com.prolix.editor.graph.templates.commands.dialogs.ProlixSWTUtils;

/**
 * @author naien
 * 
 */
public class CheckExportErrorListEntry extends Composite {

    private CheckElement element;

    /**
	 * @param parent
	 * @param style
	 */
    public CheckExportErrorListEntry(Composite parent, CheckElement element) {
        super(parent, SWT.NONE);
        this.element = element;
        this.setupView();
        this.addDrawListener();
    }

    private void addDrawListener() {
        this.addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e) {
                Composite canvas = (Composite) e.widget;
                int x = canvas.getBounds().width;
                int y = canvas.getBounds().height;
                int arcWidth = 25;
                int arcHeight = 25;
                e.gc.setLineWidth(2);
                e.gc.setBackground(ColorConstants.white);
                e.gc.setAntialias(SWT.ON);
                e.gc.setInterpolation(SWT.HIGH);
                e.gc.fillRoundRectangle(1, 1, x - 2, y - 2, arcWidth, arcHeight);
                e.gc.drawRoundRectangle(1, 1, x - 2, y - 2, arcWidth, arcHeight);
            }
        });
    }

    protected CheckElement getElement() {
        return this.element;
    }

    protected void setupView() {
        this.setLayoutData(ProlixSWTUtils.gridDataFillHorizonal());
        int spalten = 2;
        if (this.element.hasAddImage()) {
            spalten = 3;
        }
        this.setLayout(ProlixSWTUtils.gridLayout(spalten, false, 15, 15));
        Label label = new Label(this, SWT.NONE);
        label.setImage(this.element.getImage());
        label.setBackground(ColorConstants.white);
        label = new Label(this, SWT.WRAP);
        label.setText(element.getMessage());
        label.setLayoutData(ProlixSWTUtils.gridDataFillHorizonalCenter());
        label.setBackground(ColorConstants.white);
        if (this.element.hasAddImage()) {
            label = new Label(this, SWT.NONE);
            label.setImage(this.element.getAddImage());
        }
    }
}
