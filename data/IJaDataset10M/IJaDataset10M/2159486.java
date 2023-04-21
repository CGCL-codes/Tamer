package edu.emory.mathcs.jplasma.tdouble;

import java.util.concurrent.locks.Lock;
import edu.emory.mathcs.jplasma.Barrier;

class Dgeqrf {

    private Dgeqrf() {
    }

    protected static int plasma_DGEQRF(int M, int N, double[] A, int A_offset, int LDA, double[] T, int T_offset) {
        int NB, MT, NT;
        int status;
        double[] Abdl;
        double[] Tbdl;
        double[] bdl_mem;
        int size_elems;
        if (!Dcommon.plasma_cntrl.initialized) {
            Dauxiliary.plasma_warning("plasma_DGEQRF", "PLASMA not initialized");
            return Dplasma.PLASMA_ERR_NOT_INITIALIZED;
        }
        if (M < 0) {
            Dauxiliary.plasma_error("plasma_DGEQRF", "illegal value of M");
            return Dplasma.PLASMA_ERR_ILLEGAL_VALUE;
        }
        if (N < 0) {
            Dauxiliary.plasma_error("plasma_DGEQRF", "illegal value of N");
            return Dplasma.PLASMA_ERR_ILLEGAL_VALUE;
        }
        if (LDA < Math.max(1, M)) {
            Dauxiliary.plasma_error("plasma_DGEQRF", "illegal value of LDA");
            return Dplasma.PLASMA_ERR_ILLEGAL_VALUE;
        }
        if (Math.min(M, N) == 0) return Dplasma.PLASMA_SUCCESS;
        status = Dauxiliary.plasma_tune(Dglobal.PLASMA_TUNE_DGELS, M, N, 0);
        if (status != Dplasma.PLASMA_SUCCESS) {
            Dauxiliary.plasma_error("plasma_DGEQRF", "plasma_tune() failed");
            return status;
        }
        NB = Dcommon.plasma_cntrl.NB;
        NT = (N % NB == 0) ? (N / NB) : (N / NB + 1);
        MT = (M % NB == 0) ? (M / NB) : (M / NB + 1);
        if (Dcommon.plasma_cntrl.NB > Dcommon.plasma_cntrl.NB_max) {
            status = Dallocate.plasma_free_aux_work_tau();
            if (status != Dplasma.PLASMA_SUCCESS) {
                Dauxiliary.plasma_error("plasma_DGEQRF", "plasma_free_aux_work_tau() failed");
                return status;
            }
            Dcommon.plasma_cntrl.NB_max = Dcommon.plasma_cntrl.NB;
            status = Dallocate.plasma_alloc_aux_work_tau();
            if (status != Dplasma.PLASMA_SUCCESS) {
                Dauxiliary.plasma_error("plasma_DGEQRF", "plasma_alloc_aux_work_tau() failed");
                return status;
            }
        }
        size_elems = MT * NT;
        if (Dcommon.plasma_cntrl.progress_size_elems < size_elems) {
            status = Dallocate.plasma_free_aux_progress();
            if (status != Dplasma.PLASMA_SUCCESS) {
                Dauxiliary.plasma_error("plasma_DGEQRF", "plasma_free_aux_progress() failed");
            }
            status = Dallocate.plasma_alloc_aux_progress(size_elems);
            if (status != Dplasma.PLASMA_SUCCESS) {
                Dauxiliary.plasma_error("plasma_DGEQRF", "plasma_alloc_aux_progress() failed");
                return status;
            }
        }
        bdl_mem = Dcommon.plasma_aux.bdl_mem;
        Abdl = bdl_mem;
        int Abdl_offset = 0;
        Tbdl = bdl_mem;
        int Tbdl_offset = MT * NT * Dcommon.plasma_cntrl.NBNBSIZE;
        size_elems = Tbdl_offset + MT * NT * Dcommon.plasma_cntrl.IBNBSIZE;
        if (Dcommon.plasma_cntrl.bdl_size_elems < size_elems) {
            status = Dallocate.plasma_free_aux_bdl();
            if (status != Dplasma.PLASMA_SUCCESS) {
                Dauxiliary.plasma_error("plasma_DGEQRF", "plasma_free_aux_bdl() failed");
                return status;
            }
            status = Dallocate.plasma_alloc_aux_bdl(size_elems);
            if (status != Dplasma.PLASMA_SUCCESS) {
                Dauxiliary.plasma_error("plasma_DGEQRF", "plasma_alloc_aux_bdl() failed");
                return status;
            }
            bdl_mem = Dcommon.plasma_aux.bdl_mem;
            Abdl = bdl_mem;
            Abdl_offset = 0;
            Tbdl = bdl_mem;
            Tbdl_offset = MT * NT * Dcommon.plasma_cntrl.NBNBSIZE;
        }
        Dcommon.plasma_args.F77 = A;
        Dcommon.plasma_args.F77_offset = A_offset;
        Dcommon.plasma_args.A = Abdl;
        Dcommon.plasma_args.A_offset = Abdl_offset;
        Dcommon.plasma_args.M = M;
        Dcommon.plasma_args.N = N;
        Dcommon.plasma_args.LDA = LDA;
        Dcommon.plasma_args.NB = Dcommon.plasma_cntrl.NB;
        Dcommon.plasma_args.MT = MT;
        Dcommon.plasma_args.NT = NT;
        Dcommon.plasma_args.NBNBSIZE = Dcommon.plasma_cntrl.NBNBSIZE;
        Lock lock = Dcommon.plasma_cntrl.action_mutex;
        lock.lock();
        try {
            Dcommon.plasma_cntrl.action = Dglobal.PLASMA_ACT_F77_TO_BDL;
            Dcommon.plasma_cntrl.action_condt.signalAll();
        } finally {
            lock.unlock();
        }
        Barrier.plasma_barrier(0, Dcommon.plasma_cntrl.cores_num);
        Dcommon.plasma_cntrl.action = Dglobal.PLASMA_ACT_STAND_BY;
        DbdlConvert.plasma_lapack_to_bdl(Dcommon.plasma_args.F77, Dcommon.plasma_args.F77_offset, Dcommon.plasma_args.A, Dcommon.plasma_args.A_offset, Dcommon.plasma_args.M, Dcommon.plasma_args.N, Dcommon.plasma_args.LDA, Dcommon.plasma_args.NB, Dcommon.plasma_args.MT, Dcommon.plasma_args.NT, Dcommon.plasma_args.NBNBSIZE, Dcommon.plasma_cntrl.cores_num, 0);
        Barrier.plasma_barrier(0, Dcommon.plasma_cntrl.cores_num);
        Dcommon.plasma_args.M = M;
        Dcommon.plasma_args.N = N;
        Dcommon.plasma_args.A = Abdl;
        Dcommon.plasma_args.A_offset = Abdl_offset;
        Dcommon.plasma_args.NB = Dcommon.plasma_cntrl.NB;
        Dcommon.plasma_args.NBNBSIZE = Dcommon.plasma_cntrl.NBNBSIZE;
        Dcommon.plasma_args.IBNBSIZE = Dcommon.plasma_cntrl.IBNBSIZE;
        Dcommon.plasma_args.IB = Dcommon.plasma_cntrl.IB;
        Dcommon.plasma_args.MT = MT;
        Dcommon.plasma_args.NT = NT;
        Dcommon.plasma_args.T = Tbdl;
        Dcommon.plasma_args.T_offset = Tbdl_offset;
        Dauxiliary.plasma_clear_aux_progress(MT * NT, -1);
        lock = Dcommon.plasma_cntrl.action_mutex;
        lock.lock();
        try {
            Dcommon.plasma_cntrl.action = Dglobal.PLASMA_ACT_DGEQRF;
            Dcommon.plasma_cntrl.action_condt.signalAll();
        } finally {
            lock.unlock();
        }
        Barrier.plasma_barrier(0, Dcommon.plasma_cntrl.cores_num);
        Dcommon.plasma_cntrl.action = Dglobal.PLASMA_ACT_STAND_BY;
        Pdgeqrf.plasma_pDGEQRF(Dcommon.plasma_args.M, Dcommon.plasma_args.N, Dcommon.plasma_args.A, Dcommon.plasma_args.A_offset, Dcommon.plasma_args.NB, Dcommon.plasma_args.NBNBSIZE, Dcommon.plasma_args.IBNBSIZE, Dcommon.plasma_args.IB, Dcommon.plasma_args.MT, Dcommon.plasma_args.NT, Dcommon.plasma_args.T, Dcommon.plasma_args.T_offset, Dcommon.plasma_args.INFO, Dcommon.plasma_cntrl.cores_num, 0);
        Barrier.plasma_barrier(0, Dcommon.plasma_cntrl.cores_num);
        System.arraycopy(Tbdl, Tbdl_offset, T, T_offset, MT * NT * Dcommon.plasma_cntrl.IBNBSIZE);
        Dcommon.plasma_args.A = Abdl;
        Dcommon.plasma_args.A_offset = Abdl_offset;
        Dcommon.plasma_args.F77 = A;
        Dcommon.plasma_args.F77_offset = A_offset;
        Dcommon.plasma_args.M = M;
        Dcommon.plasma_args.N = N;
        Dcommon.plasma_args.LDA = LDA;
        Dcommon.plasma_args.NB = Dcommon.plasma_cntrl.NB;
        Dcommon.plasma_args.MT = MT;
        Dcommon.plasma_args.NT = NT;
        Dcommon.plasma_args.NBNBSIZE = Dcommon.plasma_cntrl.NBNBSIZE;
        lock = Dcommon.plasma_cntrl.action_mutex;
        lock.lock();
        try {
            Dcommon.plasma_cntrl.action = Dglobal.PLASMA_ACT_BDL_TO_F77;
            Dcommon.plasma_cntrl.action_condt.signalAll();
        } finally {
            lock.unlock();
        }
        Barrier.plasma_barrier(0, Dcommon.plasma_cntrl.cores_num);
        Dcommon.plasma_cntrl.action = Dglobal.PLASMA_ACT_STAND_BY;
        DbdlConvert.plasma_bdl_to_lapack(Dcommon.plasma_args.A, Dcommon.plasma_args.A_offset, Dcommon.plasma_args.F77, Dcommon.plasma_args.F77_offset, Dcommon.plasma_args.M, Dcommon.plasma_args.N, Dcommon.plasma_args.LDA, Dcommon.plasma_args.NB, Dcommon.plasma_args.MT, Dcommon.plasma_args.NT, Dcommon.plasma_args.NBNBSIZE, Dcommon.plasma_cntrl.cores_num, 0);
        Barrier.plasma_barrier(0, Dcommon.plasma_cntrl.cores_num);
        return Dplasma.PLASMA_SUCCESS;
    }
}
