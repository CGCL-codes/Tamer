package at.fhjoanneum.aim.sdi.project.window;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Window;

@SuppressWarnings("serial")
public class TopWindow extends Window {

    private static final Logger log = Logger.getLogger(TopWindow.class);

    public TopWindow() {
        log.info("TopWindow");
    }

    public void onCreate() {
    }

    public void onLogout() {
        Sessions.getCurrent().invalidate();
        Executions.sendRedirect("index.zul");
    }
}
