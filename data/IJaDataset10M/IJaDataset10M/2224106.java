package applications;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import classifiers.wekaClassifier;
import classifiers.results.ClassifierResult;
import data.dataset.DataBaseConnector;
import data.dataset.DataSet;
import data.dataset.MNISTDataSetGenerator;
import data.image.Digit;
import tasks.RunnableTask;
import tasks.TaskController;
import tasks.TaskSettings;

/**
 * @author TOSHIBA
 *
 */
public class RunFeatureWithWekaClassifier extends Observable implements RunnableTask {

    private static final transient Logger logger = Logger.getLogger(RunFeatureWithWekaClassifier.class);

    private static final String OutDir = "D:\\AUC\\Programs\\Workspace\\DigitRecognition\\ouput\\stats\\";

    protected int MaxDigit = 10;

    protected ArrayList<ClassifierResult> FullDigitResults;

    protected String BaseLocation = "";

    @Override
    public void addObserver(TaskController taskController) {
    }

    @Override
    public TaskSettings getSettings() {
        return null;
    }

    @Override
    public void setSettings(TaskSettings task) {
    }

    public void runTheDigitsFeature(int d1, int d2, int format, ArrayList<String> featuresNames, String datasetfilename) {
        logger.info(" inside the rundigitfeature in main ");
        ArrayList<Integer> digitsForTest = new ArrayList<Integer>();
        digitsForTest.add(new Integer(d1));
        digitsForTest.add(new Integer(d2));
        Digit.loadAllFeatureArray();
        Digit.setFeaturesToCompute(featuresNames);
        MNISTDataSetGenerator db = new MNISTDataSetGenerator();
        db.setStatus(DataBaseConnector.TRAIN);
        Digit.DisplayFeatureString();
        DataSet dataset = db.GetDataSetByDigits(digitsForTest);
        dataset.setFormat(format);
        dataset.SaveToFile(datasetfilename);
    }

    public ClassifierResult createClassifier(String filename) {
        wekaClassifier weka = new wekaClassifier();
        weka.ReadDataFile(BaseLocation + filename);
        weka.setDefaultOptions();
        weka.createClassifier();
        weka.CrossValidate();
        return weka.getResult();
    }

    @Override
    public void run() {
        Date d = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_hh-mm");
        formatter.format(d);
        String dat = formatter.format(d);
        String filename = "TestDigit" + dat;
        Testdigits(filename);
    }

    @Override
    public void update(Observable o, Object arg) {
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        org.apache.log4j.PropertyConfigurator.configure("log4j.properties");
        RunFeatureWithWekaClassifier test = new RunFeatureWithWekaClassifier();
        DataBaseConnector.OS = DataBaseConnector.OS_WINDOWS;
        Thread th = new Thread(test);
        th.run();
    }

    public void Testdigits(String filename) {
        ClassifierResult result;
        FullDigitResults = new ArrayList<ClassifierResult>();
        for (int d1 = 0; d1 < MaxDigit; d1++) {
            for (int d2 = d1 + 1; d2 < MaxDigit; d2++) {
                logger.info("creating features for digits " + d1 + " vs. " + d2);
                result = RunComputeFeatureThenClassifier(d1, d2);
                FullDigitResults.add(result);
            }
        }
        logger.info(" now writing to excell file ");
        saveToExcelAllDigit(filename);
    }

    public void TestSpecificDigits(String filename) {
        ClassifierResult result;
        FullDigitResults = new ArrayList<ClassifierResult>();
        int d1, d2;
        d1 = 0;
        d2 = 5;
        logger.info("creating features for digits " + d1 + " vs. " + d2);
        result = RunComputeFeatureThenClassifier(d1, d2);
        FullDigitResults.add(result);
        d1 = 8;
        d2 = 5;
        logger.info("creating features for digits " + d1 + " vs. " + d2);
        result = RunComputeFeatureThenClassifier(d1, d2);
        d1 = 2;
        d2 = 3;
        logger.info("creating features for digits " + d1 + " vs. " + d2);
        result = RunComputeFeatureThenClassifier(d1, d2);
        logger.info(" now writing to excell file ");
        saveToExcelAllDigit(filename);
    }

