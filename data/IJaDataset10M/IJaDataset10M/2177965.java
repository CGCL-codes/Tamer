package JavaOrc.ui;

import JavaOrc.BlueInterface.DiagramDataModelShell;
import diagram.Diagram;
import diagram.DiagramModel;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import JavaOrc.diagram.DependencyLinkEditor;
import JavaOrc.diagram.AssociationLinkEditor;
import JavaOrc.diagram.InterfaceEditor;
import JavaOrc.diagram.SlotLink;
import JavaOrc.diagram.DependencyLinkRenderer;
import JavaOrc.diagram.RealizationLinkRenderer;
import JavaOrc.diagram.ClassFigure;
import JavaOrc.diagram.AssociationLinkRenderer;
import JavaOrc.diagram.RealizationLinkEditor;
import JavaOrc.diagram.DependencyLink;
import JavaOrc.diagram.InterfaceFigure;
import JavaOrc.diagram.DeviceRenderer;
import JavaOrc.diagram.SlotFigure;
import JavaOrc.diagram.SimpleLinkEditor;
import JavaOrc.diagram.DeviceFigure;
import JavaOrc.diagram.RealizationLink;
import JavaOrc.diagram.NoteEditor;
import JavaOrc.diagram.AssociationLink;
import JavaOrc.diagram.ClassRenderer;
import JavaOrc.diagram.GeneralizationLinkRenderer;
import JavaOrc.diagram.DeviceEditor;
import JavaOrc.diagram.NoteRenderer;
import JavaOrc.diagram.ClassEditor;
import JavaOrc.diagram.InterfaceRenderer;
import JavaOrc.diagram.GeneralizationLinkEditor;
import JavaOrc.diagram.SimpleLinkRenderer;
import JavaOrc.diagram.CompositionLink;
import JavaOrc.diagram.SlotLinkRenderer;
import JavaOrc.diagram.CompositionLinkEditor;
import JavaOrc.diagram.NoteFigure;
import JavaOrc.diagram.SlotEditor;
import JavaOrc.diagram.SlotRenderer;
import JavaOrc.diagram.CompositionLinkRenderer;
import JavaOrc.diagram.SimpleLink;
import JavaOrc.diagram.GeneralizationLink;
import JavaOrc.diagram.TextEditor;
import JavaOrc.diagram.TextFigure;
import JavaOrc.diagram.TextRenderer;
import JavaOrc.ui.FileAction.SimpleFilter;

/**
 * @class DiagramContainer
 *
 * @date 08-20-2001
 * @author Eric Crahen
 * @version 1.0
 */
public class DiagramContainer extends JScrollPane {

    protected Action saveAction = new SaveAction();

    protected Action closeAction = new CloseAction();

    protected Action printAction = new PrintAction();

    protected Action scaledPrintAction = new ScaledPrintAction();

    protected Action exportAction = new ExportGIFAction();

    protected Action resizeAction = new ResizeAction();

    protected Action copyAction = new CopyAction();

    protected Action cutAction = new CutAction();

    protected Action pasteAction = new PasteAction();

    protected Dimension defaultSize;

    private DiagramDataModelShell ddmShell;

    private static final String[] colorProperties = { "composition.foreground", "composition.background", "class.foreground", "class.background", "device.foreground", "device.background", "slot.foreground", "slot.background", "association.foreground", "association.background", "dependency.foreground", "dependency.background", "diagram.foreground", "diagram.background", "generalization.foreground", "generalization.background", "interface.foreground", "interface.background", "note.foreground", "note.background", "realization.foreground", "realization.background", "simplelink.foreground", "simplelink.background", "text.foreground", "text.background" };

    private static final String[] fontProperties = { "composition.font", "class.font", "device.font", "slot.font", "association.font", "diagram.font", "generalization.font", "interface.font", "note.font", "dependency.font", "realization.font", "simplelink.font", "text.font" };

    /**
   * Create a new Container for a diagram
   */
    public DiagramContainer() {
        super(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS);
        setView(createDiagram());
        DiagramDataModelShell shl = new DiagramDataModelShell();
        shl.setModel(getView().getModel());
        setDataShell(shl);
    }

