package info.emptybrain.myna;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.concurrent.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ScriptTimerTask extends java.util.TimerTask {

    public static Semaphore threadPermit = new Semaphore(3);

    public String config;

    public void run() {
        try {
            MynaThread thread = new MynaThread();
            try {
                java.net.URI mynaRoot = JsCmd.class.getResource("/general.properties").toURI().resolve("../../");
                File jsFile = new File(mynaRoot.resolve("shared/js/libOO/run_cron.sjs"));
                thread.rootDir = mynaRoot.toString();
                thread.loadGeneralProperties();
                thread.environment.put("isCommandline", true);
                String[] args = { "", this.config };
                thread.environment.put("commandlineArguments", args);
                thread.handleRequest(jsFile.toURI().toString());
            } catch (Exception e) {
                thread.handleError(e);
            } finally {
                threadPermit.release();
            }
        } catch (Exception e) {
            System.err.println("============== Scheduled task error Error ============");
            System.err.println(e.toString());
            System.err.println("============== Stacktrace ============");
            e.printStackTrace(System.err);
            System.err.println("============== Config ============");
            System.err.println(this.config);
        }
    }
}
