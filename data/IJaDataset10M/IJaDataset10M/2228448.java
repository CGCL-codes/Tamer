package com.dcivision.mail.core;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import com.dcivision.dms.bean.EmailMessageToRule;
import com.dcivision.framework.DataSourceFactory;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.SystemParameterConstant;
import com.dcivision.framework.SystemParameterFactory;
import com.dcivision.framework.TextUtility;
import com.dcivision.framework.Utility;
import com.dcivision.mail.bean.EmailMessageImpl;
import com.dcivision.rules.core.RuleManager;
import com.dcivision.user.bean.UserGroup;
import com.dcivision.user.bean.UserRecord;
import com.dcivision.user.dao.UserGroupDAObject;
import com.dcivision.user.dao.UserRecordDAObject;
import com.dcivision.user.dao.UserRoleDAObject;

public class EmailSolutionHandler implements Job {

    protected Log log = LogFactory.getLog(this.getClass().getName());

    private SessionContainer sessionContainer = null;

    /**
   * 
   */
    public void setEmailSolutionHandlerScheduleJob() {
        boolean enableEmailSolutionHandler = SystemParameterFactory.getSystemParameterBoolean(SystemParameterConstant.DMS_ENABLE_EMAILARCHIVE_AUTO_TRACKER);
        String timeInterval = SystemParameterFactory.getSystemParameter(SystemParameterConstant.DMS_EMAILARCHIVE_AUTO_TRACKER_TIME_INTERVAL);
        String mailServerType = SystemParameterFactory.getSystemParameter(SystemParameterConstant.DMS_EMAILARCHIVE_AUTO_TRACKER_MAIL_SERVERTYPE);
        int actionType = 0;
        long triggerInterval = 0;
        if (!Utility.isEmpty(timeInterval)) {
            triggerInterval = TextUtility.parseInteger(timeInterval) * 60 * 1000;
        }
        org.quartz.SchedulerFactory factory = new StdSchedulerFactory();
        try {
            actionType = 1;
            Scheduler sched = factory.getScheduler();
            String emailSolutionHandleJobName = "Email_Solution_Handler_JOB";
            String emailSolutionHandleJobGroupName = "Email_Solution_Handler_JOBGROUP";
            String emailSolutionHandleTRIGGGERName = "Email_Solution_Handler_TRIGGER";
            String[] triggerGroups;
            String[] triggers;
            boolean jobIsRegist = false;
            triggerGroups = sched.getTriggerGroupNames();
            for (int i = 0; i < triggerGroups.length; i++) {
                triggers = sched.getTriggerNames(triggerGroups[i]);
                for (int j = 0; j < triggers.length; j++) {
                    Trigger tg = sched.getTrigger(triggers[j], triggerGroups[i]);
                    if (tg instanceof SimpleTrigger && tg.getName().equals(emailSolutionHandleTRIGGGERName)) {
                        log.debug(" tg.getName() : " + tg.getName());
                        log.debug(" emailSolutionHandleJobName : " + emailSolutionHandleTRIGGGERName);
                        jobIsRegist = true;
                        if (!enableEmailSolutionHandler || (Utility.isEmpty(mailServerType) && triggerInterval > 0)) {
                            sched.unscheduleJob(triggers[j], triggerGroups[i]);
                            log.debug("Cancel the schedule job for email solution handler!");
                        }
                    }
                }
            }
            if (enableEmailSolutionHandler && !(Utility.isEmpty(mailServerType) && triggerInterval > 0) && !jobIsRegist) {
                actionType = 2;
                Timestamp curTime = Utility.getCurrentTimestamp();
                Calendar triggerTime = Utility.timestampToCalendar(curTime);
                log.debug("**************** trigger Interval: " + triggerInterval / 1000 + " seconds.");
                JobDetail jobDetail = new org.quartz.JobDetail("Email_Solution_Handler_JOB", "Email_Solution_Handler_JOBGROUP", EmailSolutionHandler.class);
                Trigger trigger = new SimpleTrigger("Email_Solution_Handler_TRIGGER", "Email_Solution_Handler_TRIGGERGROUP", triggerTime.getTime(), null, SimpleTrigger.REPEAT_INDEFINITELY, triggerInterval);
                sched.scheduleJob(jobDetail, trigger);
                log.debug("Register and start the schedule job for email solution handler!");
            }
        } catch (SchedulerException e) {
            log.error(e, e);
            if (actionType == 1) {
                log.error("Cancel the schedule job for email solution handler is faild!");
            } else if (actionType == 2) {
                log.error("Register and  start the schedule job for email solution handler is faild!");
            }
            log.error(e);
        }
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        sessionContainer = new SessionContainer();
        UserRecord tmpUserRecord = new UserRecord();
        tmpUserRecord.setID(new Integer(0));
        sessionContainer.setUserRecord(tmpUserRecord);
        List mailMessageList = getEmailMessgeList();
        processMailListAndPutToRuleEngine(mailMessageList);
    }

