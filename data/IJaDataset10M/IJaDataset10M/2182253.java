package com.javaeye.delivery.web;

import java.util.Date;
import java.util.List;
import java.util.Set;
import com.javaeye.delivery.dto.CustomerOrder;
import com.javaeye.delivery.dto.OrderDetail;

public class PlanAction extends OrderAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7408989332825417843L;

    private List<Integer> packageNumbers;

    private List<Date> finishDates;

    private List<String> changeReasons;

    private List<String> batchNumbers;

    /**
	 * 查询需要安排计划的出货单列表
	 * @return
	 * @throws Exception
	 */
    public String queryPlanList() throws Exception {
        getCondition().setStatus(new Integer[] { CustomerOrder.ORDER_STATES_CREATED });
        return queryOrderList();
    }

    /**
	 * 查询用于安排计划
	 * @return
	 * @throws Exception
	 */
    public String queryOrderForPlan() throws Exception {
        return queryOrderBaseInfo();
    }

    /**
	 * 保存安排计划
	 * @return
	 * @throws Exception
	 */
    public String saveOrderPlan() throws Exception {
        CustomerOrder order = getService().getOrderBaseInfo(getOrderId());
        List<Integer> productNums = getProductNums();
        Set<OrderDetail> details = order.getOrderDetails();
        int index = 0;
        double nowNumber, changePrecent;
        for (OrderDetail detail : details) {
            nowNumber = productNums.get(index);
            changePrecent = nowNumber / detail.getNumber();
            if (changePrecent <= 0.95 || changePrecent >= 1.05) {
                if (changeReasons.get(index) == null || "".equals(changeReasons.get(index))) {
                    addActionError("[" + detail.getProductName() + "]计划数量修改超过5%, 必须填原因");
                    queryOrderForPlan();
                    return INPUT;
                }
            }
            index++;
        }
        index = 0;
        for (OrderDetail detail : details) {
            detail.setNumber(productNums.get(index));
            detail.setPackageNumber(packageNumbers.get(index));
            detail.setFinishDate(finishDates.get(index));
            if (changeReasons != null) {
                detail.setChangeReason(changeReasons.get(index));
            }
            index++;
        }
        getService().saveOrder(order);
        return SUCCESS;
    }

    public List<Integer> getPackageNumbers() {
        return packageNumbers;
    }

    public void setPackageNumbers(List<Integer> packageNumbers) {
        this.packageNumbers = packageNumbers;
    }

    public List<Date> getFinishDates() {
        return finishDates;
    }

    public void setFinishDates(List<Date> finishDates) {
        this.finishDates = finishDates;
    }

    public List<String> getChangeReasons() {
        return changeReasons;
    }

    public void setChangeReasons(List<String> changeReasons) {
        this.changeReasons = changeReasons;
    }

    /**
	 * @param batchNumbers the batchNumbers to set
	 */
    public void setBatchNumbers(List<String> batchNumbers) {
        this.batchNumbers = batchNumbers;
    }

    /**
	 * @return the batchNumbers
	 */
    public List<String> getBatchNumbers() {
        return batchNumbers;
    }
}
