package com.sisioh.erp.core.service;

import com.sisioh.erp.core.entity.partner.customer.CustomerInChargeProfileContact;
import java.util.List;
import static com.sisioh.erp.core.names.CustomerInChargeProfileContactNames.*;
import static org.seasar.extension.jdbc.operation.Operations.*;

/**
 * {@link CustomerInChargeProfileContact}のサービスクラスです。
 * 
 * @author S2JDBC-Gen
 * @suppresshack com.google.code.hack.ej2.ToStringRewriter
 */
public class CustomerInChargeProfileContactService extends AbstractService<CustomerInChargeProfileContact> {

    /**
     * 識別子でエンティティを検索します。
     * 
     * @param customerInChargeId
     *            識別子
     * @return エンティティ
     */
    public CustomerInChargeProfileContact findById(Long customerInChargeId) {
        return select().id(customerInChargeId).getSingleResult();
    }

    /**
     * 識別子とバージョン番号でエンティティを検索します。
     * 
     * @param customerInChargeId
     *            識別子
     * @param versionNo
     *            バージョン番号
     * @return エンティティ
     */
    public CustomerInChargeProfileContact findByIdVersion(Long customerInChargeId, Long versionNo) {
        return select().id(customerInChargeId).version(versionNo).getSingleResult();
    }

    /**
     * 識別子の昇順ですべてのエンティティを検索します。
     * 
     * @return エンティティのリスト
     */
    public List<CustomerInChargeProfileContact> findAllOrderById() {
        return select().orderBy(asc(customerInChargeId())).getResultList();
    }
}
