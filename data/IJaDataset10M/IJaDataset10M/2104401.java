package ces.research.oa.document.interior.form;

import ces.arch.form.ListForm;

/**
 * 
 * <p>
 * Title: �Ѱ�/����ѯ�����?
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ProcessListQueryForm extends ListForm {

    private String doAction;

    private String orderby = "m.created_time";

    private String order = "desc";

    private String summary;

    private String createdTime_begin;

    private String createdTime_end;

    private String title;

    private String interiorNo;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public void setCreatedTime_begin(String createdTime_begin) {
        this.createdTime_begin = createdTime_begin;
    }

    public ProcessListQueryForm() {
    }

    public String getCreatedTime_begin() {
        return createdTime_begin;
    }

    public String getCreatedTime_end() {
        return createdTime_end;
    }

    public String getDoAction() {
        return doAction;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setDoAction(String doAction) {
        this.doAction = doAction;
    }

    public void setCreatedTime_end(String createdTime_end) {
        this.createdTime_end = createdTime_end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInteriorNo() {
        return interiorNo;
    }

    public void setInteriorNo(String interiorNo) {
        this.interiorNo = interiorNo;
    }
}
