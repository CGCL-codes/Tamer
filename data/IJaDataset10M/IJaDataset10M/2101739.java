package com.gjzq.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.gjzq.service.ComplainService;
import com.gjzq.service.OnlineMessageService;
import com.gjzq.service.QualificationService;
import com.gjzq.service.SMSLogService;
import com.thinkive.base.jdbc.DBPage;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.util.DateHelper;
import com.thinkive.base.util.StringHelper;
import com.thinkive.business.other.service.CommonTemplateService;
import com.thinkive.plat.web.action.BaseAction;
import com.thinkive.plat.web.form.DynaForm;

/**
 * ����:  ������ѯ
 * ��Ȩ:	 Copyright (c) 2009
 * ��˾:	 ˼�ϿƼ�
 * ����:	 ��ѫ
 * �汾:	 1.0
 * ��������: 2011-4-19
 * ����ʱ��: ����16:20:34
 */
public class OnlineMessageAction extends BaseAction {

    private HashMap dataMap = new HashMap();

    private DynaForm form = new DynaForm();

    public DynaForm getForm() {
        return form;
    }

    public Map getData() {
        return dataMap;
    }

    /**
	 * ��ѯ�б�
	 *
	 * @return
	 */
    public String doDefault() {
        int curPage = this.getIntParameter("page");
        curPage = (curPage <= 0) ? 1 : curPage;
        String clientname = getStrParameter("key");
        String status = getStrParameter("status");
        String siteno = getLoginSiteNo();
        OnlineMessageService service = new OnlineMessageService();
        DBPage page = service.getPageData(curPage, 20, clientname, status, siteno);
        dataMap.put("page", page);
        return "list";
    }

    /**
	 * ������Ϣ
	 *
	 * @return
	 */
    public String doEdit() {
        OnlineMessageService service = new OnlineMessageService();
        if (isPostBack()) {
            String answer = getStrParameter("answer");
            String id = getStrParameter("id");
            String replyBy = getUID();
            DataRow data = new DataRow();
            data.set("id", id);
            data.set("answer", answer);
            data.set("reply_by", replyBy);
            data.set("status", "1");
            data.set("reply_date", DateHelper.formatDate1(new Date()));
            DataRow messagedatarow = service.findOnlineMessageById(id);
            int replayway = messagedatarow.getInt("reply_way");
            String email = messagedatarow.getString("email");
            String curQus = messagedatarow.getString("content");
            String answerDate = messagedatarow.getString("create_date");
            String clientId = messagedatarow.getString("cid");
            DataRow clientDataRow = service.findClientById(clientId);
            String clientName = "";
            String pid = "";
            if (clientDataRow != null && clientDataRow.containsKey("name")) {
                clientName = clientDataRow.getString("name");
            }
            if (clientDataRow != null && clientDataRow.containsKey("passport_id")) {
                pid = clientDataRow.getString("passport_id");
            }
            service.updateOnlineMessage(data);
            if (StringHelper.isNotEmpty(email)) {
                this.sendEmail(email, curQus, answer, clientName, answerDate);
            }
            addLog(getUID(), "main", "����������ѯ|" + getUID() + "|" + email + "|" + id + "|" + pid, "������:" + getUID() + "|������:" + email + "|���Ա��:" + id + "|PID:" + pid + "|������:[" + answer + "]");
            return MESSAGE;
        }
        String id = getStrParameter("id");
        DataRow data = service.findOnlineMessageById(id);
        form.putAll(data);
        return EDIT;
    }

    /**
	 * ɾ����Ϣ
	 *
	 * @return
	 */
    public String doDelete() {
        OnlineMessageService service = new OnlineMessageService();
        int[] idArray = getIntArrayParameter("id");
        for (int i = 0; i < idArray.length; i++) {
            service.deleteOnlineMessage(idArray[i]);
        }
        return MAIN;
    }

    /**
	 * �Ƽ���Ϣ
	 * @return
	 */
    public String doSetHot() {
        OnlineMessageService service = new OnlineMessageService();
        int[] idArray = getIntArrayParameter("id");
        for (int i = 0; i < idArray.length; i++) {
            service.OnlineMessageHot(idArray[i], 1);
        }
        return MAIN;
    }

    /**
	 * �Ƽ���Ϣ
	 * @return
	 */
    public String doCancelHot() {
        OnlineMessageService service = new OnlineMessageService();
        int[] idArray = getIntArrayParameter("id");
        for (int i = 0; i < idArray.length; i++) {
            service.OnlineMessageHot(idArray[i], 0);
        }
        return MAIN;
    }

    /**
	 * �����������ʼ�
	 * ʱ�䣺2011-7-15 ����09:19:31
	 * @param clientName
	 * @param curContent
	 * @return
	 * @throws Exception
	 */
    public boolean sendEmail(String email, String curQus, String curAns, String clientName, String answerDate) {
        DataRow logData = new DataRow();
        logData.set("status", "0");
        logData.set("user_id", getUserId());
        logData.set("valid_date", DateHelper.formatDate(new Date()));
        String logId = new ComplainService().insertEmailLog(logData);
        DataRow dr = new DataRow();
        dr.put("title", "�������Իظ�");
        String content = getEmailTemplate(clientName, curQus, curAns, answerDate);
        dr.put("content", content);
        dr.put("rec_email", email);
        dr.put("create_date", DateHelper.formatDate(new Date()));
        new CommonTemplateService().insertEmail(dr);
        new SMSLogService().addEmailLog(email, content, SMSLogService.OP_EMAIL_ONLINEMESSAGE);
        return true;
    }

    /**
	 * ��������ȡemailģ��
	 * ʱ�䣺2011-7-15 ����09:19:31
	 * @param clientName
	 * @param curContent
	 * @return
	 * @throws Exception
	 */
    private String getEmailTemplate(String clientName, String curQuestion, String curAnswer, String answerDate) {
        CommonTemplateService templateService = new CommonTemplateService();
        DataRow templateData = templateService.findByName("user.onlinemessage");
        String emailContent = "";
        if (templateData != null) {
            emailContent = templateData.getString("content");
            emailContent = emailContent.replace("$clientName$", clientName).replace("$question$", curQuestion).replace("$answer$", curAnswer).replace("$answerDate$", answerDate).replace("$questionDate$", DateHelper.formatDate(new Date()));
        }
        return emailContent;
    }
}
