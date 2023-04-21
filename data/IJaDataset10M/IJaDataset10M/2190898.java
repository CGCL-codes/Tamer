package com.aimluck.eip.modules.actions.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.jetspeed.modules.actions.portlets.VelocityPortletAction;
import org.apache.jetspeed.portal.PortletInstance;
import org.apache.jetspeed.portal.portlets.GenericMVCPortlet;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.jetspeed.services.persistence.PersistenceManager;
import org.apache.jetspeed.services.rundata.JetspeedRunData;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.common.ALEipConstants;
import com.aimluck.eip.services.orgutils.ALOrgUtilsService;
import com.aimluck.eip.services.portal.ALPortalApplicationService;
import com.aimluck.eip.util.ALCommonUtils;
import com.aimluck.eip.util.ALEipUtils;

/**
 * Velocity Portlet を扱う際の抽象クラスです。 <br />
 * 
 */
public abstract class ALBaseAction extends VelocityPortletAction implements ALAction {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(ALBaseAction.class.getName());

    /** 表示モード */
    private String mode;

    /** result */
    private Object result;

    /** 検索結果を格納するリスト */
    private List<Object> resultList;

    /** 異常系のメッセージを格納するリスト */
    private List<String> errmsgList;

    /**
   * 
   * @param obj
   */
    @Override
    public void setResultData(Object obj) {
        result = obj;
    }

    /**
   * 
   * @param obj
   */
    @Override
    public void addResultData(Object obj) {
        if (resultList == null) {
            resultList = new ArrayList<Object>();
        }
        resultList.add(obj);
    }

    /**
   * 
   * @param objList
   */
    @Override
    public void setResultDataList(List<Object> objList) {
        resultList = objList;
    }

    /**
   * 
   * @param msg
   */
    @Override
    public void addErrorMessage(String msg) {
        if (errmsgList == null) {
            errmsgList = new ArrayList<String>();
        }
        errmsgList.add(msg);
    }

    /**
   * 
   * @param msg
   */
    @Override
    public void addErrorMessages(List<String> msgs) {
        if (errmsgList == null) {
            errmsgList = new ArrayList<String>();
        }
        errmsgList.addAll(msgs);
    }

    /**
   * 
   * @param msgs
   */
    @Override
    public void setErrorMessages(List<String> msgs) {
        errmsgList = msgs;
    }

    /**
   * 
   * @param mode
   */
    @Override
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
   * 
   * @return
   */
    @Override
    public String getMode() {
        return mode;
    }

    /**
   * 
   * @param context
   */
    @Override
    public void putData(RunData rundata, Context context) {
        context.put(ALEipConstants.MODE, mode);
        context.put(ALEipConstants.RESULT, result);
        context.put(ALEipConstants.ERROR_MESSAGE_LIST, errmsgList);
        context.put(ALEipConstants.ENTITY_ID, ALEipUtils.getTemp(rundata, context, ALEipConstants.ENTITY_ID));
        context.put("utils", new ALCommonUtils());
        Map<String, String> attribute = ALOrgUtilsService.getParameters();
        for (Map.Entry<String, String> e : attribute.entrySet()) {
            context.put(e.getKey(), e.getValue());
        }
        context.put(ALEipConstants.SECURE_ID, rundata.getUser().getTemp(ALEipConstants.SECURE_ID));
    }

    @Override
    public void doPerform(RunData rundata, Context context) throws Exception {
        GenericMVCPortlet portlet = null;
        JetspeedRunData jdata = (JetspeedRunData) rundata;
        logger.debug("ALBaseAction: retrieved context: " + context);
        if (context != null) {
            portlet = (GenericMVCPortlet) context.get("portlet");
        }
        if (ALPortalApplicationService.isActive(portlet.getName())) {
            super.doPerform(rundata, context);
        } else {
            rundata.getRequest().setAttribute("redirectTemplate", "Inactive.vm");
        }
        PortletInstance portletInstance = PersistenceManager.getInstance(portlet, jdata);
        if (portletInstance == null) {
            context.put("portletInstanceTitle", portlet.getTitle());
        } else {
            context.put("portletInstanceTitle", portletInstance.getTitle());
        }
        String redirectTemplate = (String) rundata.getRequest().getAttribute("redirectTemplate");
        if (redirectTemplate != null && redirectTemplate.length() > 0) {
            setTemplate(rundata, redirectTemplate);
            rundata.getRequest().setAttribute("redirectTemplate", null);
        }
        putData(rundata, context);
    }
}