    /**
   * Update the menu bar. Add toggle option, etc.
   *
   * @param FlatMenuBar
   */
    public void updateMenus(FlatMenuBar menuBar) {
        JMenu menu = menuBar.getMenu("File");
        menu.add(new NewAction());
        menu.add(new OpenAction());
        menu.add(closeAction);
        menu.add(saveAction);
        menu.addSeparator();
        menu.add(exportAction);
        menu.addSeparator();
        menu.add(printAction);
        menu.add(scaledPrintAction);
        menu = menuBar.getMenu("Edit");
        menu.add(copyAction);
        menu.add(cutAction);
        menu.add(pasteAction);
        menu = menuBar.getMenu("Options");
        menu.add(resizeAction);
        menu.add(new FontAction(this, fontProperties));
        menu.add(new ColorAction(this, colorProperties));
        menu.add(new JSeparator(), -1);
        JCheckBoxMenuItem item = new JCheckBoxMenuItem(new ToggleRefreshAction());
        item.setState(true);
        menu.add(item, -1);
        menu = menuBar.getMenu("Tool");
        menu.add(new CSoundBuildAction(this));
    }

    /**
   * Create a new diagram
   */
    public Diagram createDiagram() {
        Diagram diagram = new Diagram();
        diagram.setFigureRenderer(CompositionLink.class, new CompositionLinkRenderer());
        diagram.setFigureRenderer(AssociationLink.class, new AssociationLinkRenderer());
        diagram.setFigureRenderer(GeneralizationLink.class, new GeneralizationLinkRenderer());
        diagram.setFigureRenderer(RealizationLink.class, new RealizationLinkRenderer());
        diagram.setFigureRenderer(DependencyLink.class, new DependencyLinkRenderer());
        diagram.setFigureRenderer(SimpleLink.class, new SimpleLinkRenderer());
        diagram.setFigureRenderer(SlotLink.class, new SlotLinkRenderer());
        diagram.setFigureRenderer(ClassFigure.class, new ClassRenderer());
        diagram.setFigureRenderer(DeviceFigure.class, new DeviceRenderer());
        diagram.setFigureRenderer(SlotFigure.class, new SlotRenderer());
        diagram.setFigureRenderer(InterfaceFigure.class, new InterfaceRenderer());
        diagram.setFigureRenderer(NoteFigure.class, new NoteRenderer());
        diagram.setFigureRenderer(TextFigure.class, new TextRenderer());
        diagram.setFigureEditor(NoteFigure.class, new NoteEditor());
        diagram.setFigureEditor(ClassFigure.class, new ClassEditor());
        diagram.setFigureEditor(DeviceFigure.class, new DeviceEditor());
        diagram.setFigureEditor(SlotFigure.class, new SlotEditor());
        diagram.setFigureEditor(InterfaceFigure.class, new InterfaceEditor());
        diagram.setFigureEditor(TextFigure.class, new TextEditor());
        diagram.setFigureEditor(CompositionLink.class, new CompositionLinkEditor());
        diagram.setFigureEditor(GeneralizationLink.class, new GeneralizationLinkEditor());
        diagram.setFigureEditor(RealizationLink.class, new RealizationLinkEditor());
        diagram.setFigureEditor(DependencyLink.class, new DependencyLinkEditor());
        diagram.setFigureEditor(AssociationLink.class, new AssociationLinkEditor());
        diagram.setFigureEditor(SimpleLink.class, new SimpleLinkEditor());
        defaultSize = new Dimension();
        defaultSize.setSize(2000, 2000);
        if (defaultSize != null) resizeDiagram(diagram, defaultSize);
        return diagram;
    }

    /**
   * Get the diagram for this view.
   *
   * @return Diagram
   */
    public Diagram getView() {
        return (Diagram) getViewport().getView();
    }

