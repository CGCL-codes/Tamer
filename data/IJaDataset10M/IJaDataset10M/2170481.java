package ru.spbau.masyu;

public class SolverSingle extends SolverAbstract {

    protected boolean performIteration(MultiThreadVector current_cells, int current_begin, int current_end, MultiThreadVector next_cells) {
        for (int i = current_begin; i != current_end; ++i) {
            if (getNextCells(current_cells.get(i), next_cells.getComponent(0))) {
                return true;
            }
        }
        return false;
    }
}
