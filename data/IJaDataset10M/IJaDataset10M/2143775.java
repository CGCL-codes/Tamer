package org.apache.batik.script;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.util.Service;
import org.w3c.dom.Document;

/**
 * A class allowing to create/query an {@link
 * org.apache.batik.script.Interpreter} corresponding to a particular
 * <tt>Document</tt> and scripting language.
 *
 * <p>By default, it is able to create interpreters for ECMAScript,
 * Python and Tcl scripting languages if you provide the right jar
 * files in your CLASSPATH (i.e.  Rhino, JPython and Jacl jar
 * files).</p>
 *
 * @author <a href="mailto:cjolif@ilog.fr">Christophe Jolif</a>
 * @version $Id: InterpreterPool.java,v 1.1 2005/11/21 09:51:40 dev Exp $
 */
public class InterpreterPool {

    /** The InterpreterFactory classname for Rhino. */
    private static final String RHINO = "org.apache.batik.script.rhino.RhinoInterpreterFactory";

    /** The InterpreterFactory classname for JPython. */
    private static final String JPYTHON = "org.apache.batik.script.jpython.JPythonInterpreterFactory";

    /** The InterpreterFactory classname for Jacl. */
    private static final String JACL = "org.apache.batik.script.jacl.JaclInterpreterFactory";

    /**
     * Name of the "document" object when referenced by scripts
     */
    public static final String BIND_NAME_DOCUMENT = "document";

    /**
     * The default InterpreterFactory map.
     */
    protected static Map defaultFactories = new HashMap(7);

    /**
     * The InterpreterFactory map.
     */
    protected Map factories = new HashMap(7);

    static {
        Iterator iter = Service.providers(InterpreterFactory.class);
        while (iter.hasNext()) {
            InterpreterFactory factory = null;
            factory = (InterpreterFactory) iter.next();
            defaultFactories.put(factory.getMimeType(), factory);
        }
    }

    /**
     * Constructs a new <tt>InterpreterPool</tt>.
     */
    public InterpreterPool() {
        factories.putAll(defaultFactories);
    }

    /**
     * Creates a new interpreter for the specified document and
     * according to the specified language. This method can return
     * null if no interpreter has been found for the specified
     * language.
     *
     * @param document the document that needs the interpreter
     * @param language the scripting language
     */
    public Interpreter createInterpreter(Document document, String language) {
        InterpreterFactory factory = (InterpreterFactory) factories.get(language);
        Interpreter interpreter = null;
        if (factory != null) interpreter = factory.createInterpreter(((SVGOMDocument) document).getURLObject());
        if (document != null) {
            interpreter.bindObject(BIND_NAME_DOCUMENT, document);
        }
        return interpreter;
    }

    /**
     * Adds for the specified language, the specified Interpreter factory.
     *
     * @param language the language for which the factory is registered
     * @param factory the <code>InterpreterFactory</code> to register
     */
    public void putInterpreterFactory(String language, InterpreterFactory factory) {
        factories.put(language, factory);
    }

    /**
     * Removes the InterpreterFactory associated to the specified language.
     *
     * @param language the language for which the factory should be removed.
     */
    public void removeInterpreterFactory(String language) {
        factories.remove(language);
    }
}
