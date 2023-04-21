package com.liferay.portalweb.portal.permissions.enterpriseadmin;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="CA_MessageBoardsRolesTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class CA_MessageBoardsRolesTest extends BaseTestCase {

    public void testCA_MessageBoardsRoles() throws Exception {
        selenium.click(RuntimeVariables.replace("//input[@value='Add Portlet Permissions']"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("link=Next"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("link=Message Boards"));
        selenium.waitForPageToLoad("30000");
        selenium.select("_79_scope19ADD_CATEGORY", RuntimeVariables.replace("label=Enterprise"));
        selenium.select("_79_scope19BAN_USER", RuntimeVariables.replace("label=Enterprise"));
        selenium.select("_79_scope19CONFIGURATION", RuntimeVariables.replace("label=Enterprise"));
        selenium.select("_79_scope19VIEW", RuntimeVariables.replace("label=Enterprise"));
        selenium.select("_79_scopecom.liferay.portlet.messageboards.model.MBCategoryADD_FILE", RuntimeVariables.replace("label=Enterprise"));
        selenium.select("_79_scopecom.liferay.portlet.messageboards.model.MBCategoryADD_MESSAGE", RuntimeVariables.replace("label=Enterprise"));
        selenium.select("_79_scopecom.liferay.portlet.messageboards.model.MBCategoryADD_SUBCATEGORY", RuntimeVariables.replace("label=Enterprise"));
        selenium.select("_79_scopecom.liferay.portlet.messageboards.model.MBCategoryDELETE", RuntimeVariables.replace("label=Enterprise"));
        selenium.select("_79_scopecom.liferay.portlet.messageboards.model.MBCategoryMOVE_THREAD", RuntimeVariables.replace("label=Enterprise"));
        selenium.select("_79_scopecom.liferay.portlet.messageboards.model.MBCategoryPERMISSIONS", RuntimeVariables.replace("label=Enterprise"));
        selenium.select("_79_scopecom.liferay.portlet.messageboards.model.MBCategoryREPLY_TO_MESSAGE", RuntimeVariables.replace("label=Enterprise"));
        selenium.select("_79_scopecom.liferay.portlet.messageboards.model.MBCategorySUBSCRIBE", RuntimeVariables.replace("label=Enterprise"));
        selenium.select("_79_scopecom.liferay.portlet.messageboards.model.MBCategoryUPDATE", RuntimeVariables.replace("label=Enterprise"));
        selenium.select("_79_scopecom.liferay.portlet.messageboards.model.MBCategoryUPDATE_THREAD_PRIORITY", RuntimeVariables.replace("label=Enterprise"));
        selenium.select("_79_scopecom.liferay.portlet.messageboards.model.MBCategoryVIEW", RuntimeVariables.replace("label=Enterprise"));
        selenium.select("_79_scopecom.liferay.portlet.messageboards.model.MBMessageDELETE", RuntimeVariables.replace("label=Enterprise"));
        selenium.select("_79_scopecom.liferay.portlet.messageboards.model.MBMessagePERMISSIONS", RuntimeVariables.replace("label=Enterprise"));
        selenium.select("_79_scopecom.liferay.portlet.messageboards.model.MBMessageSUBSCRIBE", RuntimeVariables.replace("label=Enterprise"));
        selenium.select("_79_scopecom.liferay.portlet.messageboards.model.MBMessageUPDATE", RuntimeVariables.replace("label=Enterprise"));
        selenium.select("_79_scopecom.liferay.portlet.messageboards.model.MBMessageVIEW", RuntimeVariables.replace("label=Enterprise"));
        selenium.click(RuntimeVariables.replace("//input[@value='Save']"));
        selenium.waitForPageToLoad("30000");
    }
}
