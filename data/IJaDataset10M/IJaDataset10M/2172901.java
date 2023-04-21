package at.the.gogo.parkoid.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Process;

public class CrashReportHandler implements UncaughtExceptionHandler {

    private final Activity m_context;

    public static void attach(final Activity context) {
        Thread.setDefaultUncaughtExceptionHandler(new CrashReportHandler(context));
    }

    private CrashReportHandler(final Activity context) {
        m_context = context;
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable exception) {
        final StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        final SharedPreferences uiState = m_context.getPreferences(0);
        final SharedPreferences.Editor editor = uiState.edit();
        editor.putString("error", stackTrace.toString());
        editor.commit();
        Process.killProcess(Process.myPid());
        System.exit(-1);
    }
}
