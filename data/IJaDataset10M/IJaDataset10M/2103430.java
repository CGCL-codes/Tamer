package net.ar.guia.plugins;

import net.ar.guia.helpers.*;
import net.ar.guia.managers.adapters.*;
import net.ar.guia.managers.contributors.*;
import net.ar.guia.managers.names.*;
import net.ar.guia.managers.pages.*;
import net.ar.guia.managers.skins.*;
import net.ar.guia.managers.state.*;
import net.ar.guia.managers.templates.*;
import net.ar.guia.managers.windows.*;
import net.ar.guia.own.implementation.*;
import net.ar.guia.own.interfaces.*;
import net.ar.guia.render.*;
import net.ar.guia.ui.*;
import net.ar.webonswing.own.adapters.*;

public class DefaultGuiaPlugin implements GuiaPlugin {

    protected String windowManagerClassName;

    protected String hierarchyWrapperClassName;

    protected String componentNameManagerClassName;

    protected transient SkinManager skinManager;

    protected transient PageManager pageManager;

    protected transient TemplateManager templateManager;

    protected transient ContributorManager contributorManager;

    protected transient WindowTreeStateManager windowTreeStateManager;

    protected transient AdapterManager adapterManager;

    public DefaultGuiaPlugin() {
        init();
    }

    protected Class getTemplateManagerClass() {
        return HtmlTemplateManager.class;
    }

    protected void init() {
        GuiaHelper.getXStream().alias("template-manager", getTemplateManagerClass());
        skinManager = new SimpleSkinManager();
        windowManagerClassName = DefaultWindowManager.class.getName();
        hierarchyWrapperClassName = DefaultHierarchyWrapper.class.getName();
        componentNameManagerClassName = CharacterComponentNameManager.class.getName();
        pageManager = (PageManager) GuiaHelper.restoreObjectFromXml(getPageManagerConfigXmlFile());
        templateManager = (TemplateManager) GuiaHelper.restoreObjectFromXml(getTemplateManagerConfigXmlFile());
        contributorManager = (ContributorManager) GuiaHelper.restoreObjectFromXml(getContributorManagerConfigXmlFile());
        adapterManager = (AdapterManager) GuiaHelper.restoreObjectFromXml(getAdaptersManagerConfigXmlFile());
        windowTreeStateManager = new WindowTreeStateManager();
    }

    public void persistAll() {
        GuiaHelper.persistObjectToXml(getTemplateManager(), getTemplateManagerConfigXmlFile());
        GuiaHelper.persistObjectToXml(getPageManager(), getPageManagerConfigXmlFile());
        GuiaHelper.persistObjectToXml(getContributorManager(), getContributorManagerConfigXmlFile());
        GuiaHelper.serializeObjectToFile(getWindowTreeStateManager(), getWindowTreeStateManagerConfigSerializedFile());
    }

    public WindowTreeStateManager getWindowTreeStateManager() {
        return windowTreeStateManager;
    }

    public PageManager getPageManager() {
        return pageManager;
    }

    public TemplateManager getTemplateManager() {
        return templateManager;
    }

    public ComponentNameManager getComponentNameManager() {
        return (ComponentNameManager) GuiaHelper.createClassInstance(componentNameManagerClassName);
    }

    public ContributorManager getContributorManager() {
        return contributorManager;
    }

    public void setContributorManager(ContributorManager unContributorManager) {
        contributorManager = unContributorManager;
    }

    public HierarchyWrapper getHierarchyWrapper() {
        return (HierarchyWrapper) GuiaHelper.createClassInstance(hierarchyWrapperClassName);
    }

    public WindowManager getWindowManager() {
        return (WindowManager) GuiaHelper.createClassInstance(windowManagerClassName);
    }

    public SkinManager getSkinManager() {
        return skinManager;
    }

    public RenderingContributionContainer getRenderingContributionContainer() {
        return new DefaultRenderingContributionContainer(getComponentRenderer());
    }

    public ComponentRenderer getComponentRenderer() {
        return new DefaultComponentRenderer();
    }

    public VisualComponent createNewComponentInstance(Class aClass) {
        Class componentAdapter = adapterManager.getComponentAdapterClass(aClass);
        VisualComponent instance = (VisualComponent) GuiaHelper.createClassInstance(componentAdapter.getName());
        if (instance instanceof ContainerAdapter) {
            ContainerAdapter adapter = (ContainerAdapter) instance;
            adapter.setWrappedComponent(adapter.getNewComponentInstance());
        }
        return instance;
    }

    public AdapterManager getAdapterManager() {
        return adapterManager;
    }

    public String getAdaptersManagerConfigXmlFile() {
        return "/net/ar/guia/config/guia.adapters-manager.config.xml";
    }

    public String getPageManagerConfigXmlFile() {
        return "/net/ar/guia/config/page-manager.config.xml";
    }

    public String getTemplateManagerConfigXmlFile() {
        return "/net/ar/guia/config/template-manager.config.xml";
    }

    public String getContributorManagerConfigXmlFile() {
        return "/net/ar/guia/config/contributor-manager.config.xml";
    }

    public String getWindowTreeStateManagerConfigSerializedFile() {
        return "/net/ar/guia/config/window-tree-state-manager.config.serialized";
    }
}
