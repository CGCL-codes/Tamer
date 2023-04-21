package com.pbonhomme.xf.xml.writer;

import java.io.IOException;
import com.pbonhomme.xf.common.XFrameworkParameters;

public class WriterParameters extends XFrameworkParameters {

    private static final long serialVersionUID = 7234801222494995238L;

    public static final String BUNDLE_NAME = "writer";

    public static final String PROPERTIES_NAME = "writer.properties";

    public static final String PROPERTIES_PREFIX = "xframework.writer";

    /**
     * @throws IOException
     */
    public WriterParameters() throws IOException {
        super(BUNDLE_NAME, PROPERTIES_NAME);
    }

    public void reinitialize() {
        super.reload();
    }
}
