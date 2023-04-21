package com.sisioh.erp.core.service;

import com.sisioh.erp.core.entity.partner.customer.CustomerSalesPrice;
import java.util.List;
import static com.sisioh.erp.core.names.CustomerSalesPriceNames.*;
import static org.seasar.extension.jdbc.operation.Operations.*;

/**
 * {@link CustomerSalesPrice}のサービスクラスです。
 * 
 * @author S2JDBC-Gen
 * @suppresshack com.google.code.hack.ej2.ToStringRewriter
 */
public class CustomerSalesPriceService extends AbstractService<CustomerSalesPrice> {

    /**
     * 識別子でエンティティを検索します。
     * 
     * @param customerSalesPriceId
     *            識別子
     * @return エンティティ
     */
    public CustomerSalesPrice findById(Long customerSalesPriceId) {
        return select().id(customerSalesPriceId).getSingleResult();
    }

    /**
     * 識別子とバージョン番号でエンティティを検索します。
     * 
     * @param customerSalesPriceId
     *            識別子
     * @param versionNo
     *            バージョン番号
     * @return エンティティ
     */
    public CustomerSalesPrice findByIdVersion(Long customerSalesPriceId, Long versionNo) {
        return select().id(customerSalesPriceId).version(versionNo).getSingleResult();
    }

    /**
     * 識別子の昇順ですべてのエンティティを検索します。
     * 
     * @return エンティティのリスト
     */
    public List<CustomerSalesPrice> findAllOrderById() {
        return select().orderBy(asc(customerSalesPriceId())).getResultList();
    }
}
