package com.medsol.account.service;

import java.util.List;
import com.medsol.account.model.VoucherType;
import com.medsol.account.repository.VoucherTypeRepository;
import com.medsol.common.repository.GenericRepository;
import com.medsol.common.service.GenericServiceImpl;

/**
 * Created by IntelliJ IDEA.
 * User: me
 * Date: 15 May, 2008
 * Time: 6:59:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class VoucherTypeServiceImpl extends GenericServiceImpl<VoucherType, Long, Exception> implements VoucherTypeService {

    private VoucherTypeRepository voucherTypeRepository;

    public GenericRepository getRepository() {
        return voucherTypeRepository;
    }

    public void setVoucherTypeRepository(VoucherTypeRepository voucherTypeRepository) {
        this.voucherTypeRepository = voucherTypeRepository;
    }

    public List<VoucherType> getVoucherTypeList() {
        return voucherTypeRepository.getVoucherTypeList();
    }
}
