package classifiers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;
import org.apache.log4j.Logger;
import classifiers.command.CommandClassifierController;
import classifiers.results.ImageErrors;
import classifiers.results.ResultSetAcc;
import data.dataset.DataSet;
import data.feature.FeatureSet;

/**
 * @author Maha
 *
 */
public class MultiClassClassifier extends Observable {

    CommandClassifierController singleClass;

    int classes = 10;

    private static final transient Logger logger = Logger.getLogger(MultiClassClassifier.class);

    Logger summary = Logger.getLogger("summary");

    Logger summaryDetails = Logger.getLogger("SummaryDetails");

    DataSet FullData = new DataSet();

    ArrayList<String[]> featuresArrays;

    ArrayList<String> featStrings;

    public boolean genearteTrainFiles = true;

    public boolean genearteTestFiles = true;

    public boolean trainAll = false;

    public boolean testAll = true;

    double C = 100;

    double g = 0.01;

    boolean useAllFeatures = true;

    private int Inputformat = DataSet.FILE_INPUT_FORMAT_TORCH;

    private boolean StoreErrors = true;

    ImageErrors imageEr = null;

    /**
	 * 
	 */
    public void initClassifier(int alg, int kernel) {
        this.singleClass = new CommandClassifierController();
        singleClass.createClassifier(alg);
        singleClass.setKernel(kernel);
        singleClass.setC(C);
        singleClass.setGamma(g);
    }

    public MultiClassClassifier() {
    }

    public boolean isUseAllFeatures() {
        return useAllFeatures;
    }

    public void setUseAllFeatures(boolean useAllFeatures) {
        this.useAllFeatures = useAllFeatures;
    }

