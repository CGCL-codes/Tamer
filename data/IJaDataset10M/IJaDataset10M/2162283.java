package net.sf.jqueryfaces.phaseListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

public class jQueryFacesPhaseListener implements PhaseListener {

    private static final String RESOURCES = "net/sf/jqueryfaces/resources";

    private static final String GIF = "image/gif";

    private static final String CSS = "text/css";

    private static final String PNG = "image/png";

    private static final String JS = "text/javascript";

    private static final String HTML = "text/html";

    private static final String XML = "text/xml";

    public jQueryFacesPhaseListener() {
    }

    public void afterPhase(PhaseEvent phaseEvent) {
        String viewId = phaseEvent.getFacesContext().getViewRoot().getViewId();
        int lastSlash = phaseEvent.getFacesContext().getViewRoot().getViewId().lastIndexOf("/") + 1;
        String filename = phaseEvent.getFacesContext().getViewRoot().getViewId().substring(lastSlash);
        String folderName = null;
        folderName = phaseEvent.getFacesContext().getViewRoot().getViewId().substring(0, lastSlash);
        if (filename.endsWith("jquery.js")) {
            writeResource(phaseEvent, RESOURCES + "/jquery.js", JS);
        }
        if (filename.endsWith("jquery-ui.js")) {
            writeResource(phaseEvent, RESOURCES + "/jquery-ui.js", JS);
        }
        if (filename.endsWith(".js") && phaseEvent.getFacesContext().getResponseComplete() == false) {
            writeResource(phaseEvent, RESOURCES + folderName + filename, JS);
        }
        if (filename.endsWith(".css")) {
            writeResource(phaseEvent, RESOURCES + folderName + filename, CSS);
        }
        if (filename.endsWith(".png")) {
            writeResource(phaseEvent, RESOURCES + folderName + filename, PNG);
        }
        if (filename.endsWith(".gif")) {
            writeResource(phaseEvent, RESOURCES + folderName + filename, GIF);
        }
        if (filename.endsWith(".html")) {
            writeResource(phaseEvent, RESOURCES + folderName + filename, HTML);
        }
        if (filename.endsWith(".xml")) {
            writeResource(phaseEvent, RESOURCES + folderName + filename, XML);
        }
    }

    public void beforePhase(PhaseEvent phaseEvent) {
    }

    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    private void writeResource(PhaseEvent phaseEvent, String source, String contentType) {
        HttpServletResponse response = ((HttpServletResponse) phaseEvent.getFacesContext().getExternalContext().getResponse());
        response.setContentType(contentType);
        OutputStream writer = null;
        try {
            writer = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(404);
            return;
        }
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(source);
        if (is == null) {
            return;
        }
        try {
            int len = 0;
            byte buf[] = new byte[2048];
            while ((len = is.read(buf)) != -1) {
                writer.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(404);
            return;
        }
        phaseEvent.getFacesContext().responseComplete();
    }
}
