package net.sourceforge.nattable.group.action;

import net.sourceforge.nattable.group.ColumnGroupModel;
import net.sourceforge.nattable.group.ColumnGroupUtils;
import net.sourceforge.nattable.layer.ILayer;
import net.sourceforge.nattable.reorder.action.ColumnReorderDragMode;
import net.sourceforge.nattable.selection.SelectionLayer.MoveDirectionEnum;

public class ColumnGroupReorderDragMode extends ColumnReorderDragMode {

    private final ColumnGroupModel model;

    public ColumnGroupReorderDragMode(ColumnGroupModel model) {
        this.model = model;
    }

    @Override
    protected boolean isValidTargetColumnPosition(ILayer natLayer, int fromGridColumnPosition, int toGridColumnPosition) {
        MoveDirectionEnum moveDirection = ColumnGroupUtils.getMoveDirection(fromGridColumnPosition, toGridColumnPosition);
        int columnIndex = natLayer.getColumnIndexByPosition(toGridColumnPosition);
        if (MoveDirectionEnum.RIGHT == moveDirection && isRightEdgeOfAColumnGroup(natLayer, toGridColumnPosition, columnIndex)) {
            return true;
        }
        if (MoveDirectionEnum.LEFT == moveDirection && isLeftEdgeOfAColumnGroup(natLayer, toGridColumnPosition, columnIndex)) {
            return true;
        }
        return false;
    }

    private boolean isRightEdgeOfAColumnGroup(ILayer natLayer, int columnPosition, int columnIndex) {
        int nextColumnPosition = columnPosition + 1;
        if (nextColumnPosition < natLayer.getColumnCount()) {
            int nextColumnIndex = natLayer.getColumnIndexByPosition(nextColumnPosition);
            if ((model.isPartOfAGroup(columnIndex) && !model.isPartOfAGroup(nextColumnIndex)) || !ColumnGroupUtils.isInTheSameGroup(columnIndex, nextColumnIndex, model)) {
                return true;
            }
        }
        return false;
    }

    private boolean isLeftEdgeOfAColumnGroup(ILayer natLayer, int columnPosition, int columnIndex) {
        int previousColumnPosition = columnPosition - 1;
        if (previousColumnPosition >= 0) {
            int previousColumnIndex = natLayer.getColumnIndexByPosition(previousColumnPosition);
            if ((model.isPartOfAGroup(columnIndex) && !model.isPartOfAGroup(previousColumnIndex)) || !ColumnGroupUtils.isInTheSameGroup(columnIndex, previousColumnIndex, model)) {
                return true;
            }
        }
        return false;
    }
}