    /*******
		 * 
		 * Steps to do the libsvm 
		 * first read the full ovo features files using "dataset"
		  
		 * Generate 45 file to train each classifier based on the feature set...
		 * Genearte the classifier data from teh data set == > 
		 * train 45 classifier and genreate the model files .......
		 * 
		 * for test 
		 * sperate the features into 45 files 
		 * test each of the 45 classifer to get 45 perdict files
		 * read all 45 file which will gives 10 000 to   
		 *  loop on all 10000 samples for the 
		 *       get the classification of each of the 45 classifier and count the votes of this sample 
		 *       
		 * 
		 * 
		 * ********/
    public void ReadFeatureFile(String filename) {
        FeatureSet allFeat = new FeatureSet();
        allFeat.ReadAllFeatures("feat.txt");
        featuresArrays = new ArrayList<String[]>();
        featStrings = new ArrayList<String>();
        String line;
        try {
            logger.trace("reading the file................ wait");
            File afile = new File(filename);
            Scanner input = new Scanner(new BufferedReader(new FileReader(afile)));
            FileOutputStream file;
            String featuresString;
            String[] featuresArray;
            int[] FeaturesIndeces;
            int i;
            int j;
            String st = "";
            while (input.hasNext()) {
                st = "";
                i = input.nextInt();
                j = input.nextInt();
                featuresString = input.next();
                logger.trace(" classififer s " + i + "  " + j + "  the input string is " + featuresString);
                featuresArray = featuresString.trim().split(",");
                featuresArrays.add(featuresArray);
                allFeat.createFeatures(featuresArray);
                FeaturesIndeces = allFeat.getSelectedFeaturesIndeces();
                String tt = " ";
                for (int j2 = 0; j2 < featuresArray.length; j2++) {
                    st += "  +" + featuresArray[j2];
                    tt += FeaturesIndeces[j2] + " " + featuresArray[j2] + ",";
                }
                featStrings.add(st);
                logger.trace(" output is [ " + tt + " ] ");
            }
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void TrainOVOCommand(String filename, String dir) {
        logger.info(" in the train ..........");
        int k = 0;
        if (genearteTrainFiles) {
            FullData.setFormat(Inputformat);
            FullData.ReadFromFile(filename);
            DataSet TempData;
            for (int i = 0; i < classes; i++) {
                for (int j = i + 1; j < classes; j++) {
                    if (useAllFeatures) {
                        logger.info(" now genareting data for pair " + i + " vs " + j + " using all  features ");
                        TempData = FullData.GenearteClassDataSetTwoClasses(i, j);
                    } else {
                        logger.info(" now genareting data for pair " + i + " vs " + j + " using features " + featStrings.get(k));
                        TempData = FullData.GenearteClassDataSetTwoClasses(i, j, featuresArrays.get(k));
                    }
                    String Datafile = dir + "C" + i + "_" + j + ".txt";
                    String ModelFile = dir + "C" + i + "_" + j + ".model";
                    TempData.GenearteBinaryTargest(i, j);
                    TempData.setFormat(singleClass.getClassifierInputFormat());
                    TempData.SaveToFile(Datafile);
                    k++;
                }
            }
        }
        if (trainAll) {
            for (int i = 0; i < classes; i++) {
                for (int j = i + 1; j < classes; j++) {
                    String Datafile = dir + "C" + i + "_" + j + ".txt";
                    String ModelFile = dir + "C" + i + "_" + j + ".model";
                    logger.info("train the file " + Datafile + " to produce model file " + ModelFile);
                    singleClass.Train(Datafile, ModelFile);
                }
            }
        }
    }

    public void TestOVOCommand(String filename, String dir) {
        if (StoreErrors) {
            imageEr = new ImageErrors();
        }
        FullData.setFormat(Inputformat);
        FullData.ReadFromFile(filename);
        int k = 0;
        if (this.genearteTestFiles) {
            DataSet TempData;
            for (int i = 0; i < classes; i++) {
                for (int j = i + 1; j < classes; j++) {
                    if (useAllFeatures) {
                        TempData = FullData;
                        logger.info(" Now genareting data for pair " + i + " vs " + j + " using all  features ");
                        String Datafile = dir + "C_Test_OVO.txt";
                        TempData.setFormat(singleClass.getClassifierInputFormat());
                        TempData.SaveToFile(Datafile);
                        j = 10;
                        i = 10;
                        break;
                    } else {
                        logger.info(" now genareting data for pair " + i + " vs " + j + " using features " + featStrings.get(k));
                        TempData = FullData.GenearteFeatureDataSet(featuresArrays.get(k));
                        TempData.setFormat(singleClass.getClassifierInputFormat());
                        String Datafile = dir + "C_Test" + i + "_" + j + ".txt";
                        String ModelFile = dir + "C" + i + "_" + j + ".model";
                        String PerdictFile = dir + "C" + i + "_" + j + ".out";
                        TempData.SaveToFile(Datafile);
                    }
                    k++;
                }
            }
        }
        if (this.testAll) {
            DataSet TempData;
            for (int i = 0; i < classes; i++) {
                for (int j = i + 1; j < classes; j++) {
                    String Datafile = "";
                    if (useAllFeatures) {
                        Datafile = dir + "C_Test_OVO.txt";
                    } else {
                        Datafile = dir + "C_Test" + i + "_" + j + ".txt";
                    }
                    String ModelFile = dir + "C" + i + "_" + j + ".model";
                    String PerdictFile = dir + "C" + i + "_" + j + ".out";
                    logger.info("testing  the files " + Datafile + " to produce model file " + ModelFile);
                    singleClass.Test(Datafile, ModelFile, PerdictFile);
                }
            }
        }
        ResultSetAcc[][] ClassifiersResults = new ResultSetAcc[classes][classes];
        for (int i = 0; i < classes; i++) {
            for (int j = i + 1; j < classes; j++) {
                logger.info(" Reading the result of  C" + i + "_" + j + ".out");
                String PerdictFile = dir + "C" + i + "_" + j + ".out";
                ResultSetAcc results = new ResultSetAcc();
                results.ReadFromLibSVMFile(PerdictFile);
                ClassifiersResults[i][j] = results;
            }
        }
        logger.info("  After reading alll perdicts and now compute accuarcy.... .");
        ArrayList<Integer> Finalresults = new ArrayList<Integer>();
        double acc = 0;
        double[][] classifieracc = new double[classes][classes];
        double[] classAcc = new double[classes];
        int[] classCount = new int[classes];
        int[][] ClassifiersCount = new int[classes][classes];
        double[][] errors = new double[classes][classes];
        summaryDetails.info("  The feaures used in this test ..........................");
        summaryDetails.info(FullData.getAllFeatureString());
        int ErrorLeadTowrong = 0;
        int ErrorLeadToNoWrong = 0;
        boolean errorflag = false;
        ;
        for (int f = 0; f < classifieracc.length; f++) {
            classAcc[f] = 0.0;
            classCount[f] = 0;
            for (int i = 0; i < classifieracc[f].length; i++) {
                classifieracc[f][i] = 0;
                errors[f][i] = 0;
                ClassifiersCount[f][i] = 0;
            }
        }
        ArrayList<Integer> Targets;
        Targets = FullData.getAllTargets();
        int[] votes;
        int d;
        double score;
        ArrayList<String> classifiersWrong = new ArrayList<String>();
        logger.trace("  start computing the scores...........");
        for (int s = 0; s < FullData.getNumOfSamples(); s++) {
            d = Targets.get(s);
            for (int i = 0; i < classes; i++) {
                for (int j = i + 1; j < classes; j++) {
                    if (i == d || j == d) {
                        ClassifiersCount[i][j]++;
                    }
                }
            }
            if (d < classes) classCount[d]++;
            votes = new int[classes];
            for (int i = 0; i < votes.length; i++) {
                votes[i] = 0;
            }
            errorflag = false;
            classifiersWrong = new ArrayList<String>();
            for (int i = 0; i < classes; i++) {
                for (int j = i + 1; j < classes; j++) {
                    score = ClassifiersResults[i][j].getPerdicts().get(s);
                    if (score >= 1) {
                        votes[i]++;
                    } else {
                        votes[j]++;
                    }
                    if (d == i || d == j) {
                        if (score == 1) {
                            if (d == i) {
                                classifieracc[i][j]++;
                            } else {
                                errorflag = true;
                                errors[i][j]++;
                                classifiersWrong.add(" [" + i + " Vs. " + j + "] ");
                                logger.warn("  sample  " + s + " error in classifier  " + i + "  vs.  " + j + "  target is " + d + "  classified as " + i);
                            }
                        } else if (score < 1) {
                            if (d == j) {
                                classifieracc[i][j]++;
                            } else {
                                errorflag = true;
                                errors[i][j]++;
                                classifiersWrong.add(" [" + i + " Vs. " + j + "] ");
                                logger.warn("  sample  " + s + " error in classifier  " + i + "  vs.  " + j + "  target is " + d + "  classified as " + j);
                            }
                        }
                    }
                }
            }
            int finalvote = getMaxIndex(votes);
            Finalresults.add(new Integer(finalvote));
            if (d == finalvote) {
                acc++;
                if (d < classes) {
                    classAcc[d]++;
                }
                if (errorflag) {
                    ErrorLeadToNoWrong++;
                }
            } else {
                if (errorflag) {
                    String classifierWrong = "";
                    for (int l = 0; l < classifiersWrong.size(); l++) {
                        classifierWrong += " " + classifiersWrong.get(l);
                    }
                    String ste = "  Sample " + s + "  Error  classified as " + finalvote + " with " + votes[finalvote] + " votes supposed to be " + d + " but got " + votes[d] + "  votes (classifiers that made mistakes are ==>" + classifierWrong + " )  ";
                    if (StoreErrors) {
                        imageEr.addSampleError(s, finalvote, ste);
                    }
                    summaryDetails.info(ste);
                    ErrorLeadTowrong++;
                }
            }
        }
        int numberOfCorrect = (int) acc;
        acc /= ((double) FullData.getNumOfSamples());
        acc = acc * 100.0;
        String kernel = "";
        kernel = singleClass.getAlgorithmString();
        summary.info("  This test uses " + kernel + " algorithm and feaures used in this test are    ");
        summary.info(FullData.getAllFeatureString());
        summary.info("         ");
        summary.info("   the final accuracy is " + acc + " %  = (" + numberOfCorrect + "/ " + FullData.getNumOfSamples() + ")");
        summary.info("   The details of results are ... ");
        summary.info("  There are  " + ErrorLeadToNoWrong + " errors lead to no wrong classification ");
        summary.info("  There are  " + ErrorLeadTowrong + " errors lead to  wrong classification ");
        summary.info("__________________________________________________________________________");
        summary.info("  ");
        int numC = 0;
        double perc;
        double err;
        for (int i = 0; i < classes; i++) {
            perc = 100.0 * (classAcc[i] / (double) classCount[i]);
            err = classCount[i] - classAcc[i];
            summary.info(" Class  " + i + "     " + perc + " % (" + classAcc[i] + " /" + classCount[i] + ") [  " + err + "  errors] ");
        }
        summary.info("__________________________________________________________________________");
        summary.info("  ");
        double TotalPercent = ((1.0 / 2000.0) * 100);
        double per = 0;
        for (int i = 0; i < classes; i++) {
            for (int j = i + 1; j < classes; j++) {
                TotalPercent = ((1.0 / ClassifiersCount[i][j]) * 100);
                per = TotalPercent * classifieracc[i][j];
                summary.info(" Classifier  " + i + " & " + j + "  = " + per + " % (" + classifieracc[i][j] + " /" + ClassifiersResults[i][j].NumOfSamples + ") [ " + errors[i][j] + " error] ");
            }
        }
    }

    private int getMaxIndex(double[] votes) {
        int index = 0;
        double max = votes[0];
        for (int i = 0; i < votes.length; i++) {
            if (votes[i] > max) {
                max = votes[i];
                index = i;
            }
        }
        return index;
    }

    private int getMaxIndex(int[] votes) {
        int index = -1;
        int max = -1;
        for (int i = 0; i < votes.length; i++) {
            if (votes[i] > max) {
                max = votes[i];
                index = i;
            }
        }
        return index;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
    }

    public void setInputFormat(int fromFormat) {
        this.Inputformat = fromFormat;
    }

    public ImageErrors getImageEr() {
        return imageEr;
    }

    public void setImageEr(ImageErrors imageEr) {
        this.imageEr = imageEr;
    }

    public boolean isStoreErrors() {
        return StoreErrors;
    }

    public void setStoreErrors(boolean storeErrors) {
        StoreErrors = storeErrors;
    }
}
