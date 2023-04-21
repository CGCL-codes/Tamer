package com.ingenta.workbench.data;

import com.ingenta.workbench.WorkbenchException;
import com.ingenta.workbench.WorkbenchUserError;
import java.util.ArrayList;

public class ServiceParameterOption {

    private String _label;

    private String _value;

    public ServiceParameterOption(String label, String value) {
        _label = label;
        _value = value;
    }

    public String getLabel() {
        return _label;
    }

    public String getValue() {
        return _value;
    }
}
