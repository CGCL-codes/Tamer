package de.gebea.fsrcp.internal.accelerometer.frstreamer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import de.gebea.fsrcp.accelerometer.streamer.IAccelerometerStreamer;

public class Activator implements BundleActivator {

    private static final String PROP_ACCELEROMETER_TOP = "freerunner.accelerometer.top";

    private static final String PROP_ACCELEROMETER_BOTTOM = "freerunner.accelerometer.bottom";

    public static final String DEFAULT_TOP_ACCELEROMETER = "/dev/input/event2";

    public static final String DEFAULT_BOTTOM_ACCELEROMETER = "/dev/input/event3";

    private static BundleContext context;

    public static BundleContext getContext() {
        return context;
    }

    private final InputStream[] streams = new InputStream[2];

    private final FRAccelerometerStreamer[] streamers = new FRAccelerometerStreamer[2];

    public void start(final BundleContext context) throws Exception {
        Activator.context = context;
        String topFile = context.getProperty(PROP_ACCELEROMETER_TOP);
        if (topFile == null) {
            topFile = DEFAULT_TOP_ACCELEROMETER;
        }
        final Properties properties = new Properties();
        properties.put(IAccelerometerStreamer.DIMENSIONS, "3");
        properties.put(IAccelerometerStreamer.CONTINUOUS, "true");
        properties.put(IAccelerometerStreamer.TYPE, "accelerometer");
        properties.put(IAccelerometerStreamer.UNIT, "mg");
        File file = new File(topFile);
        if (file.canRead()) {
            streams[0] = new FileInputStream(file);
            final FRAccelerometerStreamerRunnable runnable = new FRAccelerometerStreamerRunnable(streams[0]);
            properties.put(IAccelerometerStreamer.X_AXIS, "45");
            properties.put(IAccelerometerStreamer.Y_AXIS, "45");
            properties.put(IAccelerometerStreamer.Z_AXIS, "0");
            properties.put(IAccelerometerStreamer.SOURCE, topFile);
            properties.put(IAccelerometerStreamer.ACCELEROMETER_ID, PROP_ACCELEROMETER_TOP);
            streamers[0] = new FRAccelerometerStreamer(runnable, "Read accelerometer events (source: " + PROP_ACCELEROMETER_TOP + ")");
            runnable.setServiceImpl(streamers[0]);
            context.registerService(IAccelerometerStreamer.class.getName(), streamers[0], properties);
        }
        String bottomFile = context.getProperty(PROP_ACCELEROMETER_BOTTOM);
        if (bottomFile == null) {
            bottomFile = DEFAULT_BOTTOM_ACCELEROMETER;
        }
        file = new File(bottomFile);
        if (file.canRead()) {
            streams[1] = new FileInputStream(file);
            final FRAccelerometerStreamerRunnable runnable = new FRAccelerometerStreamerRunnable(streams[1]);
            properties.put(IAccelerometerStreamer.X_AXIS, "0");
            properties.put(IAccelerometerStreamer.Y_AXIS, "90");
            properties.put(IAccelerometerStreamer.Z_AXIS, "0");
            properties.put(IAccelerometerStreamer.SOURCE, bottomFile);
            properties.put(IAccelerometerStreamer.ACCELEROMETER_ID, PROP_ACCELEROMETER_BOTTOM);
            streamers[1] = new FRAccelerometerStreamer(runnable, "Read accelerometer events (source: " + PROP_ACCELEROMETER_BOTTOM + ")");
            runnable.setServiceImpl(streamers[1]);
            context.registerService(IAccelerometerStreamer.class.getName(), streamers[1], properties);
        }
    }

    public void stop(BundleContext context) throws Exception {
        for (final InputStream stream : streams) {
            stream.close();
        }
        for (final FRAccelerometerStreamer streamer : streamers) {
            streamer.cancel();
        }
        context = null;
    }
}
