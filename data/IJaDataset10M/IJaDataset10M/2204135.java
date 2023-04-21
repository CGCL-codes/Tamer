package org.formaria.editor.eclipse.project.pages;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import org.formaria.aria.ComponentFactory;
import org.formaria.aria.Page;
import org.formaria.aria.ProjectManager;
import org.formaria.editor.project.EditorProject;
import org.formaria.editor.project.pages.IPagePanel;
import org.formaria.editor.project.pages.components.EditorComponentAdapter;
import org.formaria.editor.project.pages.components.EditorRegisteredComponentFactory;
import org.formaria.editor.project.pages.events.ToolbarSelectionListener;
import org.formaria.editor.project.registry.ComponentRegistryEditor;
import org.formaria.editor.project.workspace.ComponentToolBar;

/**
 * The component palettes
 * <p> Copyright (c) Formaria Ltd., 2008</p>
 * <p> $Revision: 1.18 $</p>
 * <p> License: see License.txt</p>
 */
public class SwingComponentPalette extends JPanel implements ActionListener {

    private JPanel widgetPanel;

    private JToolBar otherWidgetsPanel, swingxWidgetsPanel;

    private CardLayout panelManager;

    private JButton editRegistryButton;

    private JToggleButton stdButton, otherButton, swingxButton;

    private Insets zeroInsets;

    private ToolbarSelectionListener toolbarListener;

    private ImageIcon defaultIcon;

    private String imageRoot = "/";

    private String os;

    private Hashtable<String, EditorComponentAdapter> nonStandardComponents;

    /**
     * A palette for choosing components/widgets to be added to a page. This palette
     * contains several sets of widgets, each on a separate page. The pages are
     * organized with a card layout so that only one set is visible at a time. A
     * toggle button allows selection of the sets.
     * <p>Copyright: Copyright Xoetrope Ltd. (c) 2001-2004</p>
     *
     * $Revision: 1.18 $
     */
    public SwingComponentPalette() {
        ComponentToolBar toolbar = new ComponentToolBar();
        os = System.getProperty("os.name").toLowerCase();
        zeroInsets = new Insets(0, 0, 0, 0);
        nonStandardComponents = new Hashtable<String, EditorComponentAdapter>();
        ButtonGroup bg = new ButtonGroup();
        stdButton = new JToggleButton("Aria");
        stdButton.setActionCommand("Aria");
        stdButton.addActionListener(this);
        stdButton.setToolTipText("Standard Aria components");
        stdButton.setSelected(true);
        bg.add(stdButton);
        swingxButton = new JToggleButton("SwingX");
        swingxButton.setActionCommand("SwingX");
        swingxButton.addActionListener(this);
        swingxButton.setToolTipText("SwingX components");
        swingxButton.setSelected(false);
        bg.add(swingxButton);
        otherButton = new JToggleButton("Other");
        otherButton.setActionCommand("Other");
        otherButton.addActionListener(this);
        otherButton.setToolTipText("Non standard and registered components");
        otherButton.setSelected(false);
        bg.add(otherButton);
        editRegistryButton = new JButton("*");
        editRegistryButton.setSelected(false);
        editRegistryButton.setActionCommand("reg");
        editRegistryButton.addActionListener(this);
        editRegistryButton.setToolTipText("Edit the component registrations");
        editRegistryButton.setSelected(false);
        toolbar.add(stdButton);
        toolbar.add(otherButton);
        toolbar.add(editRegistryButton);
        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        widgetPanel = new JPanel();
        panelManager = new CardLayout();
        widgetPanel.setLayout(panelManager);
        try {
            defaultIcon = new ImageIcon(SwingComponentPalette.class.getResource("icons/deficon.gif"));
        } catch (Exception e) {
        }
        EditorProject editorProject = (EditorProject) ProjectManager.getCurrentProject();
        boolean isSwingProject = editorProject != null ? editorProject.isSwingClient() : false;
        widgetPanel.add(addStdComponents(isSwingProject), "Aria");
        widgetPanel.add(addOtherComponents(isSwingProject, "Other"), "Other");
        if (isSwingProject) widgetPanel.add(addOtherComponents(isSwingProject, "SwingX"), "SwingX");
        add(widgetPanel, BorderLayout.CENTER);
    }

