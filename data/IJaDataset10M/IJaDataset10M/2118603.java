package net.sf.xframe.gui.adapter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.sf.xframe.gui.GuiException;
import net.sf.xframe.gui.GuiFactory;
import net.sf.xframe.gui.context.InstanceContext;
import net.sf.xframe.gui.context.SchemaContext;
import net.sf.xframe.gui.context.XMLException;
import net.sf.xframe.schema.ParticleFactory;
import net.sf.xframe.schema.XSObject;
import net.sf.xframe.schema.XSView;

/**
 * Factory for <code>Schema</code> objects.
 * 
 * @author <a href="mailto:kurt.riede@web.de">Kurt Riede</a>
 *
 */
public abstract class BaseGUIFactory implements GuiFactory {

    /** Bindings */
    private Map bindings = new HashMap();

    /**
     * constructor.
     * 
     * @param guiFactory the concrete GUI factory
     */
    public BaseGUIFactory() throws GuiException {
        setDefaultBindings();
    }

    /**
     * Create a schema context.
     * 
     * @param factory BaseGUIFactory to use withing the new context
     * @param file file to load schema from
     * @return SchemaContext
     * @throws GuiException if context cannot be created
     */
    public SchemaContext createSchemaContext(File file) throws XMLException {
        try {
            return new SchemaContext(this, file);
        } catch (XMLException e) {
            throw new XMLException("Cannot create Schema context for " + file.getName(), e);
        }
    }

    /**
     * Create an instance context.
     * 
     * @param factory BaseGUIFactory to use withing the new context
     * @param file file to load instance from
     * @return SchemaContext
     * @throws GuiException if context cannot be created
     */
    public InstanceContext createInstanceContext(File file) throws XMLException {
        try {
            return new InstanceContext(this, file);
        } catch (XMLException e) {
            throw new XMLException("Cannot create instance context for " + file.getName(), e);
        }
    }

    /**
     * Create a view for an instance context with given schema context.
     * 
     * <p>The schema context is examined from the instance context, so the
     * schema must be defined withing the instance context by a 
     * <code>xsi:schemaLocation</code> attribute or a 
     * <code>xsi:noNamespaceSchemaLocation</code> attribute in the root
     * element.</p>
     * 
     * @param instanceContext the instance context
     * @return XSView for an instance context
     * @throws GuiException if the XSView cannot be created
     */
    public XSView createView(XSObject parent, InstanceContext instanceContext) throws XMLException {
        return (XSView) create(null, null, instanceContext);
    }

    /**
     * Create a new schema for an instance context with given schema context.
     * 
     * <p>If the schema isn't defined in the instance with an
     * <code>xsi:schemaLocation</code> attribute or a 
     * <code>xsi:noNamespaceSchemaLocation</code> attribute, the schema can be
     * proposed to the factory using this method.</p>
     * 
     * @param schemaContext the schema context
     * @param instanceContext the instance context
     * @return XSView
     * @throws GuiException if the Viewcannot be created
     */
    public XSView createView(XSObject parent, SchemaContext schemaContext, InstanceContext instanceContext) throws XMLException {
        XSObject particle = (XSObject) create(parent, schemaContext, instanceContext);
        return (XSView) particle;
    }

    /**
     * Returns the current bindings.
     * 
     * @return List of bindings.
     */
    public Map getBindings() {
        return bindings;
    }

    /**
     * Clears all existing binding information.
     */
    protected void clearBinding() {
        bindings = new HashMap();
    }

    /**
     * Returns the current bindings.
     * 
     * @return List of bindings.
     */
    public String getBinding(String type) {
        return (String) bindings.get(type);
    }

    /**
     * Adds a binding of a type to class name.
     * 
     * @return List of bindings.
     */
    public void setBinding(String type, String typeClassName) {
        bindings.put(type, typeClassName);
    }

    /**
     * Create instances of classes implementing the 
     * <code>GuiBuilder</code> interface.
     * 
     * <p>The parent particle should not be <code>null</code> for other
     * instances than <code>#document</code> which is the root particle.</p>
     * 
     * <p>New GUI builders are created via reflection according to the binding
     * declarations. The GUI builder is associated with an instance of the
     * <code>XSObject</code> interface matching the current Schema context and
     * the particle is assiciated vice versa with the GUI builder.</p>
     * 
     * @param theParent parent particle
     * @param theSchemaContext schema context for the new particle
     * @param theInstanceContext instance context for the new particle
     * @return new XSObject
     * @throws GuiException if the particle cannot be created
     */
    public XSObject create(XSObject theParent, SchemaContext theSchemaContext, InstanceContext theInstanceContext) throws XMLException {
        String localName = theSchemaContext.getNode().getNodeName();
        String particleClassName = getBinding(localName);
        try {
            Class particleClass = Class.forName(particleClassName);
            GuiBuilder guiBuilder = (GuiBuilder) particleClass.newInstance();
            XSObject particle = ParticleFactory.create(theSchemaContext);
            guiBuilder.setParticle(particle);
            particle.setGuiBuilder(guiBuilder);
            System.out.println(particleClassName + " created for " + localName);
            particle.initialise(theParent, (SchemaContext) theSchemaContext, (InstanceContext) theInstanceContext);
            return particle;
        } catch (ClassCastException e) {
            System.out.println("Illegal binding (Skip): " + particleClassName);
            return null;
        } catch (NullPointerException e) {
            System.out.println("Missing binding (Skip): " + theSchemaContext.getNode().getParentNode().getNodeName() + "." + localName);
            return null;
        } catch (ClassNotFoundException e) {
            throw new XMLException("Bound class not found: " + particleClassName, e);
        } catch (InstantiationException e) {
            throw new XMLException("Cannot instantiate bound class: " + particleClassName, e);
        } catch (IllegalAccessException e) {
            throw new XMLException("Illegal access to bound class: " + particleClassName, e);
        }
    }

    /**
     * @see net.sf.xframe.gui.GuiFactory#setDefaultBindings()
     */
    public abstract void setDefaultBindings();
}
