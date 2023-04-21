package org.monet.kernel.model;

import java.util.HashMap;
import org.monet.kernel.constants.Strings;
import org.monet.kernel.library.LibraryString;

public class DataRequest {

    protected String code;

    protected String path;

    protected Integer start;

    protected Integer limit;

    protected String condition;

    protected HashMap<String, String> parameters;

    public DataRequest() {
        this.code = Strings.EMPTY;
        this.path = Strings.EMPTY;
        this.start = 0;
        this.limit = 10;
        this.condition = Strings.EMPTY;
        this.parameters = new HashMap<String, String>();
    }

    public String getCode() {
        return this.code;
    }

    public Boolean setCode(String code) {
        this.code = code;
        return true;
    }

    public String getPath() {
        return this.path;
    }

    public Boolean setPath(String path) {
        this.path = path;
        return true;
    }

    public Integer getStartPos() {
        return this.start;
    }

    public Boolean setStartPos(Integer startPos) {
        this.start = startPos;
        return true;
    }

    public Integer getLimit() {
        return this.limit;
    }

    public Boolean setLimit(Integer limit) {
        this.limit = limit;
        return true;
    }

    public String getCondition() {
        return this.condition;
    }

    public Boolean setCondition(String condition) {
        this.condition = LibraryString.cleanAccents(condition);
        return true;
    }

    public HashMap<String, String> getParameters() {
        return this.parameters;
    }

    public String getParameter(String name) {
        return this.parameters.get(name);
    }

    public void addParameter(String name, String value) {
        this.parameters.put(name, value);
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }
}
