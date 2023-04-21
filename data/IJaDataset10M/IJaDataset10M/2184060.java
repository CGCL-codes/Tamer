package com.prolix.editor.graph.figures.points;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import com.prolix.editor.LDT_Constrains;
import com.prolix.editor.graph.figures.DrawImageFigure;
import com.prolix.editor.graph.model.activities.ModelSelectionPoint;

public class SelectionPointFigure extends Figure {

    private Color color;

    private int numberToSelect;

    private Image image;

    private DrawImageFigure environments;

    private DrawImageFigure operations;

    public SelectionPointFigure(ModelSelectionPoint selectionPoint) {
        super();
        color = selectionPoint.getColor();
        image = selectionPoint.getIcon();
        numberToSelect = selectionPoint.getNumberToSelect();
        setSize(selectionPoint.getSize());
        environments = new DrawImageFigure(LDT_Constrains.ImageEnvironments, LDT_Constrains.ImageNoEnvironments, 30, 6);
        operations = new DrawImageFigure(LDT_Constrains.ICON_OPERATION_ACTIVITY, LDT_Constrains.ICON_OPERATION_ACTIVITY_EMPTY, 35, 30);
        add(environments);
    }

    protected void paintFigure(Graphics graphics) {
        graphics.setAntialias(SWT.ON);
        graphics.setInterpolation(SWT.HIGH);
        graphics.setBackgroundColor(LDT_Constrains.ColorPointBorder);
        Rectangle rec = getBounds().getCopy();
        graphics.fillRectangle(rec);
        graphics.setBackgroundColor(color);
        rec.height -= 4;
        rec.width -= 4;
        rec.x += 2;
        rec.y += 2;
        graphics.fillRectangle(rec);
        graphics.drawImage(image, rec.x + 5, rec.y + 6);
        graphics.setFont(LDT_Constrains.FontFigurSelect);
        graphics.drawString("select: " + getNumberString(), new Point(rec.x + 5, rec.y + 37));
        graphics.setFont(LDT_Constrains.FontFigurSmall);
        environments.setBounds(rec.getCopy());
    }

    private String getNumberString() {
        if (numberToSelect == 0) {
            return "All";
        }
        return "" + numberToSelect;
    }

    public void update(ModelSelectionPoint activity) {
        setLocation(activity.getLocation());
        color = activity.getColor();
        int countEnvironments = activity.getEnvironments().size();
        environments.update(countEnvironments > 0, countEnvironments > 0 ? countEnvironments : -1);
        operations.update(activity.getInteractionOperationsCount() > 0, activity.getInteractionOperationsCount() > 0 ? activity.getInteractionOperationsCount() : -1);
        numberToSelect = activity.getNumberToSelect();
    }
}
