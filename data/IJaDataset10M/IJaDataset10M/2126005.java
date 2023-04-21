package org.cofax.cms;

import org.cofax.*;
import org.cofax.cds.*;
import javax.servlet.http.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *  CofaxToolsWorkflow
 *  utilities for the workflow
 * 
 *  Copyright 2002 Smile Motoristes Internet
 *  http://www.smile.fr/
 *  Contact cofax@smile.fr for further information
 *
 *  @author Smile - Badr Chentouf
 *  @author Smile - Matthieu Bureau
 *    
 */
public class CofaxToolsWorkflow {

    /**
	* call the method setArticle and if the article can be saved, validate it
    * Validate an article means to set the field workflow_state to 1
	*
	**/
    public static String validateArticle(DataStore db, HttpServletRequest req, HttpSession session) {
        String itemID = (String) req.getAttribute("ITEMID");
        String fileName = (String) req.getAttribute("FILENAME");
        String delete = (String) req.getAttribute("DELETEARTICLE");
        if ((itemID != null) && (!(itemID.equals("")))) {
            String message = CofaxToolsDbUtils.setArticle(db, req, session, "1");
            if ((delete != null) && (delete.equals("on"))) return (message);
        }
        String requestedUrl = req.getScheme() + "://" + req.getHeader("host");
        CofaxToolsUser user = (CofaxToolsUser) (session.getAttribute("user"));
        String userFirstName = (String) user.userInfoHash.get("FIRSTNAME");
        String userLastName = (String) user.userInfoHash.get("LASTNAME");
        String emailList = getArticleValidators(db, req, session, itemID);
        if (!(emailList.equals(""))) {
            String subject = "Cofax : New article to validate : ID=" + itemID;
            String mailFrom = getAdminEmail(db, session);
            String message = "The user " + userFirstName + " " + userLastName + " asked a validation on an article on the publication <i>'" + req.getAttribute("PUBNAME") + "'</i>.<br>";
            message += "<br>The title is <b>'" + req.getAttribute("HEADLINE") + "'</b>";
            message += "<br><br>You can see a preview of the new article <a href='" + requestedUrl + "" + CofaxToolsServlet.aliasPath + "/tools/?mode=article_preview_article&ITEMID=" + itemID + "'>here</a><br>";
            message += "You can edit the article <a href='" + requestedUrl + "" + CofaxToolsServlet.aliasPath + "/tools/?mode=article_edit_article_by_itemID&ITEMID=" + itemID + "&hl=article_create_article'>here</a>";
            message += "<br><br>The webmaster";
            boolean bln = sendMail(emailList, mailFrom, subject, message);
            if (bln) emailList = " and an email has been sent to " + emailList; else emailList = " and an error has occured while sending an email to " + emailList;
        }
        int unlock = CofaxToolsLockUnlock.unlockArticle(db, (String) user.userInfoHash.get("USERID"), itemID);
        return ("'" + fileName + "' (" + itemID + ") : " + CofaxToolsUtil.getI18NMessage(req.getLocale(), "tools_articlevalidated") + " " + emailList + ".");
    }

    /**
	* call the method setArticle and if the article can be saved, publish it
    * Publish an article means to set the field workflow_state to 2 and to update
    * the table tblArticlePreload
	*
	**/
    public static String publishArticle(DataStore db, HttpServletRequest req, HttpSession session) {
        String itemID = (String) req.getAttribute("ITEMID");
        String fileName = (String) req.getAttribute("FILENAME");
        String delete = (String) req.getAttribute("DELETEARTICLE");
        if ((itemID != null) && (!(itemID.equals("")))) {
            String message = CofaxToolsDbUtils.setArticle(db, req, session, "2");
            if ((delete != null) && (delete.equals("on"))) return (message); else {
                message = "Article " + fileName + " (" + itemID + ") has been saved and published.";
                String approved = (String) req.getAttribute("APPROVED");
                CofaxToolsUser user = (CofaxToolsUser) (session.getAttribute("user"));
                String userName = (String) user.userInfoHash.get("USERNAME");
                String userFirstName = (String) user.userInfoHash.get("FIRSTNAME");
                String userLastName = (String) user.userInfoHash.get("LASTNAME");
                if (!(userName.equals(approved))) {
                    HashMap fillReq = new HashMap();
                    fillReq.put("USERNAME", approved);
                    String tag = CofaxToolsDbUtils.fillTag(db, "getUserInfoByUserName");
                    HashMap results = CofaxToolsDbUtils.getNameValuePackageHash(db, fillReq, tag);
                    String emailList = (String) results.get("EMAIL");
                    if ((emailList != null) && !(emailList.equals(""))) {
                        String subject = "Cofax : Article published : ID=" + itemID;
                        String mailFrom = getAdminEmail(db, session);
                        String messageBody = "The user " + userFirstName + " " + userLastName + " has published the article <b>'" + req.getAttribute("HEADLINE") + "'</b> on the publication <i>'" + req.getAttribute("PUBNAME") + "'</i>.";
                        messageBody += "<br><br>You can see now the article on line.<br>";
                        messageBody += "<br><br>The webmaster.";
                        boolean bln = sendMail(emailList, mailFrom, subject, messageBody);
                        if (bln) message += " and an email has been sent to its author :" + emailList; else message += " and an error has occured while sending an email to its author :" + emailList;
                    }
                }
                return (message);
            }
        } else {
            return ("ERROR : no itemID found");
        }
    }

