package org.geotools.ows.bindings;

import net.opengis.ows10.Ows10Factory;
import javax.xml.namespace.QName;
import org.geotools.ows.OWS;
import org.geotools.xml.*;

/**
 * Binding object for the type http://www.opengis.net/ows:MimeType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;simpleType name="MimeType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;XML encoded identifier of a standard MIME type, possibly a parameterized MIME type. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;restriction base="string"&gt;
 *          &lt;pattern value="(application|audio|image|text|video|message|multipart|model)/.+(;\s*.+=.+)*"/&gt;
 *      &lt;/restriction&gt;
 *  &lt;/simpleType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 */
public class MimeTypeBinding extends AbstractSimpleBinding {

    public MimeTypeBinding(Ows10Factory factory) {
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OWS.MimeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        return super.parse(instance, value);
    }
}
