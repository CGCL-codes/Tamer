package com.zhiyun.estore.website.service.impl;

import com.zhiyun.estore.common.service.impl.EntityServiceImpl;
import com.zhiyun.estore.common.vo.EbActivity;
import com.zhiyun.estore.website.service.ActivityService;

public class ActivityServiceImpl extends EntityServiceImpl<EbActivity> implements ActivityService {

    public ActivityServiceImpl() {
        setPojoName(EbActivity.class.getName());
    }
}
