package org.jboss.jmx.adaptor.snmp.agent;

import java.util.ArrayList;
import org.jboss.jmx.adaptor.snmp.config.attribute.AttributeMappings;
import org.jboss.jmx.adaptor.snmp.config.attribute.ManagedBean;
import org.jboss.jmx.adaptor.snmp.config.attribute.MappedAttribute;
import org.jboss.logging.Logger;
import org.jboss.xb.binding.ObjectModelFactory;
import org.jboss.xb.binding.UnmarshallingContext;
import org.xml.sax.Attributes;

/**
 * Parse the mapping of JMX mbean attributes to SNMP OIDs
 * 
 * TODO: extend this parsing. 
 * 		-Add "table" element. This is not represented by any MBean attribute but is still recorded by the system,
 *       as a a way to conceptually organize the desired exposed JMX metrics. This element would have the oid-prefix
 *       attribute, which would replace the mbean oid-prefix 
 *      -Remove the ability for there to be an oid-prefix for the MBeans. instead, but this into the "table" element.
 *       the reason we should do this is because SNMP works with "Objects" and "Instances". The way it is currently
 *       parsed makes it seem like each MBean is a table, and we do not want to force this behavior.
 *      
 *      New scheme for parsing: 
 *      <mbean name=...> (MBean we're interested in.)
 *         <attribute name=... oid="1.2.3.4.1.0"/> (This attribute is a scalar, because it has no prefix. it's Object is 1.2.3.4.1 and the instance is 0) 
 *         ..
 *         <table name=... oid-prefix="1.2.3.4.1.8"/>  (This indicates the creation of a conceptual table.)
 *         	 <row name=... oid=".1"/> (This is a row in the table) neither this nor the table are directly accessible in SNMP.
 *            <attribute name=... oid=".1"/> (this is an actual instance of the row (a single column in the row.) 
 *            								 This attribute is accessed by 1.2.3.4.1.8.1.1 <tableOID><rowOID><instanceOID>
 *          ..
 *      </mbean>
 * 
 * @author <a href="mailto:hwr@pilhuhn.de">Heiko W. Rupp</a>
 * @version $Revision: 81038 $
 */
public class AttributeMappingsBinding implements ObjectModelFactory {

    private static Logger log = Logger.getLogger(AttributeMappingsBinding.class);

    public Object newRoot(Object root, UnmarshallingContext ctx, String namespaceURI, String localName, Attributes attrs) {
        if (!localName.equals("attribute-mappings")) {
            throw new IllegalStateException("Unexpected root " + localName + ". Expected <attribute-mappings>");
        }
        return new AttributeMappings();
    }

    public Object completeRoot(Object root, UnmarshallingContext ctx, String uri, String name) {
        return root;
    }

    public void setValue(AttributeMappings mappings, UnmarshallingContext navigator, String namespaceUri, String localName, String value) {
    }

    public Object newChild(AttributeMappings mappings, UnmarshallingContext navigator, String namespaceUri, String localName, Attributes attrs) {
        if ("mbean".equals(localName)) {
            String name = attrs.getValue("name");
            String oidPrefix = attrs.getValue("oid-prefix");
            ManagedBean child = new ManagedBean();
            child.setName(name);
            child.setOidPrefix(oidPrefix);
            if (log.isTraceEnabled()) log.trace("newChild: " + child.toString());
            return child;
        }
        return null;
    }

    public void addChild(AttributeMappings mappings, ManagedBean mbean, UnmarshallingContext navigator, String namespaceURI, String localName) {
        mappings.addMonitoredMBean(mbean);
    }

    public Object newChild(ManagedBean mbean, UnmarshallingContext navigator, String namespaceUri, String localName, Attributes attrs) {
        MappedAttribute attribute = null;
        if ("attribute".equals(localName)) {
            String oid = attrs.getValue("oid");
            String name = attrs.getValue("name");
            String mode = attrs.getValue("mode");
            String table = attrs.getValue("table");
            attribute = new MappedAttribute();
            attribute.setMode(mode);
            attribute.setName(name);
            attribute.setTable(table);
            attribute.setOid(oid);
        }
        return attribute;
    }

    public void addChild(ManagedBean mbean, MappedAttribute attribute, UnmarshallingContext navigator, String namespaceURI, String localName) {
        if (mbean.getAttributes() == null) mbean.setAttributes(new ArrayList());
        mbean.getAttributes().add(attribute);
    }
}
