package org.hip.kernel.bom.directory.test;

import org.hip.kernel.bom.QueryStatement;
import org.hip.kernel.bom.directory.LDAPObjectHome;

/**
 * @author Luthiger
 * Created: 15.07.2007
 */
@SuppressWarnings("serial")
public class TestLDAPObjectHome extends LDAPObjectHome {

    public static final String KEY_ID = "ID";

    public static final String KEY_USER_ID = "UserID";

    public static final String KEY_NAME = "Name";

    public static final String KEY_FIRST_NAME = "Firstname";

    public static final String KEY_MAIL = "Mail";

    public static final String OBJECT_CLASS_NAME = "org.hip.kernel.bom.directory.test.TestLDAPObject";

    public static final String OBJECT_DEF = "<?xml version='1.0' encoding='ISO-8859-1'?>	\n" + "<objectDef objectName='Member' parent='org.hip.kernel.bom.DomainObject' baseDir='ou=users,ou=nethz,ou=id,ou=auth,o=ethz,c=ch' version='1.0'>	\n" + "	<keyDefs>	\n" + "		<keyDef>	\n" + "			<keyItemDef seq='0' keyPropertyName='" + KEY_ID + "'/>	\n" + "		</keyDef>	\n" + "	</keyDefs>	\n" + "	<propertyDefs>	\n" + "		<propertyDef propertyName='" + KEY_ID + "' propertyType='simple'>	\n" + "			<mappingDef columnName='cn'/>	\n" + "		</propertyDef>	\n" + "		<propertyDef propertyName='" + KEY_USER_ID + "' propertyType='simple'>	\n" + "			<mappingDef columnName='uid'/>	\n" + "		</propertyDef>	\n" + "		<propertyDef propertyName='" + KEY_NAME + "' propertyType='simple'>	\n" + "			<mappingDef columnName='name'/>	\n" + "		</propertyDef>	\n" + "		<propertyDef propertyName='" + KEY_FIRST_NAME + "' propertyType='simple'>	\n" + "			<mappingDef columnName='sn'/>	\n" + "		</propertyDef>	\n" + "		<propertyDef propertyName='" + KEY_MAIL + "' propertyType='simple'>	\n" + "			<mappingDef columnName='mail'/>	\n" + "		</propertyDef>	\n" + "	</propertyDefs>	\n" + "</objectDef>";

    public String getObjectClassName() {
        return OBJECT_CLASS_NAME;
    }

    @Override
    protected String getObjectDefString() {
        return OBJECT_DEF;
    }

    public QueryStatement createQueryStatement() {
        return new TestLDAPQueryStatement(this);
    }
}
