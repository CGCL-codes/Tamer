package com.luxoft.fitpro.core.testresult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ResultCollector {

    private ConcurrentHashMap<Integer, FitResult> suiteMap;

    private List<Integer> resultIndexer = new ArrayList<Integer>();

    public ResultCollector() {
        suiteMap = new ConcurrentHashMap<Integer, FitResult>();
    }

    public Integer getNewSuiteResultKeyAndRecordSuiteSummary(FitResult suiteSummary) {
        int resultKey = registerNewSuiteResultIndex();
        suiteMap.put(resultKey, suiteSummary);
        return resultKey;
    }

    private synchronized int registerNewSuiteResultIndex() {
        int resultIndexSize = resultIndexer.size();
        resultIndexer.add(resultIndexSize);
        return resultIndexSize;
    }

    public FitResult getResult() {
        FitResult fitResult = suiteMap.get(0);
        for (int i = 1; i < resultIndexer.size(); i++) {
            fitResult.addResult(suiteMap.get(i));
        }
        return fitResult;
    }

    public void appendResult(FitResult testResult) {
        appendResult(testResult, 0);
    }

    public void appendResult(FitResult testResult, int resultIndex) {
        FitResult fitResult = suiteMap.get(resultIndex);
        if (fitResult != null) {
            fitResult.addResult(testResult);
            suiteMap.replace(resultIndex, fitResult);
        } else {
            fitResult = testResult;
            suiteMap.put(resultIndex, fitResult);
        }
    }
}
