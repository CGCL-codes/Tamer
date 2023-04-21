package com.centraview.email.emailfacade;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Logger;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.email.EmailList;
import com.centraview.email.FolderList;
import com.centraview.email.MailAccount;
import com.centraview.email.MailMessage;
import com.centraview.email.RuleDetails;
import com.centraview.email.RuleList;
import com.centraview.email.emailList.EmailLocal;
import com.centraview.email.emailList.EmailLocalHome;
import com.centraview.email.emailmanage.EmailManageLocal;
import com.centraview.email.emailmanage.EmailManageLocalHome;
import com.centraview.email.folder.ManageFolderLocal;
import com.centraview.email.folder.ManageFolderLocalHome;
import com.centraview.email.getmail.GetMailLocal;
import com.centraview.email.getmail.GetMailLocalHome;
import com.centraview.email.helper.EmailHelperLocal;
import com.centraview.email.helper.EmailHelperLocalHome;
import com.centraview.email.mailDeliver.MailDeliverLocal;
import com.centraview.email.mailDeliver.MailDeliverLocalHome;
import com.centraview.email.rules.RulesManageLocal;
import com.centraview.email.rules.RulesManageLocalHome;
import com.centraview.email.savedraft.SaveDraftLocal;
import com.centraview.email.savedraft.SaveDraftLocalHome;
import com.centraview.email.sendmail.SendMailLocal;
import com.centraview.email.sendmail.SendMailLocalHome;
import com.centraview.email.syncemail.SyncMailLocal;
import com.centraview.email.syncemail.SyncMailLocalHome;

public class EmailFacadeBean implements SessionBean {

    private static Logger logger = Logger.getLogger(EmailFacadeBean.class);

    protected javax.ejb.SessionContext ctx;

    protected Context environment;

    private String dataSource = "MySqlDS";

    public void setSessionContext(SessionContext ctx) throws RemoteException {
        this.ctx = ctx;
    }

    public void ejbActivate() throws RemoteException {
    }

    public void ejbPassivate() throws RemoteException {
    }

    public void ejbRemove() throws RemoteException {
    }

    public void ejbCreate() throws CreateException, RemoteException {
    }

