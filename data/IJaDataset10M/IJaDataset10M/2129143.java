package com.javaeye.plan.service;

import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.javaeye.plan.dao.MaterialRequisiteFormDAO;
import com.javaeye.plan.dto.MaterialRequisiteForm;
import com.javaeye.plan.dto.MaterialRequisiteFormDetail;
import com.javaeye.store.dao.MaterialsBatchDetailDAO;
import com.javaeye.store.dao.MaterialsChangeDetailDAO;
import com.javaeye.store.dao.MaterialsSKUDetailDAO;
import com.javaeye.store.dto.MaterialsBatchDetail;
import com.javaeye.store.dto.MaterialsChangeDetail;
import com.javaeye.store.dto.MaterialsSKUDetail;
import com.javaeye.common.util.DateUtils;
import com.javaeye.common.util.ListUtil;
import com.javaeye.delivery.dao.CheckOrderDAO;
import com.javaeye.delivery.dao.OrderDetailBatchInfoDAO;
import com.javaeye.delivery.dto.OrderCheck;
import com.javaeye.delivery.dto.OrderDetailBatchInfo;

public class BackMaterialRequisiteFormService implements IBackMaterialRequisiteFormService {

    private static Log log = LogFactory.getLog(BackMaterialRequisiteFormService.class);

    private CheckOrderDAO checkOrderDao;

    private MaterialRequisiteFormDAO dao;

    private MaterialsChangeDetailDAO materialsChangeDetailDao;

    private MaterialsSKUDetailDAO materialsSKUDetailDao;

    private MaterialsBatchDetailDAO materialsBatchDetailDao;

    private OrderDetailBatchInfoDAO orderDetailBatchInfoDao;

    public void setDao(MaterialRequisiteFormDAO dao) {
        this.dao = dao;
    }

    public void setOrderDetailBatchInfoDao(OrderDetailBatchInfoDAO orderDetailBatchInfoDao) {
        this.orderDetailBatchInfoDao = orderDetailBatchInfoDao;
    }

    public void setMaterialsChangeDetailDao(MaterialsChangeDetailDAO materialsChangeDetailDao) {
        this.materialsChangeDetailDao = materialsChangeDetailDao;
    }

    public void setMaterialsSKUDetailDao(MaterialsSKUDetailDAO materialsSKUDetailDao) {
        this.materialsSKUDetailDao = materialsSKUDetailDao;
    }

    public void setMaterialsBatchDetailDao(MaterialsBatchDetailDAO materialsBatchDetailDao) {
        this.materialsBatchDetailDao = materialsBatchDetailDao;
    }

    public void setCheckOrderDao(CheckOrderDAO checkOrderDao) {
        this.checkOrderDao = checkOrderDao;
    }

    /**
	 * 删除退料单
	 * @param formId
	 */
    public void removeBackMaterialRequisiteForm(String formId) {
        MaterialRequisiteForm form = dao.getMaterialRequisiteForm(formId);
        Set<MaterialRequisiteFormDetail> details = form.getFormDetails();
        for (MaterialRequisiteFormDetail detail : details) {
            List<OrderDetailBatchInfo> batchInfos = orderDetailBatchInfoDao.getBatchInfo(formId, detail.getMaterialId());
            for (OrderDetailBatchInfo batchInfo : batchInfos) {
                orderDetailBatchInfoDao.removeOrderDetailBatchInfo(batchInfo.getId());
            }
        }
        List<OrderCheck> checkResults = checkOrderDao.getCheckResult(formId);
        for (OrderCheck result : checkResults) {
            checkOrderDao.removeCheckResult(result);
        }
        dao.removeMaterialRequisiteForm(formId);
    }

    public void saveBackMaterialRequisiteCheckResult(OrderCheck checkResult) {
        checkOrderDao.saveCheckResult(checkResult);
        String formId = checkResult.getOrderId();
        MaterialRequisiteForm form = dao.getMaterialRequisiteForm(formId);
        if (Integer.parseInt(checkResult.getResult()) == 2) {
            form.setStatus(MaterialRequisiteForm.MATERIAL_REQUISITE_FORM_STATUS_CHECKED);
        } else {
            form.setStatus(MaterialRequisiteForm.MATERIAL_REQUISITE_FORM_WAIT_IN_STORE);
        }
        dao.saveMaterialRequisiteForm(form);
    }

