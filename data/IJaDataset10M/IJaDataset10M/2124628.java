package org.hip.vif.bom.impl;

import org.hip.kernel.bom.impl.DomainObjectHomeImpl;
import org.hip.vif.bom.QuestionHistoryHome;
import org.hip.vif.bom.QuestionHome;

/**
 * This domain object home implements the QuestionHistoryHome interface.
 * 
 * @author Benno Luthiger
 * @see org.hip.vif.bom.QuestionHistoryHome
 */
public class QuestionHistoryHomeImpl extends DomainObjectHomeImpl implements QuestionHistoryHome {

    private static final String OBJECT_CLASS_NAME = "org.hip.vif.bom.impl.QuestionHistoryImpl";

    private static final String XML_OBJECT_DEF = "<?xml version='1.0' encoding='ISO-8859-1'?>	\n" + "<objectDef objectName='QuestionHistory' parent='org.hip.kernel.bom.DomainObject' version='1.0'>	\n" + "	<keyDefs>	\n" + "		<keyDef>	\n" + "			<keyItemDef seq='0' keyPropertyName='" + QuestionHome.KEY_ID + "'/>	\n" + "			<keyItemDef seq='1' keyPropertyName='" + QuestionHome.KEY_MUTATION + "'/>	\n" + "			<keyItemDef seq='2' keyPropertyName='" + KEY_VALID_TO + "'/>	\n" + "		</keyDef>	\n" + "	</keyDefs>	\n" + "	<propertyDefs>	\n" + "		<propertyDef propertyName='" + QuestionHome.KEY_ID + "' valueType='Number' propertyType='simple'>	\n" + "			<mappingDef tableName='tblQuestionHistory' columnName='QuestionID'/>	\n" + "		</propertyDef>	\n" + "		<propertyDef propertyName='" + QuestionHome.KEY_MUTATION + "' valueType='Timestamp' propertyType='simple'>	\n" + "			<mappingDef tableName='tblQuestionHistory' columnName='dtFrom'/>	\n" + "		</propertyDef>	\n" + "		<propertyDef propertyName='" + KEY_VALID_TO + "' valueType='Timestamp' propertyType='simple'>	\n" + "			<mappingDef tableName='tblQuestionHistory' columnName='dtTo'/>	\n" + "		</propertyDef>	\n" + "		<propertyDef propertyName='" + QuestionHome.KEY_QUESTION_DECIMAL + "' valueType='String' propertyType='simple'>	\n" + "			<mappingDef tableName='tblQuestionHistory' columnName='sQuestionID'/>	\n" + "		</propertyDef>	\n" + "		<propertyDef propertyName='" + QuestionHome.KEY_QUESTION + "' valueType='String' propertyType='simple'>	\n" + "			<mappingDef tableName='tblQuestionHistory' columnName='sQuestion'/>	\n" + "		</propertyDef>	\n" + "		<propertyDef propertyName='" + QuestionHome.KEY_REMARK + "' valueType='String' propertyType='simple'>	\n" + "			<mappingDef tableName='tblQuestionHistory' columnName='sRemark'/>	\n" + "		</propertyDef>	\n" + "		<propertyDef propertyName='" + QuestionHome.KEY_STATE + "' valueType='Number' propertyType='simple'>	\n" + "			<mappingDef tableName='tblQuestionHistory' columnName='nState'/>	\n" + "		</propertyDef>	\n" + "		<propertyDef propertyName='" + QuestionHome.KEY_GROUP_ID + "' valueType='Number' propertyType='simple'>	\n" + "			<mappingDef tableName='tblQuestionHistory' columnName='GroupID'/>	\n" + "		</propertyDef>	\n" + "		<propertyDef propertyName='" + QuestionHome.KEY_ROOT_QUESTION + "' valueType='Number' propertyType='simple'>	\n" + "			<mappingDef tableName='tblQuestionHistory' columnName='bRootQuestion'/>	\n" + "		</propertyDef>	\n" + "		<propertyDef propertyName='" + KEY_MEMBER_ID + "' valueType='Number' propertyType='simple'>	\n" + "			<mappingDef tableName='tblQuestionHistory' columnName='MemberID'/>	\n" + "		</propertyDef>	\n" + "	</propertyDefs>	\n" + "</objectDef>";

    /**
	 * Constructor for QuestionHistoryHomeImpl.
	 */
    public QuestionHistoryHomeImpl() {
        super();
    }

    /**
	 * @see org.hip.kernel.bom.GeneralDomainObjectHome#getObjectClassName()
	 */
    public String getObjectClassName() {
        return OBJECT_CLASS_NAME;
    }

    /**
	 * @see org.hip.kernel.bom.impl.AbstractDomainObjectHome#getObjectDefString()
	 */
    protected String getObjectDefString() {
        return XML_OBJECT_DEF;
    }
}
