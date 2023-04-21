package sheep.utils.fileio;

import edu.uci.ics.jung.visualization.PersistentLayout.Point;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import sheep.controller.Workspace;
import java.io.*;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.*;
import sheep.model.Calendrier;
import sheep.model.Note;
import sheep.model.Project;
import sheep.model.Task;
import sheep.model.Task.TaskMode;
import sheep.model.Task.TaskState;
import sheep.model.WorkGroup;
import sheep.model.Worker;
import sheep.model.graph.Graph;
import sheep.model.graph.OurVertex;
import sheep.utils.fileio.WorkspaceLoader;
import sheep.utils.fileio.WorkspaceLoader;
import sheep.view.Component.CalendarCellRenderer.Adapter;

/**
 *
 * @author alexandre
 */
public class WorkspaceAutoLoader {

    private Workspace workSpace;

    private String fileName;

    private Document document;

    private Element root;

    public WorkspaceAutoLoader(Workspace w) {
        try {
            SAXBuilder sxb = new SAXBuilder();
            this.workSpace = w;
            document = sxb.build(new File("loader.xml"));
            if (document != null) root = document.getRootElement();
        } catch (JDOMException ex) {
            Logger.getLogger(WorkspaceLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
        }
    }

    public void loadWorkspace() {
        if (root != null) {
            File f = new File(root.getChild("path").getText());
            if (f.exists()) {
                WorkspaceLoader wl = new WorkspaceLoader(workSpace, root.getChild("path").getText());
                wl.loadWorkspace();
            }
        }
    }

    public void saveWorkspace() {
        Element r = new Element("workspace");
        Element path = new Element("path");
        r.addContent(path);
        path.addContent(workSpace.getSavePath() + workSpace.getSaveFile());
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        try {
            Document doc = new Document(r);
            out.output(doc, new FileOutputStream("loader.xml"));
        } catch (IOException ex) {
            Logger.getLogger(WorkspaceSaver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
