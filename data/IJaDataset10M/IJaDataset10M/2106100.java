package org.formaria.editor.project.style;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolTip;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.formaria.debug.DebugLogger;
import org.formaria.editor.EditorDefaults;
import org.formaria.editor.project.ProjectListener;
import org.formaria.editor.project.EditorProject;
import org.formaria.editor.project.EditorProjectManager;
import org.formaria.editor.project.dialog.StyleEditorDialog;
import org.formaria.editor.project.dialog.StyleCellRenderer;
import org.formaria.editor.project.pages.IPagePanel;
import org.formaria.editor.project.pages.ComponentSizer;
import org.formaria.editor.project.pages.PageResource;
import org.formaria.editor.project.pages.components.ComponentHelper;
import org.formaria.editor.project.pages.components.PropertyHelper;
import org.formaria.editor.project.pages.components.proxy.Proxy;
import org.formaria.swing.Page;
import org.formaria.aria.ProjectManager;
import org.formaria.aria.style.Style;

/**
 * An editor component for Aria styles
 * <p>Copyright: Copyright (c) Formaria Ltd., 1998-2003</p>
 * $Revision: 1.19 $
 */
public class StyleEditor extends JPanel implements ActionListener, TreeSelectionListener, StyleListener, MouseListener, ProjectListener {

    private JTree styleTree;

    private JScrollPane stylePane;

    private JPanel mainPanel, spacer;

    private DefaultMutableTreeNode topNode;

    private Component targetComponent;

    private EditorProject currentProject;

    private Vector styleListeners;

    private EditorStyleManager styleManager;

    private Vector selectedComponents;

    private boolean listenerChanging = false;

    public StyleEditor(MouseListener listener) {
        setLayout(new BorderLayout());
        setVisible(false);
        currentProject = (EditorProject) ProjectManager.getCurrentProject();
        if (currentProject != null) styleManager = (EditorStyleManager) currentProject.getStyleManager();
        styleListeners = new Vector();
        setupStylePalette();
    }

    /**
   * Notification of project initialized.
   * @param project the editor project just initialized
   */
    public void projectInitialized(EditorProject project) {
    }

    public void projectLoaded(EditorProject project) {
        if (project != null) currentProject = project;
        createTree();
    }

    /**
   * The project has been updated
   */
    public void projectUpdated(EditorProject project) {
        if (project != null) currentProject = project;
        createTree();
    }

    public void saveProject(EditorProject project) {
    }

    public void checkProject(EditorProject project) {
    }

    public void resetProject(String moduleName, EditorProject project) {
    }

    public void setSelectedComponents(IPagePanel currentPagePanel, Vector targets, boolean contentsChanged) {
        EditorProject project = currentPagePanel.getProject();
        if (project != currentProject) projectUpdated(project);
        selectedComponents = targets;
        if ((currentProject != null) && (selectedComponents.size() == 1)) {
            ComponentSizer comp = (ComponentSizer) selectedComponents.elementAt(0);
            PropertyHelper helper = new ComponentHelper().getPropertyHelper(comp.getTarget());
            String attributeValue = helper.getPropertyValue(comp.getPageResource(), comp.getTarget(), "Style");
            attributeValue = (attributeValue.length() == 0) ? null : attributeValue;
            Style style = null;
            if (attributeValue != null) style = styleManager.getStyle(attributeValue);
            styleChanged(attributeValue, style);
        }
    }

    /**
   * Set a listener for style changes
   * @param listener the new listener
   */
    public void addStyleListener(StyleListener listener) {
        styleListeners.addElement(listener);
    }

    public Dimension getPreferredSize() {
        return new Dimension(200, 300);
    }