    public static String getArticleValidators(DataStore db, HttpServletRequest req, HttpSession session, String itemID) {
        String listEmail = "";
        CofaxToolsUser user = (CofaxToolsUser) (session.getAttribute("user"));
        HashMap ht = new HashMap();
        ht.put("ITEMID", (String) req.getAttribute("ITEMID"));
        ht.put("PUBID", user.workingPub);
        ht = CofaxToolsUtil.getPublicationInfo(db, user.workingPub);
        String Wlevels = smile.stored.utils.getString(ht, "WORKFLOW_LEVELS", "0");
        String Wmail = smile.stored.utils.getString(ht, "WORKFLOW_MAIL", "1");
        String WadminEmail = smile.stored.utils.getString(ht, "WORKFLOW_ADMINEMAIL", "");
        if (Wmail.equals("1")) {
            if (Wlevels.equals("1")) {
                return (WadminEmail);
            } else {
                String tag1 = "select S.mappingCode ";
                tag1 = tag1 + "from tblSections AS S, ";
                tag1 = tag1 + "tblArticles AS A ";
                tag1 = tag1 + "where S.pubName=A.pubName ";
                tag1 = tag1 + "and S.section = A.section ";
                tag1 = tag1 + "and A.itemID=" + (String) req.getAttribute("ITEMID") + " ";
                HashMap fillReq = new HashMap();
                Vector mappingCodeVect = CofaxToolsDbUtils.getPackageVector(db, fillReq, tag1);
                String mappingCode = mappingCodeVect.get(0) + "";
                while (listEmail.equals("") && (!(mappingCode.equals("0"))) && (!(mappingCode.equals("0")))) {
                    String tag = "select PU.email ";
                    tag = tag + "from tblPermUserSection AS PUS, tblPermUsers AS PU ";
                    tag = tag + "where PUS.manager=1 ";
                    tag = tag + "and PUS.userID = PU.userID ";
                    tag = tag + "and PUS.mappingCode=" + mappingCode + " ";
                    Vector emailVect2 = CofaxToolsDbUtils.getPackageVector(db, fillReq, tag);
                    int emailSize2 = emailVect2.size();
                    for (int i = 0; i < emailSize2; i++) {
                        if (!(listEmail.equals(""))) listEmail += ";";
                        listEmail += emailVect2.get(i) + "";
                    }
                    if (listEmail.equals("")) {
                        String tag3 = "select tblSections.subMapOf ";
                        tag3 = tag3 + "from tblSections ";
                        tag3 = tag3 + "where tblSections.mappingCode=" + mappingCode + " ";
                        mappingCodeVect = CofaxToolsDbUtils.getPackageVector(db, fillReq, tag3);
                        mappingCode = mappingCodeVect.get(0) + "";
                    }
                }
                if (listEmail.equals("")) listEmail = WadminEmail;
                return (listEmail);
            }
        } else {
            return ("");
        }
    }

    public static String getAdminEmail(DataStore db, HttpSession session) {
        CofaxToolsUser user = (CofaxToolsUser) (session.getAttribute("user"));
        String adminEmail = "cofax@cofax.org";
        try {
            HashMap ht = new HashMap();
            ht.put("PUBID", user.workingPub);
            ht = CofaxToolsUtil.getPublicationInfo(db, user.workingPub);
            adminEmail = smile.stored.utils.getString(ht, "WORKFLOW_ADMINEMAIL", "");
        } catch (Exception e) {
            CofaxToolsUtil.log("CofaxToolsWorkflow : getAdminEmail : error " + e);
        }
        return (adminEmail);
    }

