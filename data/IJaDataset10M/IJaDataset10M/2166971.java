package coho.lp;

import coho.common.matrix.*;

public interface LPSolver {

    LPResult opt();

    LPResult opt(Matrix c);

    LP lp();
}
