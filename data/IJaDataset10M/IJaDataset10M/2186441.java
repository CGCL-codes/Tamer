package com.mentorgen.tools.util.profile.bundle;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import com.mentorgen.tools.util.profile.ClientHelper;

public class OSGiServiceProfiler implements Runnable {

    private String profiler;

    private BundleServiceSerializer bundleService;

    private BundleContext context;

    public static final String PROFILER_HOST = "ca.ubc.jip.profiler.host";

    public static final String PROFILER_PORT = "ca.ubc.jip.profiler.port";

    public static final String PROFILER_PERIOD = "ca.ubc.jip.profiler.period";

    public static final String PROFILER_START_DELAY = "ca.ubc.jip.profiler.start.delay";

    public static final String INST_BUNDLE_ROOT = "ca.ubc.jip.profiler.inst.bundle.root";

    public static final String JAR_EXTENSION = ".jar";

    public static final String FILE_INSTALLATION_EXT = "file:";

    OSGiServiceProfiler(String _profiler, BundleContext _context) {
        this.profiler = _profiler;
        this.context = _context;
    }

    @SuppressWarnings("static-access")
    public void run() {
        try {
            ClientHelper.send("file " + profiler, context.getProperty(PROFILER_HOST), context.getProperty(PROFILER_PORT));
            Thread.sleep(Long.parseLong(context.getProperty(PROFILER_START_DELAY)));
            System.out.println("Profiling Started ...");
            ClientHelper.send("start", context.getProperty(PROFILER_HOST), context.getProperty(PROFILER_PORT));
            Thread.sleep(Long.parseLong(context.getProperty(PROFILER_PERIOD)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClientHelper.send("finish", context.getProperty(PROFILER_HOST), context.getProperty(PROFILER_PORT));
        System.out.println("Profiling Stopped.");
    }
}
