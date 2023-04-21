package async;

import java.io.IOException;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

public class Async2 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(Async2.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final AsyncContext actx = req.startAsync();
        actx.setTimeout(30 * 1000);
        Runnable run = new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.currentThread().setName("Async2-Thread");
                    log.info("Putting AsyncThread to sleep");
                    Thread.sleep(2 * 1000);
                    log.info("Writing data.");
                    actx.getResponse().getWriter().write("Output from background thread. Time:" + System.currentTimeMillis() + "\n");
                    actx.complete();
                } catch (InterruptedException x) {
                    log.error("Async2", x);
                } catch (IllegalStateException x) {
                    log.error("Async2", x);
                } catch (IOException x) {
                    log.error("Async2", x);
                }
            }
        };
        Thread t = new Thread(run);
        t.start();
    }
}
