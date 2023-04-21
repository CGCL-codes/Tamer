package com.teliose.logic.administrator;

import java.util.List;
import org.apache.log4j.Logger;
import com.teliose.entity.persistence.administrator.TblTaxCategory;
import com.teliose.entity.result.BaseResult;
import com.teliose.entity.result.administrator.TaxResult;
import com.teliose.entity.result.util.PagingResult;
import com.teliose.entity.type.ResultStatus;
import com.teliose.logic.AbstractLogicImpl;
import com.teliose.parameter.administrator.TaxSearchParam;
import com.teliose.util.UniqueIdGenerator;

/**
 * 
 * @author Prabath ariyarathna
 */
public class TaxLogicImpl extends AbstractLogicImpl implements TaxLogic {

    private Logger logger = Logger.getLogger(TaxLogicImpl.class);

    @Override
    public BaseResult addTaxCategory(TblTaxCategory taxCategoryDetails) {
        BaseResult result = new BaseResult();
        UniqueIdGenerator id = new UniqueIdGenerator();
        try {
            if (!taxCategoryDetails.equals(null)) {
                taxCategoryDetails.setTaxCategoryId(id.getUniqueId());
                TblTaxCategory taxDetails = daoFactory.getTblTaxCategoryDAO().save(taxCategoryDetails);
                if (!taxDetails.equals(null)) {
                    result.setStatus(ResultStatus.SUCCESS);
                } else {
                    result.setStatus(ResultStatus.ERROR);
                }
            } else {
                result.setStatus(ResultStatus.ERROR);
            }
        } catch (Exception e) {
            logger.error("Exception =" + e);
        }
        return result;
    }

    @Override
    public TaxResult getTaxCategoryListFirstPage(int pageSize) {
        TaxResult result = new TaxResult();
        List<TblTaxCategory> taxCategoryList;
        PagingResult pagingResult = new PagingResult();
        int recordSize;
        int pageMin;
        try {
            recordSize = daoFactory.getTblTaxCategoryDAO().recordSize();
            utilFactory.getPager().setRecordSize(recordSize);
            utilFactory.getPager().setPageSize(pageSize);
            pagingResult = utilFactory.getPager().firstPage();
            pageMin = pagingResult.getPageMin();
            taxCategoryList = daoFactory.getTblTaxCategoryDAO().getTaxCategoryList(pageMin, pageSize);
            if (taxCategoryList.size() > -1) {
                result.setTaxCategoryList(taxCategoryList);
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
    public TaxResult getTaxCategoryListLastPage(int pageSize) {
        TaxResult result = new TaxResult();
        List<TblTaxCategory> taxCategoryList;
        PagingResult pagingResult = new PagingResult();
        int recordSize;
        int pageMin;
        try {
            recordSize = daoFactory.getTblTaxCategoryDAO().recordSize();
            utilFactory.getPager().setRecordSize(recordSize);
            utilFactory.getPager().setPageSize(pageSize);
            pagingResult = utilFactory.getPager().lastPage();
            pageMin = pagingResult.getPageMin();
            taxCategoryList = daoFactory.getTblTaxCategoryDAO().getTaxCategoryList(pageMin, pageSize);
            if (taxCategoryList.size() > -1) {
                result.setTaxCategoryList(taxCategoryList);
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
    public TaxResult getTaxCategoryListNextPage(int pageSize) {
        TaxResult result = new TaxResult();
        List<TblTaxCategory> taxCategoryList;
        PagingResult pagingResult = new PagingResult();
        int recordSize;
        int pageMin;
        try {
            recordSize = daoFactory.getTblTaxCategoryDAO().recordSize();
            utilFactory.getPager().setRecordSize(recordSize);
            utilFactory.getPager().setPageSize(pageSize);
            pagingResult = utilFactory.getPager().nextPage();
            pageMin = pagingResult.getPageMin();
            taxCategoryList = daoFactory.getTblTaxCategoryDAO().getTaxCategoryList(pageMin, pageSize);
            if (taxCategoryList.size() > -1) {
                result.setTaxCategoryList(taxCategoryList);
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
    public TaxResult getTaxCategoryListPreviousePage(int pageSize) {
        TaxResult result = new TaxResult();
        List<TblTaxCategory> taxCategoryList;
        PagingResult pagingResult = new PagingResult();
        int recordSize;
        int pageMin;
        try {
            recordSize = daoFactory.getTblTaxCategoryDAO().recordSize();
            utilFactory.getPager().setRecordSize(recordSize);
            utilFactory.getPager().setPageSize(pageSize);
            pagingResult = utilFactory.getPager().previousPage();
            pageMin = pagingResult.getPageMin();
            taxCategoryList = daoFactory.getTblTaxCategoryDAO().getTaxCategoryList(pageMin, pageSize);
            if (taxCategoryList.size() > -1) {
                result.setTaxCategoryList(taxCategoryList);
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
    public TaxResult findById(String id) {
        TaxResult result = new TaxResult();
        try {
            TblTaxCategory taxCategory = daoFactory.getTblTaxCategoryDAO().findById(id);
            if (!taxCategory.equals(null)) {
                result.setTblTaxCategory(taxCategory);
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
    public TaxResult deleteTaxCategory(String id) {
        TaxResult result = new TaxResult();
        try {
            TblTaxCategory taxCategoryDetails = daoFactory.getTblTaxCategoryDAO().findById(id);
            daoFactory.getTblTaxCategoryDAO().makeTransient(taxCategoryDetails);
            result.setStatus(ResultStatus.SUCCESS);
        } catch (Exception e) {
            result.setStatus(ResultStatus.ERROR);
            logger.error("Exception =" + e);
        }
        return result;
    }

    @Override
    public boolean checkTaxCategoryName(String name) {
        boolean status = false;
        try {
            List<TblTaxCategory> taxDetails = daoFactory.getTblTaxCategoryDAO().findByName(name);
            if (taxDetails.size() > 0) {
                status = true;
            }
        } catch (Exception e) {
            logger.error("Exception =" + e);
        }
        return status;
    }

    @Override
    public TaxResult search(TaxSearchParam param) {
        TaxResult result = new TaxResult();
        int count = 0;
        try {
            List<TblTaxCategory> taxresult = daoFactory.getTblTaxCategoryDAO().search(param);
            count = daoFactory.getTblTaxCategoryDAO().count(param);
            result.setStatus(ResultStatus.SUCCESS);
            result.setTaxCategoryList(taxresult);
        } catch (Exception e) {
            result.setStatus(ResultStatus.ERROR);
            logger.error("Exception =" + e);
        }
        return result;
    }
}
