package BluRayWhiteBoxLevel1.BluRayWhiteBoxLevel2BluRayModel.Classes;

/** 
 * <!-- begin-UML-doc -->
 * <p>A subclass of MEDIA with the HighDefinition attribute. </p>
 * <!-- end-UML-doc -->
 * @author s4h
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class HD extends Media {

    public HD(Disc disc, String type) {
        super(disc, type);
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>An attribute that determines that this class has highdefinition audio and video.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private Boolean IsHighDefinition = true;

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>An operation that returns the value of the IsHighDefinition attribute.</p>
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public Boolean IsHighDefinition() {
        return IsHighDefinition;
    }
}
