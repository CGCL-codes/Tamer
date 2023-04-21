package org.geotools.wfs.bindings;

import javax.xml.namespace.QName;
import net.opengis.wfs.TransactionSummaryType;
import net.opengis.wfs.WfsFactory;
import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Binding object for the type
 * http://www.opengis.net/wfs:TransactionSummaryType.
 * 
 * <p>
 * 
 * <pre>
 *         <code>
 *  &lt;xsd:complexType name=&quot;TransactionSummaryType&quot;&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation xml:lang=&quot;en&quot;&gt;
 *              Reports the total number of features affected by some kind
 *              of write action (i.e, insert, update, delete).
 *           &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element minOccurs=&quot;0&quot; name=&quot;totalInserted&quot; type=&quot;xsd:nonNegativeInteger&quot;/&gt;
 *          &lt;xsd:element minOccurs=&quot;0&quot; name=&quot;totalUpdated&quot; type=&quot;xsd:nonNegativeInteger&quot;/&gt;
 *          &lt;xsd:element minOccurs=&quot;0&quot; name=&quot;totalDeleted&quot; type=&quot;xsd:nonNegativeInteger&quot;/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 * </code>
 *         </pre>
 * 
 * </p>
 * 
 * @generated
 *
 * @source $URL: http://svn.osgeo.org/geotools/tags/2.6.5/modules/extension/xsd/xsd-wfs/src/main/java/org/geotools/wfs/bindings/TransactionSummaryTypeBinding.java $
 */
public class TransactionSummaryTypeBinding extends AbstractComplexEMFBinding {

    private WfsFactory factory;

    public TransactionSummaryTypeBinding(WfsFactory factory) {
        super(factory);
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.TransactionSummaryType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return TransactionSummaryType.class;
    }

    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return super.parse(instance, node, value);
    }
}
