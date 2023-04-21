package com.teliose.logic.administrator;

import java.util.List;
import org.apache.log4j.Logger;
import com.teliose.entity.persistence.administrator.TblCustomer;
import com.teliose.entity.result.administrator.CustomerResult;
import com.teliose.entity.result.util.PagingResult;
import com.teliose.entity.type.ResultStatus;
import com.teliose.logic.AbstractLogicImpl;

public class CustomerLogicImpl extends AbstractLogicImpl implements CustomerLogic {

    private List<TblCustomer> customerList;

    private Logger logger = Logger.getLogger(CustomerLogicImpl.class);

    @Override
    public CustomerResult saveCustomer(TblCustomer customer) {
        CustomerResult result = new CustomerResult();
        try {
            TblCustomer savedCustomer = daoFactory.getTblCustomerDAO().save(customer);
            if (savedCustomer != null) {
                result.setCustomer(savedCustomer);
                result.setStatus(ResultStatus.SUCCESS);
                return result;
            } else {
                result.setStatus(ResultStatus.INPUT);
                return result;
            }
        } catch (Exception e) {
            result.setStatus(ResultStatus.ERROR);
            return result;
        }
    }

    @Override
    public CustomerResult editCustomer(TblCustomer customer) {
        CustomerResult result = new CustomerResult();
        try {
            TblCustomer editedCustomer = daoFactory.getTblCustomerDAO().edit(customer);
            if (editedCustomer != null) {
                result.setCustomer(editedCustomer);
                result.setStatus(ResultStatus.SUCCESS);
                return result;
            } else {
                result.setStatus(ResultStatus.INPUT);
                return result;
            }
        } catch (Exception e) {
            result.setStatus(ResultStatus.ERROR);
            return result;
        }
    }

    @Override
    public CustomerResult deleteCustomer(TblCustomer customer) {
        CustomerResult result = new CustomerResult();
        try {
            daoFactory.getTblCustomerDAO().delete(customer);
            result.setStatus(ResultStatus.SUCCESS);
            return result;
        } catch (Exception e) {
            result.setStatus(ResultStatus.ERROR);
            return result;
        }
    }

    @Override
    public CustomerResult getAllCustomers() {
        CustomerResult result = new CustomerResult();
        try {
            List<TblCustomer> customers = daoFactory.getTblCustomerDAO().getAllCustomers();
            if (customers.size() != 0) {
                result.setCustomers(customers);
                result.setStatus(ResultStatus.SUCCESS);
                return result;
            } else {
                result.setStatus(ResultStatus.INPUT);
                return result;
            }
        } catch (Exception e) {
            result.setStatus(ResultStatus.ERROR);
            return result;
        }
    }

    @Override
    public CustomerResult findById(String customerId) {
        CustomerResult result = new CustomerResult();
        try {
            TblCustomer customer = daoFactory.getTblCustomerDAO().findByCustomerId(customerId);
            if (customer != null) {
                result.setCustomer(customer);
                result.setStatus(ResultStatus.SUCCESS);
                return result;
            } else {
                result.setCustomer(customer);
                result.setStatus(ResultStatus.INPUT);
                return result;
            }
        } catch (Exception e) {
            result.setStatus(ResultStatus.ERROR);
            return result;
        }
    }

    @Override
    public CustomerResult getCustomerResultFirstPage(int pageSize) {
        CustomerResult result = new CustomerResult();
        List<TblCustomer> customerList;
        PagingResult pagingResult = new PagingResult();
        int recordSize;
        int pageMin;
        try {
            recordSize = daoFactory.getTblCustomerDAO().recordSize();
            utilFactory.getPager().setRecordSize(recordSize);
            utilFactory.getPager().setPageSize(pageSize);
            pagingResult = utilFactory.getPager().firstPage();
            pageMin = pagingResult.getPageMin();
            customerList = daoFactory.getTblCustomerDAO().getCustomerList(pageMin, pageSize);
            if (customerList.size() > -1) {
                result.setCustomers(customerList);
                result.setStatus(ResultStatus.SUCCESS);
                return result;
            } else {
                result.setStatus(ResultStatus.ERROR);
                return result;
            }
        } catch (Exception e) {
            result.setStatus(ResultStatus.ERROR);
            logger.error("Exception =" + e);
        }
        return result;
    }

    @Override
    public CustomerResult getCustomerResultLastPage(int pageSize) {
        CustomerResult result = new CustomerResult();
        List<TblCustomer> customerList;
        PagingResult pagingResult = new PagingResult();
        int recordSize;
        int pageMin;
        try {
            recordSize = daoFactory.getTblTaxCategoryDAO().recordSize();
            utilFactory.getPager().setRecordSize(recordSize);
            utilFactory.getPager().setPageSize(pageSize);
            pagingResult = utilFactory.getPager().lastPage();
            pageMin = pagingResult.getPageMin();
            customerList = daoFactory.getTblCustomerDAO().getCustomerList(pageMin, pageSize);
            if (customerList.size() > -1) {
                result.setCustomers(customerList);
                result.setStatus(ResultStatus.SUCCESS);
                return result;
            } else {
                result.setStatus(ResultStatus.ERROR);
                return result;
            }
        } catch (Exception e) {
            result.setStatus(ResultStatus.ERROR);
            logger.error("Exception =" + e);
        }
        return result;
    }

    @Override
    public CustomerResult getCustomerResultNextPage(int pageSize) {
        CustomerResult result = new CustomerResult();
        List<TblCustomer> customerList;
        PagingResult pagingResult = new PagingResult();
        int recordSize;
        int pageMin;
        try {
            recordSize = daoFactory.getTblTaxCategoryDAO().recordSize();
            utilFactory.getPager().setRecordSize(recordSize);
            utilFactory.getPager().setPageSize(pageSize);
            pagingResult = utilFactory.getPager().nextPage();
            pageMin = pagingResult.getPageMin();
            customerList = daoFactory.getTblCustomerDAO().getCustomerList(pageMin, pageSize);
            if (customerList.size() > -1) {
                result.setCustomers(customerList);
                result.setStatus(ResultStatus.SUCCESS);
                return result;
            } else {
                result.setStatus(ResultStatus.ERROR);
                return result;
            }
        } catch (Exception e) {
            result.setStatus(ResultStatus.ERROR);
            logger.error("Exception =" + e);
        }
        return result;
    }

    @Override
    public CustomerResult getCustomerResultPreviousePage(int pageSize) {
        CustomerResult result = new CustomerResult();
        List<TblCustomer> customerList;
        PagingResult pagingResult = new PagingResult();
        int recordSize;
        int pageMin;
        try {
            recordSize = daoFactory.getTblTaxCategoryDAO().recordSize();
            utilFactory.getPager().setRecordSize(recordSize);
            utilFactory.getPager().setPageSize(pageSize);
            pagingResult = utilFactory.getPager().previousPage();
            pageMin = pagingResult.getPageMin();
            customerList = daoFactory.getTblCustomerDAO().getCustomerList(pageMin, pageSize);
            if (customerList.size() > -1) {
                result.setCustomers(customerList);
                result.setStatus(ResultStatus.SUCCESS);
                return result;
            } else {
                result.setStatus(ResultStatus.ERROR);
                return result;
            }
        } catch (Exception e) {
            result.setStatus(ResultStatus.ERROR);
            logger.error("Exception =" + e);
        }
        return result;
    }
}
