package com.aimluck.eip.mail.db;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Properties;
import java.util.jar.Attributes;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import com.aimluck.eip.cayenne.om.portlet.EipTMail;
import com.aimluck.eip.common.ALPageNotFoundException;
import com.aimluck.eip.mail.ALAbstractFolder;
import com.aimluck.eip.mail.ALLocalMailMessage;
import com.aimluck.eip.mail.ALMailMessage;
import com.aimluck.eip.orm.Database;
import com.aimluck.eip.orm.query.SelectQuery;

/**
 * データベースを利用し、送受信したメールを保持するローカルフォルダのクラスです。 <br />
 * 
 */
public class ALDbLocalFolder extends ALAbstractFolder {

    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(ALDbLocalFolder.class.getName());

    /**
   * コンストラクタ
   * 
   * @param parentFolder
   *          親フォルダ
   * @param folderName
   *          自身のフォルダ名
   */
    public ALDbLocalFolder(int type_mail, String org_id, int user_id, int account_id) {
        super(type_mail, org_id, user_id, account_id);
    }

    /**
   * メールを取得します。
   * 
   * @param index
   * @return
   */
    @Override
    public ALLocalMailMessage getMail(int mailid) {
        try {
            SelectQuery<EipTMail> query = Database.query(EipTMail.class);
            Expression exp1 = ExpressionFactory.matchDbExp(EipTMail.MAIL_ID_PK_COLUMN, Integer.valueOf(mailid));
            Expression exp2 = ExpressionFactory.matchExp(EipTMail.USER_ID_PROPERTY, user_id);
            EipTMail email = query.setQualifier(exp1.andExp(exp2)).fetchSingle();
            if (email == null) {
                logger.debug("[Mail] Not found ID...");
                return null;
            }
            ALLocalMailMessage msg = readMail(new String(email.getMail()));
            email.setReadFlg("T");
            Database.commit();
            return msg;
        } catch (Throwable t) {
            Database.rollback();
            logger.error("[ALDbLocalFolder]", t);
            return null;
        }
    }

    /**
   * 指定されたファイルを読み込み，mail メッセージを取得する．
   * 
   * @param fileName
   * @return
   */
    private ALLocalMailMessage readMail(String mail) {
        System.setProperty("mail.mime.charset", "ISO-2022-JP");
        System.setProperty("mail.mime.decodetext.strict", "false");
        Properties prop = new Properties();
        prop.setProperty("mail.mime.address.strict", "false");
        ALLocalMailMessage localmsg = null;
        ByteArrayInputStream input = null;
        try {
            input = new ByteArrayInputStream(mail.getBytes());
            localmsg = new ALLocalMailMessage(Session.getDefaultInstance(prop), input);
            input.close();
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return localmsg;
    }

    /**
   * メールを保存する。
   * 
   * @param messages
   * @return
   */
    @Override
    public boolean saveMail(ALMailMessage mail, String orgId) {
        try {
            insertMailToDB((MimeMessage) mail, null, true, true);
        } catch (Exception ex) {
            logger.error("Exception", ex);
            return false;
        }
        return true;
    }

    /**
   * 受信サーバから受信した受信可能サイズを超えたメールを保存する。<br />
   * このメールはヘッダ情報のみ、受信サーバから取得し、他の情報は取得しない。
   * 
   * @param localMailMessage
   * @return
   */
    @Override
    public boolean saveDefectiveMail(ALMailMessage mail, String orgId) {
        try {
            insertMailToDB((MimeMessage) mail, null, false, false);
        } catch (Exception ex) {
            logger.error("Exception", ex);
            return false;
        }
        return true;
    }

    /**
   * 指定されたインデックスのメールを削除する．
   * 
   * @param mailid
   * @return
   */
    @Override
    public boolean deleteMail(int mailid) {
        try {
            SelectQuery<EipTMail> query = Database.query(EipTMail.class);
            Expression exp1 = ExpressionFactory.matchDbExp(EipTMail.MAIL_ID_PK_COLUMN, Integer.valueOf(mailid));
            Expression exp2 = ExpressionFactory.matchExp(EipTMail.USER_ID_PROPERTY, user_id);
            Expression exp3 = ExpressionFactory.matchExp(EipTMail.ACCOUNT_ID_PROPERTY, account_id);
            query.andQualifier(exp1).andQualifier(exp2).andQualifier(exp3);
            EipTMail mail = query.setQualifier(exp1.andExp(exp2).andExp(exp3)).fetchSingle();
            if (mail == null) {
                logger.debug("[ALDbLocalFolder] Not found ID...");
                throw new ALPageNotFoundException();
            }
            Database.delete(mail);
            Database.commit();
        } catch (Throwable t) {
            Database.rollback();
            logger.error("[ALDbLocalFolder]", t);
            return false;
        }
        return true;
    }

    /**
   * 指定されたインデックスのメールを削除する．
   * 
   * @param msgIndexes
   * @return
   */
    @Override
    public boolean deleteMails(List<String> msgIndexes) {
        return false;
    }

    /**
   * 
   * @return
   */
    @Override
    protected Attributes getColumnMap() {
        Attributes map = new Attributes();
        map.putValue("subject", EipTMail.SUBJECT_PROPERTY);
        map.putValue("person", EipTMail.PERSON_PROPERTY);
        map.putValue("date", EipTMail.EVENT_DATE_PROPERTY);
        map.putValue("volume", EipTMail.FILE_VOLUME_PROPERTY);
        return map;
    }

    /**
   * 新着メール数を取得する。
   * 
   * @return
   */
    @Override
    public int getNewMailNum() {
        return 0;
    }

    /**
   * 新着メール数を更新する．
   * 
   * @param num
   */
    @Override
    public void setNewMailNum(int num) {
    }

    /**
   * 指定したフォルダ内のメールの総数を取得する。
   * 
   * @param type
   *          送受信フラグ
   * @return
   */
    public int getMailSum() {
        return 0;
    }

    /**
   * 指定したフォルダ内の未読メール数を取得する．
   * 
   * @return
   */
    @Override
    public int getUnreadMailNum() {
        return 0;
    }

    /**
   * ローカルフォルダを閉じる．
   */
    @Override
    public void close() {
    }

    /**
   * @param msgIndexes
   * @return
   */
    @Override
    public boolean readMails(List<String> msgIndexes) {
        return false;
    }
}
