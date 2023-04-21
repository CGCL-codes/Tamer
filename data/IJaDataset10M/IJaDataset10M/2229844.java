package com.gantzgulch.sudoku.core.solver.impl;

import java.util.Set;
import com.gantzgulch.sudoku.core.domain.Cell;
import com.gantzgulch.sudoku.core.domain.Slice;

public class SliceSolverOne {

    public boolean solve(Slice slice) {
        boolean isChanged = false;
        Set<Integer> solvedValues = slice.getSolvedValues();
        for (Cell cell : slice) {
            if (!cell.isSolved()) {
                isChanged |= cell.removePossibleValues(solvedValues);
            }
        }
        return isChanged;
    }
}