    private JComponent addStdComponents(boolean isSwingProject) {
        JToolBar standardPanel = new JToolBar();
        standardPanel.setFloatable(false);
        standardPanel.setLayout(new AutoGridLayout());
        ButtonGroup bg = new ButtonGroup();
        addToggleTool(bg, standardPanel, "mouseicon.gif", null);
        addToggleTool(bg, standardPanel, "buttonicon.gif", Page.BUTTON);
        addToggleTool(bg, standardPanel, "checkicon.gif", Page.CHECK);
        addToggleTool(bg, standardPanel, "comboicon.gif", Page.COMBO);
        addToggleTool(bg, standardPanel, "editicon.gif", Page.EDIT);
        addToggleTool(bg, standardPanel, "hotspoticon.gif", Page.HOTSPOTIMAGE);
        addToggleTool(bg, standardPanel, "imageicon.gif", Page.IMAGE);
        addToggleTool(bg, standardPanel, "imagemapicon.gif", "ImageMap");
        addToggleTool(bg, standardPanel, "labelicon.gif", Page.LABEL);
        addToggleTool(bg, standardPanel, "listicon.gif", Page.LIST);
        addToggleTool(bg, standardPanel, "metaicon.gif", Page.METACONTENT);
        addToggleTool(bg, standardPanel, "panelicon.gif", Page.PANEL);
        addToggleTool(bg, standardPanel, "passwordicon.gif", "Password");
        addToggleTool(bg, standardPanel, "radioicon.gif", Page.RADIO);
        addToggleTool(bg, standardPanel, "scrollpaneicon.gif", Page.SCROLLPANE);
        addToggleTool(bg, standardPanel, "smetaicon.gif", Page.SCROLLABLEMETACONTENT);
        addToggleTool(bg, standardPanel, "tabicon.gif", Page.TABPANEL);
        addToggleTool(bg, standardPanel, "tableicon.gif", Page.TABLE);
        addToggleTool(bg, standardPanel, "textareaicon.gif", Page.TEXTAREA);
        if (isSwingProject) {
            addToggleTool(bg, standardPanel, "treeicon.gif", "Tree");
        }
        JScrollPane scrollPane = new JScrollPane(standardPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        return scrollPane;
    }

    /**
     * Add components registered via extra component factories. 
     * @param isSwingProject
     * @param library 
     * @return
     */
    private JComponent addOtherComponents(boolean isSwingProject, String library) {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);
        tb.setLayout(new AutoGridLayout());
        if (library.equals("SwingX")) swingxWidgetsPanel = tb; else otherWidgetsPanel = tb;
        updateOtherPanel(tb, library);
        JScrollPane scrollPane = new JScrollPane(tb);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        return scrollPane;
    }

    /**
     * Refresh the list of components registered via extra component factories.
     */
    public void refresh() {
        nonStandardComponents.clear();
        updateOtherPanel(swingxWidgetsPanel, "SwingX");
        updateOtherPanel(otherWidgetsPanel, "Other");
    }

    /**
     * Add components registered via extra component factories.
     * @param widgetPanel
     */
    private void updateOtherPanel(JToolBar otherPanel, String library) {
        if (otherPanel == null) return;
        otherPanel.removeAll();
        ButtonGroup bg = new ButtonGroup();
        Hashtable componentFactories = ComponentFactory.getFactories();
        Enumeration enumeration = componentFactories.keys();
        while (enumeration.hasMoreElements()) {
            Object obj = componentFactories.get(enumeration.nextElement());
            if (obj instanceof EditorRegisteredComponentFactory) {
                EditorRegisteredComponentFactory ercf = ((EditorRegisteredComponentFactory) obj);
                ercf.checkRegistration();
                ArrayList<EditorComponentAdapter> components = new ArrayList<EditorComponentAdapter>(ercf.getComponents().values());
                Collections.sort(components);
                Enumeration myEnum = ercf.getComponents().elements();
                int numComponents = components.size();
                for (int i = 0; i < numComponents; i++) {
                    EditorComponentAdapter eca = components.get(i);
                    if ((library.equals("SwingX") && eca.getClassName().contains("org.jdesktop.swingx")) || (library.equals("Other") && eca.getClassName().contains("aria") && !eca.getClassName().contains("org.jdesktop.swingx"))) {
                        if (eca.isIncluded()) addToggleTool(bg, otherPanel, eca);
                    }
                }
            }
        }
    }

    private void addButton(JPanel parent, ButtonGroup bg, String text, boolean selected) {
        JToggleButton stdButton = new JToggleButton(text);
        stdButton.setToolTipText("My selection button tooltip text");
        stdButton.setSelected(selected);
        bg.add(stdButton);
        parent.add(stdButton);
    }

