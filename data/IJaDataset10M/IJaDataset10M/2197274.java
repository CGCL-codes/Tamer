package com.medsol.reports.repository;

import com.medsol.common.repository.GenericRepositoryImpl;
import com.medsol.reports.detail.CustWProdSaleRep;
import com.medsol.reports.detail.ProductStockSt;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: vinay
 * Date: 7 Sep, 2008
 * Time: 4:57:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductStockRepository extends GenericRepositoryImpl<ProductStockSt, Long> {

    public List<ProductStockSt> getProductStockReport(Map params) {
        if (params.get("actionReasonCode") != null) {
            if (params.get("supplierId") != null) {
                return findUsingSQLQuery("productOpStockSt", ProductStockSt.class, params);
            } else {
                return findUsingSQLQuery("allProductOpStockSt", ProductStockSt.class, params);
            }
        }
        if (params.get("productId") != null) {
            if (params.get("fromDate") == null && params.get("toDate") == null) {
                return findUsingSQLQuery("aProductStockRep", ProductStockSt.class, params);
            } else {
                return findUsingSQLQuery("aProductStockSt", ProductStockSt.class, params);
            }
        }
        if (params.get("supplierId") != null) {
            return findUsingSQLQuery("productStockSt", ProductStockSt.class, params);
        } else {
            return findUsingSQLQuery("allProductStockSt", ProductStockSt.class, params);
        }
    }

    public List<ProductStockSt> getBatchDetForProd(Map params) {
        if (params.get("productId") != null) {
            return findUsingSQLQuery("prodBatchStock", ProductStockSt.class, params);
        }
        return null;
    }

    public List<ProductStockSt> getProdStockByExpiry(Map params) {
        if (params.get("supplierId") != null) {
            return findUsingSQLQuery("prodStockBySupExpiry", ProductStockSt.class, params);
        } else {
            return findUsingSQLQuery("prodStockByExpiry", ProductStockSt.class, params);
        }
    }

    public List<ProductStockSt> getLastInvSalePrice(Map params) {
        if (params.get("productId") != null && params.get("customerId") != null) {
            return findUsingSQLQuery("LastSaleDetail", ProductStockSt.class, params);
        }
        return null;
    }

    public List<ProductStockSt> getStockLedgerData(Map params) {
        return findUsingSQLQuery("stockLedgerSummary", ProductStockSt.class, params);
    }

    public List<ProductStockSt> getAgeWiseStock(Map params) {
        if (params.get("supplierId") != null) {
            return findUsingSQLQuery("ageSupWiseStock", ProductStockSt.class, params);
        }
        return findUsingSQLQuery("ageWiseStock", ProductStockSt.class, params);
    }

    public List<ProductStockSt> getMonthWiseSale(Map params) {
        return findUsingSQLQuery("monthWiseProdSale", ProductStockSt.class, params);
    }

    public List<ProductStockSt> getCustMonthWiseSale(Map params) {
        if (params.get("supplierId") != null) {
            return findUsingSQLQuery("custMonthWiseSupProdSale", ProductStockSt.class, params);
        } else {
            return findUsingSQLQuery("custMonthWiseProdSale", ProductStockSt.class, params);
        }
    }

    public List<ProductStockSt> getStockValueRep(Map params) {
        if (params.get("closingInd") == "C") {
            return findUsingSQLQuery("closingStockValueRepBySupplier", ProductStockSt.class, params);
        } else {
            return findUsingSQLQuery("openingStockValueRepBySupplier", ProductStockSt.class, params);
        }
    }
}
