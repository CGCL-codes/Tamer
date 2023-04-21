package freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.freemind.modes.mindmapmode;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.filechooser.FileFilter;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.freemind.main.Tools;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.freemind.modes.ControllerAdapter;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.freemind.modes.EdgeAdapter;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.freemind.modes.MapAdapter;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.freemind.modes.MindIcon;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.freemind.modes.MindMapCloud;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.freemind.modes.MindMapNode;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.freemind.modes.Mode;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.freemind.modes.StylePattern;

public class MindMapController extends ControllerAdapter {

    public boolean addAsChildMode = false;

    Vector iconActions = new Vector();

    FileFilter filefilter = new MindMapFilter();

    public Action edgeColorAction;

    public Action edgeWidthParentAction;

    public Action edgeWidthThinAction;

    public Action edgeWidthOneAction;

    public Action edgeWidthTwoAction;

    public Action edgeWidthFourAction;

    public Action edgeWidthEightAction;

    private static final int edgeWidths[] = { -1, 1, 2, 4, 8 };

    public MindMapController(Mode mode) {
        super(mode);
        updateActions();
        setAllActions(false);
        addAsChildMode = Tools.safeEquals(getFrame().getProperty("add_as_child"), "true");
    }

    public MapAdapter newModel(String title) {
        return new MindMapMapModel(getFrame(), title);
    }

    private void createIconActions() {
        Vector iconNames = MindIcon.getAllIconNames();
        for (int i = 0; i < iconNames.size(); ++i) {
            String iconName = ((String) iconNames.get(i));
            MindIcon myIcon = new MindIcon(iconName);
            Action myAction = new IconAction(myIcon);
            iconActions.add(myAction);
        }
    }

    public FileFilter getFileFilter() {
        return filefilter;
    }

    void setFontSize(int fontSize) {
        for (ListIterator e = getSelecteds().listIterator(); e.hasNext(); ) {
            MindMapNodeModel selected = (MindMapNodeModel) e.next();
            getModel().setFontSize(selected, fontSize);
        }
    }

    void setFontFamily(String fontFamily) {
        for (ListIterator e = getSelecteds().listIterator(); e.hasNext(); ) {
            MindMapNodeModel selected = (MindMapNodeModel) e.next();
            getModel().setFontFamily(selected, fontFamily);
        }
    }

    public void nodeChanged(MindMapNode n) {
    }

    public void anotherNodeSelected(MindMapNode n) {
    }

    protected MindMapNode newNode() {
        return new MindMapNodeModel("", getFrame());
    }

    /**
     * Link implementation: If this is a link, we want to make a popup with at
     * least removelink available.
     */
    public JPopupMenu getPopupForModel(java.lang.Object obj) {
        if (obj instanceof MindMapArrowLinkModel) {
            MindMapArrowLinkModel link = (MindMapArrowLinkModel) obj;
            JPopupMenu arrowLinkPopup = new JPopupMenu();
            arrowLinkPopup.addPopupMenuListener(this.popupListenerSingleton);
            arrowLinkPopup.add(new RemoveArrowLinkAction(link.getSource(), link));
            arrowLinkPopup.add(new ColorArrowLinkAction(link.getSource(), link));
            arrowLinkPopup.addSeparator();
            JRadioButtonMenuItem itemnn = new JRadioButtonMenuItem(new ChangeArrowsInArrowLinkAction("none", "images/arrow-mode-none.gif", link.getSource(), link, false, false));
            arrowLinkPopup.add(itemnn);
            JRadioButtonMenuItem itemnt = new JRadioButtonMenuItem(new ChangeArrowsInArrowLinkAction("forward", "images/arrow-mode-forward.gif", link.getSource(), link, false, true));
            arrowLinkPopup.add(itemnt);
            JRadioButtonMenuItem itemtn = new JRadioButtonMenuItem(new ChangeArrowsInArrowLinkAction("backward", "images/arrow-mode-backward.gif", link.getSource(), link, true, false));
            arrowLinkPopup.add(itemtn);
            JRadioButtonMenuItem itemtt = new JRadioButtonMenuItem(new ChangeArrowsInArrowLinkAction("both", "images/arrow-mode-both.gif", link.getSource(), link, true, true));
            arrowLinkPopup.add(itemtt);
            boolean a = !link.getStartArrow().equals("None");
            boolean b = !link.getEndArrow().equals("None");
            itemtt.setSelected(a && b);
            itemnt.setSelected(!a && b);
            itemtn.setSelected(a && !b);
            itemnn.setSelected(!a && !b);
            arrowLinkPopup.addSeparator();
            arrowLinkPopup.add(new GotoLinkNodeAction(link.getSource().toString(), link.getSource()));
            arrowLinkPopup.add(new GotoLinkNodeAction(link.getTarget().toString(), link.getTarget()));
            arrowLinkPopup.addSeparator();
            HashSet NodeAlreadyVisited = new HashSet();
            NodeAlreadyVisited.add(link.getSource());
            NodeAlreadyVisited.add(link.getTarget());
            Vector links = getModel().getLinkRegistry().getAllLinks(link.getSource());
            links.addAll(getModel().getLinkRegistry().getAllLinks(link.getTarget()));
            for (int i = 0; i < links.size(); ++i) {
                MindMapArrowLinkModel foreign_link = (MindMapArrowLinkModel) links.get(i);
                if (NodeAlreadyVisited.add(foreign_link.getTarget())) {
                    arrowLinkPopup.add(new GotoLinkNodeAction(foreign_link.getTarget().toString(), foreign_link.getTarget()));
                }
                if (NodeAlreadyVisited.add(foreign_link.getSource())) {
                    arrowLinkPopup.add(new GotoLinkNodeAction(foreign_link.getSource().toString(), foreign_link.getSource()));
                }
            }
            return arrowLinkPopup;
        }
        return null;
    }

