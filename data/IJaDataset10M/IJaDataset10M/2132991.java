package de.fzi.injectj.util;

import java.util.Vector;
import de.fzi.injectj.language.CodeMapper;

/**
 * @author Volker Kuttruff*/
public class Warning {

    private String warningCode;

    private Vector params = null;

    public Warning(String warningCode) {
        this.warningCode = warningCode;
    }

    public Warning(String warningCode, String param1) {
        this.warningCode = warningCode;
        addParameter(param1);
    }

    public Warning(String warningCode, String param1, String param2) {
        this.warningCode = warningCode;
        addParameter(param1);
        addParameter(param2);
    }

    public Warning(String warningCode, Vector params) {
        this.warningCode = warningCode;
        this.params = params;
    }

    public void addParameter(String value) {
        if (params == null) params = new Vector();
        params.addElement(value);
    }

    public String getMessage() {
        String result = CodeMapper.getText("WARNING_LABEL") + ": ";
        result = result + CodeMapper.getText(warningCode);
        for (int i = 0; i < params.size(); i++) {
            String toBeReplaced = "%" + String.valueOf(i + 1);
            String replacement = (String) params.elementAt(i);
            result = StringUtil.replace(result, toBeReplaced, replacement);
        }
        return result;
    }
}
