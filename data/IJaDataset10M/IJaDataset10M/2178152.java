package data;

import main.OpenPlotTool;
import net.smplmathparser.MathParserException;
import plot.PlotDataSet;
import plot.PlotFunction;
import plot.PlotPage;
import plot.PlotPoint;
import plot.cartesian.CartesianDataSet;
import plot.cartesian.CartesianFunction;
import plot.cartesian.CartesianPoint;
import exceptions.NullDataValueException;
import gui.cartesian.CartesianDataDialog;
import gui.cartesian.CartesianFunctionDialog;
import gui.cartesian.CartesianPointDialog;

public class CartesianDataManager {

    public static void addDataSet(PlotPage page) throws NumberFormatException, NullDataValueException {
        CartesianDataDialog dialog = new CartesianDataDialog(null);
        dialog.setVisible(true);
        if (dialog.getPressed() == CartesianDataDialog.ADD_PRESSED) {
            CartesianDataSet dataSet = dialog.getDataSet();
            page.addData(dataSet);
            OpenPlotTool.getMainFrame().getDataSetListModel().addElement(dataSet);
            page.repaint();
        }
        dialog.dispose();
    }

    public static void addFunction(PlotPage page) throws NullDataValueException, MathParserException {
        CartesianFunctionDialog dialog = new CartesianFunctionDialog(null);
        dialog.setVisible(true);
        if (dialog.getPressed() == CartesianFunctionDialog.ADD_PRESSED) {
            CartesianFunction function = dialog.getFunction();
            page.addFunction(function);
            OpenPlotTool.getMainFrame().getFunctionListModel().addElement(function);
            page.repaint();
        }
        dialog.dispose();
    }

    public static void addPoint(PlotPage page) throws NumberFormatException {
        CartesianPointDialog dialog = new CartesianPointDialog(null);
        dialog.setVisible(true);
        if (dialog.getPressed() == CartesianPointDialog.ADD_PRESSED) {
            CartesianPoint point = dialog.getPoint();
            page.addPoint(point);
            OpenPlotTool.getMainFrame().getPointListModel().addElement(point);
            page.repaint();
        }
        dialog.dispose();
    }

    public static void editDataSet(PlotPage page, int selectedIndex) throws NumberFormatException, NullDataValueException {
        CartesianDataSet currentDataSet = (CartesianDataSet) OpenPlotTool.getMainFrame().getDataSetList().getSelectedValue();
        if (currentDataSet != null) {
            CartesianDataDialog dialog = new CartesianDataDialog(currentDataSet);
            dialog.setVisible(true);
            if (dialog.getPressed() == CartesianFunctionDialog.ADD_PRESSED) {
                CartesianDataSet dataSet = dialog.getDataSet();
                page.getPlotData().set(selectedIndex, dataSet);
                OpenPlotTool.getMainFrame().getDataSetListModel().removeElement(currentDataSet);
                OpenPlotTool.getMainFrame().getDataSetListModel().add(selectedIndex, dataSet);
                page.repaint();
            }
            dialog.dispose();
        }
    }

    public static void editFunction(PlotPage page, int selectedIndex) throws MathParserException, NullDataValueException {
        CartesianFunction currentFunction = (CartesianFunction) OpenPlotTool.getMainFrame().getFunctionList().getSelectedValue();
        if (currentFunction != null) {
            CartesianFunctionDialog dialog = new CartesianFunctionDialog(currentFunction);
            dialog.setVisible(true);
            if (dialog.getPressed() == CartesianFunctionDialog.ADD_PRESSED) {
                CartesianFunction newFunction = dialog.getFunction();
                int curIndex = page.getPlotFunctions().indexOf(currentFunction);
                page.getPlotFunctions().set(curIndex, newFunction);
                OpenPlotTool.getMainFrame().getFunctionListModel().removeElement(currentFunction);
                OpenPlotTool.getMainFrame().getFunctionListModel().add(selectedIndex, newFunction);
                page.repaint();
            }
            dialog.dispose();
        }
    }

    public static void editPoint(PlotPage page, int selectedIndex) throws NumberFormatException {
        CartesianPoint currentPoint = (CartesianPoint) OpenPlotTool.getMainFrame().getPointList().getSelectedValue();
        if (currentPoint != null) {
            CartesianPointDialog dialog = new CartesianPointDialog(currentPoint);
            dialog.setVisible(true);
            if (dialog.getPressed() == CartesianFunctionDialog.ADD_PRESSED) {
                CartesianPoint newPoint = dialog.getPoint();
                int curIndex = page.getPlotPoints().indexOf(currentPoint);
                page.getPlotPoints().set(curIndex, newPoint);
                OpenPlotTool.getMainFrame().getPointListModel().removeElement(currentPoint);
                OpenPlotTool.getMainFrame().getPointListModel().add(selectedIndex, newPoint);
                page.repaint();
            }
            dialog.dispose();
        }
    }

    public static void removeDataSet(PlotPage page) {
        PlotDataSet dataSet = (PlotDataSet) OpenPlotTool.getMainFrame().getDataSetList().getSelectedValue();
        if (dataSet != null) {
            OpenPlotTool.getMainFrame().getDataSetListModel().removeElement(dataSet);
            page.removeData(dataSet);
            page.repaint();
        }
    }

    public static void removeFunction(PlotPage page) {
        PlotFunction function = (PlotFunction) OpenPlotTool.getMainFrame().getFunctionList().getSelectedValue();
        if (function != null) {
            OpenPlotTool.getMainFrame().getFunctionListModel().removeElement(function);
            page.removeFunction(function);
            page.repaint();
        }
    }

    public static void removePoint(PlotPage page) {
        PlotPoint point = (PlotPoint) OpenPlotTool.getMainFrame().getPointList().getSelectedValue();
        if (point != null) {
            OpenPlotTool.getMainFrame().getPointListModel().removeElement(point);
            page.removePoint(point);
            page.repaint();
        }
    }
}
