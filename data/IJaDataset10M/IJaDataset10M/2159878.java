package com.yoursway.crashkit.internal;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import com.yoursway.crashkit.CrashKit;
import com.yoursway.crashkit.exceptions.Blob;
import com.yoursway.crashkit.exceptions.NullReportedExceptionFailure;
import com.yoursway.crashkit.internal.model.ClaimedPackages;
import com.yoursway.crashkit.internal.model.ExceptionInfo;
import com.yoursway.crashkit.internal.model.Report;
import com.yoursway.crashkit.internal.model.ReportFile;
import com.yoursway.crashkit.internal.model.Repository;
import com.yoursway.crashkit.internal.model.Severity;
import com.yoursway.crashkit.internal.utils.EventScheduler;
import com.yoursway.crashkit.internal.utils.YsFileUtils;

public final class CrashKitImpl extends CrashKit {

    private final Repository storage;

    private final ServerConnection communicator;

    private final ClientCredentialsManager clientCredentialsManager;

    private final String productName;

    private final String productVersion;

    private final String role;

    private final EventScheduler scheduler = new EventScheduler();

    private final ClaimedPackages claimedPackages;

    private final Collection<String> blobsToSend = Collections.synchronizedList(new ArrayList<String>());

    public CrashKitImpl(String productName, String productVersion, String[] claimedPackages, Repository storage, ServerConnection communicator) {
        if (productName == null) throw new NullPointerException("productName is null");
        if (productVersion == null) throw new NullPointerException("productVersion is null");
        if (claimedPackages == null) throw new NullPointerException("claimedPackages is null");
        if (storage == null) throw new NullPointerException("storage is null");
        if (communicator == null) throw new NullPointerException("communicator is null");
        this.productName = productName;
        this.productVersion = productVersion;
        this.claimedPackages = new ClaimedPackages(claimedPackages);
        this.storage = storage;
        this.communicator = communicator;
        this.clientCredentialsManager = new ClientCredentialsManager(storage, communicator);
        this.role = determineRole(productName);
        if (!"customer".equals(role)) System.out.println(productName + " Feedback Role: " + role);
        new FeedbackPostingThread().start();
    }

    private static String determineRole(String productName) {
        String unixStyleName = productName.replaceAll("[^a-zA-Z0-9.]+", "");
        String override = System.getProperty(unixStyleName.toLowerCase() + ".feedback.role");
        if (override != null) return override;
        override = System.getenv(productName.replaceAll("[^a-zA-Z0-9]+", "_").toUpperCase() + "_FEEDBACK_ROLE");
        if (override != null && override.trim().length() > 0) return override.trim();
        File path = new File(new File(System.getProperty("user.home")), unixStyleName + ".role");
        try {
            override = YsFileUtils.readAsString(path).trim();
            if (override.length() > 0) return override;
        } catch (IOException e) {
        }
        return "customer";
    }

    protected void report(Severity severity, Throwable cause) {
        if (cause == null) cause = new NullReportedExceptionFailure();
        Map<String, String> data = new HashMap<String, String>();
        Collection<Blob> attachments = new ArrayList<Blob>();
        collectData(cause, data, attachments);
        Report report = new Report(severity.toString(), today(), ".", collectExceptions(cause), data, collectEnvironmentInfo(), role, "java", Constants.JAVA_CLIENT_VERSION);
        report.claimPackages(claimedPackages);
        scheduler.scheduleIn(Constants.NEW_EXCEPTION_DELAY);
        storage.addReport(report, attachments);
    }