    /**
	 *  Description of the Method
	 *
	 *@param  glossary  Description of the Parameter
	 *@param  hostName  Description of the Parameter
	 *@return           Description of the Return Value
	 */
    public static boolean sendMail(String mailList, String mailFrom, String subject, String messageBody) {
        try {
            Session smtpSession;
            Properties props = new Properties();
            props.put("mail.smtp.host", CDSServlet.mailHost);
            smtpSession = Session.getDefaultInstance(props, null);
            String mailTo = "";
            if (mailList.indexOf(";") > 0) {
                while (mailList.indexOf(";") > 0) {
                    try {
                        mailTo = mailList.substring(0, mailList.indexOf(";"));
                        mailList = mailList.substring(mailList.indexOf(";") + 1);
                        MimeMessage message = new MimeMessage(smtpSession);
                        message.setFrom(new InternetAddress(mailFrom));
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
                        message.setSubject(subject);
                        message.setContent(messageBody, "text/html");
                        CofaxToolsUtil.log("Sending mail to " + mailTo + "...");
                        Transport.send(message);
                        CofaxToolsUtil.log("Sending mail to " + mailTo + "...OK");
                    } catch (Exception e) {
                        CofaxToolsUtil.log("Error while sending mail to " + mailTo + " : " + e);
                    }
                }
                MimeMessage message = new MimeMessage(smtpSession);
                message.setFrom(new InternetAddress(mailFrom));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailList));
                message.setSubject(subject);
                message.setContent(messageBody, "text/html");
                CofaxToolsUtil.log("Sending mail to " + mailList + "...");
                Transport.send(message);
                CofaxToolsUtil.log("Sending mail to " + mailList + "...OK");
            } else {
                try {
                    MimeMessage message = new MimeMessage(smtpSession);
                    message.setFrom(new InternetAddress(mailFrom));
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailList));
                    message.setSubject(subject);
                    message.setContent(messageBody, "text/html");
                    CofaxToolsUtil.log("Sending mail to " + mailList + "...");
                    Transport.send(message);
                    CofaxToolsUtil.log("Sending mail to " + mailList + "...OK");
                } catch (Exception e) {
                    CofaxToolsUtil.log("Error while sending mail to " + mailList + " : " + e);
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static Vector disableArticle(HashMap ht, String disableArticle, String validated, DataStore db, boolean permission) {
        int toValidOrPubtemp = 0;
        Vector disable = new Vector(2);
        int check = 0;
        if (disableArticle != null && disableArticle.equals("on")) {
            if (validated.equals("0")) {
                toValidOrPubtemp = 0;
                check = 1;
            } else if (validated.equals("1")) {
                toValidOrPubtemp = 1;
                check = 1;
            } else if (validated.equals("2")) {
                if (permission) {
                    toValidOrPubtemp = 2;
                    check = 1;
                } else {
                    toValidOrPubtemp = 0;
                    check = 0;
                }
            } else {
                toValidOrPubtemp = 0;
                check = 1;
            }
        } else {
            if (validated.equals("2")) {
                if (permission) {
                    StringBuffer tag_get = new StringBuffer();
                    tag_get.append("SELECT disableArticle FROM tblactivearticles WHERE pubName='req:PUBNAME' and section='req:SECTION' AND itemID='req:ITEMID' AND filename='req:FILENAME'");
                    ht = CofaxToolsDbUtils.getNameValuePackageHash(db, ht, tag_get.toString());
                    String disableArticleOld = (String) ht.get("DISABLEARTICLE");
                    if (disableArticleOld.equals("1")) {
                        toValidOrPubtemp = 2;
                        check = 1;
                    } else {
                        toValidOrPubtemp = 0;
                        check = 1;
                    }
                } else {
                    toValidOrPubtemp = 0;
                    check = 1;
                }
            } else {
                toValidOrPubtemp = 0;
                check = 1;
            }
        }
        Integer toValidOrPub = new Integer(toValidOrPubtemp);
        Integer check2 = new Integer(check);
        disable.add(toValidOrPub);
        disable.add(check2);
        return (disable);
    }
}
