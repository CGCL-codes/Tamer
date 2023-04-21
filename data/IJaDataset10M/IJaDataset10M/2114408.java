package edu.mta.ok.nworkshop.predictor;

import java.io.File;
import edu.mta.ok.nworkshop.Constants;
import edu.mta.ok.nworkshop.PredictorProperties;
import edu.mta.ok.nworkshop.PredictorProperties.Predictors;
import edu.mta.ok.nworkshop.PredictorProperties.PropertyKeys;
import edu.mta.ok.nworkshop.model.MovieIndexedModelRatings;
import edu.mta.ok.nworkshop.model.UserIndexedModel;
import edu.mta.ok.nworkshop.model.UserIndexedModelRatings;
import edu.mta.ok.nworkshop.preprocess.PreProcessItemViewUsers;
import edu.mta.ok.nworkshop.similarity.InterpolationSimilarityRawScores;
import edu.mta.ok.nworkshop.similarity.InterpolationSimilarityResiduals;
import edu.mta.ok.nworkshop.similarity.SimilarityCalculator;
import edu.mta.ok.nworkshop.utils.FileUtils;

/**
 * Implementation of Bellkor interpolation KNN predictor.
 * The interpolation model used in this class was calculated using raw ratings scores given by Netflix.
 * 
 * Due to bad results, the algorithm is not used in the final blend.
 */
public class ImprovedKNNPredictionRawScore extends ImprovedKNNPredictorAbstract {

    public ImprovedKNNPredictionRawScore() {
        UserIndexedModelRatings userModelTemp = new UserIndexedModelRatings();
        this.neighborsNum = PredictorProperties.getInstance().getPredictorIntProperty(Predictors.IMPROVED_KNN, PropertyKeys.NEIGHBORS_NUM, DEFAULT_NEIGHBOARS_NUM);
        interpolationSimilarityScores = new InterpolationSimilarityResiduals(new MovieIndexedModelRatings(), userModelTemp);
        this.userModel = userModelTemp;
        this.alpha = DEFAULT_ALPHA;
        interpolationSimilarityScores.calculateSimilarities();
        String interpolationFile = PredictorProperties.getInstance().getPredictorStringProperty(Predictors.IMPROVED_KNN, PropertyKeys.INTERPOLATION_FILE_NAME, null);
        if (interpolationFile != null && (new File(interpolationFile)).exists()) {
            System.out.print("Loads interpolation data from configured file " + interpolationFile + "...");
            this.interpolationVals = FileUtils.loadDataFromFile(interpolationFile);
            System.out.println("Done!");
        } else {
            System.out.print("Configured interpolation file does not exist, Start calculating interpolation data ...");
            this.interpolationVals = (new PreProcessItemViewUsers()).calcFinalValues();
            System.out.println("Done!");
        }
    }

    public ImprovedKNNPredictionRawScore(SimilarityCalculator simModel, UserIndexedModel userModel, String interpolationValsFileName) {
        super(simModel, userModel, interpolationValsFileName, DEFAULT_NEIGHBOARS_NUM);
    }

    public ImprovedKNNPredictionRawScore(SimilarityCalculator simModel, UserIndexedModel userModel, int neighboarsNum, String interpolationValsFileName) {
        super(simModel, userModel, neighboarsNum, interpolationValsFileName);
    }

    public ImprovedKNNPredictionRawScore(SimilarityCalculator simModel, UserIndexedModel userModel, int neighboarsNum, int alpha, String interpolationValsFileName) {
        super(simModel, userModel, alpha, interpolationValsFileName, DEFAULT_NEIGHBOARS_NUM);
    }

    @Override
    protected double getFinalPrediction(double currPrediction, int probeIndex) {
        return currPrediction;
    }

    @Override
    protected double getRatingValue(int userID, int position) {
        return ((byte[]) userModel.getUserRatings(userID))[position];
    }

    public static void main(String[] args) {
        ImprovedKNNPredictionRawScore predictor = new ImprovedKNNPredictionRawScore(InterpolationSimilarityRawScores.getSimilarityFromFile("D:\\FinalProject\\code\\netflixWorkshop\\binFiles\\interpolation\\similarityModel-raw.data", false), new UserIndexedModelRatings(Constants.NETFLIX_OUTPUT_DIR + "cleanedUserIndexedSlab-SortedByMovieId.data"), Constants.NETFLIX_OUTPUT_DIR + "interpolation\\moviesCommonUsersLists-Final-raw.data");
        PredictionTester.getProbeError(predictor, Constants.NETFLIX_OUTPUT_DIR + "Predictions/InterpolationPredictorResiduals.txt", Constants.NETFLIX_OUTPUT_DIR + Constants.DEFAULT_PROBE_FILE_NAME);
    }
}
