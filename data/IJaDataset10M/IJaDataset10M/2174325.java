package org.alcibiade.eternity.editor.solver.concurrent;

import static org.junit.Assert.assertEquals;
import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.alcibiade.eternity.editor.log.ConsoleLog;
import org.alcibiade.eternity.editor.model.GridModel;
import org.alcibiade.eternity.editor.model.operation.GridFiller;
import org.alcibiade.eternity.editor.solver.ClusterManager;
import org.alcibiade.eternity.editor.solver.EternitySolver;
import org.alcibiade.eternity.editor.solver.SolverFactory;
import org.gridgain.grid.Grid;
import org.gridgain.grid.GridFactory;
import org.junit.Test;

public class RemoteSolving {

    @Test
    public void testRemovteSolving() throws Exception {
        GridModel gridModel = new GridModel(3);
        GridFiller filler = new GridFiller(gridModel);
        filler.fillRandom(3);
        gridModel.shuffle();
        Grid grid = GridFactory.start();
        ExecutorService executor = grid.newGridExecutorService();
        Future<Integer> futureResult = executor.submit(new RemoteRunner(gridModel));
        executor.awaitTermination(10, TimeUnit.SECONDS);
        GridFactory.stop(true);
        assertEquals(new Integer(1), futureResult.get());
    }

    private class RemoteRunner implements Callable<Integer>, Serializable {

        private static final long serialVersionUID = 1L;

        private GridModel gridModel;

        public RemoteRunner(GridModel gridModel) {
            this.gridModel = gridModel;
        }

        public Integer call() throws Exception {
            try {
                ClusterManager clusterManager = new ClusterManager(new ConsoleLog());
                clusterManager.logMessage("Grid:\n" + gridModel.toQuadString());
                GridModel solution = new GridModel();
                EternitySolver solver = SolverFactory.createSolver(SolverFactory.LABEL_ITPATHMKI, gridModel, solution, clusterManager);
                solver.start();
                solver.join();
                clusterManager.logMessage("Solution:\n" + solution.toQuadString());
                return clusterManager.isSolutionFound() ? 1 : 0;
            } catch (AssertionError e) {
                e.printStackTrace();
                return -1;
            }
        }
    }
}