    public void materialRequisiteFormInStore(String formId, List<OrderDetailBatchInfo> batchInfos) {
        MaterialRequisiteForm form = dao.getMaterialRequisiteForm(formId);
        form.setStatus(MaterialRequisiteForm.MATERIAL_REQUISITE_FORM_STATUS_FINISH);
        dao.saveMaterialRequisiteForm(form);
        MaterialRequisiteForm sourceForm = dao.getMaterialRequisiteForm(form.getSourceFormId());
        Set<MaterialRequisiteFormDetail> details = sourceForm.getFormDetails();
        for (MaterialRequisiteFormDetail detail : details) {
            detail.setBackNumber(detail.getBackNumber() + getBackNumber(detail.getMaterialId(), batchInfos));
        }
        dao.saveMaterialRequisiteForm(sourceForm);
        for (OrderDetailBatchInfo batchInfo : batchInfos) {
            OrderDetailBatchInfo sourceBatchInfo = orderDetailBatchInfoDao.getBatchInfo(sourceForm.getId(), batchInfo.getProductId(), batchInfo.getBatchNo());
            sourceBatchInfo.setBackNumber(sourceBatchInfo.getBackNumber() + batchInfo.getNumber());
            orderDetailBatchInfoDao.save(sourceBatchInfo);
            orderDetailBatchInfoDao.save(batchInfo);
            int materialsId = batchInfo.getProductId();
            MaterialsChangeDetail changeDetail = new MaterialsChangeDetail();
            changeDetail.setMaterialsId(materialsId);
            changeDetail.setVoucherNo(formId);
            if (form.getType() == MaterialRequisiteForm.FORM_TYPE_QUALIFIED_BACK) {
                changeDetail.setChangeType(MaterialsChangeDetail.MATERIALS_QUALIFIED_INSTORE);
            } else {
                changeDetail.setChangeType(MaterialsChangeDetail.MATERIALS_UNQUALIFIED_INSTORE);
            }
            changeDetail.setBatchNo(batchInfo.getBatchNo());
            changeDetail.setNumber(batchInfo.getNumber());
            materialsChangeDetailDao.save(changeDetail);
            MaterialsSKUDetail skuDetail = materialsSKUDetailDao.getMaterialsSKUDetail(materialsId);
            if (form.getType() == MaterialRequisiteForm.FORM_TYPE_QUALIFIED_BACK) {
                skuDetail.setNumber(skuDetail.getNumber() + batchInfo.getNumber());
            } else {
                skuDetail.setDefectiveNumber(skuDetail.getDefectiveNumber() + batchInfo.getNumber());
            }
            materialsSKUDetailDao.saveMaterialsSKUDetail(skuDetail);
            MaterialsBatchDetail batchDetail = materialsBatchDetailDao.getBatchDetail(materialsId, batchInfo.getBatchNo());
            if (batchDetail == null) {
                batchDetail = new MaterialsBatchDetail();
                batchDetail.setMaterialsId(materialsId);
                batchDetail.setBatchNo(batchInfo.getBatchNo());
                batchDetail.setType(MaterialsBatchDetail.MATERIALS_BATCH_TYPE_MATERIALS);
            }
            if (form.getType() == MaterialRequisiteForm.FORM_TYPE_QUALIFIED_BACK) {
                batchDetail.setInNumber(batchDetail.getInNumber() + batchInfo.getNumber());
            } else {
                batchDetail.setPlanNumber(batchDetail.getPlanNumber() + batchInfo.getNumber());
            }
            materialsBatchDetailDao.save(batchDetail);
        }
    }

    public void saveBackMaterialRequisiteForm(MaterialRequisiteForm form) {
        dao.saveMaterialRequisiteForm(form);
    }

    public void saveBackMaterialRequisiteForm(MaterialRequisiteForm qualifiedBackForm, MaterialRequisiteForm unqualifiedBackForm) {
        if (qualifiedBackForm != null) {
            String id = ListUtil.MATERIALS_REQUISITE_ORDER_ID_PRE + DateUtils.dateToString("yyyyMM");
            int maxId = dao.getMaxMaterialRequisiteFormId(id) + 1;
            id = id + String.format("%04d", maxId);
            log.debug("生成的合格品退料单编号：" + id);
            qualifiedBackForm.setId(id);
            qualifiedBackForm.setCreateDate(DateUtils.now());
            qualifiedBackForm.setType(MaterialRequisiteForm.FORM_TYPE_QUALIFIED_BACK);
            qualifiedBackForm.setStatus(MaterialRequisiteForm.MATERIAL_REQUISITE_FORM_STATUS_CREATE);
            dao.saveMaterialRequisiteForm(qualifiedBackForm);
        }
        if (unqualifiedBackForm != null) {
            String id = ListUtil.MATERIALS_REQUISITE_ORDER_ID_PRE + DateUtils.dateToString("yyyyMM");
            int maxId = dao.getMaxMaterialRequisiteFormId(id) + 1;
            id = id + String.format("%04d", maxId);
            log.debug("生成的不合格品退料单编号：" + id);
            unqualifiedBackForm.setId(id);
            unqualifiedBackForm.setCreateDate(DateUtils.now());
            unqualifiedBackForm.setType(MaterialRequisiteForm.FORM_TYPE_UNQUALIFIED_BACK);
            unqualifiedBackForm.setStatus(MaterialRequisiteForm.MATERIAL_REQUISITE_FORM_STATUS_CREATE);
            dao.saveMaterialRequisiteForm(unqualifiedBackForm);
        }
    }

    private float getBackNumber(int materialId, List<OrderDetailBatchInfo> batchInfos) {
        float totle = 0;
        for (OrderDetailBatchInfo info : batchInfos) {
            if (info.getProductId() == materialId) {
                totle += info.getNumber();
            }
        }
        return totle;
    }
}
