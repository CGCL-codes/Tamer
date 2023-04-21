package com.dotmarketing.util.jasper;

import org.apache.jasper.JasperException;
import org.apache.jasper.JspC;

public class DotJasperTask extends JspC {

    @Override
    protected void processFile(String arg0) throws JasperException {
        if (!arg0.endsWith("_inc.jsp")) {
            super.processFile(arg0);
        } else {
        }
    }
}