    /**
	 * This method retrieves the user's email account id based on the individualid
	 * @param userId
	 * @return email account id
	 */
    public int getDefaultAccountID(int individualid) throws AuthorizationFailedException {
        if (!CVUtility.isModuleVisible("Email", individualid, this.dataSource)) {
            throw new AuthorizationFailedException("Email- getDefaultAccountID");
        }
        int accountid = 0;
        String strSQL = "email.getdefaultemailaccountid";
        try {
            CVDal cvdl = new CVDal(dataSource);
            cvdl.setSql(strSQL);
            cvdl.setInt(1, individualid);
            Collection v = cvdl.executeQuery();
            cvdl.clearParameters();
            cvdl.destroy();
            Iterator it = v.iterator();
            while (it.hasNext()) {
                HashMap hm = (HashMap) it.next();
                accountid = ((Long) hm.get("AccountID")).intValue();
            }
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.getDefaultAccountID] Exception Thrown: " + e);
            e.printStackTrace();
        }
        return accountid;
    }

    public EmailList getEmailList(int individualID, HashMap info) throws AuthorizationFailedException {
        if (!CVUtility.isModuleVisible("Email", individualID, this.dataSource)) {
            throw new AuthorizationFailedException("[EmailFacadeBean.getEmailList() for individualID = " + individualID);
        }
        EmailList emailList = new EmailList();
        try {
            InitialContext ic = CVUtility.getInitialContext();
            EmailLocalHome home = (EmailLocalHome) ic.lookup("local/EmailList");
            EmailLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            emailList = (EmailList) remote.getEmailList(individualID, info);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.getEmailList] Exception Thrown: " + e);
            e.printStackTrace();
        }
        if (emailList == null) {
            emailList = new EmailList();
        }
        return emailList;
    }

    /**
	this method returns returns mailmessage object
	which contains data of message
	*/
    public MailMessage getMailMessage(int userId, HashMap preference) throws AuthorizationFailedException {
        if (!CVUtility.isModuleVisible("Email", userId, this.dataSource)) throw new AuthorizationFailedException("Email- getMailMessage");
        MailMessage mailmessage = null;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            GetMailLocalHome home = (GetMailLocalHome) ic.lookup("local/GetMail");
            GetMailLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            mailmessage = (MailMessage) remote.getMailMessage(userId, preference);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.getMailMessage] Exception Thrown: " + e);
            e.printStackTrace();
            return null;
        }
        return mailmessage;
    }

    public MailMessage getAttachment(int userId, HashMap preference) throws AuthorizationFailedException {
        if (!CVUtility.isModuleVisible("Email", userId, this.dataSource)) throw new AuthorizationFailedException("Email- getAttachment");
        MailMessage mailmessage = null;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            GetMailLocalHome home = (GetMailLocalHome) ic.lookup("local/GetMail");
            GetMailLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            mailmessage = (MailMessage) remote.getAttachment(userId, preference);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.getAttachment] Exception Thrown: " + e);
            e.printStackTrace();
        }
        return mailmessage;
    }

    public boolean checkEmailAccount(int userId) {
        boolean emailFlag = true;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            GetMailLocalHome home = (GetMailLocalHome) ic.lookup("local/GetMail");
            GetMailLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            emailFlag = remote.checkEmailAccount(userId);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.checkEmailAccount] Exception Thrown: " + e);
            e.printStackTrace();
        }
        return emailFlag;
    }

    /**
	this method save email data to draft folder
	which contains data of message
	*/
    public int saveDraft(int userId, MailMessage mailmessage) throws AuthorizationFailedException {
        if (!CVUtility.isModuleVisible("Email", userId, this.dataSource)) throw new AuthorizationFailedException("Email- saveDraft");
        int messageid = 0;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            SaveDraftLocalHome home = (SaveDraftLocalHome) ic.lookup("local/SaveDraft");
            SaveDraftLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            messageid = remote.saveDraft(userId, mailmessage);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.saveDraft] Exception Thrown: " + e);
            e.printStackTrace();
        }
        return messageid;
    }

    /**
	this method edit email data to draft folder
	which contains data of message
	*/
    public int editDraft(int userId, MailMessage mailmessage) throws AuthorizationFailedException {
        if (!CVUtility.isModuleVisible("Email", userId, this.dataSource)) throw new AuthorizationFailedException("Email- editDraft");
        int messageid = 0;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            SaveDraftLocalHome home = (SaveDraftLocalHome) ic.lookup("local/SaveDraft");
            SaveDraftLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            messageid = remote.editDraft(userId, mailmessage);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.editDraft] Exception Thrown: " + e);
            e.printStackTrace();
        }
        return messageid;
    }

    /**
	this method returns list of folders
	*/
    public FolderList getFolderList(int userId) throws AuthorizationFailedException {
        if (!CVUtility.isModuleVisible("Email", userId, this.dataSource)) throw new AuthorizationFailedException("Email- getFolderList");
        FolderList folderList = null;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            ManageFolderLocalHome home = (ManageFolderLocalHome) ic.lookup("local/ManageFolder");
            ManageFolderLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            folderList = (FolderList) remote.getFolderList(userId);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.getFolderList] Exception Thrown: " + e);
            e.printStackTrace();
            return null;
        }
        return folderList;
    }

    /**
	this method adds folder
	*/
    public int addFolder(int userId, HashMap preference) throws AuthorizationFailedException {
        if (!CVUtility.isModuleVisible("Email", userId, this.dataSource)) throw new AuthorizationFailedException("Email- addFolder");
        int i = 0;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            ManageFolderLocalHome home = (ManageFolderLocalHome) ic.lookup("local/ManageFolder");
            ManageFolderLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            i = remote.addFolder(userId, preference);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.addFolder] Exception Thrown: " + e);
            e.printStackTrace();
            return 0;
        }
        return i;
    }

    /**
	* This method checks the folder is already present
	*/
    public int editFolder(int userId, HashMap preference) throws AuthorizationFailedException {
        if (!CVUtility.isModuleVisible("Email", userId, this.dataSource)) throw new AuthorizationFailedException("Email- editFolder");
        int i = 0;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            ManageFolderLocalHome home = (ManageFolderLocalHome) ic.lookup("local/ManageFolder");
            ManageFolderLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            i = remote.editFolder(userId, preference);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.editFolder] Exception Thrown: " + e);
            e.printStackTrace();
            return 0;
        }
        return i;
    }

    /**
   * Checks if the folder is already present
   */
    public int checkFoldersPresence(int userId, HashMap preference) throws AuthorizationFailedException {
        if (!CVUtility.isModuleVisible("Email", userId, this.dataSource)) {
            throw new AuthorizationFailedException("Email- checkFoldersPresence");
        }
        int i = 0;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            ManageFolderLocalHome home = (ManageFolderLocalHome) ic.lookup("local/ManageFolder");
            ManageFolderLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            i = remote.checkFoldersPresence(userId, preference);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.checkFoldersPresence] Exception Thrown: " + e);
            e.printStackTrace();
            return 0;
        }
        return i;
    }

    /**
	this method returns rule list
	*/
    public RuleList getRuleList(int userId, HashMap preference) throws AuthorizationFailedException {
        if (!CVUtility.isModuleVisible("Email", userId, this.dataSource)) throw new AuthorizationFailedException("Email- getRuleList");
        RuleList rulelist = null;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            EmailLocalHome home = (EmailLocalHome) ic.lookup("local/EmailList");
            EmailLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            rulelist = (RuleList) remote.getRuleList(userId, preference);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.getRuleList] Exception Thrown: " + e);
            e.printStackTrace();
            return null;
        }
        return rulelist;
    }

    /**
	this method delete rule
	*/
    public int deleteRule(int userid, int elementid) throws AuthorizationFailedException {
        if (!CVUtility.isModuleVisible("Email", userid, this.dataSource)) throw new AuthorizationFailedException("Email- deleteRule");
        int status = 0;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            EmailLocalHome home = (EmailLocalHome) ic.lookup("local/EmailList");
            EmailLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            status = remote.deleteRule(userid, elementid);
        } catch (CreateException ce) {
            throw new EJBException(ce);
        } catch (NamingException ce) {
            throw new EJBException(ce);
        }
        return status;
    }

    /**
	this method enableordisableRule
	*/
    public int enableordisableRule(int userid, int elementid, boolean status) throws AuthorizationFailedException {
        if (!CVUtility.isModuleVisible("Email", userid, this.dataSource)) throw new AuthorizationFailedException("Email- enablediableRule");
        int flag = 0;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            EmailLocalHome home = (EmailLocalHome) ic.lookup("local/EmailList");
            EmailLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            flag = remote.enableordisableRule(userid, elementid, status);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.enableordisableRule] Exception Thrown: " + e);
            e.printStackTrace();
            return 0;
        }
        return flag;
    }

    /**
   * this method create new email account
   */
    public int createNewEmailAccount(int userId, MailAccount mailaccount) throws AuthorizationFailedException {
        if (!CVUtility.isModuleVisible("Email", userId, this.dataSource)) {
            throw new AuthorizationFailedException("Email- createNewEmailAccount");
        }
        int mailmessage = 0;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            GetMailLocalHome home = (GetMailLocalHome) ic.lookup("local/GetMail");
            GetMailLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            mailmessage = remote.createNewEmailAccount(userId, mailaccount);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.createNewEmailAccount] Exception Thrown: " + e);
            e.printStackTrace();
            return 0;
        }
        return mailmessage;
    }

    /**
	this method edit email account
	*/
    public int editEmailAccount(int userId, MailAccount mailaccount) throws AuthorizationFailedException {
        if (!CVUtility.isModuleVisible("Email", userId, this.dataSource)) throw new AuthorizationFailedException("Email- editEmailAccount");
        int mailmessage = 0;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            GetMailLocalHome home = (GetMailLocalHome) ic.lookup("local/GetMail");
            GetMailLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            mailmessage = remote.editEmailAccount(userId, mailaccount);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.editEmailAccount] Exception Thrown: " + e);
            e.printStackTrace();
            return 0;
        }
        return mailmessage;
    }

    /**
	this method delete email account
	*/
    public int deleteEmailAccount(int userId, int AccountID) throws AuthorizationFailedException {
        if (!CVUtility.isModuleVisible("Email", userId, this.dataSource)) throw new AuthorizationFailedException("Email- deleteEmailAccount");
        int mailmessage = 0;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            GetMailLocalHome home = (GetMailLocalHome) ic.lookup("local/GetMail");
            GetMailLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            mailmessage = remote.deleteEmailAccount(userId, AccountID);
        } catch (CreateException ce) {
            throw new EJBException(ce);
        } catch (NamingException ce) {
            throw new EJBException(ce);
        }
        return mailmessage;
    }

    /**
	this method getAccountDetails
	*/
    public MailAccount getAccountDetails(int userid, HashMap preference) throws AuthorizationFailedException {
        if (!CVUtility.isModuleVisible("Email", userid, this.dataSource)) throw new AuthorizationFailedException("Email- getAccountDetails");
        MailAccount mailaccount = null;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            GetMailLocalHome home = (GetMailLocalHome) ic.lookup("local/GetMail");
            GetMailLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            mailaccount = remote.getAccountDetails(userid, preference);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.getAccountDetails] Exception Thrown: " + e);
            e.printStackTrace();
            return null;
        }
        return mailaccount;
    }

    public boolean sendMailMessage(int userId, MailMessage mailmessage) throws AuthorizationFailedException {
        boolean result = false;
        if (!CVUtility.isModuleVisible("Email", userId, this.dataSource)) {
            throw new AuthorizationFailedException("Email- sendEmailMessage");
        }
        try {
            InitialContext ic = CVUtility.getInitialContext();
            SendMailLocalHome home = (SendMailLocalHome) ic.lookup("local/SendMail");
            SendMailLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            result = remote.sendMailMessage(userId, mailmessage);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.sendMailMessage] Exception Thrown: " + e);
            e.printStackTrace();
        }
        return (result);
    }

    /**
         * this method send forgotten password to user
         * @param mailmessage MailMessage
         * @author : Valery Kasinski
         */
    public void sendForgottenPasswordMail(MailMessage mailmessage) {
        try {
            InitialContext ic = CVUtility.getInitialContext();
            SendMailLocalHome home = (SendMailLocalHome) ic.lookup("local/SendMail");
            SendMailLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            remote.sendForgottenPasswordMail(mailmessage);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.sendForgottenPasswordMail] Exception Thrown: " + e);
            e.printStackTrace();
        }
    }

    public void mailDeliverMessage(HashMap userId) {
        try {
            HashMap accountInfo = (HashMap) userId.get("account");
            if (accountInfo != null) {
                int userID = ((Long) accountInfo.get("owner")).intValue();
                if (!CVUtility.isModuleVisible("Email", userID, this.dataSource)) throw new AuthorizationFailedException("Email- sendEmailMessage");
            }
            accountInfo = null;
            InitialContext ic = CVUtility.getInitialContext();
            MailDeliverLocalHome home = (MailDeliverLocalHome) ic.lookup("local/MailDeliver");
            MailDeliverLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            remote.mailDeliverMessage(userId);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.mailDeliverMessage] Exception Thrown: " + e);
            e.printStackTrace();
        }
    }

    /**
	* Moves email into particular folder.
	* delegates to PreparedStatement's setBoolean
	* @param	int	   sourceId  Source Id of the folder.
	* @param	int    destId    Destination Id of the folder.
	* @param	string mailIdList Mail ID List.
	*/
    public int emailMoveTo(int sourceId, int destId, String mailIdList[]) {
        int result = 0;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            EmailManageLocalHome home = (EmailManageLocalHome) ic.lookup("local/EmailManage");
            EmailManageLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            result = remote.emailMoveTo(sourceId, destId, mailIdList);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.emailMoveTo] Exception Thrown: " + e);
            e.printStackTrace();
            return 1;
        }
        return result;
    }

    /**
	* Get email's Sender information from particular message.
	* @param	string mailIdList Mail ID List.
	*/
    public ArrayList getEmailsFrom(String mailIdList[]) {
        ArrayList emailsList = new ArrayList();
        try {
            InitialContext ic = CVUtility.getInitialContext();
            EmailManageLocalHome home = (EmailManageLocalHome) ic.lookup("local/EmailManage");
            EmailManageLocal local = home.create();
            local.setDataSource(this.dataSource);
            emailsList = local.getEmailsFrom(mailIdList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emailsList;
    }

    /**
	* Delete email's from particular folder.
	* @param	int	   sourceId  Source Id of the folder.
	* @param	int    trashfolderId  Id of trash folder.
	* @param	string mailIdList Mail ID List.
	*/
    public int emailDelete(int sourceId, int accountId, String mailIdList[]) {
        int result = 0;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            EmailManageLocalHome home = (EmailManageLocalHome) ic.lookup("local/EmailManage");
            EmailManageLocal local = home.create();
            local.setDataSource(this.dataSource);
            result = local.emailDelete(sourceId, accountId, mailIdList);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.emailDelete] Exception Thrown: " + e);
            e.printStackTrace();
            return 1;
        }
        return result;
    }

    /**
	* Marks email as Read
	* @param	int    sourceId  Id of source folder.
	* @param	int    readflag  Flag to indicate
	*				   read/unread
	* @param	string mailIdList Mail ID List.
	*/
    public int emailMarkasRead(int sourceId, int readflag, String mailIdList[]) {
        int result = 0;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            EmailManageLocalHome home = (EmailManageLocalHome) ic.lookup("local/EmailManage");
            EmailManageLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            result = remote.emailMarkasRead(sourceId, readflag, mailIdList);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.emailMarkasRead] Exception Thrown: " + e);
            e.printStackTrace();
            return 1;
        }
        return result;
    }

    /**
    * Remove Folder
    * @param	int	   sourceId  Source Id of the folder.
    * @param	int    trashfolderId  Id of trash folder.
    */
    public int removeFolder(int sourceId, int trashfolderId) {
        int result = 0;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            ManageFolderLocalHome home = (ManageFolderLocalHome) ic.lookup("local/ManageFolder");
            ManageFolderLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            result = remote.removeFolder(sourceId, trashfolderId);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.removeFolder] Exception Thrown: " + e);
            e.printStackTrace();
            return 1;
        }
        return result;
    }

    /**
	* gets rule details for particular rule.
	* @param	int	   ruleid  Rule Id for getting details.
	*/
    public RuleDetails getRuleDetails(int ruleid) {
        RuleDetails ruleList = null;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            RulesManageLocalHome home = (RulesManageLocalHome) ic.lookup("local/RulesManage");
            com.centraview.email.rules.RulesManageLocal local = home.create();
            local.setDataSource(this.dataSource);
            ruleList = (RuleDetails) local.getRuleDetails(ruleid);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.getRuleDetails] Exception Thrown: " + e);
            e.printStackTrace();
            return null;
        }
        return ruleList;
    }

    /**
    * Get ruleId for the JunkMail ruleid.
    * @param	int accountid.
    */
    public int getRuleId(int accountid) {
        int result = 0;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            RulesManageLocalHome home = (RulesManageLocalHome) ic.lookup("local/RulesManage");
            RulesManageLocal local = home.create();
            local.setDataSource(this.dataSource);
            result = local.getRuleId(accountid);
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        return result;
    }

    /**
	* Adds Rule in rules related tables
	* @param	HashMap containing all the fields of add rule
	*/
    public int addRule(HashMap preference) {
        int result = 0;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            RulesManageLocalHome home = (RulesManageLocalHome) ic.lookup("local/RulesManage");
            RulesManageLocal local = home.create();
            local.setDataSource(this.dataSource);
            result = local.addRule(preference);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.addRule] Exception Thrown: " + e);
            e.printStackTrace();
            return 1;
        }
        return result;
    }

    /**
	* edit Rule in rules related tables
	* @param	HashMap containing all the fields of edit rule
	*/
    public int editRule(HashMap preference) {
        int result = 0;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            RulesManageLocalHome home = (RulesManageLocalHome) ic.lookup("local/RulesManage");
            RulesManageLocal local = home.create();
            local.setDataSource(this.dataSource);
            result = local.editRule(preference);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.editRule] Exception Thrown: " + e);
            e.printStackTrace();
            return 1;
        }
        return result;
    }

    /**
	* delete Rule in rules related tables
	* @param	HashMap containing rule id of delete rule.
	*/
    public int deleteRule(HashMap preference) {
        int result = 0;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            RulesManageLocalHome home = (RulesManageLocalHome) ic.lookup("local/RulesManage");
            RulesManageLocal local = home.create();
            local.setDataSource(this.dataSource);
            result = local.deleteRule(preference);
        } catch (CreateException ce) {
            throw new EJBException(ce);
        } catch (NamingException ce) {
            throw new EJBException(ce);
        }
        return result;
    }

    public void updateHeader(int userId, int messageId, String headerName, String headerValue) throws AuthorizationFailedException {
        if (!CVUtility.isModuleVisible("Email", userId, this.dataSource)) throw new AuthorizationFailedException("Email- updateHeader");
        try {
            InitialContext ic = CVUtility.getInitialContext();
            SendMailLocalHome home = (SendMailLocalHome) ic.lookup("local/SendMail");
            SendMailLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            remote.updateHeader(userId, messageId, headerName, headerValue);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.updateHeader] Exception Thrown: " + e);
            e.printStackTrace();
        }
    }

    public int addEmail(HashMap messageData) throws AuthorizationFailedException {
        HashMap accountInfo = (HashMap) messageData.get("account");
        if (accountInfo != null) {
            int userID = ((Number) accountInfo.get("owner")).intValue();
            if (!CVUtility.isModuleVisible("Email", userID, this.dataSource)) throw new AuthorizationFailedException("Email- sendEmailMessage");
        }
        accountInfo = null;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            com.centraview.email.syncemail.SyncMailLocalHome home = (com.centraview.email.syncemail.SyncMailLocalHome) ic.lookup("local/SyncMail");
            com.centraview.email.syncemail.SyncMailLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            return remote.addEmail(messageData);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.addEmail] Exception Thrown: " + e);
            return (0);
        }
    }

    public boolean editEmailFunction(HashMap editDetails) throws AuthorizationFailedException {
        if ((Long) editDetails.get("owner") != null) {
            int userID = ((Number) editDetails.get("owner")).intValue();
            if (!CVUtility.isModuleVisible("Email", userID, this.dataSource)) throw new AuthorizationFailedException("Email- editEmailFunction");
        }
        try {
            InitialContext ic = CVUtility.getInitialContext();
            SyncMailLocalHome home = (SyncMailLocalHome) ic.lookup("local/SyncMail");
            SyncMailLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            return (remote.editEmailFunction(editDetails));
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean.editEmailFunction] Exception Thrown: " + e);
            return false;
        }
    }

    /**
   * Returns a String object to be used when calling
   * ListGenerator.getEmailList() to obtain a list of emails
   * related to a given entity or individual. This method is used by the
   * related info frame for Entity and Individual detail. It will
   * select all email messages To: or From: any of the individuals
   * associated with the entity (when contactType==1) or the given
   * individual (when contactType==2).
   *
   * @param   individualID  The individualID of the user who is
   * attempting to view this data.
   * @param   contactID     The contactID for which we are selected
   * related emails. This is either an entityID or and individualID.
   * @param   contactType   If 1, then contactID is an entityID. If
   * 2, then contactID is an individualID.
   * @param   startAt       The beginning offset of the LIMIT clause
   * on the SQL statement.
   * @param   endAt         The end offset of the LIMIT clause on the
   * SQL statement (startAt + recordsPerPage)
   * @param   sortMem       The field on which to sort results.
   * @param   sortType      Ascending or Descending ("DESC")
   * @return    EmailList object containing the results of the query.
   */
    public String getRelatedEmailList(int individualID, int contactID, int contactType) throws AuthorizationFailedException {
        if (!CVUtility.isModuleVisible("Email", individualID, this.dataSource)) throw new AuthorizationFailedException("Email- getRelatedEmailList");
        String filterString = "";
        try {
            CVDal db = new CVDal(dataSource);
            Vector individualList = new Vector();
            ArrayList contentList = new ArrayList();
            if (contactType == 1) {
                String sql = " SELECT moc.Content FROM entity e " + " LEFT JOIN mocrelate mr ON (e.EntityID=mr.ContactID) " + " LEFT JOIN methodofcontact moc ON (mr.MOCID=moc.MOCID AND moc.MOCType = 1) " + " WHERE e.EntityID = " + contactID;
                db.setSqlQuery(sql);
                Collection results = db.executeQuery();
                db.clearParameters();
                StringBuffer searchString = new StringBuffer("ADVANCED-RELATED: emailmessage.messageid IN (");
                if (results != null && results.size() != 0) {
                    Iterator addressIter = results.iterator();
                    while (addressIter.hasNext()) {
                        HashMap addressMap = (HashMap) addressIter.next();
                        String address = (String) addressMap.get("Content");
                        if (address != null) {
                            if (!contentList.contains(address)) {
                                contentList.add(address);
                            }
                        }
                    }
                }
                sql = "SELECT IndividualID FROM individual WHERE Entity=" + contactID;
                db.setSqlQueryToNull();
                db.setSqlQuery(sql);
                results = null;
                results = db.executeQuery();
                db.clearParameters();
                Iterator iter = results.iterator();
                while (iter.hasNext()) {
                    HashMap row = (HashMap) iter.next();
                    int individualIDx = ((Number) row.get("IndividualID")).intValue();
                    individualList.add(new Integer(individualIDx));
                }
            } else if (contactType == 2) {
                individualList.add(new Integer(contactID));
            }
            StringBuffer inClause = new StringBuffer(" IN (");
            if (individualList != null && individualList.size() != 0) {
                Iterator indivIter = individualList.iterator();
                while (indivIter.hasNext()) {
                    Integer indID = (Integer) indivIter.next();
                    inClause.append(indID.toString() + ", ");
                }
            }
            inClause.append("-1) ");
            String sql = " SELECT moc.Content FROM individual i " + " LEFT JOIN mocrelate mr ON (i.IndividualID=mr.ContactID) " + " LEFT JOIN methodofcontact moc ON (mr.MOCID=moc.MOCID AND moc.MOCType = 1) " + " WHERE i.IndividualID " + inClause.toString();
            db.setSqlQuery(sql);
            Collection results = db.executeQuery();
            db.clearParameters();
            StringBuffer searchString = new StringBuffer("SELECT emailmessage.messageid FROM emailmessage WHERE emailmessage.messageid IN (");
            if (results != null && results.size() != 0) {
                Iterator addressIter = results.iterator();
                while (addressIter.hasNext()) {
                    HashMap addressMap = (HashMap) addressIter.next();
                    String address = (String) addressMap.get("Content");
                    if (address != null) {
                        if (!contentList.contains(address)) {
                            contentList.add(address);
                        }
                    }
                }
            }
            for (int i = 0; i < contentList.size(); i++) {
                String emailAddress = (String) contentList.get(i);
                if (emailAddress != null && !emailAddress.equals("null")) {
                    String sqlMessages = " SELECT MessageID FROM emailrecipient WHERE Address Like '%" + emailAddress + "%' UNION" + " SELECT MessageID FROM emailmessage WHERE MailFrom Like '%" + emailAddress + "%' UNION" + " SELECT MessageID FROM emailmessage WHERE ReplyTo Like '%" + emailAddress + "%' group By MessageID ";
                    db.setSqlQuery(sqlMessages);
                    Collection resultMessages = db.executeQuery();
                    db.clearParameters();
                    if (resultMessages != null && resultMessages.size() != 0) {
                        Iterator messageIter = resultMessages.iterator();
                        while (messageIter.hasNext()) {
                            HashMap messageMap = (HashMap) messageIter.next();
                            Object messageIDObject = messageMap.get("MessageID");
                            if (messageIDObject != null) {
                                String messageID = messageIDObject.toString();
                                searchString.append(messageID + ", ");
                            }
                        }
                    }
                }
            }
            searchString.append("-1)");
            filterString = searchString.toString();
        } catch (Exception e) {
            logger.error("[getRelatedEmailList] Exception thrown.", e);
        }
        return (filterString);
    }

    /**
   * Interface to EmailHelperEJB.getSystemEmailInfo() which returns
   * a HashMap representation of the information needed
   * to send a particular system email (ie: Forgot Password email)
   * for the given System Email ID.
   * @param systemEmailID The ID of the system email which we need
   * information for. Use one of the correct constants found in
   * com.centraview.administration.common.AdminConstantKeys
   */
    public HashMap getSystemEmailInfo(int systemEmailID) throws RemoteException {
        HashMap emailInfo = new HashMap();
        try {
            InitialContext ic = CVUtility.getInitialContext();
            EmailHelperLocalHome home = (EmailHelperLocalHome) ic.lookup("local/EmailHelper");
            EmailHelperLocal remote = (EmailHelperLocal) home.create();
            remote.setDataSource(this.dataSource);
            emailInfo = (HashMap) remote.getSystemEmailInfo(systemEmailID);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean] Exception thrown in getSystemEmailInfo(): " + e);
            e.printStackTrace();
        }
        return (emailInfo);
    }

    /**
   * Sends an email that is generated from the given
   * MailMessage object. This method does not do anything
   * other than send the email. All code in CentraView
   * should call this method for the transmission of email.
   * Any information required to send a valid email is
   * required to be passed to this method via the mailmessage
   * parameter, including To:, From:, ReplyTo:, Subject:,
   * Body, Attachments, etc.
   * @param mailmessage The MailMessage
   * @return boolean True for success, False for failure
   */
    public boolean sendMailMessageBasic(MailMessage mailMessage) throws RemoteException {
        boolean returnValue = false;
        try {
            InitialContext ic = CVUtility.getInitialContext();
            SendMailLocalHome home = (SendMailLocalHome) ic.lookup("local/SendMail");
            SendMailLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            returnValue = (boolean) remote.sendMailMessageBasic(mailMessage);
        } catch (Exception e) {
            System.out.println("[Exception][EmailFacadeBean] Exception thrown in sendMailMessageBasic(): " + e);
            e.printStackTrace();
        }
        return (returnValue);
    }

    /**
   * Returns a list of emails for display in the Related Info
   * Email module. Will return all emails which have a To: or
   * From: field which matches <strong>any</strong> address
   * related to the given <em>entityID</em>, including all
   * addresses related to any Individual related to the Entity.
   * @param searchCondition The searchCondition of Entity Or Individuals for which we are retreiving related emails.
   * @param individualID The individualID of the user retreiving the list.
   * @return EmailList object (DisplayList) containing records found.
   */
    public EmailList getRelatedEmailList(HashMap searchCondition, int individualID) {
        EmailList emailList = new EmailList();
        try {
            InitialContext ic = CVUtility.getInitialContext();
            EmailLocalHome home = (EmailLocalHome) ic.lookup("local/EmailList");
            EmailLocal remote = home.create();
            remote.setDataSource(this.dataSource);
            emailList = (EmailList) remote.getRelatedEmailList(searchCondition, individualID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (emailList);
    }

    /**
	 * This simply sets the target datasource to be used for DB interaction
	 * @param ds A string that contains the cannonical JNDI name of the datasource
	 * @author Kevin McAllister <kevin@centraview.com>
	 */
    public void setDataSource(String ds) {
        this.dataSource = ds;
    }
}
