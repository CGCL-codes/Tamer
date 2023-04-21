package core.preprocess.selection;

import core.preprocess.analyzation.DataAnalyzer;

public class MIFeatureSelector extends FeatureSelector {

    public MIFeatureSelector(DataAnalyzer data) {
        super(data);
    }

    @Override
    public double getAvgSelectionWeighting(int featureId) {
        double res = 0;
        double A, B, C, N, L, M;
        double log2 = Math.log(2);
        N = analyzer.getN();
        M = analyzer.getM();
        for (int i = 0; i != M; i++) {
            L = analyzer.getN_ci(i);
            A = analyzer.getN_ci_tk(i, featureId);
            B = analyzer.getN_not_ci_tk(i, featureId);
            C = analyzer.getN_ci_exclude_tk(i, featureId);
            res += L / N * Math.log((A * N + 1) / ((A + C) * (A + B) + 1)) / log2;
        }
        return res;
    }

    @Override
    public double getMaxSelectionWeighting(int featureId) {
        double res = 0;
        double tmp;
        double A, B, C, N, M;
        N = analyzer.getN();
        M = analyzer.getM();
        for (int i = 0; i != M; i++) {
            A = analyzer.getN_ci_tk(i, featureId);
            B = analyzer.getN_not_ci_tk(i, featureId);
            C = analyzer.getN_ci_exclude_tk(i, featureId);
            tmp = (A * N + 1) / ((A + C) * (A + B) + 1);
            if (tmp > res) {
                res = tmp;
            }
        }
        return res;
    }
}
