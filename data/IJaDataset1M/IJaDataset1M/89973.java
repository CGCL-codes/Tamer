package com.javaeye.store.web;

import java.util.ArrayList;
import java.util.List;
import com.javaeye.common.util.ListItem;
import com.javaeye.store.dto.MaterialsBatchDetail;
import com.javaeye.store.service.IMaterialsBatchInfoService;
import com.opensymphony.xwork2.ActionSupport;

public class MaterialsBatchInfoAction extends ActionSupport {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1998966732318406119L;

    private IMaterialsBatchInfoService service;

    private List<MaterialsBatchDetail> batchInfoList;

    private int materialsId;

    private List<ListItem> batchItems;

    public MaterialsBatchInfoAction() {
        materialsId = -1;
    }

    public void setService(IMaterialsBatchInfoService service) {
        this.service = service;
    }

    public String getNotFinishProductBatchInfo() throws Exception {
        List<String> batchNos = service.getNotFinishProductBatchInfo();
        batchItems = new ArrayList<ListItem>();
        for (String batchNo : batchNos) {
            batchItems.add(new ListItem(batchNo, batchNo));
        }
        return SUCCESS;
    }

    public String queryBatchInfoJOSN() throws Exception {
        batchInfoList = service.getNotFinishBatchInfo(materialsId);
        return SUCCESS;
    }

    public String queryNotEmptyMeterialsBatchInfoJOSN() throws Exception {
        batchInfoList = service.getNotEmptyMeterialsBatchInfo(materialsId);
        return SUCCESS;
    }

    public List<MaterialsBatchDetail> getBatchInfoList() {
        return batchInfoList;
    }

    public void setBatchInfoList(List<MaterialsBatchDetail> batchInfoList) {
        this.batchInfoList = batchInfoList;
    }

    public int getMaterialsId() {
        return materialsId;
    }

    public void setMaterialsId(int materialsId) {
        this.materialsId = materialsId;
    }

    public List<ListItem> getBatchItems() {
        return batchItems;
    }

    public void setBatchItems(List<ListItem> batchItems) {
        this.batchItems = batchItems;
    }
}
