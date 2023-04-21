package com.gantzgulch.sudoku.core.solver.impl;

import com.gantzgulch.sudoku.core.domain.Board;
import com.gantzgulch.sudoku.core.domain.SliceType;

public class BlockSliceRule extends AbstractSliceRule {

    public BlockSliceRule() {
        super("bs");
    }

    @Override
    public boolean solve(Board board) {
        return solve(SliceType.BLOCK, board);
    }
}
