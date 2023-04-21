package com.jspx.txweb.support;

import com.jspx.bundle.Bundle;
import com.jspx.txweb.annotation.IocRef;
import com.jspx.txweb.template.DataModel;
import com.jspx.txweb.util.TXWebUtil;
import com.jspx.utils.FileUtil;
import com.jspx.utils.RequestUtil;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User:chenYuan (mail:cayurain@21cn.com)
 * Date: 2007-1-28
 * Time: 23:16:20
 * com.jspx.txweb.support.TemplateSupport
 */
public abstract class TemplateSupport extends ActionSupport {

    public TemplateSupport() {
    }

    protected Bundle config;

    protected Bundle language;

    @IocRef(name = "language", test = true)
    public void setLanguage(Bundle language) {
        this.language = language;
    }

    @IocRef(name = "config", test = true)
    public void setConfig(Bundle config) {
        this.config = config;
    }

    private String getTemplatePath() {
        StringBuffer sb = new StringBuffer(getEnvironment(Key_RealPath));
        sb.append(getEnvironment(Key_Namespace)).append("/");
        return FileUtil.mendPath(sb.toString());
    }

    public void processTemplate(Map<String, Object> valueMap) throws Exception {
        if (getEnvironment().containsKey(Key_Template)) {
            DataModel dataModel = TXWebUtil.createDataModel();
            dataModel.setTemplateName(getEnvironment(Key_ActionName));
            dataModel.setTemplateString(getEnvironment(Key_Template));
            dataModel.setTemplatePath(getTemplatePath());
            valueMap.put(Key_TemplatePath, dataModel.getTemplatePath());
            valueMap.put(Key_Request, RequestUtil.getRequestMap(request));
            valueMap.put(Key_Session, RequestUtil.getSessionMap(request));
            valueMap.put(Key_Response, TXWebUtil.getResponseMap(response));
            valueMap.put(Key_Config, config);
            valueMap.put(Key_Language, language);
            valueMap.put(Key_This, this);
            setResult(dataModel.processTemplate(valueMap));
            dataModel.clear();
        }
    }
}
