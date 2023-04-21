package cf.e_commerce.evaluator.newEval.testers;

import java.io.FileWriter;
import cf.e_commerce.base.constants.Constants;
import cf.e_commerce.evaluator.newEval.testers.impl.AccuracyTestStrategy;
import cf.e_commerce.evaluator.newEval.testers.impl.EnsembleTester;
import cf.e_commerce.evaluator.newEval.testers.impl.GenericBooleanTester;
import cf.e_commerce.evaluator.newEval.testers.impl.GenericItemBasedTester;
import cf.e_commerce.evaluator.newEval.testers.impl.GenericUserBasedTester;
import cf.e_commerce.evaluator.newEval.testers.impl.ItemAverageTester;
import cf.e_commerce.evaluator.newEval.testers.impl.ItemUserAverageTester;
import cf.e_commerce.evaluator.newEval.testers.impl.KnnItemBasedTester;
import cf.e_commerce.evaluator.newEval.testers.impl.LinkAnalysisTester;
import cf.e_commerce.evaluator.newEval.testers.impl.MatrixFactorizationTester;
import cf.e_commerce.evaluator.newEval.testers.impl.SVDTester;
import cf.e_commerce.evaluator.newEval.testers.impl.SlopeOneTester;
import cf.e_commerce.evaluator.newEval.testers.impl.TreeClustering1Tester;
import cf.e_commerce.evaluator.newEval.testers.impl.TreeClustering2Tester;
import cf.e_commerce.evaluator.newEval.testers.impl.WeightedMatrixFactorizationTester;

final class AccuracyTestFactory {

    public static final AccuracyTestStrategy getInstance(int t, FileWriter writer, FileWriter estimateWriter) {
        switch(t) {
            case Constants.REC_ENSEMBLE:
                return new EnsembleTester(writer, estimateWriter);
            case Constants.REC_GENERIC_BOOLEAN:
                return new GenericBooleanTester(writer, estimateWriter);
            case Constants.REC_GENERIC_ITEM_BASED:
                return new GenericItemBasedTester(writer, estimateWriter);
            case Constants.REC_USER_SIMILARITY:
                return new GenericUserBasedTester(writer, estimateWriter);
            case Constants.REC_ITEM_AVERAGE:
                return new ItemAverageTester(writer, estimateWriter);
            case Constants.REC_ITEM_USER:
                return new ItemUserAverageTester(writer, estimateWriter);
            case Constants.REC_KNN_ITEM_BASED:
                return new KnnItemBasedTester(writer, estimateWriter);
            case Constants.REC_LINK_ANALYSIS:
                return new LinkAnalysisTester(writer, estimateWriter);
            case Constants.REC_MATRIX_FACTORIZATION:
                return new MatrixFactorizationTester(writer, estimateWriter);
            case Constants.REC_WEIGHTED_MATRIX_FACTORIZATION:
                return new WeightedMatrixFactorizationTester(writer, estimateWriter);
            case Constants.REC_SLOPE_ONE:
                return new SlopeOneTester(writer, estimateWriter);
            case Constants.REC_SVD:
                return new SVDTester(writer, estimateWriter);
            case Constants.REC_TREE_CLUSTERING_1:
                return new TreeClustering1Tester(writer, estimateWriter);
            case Constants.REC_TREE_CLUSTERING_2:
                return new TreeClustering2Tester(writer, estimateWriter);
        }
        return null;
    }
}
