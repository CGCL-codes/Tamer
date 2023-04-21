package Script.UAT.Framework;

import java.util.*;
import java.util.regex.*;

public class DataEngine {

    private KeywordParser _keyParser;

    private Pattern _dataPoolPat;

    private String[] _originSteps;

    private String[] _dataPools;

    private String _spliter;

    public DataEngine() {
        this._keyParser = KeywordParser.GetKeyParserIns();
        this._spliter = this._keyParser.GetSpliter();
        this._dataPoolPat = Pattern.compile("\\{[^{]+\\}");
    }

    public String[] BuildTestStepsWithData(String[] testSteps) {
        this._originSteps = testSteps;
        ArrayList stepsList = new ArrayList();
        this._dataPools = GetDataPool(testSteps);
        int len = this._dataPools.length;
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                String curDataPool = this._dataPools[i];
                AddTestData(curDataPool, testSteps, stepsList);
            }
        } else {
            return this._originSteps;
        }
        int listCount = stepsList.size();
        if (listCount > 0) {
            String[] temp = new String[listCount];
            for (int i = 0; i < listCount; i++) {
                temp[i] = stepsList.get(i).toString().trim();
            }
            return temp;
        } else {
            return this._originSteps;
        }
    }

    private void AddTestData(String dataPool, String[] testSteps, ArrayList stepsList) {
        if (stepsList == null) {
            stepsList = new ArrayList();
        }
        String[] dataPoolArr = dataPool.split(this._spliter);
        int stepsCount = testSteps.length;
        for (int i = 0; i < stepsCount; i++) {
            String[] curStep = testSteps[i].trim().split(this._spliter);
            String newCurStep = "";
            for (int j = 0; j <= 6; j++) {
                String curVal = curStep[j].toLowerCase();
                Matcher dataMatcher = this._dataPoolPat.matcher(curVal);
                if (dataMatcher.find()) {
                    try {
                        String dataVal = dataMatcher.group();
                        int dotPos = dataVal.indexOf(".");
                        String valName = dataVal.substring(dotPos + 1, dataVal.length() - 1);
                        int valIndex = Integer.parseInt(valName.replace("value", ""));
                        String realDataVal = dataPoolArr[valIndex];
                        curStep[j] = realDataVal;
                    } catch (Exception e) {
                    }
                }
                newCurStep += (curStep[j] + this._spliter);
            }
            stepsList.add(newCurStep);
        }
    }

    private String[] GetDataPool(String[] testSteps) {
        String dataPoolName = null;
        int len = testSteps.length;
        for (int i = 0; i < len; i++) {
            String curTestStep = testSteps[i].trim();
            if (curTestStep == null || curTestStep.equals("")) {
                throw new RuntimeException("Error: test step can not be null.");
            }
            Matcher dataMatcher = this._dataPoolPat.matcher(curTestStep);
            if (dataMatcher.find()) {
                String dataValue = dataMatcher.group();
                int dotPos = dataValue.indexOf(".");
                dataPoolName = dataValue.substring(1, dotPos);
                break;
            }
        }
        String[] tmp = null;
        if (dataPoolName != null && dataPoolName.length() > 0) {
            tmp = this._keyParser.GetDataByName(dataPoolName);
        }
        return tmp;
    }
}
