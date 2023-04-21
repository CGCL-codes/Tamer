package com.medsol.reports.service;

import com.medsol.common.service.GenericServiceImpl;
import com.medsol.common.repository.GenericRepository;
import com.medsol.reports.repository.CustWProdSaleRepository;
import com.medsol.reports.detail.CustWProdSaleRep;
import java.util.Map;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: vinay
 * Date: 7 Sep, 2008
 * Time: 4:59:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustWProdSaleServiceImpl implements CustWProdSaleService {

    private CustWProdSaleRepository custWProdSaleRepository;

    public GenericRepository getRepository() {
        return custWProdSaleRepository;
    }

    public void setCustWProdSaleRepository(CustWProdSaleRepository custWProdSaleRepository) {
        this.custWProdSaleRepository = custWProdSaleRepository;
    }

    public List<CustWProdSaleRep> getCustWProdSaleReport(Map params) {
        return custWProdSaleRepository.getCustWProdSaleRepReport(params);
    }

    public List<CustWProdSaleRep> getPartyWiseSale(Map params) {
        return custWProdSaleRepository.getPartyWiseSale(params);
    }

    public List<CustWProdSaleRep> getAvgProdSale(Map params) {
        return custWProdSaleRepository.getAvgProdSale(params);
    }

    public List<CustWProdSaleRep> getCustSupWiseRep(Map params) {
        return custWProdSaleRepository.getCustSupWiseRep(params);
    }

    public List<CustWProdSaleRep> getNonPaidSI(Map params) {
        return custWProdSaleRepository.getNonPaidSI(params);
    }

    public List<CustWProdSaleRep> getCreditorRep(Map params) {
        return custWProdSaleRepository.getCreditorRep(params);
    }

    public List<CustWProdSaleRep> getNonPaidInvByCreditors(Map params) {
        return custWProdSaleRepository.getNonPaidInvByCreditors(params);
    }

    public List<CustWProdSaleRep> getNonPaidInvByDebtor(Map params) {
        return custWProdSaleRepository.getNonPaidInvByDebtor(params);
    }

    public List<CustWProdSaleRep> getDebtorRep(Map params) {
        return custWProdSaleRepository.getDebtorRep(params);
    }

    public List<CustWProdSaleRep> getTurnOverRep(Map params) {
        return custWProdSaleRepository.getTurnOverRep(params);
    }

    public List<CustWProdSaleRep> getNonPaidInvByCreditor(Map params) {
        return custWProdSaleRepository.getNonPaidInvByCreditor(params);
    }
}
