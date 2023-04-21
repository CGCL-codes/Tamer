package com.narirelays.ems.json;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonBeanProcessor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.json.JSONResult;
import org.apache.struts2.json.JSONUtil;
import com.narirelays.ems.applogic.OperResult;
import com.narirelays.ems.persistence.orm.EntHierarchy;
import com.narirelays.ems.resources.StorageService;
import com.narirelays.ems.resources.WebVariable;
import com.narirelays.ems.utils.I18nUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import static com.narirelays.ems.resources.EMSi18n.OPERRESULT_IS_NULL;

public class MyJsonResult extends JSONResult {

    /**
	 * 
	 */
    private static JsonConfig jsonConfig = null;

    static {
        jsonConfig = new JsonConfig();
    }

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(MyJsonResult.class);

    public void execute(ActionInvocation invocation) throws Exception {
        ActionContext actionContext = invocation.getInvocationContext();
        HttpServletRequest request = (HttpServletRequest) actionContext.get(StrutsStatics.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) actionContext.get(StrutsStatics.HTTP_RESPONSE);
        try {
            String json;
            Object rootObject;
            if (this.getRoot() != null) {
                ValueStack stack = invocation.getStack();
                rootObject = stack.findValue(this.getRoot());
            } else {
                rootObject = invocation.getAction();
            }
            if (rootObject == null) {
                OperResult jsonResult = new OperResult();
                jsonResult.setFailed(OPERRESULT_IS_NULL);
                Map<String, Object> sessionMap = invocation.getInvocationContext().getSession();
                if (sessionMap.containsKey(WebVariable.I18N_ATTRIBUTE_NAME)) {
                    jsonResult.MakeI18NInfoMSG(I18nUtils.getLocaleFromString(sessionMap.get(WebVariable.I18N_ATTRIBUTE_NAME).toString()));
                } else {
                    jsonResult.MakeI18NInfoMSG(I18nUtils.getLocaleFromString(WebVariable.DEFAULT_I18N));
                }
                json = JSONObject.fromObject(jsonResult).toString();
            } else {
                if (rootObject instanceof String) {
                    json = rootObject.toString();
                } else if (rootObject instanceof OperResult) {
                    OperResult jsonResult = (OperResult) rootObject;
                    Map<String, Object> sessionMap = invocation.getInvocationContext().getSession();
                    if (sessionMap.containsKey(WebVariable.I18N_ATTRIBUTE_NAME)) {
                        jsonResult.MakeI18NInfoMSG(I18nUtils.getLocaleFromString(sessionMap.get(WebVariable.I18N_ATTRIBUTE_NAME).toString()));
                    } else {
                        jsonResult.MakeI18NInfoMSG(I18nUtils.getLocaleFromString(WebVariable.DEFAULT_I18N));
                    }
                    json = JSONObject.fromObject(rootObject).toString();
                } else if (rootObject instanceof Object[]) {
                    json = JSONArray.fromObject(rootObject).toString();
                } else if (rootObject instanceof List) {
                    List objList = (List) rootObject;
                    json = JSONArray.fromObject(objList.toArray()).toString();
                } else if (rootObject instanceof Workbook) {
                    Workbook workBook = (Workbook) rootObject;
                    OutputStream os = response.getOutputStream();
                    try {
                        response.setContentType("application/vnd.ms-excel");
                        workBook.write(os);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (os != null) os.flush();
                    }
                    json = null;
                } else {
                    json = JSONObject.fromObject(rootObject).toString();
                }
            }
            if (json != null) {
                json = addCallbackIfApplicable(request, json);
                boolean writeGzip = this.isEnableGZIP() && JSONUtil.isGzipInRequest(request);
                writeToResponse(response, json, writeGzip);
            }
        } catch (IOException exception) {
            LOG.error(exception.getMessage(), exception);
            throw exception;
        }
    }
}
