package visualbiology.reactionEditor.figures;

import java.util.ArrayList;
import mySBML.utilities.SbmlProblem;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class KineticLawFigure extends RectangleFigure {

    private Label expressionLabel = new Label();

    private Label paramLabel = new Label();

    public KineticLawFigure() {
        expressionLabel.setForegroundColor(ColorConstants.darkGray);
        paramLabel.setForegroundColor(ColorConstants.darkGray);
        setLayoutManager(new GridLayout(1, true));
        add(expressionLabel, new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, GridData.VERTICAL_ALIGN_BEGINNING, true, true));
        add(paramLabel, new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, GridData.VERTICAL_ALIGN_BEGINNING, true, true, 1, 2));
    }

    public void setExpression(String expression) {
        expressionLabel.setText(expression);
        setToolTip(new Label(" Kinetic Law: \n " + new String(expression) + ' '));
    }

    public void setParameters(String parameters) {
        paramLabel.setText(parameters);
    }

    public void setProblemTooltip(ArrayList<SbmlProblem> problems) {
        String tooltipText = "";
        boolean errorFound = false;
        boolean warningFound = false;
        for (int i = 0; i < problems.size(); i++) if (problems.get(i).isError()) {
            if (!errorFound) {
                tooltipText += " Errors: \n";
                errorFound = true;
            }
            tooltipText += ' ' + problems.get(i).getMessage() + " \n";
        }
        for (int i = 0; i < problems.size(); i++) if (problems.get(i).isWarning()) {
            if (!warningFound) {
                if (errorFound) tooltipText += "\n";
                tooltipText += " Warnings:\n ";
                warningFound = true;
            }
            tooltipText += ' ' + problems.get(i).getMessage() + " \n";
        }
        setToolTip(new Label(tooltipText));
    }

    public void setLayout(Rectangle rect) {
        getParent().setConstraint(this, rect);
    }

    static final Color COLOR_GRADIENT_1 = new Color(null, 240, 217, 169);

    @Override
    protected void fillShape(Graphics graphics) {
        graphics.setForegroundColor(COLOR_GRADIENT_1);
        graphics.setBackgroundColor(ColorConstants.white);
        graphics.fillGradient(getBounds(), true);
    }
}