    private String today() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setCalendar(Calendar.getInstance(TimeZone.getTimeZone("GMT")));
        String date = dateFormat.format(new Date());
        return date;
    }

    @SuppressWarnings("unchecked")
    private void collectData(Throwable cause, Map<String, String> data, Collection<Blob> attachments) {
        for (Throwable throwable = cause; throwable != null; throwable = throwable.getCause()) {
            try {
                Method method = throwable.getClass().getMethod("feedbackDetails");
                if (!Modifier.isStatic(method.getModifiers()) && Map.class.isAssignableFrom(method.getReturnType())) {
                    Map<String, Object> map = (Map<String, Object>) method.invoke(throwable);
                    if (map != null) {
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            if (value instanceof Blob) {
                                Blob blob = (Blob) value;
                                attachments.add(blob);
                                value = "blob:" + blob.getHash();
                            }
                            new Detail(key, value).addTo(data);
                        }
                    }
                }
            } catch (Throwable e) {
            }
        }
    }

    private List<ExceptionInfo> collectExceptions(Throwable cause) {
        List<ExceptionInfo> result = new ArrayList<ExceptionInfo>();
        processException(cause, null, result);
        return result;
    }

    private void processException(Throwable throwable, Throwable wrapper, List<ExceptionInfo> result) {
        Throwable cause = throwable.getCause();
        if (cause != null) processException(cause, throwable, result);
        result.add(encode(throwable, wrapper));
    }

    private ExceptionInfo encode(Throwable throwable, Throwable wrapper) {
        StackTraceElement[] ours = throwable.getStackTrace();
        int lastOurs = ours.length - 1;
        if (wrapper != null) {
            StackTraceElement[] theirs = wrapper.getStackTrace();
            int lastTheirs = theirs.length - 1;
            while (lastOurs >= 0 && lastTheirs >= 0 && ours[lastOurs].equals(theirs[lastTheirs])) {
                lastOurs--;
                lastTheirs--;
            }
        }
        StackTraceElement[] trace = new StackTraceElement[lastOurs + 1];
        System.arraycopy(ours, 0, trace, 0, lastOurs + 1);
        ExceptionInfo info = new ExceptionInfo(simpleNameOf(throwable.getClass()), throwable.getMessage(), trace);
        return info;
    }

    private Map<String, String> collectEnvironmentInfo() {
        Map<String, String> data = new HashMap<String, String>();
        putSystemProperty(data, "java_version", "java.version");
        data.put("product_name", productName);
        data.put("product_version", productVersion);
        putSystemProperty(data, "os_name", "os.name");
        putSystemProperty(data, "os_version", "os.version");
        putSystemProperty(data, "os_arch", "os.arch");
        putSystemProperty(data, "eclipse_build_id", "eclipse.buildId");
        putSystemProperty(data, "eclipse_product", "eclipse.product");
        putSystemProperty(data, "osgi_nl", "osgi.nl");
        putSystemProperty(data, "osgi_os", "osgi.os");
        putSystemProperty(data, "osgi_ws", "osgi.ws");
        data.put("cpu_count", Integer.toString(Runtime.getRuntime().availableProcessors()));
        return data;
    }

    private void putSystemProperty(Map<String, String> data, String key, String property) {
        String value = System.getProperty(property);
        if (value != null && value.length() > 0) data.put(key, value);
    }

    class FeedbackPostingThread extends Thread {

        public FeedbackPostingThread() {
            setName(productName + " Bug Reporter");
            setDaemon(true);
        }

        @Override
        public void run() {
            scheduler.scheduleIn(Constants.STARTUP_DELAY);
            while (true) {
                try {
                    scheduler.waitForScheduledEvent(Constants.MAXIMUM_WAIT_TIME);
                    Collection<ReportFile> reportsToRequeue = new ArrayList<ReportFile>();
                    try {
                        for (ReportFile report = storage.obtainReportToSend(); report != null; report = storage.obtainReportToSend()) {
                            try {
                                String text = report.read().trim();
                                try {
                                    communicator.sendReport(clientCredentialsManager.get(), "[" + text + "]", blobsToSend);
                                    report.delete();
                                } catch (ServerInitiatedError e) {
                                    handleDesignatedFailure(e);
                                    scheduler.scheduleIn(Constants.FAILED_DELIVERY_RETRY_DELAY);
                                }
                            } catch (IOException e) {
                                scheduler.scheduleIn(Constants.FAILED_DELIVERY_RETRY_DELAY);
                                reportsToRequeue.add(report);
                                System.out.println("Failed sending bug report: " + e.getMessage());
                            }
                        }
                        for (Iterator<String> iterator = blobsToSend.iterator(); iterator.hasNext(); ) {
                            String blob = iterator.next();
                            String body = storage.obtainBlob(blob);
                            if (body != null) try {
                                communicator.sendBlob(clientCredentialsManager.get(), blob, body);
                                iterator.remove();
                            } catch (IOException e) {
                                scheduler.scheduleIn(Constants.FAILED_DELIVERY_RETRY_DELAY);
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace(System.err);
                    }
                    for (ReportFile rf : reportsToRequeue) try {
                        rf.requeue();
                    } catch (Throwable e) {
                        e.printStackTrace(System.err);
                    }
                } catch (InterruptedException e1) {
                }
            }
        }
    }

    public void handleDesignatedFailure(ServerInitiatedError e) {
        if (e.is("invalid-client-id")) clientCredentialsManager.reset();
    }

    private static String simpleNameOf(Class<?> klass) {
        String simpleName = klass.getSimpleName();
        if (simpleName.length() == 0) {
            String fullName = klass.getName();
            simpleName = fullName.substring(fullName.lastIndexOf('.') + 1);
        }
        return simpleName;
    }
}
