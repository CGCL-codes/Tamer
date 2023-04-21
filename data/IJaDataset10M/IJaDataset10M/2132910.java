package org.monet.kernel.model;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;

public class PushClient {

    private ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<String>();

    private AsyncContext context;

    private boolean isCompatibilityMode = false;

    private String viewId;

    public synchronized void refreshContext(AsyncContext context, boolean compatibilityMode) {
        if (this.context != null) {
            try {
                this.context.complete();
            } catch (Exception ex) {
            }
        }
        this.context = context;
        this.isCompatibilityMode = compatibilityMode;
        String message;
        while ((message = messageQueue.poll()) != null) {
            this.push(message);
        }
    }

    public synchronized void push(String message) {
        ServletResponse response = null;
        PrintWriter writer = null;
        try {
            if (this.context != null && (response = this.context.getResponse()) != null && (writer = response.getWriter()) != null) {
                if (isCompatibilityMode) {
                    writer.print("<script> parent.goPush(");
                    writer.print(message);
                    writer.print(");</script>");
                    writer.print("<p>                                                                                        </p>");
                    writer.print("<p>                                                                                        </p>");
                } else {
                    writer.println("/--push--/");
                    writer.println(message);
                    writer.println("/--end--/");
                }
                writer.flush();
                return;
            }
        } catch (IOException e) {
            try {
                this.context.complete();
            } catch (Exception ex) {
            }
            this.context = null;
        }
        this.messageQueue.add(message);
    }

    public void destroy() {
        if (this.context == null) return;
        try {
            this.context.complete();
        } catch (Exception ex) {
        }
    }

    public String getViewId() {
        return this.viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }
}
