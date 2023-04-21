package net.teqlo.db.sleepycat;

import java.net.URI;
import net.teqlo.db.XmlDatabase;
import net.teqlo.util.Loggers;
import net.teqlo.xml.XmlConstantsV10c;
import com.sleepycat.dbxml.XmlException;
import com.sleepycat.dbxml.XmlQueryContext;
import com.sleepycat.dbxml.XmlValue;

/**
 * This class provides a ThreadLocal query context,
 * @author jthwaites
 *
 */
public class SleepycatXmlQueryContext {

    /**
	 * Static field holds query context ThreadLocal object, with an initialValue() method
	 */
    private static ThreadLocal<XmlQueryContext> qc = new ThreadLocal<XmlQueryContext>() {

        @Override
        protected synchronized XmlQueryContext initialValue() {
            XmlQueryContext qc = null;
            SleepycatXmlDatabase db = null;
            try {
                db = (SleepycatXmlDatabase) XmlDatabase.getInstance();
                String path = db.databaseHome + (db.databaseHome.endsWith("/") ? "" : "/");
                URI baseURI = new URI("file", path, null);
                qc = db.manager.createQueryContext(XmlQueryContext.LiveValues);
                String uriString = baseURI.getScheme() == null ? baseURI.getSchemeSpecificPart() : baseURI.getScheme() + ":" + baseURI.getSchemeSpecificPart();
                qc.setBaseURI(uriString);
                qc.setNamespace(XmlConstantsV10c.TEQLO_NS_PREFIX, XmlConstantsV10c.TEQLO_NAMESPACE_URI);
            } catch (Exception e) {
                Loggers.XML_RUNTIME.fatal("Failed to create XmlQueryContext on " + db, e);
                throw new IllegalStateException("Failed to create XmlQueryContext on " + db);
            }
            return qc;
        }
    };

    /**
	 * Get the thread local query context object
	 * @return
	 */
    protected static XmlQueryContext get() throws XmlException {
        XmlQueryContext qc = SleepycatXmlQueryContext.qc.get();
        if (qc == null) throw new XmlException(XmlException.INVALID_VALUE, "XmlQueryContext was null", null, 0);
        return qc;
    }

    /**
	 * Remove the thread local query context object and delete native resources
	 */
    protected static void remove() {
        XmlQueryContext qc = SleepycatXmlQueryContext.qc.get();
        if (qc != null) qc.delete();
        SleepycatXmlQueryContext.qc.remove();
    }

    /**
	 * Set query context variable value
	 * @param variable
	 * @param value
	 * @throws XmlException
	 */
    protected static void setVariable(String variable, XmlValue value) throws XmlException {
        XmlQueryContext qc = SleepycatXmlQueryContext.qc.get();
        qc.setVariableValue(variable, value);
    }
}