    /**
   * Set the diagram for this view.
   *
   * @param Diagram
   */
    public void setView(Diagram diagram) {
        Diagram oldDiagram = getView();
        setViewportView(diagram);
        if (diagram == null) {
            closeAction.setEnabled(false);
            saveAction.setEnabled(false);
            exportAction.setEnabled(false);
            printAction.setEnabled(false);
            scaledPrintAction.setEnabled(false);
            resizeAction.setEnabled(false);
            copyAction.setEnabled(false);
            cutAction.setEnabled(false);
            pasteAction.setEnabled(false);
        } else if (oldDiagram == null) {
            closeAction.setEnabled(true);
            saveAction.setEnabled(true);
            exportAction.setEnabled(true);
            printAction.setEnabled(true);
            scaledPrintAction.setEnabled(true);
            resizeAction.setEnabled(true);
            copyAction.setEnabled(true);
            cutAction.setEnabled(true);
            pasteAction.setEnabled(true);
        }
        super.firePropertyChange("diagram.container", oldDiagram, diagram);
    }

    /**
   * Find the Frame for this event
   */
    protected Component getFrame(ActionEvent e) {
        return getFrame((Component) e.getSource());
    }

    protected Frame getFrame(Component frame) {
        for (; !(frame instanceof Frame); frame = frame.getParent()) if (frame instanceof JPopupMenu) frame = ((JPopupMenu) frame).getInvoker();
        return (frame instanceof Frame) ? (Frame) frame : null;
    }

    public Frame getFrame() {
        return getFrame(this);
    }

    public void setDataShell(DiagramDataModelShell shell) {
        ddmShell = shell;
        Diagram dgrm = this.getView();
        dgrm.setModel(ddmShell.getModel());
        dgrm.repaint();
    }

    /**
   * Load an Icon with the IconManager
   */
    protected Icon getIcon(String name) {
        return IconManager.getInstance().getIconResource(this, name);
    }

    /**
   * Popup an error message
   */
    protected void displayError(Throwable t) {
        t.printStackTrace();
        displayError(t.getClass().getName(), t.getMessage());
    }

    /**
   * Popup an error message
   */
    protected void displayError(String title, String msg) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
   * Resize & update the diagram
   */
    protected void resizeDiagram(Diagram diagram, Dimension d) {
        diagram.setMinimumSize(d);
        diagram.setPreferredSize(d);
        diagram.setBounds(0, 0, d.width, d.height);
        doLayout();
    }

    /**
   * @class NewAction
   *
   */
    protected class NewAction extends AbstractAction {

        public NewAction() {
            super("New", getIcon("images/New.gif"));
        }

        public void actionPerformed(ActionEvent e) {
            setView(createDiagram());
            ddmShell.setModel(getView().getModel());
        }
    }

    /**
   * @class CloseAction
   *
   */
    protected class CloseAction extends AbstractAction {

        public CloseAction() {
            super("Close", getIcon("images/Close.gif"));
        }

        public void actionPerformed(ActionEvent e) {
            setView(null);
        }
    }

    /**
   * @class OpenAction
   *
   */
    protected class OpenAction extends FileAction {

        private SimpleFilter filter = new SimpleFilter("dia", "Diagrams");

        public OpenAction() {
            super("Open", getIcon("images/Open.gif"));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('O', Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = getChooser();
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileFilter(filter);
            if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(getFrame(e))) {
                openFile(chooser.getSelectedFile());
                ddmShell.setModel(getView().getModel());
            }
        }

        public void openFile(File file) {
            try {
                String name = file.getName().toLowerCase();
                if (!name.endsWith(".dia")) throw new RuntimeException("Not a valid diagram file extension");
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                Diagram diagram = getView();
                if (diagram == null) setView(diagram = createDiagram());
                diagram.setModel((DiagramModel) ois.readObject());
                diagram.repaint();
            } catch (Throwable t) {
                t.printStackTrace();
                displayError("File Error", "Invalid diagram file");
            }
        }
    }

    /**
   * @class SaveAction
   *
   */
    protected class SaveAction extends FileAction {

        private SimpleFilter filter = new SimpleFilter("dia", "Diagrams");

