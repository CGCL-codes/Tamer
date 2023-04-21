package edu.mit.lcs.haystack.ozone.standard.behaviors;

import edu.mit.lcs.haystack.adenine.interpreter.*;
import edu.mit.lcs.haystack.ozone.core.Context;
import edu.mit.lcs.haystack.ozone.core.IBehavior;
import edu.mit.lcs.haystack.ozone.core.IPart;
import edu.mit.lcs.haystack.ozone.core.Ozone;
import edu.mit.lcs.haystack.ozone.core.OzoneConstants;
import edu.mit.lcs.haystack.rdf.*;
import java.util.*;

/**
 * Invokes an Adenine method with parameters.
 * @version 	1.0
 * @author		David Huynh
 */
public class AdenineCallBehavior implements IBehavior {

    static org.apache.log4j.Logger s_logger = org.apache.log4j.Logger.getLogger(AdenineCallBehavior.class);

    public void initializeFromDeserialization(IRDFContainer source) {
        m_source = source;
    }

    IRDFContainer m_source;

    Context m_context;

    Resource m_resPartData;

    Interpreter m_interpreter;

    DynamicEnvironment m_denv;

    static final Resource METHOD = new Resource(OzoneConstants.s_namespace + "method");

    static final Resource PARAMETERS = new Resource(OzoneConstants.s_namespace + "parameters");

    /**
	 * @see IPart#initialize(IRDFContainer, Context)
	 */
    public void initialize(IRDFContainer source, Context c) {
        m_source = source;
        m_context = c;
        getInitializationData();
        internalInitialize();
    }

    /**
	 * @see IBehavior#activate(Resource, IPart, EventObject)
	 */
    public void activate(Resource resElement, IPart part, EventObject event) {
        try {
            Resource resMethod = Utilities.getResourceProperty(m_resPartData, METHOD, m_source);
            Resource resParameters = Utilities.getResourceProperty(m_resPartData, PARAMETERS, m_source);
            ArrayList l = new ArrayList();
            Iterator i = ListUtilities.accessDAMLList(resParameters, m_source);
            while (i.hasNext()) {
                l.add(i.next());
            }
            m_interpreter.callMethod(resMethod, l.toArray(), m_denv);
        } catch (Exception e) {
            s_logger.error("An error occurred executing behavior " + m_resPartData, e);
        }
    }

    /**
	 * @see IPart#dispose()
	 */
    public void dispose() {
    }

    /**
	 * @see IPart#handleEvent(Resource, Object)
	 */
    public boolean handleEvent(Resource eventType, Object event) {
        return false;
    }

    /**
	 * Retrieves resources.
	 */
    private void getInitializationData() {
        m_resPartData = (Resource) m_context.getProperty(OzoneConstants.s_partData);
    }

    /**
	 * Does the actual initialization work.
	 */
    private void internalInitialize() {
        m_interpreter = new Interpreter(m_source);
        m_denv = new DynamicEnvironment(m_source);
        Ozone.initializeDynamicEnvironment(m_denv, m_context);
    }
}
