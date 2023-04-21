package cn.myapps.mobile.pda;

import java.util.Collection;
import java.util.Iterator;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.notification.ejb.sendmode.SMSMode;
import cn.myapps.util.ProcessFactory;

public class SMSSent {

    public static void sent(Document doc, WebUser user) {
        try {
            Item key1 = doc.findItem("障碍名称");
            Item key2 = doc.findItem("故障原因");
            boolean flag = false;
            if (key1 == null || key2 == null) {
                key1 = doc.findItem("隐患名称");
                key2 = doc.findItem("隐患原因");
                flag = true;
            }
            if (key1 != null && key2 != null) {
                String type = doc.getItemValueAsString("严重程度");
                String dwdw = doc.getItemValueAsString("代维单位");
                String sql = "SELECT ITEM_接收报警号码 FROM TLK_报警号码 WHERE ITEM_接收事故级别 = '" + type + "' AND ITEM_事故所属代维单位='" + dwdw + "' AND DOMAINID = '" + user.getDomainid() + "'";
                DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, doc.getApplicationid());
                Collection<Document> docs = dp.queryBySQL(sql);
                if (docs != null && !docs.isEmpty()) {
                    SMSMode smsMode = new SMSMode(user);
                    StringBuffer receivers = new StringBuffer();
                    for (Iterator<Document> it = docs.iterator(); it.hasNext(); ) {
                        Document rDoc = it.next();
                        String receiver = rDoc.getItemValueAsString("接收报警号码");
                        if (receivers.length() < 1) {
                            receivers.append(receiver);
                        } else {
                            receivers.append(",").append(receiver);
                        }
                    }
                    if (receivers.length() > 11) {
                        String person = doc.getItemValueAsString("上报人");
                        if (flag) {
                            String context = doc.getItemValueAsString("隐患名称");
                            smsMode.send("隐患报告[" + person + "]", context, receivers.toString(), true);
                        } else {
                            String context = doc.getItemValueAsString("障碍名称");
                            smsMode.send("障碍报告[" + person + "]", context, receivers.toString(), true);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
