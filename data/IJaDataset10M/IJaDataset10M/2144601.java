package gnu.java.beans.decoder;

import java.beans.ExceptionListener;
import org.xml.sax.Attributes;

/** ElementHandler manages a Context instance and interacts with
 * its parent and child handlers.
 *
 * @author Robert Schuster
 */
abstract class AbstractElementHandler implements ElementHandler {

    /** The Context instance of this handler. The instance is available after the startElement()
   * method was called. Otherwise the handler is marked as failed.
   */
    private Context context;

    /** The parent handler. */
    private ElementHandler parent;

    /** Stores whether this handler is marked as failed. */
    private boolean hasFailed;

    /** Stores the character data which is contained in the body of the XML tag. */
    private StringBuffer buffer = new StringBuffer();

    /** Stores whether this ElementHandler can have subelements. The information for this is taken from
   * javabeans.dtd which can be found here:
   * <a href="http://java.sun.com/products/jfc/tsc/articles/persistence3/">Java Persistence Article</a>
   */
    private boolean allowsSubelements;

    /** Creates a new ElementHandler with the given ElementHandler instance
   * as parent.
   *
   * @param parentHandler The parent handler.
   */
    protected AbstractElementHandler(ElementHandler parentHandler, boolean allowsSubs) {
        parent = parentHandler;
        allowsSubelements = allowsSubs;
    }

    /** Evaluates the attributes and creates a Context instance.
   * If the creation of the Context instance fails the ElementHandler
   * is marked as failed which may affect the parent handler other.
   *
   * @param attributes Attributes of the XML tag.
   */
    public final void start(Attributes attributes, ExceptionListener exceptionListener) {
        try {
            context = startElement(attributes, exceptionListener);
        } catch (AssemblyException pe) {
            Throwable t = pe.getCause();
            if (t instanceof Exception) exceptionListener.exceptionThrown((Exception) t); else throw new InternalError("Unexpected Throwable type in AssemblerException. Please file a bug report.");
            notifyContextFailed();
            return;
        }
    }

    /** Analyses the content of the Attributes instance and creates a Context
   * object accordingly.
   * An AssemblerException is thrown when the Context instance could not
   * be created.
   *
   * @param attributes Attributes of the XML tag.
   * @return A Context instance.
   * @throws AssemblerException when Context instance could not be created.
   */
    protected abstract Context startElement(Attributes attributes, ExceptionListener exceptionListener) throws AssemblyException;

    /** Post-processes the Context.
   */
    public final void end(ExceptionListener exceptionListener) {
        if (!hasFailed) {
            try {
                endElement(buffer.toString());
                if (context.isStatement()) {
                    parent.notifyStatement(exceptionListener);
                    if (parent.hasFailed()) return;
                }
                putObject(context.getId(), context.endContext(parent.getContext()));
                if (!context.isStatement()) parent.getContext().addParameterObject(context.getResult());
            } catch (AssemblyException pe) {
                Throwable t = pe.getCause();
                if (t instanceof Exception) exceptionListener.exceptionThrown((Exception) t); else throw (InternalError) new InternalError("Severe problem while decoding XML data.").initCause(t);
                notifyContextFailed();
            }
        }
    }

    /** Notifies the handler's Context that its child Context will not return
   * a value back. Some Context variants need this information to know when
   * a method or a constructor call can be made.
   *
   * This method is called by a child handler.
   */
    public void notifyStatement(ExceptionListener exceptionListener) {
        try {
            if (context.isStatement()) {
                parent.notifyStatement(exceptionListener);
            }
            context.notifyStatement(parent.getContext());
        } catch (AssemblyException ae) {
            Throwable t = ae.getCause();
            if (t instanceof Exception) exceptionListener.exceptionThrown((Exception) t); else throw (InternalError) new InternalError("Severe problem while decoding XML data.").initCause(t);
            notifyContextFailed();
        }
    }

    /** Marks this and any depending parent handlers as failed. Which means that on their end
   * no result is calculated.
   *
   * When a handler has failed no more handlers are accepted within it.
   */
    public final void notifyContextFailed() {
        hasFailed = true;
        if (parent.getContext().subContextFailed()) parent.notifyContextFailed();
    }

    /** Returns whether this handler has failed.
   *
   * This is used to skip child elements.
   *
   * @return Whether this handler has failed.
   */
    public final boolean hasFailed() {
        return hasFailed;
    }

    /** Processes the character data when the element ends.
   *
   * The default implementation does nothing for convenience.
   *
   * @param characters
   * @throws AssemblerException
   */
    protected void endElement(String characters) throws AssemblyException {
    }

    /** Adds characters from the body of the XML tag to the buffer.
   *
   * @param ch
   * @param start
   * @param length
   * @throws SAXException
   */
    public final void characters(char[] ch, int start, int length) {
        buffer.append(ch, start, length);
    }

    /** Stores an object globally under a unique id. If the id is
   * null the object is not stored.
   *
   * @param objectId
   * @param o
   */
    public void putObject(String objectId, Object o) {
        if (objectId != null) parent.putObject(objectId, o);
    }

    /** Returns a previously stored object. If the id is null the
   * result is null, too.
   *
   * @param objectId
   * @return Returns a previously stored object or null.
   */
    public Object getObject(String objectId) throws AssemblyException {
        return objectId == null ? null : parent.getObject(objectId);
    }

    /** Returns the Class instance as if called Class.forName() but
   * uses a ClassLoader given by the user.
   *
   * @param className
   * @return
   * @throws ClassNotFoundException
   */
    public Class instantiateClass(String className) throws ClassNotFoundException {
        return parent.instantiateClass(className);
    }

    public final boolean isSubelementAllowed(String subElementName) {
        return allowsSubelements && !subElementName.equals("java");
    }

    public final Context getContext() {
        return context;
    }

    public final ElementHandler getParent() {
        return parent;
    }
}