    private List getEmailMessgeList() {
        EmailReceiver mailReceiver = null;
        String incomingServerHost = "";
        String incomingServerLoginName = "";
        String incomingServerPassword = "";
        String mailServerType = "";
        List emailMessageList = null;
        try {
            incomingServerHost = SystemParameterFactory.getSystemParameter(SystemParameterConstant.DMS_EMAILARCHIVE_AUTO_TRACKER_INCOMING_SERVER_HOST);
            incomingServerLoginName = SystemParameterFactory.getSystemParameter(SystemParameterConstant.DMS_EMAILARCHIVE_AUTO_TRACKER_INCOMING_SERVER_LOGINNAME);
            incomingServerPassword = SystemParameterFactory.getSystemParameter(SystemParameterConstant.DMS_EMAILARCHIVE_AUTO_TRACKER_INCOMING_SERVER_PASSWORD);
            mailServerType = SystemParameterFactory.getSystemParameter(SystemParameterConstant.DMS_EMAILARCHIVE_AUTO_TRACKER_MAIL_SERVERTYPE);
            mailReceiver = EmailReceiverFactory.getEmailReceiverInstanceByServerType(sessionContainer, mailServerType);
            emailMessageList = new ArrayList();
            if (!Utility.isEmpty(incomingServerHost) && !Utility.isEmpty(mailServerType)) {
                emailMessageList = mailReceiver.loadAndProcessMailList(incomingServerHost, incomingServerLoginName, incomingServerPassword, mailServerType);
            }
        } catch (Exception e) {
            log.debug("Receive email error!");
            log.error(e, e);
        }
        return emailMessageList;
    }

    private void processMailListAndPutToRuleEngine(List emailMessageList) {
        Connection connection = null;
        UserRecordDAObject userRecordDAObject = null;
        RuleManager ruleManager = null;
        EmailMessageToRule emailMessageToRule = new EmailMessageToRule();
        List allUser = new ArrayList();
        try {
            if (Utility.isEmpty(emailMessageList)) {
                log.debug("emailMessageList is empty!");
                return;
            } else {
                connection = DataSourceFactory.getConnection();
                userRecordDAObject = new UserRecordDAObject(sessionContainer, connection);
                ruleManager = new RuleManager(sessionContainer, connection);
                for (int i = 0; i < emailMessageList.size(); i++) {
                    long startSubTime = 0;
                    startSubTime = new java.util.Date().getTime();
                    log.debug("**********************Process email :number is :" + (i + 1) + " .");
                    EmailMessageImpl emlEmailMessage = (EmailMessageImpl) emailMessageList.get(i);
                    try {
                        if (emlEmailMessage != null) {
                            emailMessageToRule.setEmailMessageData(emlEmailMessage);
                            String allEmailAddressUserID = "";
                            String allEmailAddress = "";
                            String senderUserIDStr = "";
                            String toUserIDStr = "";
                            String ccUserIDStr = "";
                            String fromEmailAddress = "";
                            String toEmailAddresses = "";
                            String ccEmailAddresses = "";
                            String[] toEmailAddressesArr;
                            String[] ccEmailAddressesArr;
                            fromEmailAddress = EmailReceiverImpl.getEmailAdressStr(emlEmailMessage.getSender());
                            toEmailAddresses = EmailReceiverImpl.getEmailAdressStr(emlEmailMessage.getTo());
                            ccEmailAddresses = EmailReceiverImpl.getEmailAdressStr(emlEmailMessage.getCc());
                            if (!Utility.isEmpty(fromEmailAddress)) {
                                UserRecord tmpUserRecord = (UserRecord) userRecordDAObject.getUserByEmailAddress(fromEmailAddress);
                                if (tmpUserRecord != null) {
                                    String tmpStr = getTheUserIDGroupIDRoleIDStrByUserID(tmpUserRecord.getID(), connection);
                                    if (!allUser.contains(tmpUserRecord)) {
                                        if (!Utility.isEmpty(tmpStr)) {
                                            senderUserIDStr += tmpStr + ",";
                                        }
                                        allUser.add(tmpUserRecord);
                                        allEmailAddressUserID += tmpUserRecord.getID().toString() + ",";
                                        allEmailAddress += fromEmailAddress + ",";
                                    }
                                }
                            }
                            if (!Utility.isEmpty(toEmailAddresses)) {
                                toEmailAddressesArr = TextUtility.splitString(toEmailAddresses, ",");
                                for (int addrNum = 0; addrNum < toEmailAddressesArr.length; addrNum++) {
                                    UserRecord tmpUserRecord = (UserRecord) userRecordDAObject.getUserByEmailAddress(toEmailAddressesArr[addrNum]);
                                    if (tmpUserRecord != null) {
                                        String tmpStr = getTheUserIDGroupIDRoleIDStrByUserID(tmpUserRecord.getID(), connection);
                                        if (!allUser.contains(tmpUserRecord)) {
                                            if (!Utility.isEmpty(tmpStr)) {
                                                toUserIDStr += tmpStr + ",";
                                            }
                                            allUser.add(tmpUserRecord);
                                            allEmailAddressUserID += tmpUserRecord.getID().toString() + ",";
                                            allEmailAddress += toEmailAddressesArr[addrNum] + ",";
                                        }
                                    }
                                }
                            }
                            if (!Utility.isEmpty(ccEmailAddresses)) {
                                ccEmailAddressesArr = TextUtility.splitString(ccEmailAddresses, ",");
                                for (int addrNum = 0; addrNum < ccEmailAddressesArr.length; addrNum++) {
                                    UserRecord tmpUserRecord = (UserRecord) userRecordDAObject.getUserByEmailAddress(ccEmailAddressesArr[addrNum]);
                                    if (tmpUserRecord != null) {
                                        String tmpStr = getTheUserIDGroupIDRoleIDStrByUserID(tmpUserRecord.getID(), connection);
                                        if (!allUser.contains(tmpUserRecord)) {
                                            if (!Utility.isEmpty(tmpStr)) {
                                                ccUserIDStr += tmpStr + ",";
                                            }
                                            allUser.add(tmpUserRecord);
                                            allEmailAddressUserID += tmpUserRecord.getID().toString() + ",";
                                            allEmailAddress += ccEmailAddressesArr[addrNum] + ",";
                                        }
                                    }
                                }
                            }
                            emailMessageToRule.setSenderEmailAddress(fromEmailAddress);
                            emailMessageToRule.setToEmailAddress(toEmailAddresses);
                            emailMessageToRule.setCcEmailAddress(ccEmailAddresses);
                            emailMessageToRule.setSenderUserIDStr(senderUserIDStr);
                            emailMessageToRule.setToUserIDStr(toUserIDStr);
                            emailMessageToRule.setCcUserIDStr(ccUserIDStr);
                            emailMessageToRule.setEmailAllEmailAddress(allEmailAddress);
                            if (!Utility.isEmpty(allEmailAddressUserID)) {
                                allEmailAddressUserID = allEmailAddressUserID.substring(0, allEmailAddressUserID.lastIndexOf(","));
                                emailMessageToRule.setEmailAllUserIDStr(allEmailAddressUserID);
                                emailMessageToRule.setIsPrivaeteRule(false);
                                log.debug("/*****Call Public Rule Start***********/");
                                ruleManager.performEmailArchiveRule(emailMessageToRule);
                                log.debug("/*****Call Public Rule End***********/");
                                for (int j = 0; j < allUser.size(); j++) {
                                    UserRecord tmpUserRecord = (UserRecord) allUser.get(j);
                                    if (tmpUserRecord != null) {
                                        emailMessageToRule.setMessgeUserID(tmpUserRecord.getID());
                                        emailMessageToRule.setIsPrivaeteRule(true);
                                        log.debug("/*****Call Private Rule  Start***********/");
                                        ruleManager.performEmailArchiveRule(emailMessageToRule);
                                        log.debug("/*****Call Private Rule  End***********/");
                                    }
                                }
                            } else {
                                log.debug("**********************Email address have not mapping user id ");
                            }
                        }
                    } catch (Exception e) {
                        log.debug("process email << " + emlEmailMessage.getSubject() + " >> faild!");
                        log.error(e, e);
                    }
                    long endSubTime = 0;
                    endSubTime = new java.util.Date().getTime();
                    log.debug("**********************Process email :number is :" + (i + 1) + " need time :" + (endSubTime - startSubTime) + "ms.");
                }
            }
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            try {
                connection.close();
            } catch (Exception ignore) {
            } finally {
                connection = null;
            }
        }
    }

