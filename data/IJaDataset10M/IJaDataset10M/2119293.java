package com.shine.sourceflow.service.show.detail;

import com.shine.sourceflow.dao.show.detail.AppTrafficDetailDao;
import com.shine.sourceflow.service.GenericService;

public class AppTrafficDetailService extends GenericService {

    public AppTrafficDetailService() {
        this.dao = new AppTrafficDetailDao();
    }
}
