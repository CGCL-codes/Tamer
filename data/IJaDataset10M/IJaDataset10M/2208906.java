package com.sin;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.servlet.http.*;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.inject.Guice;
import com.google.inject.Injector;

@SuppressWarnings("serial")
public class JNPLServ0_backendServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(JNPLServ0_backendServlet.class.getName());

    private static int maxlines = 30;

    private static int minlines = 10;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("Start JNPLServ0_backendServlet");
        Injector injector = Guice.createInjector(new GuiceModule());
        JNPLServ0_backendServletManager jNPLServ0_backendServletManager = injector.getInstance(JNPLServ0_backendServletManager.class);
        Queue q = QueueFactory.getQueue("pull-markov");
        List<TaskHandle> tasks = q.leaseTasks(60, TimeUnit.SECONDS, 200);
        for (int i = 0; i < tasks.size(); i++) {
            String payload = new String(tasks.get(i).getPayload());
            if (jNPLServ0_backendServletManager.elaborateQueue(payload, maxlines, minlines)) {
                q.deleteTask(tasks.get(i).getName());
            } else {
                log.severe("Something wrong con jNPLServ0_backendServletManager!!");
            }
        }
    }
}