    /**
     * Add an toggle button to the toolbar.
     * @param bg the buttton group to which the new button belongs
     * @param targetPanel the tab panel to which the new tool is added
     * @param imageName the name of the image resource
     * @param compId the ID of the component type
     * @param tooltipText the text of the tooltip
     * @return the new image component
     */
    public JToggleButton addToggleTool(ButtonGroup bg, JToolBar targetPanel, String imageName, String tooltipText) {
        JToggleButton tool = new JToggleButton();
        try {
            if (imageName.length() == 0) tool.setIcon(defaultIcon); else {
                if (imageName.indexOf('/') < 0) imageName = imageRoot + imageName;
                ImageIcon icon;
                if (imageName.indexOf("file:") == 0) {
                    URL url = new URL("jar:" + imageName);
                    java.awt.Image image = Toolkit.getDefaultToolkit().getImage(url);
                    icon = new ImageIcon(image);
                } else {
                    URL url = ComponentPalette.class.getResource(imageName);
                    if (url != null) {
                        icon = new ImageIcon(url);
                        tool.setIcon(icon);
                    }
                }
            }
            tool.setSize(new Dimension(16, 16));
        } catch (Exception e) {
            if (defaultIcon != null) tool.setIcon(defaultIcon);
        }
        tool.setText(tooltipText);
        tool.setHorizontalAlignment(SwingConstants.LEFT);
        targetPanel.add(tool);
        if (!os.contains("mac")) tool.setBorder(null);
        tool.addActionListener(this);
        tool.setName(tooltipText);
        tool.setMargin(zeroInsets);
        tool.setToolTipText(tooltipText);
        bg.add(tool);
        return tool;
    }

