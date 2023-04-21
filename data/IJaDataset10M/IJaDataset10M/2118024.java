package com.pcmsolutions.device.EMU.E4.packaging;

import com.pcmsolutions.device.EMU.E4.preset.IsolatedSample;
import java.util.Map;

public interface SamplePackage {

    public static final String SAMPLE_DIR_EXT = "zspkg_dir";

    public static final String SAMPLE_PKG_EXT = "zspkg";

    public static final String SAMPLE_PKG_CONTENT_ENTRY = "zspkg_contents";

    public IsolatedSample[] getSamples();

    public Map getCustomObjectMap();

    public Header getHeader();

    public static interface Header extends PackageHeader {

        public int getSampleCount();

        public int getPhysicalSampleCount();
    }
}
