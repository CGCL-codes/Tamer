package net.teqlo.db.sleepycat;

import static net.teqlo.db.sleepycat.SleepycatXmlDatabase.getAttributeValue;
import static net.teqlo.db.sleepycat.SleepycatXmlDatabase.getAttributeValueOrNull;
import static net.teqlo.xml.XmlConstantsV10c.JAVA_CLASS_ATTRIBUTE;
import java.io.Serializable;
import net.teqlo.TeqloException;
import net.teqlo.db.ComponentLookup;
import net.teqlo.xml.XmlConstantsV10c;
import com.sleepycat.dbxml.XmlException;
import com.sleepycat.dbxml.XmlQueryContext;
import com.sleepycat.dbxml.XmlResults;
import com.sleepycat.dbxml.XmlValue;

public class SleepycatComponentLookup implements ComponentLookup, Serializable {

    private static final long serialVersionUID = 1L;

    private String componentUri = null;

    private String label = null;

    @SuppressWarnings("unchecked")
    private transient Class javaClass = null;

    private String javaClassStr = null;

    /**
	 * Returns the java class of the component
	 * @throws TeqloException 
	 */
    @SuppressWarnings("unchecked")
    public Class getJavaClass() throws TeqloException {
        if (javaClassStr != null && javaClass == null) try {
            javaClass = Thread.currentThread().getContextClassLoader().loadClass(javaClassStr);
        } catch (Exception e) {
            throw new TeqloException(this, javaClassStr, e, "Error loading this class for component: " + componentUri);
        }
        return javaClass;
    }

    public String getComponentUri() {
        return this.componentUri;
    }

    public String getComponentLabel() {
        return this.label;
    }

    /**
	 * Factory method that constructs a component lookup
	 * @param db
	 * @param uri
	 * @return
	 * @throws TeqloException
	 */
    public static SleepycatComponentLookup make(final SleepycatXmlDatabase db, final String uri) throws TeqloException {
        if (uri == null) throw new TeqloException(db, uri, null, "Cannot make component lookup with null uri");
        final SleepycatComponentLookup cl = new SleepycatComponentLookup();
        cl.componentUri = uri;
        try {
            SleepycatXmlTransaction.wrapWithDeadlockRetry("ComponentLookup.make", new SleepycatXmlTransactionWrapper<Object>() {

                public Object run() throws Exception {
                    XmlResults results = null;
                    XmlValue componentVal = null;
                    XmlValue uriVal = null;
                    try {
                        XmlQueryContext qc = SleepycatXmlQueryContext.get();
                        uriVal = new XmlValue(uri);
                        qc.setVariableValue(SleepycatXmlDatabase.COMPONENT_URI_VAR, uriVal);
                        results = db.executeQuery(SleepycatXmlDatabase.COMPONENT_XQUERY, true);
                        if (!results.hasNext()) throw new XmlException(XmlException.INVALID_VALUE, "Could not find component definition with uri: " + uri);
                        componentVal = results.next();
                        cl.javaClassStr = getAttributeValueOrNull(componentVal, XmlConstantsV10c.TEQLO_JAVA_NAMESPACE_URI, JAVA_CLASS_ATTRIBUTE);
                        cl.label = getAttributeValue(componentVal, XmlConstantsV10c.LABEL_ATTRIBUTE);
                    } finally {
                        if (results != null) results.delete();
                        if (componentVal != null) componentVal.delete();
                        if (uriVal != null) uriVal.delete();
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            throw new TeqloException(db, uri, e, "Error making component lookup for this uri");
        }
        return cl;
    }
}
