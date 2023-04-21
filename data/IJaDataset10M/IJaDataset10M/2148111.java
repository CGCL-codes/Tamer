package org.isakiev.xl.model.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.isakiev.xl.model.BlockReference;
import org.isakiev.xl.model.CellNode;
import org.isakiev.xl.model.CellReference;
import org.isakiev.xl.model.CellVisitor;

public class BufferedCellNodeContainer implements CellNodeContainer, BufferedEntity {

    private final Map<Integer, Map<Integer, CellNode>> rowsToColumnsMap = new HashMap<Integer, Map<Integer, CellNode>>();

    private final Map<Integer, Map<Integer, CellNode>> columnsToRowsMap = new HashMap<Integer, Map<Integer, CellNode>>();

    private final List<CellReference> newCells = new ArrayList<CellReference>();

    private final Map<CellReference, CellNode> removedNodes = new HashMap<CellReference, CellNode>();

    private int lastValuableRow;

    private int lastValuableColumn;

    @Override
    public synchronized boolean containsCell(CellReference cell) {
        if (rowsToColumnsMap.containsKey(cell.getRow())) {
            Map<Integer, CellNode> columnsMap = rowsToColumnsMap.get(cell.getRow());
            return columnsMap.containsKey(cell.getColumn());
        }
        return false;
    }

    @Override
    public synchronized CellNode getNode(CellReference cell) {
        if (rowsToColumnsMap.containsKey(cell.getRow())) {
            Map<Integer, CellNode> columnsMap = rowsToColumnsMap.get(cell.getRow());
            if (columnsMap.containsKey(cell.getColumn())) {
                return columnsMap.get(cell.getColumn());
            }
        }
        return null;
    }

    @Override
    public synchronized void putNode(CellReference cell, CellNode node) {
        doPutNode(cell, node);
        removedNodes.remove(cell);
        newCells.add(cell);
    }

    private void doPutNode(CellReference cell, CellNode node) {
        Map<Integer, CellNode> columnsMap = rowsToColumnsMap.get(cell.getRow());
        if (columnsMap == null) {
            columnsMap = new HashMap<Integer, CellNode>();
            rowsToColumnsMap.put(cell.getRow(), columnsMap);
        }
        columnsMap.put(cell.getColumn(), node);
        Map<Integer, CellNode> rowsMap = columnsToRowsMap.get(cell.getColumn());
        if (rowsMap == null) {
            rowsMap = new HashMap<Integer, CellNode>();
            columnsToRowsMap.put(cell.getColumn(), rowsMap);
        }
        rowsMap.put(cell.getRow(), node);
        lastValuableColumn = Math.max(cell.getColumn(), lastValuableColumn);
        lastValuableRow = Math.max(cell.getRow(), lastValuableRow);
    }

    @Override
    public synchronized void removeNode(CellReference cell) {
        CellNode node = doRemoveNode(cell);
        assert node != null;
        newCells.remove(cell);
        removedNodes.put(cell, node);
    }

    /**
	 * @return CellNode if it was successfully removed or null if it wasn't
	 *         found
	 */
    private CellNode doRemoveNode(CellReference cell) {
        Map<Integer, CellNode> columnsMap = rowsToColumnsMap.get(cell.getRow());
        Map<Integer, CellNode> rowsMap = columnsToRowsMap.get(cell.getColumn());
        if (columnsMap != null && rowsMap != null) {
            assert !(columnsMap.containsKey(cell.getColumn()) ^ rowsMap.containsKey(cell.getRow()));
            if (columnsMap.containsKey(cell.getColumn())) {
                CellNode node = columnsMap.get(cell.getColumn());
                columnsMap.remove(cell.getColumn());
                if (columnsMap.size() == 0) {
                    rowsToColumnsMap.remove(cell.getRow());
                }
                rowsMap.remove(cell.getRow());
                if (rowsMap.size() == 0) {
                    columnsToRowsMap.remove(cell.getColumn());
                }
                if (cell.getRow() == lastValuableRow && !rowsToColumnsMap.containsKey(cell.getRow())) {
                    lastValuableRow = calculateLastValuableRow();
                }
                if (cell.getColumn() == lastValuableColumn && !columnsToRowsMap.containsKey(cell.getColumn())) {
                    lastValuableColumn = calculateLastValuableColumn();
                }
                return node;
            }
        }
        return null;
    }

    @Override
    public synchronized void visitAllNodes(final CellVisitor visitor) {
        for (Map.Entry<Integer, Map<Integer, CellNode>> columnsMapEntry : rowsToColumnsMap.entrySet()) {
            for (Map.Entry<Integer, CellNode> nodeEntry : columnsMapEntry.getValue().entrySet()) {
                if (!visitor.visit(nodeEntry.getValue())) {
                    return;
                }
            }
        }
    }

    @Override
    public void visitBlockNodes(BlockReference block, CellVisitor visitor) {
        int minColumn = block.getTopLeftCell().getColumn();
        int maxColumn = block.getBottomRightCell().getColumn();
        int minRow = block.getTopLeftCell().getRow();
        int maxRow = block.getBottomRightCell().getRow();
        int width = maxColumn - minColumn + 1;
        int height = maxRow - minRow + 1;
        if (width < 1 || height < 1) {
            return;
        }
        if (width > height) {
            for (Map.Entry<Integer, Map<Integer, CellNode>> columnsMapEntry : rowsToColumnsMap.entrySet()) {
                int row = columnsMapEntry.getKey();
                Map<Integer, CellNode> columnsMap = columnsMapEntry.getValue();
                if (minRow <= row && row <= maxRow) {
                    for (Map.Entry<Integer, CellNode> nodeEntry : columnsMap.entrySet()) {
                        int column = nodeEntry.getKey();
                        CellNode node = nodeEntry.getValue();
                        if (minColumn <= column && column <= maxColumn) {
                            if (!visitor.visit(node)) {
                                return;
                            }
                        }
                    }
                }
            }
        } else {
            for (Map.Entry<Integer, Map<Integer, CellNode>> rowsMapEntry : columnsToRowsMap.entrySet()) {
                int column = rowsMapEntry.getKey();
                Map<Integer, CellNode> rowsMap = rowsMapEntry.getValue();
                if (minColumn <= column && column <= maxColumn) {
                    for (Map.Entry<Integer, CellNode> nodeEntry : rowsMap.entrySet()) {
                        int row = nodeEntry.getKey();
                        CellNode node = nodeEntry.getValue();
                        if (minRow <= row && row <= maxRow) {
                            if (!visitor.visit(node)) {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public synchronized void commitChanges() {
        newCells.clear();
        removedNodes.clear();
    }

    @Override
    public synchronized void revertChanges() {
        for (CellReference cell : newCells) {
            doRemoveNode(cell);
        }
        newCells.clear();
        for (Map.Entry<CellReference, CellNode> entry : removedNodes.entrySet()) {
            doPutNode(entry.getKey(), entry.getValue());
        }
        removedNodes.clear();
    }

    @Override
    public synchronized int getLastValuableColumn() {
        return lastValuableColumn;
    }

    @Override
    public synchronized int getLastValuableRow() {
        return lastValuableRow;
    }

    private int calculateLastValuableColumn() {
        int max = 0;
        for (int index : columnsToRowsMap.keySet()) {
            max = Math.max(index, max);
        }
        return max;
    }

    private int calculateLastValuableRow() {
        int max = 0;
        for (int index : rowsToColumnsMap.keySet()) {
            max = Math.max(index, max);
        }
        return max;
    }
}
