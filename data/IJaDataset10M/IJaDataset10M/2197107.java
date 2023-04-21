package cn.myapps.core.dynaform.smsfilldocument;

import java.util.Collection;
import java.util.Iterator;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.document.ejb.DocumentProcessBean;
import cn.myapps.core.dynaform.form.action.FormHelper;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.shortmessage.received.ejb.ReceivedMessageVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.notification.ejb.sendmode.SMSMode;
import com.jamonapi.proxy.MonProxyFactory;

public class RecievedMessageParser {

    public static void parse(ReceivedMessageVO recVO) throws DataMessageException {
        int status = 0;
        String content = "";
        SMSMode sender = new SMSMode("", recVO.getDomainid(), recVO.getApplicationid());
        try {
            String recMsg = recVO.getContent();
            String[] parse = recMsg.split(";");
            DataMessageParser parser = new DataMessageParser(parse);
            WebUser user = parser.getUser();
            FormHelper helper = new FormHelper();
            Form form = helper.findFormByRelationName(parse[3]);
            String[] values = parser.getFieldValuses();
            content = "Fill Document[" + parse[3];
            for (int i = 0; i < values.length; i++) {
                if (i > 3) break;
                content += ";" + values[i];
            }
            content += "...] ";
            if (form != null) {
                try {
                    sender.setDomainid(user.getDomainid());
                    sender.setApplication(form.getApplicationid());
                    ParamsTable params = parser.getParamsByRelationText(form.getRelationText());
                    addFlowId(params, form);
                    user.setDefaultApplication(form.getApplicationid());
                    Document doc = form.createDocument(params, user);
                    DocumentProcess process = (DocumentProcess) MonProxyFactory.monitor(new DocumentProcessBean(form.getApplicationid()));
                    process.doCreate(doc);
                    if (form.isOnSaveStartFlow()) {
                        process.doStartFlowOrUpdate(doc, params, user);
                    } else {
                        process.doUpdate(doc, user);
                    }
                } catch (Exception e) {
                    content += "Failure! Error: " + e.getMessage();
                    status = -2;
                }
                content += "Successful!";
            } else {
                content += "Failure! Error: No such Form[" + parse[3] + "] in Application!";
                status = -1;
            }
        } catch (Exception e) {
            content += "Fill Document Failure! Error: " + e.getMessage();
            status = -2;
        }
        try {
            sender.send("", content, recVO.getSender());
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new DataMessageException(status, content);
    }

    private static void addFlowId(ParamsTable params, Form form) {
        Collection acts = form.getActivitys();
        if (acts != null) {
            for (Iterator iter = acts.iterator(); iter.hasNext(); ) {
                Activity act = (Activity) iter.next();
                if (act.getType() == ActivityType.WORKFLOW_PROCESS) {
                    params.setParameter("_flowid", act.getOnActionFlow());
                }
            }
        }
    }
}
