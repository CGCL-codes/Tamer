package org.tagbox.engine;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.tagbox.xml.NodeFinder;
import org.tagbox.config.Configurable;
import org.tagbox.config.ConfigurationRegistry;
import org.tagbox.config.ConfigurationException;
import org.tagbox.config.PropertyInfo;
import org.tagbox.engine.repository.Repository;
import org.tagbox.engine.repository.FileRepository;
import org.tagbox.util.Log;
import org.tagbox.util.ResourceManager;

/**
 * a TagBox Component is responsible for constructing a Configuration
 * and to do component-specific initialisation.
 */
public class Component implements Configurable {

    private Context context;

    private Configuration config;

    private String name;

    private Repository repository;

    private AttachmentFactory attachmentFactory;

    public void init(Context context, Node cfg) throws TagBoxException {
        this.context = context;
        NodeFinder finder = new NodeFinder();
        name = finder.getElementValue(cfg, "name");
        if (name == null || name.equals("")) throw new TagBoxConfigurationException("missing or empty component element: name");
        repository = new FileRepository();
        repository.init(this, cfg);
        attachmentFactory = createAttachmentFactory(cfg);
        config = new TagBoxConfiguration(cfg);
        config.init(this);
    }

    protected AttachmentFactory createAttachmentFactory(Node cfg) throws TagBoxConfigurationException {
        NodeFinder finder = new NodeFinder();
        cfg = finder.getElement(cfg, "attachment-factory");
        AttachmentFactory attachmentFactory;
        if (cfg == null) {
            Log.warning("no attachment-factory configured");
            return null;
        }
        String className = finder.getElementValue(cfg, "class-name");
        if (className == null || className.equals("")) {
            attachmentFactory = new AttachmentFactory();
        } else {
            try {
                attachmentFactory = (AttachmentFactory) ResourceManager.getClassForName(className).newInstance();
            } catch (Exception e) {
                throw new TagBoxConfigurationException("instantiating attachment factory", e);
            }
        }
        attachmentFactory.init(this, cfg);
        return attachmentFactory;
    }

    public String getName() {
        return name;
    }

    public Configuration getConfiguration() {
        return config;
    }

    public Context getContext() {
        return context;
    }

    public Repository getRepository() {
        return repository;
    }

    public AttachmentFactory getAttachmentFactory() {
        return attachmentFactory;
    }

    public void register(ConfigurationRegistry cr) throws ConfigurationException {
        cr.register(new PropertyInfo("name", "the name of this component", String.class, "getName"));
        cr.register(context, "context");
        cr.register(repository, "repository");
        cr.register(config, "configuration");
    }
}
