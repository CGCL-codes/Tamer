package net.sf.pged.gui.gfactory;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.pged.gui.EditorState;
import net.sf.pged.gui.graph.VisualEdge;
import net.sf.pged.gui.graph.VisualVertex;
import net.sf.pged.plugins.ongraph.info.VertexDegreeDialog;
import net.sf.pged.pmanager.format.FormatPluginFileFilter;
import net.sf.pged.project.GraphProject;
import net.sf.pged.project.ProjectFactory;
import net.sf.pged.project.ProjectKeeper;

/**
 * @author dude03
 *
 */
public class GraphicFactory {

    private GraphicFactory() {
    }

    ;

    private static JFrame mainFrame = null;

    private static JTabbedPane tabbedPane = null;

    private static JProgressBar progressBar = null;

    private static NewProjectDialog newProjectDialog = null;

    private static EdgePropertyDialog edgePropertyDialog = null;

    private static VertexPropertyDialog vertexPropertyDialog = null;

    private static VertexDegreeDialog vertexDegreeDialog = null;

    private static final int GRAPH_NAME_MAX_SIZE = 15;

    public static void initFactory(JFrame frame, JTabbedPane tabpane, FormatPluginFileFilter[] exportImportFileFilters, FormatPluginFileFilter[] exportFileFilters) {
        mainFrame = frame;
        tabbedPane = tabpane;
        edgePropertyDialog = new EdgePropertyDialog(mainFrame);
        vertexPropertyDialog = new VertexPropertyDialog(mainFrame);
        vertexDegreeDialog = new VertexDegreeDialog(mainFrame);
        tabbedPane.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent event) {
                ProjectKeeper keeper = (ProjectKeeper) tabbedPane.getSelectedComponent();
                if (keeper != null) {
                    GraphProject project = keeper.getProject();
                    if (project.getSavedFlag()) {
                    }
                    if (project.getGraph().isOrient()) EditorState.setInsertMenuMode(EditorState.ORIENT_EDITMODE); else EditorState.setInsertMenuMode(EditorState.UNORIENT_EDITMODE);
                } else {
                    EditorState.setFileMenuMode(EditorState.CLOSE_FILE_MENUMODE);
                    EditorState.setEditMenuMode(EditorState.CLOSE_EDIT_MENUMODE);
                    EditorState.setInsertMenuMode(EditorState.CLOSE_INSERT_MENUMODE);
                }
            }
        });
    }

    public static GraphProject getCurrentProject() {
        ProjectKeeper keeper = (ProjectKeeper) tabbedPane.getSelectedComponent();
        if (keeper != null) return keeper.getProject();
        return null;
    }

    public static void setCurrentProject(GraphProject project) {
        ProjectKeeper keeper = (ProjectKeeper) tabbedPane.getSelectedComponent();
        if (keeper != null) {
            keeper.setProject(project);
            repaintCurTab();
        }
    }

    public static ArrayList<ObjectForSelecting> showGraphSelectingDialog(int minSel, int maxSel) {
        return null;
    }

    public static ArrayList<ObjectForSelecting> showSelectingDialog(ArrayList<ObjectForSelecting> items, int minSel, int maxSel) {
        return items;
    }

    public static void createNewProject() {
        GraphProject project = ProjectFactory.newProject();
        project.setName("Untitled");
        if (newProjectDialog == null) {
            newProjectDialog = new NewProjectDialog(mainFrame);
        }
        newProjectDialog.setProject(project);
        setPrettyDialogLocation(newProjectDialog);
        newProjectDialog.setVisible(true);
    }

    public static void createNewTab(GraphProject project) {
        tabbedPane.addTab(project.getName(), new GraphicJPanel(project));
        tabbedPane.repaint();
        EditorState.setEditMenuMode(EditorState.NO_SELECTED_EDIT_MENUMODE);
        EditorState.setFileMenuMode(EditorState.FULL_FILE_MENUMODE);
    }

    public static void repaintCurTab() {
        tabbedPane.repaint();
    }

    public static int showSaveRequest() {
        return JOptionPane.showConfirmDialog(mainFrame, "Do you want to save this project?");
    }

    public static void setPrettySize(Component c) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        c.setSize(screenWidth / 2, screenHeight / 2);
    }

    public static void setPrettyLocation(Component c) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        c.setLocation(screenWidth / 4, screenHeight / 4);
    }

    public static void setPrettyDialogLocation(Component c) {
        Dimension frameSize = mainFrame.getSize();
        Point frameLocation = mainFrame.getLocation();
        c.setLocation((int) (frameLocation.getX() + frameSize.getWidth() / 2), (int) (frameLocation.getY() + frameSize.getHeight() / 2));
    }

    public static void setFullScreenSize(Component c) {
        c.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    }

    public static void showError(String title, String message) {
        JOptionPane.showMessageDialog(mainFrame, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static String showInputDialog(String message) {
        return JOptionPane.showInputDialog(mainFrame, message);
    }

    public static void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(mainFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
	 * Show dialog for change property's of edge. 
	 * 
	 * @param edge the edge object for change.
	 * @return if property's of edge changed, then true, else false.
	 */
    public static boolean showEdgePropertyDlg(VisualEdge edge) {
        edgePropertyDialog.setEdge(edge);
        setPrettyDialogLocation(edgePropertyDialog);
        edgePropertyDialog.setVisible(true);
        return edgePropertyDialog.isEdgeChanged();
    }

    public static boolean showVertexPropertyDlg(VisualVertex vertex) {
        vertexPropertyDialog.setVertex(vertex);
        setPrettyDialogLocation(vertexPropertyDialog);
        vertexPropertyDialog.setVisible(true);
        return vertexPropertyDialog.isVertexChanged();
    }

    public static void showVertexDegreeDlg(Vector data) {
        vertexDegreeDialog.setVertexInfo(data);
        setPrettyDialogLocation(vertexDegreeDialog);
        vertexDegreeDialog.setVisible(true);
    }
}
