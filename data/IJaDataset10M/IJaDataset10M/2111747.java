package org.jsystemtest.course.tests;

import java.io.IOException;
import org.junit.Test;
import jsystem.framework.RunProperties;
import jsystem.framework.TestProperties;
import jsystem.framework.report.Summary;
import junit.framework.SystemTestCase4;

public class ReferenceOperations extends SystemTestCase4 {

    private String key;

    private String value;

    @Test
    @TestProperties(name = "Add run property", paramsInclude = { "key", "value" })
    public void addRunProperty() throws IOException {
        RunProperties.getInstance().setRunProperty(key, value);
    }

    @Test
    @TestProperties(name = "Add summary property", paramsInclude = { "key", "value" })
    public void addSummaryProperty() throws Exception {
        Summary.getInstance().setProperty(key, value);
    }

    @Test
    @TestProperties(name = "Report value ${value}", paramsInclude = { "value" })
    public void reportValue() {
        report.step("Reporting value: " + value);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
