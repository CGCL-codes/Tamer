package org.geotools.wfs.bindings;

import javax.xml.namespace.QName;
import net.opengis.wfs.OperationsType;
import net.opengis.wfs.WfsFactory;
import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Binding object for the type http://www.opengis.net/wfs:OperationsType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="OperationsType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="unbounded" name="Operation" type="wfs:OperationType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL: http://svn.osgeo.org/geotools/tags/2.6.5/modules/extension/xsd/xsd-wfs/src/main/java/org/geotools/wfs/bindings/OperationsTypeBinding.java $
 */
public class OperationsTypeBinding extends AbstractComplexEMFBinding {

    public OperationsTypeBinding(WfsFactory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.OperationsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return OperationsType.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return super.parse(instance, node, value);
    }
}
