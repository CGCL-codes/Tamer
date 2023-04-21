package com.devunion.salon.web.action;

import com.devunion.salon.persistence.CashInOut;
import com.devunion.salon.persistence.Payment;
import com.devunion.salon.persistence.dao.CashInOutDao;
import com.devunion.salon.persistence.dao.LocationDao;
import com.devunion.salon.util.DateFormatter;
import com.devunion.salon.util.TransactionCodeGenerator;
import com.devunion.salon.web.bean.CashInOutBean;
import com.devunion.salon.web.bean.PaymentBean;
import com.devunion.salon.web.form.CashInOutEditForm;
import com.devunion.salon.web.form.CashInOutForm;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * @author Timoshenko Alexander
 */
public class CashInOutAction extends CoreAction {

    private CashInOutDao cashInOutDao;

    private LocationDao locationDao;

    public void setCashInOutDao(CashInOutDao cashInOutDao) {
        this.cashInOutDao = cashInOutDao;
    }

    public void setLocationDao(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    public ActionForward welcome(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        CashInOutForm form = (CashInOutForm) actionForm;
        Collection<CashInOut> caches = cashInOutDao.getCashByDate(DateFormatter.getSQLDate(form.getHiddenDate()), getUsername());
        for (CashInOut cache : caches) {
            CashInOutBean bean = new CashInOutBean(cache);
            bean.setPaymentBean(new PaymentBean(cache.getPayment()));
            form.getCaches().add(bean);
        }
        return actionMapping.getInputForward();
    }

    public ActionForward save(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        CashInOutForm form = (CashInOutForm) actionForm;
        CashInOut cashInOut = new CashInOut();
        cashInOut.setCreated(DateFormatter.getSQLDate(form.getHiddenDate()));
        cashInOut.setNumber(TransactionCodeGenerator.next());
        cashInOut.setLocation(locationDao.getLocation(getUsername()));
        Payment payment = new Payment();
        if (!StringUtils.isBlank(form.getCashIn())) {
            payment.setValue(Float.valueOf(form.getCashIn()));
            payment.setMethod("cashin");
            cashInOut.setNotes(form.getCashInNotes());
        } else {
            payment.setValue(Float.valueOf(form.getCashOut()));
            payment.setMethod("cashout");
            cashInOut.setNotes(form.getCashOutNotes());
        }
        cashInOut.setPayment(payment);
        cashInOutDao.save(cashInOut);
        return actionMapping.findForward("success");
    }

    public ActionForward edit(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        CashInOutEditForm form = (CashInOutEditForm) actionForm;
        if (!StringUtils.isBlank(form.getSubmittedValue())) {
            CashInOut cashInOut = cashInOutDao.getCashByNumber(getUsername(), form.getNumber());
            if (cashInOut != null) {
                cashInOut.setNotes(form.getNotes());
                cashInOut.getPayment().setMethod(form.getPaymentBean().getMethod());
                cashInOut.getPayment().setValue(form.getPaymentBean().getAmount());
                cashInOutDao.update(cashInOut);
                return actionMapping.findForward("success");
            }
        }
        form.getMethods().add(new LabelValueBean("cashin", "cashin"));
        form.getMethods().add(new LabelValueBean("cashout", "cashout"));
        return actionMapping.getInputForward();
    }

    public ActionForward delete(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        CashInOutEditForm form = (CashInOutEditForm) actionForm;
        CashInOut cashInOut = cashInOutDao.getCashByNumber(getUsername(), form.getNumber());
        if (cashInOut != null) {
            cashInOutDao.delete(cashInOut);
        }
        return actionMapping.findForward("success");
    }
}
