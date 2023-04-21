package org.cmsuite2.business.validator;

import it.ec.commons.web.ValidateBean;
import it.ec.commons.web.ValidateException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.cmsuite2.model.employee.Employee;
import org.cmsuite2.model.payment.Payment;
import org.cmsuite2.model.store.Store;
import org.cmsuite2.model.supplier.Supplier;
import org.cmsuite2.model.supplier.SupplierDeferredBill;
import org.cmsuite2.model.supplier.SupplierPackList;

public class SupplierDeferredBillValidator {

    private Logger logger = Logger.getLogger(SupplierDeferredBillValidator.class);

    public void validateStep1(SupplierDeferredBill supplierDeferredBill) throws ValidateException {
        if (logger.isDebugEnabled()) logger.debug("validateStep1[" + supplierDeferredBill + "]");
        List<ValidateBean> errors = new ArrayList<ValidateBean>();
        if (supplierDeferredBill != null) {
            Employee employee = supplierDeferredBill.getEmployee();
            if (employee == null) errors.add(new ValidateBean("employeeId", "message.employee.em"));
            Payment payment = supplierDeferredBill.getPayment();
            if (payment == null) errors.add(new ValidateBean("paymentId", "message.payment.em"));
            Store store = supplierDeferredBill.getStore();
            if (store == null) errors.add(new ValidateBean("storeId", "message.store.em"));
            Supplier supplier = supplierDeferredBill.getSupplier();
            if (supplier == null) errors.add(new ValidateBean("supplierId", "message.supplier.em"));
        } else {
            throw new ValidateException();
        }
        if (errors.size() > 0) throw new ValidateException(errors);
    }

    public void validateStep2(SupplierDeferredBill supplierDeferredBill) throws ValidateException {
        if (logger.isDebugEnabled()) logger.debug("validateStep2[" + supplierDeferredBill + "]");
        List<ValidateBean> errors = new ArrayList<ValidateBean>();
        if (supplierDeferredBill != null) {
            List<SupplierPackList> supplierPackList = supplierDeferredBill.getSupplierPackLists();
            if (supplierPackList == null || supplierPackList.size() == 0) errors.add(new ValidateBean("packListId", "message.supplierpacklist.em"));
            String description = supplierDeferredBill.getDescription();
            if (StringUtils.isNotBlank(description) && description.length() > 1000) errors.add(new ValidateBean("supplierPackList.description", "message.description.nv"));
        } else {
            throw new ValidateException();
        }
        if (errors.size() > 0) throw new ValidateException(errors);
    }
}
