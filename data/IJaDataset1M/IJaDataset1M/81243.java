package core.web.controller;

import java.util.List;
import java.util.LinkedList;

public class ErrorController extends BasicController {

    protected static final String ERROR = "error";

    protected static final String SUCCESS = "success";

    private List<String> errors = new LinkedList<String>();

    private boolean hasFatalErrors = false;

    public List<String> getErrors() {
        return errors;
    }

    public void addError(String error) {
        errors.add(error);
    }

    public void addFatalError(String error) {
        errors.add(error);
        hasFatalErrors = true;
    }

    protected String basicPerform() throws Exception {
        String result = errorPerform();
        if (errors.size() > 0) {
            return ERROR;
        }
        return result;
    }

    protected String errorPerform() throws Exception {
        return SUCCESS;
    }

    public boolean isHasErrors() {
        return errors.size() > 0;
    }

    public boolean isHasFatalErrors() {
        return hasFatalErrors;
    }
}
