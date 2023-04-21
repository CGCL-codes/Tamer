package com.aimluck.eip.webmail;

import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.commons.utils.ALDateUtil;
import com.aimluck.eip.cayenne.om.portlet.EipMMailAccount;
import com.aimluck.eip.cayenne.om.portlet.EipTMailFolder;
import com.aimluck.eip.common.ALAbstractSelectData;
import com.aimluck.eip.common.ALDBErrorException;
import com.aimluck.eip.common.ALEipUser;
import com.aimluck.eip.common.ALPageNotFoundException;
import com.aimluck.eip.mail.util.ALMailUtils;
import com.aimluck.eip.modules.actions.common.ALAction;
import com.aimluck.eip.orm.Database;
import com.aimluck.eip.orm.query.ResultList;
import com.aimluck.eip.orm.query.SelectQuery;
import com.aimluck.eip.util.ALEipUtils;
import com.aimluck.eip.webmail.beans.WebmailAccountLiteBean;
import com.aimluck.eip.webmail.util.WebMailUtils;

/**
 * ウェブメールのフォルダを管理するためのクラスです。 <br />
 */
public class WebMailFolderSelectData extends ALAbstractSelectData<EipTMailFolder, EipTMailFolder> {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(WebMailFolderSelectData.class.getName());

    /** フォルダID */
    String folder_id = null;

    /** メールアカウント */
    private EipMMailAccount mail_account;

    private List<WebmailAccountLiteBean> mailAccountList;

    private List<WebMailFolderResultData> mailFolderList;

    private Map<Integer, Integer> unreadMailSumMap;

    /**
   * 
   * @param action
   * @param rundata
   * @param context
   */
    @Override
    public void init(ALAction action, RunData rundata, Context context) throws ALPageNotFoundException, ALDBErrorException {
        int mailAccountId = 0;
        if (ALEipUtils.isMatch(rundata, context)) {
            if (rundata.getParameters().containsKey(WebMailUtils.FOLDER_ID)) {
                folder_id = rundata.getParameters().get(WebMailUtils.FOLDER_ID);
            }
            if (rundata.getParameters().containsKey(WebMailUtils.ACCOUNT_ID)) {
                mailAccountId = rundata.getParameters().getInt(WebMailUtils.ACCOUNT_ID);
            } else {
                mailAccountId = Integer.parseInt(ALEipUtils.getTemp(rundata, context, WebMailUtils.ACCOUNT_ID));
            }
        }
        ALEipUser login_user = ALEipUtils.getALEipUser(rundata);
        mail_account = ALMailUtils.getMailAccount((int) login_user.getUserId().getValue(), mailAccountId);
        if (mail_account == null) {
            return;
        }
        mailFolderList = WebMailUtils.getMailFolderAll(mail_account);
        unreadMailSumMap = WebMailUtils.getUnreadMailNumberMap(rundata, ALEipUtils.getUserId(rundata), mailAccountId);
        super.init(action, rundata, context);
    }

    /**
   * 
   * @param rundata
   * @param context
   * @return
   * @throws ALPageNotFoundException
   * @throws ALDBErrorException
   */
    @Override
    protected ResultList<EipTMailFolder> selectList(RunData rundata, Context context) throws ALPageNotFoundException, ALDBErrorException {
        SelectQuery<EipTMailFolder> query = getSelectQuery(rundata, context);
        buildSelectQueryForListView(query);
        buildSelectQueryForListViewSort(query, rundata, context);
        return query.getResultList();
    }

    /**
   * @param rundata
   * @param context
   * @return
   */
    private SelectQuery<EipTMailFolder> getSelectQuery(RunData rundata, Context context) {
        SelectQuery<EipTMailFolder> query = Database.query(EipTMailFolder.class);
        Expression exp = ExpressionFactory.matchDbExp(EipTMailFolder.EIP_MMAIL_ACCOUNT_PROPERTY, mail_account);
        query.setQualifier(exp);
        return query;
    }

    /**
   * 
   * @param rundata
   * @param context
   * @return
   * @throws ALPageNotFoundException
   * @throws ALDBErrorException
   */
    @Override
    protected EipTMailFolder selectDetail(RunData rundata, Context context) throws ALPageNotFoundException, ALDBErrorException {
        return WebMailUtils.getEipTMailFolder(mail_account, ALEipUtils.getParameter(rundata, context, WebMailUtils.FOLDER_ID));
    }

    /**
   * フォルダのデータを取得します。
   * 
   * 
   */
    @Override
    protected WebMailFolderResultData getResultData(EipTMailFolder obj) throws ALPageNotFoundException, ALDBErrorException {
        return new WebMailFolderResultData(obj);
    }

    /**
   * フォルダの詳細データを取得します。
   * 
   * @param record
   * @return
   * @throws ALPageNotFoundException
   * @throws ALDBErrorException
   */
    @Override
    protected Object getResultDataDetail(EipTMailFolder record) throws ALPageNotFoundException, ALDBErrorException {
        try {
            folder_id = record.getFolderId().toString();
            WebMailFolderResultData rd = new WebMailFolderResultData();
            rd.initField();
            rd.setFolderId(record.getFolderId());
            rd.setFolderName(record.getFolderName());
            rd.setCreateDate(ALDateUtil.format(record.getCreateDate(), "yyyy年M月d日"));
            if (mail_account.getDefaultFolderId().equals(record.getFolderId())) {
                rd.setCanUpdate(false);
            } else {
                rd.setCanUpdate(true);
            }
            return rd;
        } catch (Exception ex) {
            logger.error("Exception", ex);
            return null;
        }
    }

    /**
   * メールアカウント一覧を取得します。
   * 
   * @param rundata
   * @param context
   */
    public void loadMailAccountList(RunData rundata, Context context) {
        mailAccountList = WebMailUtils.getMailAccountList(rundata, context);
    }

    /**
   * メールアカウントの一覧を取得します。
   * 
   * @return
   */
    public List<WebmailAccountLiteBean> getMailAccountList() {
        return mailAccountList;
    }

    /**
   * 現在のアカウントが持つメールフォルダを取得します。
   * 
   * @return
   */
    public List<WebMailFolderResultData> getFolderList() {
        return mailFolderList;
    }

    @Override
    protected Attributes getColumnMap() {
        Attributes map = new Attributes();
        map.putValue("folder_name", EipTMailFolder.FOLDER_NAME_PROPERTY);
        return map;
    }

    public String getFolderId() {
        return folder_id;
    }

    /**
   * フォルダ別未読メール数を取得する。
   * 
   * @return
   */
    public int getUnReadMailSumByFolderId(int folder_id) {
        return unreadMailSumMap.get(folder_id);
    }

    /**
   * 現在選択中のアカウントIDを取得します。
   * 
   * @return
   */
    public int getAccountId() {
        return mail_account.getAccountId();
    }

    public boolean isMatch(int id1, long id2) {
        return id1 == (int) id2;
    }
}
