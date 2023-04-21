package de.mse.mogwai.utils.erdesigner.plugins;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import de.mse.mogwai.utils.erdesigner.logging.ERDesignerLogger;
import de.mse.mogwai.utils.erdesigner.types.EntityContainer;
import de.mse.mogwai.utils.erdesigner.utils.GenericFileFilter;

/**
 * Base class for all ERDesigner plug ins.
 * 
 * @author Mirko Sertic
 */
public abstract class Plugin {

    private EntityContainer m_container;

    private ERDesignerLogger m_logger;

    /**
	 * Construct a plug in.
	 * 
	 * @param container
	 *            the plugin container.
	 */
    protected Plugin(EntityContainer container) {
        this.m_container = container;
        this.m_logger = container.getLogger();
    }

    /**
	 * Get the current logger instance.
	 * 
	 * @return The logger instance
	 */
    public ERDesignerLogger getLogger() {
        return this.m_logger;
    }

    /**
	 * Ask for a save file name.
	 * 
	 * @param extension
	 *            the desired extension
	 * @param description
	 *            the file description
	 * @return filename or false is canceled
	 */
    public String askForSaveFileName(String extension, String description) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new GenericFileFilter(description, extension));
        if (chooser.showSaveDialog(this.getParentFrame()) == JFileChooser.APPROVE_OPTION) {
            String name = chooser.getSelectedFile().toString();
            if (!name.toUpperCase().endsWith("." + extension)) name += "." + extension;
            return name;
        }
        return null;
    }

    /**
	 * Get a property.
	 * 
	 * @param name
	 *            the property name
	 * @return the property
	 */
    protected Object getProperty(String name) {
        return this.m_container.getProperty(name);
    }

    /**
	 * Set a property.
	 * 
	 * @param name
	 *            the property name
	 * @param value
	 *            the property value
	 */
    protected void setProperty(String name, String value) {
        this.m_container.setProperty(name, value);
    }

    /**
	 * Get a property as string.
	 * 
	 * @param name
	 *            the property name
	 * @return the property value as a string
	 */
    protected String getPropertyAsString(String name) {
        return this.m_container.getPropertyAsString(name);
    }

    /**
	 * Get the property parent frame.
	 * 
	 * @return the frame
	 */
    protected JFrame getParentFrame() {
        return this.m_container.getParentFrame();
    }

    /**
	 * Execute this plugin.
	 */
    public abstract void execute();

    /**
	 * Get the entity container.
	 * 
	 * @return the container
	 */
    public EntityContainer getEntityContainer() {
        return this.m_container;
    }
}
