package simple.template.velocity;

import org.apache.velocity.app.VelocityEngine;
import simple.template.layout.ViewerFactory;
import simple.template.layout.LayoutFactory;
import simple.template.layout.Layout;
import simple.http.serve.Context;
import simple.template.Document;

/**
 * The <code>VelocityLayout</code> object provides an implementation
 * of the <code>Layout</code> interface for <cite>Velocity</cite>.
 * This acts a a convinience object that avoids having to deal
 * with the <code>LayoutFactory</code>. Internally this delegates
 * to an instance retrieved from the factory.
 * 
 * @author Niall Gallagher
 */
final class VelocityLayout implements Layout {

    /**
    * The <code>Layout</code> used internally by this instance.
    */
    private Layout layout;

    /**
    * Constructor for the <code>VelocityLayout</code> object. This
    * uses the provided <code>VelocityEngine</code> to interface
    * with the <cite>Velocity</code> templating system and each of
    * the documents produced will be rendered by that system.
    *
    * @param engine interfaces with the templating system used
    * @param context this is used to provide the configuration
    */
    public VelocityLayout(VelocityEngine engine, Context context) {
        this(new VelocityLoader(engine, context), context);
    }

    /**
    * Constructor for the <code>VelocityLayout</code> object. This
    * uses the provided <code>VelocityEngine</code> to interface
    * with the <cite>Velocity</code> templating system and each of
    * the documents produced will be rendered by that system.
    *
    * @param engine interfaces with the templating system used
    * @param context this is used to provide the configuration
    */
    public VelocityLayout(VelocityLoader loader, Context context) {
        this(new VelocityViewerFactory(loader), context);
    }

    /**
    * Constructor for the <code>VelocityLayout</code> object. This
    * uses the provided <code>ViewerFactory</code> to interface
    * with the <cite>Velocity</code> templating system and each of
    * the documents produced will be rendered by that system.
    * 
    * @param factory interfaces with the templating system used
    * @param context this is used to provide the configuration
    */
    public VelocityLayout(ViewerFactory factory, Context context) {
        this.layout = LayoutFactory.getInstance(factory, context);
    }

    /**
    * This method is used to delegate directly to the internal
    * <code>Layout</code>. Because this is used as a convinience
    * object it does not provide any direct functionality, so
    * this essentially acts as a wrapper for the internal object.
    *
    * @param name this is the target template or layout to use
    * @param data this is the data source used by the template
    * @param share should the data model be inherited or shared
    */
    public Document getDocument(String name, Object data, boolean share) throws Exception {
        return layout.getDocument(name, data, share);
    }
}
