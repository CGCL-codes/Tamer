package net.cordova.justus.profiler;

import java.util.HashMap;

public class ProfilerData {

    private ResultParser parsingData;

    private HashMap<String, ExecutionProgram> sourceByName;

    private HashMap<Integer, ExecutionProgram> sourceById;

    public ResultParser getParsingData() {
        return parsingData;
    }

    public void setParsingData(ResultParser parsingData) {
        this.parsingData = parsingData;
    }

    public HashMap<String, ExecutionProgram> getSourceByName() {
        return sourceByName;
    }

    public void setSourceByName(HashMap<String, ExecutionProgram> sourceByName) {
        this.sourceByName = sourceByName;
    }

    public HashMap<Integer, ExecutionProgram> getSourceById() {
        return sourceById;
    }

    public void setSourceById(HashMap<Integer, ExecutionProgram> sourceById) {
        this.sourceById = sourceById;
    }
}
