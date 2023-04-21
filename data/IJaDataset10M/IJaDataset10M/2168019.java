package com.super4.dang.service;

import java.util.List;
import com.super4.dang.dao.DaoImplFactory;
import com.super4.dang.dao.RHNDao;

public class RHNService {

    private RHNDao dao = DaoImplFactory.getRHNDao();

    public List<?> findAllRHN(String type, Integer count) {
        return dao.findAllRHN(type, count);
    }
}
