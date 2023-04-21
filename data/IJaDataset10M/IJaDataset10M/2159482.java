package org.osmius.model.aux;

public class ResultSet {

    Result[] Result;

    public ResultSet() {
    }

    public ResultSet(Result[] result) {
        Result = result;
    }

    public Result[] getResult() {
        return Result;
    }

    public void setResult(Result[] result) {
        Result = result;
    }
}
