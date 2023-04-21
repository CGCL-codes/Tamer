package org.geotools.ows.bindings;

import net.opengis.ows10.CodeType;
import net.opengis.ows10.Ows10Factory;
import javax.xml.namespace.QName;
import org.geotools.ows.OWS;
import org.geotools.xml.*;

/**
 * Binding object for the type http://www.opengis.net/ows:CodeType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="CodeType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Name or code with an (optional) authority. If the codeSpace attribute is present, its value should reference a dictionary, thesaurus, or authority for the name or code, such as the organisation who assigned the value, or the dictionary from which it is taken. &lt;/documentation&gt;
 *          &lt;documentation&gt;Type copied from basicTypes.xsd of GML 3 with documentation edited, for possible use outside the ServiceIdentification section of a service metadata document. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;simpleContent&gt;
 *          &lt;extension base="string"&gt;
 *              &lt;attribute name="codeSpace" type="anyURI" use="optional"/&gt;
 *          &lt;/extension&gt;
 *      &lt;/simpleContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 */
public class CodeTypeBinding extends AbstractComplexEMFBinding {

    public CodeTypeBinding(Ows10Factory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OWS.CodeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return super.getType();
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
