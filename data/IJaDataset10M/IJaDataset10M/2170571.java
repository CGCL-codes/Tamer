package net.maizegenetics.stats.MLM;

/**
 * Created using IntelliJ IDEA.
 * Author: Peter Bradbury
 * Date: Jan 31, 2005
 * Time: 9:14:57 AM
 * A net.maizegenetics.jGLiM.ReducedLinearModel is net.maizegenetics.jGLiM.LinearModel that calculated by subtracting a reduced model from a full model.
 * It tests the factors present in the full model but not in the reduced model.  This model returns null for some
 * statistics.  For example, beta, which is an estimate of model parameters, is always returned as null by this class.
 */
public class ReducedMixedLinearModel extends AbstractMixedLinearModel {

    private LinearModel lmFull;

    private LinearModel lmReduced;

    private MixedLinearModelSublementary mlm;

    public ReducedMixedLinearModel(LinearModel lmFull, LinearModel lmReduced) {
        this.lmFull = lmFull;
        this.lmReduced = lmReduced;
        calculate();
    }

    public ReducedMixedLinearModel(LinearModel lmFull) {
        this.lmFull = lmFull;
        mlm = lmFull.getMixedLinearModelSublementary();
        calculateContrast();
    }

    public void calculateContrast() {
        ssError = lmFull.getSSError();
        dfError = lmFull.getdfError();
        dfModel = mlm.getNAllele() - 1;
        ssModel = mlm.getF() * mlm.getVarResidual() * dfModel;
        if (ssModel < 0) ssModel = 0.0000001;
        Rsq = ssModel / (ssError + lmFull.getSSModel());
    }

    public void calculate() {
        ssError = lmFull.getSSError();
        ssModel = lmFull.getSSModel() - lmReduced.getSSModel();
        dfError = lmFull.getdfError();
        dfModel = lmFull.getdfModel() - lmReduced.getdfModel();
        if (ssModel < 0) ssModel = 0.0000001;
        Rsq = ssModel / (ssError + lmFull.getSSModel());
    }

    public void permute() {
        if (numberOfPermutations > 0 && dataShuffler != null) {
            int numberOfGreaterF = 0;
            double origF = getF();
            dataShuffler.resetRandomGenerator();
            Fvalues = new double[numberOfPermutations];
            for (int i = 0; i < numberOfPermutations; i++) {
                dataShuffler.permuteData();
                if (modelForError != null) modelForError.calculate();
                lmFull.calculate();
                lmReduced.calculate();
                calculate();
                Fvalues[i] = getF();
                if (Fvalues[i] >= origF) numberOfGreaterF++;
            }
            permute_p = ((double) (numberOfGreaterF + 1)) / (numberOfPermutations + 1);
            dataShuffler.setDataToOriginalOrder();
            if (modelForError != null) modelForError.calculate();
            lmFull.calculate();
            lmReduced.calculate();
            calculate();
        }
    }
}
