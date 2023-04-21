package org.hip.kernel.bom.impl.test;

import java.util.Vector;
import org.hip.kernel.bom.impl.DomainObjectHomeImpl;
import org.hip.kernel.bom.BOMException;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.util.VInvalidNameException;
import org.hip.kernel.util.VInvalidValueException;

/**
 * TestHome used to test the joining of domain objects.
 * 
 * @author: Benno Luthiger
 */
@SuppressWarnings("serial")
public class QuestionHomeImpl extends DomainObjectHomeImpl {

    /** Every home has to know the class it handles. They provide access to
		this name through the method <I>getObjectClassName</I>;
	*/
    private static final String TESTOBJECT_CLASS_NAME = "org.hip.kernel.bom.impl.test.QuestionImpl";

    private static final String XML_OBJECT_DEF = "<?xml version='1.0' encoding='us-ascii'?>			\n" + "<objectDef objectName='Question' parent='org.hip.kernel.bom.DomainObject' version='1.0'>			\n" + "	<keyDefs>			\n" + "		<keyDef>			\n" + "			<keyItemDef seq='0' keyPropertyName='QuestionID'/>			\n" + "		</keyDef>			\n" + "	</keyDefs>			\n" + "	<propertyDefs>			\n" + "		<propertyDef propertyName='QuestionID' valueType='Number' propertyType='simple'>			\n" + "			<mappingDef tableName='tblQuestion' columnName='QUESTIONID'/>			\n" + "		</propertyDef>			\n" + "		<propertyDef propertyName='DecimalID' valueType='String' propertyType='simple'>			\n" + "			<mappingDef tableName='tblQuestion' columnName='SQUESTIONID'/>			\n" + "		</propertyDef>			\n" + "		<propertyDef propertyName='Question' valueType='String' propertyType='simple'>			\n" + "			<mappingDef tableName='tblQuestion' columnName='SQUESTION'/>			\n" + "		</propertyDef>			\n" + "		<propertyDef propertyName='Remark' valueType='String' propertyType='simple'>			\n" + "			<mappingDef tableName='tblQuestion' columnName='SREMARK'/>			\n" + "		</propertyDef>			\n" + "	</propertyDefs>			\n" + "</objectDef>			";

    /**
 * TestDomainObjectHomeImpl default constructor.
 */
    public QuestionHomeImpl() {
        super();
    }

    /**
 * Creates a vector containing objects for testing purpose.
 */
    protected Vector<Object> createTestObjects() {
        Vector<Object> lTestObjects = new Vector<Object>();
        KeyObject lKey = new KeyObjectImpl();
        try {
            lKey.setValue("Name", "Luthiger");
            lKey.setValue("FirstName", "Benno");
            lTestObjects.addElement(createCountAllString());
            lTestObjects.addElement(createCountString(lKey));
            lTestObjects.addElement(createKeyCountColumnList());
            lTestObjects.addElement(createPreparedSelectString(lKey));
            lTestObjects.addElement(createSelectAllString());
            lTestObjects.addElement(createSelectString(lKey));
        } catch (VInvalidNameException exc) {
        } catch (VInvalidValueException exc) {
        } catch (BOMException exc) {
        }
        return lTestObjects;
    }

    public String getObjectClassName() {
        return TESTOBJECT_CLASS_NAME;
    }

    protected String getObjectDefString() {
        return XML_OBJECT_DEF;
    }
}
