package com.javaeye.plan.web;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.opensymphony.xwork2.ActionSupport;
import com.javaeye.common.service.ICodeService;
import com.javaeye.common.util.ListItem;
import com.javaeye.common.util.ListUtil;
import com.javaeye.common.web.PageInfo;
import com.javaeye.plan.dto.InStoreVoucher;
import com.javaeye.plan.dto.InStoreVoucherDetail;
import com.javaeye.plan.service.IInStoreVoucherService;
import com.javaeye.store.dto.Materials;
import com.javaeye.store.service.IMaterialsService;

public class InStoreVoucherAction extends ActionSupport {

    protected static Log log = LogFactory.getLog(InStoreVoucherAction.class);

    /**
	 * 
	 */
    private static final long serialVersionUID = -3216585574200112708L;

    /**
	 * 用于显示一览结果集
	 */
    private List<InStoreVoucher> voucherList;

    /**
	 * 用于接收分页值对象
	 */
    private PageInfo pageInfo;

    /**
	 * 用于接收查询条件值对象
	 */
    private VoucherCondition condition;

    /**
	 * 用于接收添加画面和编辑画面的值对象
	 */
    private InStoreVoucher voucher;

    /**
	 * 用于接收需要修改和删除的ID值
	 */
    private String voucherId;

    /**
	 * 用于保存在库品编码
	 */
    private List<String> productCodes;

    /**
	 * 用于保存在库品分类名称
	 */
    private List<String> rootCategoryNames;

    /**
	 * 用于保存在库品分类名称
	 */
    private List<String> categoryNames;

    /**
	 * 用于保存在库品生产批号
	 */
    private List<String> batchNos;

    /**
	 * 用于保存在库品送检数量
	 */
    private List<Float> productNums;

    /**
	 * 用于生成查询画面上选择单位的下拉框的值
	 */
    private List<ListItem> departmentList;

    private List<Integer> detailIds;

    private List<Integer> productIds;

    private List<String> productSkus;

    /**
	 * Service
	 */
    protected IInStoreVoucherService service;

    private IMaterialsService materialsService;

    protected ICodeService codeService;

    public void setService(IInStoreVoucherService service) {
        this.service = service;
    }

    public void setCodeService(ICodeService codeService) {
        this.codeService = codeService;
    }

    public void setMaterialsService(IMaterialsService materialsService) {
        this.materialsService = materialsService;
    }

    public InStoreVoucherAction() {
        pageInfo = new PageInfo();
        pageInfo.setPageIndex(0);
        condition = new VoucherCondition();
        condition.setBeginDate(null);
        condition.setEndDate(null);
        condition.setVoucherNo("");
        condition.setBatchNo("");
        condition.setSkuId(-1);
        condition.setStatus(-1);
        condition.setMaterialsName("");
        condition.setCheckNo("");
    }

    public String queryInStoreVoucherList() throws Exception {
        departmentList = ListUtil.baseCodeList(codeService, ListUtil.UNIT_CODE_DEPATMENT);
        voucherList = service.getVoucherList(condition, pageInfo);
        return SUCCESS;
    }

    public String queryForAddVoucher() throws Exception {
        departmentList = ListUtil.baseCodeList(codeService, ListUtil.UNIT_CODE_DEPATMENT);
        return SUCCESS;
    }

    public String queryInStoreVoucherInfo() throws Exception {
        voucher = service.getInStoreVoucher(voucherId);
        return SUCCESS;
    }

    public String queryInStoreVoucherForEdit() throws Exception {
        departmentList = ListUtil.baseCodeList(codeService, ListUtil.UNIT_CODE_DEPATMENT);
        voucher = service.getInStoreVoucher(voucherId);
        return SUCCESS;
    }

    /**
	 * 保存
	 * @return
	 * @throws Exception
	 */
    public String saveInStoreVoucher() throws Exception {
        voucher.setDeptName(ListUtil.baseCodeName(codeService, String.valueOf(voucher.getDeptId())));
        for (int i = 0; i < productCodes.size(); i++) {
            String productCode = productCodes.get(i);
            Materials product = materialsService.getMaterialsByCode(productCode);
            InStoreVoucherDetail detail = new InStoreVoucherDetail();
            detail.setProductId(productIds.get(i));
            detail.setProductNo(productCode);
            detail.setProductRootCategory(rootCategoryNames.get(i));
            detail.setProductCategory(categoryNames.get(i));
            detail.setProductName(product.getName());
            detail.setProductType(product.getType());
            detail.setProductSku(productSkus.get(i));
            detail.setUnit(product.getUnits());
            detail.setBatchNo(batchNos.get(i));
            detail.setNumber(productNums.get(i));
            if (detailIds != null && detailIds.size() > 0) {
                if (i < detailIds.size()) {
                    detail.setId(detailIds.get(i));
                }
            }
            voucher.addDetail(detail);
        }
        service.saveInStoreVoucher(voucher);
        return SUCCESS;
    }

    public String removeInStoreVoucher() throws Exception {
        service.removeInStoreVoucher(voucherId);
        return SUCCESS;
    }

    public List<InStoreVoucher> getVoucherList() {
        return voucherList;
    }

    public void setVoucherList(List<InStoreVoucher> voucherList) {
        this.voucherList = voucherList;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public VoucherCondition getCondition() {
        return condition;
    }

    public void setCondition(VoucherCondition condition) {
        this.condition = condition;
    }

    public InStoreVoucher getVoucher() {
        return voucher;
    }

    public void setVoucher(InStoreVoucher voucher) {
        this.voucher = voucher;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public List<String> getRootCategoryNames() {
        return rootCategoryNames;
    }

    public void setRootCategoryNames(List<String> rootCategoryNames) {
        this.rootCategoryNames = rootCategoryNames;
    }

    public List<String> getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames;
    }

    public List<String> getBatchNos() {
        return batchNos;
    }

    public void setBatchNos(List<String> batchNos) {
        this.batchNos = batchNos;
    }

    public List<Float> getProductNums() {
        return productNums;
    }

    public void setProductNums(List<Float> productNums) {
        this.productNums = productNums;
    }

    public List<ListItem> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<ListItem> departmentList) {
        this.departmentList = departmentList;
    }

    public List<String> getProductCodes() {
        return productCodes;
    }

    public void setProductCodes(List<String> productCodes) {
        this.productCodes = productCodes;
    }

    public List<Integer> getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(List<Integer> detailIds) {
        this.detailIds = detailIds;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }

    public List<String> getProductSkus() {
        return productSkus;
    }

    public void setProductSkus(List<String> productSkus) {
        this.productSkus = productSkus;
    }
}
