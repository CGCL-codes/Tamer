package gov.ornl.hermes.Persistence.Extensions;

import gov.ornl.hermes.ParentElement;
import java.io.File;
import java.io.InputStream;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import gov.ornl.hermes.ReactingElement;
import gov.ornl.hermes.Persistence.XMLPersistence;

/** 
 * <!-- begin-UML-doc -->
 * <p>A class that extends the ParentElement class for Persistence.</p>
 * <!-- end-UML-doc -->
 * @author s4h
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(namespace = "ParentElement")
public class PersistentParentElement extends ParentElement implements IPersistable {

    /** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private Set<ReactingElement> reactingElement;

    /** 
	 * (non-Javadoc)
	 * @throws Exception 
	 * @see IPersistable#load(Object inputStream)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public void load(InputStream inputStream) throws Exception {
        if (inputStream == null) {
            throw new Exception("InputStreamException: InputStream can not be null");
        }
        Object o = XMLPersistence.convertFromXML(inputStream, this.getClass());
        PersistentParentElement element = (PersistentParentElement) o;
        this.fromString(element.toString());
    }

    /** 
	 * (non-Javadoc)
	 * @throws Exception 
	 * @see IPersistable#persist(String xmlFile)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public void persist(String xmlFile) throws Exception {
        XMLPersistence.convertToXML(xmlFile, this);
    }
}