    public String getTheUserIDGroupIDRoleIDStrByUserID(Integer userID, Connection connection) {
        String userIDsymbol = "U:";
        String groupIDsymbol = "G:";
        String roleIDsymbol = "R:";
        String userIDGroupIDRoleIDStr = userIDsymbol + userID;
        try {
            UserGroupDAObject userGroupDAO = new UserGroupDAObject(this.sessionContainer, connection);
            List listGroup = userGroupDAO.getListByUserRecordIDGroupType(userID, UserGroup.GROUP_TYPE_PUBLIC);
            Integer[] iaGroupID = (Integer[]) Utility.getPropertyArray(listGroup, "ID", new Integer[0]);
            String[] saGroupID = Utility.getStringArray(iaGroupID);
            if (!Utility.isEmpty(saGroupID)) {
                for (int i = 0; i < saGroupID.length; i++) {
                    userIDGroupIDRoleIDStr += "," + groupIDsymbol + saGroupID[i];
                }
            }
            UserRoleDAObject userRoleDAO = new UserRoleDAObject(this.sessionContainer, connection);
            List listRole = userRoleDAO.getListByUserRecordID(userID);
            Integer[] iaRoleID = (Integer[]) Utility.getPropertyArray(listRole, "ID", new Integer[0]);
            String[] saRoleID = Utility.getStringArray(iaRoleID);
            if (!Utility.isEmpty(saRoleID)) {
                for (int i = 0; i < saRoleID.length; i++) {
                    userIDGroupIDRoleIDStr += "," + roleIDsymbol + saRoleID[i];
                }
            }
        } catch (Exception e) {
            log.error("Retrieve userID groupID roleID.", e);
        }
        return userIDGroupIDRoleIDStr;
    }

    /**
   * @param args
   */
    public static void main(String[] args) {
    }
}
