package org.spantus.math.services;

import java.util.List;
import org.spantus.math.LPCResult;

public interface LPCService {

    public LPCResult calculateLPC(List<Double> x, int order);
}
