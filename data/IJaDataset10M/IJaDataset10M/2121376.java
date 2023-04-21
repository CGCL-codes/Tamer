package com.lms.admin.ovo;

import java.util.List;
import org.apache.struts.action.ActionErrors;
import com.lms.admin.orm.ApproveOrm;
import com.lms.util.PaginationBean;

/**
 * Created by G.Vijayaraja
 * 
 * lms
 */
public class ApproveOVO {

    private List<List> displayList = null;

    private PaginationBean paginationBean = null;

    private ActionErrors errors = null;

    private ApproveOrm approveOrm = null;

    public ApproveOrm getApproveOrm() {
        return this.approveOrm;
    }

    public void setApproveOrm(final ApproveOrm approveOrm) {
        this.approveOrm = approveOrm;
    }

    public List<List> getDisplayList() {
        return this.displayList;
    }

    public void setDisplayList(final List<List> displayList) {
        this.displayList = displayList;
    }

    public ActionErrors getErrors() {
        return this.errors;
    }

    public void setErrors(final ActionErrors errors) {
        this.errors = errors;
    }

    public PaginationBean getPaginationBean() {
        return this.paginationBean;
    }

    public void setPaginationBean(final PaginationBean paginationBean) {
        this.paginationBean = paginationBean;
    }
}
