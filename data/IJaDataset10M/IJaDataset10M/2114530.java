package org.zerokode.designer.ui;

import org.apache.commons.lang.ArrayUtils;
import org.zerokode.designer.config.Configurator;
import org.zerokode.designer.model.ComponentFactory;
import org.zkoss.idom.Element;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Image;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

/**
 * Class that utilizes the Widget View of the
 * designer, where the user selects widgets
 * and places them on the canvas.
 * @author chris.spiliotopoulos
 *
 */
public class DesignerToolkit extends DesignerGroupbox {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2144390995947399856L;

    /**
	 * The Tabbox container 
	 */
    protected Tabbox _tabBox = null;

    /**
	 * The Configurator instance used for loading
	 * configuration preferences 
	 */
    private static Configurator _config = null;

    /**
	 * The total number of component groups 
	 * defined in the configuration file 'toolkit.xml' 
	 */
    private int _nGroupNum = 0;

    /**
	 * @param designer
	 */
    DesignerToolkit(Designer designer) {
        super(designer);
    }

    /**
	 * Returns the current Configurator object that holds 
	 * components information defined in the 'toolkit.xml' 
	 * configuration file
	 * @return The active components' Configurator object
	 */
    public static Configurator getComponentsConfigurator() {
        return _config;
    }

    public void onCreate() {
        setId("designerToolkit");
        setWidth("400px");
        setMold("3d");
        setTooltiptext("Drag-and-drop widgets onto the model treeview in order to be added to the canvas.");
        Caption caption = new Caption();
        caption.setLabel("Component Toolkit");
        appendChild(caption);
        try {
            _config = new Configurator(getDesigner().getWebApp().getRealPath("/config/toolkit/toolkit.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        createTabs();
    }

    /**
	 * Creates the Accordion style Tabs container 
	 */
    protected void createTabs() {
        if (_config == null) return;
        try {
            _tabBox = new Tabbox();
            _tabBox.setWidth("100%");
            appendChild(_tabBox);
            Element[] arrGroups = _config.getElements("group", null);
            if (ArrayUtils.isEmpty(arrGroups)) return;
            _nGroupNum = arrGroups.length;
            createComponentGroups(arrGroups);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    /**
	 * Creates the individual component groups (tabs)
	 */
    protected void createComponentGroups(Element[] arrGroups) {
        if (ArrayUtils.isEmpty(arrGroups)) return;
        Tabs tabs = new Tabs();
        _tabBox.appendChild(tabs);
        Tabpanels panels = new Tabpanels();
        panels.setWidth("90%");
        _tabBox.appendChild(panels);
        for (int i = 0; i < arrGroups.length; i++) {
            Element group = arrGroups[i];
            if (_nGroupNum > 1) {
                Tab tabGroup = new Tab(group.getAttribute("name"));
                tabs.appendChild(tabGroup);
                Tabpanel panelGroup = new Tabpanel();
                panels.appendChild(panelGroup);
                createComponentTabbox(group, panelGroup);
            } else {
                createComponentTabbox(group, null);
            }
        }
    }

    /**
	 * Creates a components tabbox within the 
	 * group for the specified element. 
	 * @param group
	 * @param panel
	 */
    protected void createComponentTabbox(Element domGroup, Tabpanel tabpanel) {
        if ((_config == null) || (domGroup == null)) return;
        Element[] arrTabs = _config.getElements("tab", domGroup);
        if (ArrayUtils.isEmpty(arrTabs)) return;
        Tabbox box = null;
        Tabs tabs = null;
        Tabpanels panels = null;
        if (_nGroupNum > 1) {
            box = new Tabbox();
            tabpanel.appendChild(box);
            tabs = new Tabs();
            box.appendChild(tabs);
            panels = new Tabpanels();
            box.appendChild(panels);
        }
        for (int i = 0; i < arrTabs.length; i++) {
            Element elementTab = arrTabs[i];
            Tab tab = new Tab(elementTab.getAttribute("name"));
            if (_nGroupNum > 1) tabs.appendChild(tab); else _tabBox.getTabs().appendChild(tab);
            Tabpanel panel = new Tabpanel();
            if (_nGroupNum > 1) panels.appendChild(panel); else _tabBox.getTabpanels().appendChild(panel);
            addComponentsToTab(elementTab, panel);
        }
        if (_nGroupNum > 1) {
            _tabBox.setMold("accordion");
        }
    }

    /**
	 * Retrieves the predefined visual components
	 * declared within the iDOM and attaches them 
	 * onto the specified tab panel 
	 * @param elementTab The iDOM description of the
	 * tab element
	 * @param tabpanel The panel where the components 
	 * will be attached to
	 */
    private void addComponentsToTab(Element domTab, Tabpanel tabpanel) {
        if ((_config == null) || (domTab == null) || (tabpanel == null)) return;
        Element[] arrComponents = _config.getElements("component", domTab);
        if (ArrayUtils.isEmpty(arrComponents)) return;
        for (int i = 0; i < arrComponents.length; i++) {
            try {
                Element domComponent = arrComponents[i];
                Element domClass = _config.getElement("class", domComponent);
                Element domImage = _config.getElement("image32", domComponent);
                Element domTooltip = _config.getElement("tooltip", domComponent);
                if (domClass == null) continue;
                addComponentImage(tabpanel, domClass.getText(), domImage.getText(), domTooltip.getText());
            } catch (Exception e) {
                continue;
            }
        }
    }

    /**
	 * Adds a component's image to the toolkit at
	 * the selected tab panel.
	 * @param panel
	 * @param sId
	 * @param sImgSrc
	 * @param sTooltip
	 */
    protected void addComponentImage(Tabpanel panel, String sComponentClass, String sImageSrc, String sTooltip) {
        try {
            Image img = ComponentFactory.createImage(sComponentClass, sImageSrc);
            if (img == null) {
                img = ComponentFactory.createImage(getClass().getName(), "images/designer/components/unknown32.png");
            }
            if (img == null) return;
            img.setId(sComponentClass);
            img.setDraggable("toolkitComponent");
            img.setTooltiptext(sTooltip);
            panel.appendChild(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
