package com.bluestone.assertor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.spi.LoggingEvent;
import com.bluestone.util.Util;
import com.voxeo.logging.EncodedPatternLayout;

public class LoggingAssertor implements IAssertor, Runnable {

    static int port = 4560;

    static ServerSocket serverSocket;

    static Socket socket;

    static List<LoggingEvent> logs = new ArrayList();

    static List<AssertorFilter> filters = new ArrayList();

    static boolean isDefault = false;

    static boolean acceptLog = true;

    static Thread thread = null;

    static boolean shutdown = false;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        new LoggingAssertor().start();
    }

    public LoggingAssertor() {
    }

    private void addLog(LoggingEvent event) {
        boolean flag = true;
        for (AssertorFilter filter : filters) {
            flag = filter.doFilter(event);
            if (!flag) {
                break;
            }
        }
        if (flag) {
            Util.getLogger().info("get data from voexo:" + convertMsg(event));
            logs.add(event);
        }
    }

    public void assertAfter() {
        logs.clear();
    }

    public void assertBefore() {
        logs.clear();
    }

    public synchronized boolean assertExecute(String method, Object obj) {
        if (method.equalsIgnoreCase("assertcleardata")) {
            logs.clear();
            return true;
        }
        while (true) {
            if (logs.size() > 0) {
                break;
            }
            try {
                this.wait(200);
                Util.getLogger().info("wait 200 for LoggingAssertor");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (LoggingEvent event : logs) {
            if (method.equalsIgnoreCase("assertcontains")) {
                if (String.class.isInstance(obj)) {
                    String msg = Util.demaskTags(convertMsg(event));
                    if (msg.indexOf(Util.demaskTags((String) obj)) >= 0) {
                        return true;
                    }
                }
            } else if (method.equalsIgnoreCase("assertnotempty")) {
                return true;
            } else if (method.equalsIgnoreCase("assertcleardata")) {
                logs.clear();
                return true;
            }
        }
        return false;
    }

    public String convertMsg(LoggingEvent event) {
        String msg = null;
        msg = ((String) event.getMessage()).trim();
        msg = msg.substring(msg.indexOf("TTS:") + 4);
        msg = msg.replace("\r", "");
        msg = msg.replace("\n", "");
        msg = msg.replace("\t", "");
        if (msg.indexOf("<prosody") > 0 && msg.indexOf(" <prosody") < 0) {
            msg = msg.replaceAll("<prosody", " <prosody");
        }
        if (msg.indexOf("<break") > 0) {
            msg = msg.replaceAll("<break", " <break");
            msg = msg.replaceAll("  <break", " <break");
            msg = msg.replaceAll(", <break", ",<break");
            msg = msg.replaceAll("> <break", "><break");
        }
        return msg;
    }

    public void setFilters(List filters) {
        filters = filters;
    }

    public void start() {
        shutdown = false;
        thread = new Thread(new LoggingAssertor());
        thread.start();
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            Util.info("start LoggingAssertor " + port);
            while (true) {
                if (shutdown) {
                    break;
                }
                socket = serverSocket.accept();
                new LoggingSocket(socket, this).run();
            }
        } catch (Exception me) {
            Util.error("start LoggingAssertor failure " + me);
        }
    }

    public void stop() {
        try {
            shutdown = true;
            if (socket != null) {
                socket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
            Util.getLogger().info("stop loggingAssertor");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFilter(AssertorFilter filter) {
        filters.add(filter);
    }

    public void run() {
        startServer();
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean flag) {
        isDefault = flag;
    }

    public void putData(Object obj) {
        if (LoggingEvent.class.isInstance(obj)) {
            addLog((LoggingEvent) obj);
        }
    }
}