        public SaveAction() {
            super("Save", getIcon("images/Save.gif"));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('S', Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = getChooser();
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileFilter(filter);
            if (JFileChooser.APPROVE_OPTION == chooser.showSaveDialog(getFrame(e))) saveFile(chooser.getSelectedFile());
        }

        public void saveFile(File file) {
            String name = file.getName().toLowerCase();
            if (!name.endsWith(".dia")) file = new File(file.getPath() + ".dia");
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject((DiagramModel) getView().getModel());
            } catch (Throwable t) {
                t.printStackTrace();
                displayError("File Error", "Error writing to file");
            }
        }
    }

    /**
   * @class ExportAction
   *
   */
    protected class ExportGIFAction extends ExportAction {

        private SimpleFilter filter = new SimpleFilter("gif", "Images");

        public ExportGIFAction() {
            super("Save Image", getIcon("images/ExportImage.gif"));
        }

        protected Component getComponent() {
            return getView();
        }

        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = getChooser();
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileFilter(filter);
            if (JFileChooser.APPROVE_OPTION == chooser.showSaveDialog(getFrame(e))) {
                try {
                    writeGIF(chooser.getSelectedFile());
                } catch (Throwable t) {
                    displayError(t);
                }
            }
        }
    }

    /**
   * @class PrintAction
   *
   */
    protected class PrintAction extends PrintableAction {

        public PrintAction() {
            super("Print ...");
        }

        public Component getComponent() {
            return getView();
        }
    }

    /**
   * @class ScaledPrintAction
   *
   */
    protected class ScaledPrintAction extends ScaledPrintableAction {

        public ScaledPrintAction() {
            super("Scaled Print", getIcon("images/Print.gif"));
        }

        public Component getComponent() {
            return getView();
        }
    }

    /**
   * @class CopyAction
   *
   */
    protected class CopyAction extends AbstractAction {

        public CopyAction() {
            super("Copy", getIcon("images/Copy.gif"));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('C', Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            Diagram diagram = getView();
            if (diagram != null) {
                Action action = diagram.getActionMap().get("copy");
                if (action != null) action.actionPerformed(e);
            }
        }
    }

    /**
   * @class CutAction
   *
   */
    protected class CutAction extends AbstractAction {

        public CutAction() {
            super("Cut", getIcon("images/Cut.gif"));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('X', Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            Diagram diagram = getView();
            if (diagram != null) {
                Action action = diagram.getActionMap().get("cut");
                if (action != null) action.actionPerformed(e);
            }
        }
    }

    /**
   * @class PasteAction
   *
   */
    protected class PasteAction extends AbstractAction {

        public PasteAction() {
            super("Paste", getIcon("images/Paste.gif"));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('V', Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            Diagram diagram = getView();
            if (diagram != null) {
                Action action = diagram.getActionMap().get("paste");
                if (action != null) action.actionPerformed(e);
            }
        }
    }

    /**
   * @class ResizeAction
   *
   */
    protected class ResizeAction extends AbstractAction {

        public ResizeAction() {
            super("Resize ...");
        }

        public void actionPerformed(ActionEvent e) {
            Diagram diagram = getView();
            if (diagram != null) promptResize(diagram);
        }

        protected void promptResize(Diagram diagram) {
            SizePanel size = new SizePanel(diagram);
            int n = JOptionPane.showConfirmDialog(DiagramContainer.this, size, "Resize Diagram", JOptionPane.OK_CANCEL_OPTION);
            if (n == JOptionPane.OK_OPTION) {
                defaultSize = size.getDimension(defaultSize);
                resizeDiagram(diagram, defaultSize);
            }
        }
    }

    /**
   * @class ToggleRefreshAction
   *
   */
    protected class ToggleRefreshAction extends AbstractAction {

        public ToggleRefreshAction() {
            super("Fast refresh");
        }

        public void actionPerformed(ActionEvent e) {
            Diagram diagram = getView();
            if (diagram != null) {
                boolean toggle = !diagram.isFastRefreshEnabled();
                diagram.enableFastRefresh(toggle);
            }
        }
    }
}
