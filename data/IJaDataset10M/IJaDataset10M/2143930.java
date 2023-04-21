package ru.jnano.math.calc.diff.app;

import ru.jnano.math.calc.diff.contrl.StaticStepController;
import ru.jnano.math.calc.diff.diff.DiffSolveRun;
import ru.jnano.math.calc.diff.diff.DiffStatus;
import ru.jnano.math.calc.diff.diff.IDiffSolve;
import ru.jnano.math.calc.diff.diff.RungeKutta4Step;
import ru.jnano.math.calc.diff.rp.DirectDriveComputeVar;
import ru.jnano.math.calc.diff.rp.DirectDriveModel;
import ru.jnano.math.calc.diff.rp.IDataRightPart;
import ru.jnano.math.calc.diff.rp.IRightPart;
import ru.jnano.math.calc.diff.rp.ListDataRightPart;
import ru.jnano.math.calc.diff.seq.DiffSimpleSeqSolveRun;

public class AppSimpleSeq {

    public static void main(String[] args) throws InterruptedException {
        DirectDriveModel model = new DirectDriveModel(1);
        IRightPart rightPart = new DirectDriveComputeVar(model);
        IDataRightPart data = new ListDataRightPart();
        IDiffSolve diff = new DiffSolveRun(rightPart, data, new StaticStepController(0, new double[] { 0.0, 0.0 }, 100, 1), new RungeKutta4Step());
        IDiffSolve diffSeq = new DiffSimpleSeqSolveRun(diff, 200000);
        long startTime = System.currentTimeMillis();
        DiffStatus status = diffSeq.call();
        System.out.println("������:" + status + "; �����:" + (System.currentTimeMillis() - startTime) + " ��");
    }
}