    private MindMapMapModel getModel() {
        return (MindMapMapModel) getController().getModel();
    }

    private MindMapNodeModel getSelected() {
        return (MindMapNodeModel) getView().getSelected().getModel();
    }

    protected class ExportToHTMLAction extends AbstractAction {

        MindMapController c;

        public ExportToHTMLAction(MindMapController controller) {
            super(getText("export_to_html"));
            c = controller;
        }

        public void actionPerformed(ActionEvent e) {
            File file = new File(c.getModel().getFile() + ".html");
            if (c.getModel().saveHTML((MindMapNodeModel) c.getModel().getRoot(), file)) {
                loadURL(file.toString());
            }
        }
    }

    protected class ExportBranchToHTMLAction extends AbstractAction {

        MindMapController c;

        public ExportBranchToHTMLAction(MindMapController controller) {
            super(getText("export_branch_to_html"));
            c = controller;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                File file = File.createTempFile("tmm", ".html");
                if (c.getModel().saveHTML((MindMapNodeModel) getSelected(), file)) {
                    loadURL(file.toString());
                }
            } catch (IOException ex) {
            }
        }
    }

    private class ExportBranchAction extends AbstractAction {

        ExportBranchAction() {
            super(getText("export_branch"));
        }

        public void actionPerformed(ActionEvent e) {
            MindMapNodeModel node = (MindMapNodeModel) getSelected();
            if (getMap() == null || node == null || node.isRoot()) {
                return;
            }
            if (getMap().getFile() == null) {
                save();
            }
            JFileChooser chooser;
            if (getMap().getFile().getParentFile() != null) {
                chooser = new JFileChooser(getMap().getFile().getParentFile());
            } else {
                chooser = new JFileChooser();
            }
            if (getFileFilter() != null) {
                chooser.addChoosableFileFilter(getFileFilter());
            }
            int returnVal = chooser.showSaveDialog(getSelected().getViewer());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                URL link;
                String ext = Tools.getExtension(f.getName());
                if (!ext.equals("mm")) {
                    f = new File(f.getParent(), f.getName() + ".mm");
                }
                try {
                    link = f.toURL();
                } catch (MalformedURLException ex) {
                    JOptionPane.showMessageDialog(getView(), "couldn't create valid URL!");
                    return;
                }
                MindMapNodeModel parent = (MindMapNodeModel) node.getParentNode();
                MindMapNodeModel newNode = new MindMapNodeModel(node.toString(), getFrame());
                getModel().removeNodeFromParent(node);
                node.setParent(null);
                MindMapMapModel map = new MindMapMapModel(node, getFrame());
                if (getModel().getFile() != null) {
                    try {
                        map.setLink(node, Tools.toRelativeURL(f.toURL(), getModel().getFile().toURL()));
                    } catch (MalformedURLException ex) {
                    }
                }
                map.save(f);
                getModel().insertNodeInto(newNode, parent, 0);
                try {
                    String linkString = Tools.toRelativeURL(getModel().getFile().toURL(), f.toURL());
                    getModel().setLink(newNode, linkString);
                } catch (MalformedURLException ex) {
                }
                getModel().save(getModel().getFile());
            }
        }
    }

    private class ImportBranchAction extends AbstractAction {

        ImportBranchAction() {
            super(getText("import_branch"));
        }

        public void actionPerformed(ActionEvent e) {
            MindMapNodeModel parent = (MindMapNodeModel) getSelected();
            if (parent == null) {
                return;
            }
            JFileChooser chooser = new JFileChooser();
            if (getFileFilter() != null) {
                chooser.addChoosableFileFilter(getFileFilter());
            }
            int returnVal = chooser.showOpenDialog(getFrame().getContentPane());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    MindMapNodeModel node = getModel().loadTree(chooser.getSelectedFile());
                    getModel().paste(node, parent);
                } catch (Exception ex) {
                    handleLoadingException(ex);
                }
            }
        }
    }

    private class ImportLinkedBranchAction extends AbstractAction {

        ImportLinkedBranchAction() {
            super(getText("import_linked_branch"));
        }

        public void actionPerformed(ActionEvent e) {
            MindMapNodeModel parent = (MindMapNodeModel) getSelected();
            if (parent == null || parent.getLink() == null) {
                return;
            }
            URL absolute = null;
            try {
                String relative = parent.getLink();
                absolute = Tools.isAbsolutePath(relative) ? new File(relative).toURL() : new URL(getMap().getFile().toURL(), relative);
            } catch (MalformedURLException ex) {
                JOptionPane.showMessageDialog(getView(), "Couldn't create valid URL for:" + getMap().getFile());
                ex.printStackTrace();
                return;
            }
            try {
                MindMapNodeModel node = getModel().loadTree(new File(absolute.getFile()));
                getModel().paste(node, parent);
            } catch (Exception ex) {
                handleLoadingException(ex);
            }
        }
    }

    /**
     * This is exactly the opposite of exportBranch.
     */
    private class ImportLinkedBranchWithoutRootAction extends AbstractAction {

        ImportLinkedBranchWithoutRootAction() {
            super(getText("import_linked_branch_without_root"));
        }

        public void actionPerformed(ActionEvent e) {
            MindMapNodeModel parent = (MindMapNodeModel) getSelected();
            if (parent == null || parent.getLink() == null) {
                return;
            }
            URL absolute = null;
            try {
                String relative = parent.getLink();
                absolute = Tools.isAbsolutePath(relative) ? new File(relative).toURL() : new URL(getMap().getFile().toURL(), relative);
            } catch (MalformedURLException ex) {
                JOptionPane.showMessageDialog(getView(), "Couldn't create valid URL.");
                return;
            }
            try {
                MindMapNodeModel node = getModel().loadTree(new File(absolute.getFile()));
                for (ListIterator i = node.childrenUnfolded(); i.hasNext(); ) {
                    getModel().paste((MindMapNodeModel) i.next(), parent);
                }
            } catch (Exception ex) {
                handleLoadingException(ex);
            }
        }
    }

    private class NodeColorAction extends AbstractAction {

        NodeColorAction() {
            super(getText("node_color"));
        }

        public void actionPerformed(ActionEvent e) {
            Color color = JColorChooser.showDialog(getView().getSelected(), getFrame().getInternationalization("choose_node_color"), getSelected().getColor());
            if (color == null) {
                return;
            }
            for (ListIterator it = getSelecteds().listIterator(); it.hasNext(); ) {
                MindMapNodeModel selected = (MindMapNodeModel) it.next();
                getModel().setNodeColor(selected, color);
            }
        }
    }

    private class EdgeColorAction extends AbstractAction {

        EdgeColorAction() {
            super(getText("edge_color"));
        }

        public void actionPerformed(ActionEvent e) {
            Color color = JColorChooser.showDialog(getView().getSelected(), getFrame().getInternationalization("choose_edge_color"), getSelected().getEdge().getColor());
            if (color == null) return;
            for (ListIterator it = getSelecteds().listIterator(); it.hasNext(); ) {
                MindMapNodeModel selected = (MindMapNodeModel) it.next();
                getModel().setEdgeColor(selected, color);
            }
        }
    }

    private class CloudColorAction extends AbstractAction {

        CloudColorAction() {
            super(getText("cloud_color"), new ImageIcon(getResource("images/Colors24.gif")));
            putValue(Action.SHORT_DESCRIPTION, getValue(Action.NAME));
        }

        public void actionPerformed(ActionEvent e) {
            Color selectedColor = null;
            if (getSelected().getCloud() != null) selectedColor = getSelected().getCloud().getColor();
            Color color = JColorChooser.showDialog(getView().getSelected(), getFrame().getInternationalization("choose_cloud_color"), selectedColor);
            if (color == null) return;
            for (ListIterator it = getSelecteds().listIterator(); it.hasNext(); ) {
                MindMapNodeModel selected = (MindMapNodeModel) it.next();
                getModel().setCloudColor(selected, color);
            }
        }
    }

    protected class ColorArrowLinkAction extends AbstractAction {

        MindMapNode source;

        MindMapArrowLinkModel arrowLink;

        public ColorArrowLinkAction(MindMapNode source, MindMapArrowLinkModel arrowLink) {
            super(getText("arrow_link_color"), new ImageIcon(getResource("images/Colors24.gif")));
            this.source = source;
            this.arrowLink = arrowLink;
        }

        public void actionPerformed(ActionEvent e) {
            Color selectedColor = arrowLink.getColor();
            Color color = JColorChooser.showDialog(getView().getSelected(), (String) this.getValue(Action.NAME), selectedColor);
            if (color == null) return;
            getModel().setArrowLinkColor(source, arrowLink, color);
        }
    }

    private class IconAction extends AbstractAction {

        MindIcon icon;

        public IconAction(MindIcon _icon) {
            super(_icon.getDescription(getFrame()), _icon.getIcon(getFrame()));
            putValue(Action.SHORT_DESCRIPTION, _icon.getDescription(getFrame()));
            this.icon = _icon;
        }

        ;

        public void actionPerformed(ActionEvent e) {
            for (ListIterator it = getSelecteds().listIterator(); it.hasNext(); ) {
                MindMapNodeModel selected = (MindMapNodeModel) it.next();
                (getModel()).addIcon(selected, icon);
            }
        }

        ;
    }

    protected class RemoveArrowLinkAction extends AbstractAction {

        MindMapNode source;

        MindMapArrowLinkModel arrowLink;

        public RemoveArrowLinkAction(MindMapNode source, MindMapArrowLinkModel arrowLink) {
            super(getText("remove_arrow_link"), new ImageIcon(getResource("images/edittrash.png")));
            this.source = source;
            this.arrowLink = arrowLink;
        }

        public void actionPerformed(ActionEvent e) {
            getModel().removeReference(source, arrowLink);
        }
    }

    protected class ChangeArrowsInArrowLinkAction extends AbstractAction {

        MindMapNode source;

        MindMapArrowLinkModel arrowLink;

        boolean hasStartArrow;

        boolean hasEndArrow;

        public ChangeArrowsInArrowLinkAction(String text, String iconPath, MindMapNode source, MindMapArrowLinkModel arrowLink, boolean hasStartArrow, boolean hasEndArrow) {
            super("", iconPath != null ? new ImageIcon(getResource(iconPath)) : null);
            this.source = source;
            this.arrowLink = arrowLink;
            this.hasStartArrow = hasStartArrow;
            this.hasEndArrow = hasEndArrow;
        }

        public void actionPerformed(ActionEvent e) {
            getModel().changeArrowsOfArrowLink(source, arrowLink, hasStartArrow, hasEndArrow);
        }
    }

    private interface SingleNodeOperation {

        public void apply(MindMapMapModel map, MindMapNodeModel node);
    }

    private class NodeGeneralAction extends AbstractAction {

        SingleNodeOperation singleNodeOperation;

        NodeGeneralAction(String textID, String iconPath, SingleNodeOperation singleNodeOperation) {
            super(getText(textID), iconPath != null ? new ImageIcon(getResource(iconPath)) : null);
            putValue(Action.SHORT_DESCRIPTION, getText(textID));
            this.singleNodeOperation = singleNodeOperation;
        }

        public void actionPerformed(ActionEvent e) {
            for (ListIterator it = getSelecteds().listIterator(); it.hasNext(); ) {
                MindMapNodeModel selected = (MindMapNodeModel) it.next();
                singleNodeOperation.apply(getModel(), selected);
            }
        }
    }

    private String getWidthTitle(int width) {
        if (width == EdgeAdapter.WIDTH_PARENT) return getText("edge_width_parent");
        if (width == EdgeAdapter.WIDTH_THIN) return getText("edge_width_thin");
        return Integer.toString(width);
    }

    private class EdgeWidthAction extends AbstractAction {

        int width;

        EdgeWidthAction(int width) {
            super(getWidthTitle(width));
            this.width = width;
        }

        public void actionPerformed(ActionEvent e) {
            for (ListIterator it = getSelecteds().listIterator(); it.hasNext(); ) {
                MindMapNodeModel selected = (MindMapNodeModel) it.next();
                getModel().setEdgeWidth(selected, width);
            }
        }
    }

    private class JoinNodesAction extends AbstractAction {

        JoinNodesAction() {
            super(getText("join_nodes"));
        }

        public void actionPerformed(ActionEvent e) {
            ((MindMapMapModel) getView().getModel()).joinNodes();
        }
    }

    private class FollowLinkAction extends AbstractAction {

        FollowLinkAction() {
            super(getText("follow_link"));
        }

        public void actionPerformed(ActionEvent e) {
            loadURL();
        }
    }

    private class ForkAction extends AbstractAction {

        ForkAction() {
            super(getText(MindMapNode.STYLE_FORK));
        }

        public void actionPerformed(ActionEvent e) {
            for (ListIterator it = getSelecteds().listIterator(); it.hasNext(); ) {
                MindMapNodeModel selected = (MindMapNodeModel) it.next();
                getModel().setNodeStyle(selected, MindMapNode.STYLE_FORK);
            }
        }
    }

    private class BubbleAction extends AbstractAction {

        BubbleAction() {
            super(getText(MindMapNode.STYLE_BUBBLE));
        }

        public void actionPerformed(ActionEvent e) {
            for (ListIterator it = getSelecteds().listIterator(); it.hasNext(); ) {
                MindMapNodeModel selected = (MindMapNodeModel) it.next();
                getModel().setNodeStyle(selected, MindMapNode.STYLE_BUBBLE);
            }
        }
    }

    private class EdgeStyleAction extends AbstractAction {

        String style;

        EdgeStyleAction(String style) {
            super(getText(style));
            this.style = style;
        }

        public void actionPerformed(ActionEvent e) {
            for (ListIterator it = getSelecteds().listIterator(); it.hasNext(); ) {
                MindMapNodeModel selected = (MindMapNodeModel) it.next();
                getModel().setEdgeStyle(selected, style);
            }
        }
    }

    private class ApplyPatternAction extends AbstractAction {

        StylePattern pattern;

        ApplyPatternAction(StylePattern pattern) {
            super(pattern.getName());
            this.pattern = pattern;
        }

        public void actionPerformed(ActionEvent e) {
            for (ListIterator it = getSelecteds().listIterator(); it.hasNext(); ) {
                MindMapNodeModel selected = (MindMapNodeModel) it.next();
                ((MindMapMapModel) getModel()).applyPattern(selected, pattern);
            }
        }
    }

    private class MindMapFilter extends FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory()) return true;
            String extension = Tools.getExtension(f.getName());
            if (extension != null) {
                if (extension.equals("mm")) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        public String getDescription() {
            return getText("mindmaps_desc");
        }
    }

    public void increaseEdgeWidth() {
        for (ListIterator e = getSelecteds().listIterator(); e.hasNext(); ) {
            MindMapNodeModel selected = (MindMapNodeModel) e.next();
            int currentEdgeWidth = ((MindMapNodeModel) selected).getWidthWithParentInformation();
            int currentEdgeWidthArrayIndex = getEdgeWidthInEdgeWidths(currentEdgeWidth);
            if (currentEdgeWidthArrayIndex < edgeWidths.length - 1) getModel().setEdgeWidth(selected, edgeWidths[currentEdgeWidthArrayIndex + 1]);
        }
    }

    public void decreaseEdgeWidth() {
        for (ListIterator e = getSelecteds().listIterator(); e.hasNext(); ) {
            MindMapNodeModel selected = (MindMapNodeModel) e.next();
            int currentEdgeWidth = ((MindMapNodeModel) selected).getWidthWithParentInformation();
            int currentEdgeWidthArrayIndex = getEdgeWidthInEdgeWidths(currentEdgeWidth);
            if (currentEdgeWidthArrayIndex > 0) getModel().setEdgeWidth(selected, edgeWidths[currentEdgeWidthArrayIndex - 1]);
        }
    }

    private int getEdgeWidthInEdgeWidths(int edgeWidth) {
        int edgeWidthArrayIndex = 0;
        switch(edgeWidth) {
            case -1:
                edgeWidthArrayIndex = 0;
                break;
            case 0:
                edgeWidthArrayIndex = 1;
                break;
            case 1:
                edgeWidthArrayIndex = 1;
                break;
            case 2:
                edgeWidthArrayIndex = 2;
                break;
            case 4:
                edgeWidthArrayIndex = 3;
                break;
            case 8:
                edgeWidthArrayIndex = 4;
                break;
        }
        return edgeWidthArrayIndex;
    }

    private void edgeWidth(int edgeWidth) {
    }

    /**
     * Updates all Actions so that the right set of them is available depending
     * on the current edit mode
     */
    public void updateActions() {
        if (isEditable()) {
            editAction = new EditAction();
            newChildAction = new NewChildAction();
            cutAction = new CutAction(this);
            copyAction = new CopyAction(this);
            pasteAction = new PasteAction(this);
            removeAction = new RemoveAction();
            newSiblingAction = new NewSiblingAction();
            newPreviousSiblingAction = new NewPreviousSiblingAction();
            nodeUpAction = new NodeUpAction();
            nodeDownAction = new NodeDownAction();
            toggleFoldedAction = new ToggleFoldedAction();
            increaseFontSizeAction = new NodeGeneralAction("increase_node_font_size", null, new SingleNodeOperation() {

                public void apply(MindMapMapModel map, MindMapNodeModel node) {
                    map.increaseFontSize(node, 1);
                }
            });
            decreaseFontSizeAction = new NodeGeneralAction("decrease_node_font_size", null, new SingleNodeOperation() {

                public void apply(MindMapMapModel map, MindMapNodeModel node) {
                    map.increaseFontSize(node, -1);
                }
            });
            italicAction = new NodeGeneralAction("italic", "images/Italic24.gif", new SingleNodeOperation() {

                public void apply(MindMapMapModel map, MindMapNodeModel node) {
                    map.setItalic(node);
                }
            });
            boldAction = new NodeGeneralAction("bold", "images/Bold24.gif", new SingleNodeOperation() {

                public void apply(MindMapMapModel map, MindMapNodeModel node) {
                    map.setBold(node);
                }
            });
            nodeColorAction = new NodeColorAction();
            cloudAction = new NodeGeneralAction("cloud", "images/Cloud24.gif", new SingleNodeOperation() {

                private MindMapCloud lastCloud;

                private MindMapNodeModel nodeOfLastCloud;

                public void apply(MindMapMapModel map, MindMapNodeModel node) {
                    if (node.getCloud() != null) {
                        lastCloud = node.getCloud();
                        nodeOfLastCloud = node;
                    }
                    map.setCloud(node);
                    if ((node.getCloud() != null) && (node == nodeOfLastCloud)) {
                        node.setCloud(lastCloud);
                    }
                }
            });
            cloudColorAction = new CloudColorAction();
            edgeColorAction = new EdgeColorAction();
            edgeWidthParentAction = new EdgeWidthAction(EdgeAdapter.WIDTH_PARENT);
            edgeWidthThinAction = new EdgeWidthAction(EdgeAdapter.WIDTH_THIN);
            edgeWidthOneAction = new EdgeWidthAction(1);
            edgeWidthTwoAction = new EdgeWidthAction(2);
            edgeWidthFourAction = new EdgeWidthAction(4);
            edgeWidthEightAction = new EdgeWidthAction(8);
        } else {
            toggleFoldedAction = new ToggleFoldedAction();
        }
    }

    public void addNodeIcon(MindIcon icon) {
        for (ListIterator it = getSelecteds().listIterator(); it.hasNext(); ) {
            MindMapNodeModel selected = (MindMapNodeModel) it.next();
            (getModel()).addIcon(selected, icon);
        }
    }

    public void removeNodeIcon() {
        for (ListIterator it = getSelecteds().listIterator(); it.hasNext(); ) {
            MindMapNodeModel selected = (MindMapNodeModel) it.next();
            (getModel()).removeLastIcon(selected);
        }
    }
}
