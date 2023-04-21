package plot.cartesian;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import net.smplmathparser.EvaluationTree;
import net.smplmathparser.MathParser;
import net.smplmathparser.MathParserException;
import plot.Axis;
import plot.PlotFunction;

public class CartesianFunction extends PlotFunction {

    private EvaluationTree functionTree = null;

    private double jumpSize = 100;

    private MathParser parser = new MathParser();

    public CartesianFunction(String funcStr) throws MathParserException {
        this.setFunctionString(funcStr);
        functionTree = parser.parse(this.getFunctionString());
    }

    @Override
    public void draw(Graphics g, Axis axis) {
        try {
            if (this.getDrawMethod() == PlotFunction.LINE_DRAW) {
                lineDraw(g, axis);
            } else if (this.getDrawMethod() == PlotFunction.POINT_DRAW) {
                pointDraw(g, axis);
            }
        } catch (MathParserException e) {
            e.printStackTrace();
        }
    }

    public double getJumpSize() {
        return jumpSize;
    }

    private void lineDraw(Graphics g, Axis axis) throws MathParserException {
        Graphics2D gc = (Graphics2D) g;
        CartesianAxis caxis = (CartesianAxis) axis;
        CartesianSettings settings = (CartesianSettings) caxis.getPlotSettings();
        gc.setColor(this.getDrawColor());
        gc.setStroke(new BasicStroke(this.getDrawSize()));
        double xValue = settings.getxMin();
        double spacingSize = settings.getxSplitSize() / jumpSize;
        while (xValue <= settings.getxMax()) {
            if (!functionTree.isConstant()) {
                functionTree.setVariable("x", xValue);
            }
            double yValue = functionTree.evaluate();
            if (yValue >= settings.getyMin() && yValue <= settings.getyMax()) {
                double drawXPoint1 = caxis.getxIndent() + caxis.getxLowerSectionPad() + ((xValue - settings.getxMin() - caxis.getxBelow()) / settings.getxSplitSize()) * caxis.getxSectionSize();
                int iDrawXPoint1 = (int) Math.round(drawXPoint1);
                double drawYPoint1 = caxis.getyIndent() + caxis.getyUpperSectionPad() + ((settings.getyMax() - caxis.getyOver() - yValue) / settings.getySplitSize()) * caxis.getySectionSize();
                int iDrawYPoint1 = (int) Math.round(drawYPoint1);
                xValue = xValue + spacingSize;
                if (!functionTree.isConstant()) {
                    functionTree.setVariable("x", xValue);
                }
                yValue = functionTree.evaluate();
                if (yValue >= settings.getyMin() && yValue <= settings.getyMax()) {
                    double drawXPoint2 = caxis.getxIndent() + caxis.getxLowerSectionPad() + ((xValue - settings.getxMin() - caxis.getxBelow()) / settings.getxSplitSize()) * caxis.getxSectionSize();
                    int iDrawXPoint2 = (int) Math.round(drawXPoint2);
                    double drawYPoint2 = caxis.getyIndent() + caxis.getyUpperSectionPad() + ((settings.getyMax() - caxis.getyOver() - yValue) / settings.getySplitSize()) * caxis.getySectionSize();
                    int iDrawYPoint2 = (int) Math.round(drawYPoint2);
                    gc.drawLine(iDrawXPoint1, iDrawYPoint1, iDrawXPoint2, iDrawYPoint2);
                }
            } else {
                xValue = xValue + spacingSize;
            }
        }
    }

    private void pointDraw(Graphics g, Axis axis) throws MathParserException {
        CartesianAxis caxis = (CartesianAxis) axis;
        CartesianSettings settings = (CartesianSettings) caxis.getPlotSettings();
        g.setColor(this.getDrawColor());
        double xValue = settings.getxMin() + caxis.getxBelow();
        while (xValue <= settings.getxMax()) {
            if (!functionTree.isConstant()) {
                functionTree.setVariable("x", xValue);
            }
            double yValue = functionTree.evaluate();
            if (yValue >= settings.getyMin() && yValue <= settings.getyMax()) {
                double drawXPoint = caxis.getxIndent() + caxis.getxLowerSectionPad() + ((xValue - settings.getxMin() - caxis.getxBelow()) / settings.getxSplitSize()) * caxis.getxSectionSize();
                int iDrawXPoint = (int) Math.round(drawXPoint);
                double drawYPoint = caxis.getyIndent() + caxis.getyUpperSectionPad() + ((settings.getyMax() - caxis.getyOver() - yValue) / settings.getySplitSize()) * caxis.getySectionSize();
                int iDrawYPoint = (int) Math.round(drawYPoint);
                g.fillRect(iDrawXPoint - 2, iDrawYPoint - 2, 5, 5);
            }
            xValue = xValue + settings.getxSplitSize();
        }
    }

    public void setJumpSize(double jumpSize) {
        this.jumpSize = jumpSize;
    }

    @Override
    public String toString() {
        return "XYFunction: " + this.getFunctionString();
    }
}
