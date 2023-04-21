package com.ibm.tuningfork.infra.dogfooder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import com.ibm.tuningfork.infra.Logging;
import com.ibm.tuningfork.infra.feed.SocketTraceSource;
import com.ibm.tuningfork.tracegen.IFeedlet;
import com.ibm.tuningfork.tracegen.ILogger;
import com.ibm.tuningfork.tracegen.ITimerEvent;
import com.ibm.tuningfork.tracegen.IValueEvent;
import com.ibm.tuningfork.tracegen.LoggerFactory;

/**
 * Recursive instrumentation of TuningFork -- must eat our own dogfood!!
 */
public final class DogFooder {

    public static final boolean EAT_DOGFOOD = false;

    public static ILogger logger = LoggerFactory.makeNullLogger();

    public static IFeedlet signalFeedlet, activityFeedlet, primaryGUIFeedlet;

    public static ITimerEvent paintTimer, oscPaintTimer;

    public static IValueEvent signalET, loudnessET;

    private static final Map<Object, ITimerEvent> viewTimers = new HashMap<Object, ITimerEvent>();

    private static final Map<String, Object> figureNames = new HashMap<String, Object>();

    public static void init(String fileNameOrSocketNumber) {
        if (EAT_DOGFOOD && fileNameOrSocketNumber != null) {
            try {
                int port = Integer.parseInt(fileNameOrSocketNumber);
                logger = LoggerFactory.makeServerLogger(port);
                if (logger != null) Logging.msgln("Opened socket " + fileNameOrSocketNumber + " as TuningFork trace of this TuningFork execution");
            } catch (NumberFormatException nfe) {
                try {
                    String filename = fileNameOrSocketNumber.replaceFirst("@", SocketTraceSource.makeDateForTemporaryFilename());
                    logger = LoggerFactory.makeFileLogger(new File(filename));
                    if (logger != null) Logging.msgln("Opened file " + filename + " as TuningFork trace of this TuningFork execution");
                } catch (Exception e) {
                    Logging.errorln("error creating file: " + e);
                    e.printStackTrace();
                }
            } catch (Exception e) {
                Logging.errorln("error creating socket: " + e);
                e.printStackTrace();
            }
        }
        if (EAT_DOGFOOD) {
            instrumentationInit();
        }
    }

    private static void instrumentationInit() {
        addProperties(System.getProperties(), "Java: ");
        addMapToProperties(System.getenv(), "Env: ");
        primaryGUIFeedlet = logger.makeFeedlet("Primary GUI", "Main RCP Thread running the TuningFork GUI");
        primaryGUIFeedlet.bindToCurrentThread();
    }

    public static void registerView(Object view) {
        if (EAT_DOGFOOD) {
            String baseName = "Paint " + view.getClass().getSimpleName();
            String name = baseName;
            for (int i = 2; figureNames.get(name) != null; i++) {
                name = baseName + " " + i;
            }
            ITimerEvent event = logger.makeTimerEvent(name);
            figureNames.put(name, view);
            viewTimers.put(view, event);
        }
    }

    public static void startViewPaint(Object view) {
        if (EAT_DOGFOOD) {
            viewTimers.get(view).start();
        }
    }

    public static void stopViewPaint(Object view) {
        if (EAT_DOGFOOD) {
            viewTimers.get(view).stop();
        }
    }

    private static void addMapToProperties(Map<String, String> map, String prefix) {
        for (String key : map.keySet()) {
            logger.addProperty(prefix + key, map.get(key));
        }
    }

    private static void addProperties(Properties properties, String prefix) {
        for (Object key : properties.keySet()) {
            String value = properties.getProperty((String) key);
            logger.addProperty(prefix + key, value);
        }
    }
}
