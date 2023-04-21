package com.dotmarketing.viewtools;

import java.io.StringWriter;
import org.apache.velocity.tools.view.tools.ViewTool;
import com.dotmarketing.util.StringUtils;

public class StringsWebApi implements ViewTool {

    public void init(Object obj) {
    }

    public String formatPhoneNumber(String phoneNumber) {
        return StringUtils.formatPhoneNumber(phoneNumber);
    }

    public StringWriter getEmptyStringWriter() {
        return new StringWriter();
    }
}
