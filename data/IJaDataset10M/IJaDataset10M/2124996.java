package skylight1.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

/**
 * Allows detailed logging of exceptions, including sending the information to a server. Particular care is taken not to
 * include any information that may be considered private by any reasonable person, such as phone id or location.
 *
 * The following permissions are required to write the exception to a server, and to include log messages in the
 * message, respectively: <code>
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.READ_LOGS" />
 * </code>
 */
public class LoggingExceptionHandler implements UncaughtExceptionHandler {

    private static final String XML_FORMAT = "<exception packageName=\"%s\" versionCode=\"%d\" versionName=\"%s\" threadName=\"%s\" time=\"%s\" \n\tphoneIdHash=\"%s\" device=\"%s\" configuration=\"%s\">\n\t<stackTrace>%s\n\t</stackTrace>\n\t<context>%s\n\t</context>\n\n\t<log>%s\n\t</log>\n</exception>";

    private static final String SIMPLE_FORMAT_NOTIME = "%s,%d,%s,%s,%s,%s,%s,%s,%s,%s";

    private static final Pattern LOG_MESSAGE_PATTERN = Pattern.compile("./[^(]+\\( *(\\d+)\\).*");

    private static URL loggingURL;

    private static boolean loggingURLSet;

    private final Context context;

    private UncaughtExceptionHandler originalHandler;

    private Date previousDate = new Date();

    private String previousMessage = "";

    /**
	 * Sets the server URL to which the information will be posted.  If null is passed, then exceptions will not be logged
	 * on a server, only to the Android log.
	 */
    public static void setURL(String aURLString) {
        loggingURLSet = true;
        if (aURLString == null || aURLString.length() == 0) {
            Log.e(LoggingExceptionHandler.class.getName(), "null URL passed, exceptions will not be sent to a server");
            return;
        }
        try {
            loggingURL = new URL(aURLString);
        } catch (MalformedURLException e) {
            final String errorMessage = String.format("Logging URL \"%s\" is malformed", aURLString);
            Log.e(LoggingExceptionHandler.class.getName(), errorMessage, e);
            throw new IllegalArgumentException(errorMessage, e);
        }
    }

    /**
	 * Creates the handler.
	 *
	 * @param aContext
	 *            Used to obtain information about the context of the exception. Also a <code>toString()</code> of the
	 *            context is included in the message, so it is recommended to provide a helpful toString() on your
	 *            context (i.e., activity, service, or application).
	 */
    public LoggingExceptionHandler(Context aContext) {
        context = aContext;
        if (!loggingURLSet) {
            throw new IllegalStateException("The static method setURL must be called before a handler can be instantiated.");
        }
        originalHandler = Thread.currentThread().getUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread aThread, Throwable aThrowable) {
        Date date = new Date();
        PhoneIdHasher pih = new PhoneIdHasher();
        final String message = String.format(XML_FORMAT, getPackageName(), getVersionNumber(), getVersionName(), getThreadName(aThread), getTimeAsString(date), pih.getHashedPhoneId(context), getdeviceInformation(), getConfiguration(), getStackTrace(aThrowable), getContextString(), getLog());
        final String simplerMessage = String.format(SIMPLE_FORMAT_NOTIME, getPackageName(), getVersionNumber(), getVersionName(), getThreadName(aThread), pih.getHashedPhoneId(context), getdeviceInformation(), getConfiguration(), getStackTrace(aThrowable), getContextString(), getLog());
        if (!simplerMessage.equals(previousMessage)) {
            Log.e(LoggingExceptionHandler.class.getName(), message);
            logToServer(message);
        } else if (date.getTime() > (previousDate.getTime() + 1000L)) {
            Log.e(LoggingExceptionHandler.class.getName(), message);
            logToServer(message);
        }
        previousMessage = simplerMessage;
        previousDate = date;
        if (originalHandler != null) {
            originalHandler.uncaughtException(aThread, aThrowable);
        }
    }

    private String getVersionName() {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            Log.e(LoggingExceptionHandler.class.getName(), "Unable to get version name", e);
            return "Unable to obtain";
        }
    }

    private String getdeviceInformation() {
        return Build.DISPLAY;
    }

    private int getVersionNumber() {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            Log.e(LoggingExceptionHandler.class.getName(), "Unable to get version code", e);
            return 0;
        }
    }

    private String getThreadName(final Thread aThread) {
        return aThread.getName();
    }

    private String getConfiguration() {
        return context.getResources().getConfiguration().toString();
    }

    private String getContextString() {
        return context.toString();
    }

    private String getPackageName() {
        return context.getPackageName();
    }

    private Object getStackTrace(final Throwable aThrowable) {
        StringWriter stringWriter = new StringWriter();
        aThrowable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    private String getTimeAsString(Date date) {
        try {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            Log.e(LoggingExceptionHandler.class.getName(), "Unable to get phone id", e);
            return "Not Available";
        }
    }

    private String getLog() {
        if (context.checkCallingOrSelfPermission(Manifest.permission.READ_LOGS) == PackageManager.PERMISSION_DENIED) {
            return "No permission to read logs";
        }
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader logReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            Matcher matcher = LOG_MESSAGE_PATTERN.matcher("");
            while ((line = logReader.readLine()) != null) {
                matcher.reset(line);
                if (matcher.matches()) {
                    if (matcher.group(1).equals(Integer.toString(android.os.Process.myPid()))) {
                        printWriter.println(line);
                    }
                }
            }
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                printWriter.println(line);
            }
            return stringWriter.toString();
        } catch (IOException e) {
            Log.e(LoggingExceptionHandler.class.getName(), "Unable to read log", e);
            return "Not Available";
        }
    }

    /**
	 * Log the message to the server.
	 */
    private void logToServer(final String aLogMessage) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {
            Log.i(LoggingExceptionHandler.class.getName(), "Nor permitted to use Internet; exception message not sent.");
            return;
        }
        if (loggingURL != null) {
            try {
                final HttpURLConnection httpURLConnection = (HttpURLConnection) loggingURL.openConnection();
                httpURLConnection.setRequestProperty("Content-type", "text/xml");
                httpURLConnection.setRequestProperty("User-Agent", context.getApplicationContext().getPackageName());
                httpURLConnection.setRequestProperty("Content-Length", Integer.toString(aLogMessage.length()));
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.append(aLogMessage);
                printStream.close();
            } catch (Exception e) {
                Log.e(LoggingExceptionHandler.class.getName(), "Failed to send log message to server", e);
            }
        }
    }
}