    /**
     * Add an toggle button to the toolbar.
     * @param bg the buttton group to which the new button belongs
     * @param targetPanel the tab panel to which the new tool is added
     * @param imageName the name of the image resource
     * @param compId the ID of the component type
     * @param tooltipText the text of the tooltip
     * @return the new image component
     */
    public JToggleButton addToggleTool(ButtonGroup bg, JToolBar targetPanel, EditorComponentAdapter eca) {
        String imageName = eca.getIcon(true);
        String tooltipText = eca.getType();
        JToggleButton tool = new JToggleButton();
        try {
            if (imageName.length() == 0) tool.setIcon(defaultIcon); else if (imageName.equals("bean") || imageName.equals("bean")) {
                BeanInfo bi = Introspector.getBeanInfo(eca.getComponentClass());
                Image img = bi.getIcon(BeanInfo.ICON_COLOR_16x16);
                ImageIcon icon = new ImageIcon(img);
                tool.setIcon(icon);
            } else {
                if (imageName.indexOf('/') < 0) imageName = imageRoot + imageName;
                ImageIcon icon;
                if (imageName.indexOf("file:") == 0) {
                    URL url = new URL("jar:" + imageName);
                    java.awt.Image image = Toolkit.getDefaultToolkit().getImage(url);
                    icon = new ImageIcon(image);
                    tool.setIcon(icon);
                } else {
                    URL url = ComponentPalette.class.getResource(imageName);
                    if (url != null) {
                        icon = new ImageIcon(url);
                        tool.setIcon(icon);
                    }
                }
            }
            tool.setSize(new Dimension(16, 16));
        } catch (Exception e) {
            if (defaultIcon != null) tool.setIcon(defaultIcon);
        }
        tool.setText(tooltipText);
        tool.setHorizontalAlignment(SwingConstants.LEFT);
        targetPanel.add(tool);
        if (!os.contains("mac")) tool.setBorder(null);
        tool.addActionListener(this);
        tool.setName(tooltipText);
        tool.setMargin(zeroInsets);
        tool.setToolTipText(tooltipText);
        nonStandardComponents.put(tooltipText, eca);
        bg.add(tool);
        return tool;
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == editRegistryButton) showComponentRegistrationEditor((e.getModifiers() & InputEvent.CTRL_MASK) != 0); else {
            String page = null;
            if (o == stdButton) page = "Aria"; else if (o == otherButton) page = "Other"; else if (o == swingxButton) page = "SwingX";
            if (page != null) panelManager.show(widgetPanel, page); else {
                String id = ((JToggleButton) o).getToolTipText();
                EditorComponentAdapter eca = null;
                if (id != null) eca = nonStandardComponents.get(id);
                toolbarListener.toolSelected(id, eca);
            }
        }
    }

    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, y, w, h);
        doLayout();
        int numComponents = widgetPanel.getComponentCount();
        for (int i = 0; i < numComponents; i++) {
            ((JComponent) ((JScrollPane) widgetPanel.getComponent(i)).getViewport().getComponent(0)).revalidate();
        }
    }

    static class AutoGridLayout implements LayoutManager {

        private int h_margin_left = 2;

        private int h_margin_right = 1;

        private int v_margin_top = 2;

        private int v_margin_bottom = 3;

        private int h_gap = 1;

        private int v_gap = 1;

        public void addLayoutComponent(String name, Component comp) {
        }

        public void removeLayoutComponent(Component comp) {
        }

        public Dimension preferredLayoutSize(Container parent) {
            Container owner = parent.getParent();
            synchronized (owner.getTreeLock()) {
                int containerWidth = owner.getWidth();
                int count = parent.getComponentCount();
                if (containerWidth <= 0 || count == 0) {
                    int cumulatedWidth = 0;
                    int height = 0;
                    for (int i = 0; i < count; i++) {
                        Dimension size = parent.getComponent(i).getPreferredSize();
                        cumulatedWidth += size.width;
                        if (i + 1 < count) cumulatedWidth += h_gap;
                        if (size.height > height) height = size.height;
                    }
                    cumulatedWidth += h_margin_left + h_margin_right;
                    height += v_margin_top + v_margin_bottom;
                    return new Dimension(cumulatedWidth, height);
                }
                int columnWidth = 0;
                int rowHeight = 0;
                for (int i = 0; i < count; i++) {
                    Dimension size = parent.getComponent(i).getPreferredSize();
                    if (size.width > columnWidth) columnWidth = size.width;
                    if (size.height > rowHeight) rowHeight = size.height;
                }
                int columnCount = 0;
                int w = h_margin_left + columnWidth + h_margin_right;
                do {
                    columnCount++;
                    w += h_gap + columnWidth;
                } while (w <= containerWidth && columnCount < count);
                int rowCount = count / columnCount + (count % columnCount > 0 ? 1 : 0);
                int prefHeight = v_margin_top + rowCount * rowHeight + (rowCount - 1) * v_gap + v_margin_bottom;
                return new Dimension(containerWidth, prefHeight);
            }
        }

        public Dimension minimumLayoutSize(Container parent) {
            return new Dimension(h_margin_left + h_margin_right, v_margin_top + v_margin_bottom);
        }

        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                int count = parent.getComponentCount();
                if (count == 0) return;
                int columnWidth = 0;
                int rowHeight = 0;
                for (int i = 0; i < count; i++) {
                    Dimension size = parent.getComponent(i).getPreferredSize();
                    if (size.width > columnWidth) columnWidth = size.width;
                    if (size.height > rowHeight) rowHeight = size.height;
                }
                int containerWidth = parent.getWidth();
                int columnCount = 0;
                int w = h_margin_left + columnWidth + h_margin_right;
                do {
                    columnCount++;
                    w += h_gap + columnWidth;
                } while (w <= containerWidth && columnCount < count);
                if (count % columnCount > 0) {
                    int roundedRowCount = count / columnCount;
                    int lastRowEmpty = columnCount - count % columnCount;
                    if (lastRowEmpty > roundedRowCount) columnCount -= lastRowEmpty / (roundedRowCount + 1);
                }
                if (count > columnCount) columnWidth = (containerWidth - h_margin_left - h_margin_right - (columnCount - 1) * h_gap) / columnCount;
                if (columnWidth < 0) columnWidth = 0;
                for (int i = 0, col = 0, row = 0; i < count; i++) {
                    parent.getComponent(i).setBounds(h_margin_left + col * (columnWidth + h_gap), v_margin_top + row * (rowHeight + v_gap), columnWidth, rowHeight);
                    if (++col >= columnCount) {
                        col = 0;
                        row++;
                    }
                }
            }
        }
    }

    /**
     * Called when a component has been selected
     * @param selectedComponents
     */
    public void setSelectedComponents(IPagePanel currentPagePanel, Vector selectedComponents, boolean contentsChanged) {
    }

    public void componentAdded() {
    }

    public void setNewComponent(Component c) {
    }

    public void addToolbarListener(ToolbarSelectionListener l) {
        toolbarListener = l;
    }

    /**
     * Show the component registration editor
     */
    private void showComponentRegistrationEditor(boolean expertMode) {
        String fileName = EditorRegisteredComponentFactory.getUserRegistrationFileName();
        ComponentRegistryEditor csd = new ComponentRegistryEditor("Component Registration Editor", fileName, expertMode);
        csd.setSize(new Dimension(800, 600));
        csd.setLocationRelativeTo(this);
        csd.setModal(true);
        csd.setVisible(true);
        updateOtherPanel(swingxWidgetsPanel, "SwingX");
        updateOtherPanel(otherWidgetsPanel, "Other");
    }
}
