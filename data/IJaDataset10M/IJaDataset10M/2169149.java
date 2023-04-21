package org.hip.vif.bom.impl;

import java.sql.SQLException;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.bom.KeyObject.BinaryBooleanOperator;
import org.hip.kernel.bom.impl.JoinedDomainObjectHomeImpl;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.exc.VException;
import org.hip.vif.bom.QuestionAuthorReviewerHome;
import org.hip.vif.bom.QuestionHierarchyHome;
import org.hip.vif.bom.QuestionHome;
import org.hip.vif.bom.ResponsibleHome;

/**
 * Home of join from the question hierachy to a child questions and 
 * author/reviewer table.
 * This home can be used to retrieve the data of child questions to
 * the specified question being authored by the specified member.
 * 
 * Created on 12.08.2003
 * @author Benno Luthiger
 */
public class JoinQuestionToChildAndAuthorHome extends JoinedDomainObjectHomeImpl {

    private static final String JOIN_OBJECT_CLASS_NAME = "org.hip.vif.bom.impl.JoinQuestionToChildAndAuthor";

    private static final String XML_OBJECT_DEF = "<?xml version='1.0' encoding='ISO-8859-1'?>	\n" + "<joinedObjectDef objectName='JoinQuestionToChildAndAuthor' parent='org.hip.kernel.bom.ReadOnlyDomainObject' version='1.0'>	\n" + "	<columnDefs>	\n" + "		<columnDef columnName='" + QuestionHierarchyHome.KEY_PARENT_ID + "' domainObject='org.hip.vif.bom.impl.QuestionHierarchyImpl'/>	\n" + "		<columnDef columnName='" + QuestionHome.KEY_ID + "' domainObject='org.hip.vif.bom.impl.QuestionImpl'/>	\n" + "		<columnDef columnName='" + QuestionHome.KEY_QUESTION_DECIMAL + "' domainObject='org.hip.vif.bom.impl.QuestionImpl'/>	\n" + "		<columnDef columnName='" + QuestionHome.KEY_QUESTION + "' domainObject='org.hip.vif.bom.impl.QuestionImpl'/>	\n" + "		<columnDef columnName='" + QuestionHome.KEY_REMARK + "' domainObject='org.hip.vif.bom.impl.QuestionImpl'/>	\n" + "		<columnDef columnName='" + QuestionHome.KEY_ROOT_QUESTION + "' domainObject='org.hip.vif.bom.impl.QuestionImpl'/>	\n" + "		<columnDef columnName='" + QuestionHome.KEY_GROUP_ID + "' domainObject='org.hip.vif.bom.impl.QuestionImpl'/>	\n" + "		<columnDef columnName='" + QuestionHome.KEY_STATE + "' domainObject='org.hip.vif.bom.impl.QuestionImpl'/>	\n" + "		<columnDef columnName='" + QuestionHome.KEY_MUTATION + "' domainObject='org.hip.vif.bom.impl.QuestionImpl'/>	\n" + "		<columnDef columnName='" + ResponsibleHome.KEY_MEMBER_ID + "' domainObject='org.hip.vif.bom.impl.QuestionAuthorReviewerImpl'/>	\n" + "		<columnDef columnName='" + ResponsibleHome.KEY_TYPE + "' domainObject='org.hip.vif.bom.impl.QuestionAuthorReviewerImpl'/>	\n" + "	</columnDefs>	\n" + "	<joinDef joinType='EQUI_JOIN'>	\n" + "		<objectDesc objectClassName='org.hip.vif.bom.impl.QuestionHierarchyImpl'/>	\n" + "		<objectDesc objectClassName='org.hip.vif.bom.impl.QuestionImpl'/>	\n" + "		<joinCondition>	\n" + "			<columnDef columnName='" + QuestionHierarchyHome.KEY_CHILD_ID + "' domainObject='org.hip.vif.bom.impl.QuestionHierarchyImpl'/>	\n" + "			<columnDef columnName='" + QuestionHome.KEY_ID + "' domainObject='org.hip.vif.bom.impl.QuestionImpl'/>	\n" + "		</joinCondition>	\n" + "		<joinDef joinType='EQUI_JOIN'>	\n" + "			<objectDesc objectClassName='org.hip.vif.bom.impl.QuestionImpl'/>	\n" + "			<objectDesc objectClassName='org.hip.vif.bom.impl.QuestionAuthorReviewerImpl'/>	\n" + "			<joinCondition>	\n" + "				<columnDef columnName='" + QuestionHome.KEY_ID + "' domainObject='org.hip.vif.bom.impl.QuestionImpl'/>	\n" + "				<columnDef columnName='" + QuestionAuthorReviewerHome.KEY_QUESTION_ID + "' domainObject='org.hip.vif.bom.impl.QuestionAuthorReviewerImpl'/>	\n" + "			</joinCondition>	\n" + "		</joinDef>	\n" + "	</joinDef>	\n" + "</joinedObjectDef>";

    /**
	 * JoinQuestionToChildAndAuthorHome default constructor.
	 * 
	 */
    public JoinQuestionToChildAndAuthorHome() {
        super();
    }

    /**
	 * @see org.hip.kernel.bom.GeneralDomainObjectHome#getObjectClassName()
	 */
    public String getObjectClassName() {
        return JOIN_OBJECT_CLASS_NAME;
    }

    /**
	 * @see org.hip.kernel.bom.impl.AbstractDomainObjectHome#getObjectDefString()
	 */
    protected String getObjectDefString() {
        return XML_OBJECT_DEF;
    }

    /**
	 * <p>Returns all child questions to the specified question 
	 * that are either authored by the specified member or in the specified states.</p>
	 * 
	 * @param inQuestionID String
	 * @param inAuthorID Integer
	 * @param inState Integer[]
	 * @return QueryResult
	 * @throws VException
	 * @throws SQLException
	 */
    public QueryResult getAuthorView(String inQuestionID, Long inAuthorID, Integer[] inState) throws VException, SQLException {
        KeyObject lKey = new KeyObjectImpl();
        lKey.setValue(QuestionHierarchyHome.KEY_PARENT_ID, inQuestionID);
        lKey.setValue(ResponsibleHome.KEY_TYPE, ResponsibleHome.VALUE_AUTHOR);
        KeyObject lKeyStates = BOMHelper.getKeyStates(QuestionHome.KEY_STATE, inState);
        lKeyStates.setValue(ResponsibleHome.KEY_MEMBER_ID, inAuthorID, "=", BinaryBooleanOperator.OR);
        lKey.setValue(lKeyStates);
        return select(lKey);
    }
}
