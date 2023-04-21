package com.medsol.gr.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.medsol.gr.model.GRVoucher;
import com.medsol.gr.model.GRVoucherPK;
import com.medsol.gr.repository.GRVoucherRepository;
import com.medsol.common.service.GenericServiceImpl;

/**
 * Created by IntelliJ IDEA.
 * User: Naraya
 * Date: May 24, 2008
 * Time: 3:47:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class GRVoucherServiceImpl extends GenericServiceImpl<GRVoucher, GRVoucherPK, Exception> implements GRVoucherService {

    public GRVoucherRepository grVoucherRepository;

    public GRVoucherRepository getRepository() {
        return grVoucherRepository;
    }

    public void setGrVoucherRepository(GRVoucherRepository grVoucherRepository) {
        this.grVoucherRepository = grVoucherRepository;
    }

    public List<GRVoucher> getGRVoucher(Map params) {
        return this.grVoucherRepository.getGRVoucher(params);
    }
}
