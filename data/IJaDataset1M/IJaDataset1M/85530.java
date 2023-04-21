package org.skirmishgame.financewizard.gui.generalgrid;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

@SuppressWarnings("serial")
public class GeneralGrid extends JPanel {

    protected GeneralGridInternalModel internalModel = null;

    private GridBagLayout layout = new GridBagLayout();

    private InternalMouseListener listener = new InternalMouseListener();

    private GeneralGridBorder defaultInner = new GeneralGridBorder(1, 1, 1, 1, Color.LIGHT_GRAY.brighter());

    public GeneralGrid(int width, int height) {
        this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        this.setLayout(layout);
        layout.columnWidths = new int[width];
        layout.rowHeights = new int[height];
        double[] columnWeights = new double[width];
        for (int i = 0; i < width; i++) columnWeights[i] = 1.0;
        layout.columnWeights = columnWeights;
        double[] rowWeights = new double[height];
        for (int i = 0; i < height; i++) rowWeights[i] = 1.0;
        layout.rowWeights = rowWeights;
        internalModel = new GeneralGridInternalModel(width, height);
        reorderContainer();
        ResetBorders();
        addGeneralGridComponentMouseListener(listener);
    }

    protected void reorderContainer() {
        this.removeAll();
        GridBagConstraints c = new GridBagConstraints();
        for (int columnId = 0; columnId < internalModel.GetColumnCount(); columnId++) for (int rowId = 0; rowId < internalModel.GetRowCount(); rowId++) {
            c.fill = GridBagConstraints.BOTH;
            c.gridx = internalModel.GetColumnCount() - 1 - columnId;
            c.gridy = rowId;
            c.anchor = GridBagConstraints.CENTER;
            this.add(internalModel.GetGridCell(columnId, rowId).GetComponent(), c);
        }
        for (int i = 0; i < layout.columnWidths.length; i++) layout.columnWidths[i] = 1;
        for (int i = 0; i < layout.rowHeights.length; i++) layout.rowHeights[i] = 1;
        this.revalidate();
    }

    public void SetColumnWidth(int columnId, Integer width) {
        columnId = layout.columnWidths.length - (columnId) - 1;
        int columnWidth = layout.columnWidths[columnId];
        int delta = (columnWidth - width) / (layout.columnWidths.length - 1);
        for (int i = 0; i < layout.columnWidths.length; i++) {
            if (i == columnId) layout.columnWidths[i] = width; else layout.columnWidths[i] += delta;
        }
        this.revalidate();
    }

    public void SetRowHeight(int rowId, Integer height) {
        int delta = (layout.rowHeights[rowId] - height) / (layout.rowHeights.length - 1);
        for (int i = 0; i < layout.rowHeights.length; i++) {
            if (i == rowId) layout.rowHeights[i] = height; else layout.rowHeights[i] += delta;
        }
        this.revalidate();
    }

    public void ResetBorders() {
        for (int columnId = 0; columnId < internalModel.GetColumnCount(); columnId++) for (int rowId = 0; rowId < internalModel.GetRowCount(); rowId++) {
            int matteBorderBottom = columnId == internalModel.GetColumnCount() - 1 ? 1 : 0;
            int matteBorderRight = rowId == internalModel.GetRowCount() - 1 ? 1 : 0;
            GeneralGridBorder externalBorder = new GeneralGridBorder(1, 1, matteBorderRight, matteBorderBottom, Color.LIGHT_GRAY);
            CompoundBorder border = GeneralGridBorder.MixBorders(defaultInner, externalBorder);
            internalModel.GetGridCell(columnId, rowId).GetComponent().setBorder(border);
        }
    }

    public void ResetBorder(int columnId, int rowId) {
        int matteBorderBottom = columnId == internalModel.GetColumnCount() - 1 ? 1 : 0;
        int matteBorderRight = rowId == internalModel.GetRowCount() - 1 ? 1 : 0;
        GeneralGridBorder externalBorder = new GeneralGridBorder(1, 1, matteBorderRight, matteBorderBottom, Color.LIGHT_GRAY);
        CompoundBorder border = GeneralGridBorder.MixBorders(defaultInner, externalBorder);
        internalModel.GetGridCell(columnId, rowId).GetComponent().setBorder(border);
    }

