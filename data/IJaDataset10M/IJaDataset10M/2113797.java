package com.spukmk2me.debug;

public final class Logger {

    public static void InitialiseLoggingSystem() {
        if (UNINITIALISED) {
            CURRENT_EXPORTER = null;
            UNINITIALISED = false;
        }
    }

    public static void AssignExporter(MessageExporter exporter) {
        if (UNINITIALISED) return;
        if (CURRENT_EXPORTER != null) CURRENT_EXPORTER.Finalise();
        CURRENT_EXPORTER = exporter;
        if (CURRENT_EXPORTER != null) CURRENT_EXPORTER.Initialise();
    }

    public static void Log(String message) {
        if (!UNINITIALISED) {
            if (CURRENT_EXPORTER != null) CURRENT_EXPORTER.ExportMessage(message);
        }
    }

    public static void Trace(String message) {
        if (!UNINITIALISED) {
            if (CURRENT_EXPORTER != null) CURRENT_EXPORTER.ExportTracingMessage(message);
        }
    }

    public static void FinaliseLoggingSystem() {
        if (!UNINITIALISED) {
            if (CURRENT_EXPORTER != null) {
                CURRENT_EXPORTER.Finalise();
                CURRENT_EXPORTER = null;
            }
        }
    }

    private static MessageExporter CURRENT_EXPORTER;

    private static boolean UNINITIALISED = true;
}
