package com.hand.action.up;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.hand.model.TAccountAdm;
import com.hand.model.TGeneralMasterDataAdm;
import com.hand.service.IAgentService;
import com.hand.service.IMobileBackedService;
import com.hand.utils.data.MapDatas;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 系统名：HCSMobileApp
 * 子系统名：添加手机后台数据信息
 * 著作权：COPYRIGHT (C) 2011 HAND 
 *			INFORMATION SYSTEMS CORPORATION  ALL RIGHTS RESERVED.
 * @author nianchun.li
 * @createTime May 30, 2011
 */
public class AddGmdaAction extends ActionSupport {

    private IMobileBackedService mobileBackedService;

    private IAgentService agentService;

    private String contentCN;

    private String contentJA;

    private String dataCode;

    private String dataType;

    private int type;

    private int result;

    @Override
    public String execute() throws Exception {
        MapDatas mapDatas = new MapDatas();
        dataType = mapDatas.getDataType().get((short) type);
        int code = Integer.parseInt(dataCode);
        List<TGeneralMasterDataAdm> gmdaList = mobileBackedService.getMobileBackedById(dataType, code);
        if (null != gmdaList && !gmdaList.isEmpty()) {
            result = 2;
            return SUCCESS;
        }
        boolean isSuccess = true;
        ActionContext actionContext = ActionContext.getContext();
        Map<String, Object> session = actionContext.getSession();
        String userId = (String) session.get("userId");
        TAccountAdm accountAdm = agentService.getAgentByUserId(userId);
        TGeneralMasterDataAdm gmda = new TGeneralMasterDataAdm();
        gmda.setAgentIdC(accountAdm.getAgentIdC());
        gmda.setCreateDate(new Date());
        gmda.setCreateOwner(userId);
        gmda.setDataCode(code);
        gmda.setDataType(dataType);
        gmda.setDataValue(contentCN);
        gmda.setDelFlag((short) 0);
        gmda.setLangTypeC("CN");
        if (0 == mobileBackedService.addMobileBacked(gmda)) {
            isSuccess = false;
        }
        gmda.setDataValue(contentJA);
        gmda.setLangTypeC("JA");
        if (0 == mobileBackedService.addMobileBacked(gmda)) {
            isSuccess = false;
        }
        if (isSuccess) {
            result = 1;
        }
        return SUCCESS;
    }

    public void setMobileBackedService(IMobileBackedService mobileBackedService) {
        this.mobileBackedService = mobileBackedService;
    }

    public void setAgentService(IAgentService agentService) {
        this.agentService = agentService;
    }

    public void setContentCN(String contentCN) {
        this.contentCN = contentCN;
    }

    public void setContentJA(String contentJA) {
        this.contentJA = contentJA;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResult() {
        return result;
    }
}
