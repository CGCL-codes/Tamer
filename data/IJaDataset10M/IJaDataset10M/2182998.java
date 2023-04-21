package org.hip.vif.bom.impl;

import org.hip.vif.bom.GroupAdminHome;
import org.hip.vif.bom.MemberHome;
import org.hip.vif.bom.ParticipantHome;

/**
 * Home to list a specified group's participants. 
 * Note: This version is used for special databases like Derby which don't like expressions like 'NOW() >= tblParticipant.DTSUSPENDFROM'.
 * 
 * @author Benno Luthiger
 * Created on Jan 7, 2009
 */
public class NestedParticipantsOfGroupHome2 extends NestedParticipantsOfGroupHome {

    public static final String NESTED_ALIAS = "Admins";

    public static final String KEY_GROUPADMIN = "GroupAdminID";

    public static final String KEY_SUSPENDED_TEST1 = "TestSuspended1";

    public static final String KEY_SUSPENDED_TEST2 = "TestSuspended2";

    private static final String OBJECT_CLASS_NAME = "org.hip.vif.bom.impl.NestedParticipantsOfGroup";

    private static final String XML_OBJECT_DEF = "<?xml version='1.0' encoding='ISO-8859-1'?>	" + "<joinedObjectDef objectName='NestedParticipantsOfGroup' parent='org.hip.kernel.bom.ReadOnlyDomainObject' version='1.0'>	" + "	<columnDefs>	" + "		<columnDef columnName='" + ParticipantHome.KEY_GROUP_ID + "' domainObject='org.hip.vif.bom.impl.ParticipantImpl'/>	\n" + "		<columnDef columnName='" + ParticipantHome.KEY_GROUP_ID + "' as='" + KEY_SUSPENDED_TEST1 + "' domainObject='org.hip.vif.bom.impl.ParticipantImpl'/>	\n" + "		<columnDef columnName='" + ParticipantHome.KEY_GROUP_ID + "' as='" + KEY_SUSPENDED_TEST2 + "' domainObject='org.hip.vif.bom.impl.ParticipantImpl'/>	\n" + "		<columnDef columnName='" + GroupAdminHome.KEY_MEMBER_ID + "' as='" + KEY_GROUPADMIN + "' nestedObject='" + NESTED_ALIAS + "' domainObject='org.hip.vif.bom.impl.GroupAdminImpl'/>	\n" + "		<columnDef columnName='" + MemberHome.KEY_ID + "' domainObject='org.hip.vif.bom.impl.MemberImpl'/>	\n" + "		<columnDef columnName='" + MemberHome.KEY_USER_ID + "' domainObject='org.hip.vif.bom.impl.MemberImpl'/>	\n" + "		<columnDef columnName='" + MemberHome.KEY_NAME + "' domainObject='org.hip.vif.bom.impl.MemberImpl'/>	\n" + "		<columnDef columnName='" + MemberHome.KEY_FIRSTNAME + "' domainObject='org.hip.vif.bom.impl.MemberImpl'/>	\n" + "		<columnDef columnName='" + MemberHome.KEY_CITY + "' domainObject='org.hip.vif.bom.impl.MemberImpl'/>	\n" + "		<columnDef columnName='" + MemberHome.KEY_ZIP + "' domainObject='org.hip.vif.bom.impl.MemberImpl'/>	\n" + "		<columnDef columnName='" + MemberHome.KEY_MAIL + "' domainObject='org.hip.vif.bom.impl.MemberImpl'/>	\n" + "		<columnDef columnName='" + MemberHome.KEY_SEX + "' domainObject='org.hip.vif.bom.impl.MemberImpl'/>	\n" + "	</columnDefs>	" + "	<joinDef joinType='EQUI_JOIN'>	\n" + "		<objectDesc objectClassName='org.hip.vif.bom.impl.ParticipantImpl'/>	\n" + "		<objectDesc objectClassName='org.hip.vif.bom.impl.MemberImpl'/>	\n" + "		<joinCondition>	\n" + "			<columnDef columnName='" + ParticipantHome.KEY_MEMBER_ID + "' domainObject='org.hip.vif.bom.impl.ParticipantImpl'/>	\n" + "			<columnDef columnName='" + MemberHome.KEY_ID + "' domainObject='org.hip.vif.bom.impl.MemberImpl'/>	\n" + "		</joinCondition>	\n" + "		<joinDef joinType='LEFT_JOIN'>	\n" + "			<objectDesc objectClassName='org.hip.vif.bom.impl.ParticipantImpl'/>	\n" + "			<objectPlaceholder name='" + NESTED_ALIAS + "' />	\n" + "			<joinCondition>	\n" + "				<columnDef columnName='" + ParticipantHome.KEY_MEMBER_ID + "' domainObject='org.hip.vif.bom.impl.ParticipantImpl'/>	\n" + "				<columnDef columnName='" + GroupAdminHome.KEY_MEMBER_ID + "' nestedObject='" + NESTED_ALIAS + "' domainObject='org.hip.vif.bom.impl.GroupAdminImpl'/>	\n" + "			</joinCondition>	\n" + "		</joinDef>	\n" + "	</joinDef>	\n" + "</joinedObjectDef>";

    /**
	 * Returns the name of the objects which this home can create.
	 *
	 * @return java.lang.String
	 */
    public String getObjectClassName() {
        return OBJECT_CLASS_NAME;
    }

    /**
	 * Returns the object definition string of the class managed by this home.
	 *
	 * @return java.lang.String
	 */
    protected String getObjectDefString() {
        return XML_OBJECT_DEF;
    }
}
