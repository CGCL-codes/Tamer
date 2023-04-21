package com.hand.action.expor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.hand.utils.MsgPrint;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 系统名：HCSMobileApp
 * 子系统名：保存选中的预购\拜访\客户ID(单项)
 * 著作权：COPYRIGHT (C) 2011 HAND 
 *			INFORMATION SYSTEMS CORPORATION  ALL RIGHTS RESERVED.
 * @author nianchun.li
 * @createTime Jun 14, 2011
 */
public class UpdateExportAction extends ActionSupport {

    private boolean checked;

    private int createId;

    private int type;

    private int result;

    @Override
    public String execute() throws Exception {
        MsgPrint.showMsg("checked=" + checked);
        MsgPrint.showMsg("createId=" + createId);
        ActionContext actionContext = ActionContext.getContext();
        Map<String, Object> session = actionContext.getSession();
        Map<String, Boolean> exportMap = (Map<String, Boolean>) session.get("exportMap");
        if (null == exportMap) {
            if (checked) {
                exportMap = new HashMap<String, Boolean>();
                exportMap.put(createId + "|" + type, checked);
                session.put("exportMap", exportMap);
            }
        } else {
            if (checked) {
                exportMap.put(createId + "|" + type, checked);
                session.put("exportMap", exportMap);
            } else {
                exportMap.remove(createId + "|" + type);
                session.put("exportMap", exportMap);
            }
        }
        Iterator iter = exportMap.keySet().iterator();
        while (iter.hasNext()) {
            String createId = (String) iter.next();
            boolean value = (Boolean) exportMap.get(createId);
            MsgPrint.showMsg(createId + " <--> " + value);
        }
        result = 1;
        return SUCCESS;
    }

    public void setCreateId(int createId) {
        this.createId = createId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getResult() {
        return result;
    }
}
