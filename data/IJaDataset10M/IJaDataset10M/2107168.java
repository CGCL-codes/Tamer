package edu.emory.mathcs.jplasma.tdouble;

import java.util.concurrent.locks.Lock;
import edu.emory.mathcs.jplasma.Barrier;

class Dtrsmpl {

    private Dtrsmpl() {
    }

    protected static int plasma_DTRSMPL(int M, int NRHS, int N, double[] A, int A_offset, int LDA, double[] L, int L_offset, int[] IPIV, int IPIV_offset, double[] B, int B_offset, int LDB) {
        int NB, MT, NT, NTRHS;
        int status;
        double[] Abdl;
        double[] Bbdl;
        double[] Lbdl;
        double[] bdl_mem;
        int size_elems;
        if (!Dcommon.plasma_cntrl.initialized) {
            Dauxiliary.plasma_warning("plasma_DGETRS", "PLASMA not initialized");
            return Dplasma.PLASMA_ERR_NOT_INITIALIZED;
        }
        if (M < 0) {
            Dauxiliary.plasma_error("plasma_DGETRS", "illegal value of M");
            return Dplasma.PLASMA_ERR_ILLEGAL_VALUE;
        }
        if (N < 0) {
            Dauxiliary.plasma_error("plasma_DGETRS", "illegal value of N");
            return Dplasma.PLASMA_ERR_ILLEGAL_VALUE;
        }
        if (NRHS < 0) {
            Dauxiliary.plasma_error("plasma_DGETRS", "illegal value of NRHS");
            return Dplasma.PLASMA_ERR_ILLEGAL_VALUE;
        }
        if (LDA < Math.max(1, M)) {
            Dauxiliary.plasma_error("plasma_DGETRS", "illegal value of LDA");
            return Dplasma.PLASMA_ERR_ILLEGAL_VALUE;
        }
        if (LDB < Math.max(M, N)) {
            Dauxiliary.plasma_error("plasma_DGETRS", "illegal value of LDB");
            return Dplasma.PLASMA_ERR_ILLEGAL_VALUE;
        }
        if (Math.min(M, Math.min(N, NRHS)) == 0) return Dplasma.PLASMA_SUCCESS;
        status = Dauxiliary.plasma_tune(Dglobal.PLASMA_TUNE_DGESV, N, N, NRHS);
        if (status != Dplasma.PLASMA_SUCCESS) {
            Dauxiliary.plasma_error("plasma_DGETRS", "plasma_tune() failed");
            return status;
        }
        NB = Dcommon.plasma_cntrl.NB;
        MT = (M % NB == 0) ? (M / NB) : (M / NB + 1);
        NT = (N % NB == 0) ? (N / NB) : (N / NB + 1);
        NTRHS = (NRHS % NB == 0) ? (NRHS / NB) : (NRHS / NB + 1);
        size_elems = Math.max(MT, NT) * Math.max(NT, NTRHS);
        if (Dcommon.plasma_cntrl.progress_size_elems < size_elems) {
            status = Dallocate.plasma_free_aux_progress();
            if (status != Dplasma.PLASMA_SUCCESS) {
                Dauxiliary.plasma_error("plasma_DGETRS", "plasma_free_aux_progress() failed");
            }
            status = Dallocate.plasma_alloc_aux_progress(size_elems);
            if (status != Dplasma.PLASMA_SUCCESS) {
                Dauxiliary.plasma_error("plasma_DGETRS", "plasma_alloc_aux_progress() failed");
                return status;
            }
        }
        bdl_mem = Dcommon.plasma_aux.bdl_mem;
        Abdl = bdl_mem;
        int Abdl_offset = 0;
        Lbdl = bdl_mem;
        int Lbdl_offset = MT * NT * Dcommon.plasma_cntrl.NBNBSIZE;
        Bbdl = bdl_mem;
        int Bbdl_offset = Lbdl_offset + MT * NT * Dcommon.plasma_cntrl.IBNBSIZE;
        size_elems = Bbdl_offset + Math.max(MT, NT) * NTRHS * Dcommon.plasma_cntrl.NBNBSIZE;
        if (Dcommon.plasma_cntrl.bdl_size_elems < size_elems) {
            status = Dallocate.plasma_free_aux_bdl();
            if (status != Dplasma.PLASMA_SUCCESS) {
                Dauxiliary.plasma_error("plasma_DGETRS", "plasma_free_aux_bdl() failed");
                return status;
            }
            status = Dallocate.plasma_alloc_aux_bdl(size_elems);
            if (status != Dplasma.PLASMA_SUCCESS) {
                Dauxiliary.plasma_error("plasma_DGETRS", "plasma_alloc_aux_bdl() failed");
                return status;
            }
            bdl_mem = Dcommon.plasma_aux.bdl_mem;
            Abdl = bdl_mem;
            Abdl_offset = 0;
            Lbdl = bdl_mem;
            Lbdl_offset = MT * NT * Dcommon.plasma_cntrl.NBNBSIZE;
            Bbdl = bdl_mem;
            Bbdl_offset = Lbdl_offset + MT * NT * Dcommon.plasma_cntrl.IBNBSIZE;
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
        Dcommon.plasma_args.F77 = B;
        Dcommon.plasma_args.F77_offset = B_offset;
        Dcommon.plasma_args.A = Bbdl;
        Dcommon.plasma_args.A_offset = Bbdl_offset;
        Dcommon.plasma_args.M = M;
        Dcommon.plasma_args.N = NRHS;
        Dcommon.plasma_args.LDA = LDB;
        Dcommon.plasma_args.NB = Dcommon.plasma_cntrl.NB;
        Dcommon.plasma_args.MT = MT;
        Dcommon.plasma_args.NT = NTRHS;
        Dcommon.plasma_args.NBNBSIZE = Dcommon.plasma_cntrl.NBNBSIZE;
        lock = Dcommon.plasma_cntrl.action_mutex;
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
        System.arraycopy(L, L_offset, Lbdl, Lbdl_offset, MT * NT * Dcommon.plasma_cntrl.IBNBSIZE);
        Dcommon.plasma_args.M = M;
        Dcommon.plasma_args.N = N;
        Dcommon.plasma_args.NRHS = NRHS;
        Dcommon.plasma_args.A = Abdl;
        Dcommon.plasma_args.A_offset = Abdl_offset;
        Dcommon.plasma_args.NB = Dcommon.plasma_cntrl.NB;
        Dcommon.plasma_args.NBNBSIZE = Dcommon.plasma_cntrl.NBNBSIZE;
        Dcommon.plasma_args.IBNBSIZE = Dcommon.plasma_cntrl.IBNBSIZE;
        Dcommon.plasma_args.IB = Dcommon.plasma_cntrl.IB;
        Dcommon.plasma_args.MT = MT;
        Dcommon.plasma_args.NTRHS = NTRHS;
        Dcommon.plasma_args.NT = NT;
        Dcommon.plasma_args.L = Lbdl;
        Dcommon.plasma_args.L_offset = Lbdl_offset;
        Dcommon.plasma_args.IPIV = IPIV;
        Dcommon.plasma_args.IPIV_offset = IPIV_offset;
        Dcommon.plasma_args.B = Bbdl;
        Dcommon.plasma_args.B_offset = Bbdl_offset;
        Dauxiliary.plasma_clear_aux_progress(MT * NTRHS, -1);
        lock = Dcommon.plasma_cntrl.action_mutex;
        lock.lock();
        try {
            Dcommon.plasma_cntrl.action = Dglobal.PLASMA_ACT_DTRSMPL;
            Dcommon.plasma_cntrl.action_condt.signalAll();
        } finally {
            lock.unlock();
        }
        Barrier.plasma_barrier(0, Dcommon.plasma_cntrl.cores_num);
        Dcommon.plasma_cntrl.action = Dglobal.PLASMA_ACT_STAND_BY;
        Pdtrsmpl.plasma_pDTRSMPL(Dcommon.plasma_args.M, Dcommon.plasma_args.NRHS, Dcommon.plasma_args.N, Dcommon.plasma_args.A, Dcommon.plasma_args.A_offset, Dcommon.plasma_args.NB, Dcommon.plasma_args.NBNBSIZE, Dcommon.plasma_args.IBNBSIZE, Dcommon.plasma_args.IB, Dcommon.plasma_args.MT, Dcommon.plasma_args.NTRHS, Dcommon.plasma_args.NT, Dcommon.plasma_args.L, Dcommon.plasma_args.L_offset, Dcommon.plasma_args.IPIV, Dcommon.plasma_args.IPIV_offset, Dcommon.plasma_args.B, Dcommon.plasma_args.B_offset, Dcommon.plasma_args.INFO, Dcommon.plasma_cntrl.cores_num, 0);
        Barrier.plasma_barrier(0, Dcommon.plasma_cntrl.cores_num);
        Dcommon.plasma_args.A = Bbdl;
        Dcommon.plasma_args.A_offset = Bbdl_offset;
        Dcommon.plasma_args.F77 = B;
        Dcommon.plasma_args.F77_offset = B_offset;
        Dcommon.plasma_args.M = M;
        Dcommon.plasma_args.N = NRHS;
        Dcommon.plasma_args.LDA = LDB;
        Dcommon.plasma_args.NB = Dcommon.plasma_cntrl.NB;
        Dcommon.plasma_args.MT = MT;
        Dcommon.plasma_args.NT = NTRHS;
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