    public void setupStylePalette() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        add(mainPanel);
        spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(200, 1));
        spacer.setBorder(new EmptyBorder(0, 200, 0, 0));
        if (stylePane != null) {
            stylePane.getParent().remove(stylePane);
            stylePane = null;
        }
        topNode = new DefaultMutableTreeNode("Styles");
        styleTree = new JStyleTree(topNode);
        styleTree.setFont(EditorDefaults.defaultFont);
        styleTree.setCellRenderer(new StyleCellRenderer());
        styleTree.setScrollsOnExpand(true);
        styleTree.addTreeSelectionListener(this);
        styleTree.addMouseListener(this);
        stylePane = new JScrollPane(styleTree);
        stylePane.setBorder(new EmptyBorder(0, 0, 0, 0));
        styleTree.setBorder(new EmptyBorder(0, 0, 0, 0));
        mainPanel.add(stylePane, BorderLayout.CENTER);
        mainPanel.add(spacer, BorderLayout.SOUTH);
        spacer.doLayout();
        stylePane.doLayout();
        mainPanel.doLayout();
        doLayout();
        if (currentProject != null) currentProject.setObject("StyleEditor", this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Add...")) {
            String styleName = getTreePath();
            Style style = styleManager.getStyle(styleName);
            StyleEditorDialog sed = new StyleEditorDialog(true, style, styleName);
            sed.setLocationRelativeTo(this);
            sed.setVisible(true);
            if (sed.getStatus()) {
                Style updatedStyle = sed.getStyle();
                String path = getTreePath() + "/";
                if (path.compareTo("/") == 0) path = "";
                String updatedStyleName = path + sed.getStyleName();
                styleManager.addStyle(updatedStyleName, updatedStyle);
                updateStyle(sed.getStyle(), updatedStyleName, true);
                createTree();
                setProjectModified();
            }
        } else if (e.getActionCommand().equals("Edit...")) {
            String styleName = getTreePath();
            Style style = styleManager.getStyle(styleName);
            StyleEditorDialog sed = new StyleEditorDialog(false, style, styleName);
            sed.setLocationRelativeTo(this);
            sed.setVisible(true);
            if (sed.getStatus()) {
                sed.getStyle();
                String path = getTreePath() + "/";
                if (path.compareTo("/") == 0) path = "";
                String updatedStyleName = styleName;
                updateStyle(style, updatedStyleName, false);
                createTree();
                setProjectModified();
            }
        } else if (e.getActionCommand().equals("Color Scheme...")) {
            ColorSchemeDialog sed = new ColorSchemeDialog(currentProject.getLafName().indexOf("Synth") >= 0);
            sed.setLocationRelativeTo(this);
            sed.setVisible(true);
            if (sed.getStatus() == 0) {
                for (int i = 0; i < 6; i++) {
                    String styleName = sed.getStyleName(i);
                    Style style = styleManager.getStyle(styleName, false);
                    Style updatedStyle = (style == null ? new Style() : style);
                    updatedStyle.setStyle(Style.COLOR_FORE, sed.getForegroundColor(i));
                    updatedStyle.setStyle(Style.COLOR_BACK, sed.getBackgroundColor(i));
                    Font f = sed.getFont(i);
                    updatedStyle.setStyle(Style.FONT_FACE, f.getFontName());
                    updatedStyle.setStyle(Style.FONT_ITALIC, (f.isItalic() ? 1 : 0));
                    updatedStyle.setStyle(Style.FONT_SIZE, f.getSize());
                    updatedStyle.setStyle(Style.FONT_WEIGHT, f.isBold() ? 1 : 0);
                    updateStyle(updatedStyle, styleName, style == null);
                    if (style == null) {
                        styleManager.addStyle(styleName, updatedStyle);
                    }
                    updateStyle(updatedStyle, styleName, false);
                }
                createTree();
                setProjectModified();
            }
        } else if (e.getActionCommand().equals("Delete")) {
            styleManager.removeStyle(getTreePath());
            for (int i = 0; i < styleListeners.size(); i++) ((StyleListener) styleListeners.elementAt(i)).styleChanged(getTreePath(), null);
            updateComponentStyles(getTreePath(), false);
            createTree();
            setProjectModified();
        } else if (e.getActionCommand().equals("Add synth styles")) {
            Style updatedStyle = new Style();
            updatedStyle.setStyle(Style.COLOR_FORE, new Color(0, 0, 80));
            updatedStyle.setStyle(Style.COLOR_BACK, new Color(0, 0, 192));
            updatedStyle.setStyle(Style.FONT_FACE, "Arial");
            updatedStyle.setStyle(Style.FONT_ITALIC, 0);
            updatedStyle.setStyle(Style.FONT_SIZE, 10);
            updatedStyle.setStyle(Style.FONT_WEIGHT, 0);
            String styleName = "synthPanel";
            Style style = styleManager.getStyle(styleName, false);
            updateStyle(updatedStyle, styleName, style == null);
            if (style == null) {
                styleManager.addStyle(styleName, updatedStyle);
                styleManager.addStyle("synthPanelLight", updatedStyle);
                styleManager.addStyle("synthButton", updatedStyle);
                styleManager.addStyle("synthButtonRollover", updatedStyle);
                styleManager.addStyle("synthBorder", updatedStyle);
            }
            updateStyle(updatedStyle, styleName, false);
            createTree();
            setProjectModified();
        }
    }

    public void stylesUpdated() {
        createTree();
        updateComponentStyles();
    }

    private void setProjectModified() {
        ((EditorProject) EditorProjectManager.getCurrentProject()).setModified(true);
    }

    private void applyStyle(Style style) {
        Color fore = style.getStyleAsColor(style.COLOR_FORE);
        Color back = style.getStyleAsColor(style.COLOR_BACK);
        String tempfontface = style.getStyleAsString(style.FONT_FACE);
        if (tempfontface != null) {
            String fontface = tempfontface.substring(0, 1).toUpperCase();
            fontface += tempfontface.substring(1, tempfontface.length());
            int fontsize = style.getStyleAsInt(style.FONT_SIZE);
            int fontitalic = style.getStyleAsInt(style.FONT_ITALIC);
            int fontweight = style.getStyleAsInt(style.FONT_WEIGHT);
            int fontStyle = 0;
            if (fontweight == 1) fontStyle = Font.BOLD;
            if (fontitalic == 1) fontStyle = fontStyle | Font.ITALIC;
            Font font = new Font(fontface, fontStyle, fontsize);
            if (targetComponent != null) targetComponent.setFont(font);
        }
        if (targetComponent != null) {
            targetComponent.setForeground(fore);
            targetComponent.setBackground(back);
        }
    }

    private void updateStyle(Style modifiedStyle, String styleName, boolean newStyle) {
        if (!newStyle) {
            for (int i = 0; i < styleListeners.size(); i++) ((StyleListener) styleListeners.elementAt(i)).styleChanged(getTreePath(), modifiedStyle);
        }
        updateComponentStyles(styleName, newStyle);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) styleTree.getLastSelectedPathComponent();
        if (node == null) return;
        if (!newStyle) node.setUserObject((getStrippedPath((String) node.getUserObject())) + getStyleAttribs(modifiedStyle));
        styleTree.repaint();
    }

    private void updateComponentStyles() {
        if (currentProject != null) {
            HashMap pages = currentProject.getPageResources();
            Iterator keys = pages.keySet().iterator();
            while (keys.hasNext()) {
                String pageName = (String) keys.next();
                PageResource pageResource = (PageResource) pages.get(pageName);
                Page page = pageResource.getPage();
                if (page != null) {
                    styleManager = (EditorStyleManager) currentProject.getStyleManager();
                    String[] values = styleManager.getStylesArray();
                    for (int i = 0; i < values.length; i++) {
                        String styleName = values[i];
                        updateComponentStyle(pageResource, (Component) page, styleName, false);
                    }
                }
            }
        }
    }

    private void updateComponentStyles(String styleName, boolean newStyle) {
        if (currentProject != null) {
            if (!newStyle) {
                HashMap pages = currentProject.getPageResources();
                Iterator keys = pages.keySet().iterator();
                while (keys.hasNext()) {
                    String pageName = (String) keys.next();
                    PageResource pageResource = (PageResource) pages.get(pageName);
                    Page page = pageResource.getPage();
                    if (page != null) {
                        updateComponentStyle(pageResource, page, styleName, newStyle);
                    }
                }
            }
        }
    }

    private void updateComponentStyle(PageResource pageResource, Component comp, String styleName, boolean newStyle) {
        PropertyHelper helper = new ComponentHelper().getPropertyHelper(comp);
        if (helper == null) return;
        String oldStyleName = helper.getPropertyValue(pageResource, comp, "Style");
        if ((oldStyleName != null) && oldStyleName.equals(styleName)) {
            if (comp instanceof Proxy) comp = ((Proxy) comp).getProxiedComponent();
            helper.setPropertyValue(pageResource, comp, "Style", styleName);
        }
        if (comp instanceof Container) {
            Container cont = (Container) comp;
            int numChildren = cont.getComponentCount();
            for (int i = 0; i < numChildren; i++) updateComponentStyle(pageResource, cont.getComponent(i), styleName, newStyle);
        }
    }

    private DefaultMutableTreeNode createTree() {
        if (currentProject != null) {
            styleManager = (EditorStyleManager) currentProject.getStyleManager();
            String[] values = styleManager.getStylesArray();
            topNode.removeAllChildren();
            for (int i = 0; i < values.length; i++) {
                String rootPath = values[i];
                Style style = styleManager.getStyle(rootPath);
                String text = rootPath;
                int pos = text.lastIndexOf("/");
                if (pos > -1) text = text.substring(pos + 1, text.length());
                DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode(text + getStyleAttribs(style));
                getParentNode(rootPath).add(newRoot);
            }
        }
        TreePath path = new TreePath(topNode);
        styleTree.expandPath(path);
        styleTree.repaint();
        styleTree.updateUI();
        return topNode;
    }

    private DefaultMutableTreeNode getParentNode(String stylename) {
        int pos = stylename.lastIndexOf("/");
        if (pos > -1) stylename = stylename.substring(0, pos);
        stylename += getStyleAttribs(styleManager.getStyle(stylename));
        Vector nodes = new Vector();
        nodes.add(topNode);
        addNodes(nodes, topNode, stylename);
        if (nodes.size() > 0) {
            Object nodeArray[] = nodes.toArray();
            TreePath path = new TreePath(nodeArray);
            return (DefaultMutableTreeNode) path.getLastPathComponent();
        }
        return topNode;
    }

    /**
   * Responds to tree selection changes
   * @param evt
   */
    public void valueChanged(TreeSelectionEvent evt) {
        String styleName = getTreePath();
        Style style = styleManager.getStyle(styleName);
        applyStyle(style);
        if (styleName != null) {
            int idx = styleName.indexOf('|');
            if (idx > 0) styleName = styleName.substring(0, idx);
        }
    }

    /**
   * Update the selected component if double clicked
   * @param me
   */
    public void mouseClicked(MouseEvent me) {
        if (me.getClickCount() > 1) {
            if (listenerChanging) DebugLogger.logWarning("cannot assign style because listenerChanging is true");
            if (!listenerChanging) {
                listenerChanging = true;
                for (int i = 0; i < styleListeners.size(); i++) {
                    String styleName = getTreePath();
                    Style style = styleManager.getStyle(styleName);
                    ((StyleListener) styleListeners.elementAt(i)).styleChanged(styleName, style);
                }
                listenerChanging = false;
            }
        }
    }

    public void mousePressed(MouseEvent me) {
        if (me.isPopupTrigger()) showPopupMenu(me);
    }

    public void mouseReleased(MouseEvent me) {
        if (me.isPopupTrigger()) showPopupMenu(me);
    }

    private void showPopupMenu(MouseEvent me) {
        JPopupMenu popupMenu = new JPopupMenu("Styles");
        if (styleTree.getSelectionPath() != null) {
            if (styleTree.getSelectionPath().getPathCount() == 1) {
                JMenuItem mi = new JMenuItem("Add...");
                mi.addActionListener(this);
                popupMenu.add(mi);
            } else {
                JMenuItem mi = new JMenuItem("Edit...");
                mi.addActionListener(this);
                popupMenu.add(mi);
                mi = new JMenuItem("Add...");
                mi.addActionListener(this);
                popupMenu.add(mi);
                popupMenu.addSeparator();
                mi = new JMenuItem("Delete");
                mi.addActionListener(this);
                popupMenu.add(mi);
                popupMenu.addSeparator();
                mi = new JMenuItem("Color Scheme...");
                mi.addActionListener(this);
                popupMenu.add(mi);
                if (((EditorProject) currentProject).getLafName().equals("Synth")) {
                    popupMenu.addSeparator();
                    mi = new JMenuItem("Add synth styles");
                    mi.addActionListener(this);
                    popupMenu.add(mi);
                }
            }
            Point pt = me.getPoint();
            Point vp = styleTree.getLocation();
            popupMenu.show(this, pt.x + vp.x, pt.y + vp.y);
        }
    }

    public void mouseEntered(MouseEvent me) {
    }

    public void mouseExited(MouseEvent me) {
    }

    /**
   * Called when a style is changed. This allows the editor to reset the
   * components visually.
   * @param styleName the name of the style which has been changed
   * @param newstyle The new Style
   */
    public void styleChanged(String styleName, Style style) {
        if (!listenerChanging) {
            listenerChanging = true;
            if (style != null) applyStyle(style);
            if (styleName != null) {
                int idx = styleName.indexOf('|');
                if (idx > 0) styleName = styleName.substring(0, idx);
                Vector nodes = new Vector();
                nodes.add(topNode);
                addNodes(nodes, topNode, styleName);
                if (nodes.size() > 0) {
                    Object nodeArray[] = nodes.toArray();
                    TreePath path = new TreePath(nodeArray);
                    styleTree.setSelectionPath(path);
                    styleTree.scrollPathToVisible(path);
                }
            } else styleTree.setSelectionPath(null);
            listenerChanging = false;
        }
    }

    private void addNodes(Vector nodes, TreeNode node, String styleName) {
        int pos = styleName.indexOf('/');
        String subStyle = pos > 0 ? styleName.substring(0, pos) : styleName;
        int numChildren = node.getChildCount();
        for (int i = 0; i < numChildren; i++) {
            TreeNode childNode = node.getChildAt(i);
            if (childNode.toString().indexOf(subStyle) >= 0) {
                nodes.add(childNode);
                if ((pos > 0) && (pos < styleName.length())) addNodes(nodes, childNode, styleName.substring(pos + 1));
                break;
            }
        }
    }

    private String getTreePath() {
        String styleName = null;
        if (styleTree.getSelectionPath() != null) {
            Object path[] = styleTree.getSelectionPath().getPath();
            styleName = "";
            for (int i = 1; i < path.length; i++) {
                styleName += getStrippedPath(path[i].toString()) + "/";
            }
            if (styleName.length() > 0) styleName = styleName.substring(0, styleName.length() - 1);
        }
        if (styleName == null) return ""; else if (styleName.trim().compareTo("") != 0) return styleName; else return "";
    }

    private String getTreePath(MouseEvent me) {
        String styleName = null;
        TreePath path = styleTree.getClosestPathForLocation(me.getPoint().x, me.getPoint().y);
        if (path != null) {
            Object paths[] = path.getPath();
            styleName = "";
            for (int i = 1; i < paths.length; i++) {
                styleName += getStrippedPath(paths[i].toString()) + "/";
            }
            if (styleName.length() > 0) styleName = styleName.substring(0, styleName.length() - 1);
        }
        return styleName;
    }

    private String getStrippedPath(String path) {
        int delim = path.indexOf("|");
        if (delim < 0) return path; else return path.substring(0, delim);
    }

    private String getStyleAttribs(Style style) {
        Color bgColor = style.getStyleAsColor(Style.COLOR_BACK);
        Color fgColor = style.getStyleAsColor(Style.COLOR_FORE);
        int fontSize = style.getStyleAsInt(Style.FONT_SIZE);
        if (bgColor == null) bgColor = Color.white;
        if (fgColor == null) fgColor = Color.black;
        if (fontSize == 0) fontSize = 10;
        return "|" + bgColor.getRGB() + "|" + fgColor.getRGB() + "|" + fontSize;
    }

    /**
   * Subclass the tree so as to add tooltip support
   */
    class JStyleTree extends JTree {

        Font toolTipFont;

        Style toolTipStyle;

        public JStyleTree(DefaultMutableTreeNode model) {
            super(model);
            ToolTipManager.sharedInstance().registerComponent(this);
        }

        public JToolTip createToolTip() {
            StyleToolTip tt = new StyleToolTip();
            tt.setComponent(this);
            if (toolTipFont != null) tt.setFont(toolTipFont);
            if (toolTipStyle != null) {
                tt.setBackground(toolTipStyle.getStyleAsColor(Style.COLOR_BACK));
                tt.setForeground(toolTipStyle.getStyleAsColor(Style.COLOR_FORE));
            }
            return tt;
        }

        public String getToolTipText(MouseEvent me) {
            String styleName = getTreePath(me);
            if (styleName.length() > 0) {
                toolTipStyle = styleManager.getStyle(styleName);
                String fontDescription = styleName + " [" + toolTipStyle.getStyleAsString(Style.FONT_FACE);
                int fontSize = toolTipStyle.getStyleAsInt(Style.FONT_SIZE);
                fontDescription += ": " + fontSize + "pt";
                fontSize = Math.max(fontSize, 10);
                int fontStyle = 0;
                if (toolTipStyle.getStyleAsInt(Style.FONT_ITALIC) == 1) {
                    fontDescription += ", italic";
                    fontStyle |= Font.ITALIC;
                }
                if (toolTipStyle.getStyleAsInt(Style.FONT_WEIGHT) == 1) {
                    fontDescription += ", bold";
                    fontStyle |= Font.BOLD;
                }
                fontDescription += "]";
                toolTipFont = new Font(toolTipStyle.getStyleAsString(Style.FONT_FACE), fontStyle, fontSize);
                return fontDescription;
            } else return "";
        }
    }

    /**
   * Subclass the tooltip to allow the colours to be changed
   */
    class StyleToolTip extends JToolTip {

        public StyleToolTip() {
        }

        public void paintComponent(Graphics g) {
            g.setColor(getForeground());
            super.paintComponent(g);
        }
    }
}
