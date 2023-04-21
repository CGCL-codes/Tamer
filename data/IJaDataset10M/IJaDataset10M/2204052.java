package org.proclos.etlcore.component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.proclos.etlcore.config.IConfigurator;
import org.proclos.etlcore.project.ConfigManager;

public abstract class Component extends Locateable implements IComponent {

    private static final Log log = LogFactory.getLog(Component.class);

    private IConfigurator configurator;

    private ComponentState state = new ComponentState();

    private HashMap<String, IManager> managers = new HashMap<String, IManager>();

    private boolean isDirty = false;

    public void setConfigurator(IConfigurator configurator) {
        this.configurator = configurator;
    }

    public IConfigurator getConfigurator() {
        return configurator;
    }

    public Properties getParameter() {
        return getConfigurator().getParameter();
    }

    protected void addManager(Manager manager, IManager.LookupModes mode) {
        manager.setLocator(getLocator().clone().add(manager.getName()));
        manager.setParameter(ConfigManager.getInstance().getParameter(getLocator().getRootLocator().add(manager.getName())));
        manager.setParameter(getParameter());
        manager.setParameter(ConfigManager.getInstance().getParameter(getLocator().clone().add(manager.getName())));
        manager.setContext(getContext());
        manager.setLookupMode(mode);
        managers.put(manager.getName(), manager);
    }

    protected void addManager(Manager manager) {
        addManager(manager, IManager.LookupModes.Name);
    }

    protected void clearManagers() {
        managers.clear();
    }

    public IManager getManager(String type) {
        if (type == null) return null;
        return managers.get(type);
    }

    public IManager[] getManagers() {
        return managers.values().toArray(new IManager[managers.values().size()]);
    }

    protected void getOutline(Collection<IManager> managers, Element root) {
        for (IManager manager : managers) {
            Element m = new Element(manager.getName());
            root.addContent(m);
            IComponent[] components = manager.getAll();
            for (IComponent component : components) {
                Element c = new Element(component.getName());
                m.addContent(c);
            }
        }
    }

    public String getOutline() {
        StringWriter writer = new StringWriter();
        try {
            Document document = new Document();
            Element root = new Element(getName());
            document.setRootElement(root);
            getOutline(managers.values(), root);
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(document, writer);
        } catch (IOException e) {
            log.error("Failed to get Component outline: " + e.getMessage());
            log.debug(e);
        }
        return writer.toString();
    }

    public ArrayList<IComponent> getDependencies(ArrayList<IComponent> list) {
        IManager[] managers = getManagers();
        for (IManager manager : managers) {
            IComponent[] components = manager.getAll();
            for (IComponent component : components) {
                if (!list.contains(component)) {
                    log.debug("Resolving Dependancy: " + this.getName() + ":" + manager.getName() + ":" + component.getName() + " of type " + component.getClass().getName());
                    list.add(component);
                    list = component.getDependencies(list);
                }
            }
        }
        return list;
    }

    public void setDirty() {
        isDirty = true;
        getState().setDirty(getLocator());
    }

    public boolean isDirty(boolean checkDependencies) {
        if (!isDirty) {
            if (checkDependencies) {
                ArrayList<IComponent> dependencies = getDependencies(new ArrayList<IComponent>());
                for (IComponent dependency : dependencies) {
                    if (dependency.isDirty(false)) setDirty();
                }
            }
        }
        return isDirty;
    }

    public synchronized void setState(ComponentState state) {
        this.state = state;
    }

    public ComponentState getState() {
        return state;
    }

    public void init() throws InitializationException {
        try {
            clearManagers();
            getConfigurator().configure();
            setName(getConfigurator().getName());
            setContext(getConfigurator().getContext());
            setLocator(getConfigurator().getLocator().clone().add(getName()));
        } catch (Exception e) {
            throw new InitializationException(e);
        }
    }
}
