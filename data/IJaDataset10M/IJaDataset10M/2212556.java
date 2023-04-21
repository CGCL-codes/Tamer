package gov.ornl.nice.tests.item;

import gov.ornl.nice.nicedatastructures.form.DataComponent;

/** 
 * <!-- begin-UML-doc -->
 * <p>The FakeDataComponent class is a subclass of DataComponent that is used for testing.</p>
 * <!-- end-UML-doc -->
 * @author bkj
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FakeDataComponent extends DataComponent {

    String value;

    @Override
    public void update(String key, String newValue) {
        this.value = newValue;
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the value that was passed to the update operation inherited from DataComponent.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The value submitted to the update operation.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public String getUpdatedValue() {
        return value;
    }
}
