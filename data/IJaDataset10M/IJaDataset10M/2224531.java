package com.fourspaces.scratch.result;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fourspaces.scratch.mapping.ControllerMappingPath;

public class ResultList extends Result {

    protected List<Result> results = new ArrayList<Result>();

    public ResultList add(Result result) {
        results.add(result);
        return this;
    }

    @Override
    protected void process(HttpServletRequest request, HttpServletResponse response, ControllerMappingPath mappedPath, boolean contextRelativePaths) throws ResultException {
        for (Result result : results) {
            result.process(request, response, mappedPath, contextRelativePaths);
        }
    }

    @Override
    public boolean isError() {
        for (Result result : results) {
            if (result.isError()) {
                return true;
            }
        }
        return error;
    }
}