    public ClassifierResult RunComputeFeatureThenClassifier(int d1, int d2) {
        ClassifierResult result;
        String filename = "test";
        ArrayList<String> feats = getFeaturesNames(d1, d2);
        logger.info(" features are " + ArrayToString(feats));
        runTheDigitsFeature(d1, d2, DataSet.FILE_INPUT_FORMAT_ARFF, feats, filename);
        logger.info("finished computing digit  " + d1 + " vs. " + d2 + " features in   " + ArrayToString(feats));
        result = createClassifier(filename + ".arff");
        logger.info(" classifier finished with result = " + result.getPercentCorrect());
        result.setClassifierString("Features" + ArrayToString(feats) + " digits" + d1 + "VS" + d2);
        result.setSmallString("C" + d1 + "Vs" + d2 + "F(" + ArrayToString(feats) + ")");
        return result;
    }

    public ArrayList<String> getFeaturesNames(int d1, int d2) {
        ArrayList<String> feats = new ArrayList<String>();
        if (d1 == 0 || d2 == 0) {
            feats.add("wsb");
            feats.add("lhg");
            feats.add("lvg");
            feats.add("BlackWide");
        }
        if (d1 == 1 || d2 == 1) {
            feats.add("pb");
            feats.add("hOw");
            feats.add("CountZeroTransition");
            feats.add("lhg");
            feats.add("MaxVBlackLength");
            feats.add("BlackWide");
        }
        if (d1 == 2 || d2 == 2) {
            feats.add("frright");
            feats.add("MaxHBlackLength");
            feats.add("MaxHBlackLengthLocation");
            feats.add("lvgi");
            feats.add("lvg");
            feats.add("lhgi");
            feats.add("lhg");
            feats.add("frleft");
            feats.add("dirMaxW");
            feats.add("PbCountR");
            feats.add("PbCountD");
            feats.add("PbCountL");
            feats.add("SrB3FromRight");
            feats.add("BlackWide");
            feats.add("frdown");
            feats.add("PbinL4R");
            feats.add("CountNegativeTransition");
            feats.add("SudenChangeFRight");
            feats.add("SudenChangeFRightLocation");
            feats.add("SudenChangeFLeft");
            feats.add("SudenChangeFLeftLocation");
            feats.add("FromLeftDown");
        }
        if (d1 == 3 || d2 == 3) {
            feats.add("frleft");
            feats.add("PbCountR");
            feats.add("PbCountL");
            feats.add("srby3");
            feats.add("pb");
            feats.add("dirMaxW");
            feats.add("PBinLeftVsRight");
            feats.add("SrB3FromRight");
            feats.add("PBinUpVsDown");
            feats.add("BlackWide");
            feats.add("SrB3FromRight");
            feats.add("CountNegativeTransition");
        }
        if (d1 == 4 || d2 == 4) {
            feats.add("wsb");
            feats.add("frup");
            feats.add("frdown");
            feats.add("frleft");
            feats.add("frright");
            feats.add("MaxVBlackLength");
            feats.add("MaxVBlackLengthLocation");
            feats.add("dirMaxW");
            feats.add("CountZeroTransition");
            feats.add("SudenChangeFRight");
            feats.add("SudenChangeFRightLocation");
        }
        if (d1 == 5 || d2 == 5) {
            feats.add("BlackWide");
            feats.add("SrB3FromRight");
            feats.add("FromRightUp");
            feats.add("PbinF4R");
            feats.add("PbinF4CinUpper");
            feats.add("CountLargeNegativeTransition");
            feats.add("dirMaxW");
            feats.add("MaxHBlackLength");
            feats.add("MaxHBlackLengthLocation");
            feats.add("PBinLeftVsRight");
            feats.add("PbCountR");
            feats.add("PbCountL");
            feats.add("PbCountU");
            feats.add("PbCountD");
            feats.add("CountZeroTransition");
            feats.add("SudenChangeFRight");
            feats.add("SudenChangeFRightLocation");
            feats.add("SudenChangeFLeft");
            feats.add("SudenChangeFLeftLocation");
        }
        if (d1 == 6 || d2 == 6) {
            feats.add("wsb");
            feats.add("frleft");
            feats.add("frright");
            feats.add("wsbInLower");
            feats.add("FromRightUp");
            feats.add("dirMaxW");
            feats.add("PBinLeftVsRight");
            feats.add("PBinUpVsDown");
            feats.add("PbCountL");
            feats.add("PbCountU");
            feats.add("CountNegativeTransition");
            feats.add("PbCountD");
            feats.add("SudenChangeFRight");
            feats.add("SudenChangeFRightLocation");
        }
        if (d1 == 7 || d2 == 7) {
            feats.add("MaxHBlackLength");
            feats.add("MaxHBlackLengthLocation");
            feats.add("MaxVBlackLength");
            feats.add("MaxVBlackLengthLocation");
            feats.add("frdown");
            feats.add("frleft");
            feats.add("frup");
            feats.add("PbCountU");
            feats.add("PbinF4R");
            feats.add("CountNegativeTransition");
            feats.add("CountLargeNegativeTransition");
            feats.add("BlackWide");
            feats.add("SudenChangeFLeft");
            feats.add("SudenChangeFLeftLocation");
        }
        if (d1 == 8 || d2 == 8) {
            feats.add("wsb");
            feats.add("pb");
            feats.add("BlackWide");
            feats.add("PbCountR");
            feats.add("PbCountL");
            feats.add("PbCountU");
            feats.add("PbCountD");
            feats.add("wsbInUpper");
            feats.add("wsbInLower");
            feats.add("PBinLeftVsRight");
            feats.add("PBinUpVsDown");
            feats.add("frup");
            feats.add("frdown");
            feats.add("PbinF4R");
            feats.add("PbinL4R");
            feats.add("PbinF4CinUpper");
        }
        if (d1 == 9 || d2 == 9) {
            feats.add("wsb");
            feats.add("frdown");
            feats.add("frleft");
            feats.add("frright");
            feats.add("dirMaxW");
            feats.add("lhg");
            feats.add("PBinLeftVsRight");
            feats.add("PBinUpVsDown");
            feats.add("wsbInUpper");
            feats.add("MaxVBlackLength");
            feats.add("MaxVBlackLengthLocation");
            feats.add("PbCountR");
            feats.add("PbCountU");
            feats.add("PbCountD");
            feats.add("PbinF4R");
            feats.add("PbinL4R");
            feats.add("PbinL4C");
            feats.add("CountNegativeTransition");
            feats.add("CountLargeNegativeTransition");
            feats.add("SudenChangeFLeft");
            feats.add("SudenChangeFLeftLocation");
        }
        ArrayList<String> featsUnrepeated = new ArrayList<String>();
        boolean found = false;
        for (int i = 0; i < feats.size(); i++) {
            found = false;
            for (int j = 0; j < featsUnrepeated.size(); j++) {
                if (feats.get(i).equals(featsUnrepeated.get(j))) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                featsUnrepeated.add(new String(feats.get(i)));
            }
        }
        Digit d = new Digit();
        d.loadAllFeatureArray();
        return new ArrayList<String>(d.getComputedFeatures());
    }

    public String ArrayToString(ArrayList<String> list) {
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            str += list.get(i) + " , ";
        }
        return str;
    }

    private void saveToExcelAllDigit(String filename) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("digit");
        HSSFRow row = sheet.createRow((short) 0);
        row.createCell((short) 0).setCellValue(new HSSFRichTextString(" Small string "));
        row.createCell((short) 1).setCellValue(new HSSFRichTextString(" Percent  Correct  "));
        row.createCell((short) 2).setCellValue(new HSSFRichTextString(" Number of error  "));
        for (int i = 0; i < FullDigitResults.size(); i++) {
            HSSFRow row2 = sheet.createRow((short) (i + 1));
            row2.createCell((short) (0)).setCellValue(new HSSFRichTextString(FullDigitResults.get(i).getSmallString()));
            row2.createCell((short) (1)).setCellValue(FullDigitResults.get(i).getPercentCorrect());
            row2.createCell((short) (2)).setCellValue(FullDigitResults.get(i).getNumberOfIncorrect());
        }
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(OutDir + filename + ".xls");
            wb.write(fileOut);
            fileOut.close();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