    public void MarkCell(int columnId, int rowId, Color color, int thickness) {
        GeneralGridBorder externalBorder = new GeneralGridBorder(thickness, thickness, thickness, thickness, color);
        CompoundBorder border = GeneralGridBorder.ReplaceOuter(internalModel.GetGridCell(columnId, rowId).GetComponent().getBorder(), externalBorder);
        internalModel.GetGridCell(columnId, rowId).GetComponent().setBorder(border);
    }

    public void HorizontalLine(int afterRowId, Color color, int thickness) {
        for (int columnId = 0; columnId < internalModel.GetColumnCount(); columnId++) {
            Border outsideBorder = ((CompoundBorder) internalModel.GetGridCell(columnId, afterRowId).GetComponent().getBorder()).getOutsideBorder();
            GeneralGridBorder markExternal = (GeneralGridBorder) outsideBorder;
            GeneralGridBorder newBorder = new GeneralGridBorder(markExternal.getTopThickness(), markExternal.getLeftThickness(), thickness, markExternal.getRightThickness(), color);
            newBorder.setRightColor(markExternal.getRightColor());
            newBorder.setTopColor(markExternal.getTopColor());
            newBorder.setLeftColor(markExternal.getLeftColor());
            CompoundBorder border = GeneralGridBorder.ReplaceOuter(internalModel.GetGridCell(columnId, afterRowId).GetComponent().getBorder(), newBorder);
            internalModel.GetGridCell(columnId, afterRowId).GetComponent().setBorder(border);
        }
    }

    public void VerticalLine(int afterColumnId, Color color, int thickness) {
        for (int rowId = 0; rowId < internalModel.GetRowCount(); rowId++) {
            GeneralGridBorder markExternal = (GeneralGridBorder) ((CompoundBorder) internalModel.GetGridCell(afterColumnId, rowId).GetComponent().getBorder()).getOutsideBorder();
            GeneralGridBorder newBorder = new GeneralGridBorder(markExternal.getTopThickness(), markExternal.getLeftThickness(), markExternal.getBottomThickness(), thickness, color);
            newBorder.setBottomColor(markExternal.getBottomColor());
            newBorder.setTopColor(markExternal.getTopColor());
            newBorder.setLeftColor(markExternal.getLeftColor());
            CompoundBorder border = GeneralGridBorder.ReplaceOuter(internalModel.GetGridCell(afterColumnId, rowId).GetComponent().getBorder(), newBorder);
            internalModel.GetGridCell(afterColumnId, rowId).GetComponent().setBorder(border);
        }
    }

    public void addGeneralGridComponentMouseListener(IGeneralGridComponentMouseEventListener listener) {
        internalModel.addGeneralGridComponentMouseListener(listener);
    }

    public void removeGeneralGridComponentMouseListener(IGeneralGridComponentMouseEventListener listener) {
        internalModel.removeGeneralGridComponentMouseListener(listener);
    }
}

class InternalMouseListenerX implements IGeneralGridComponentMouseEventListener {

    private CompoundBorder lastCellBorder;

    private JComponent lastCell = null;

    private GeneralGridBorder defaultInnerHighlighted = new GeneralGridBorder(1, 1, 1, 1, Color.BLUE);

    @Override
    public void mouseClicked(MouseEvent e, int row, int column, GeneralGridCellType type) {
    }

    @Override
    public void mouseEntered(MouseEvent e, int row, int column, GeneralGridCellType type) {
        lastCell = (JComponent) e.getComponent();
        lastCellBorder = (CompoundBorder) lastCell.getBorder();
        CompoundBorder border = GeneralGridBorder.ReplaceInner(lastCellBorder, defaultInnerHighlighted);
        lastCell.setBorder(border);
        lastCell.revalidate();
    }

    @Override
    public void mouseExited(MouseEvent e, int row, int column, GeneralGridCellType type) {
        lastCell.setBorder(lastCellBorder);
        lastCell.revalidate();
    }

    @Override
    public void mousePressed(MouseEvent e, int row, int column, GeneralGridCellType type) {
    }

    @Override
    public void mouseReleased(MouseEvent e, int row, int column, GeneralGridCellType type) {
    }
}
