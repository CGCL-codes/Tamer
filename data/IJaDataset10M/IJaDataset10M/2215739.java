package de.fraunhofer.isst.axbench.timing.algorithms.special;

import de.fraunhofer.isst.axbench.timing.algorithms.AlgorithmExact;
import de.fraunhofer.isst.axbench.timing.algorithms.Combinator.NueTuple;
import de.fraunhofer.isst.axbench.timing.model.Task;
import de.fraunhofer.isst.axbench.timing.model.Transaction;
import de.fraunhofer.isst.axbench.timing.model.TransactionSet;

public class AlgorithmExactInfluencePattern extends AlgorithmExact {

    private double highest_r_uav = 0.0;

    private NueTuple v_of_highest_r_uav;

    public AlgorithmExactInfluencePattern(TransactionSet gamma) {
        super(gamma);
    }

    public AlgorithmExactInfluencePattern(AlgorithmExact baseAlgo) {
        super(baseAlgo);
    }

    @Override
    public double R_uav(Task tau_ua, NueTuple v, int p) throws AbortedException {
        double r_uav = super.R_uav(tau_ua, v, p);
        if (r_uav > highest_r_uav) {
            highest_r_uav = r_uav;
            v_of_highest_r_uav = v;
        }
        return r_uav;
    }

    public ResultTransactionSort createTransactionSort(Task tau_ua) throws AbortedException {
        highest_r_uav = 0.0;
        v_of_highest_r_uav = null;
        R(tau_ua);
        Transaction gammaU = transactionOf(tau_ua);
        Task tau_vu = v_of_highest_r_uav.get(gammaU);
        int p0 = p_0(tau_ua, tau_vu);
        int pL = p_L(tau_ua, v_of_highest_r_uav);
        double r2 = R_uav(tau_ua, v_of_highest_r_uav, pL);
        return new ResultTransactionSort(gamma, tau_ua, v_of_highest_r_uav, p0, pL, r2);
    }
}
