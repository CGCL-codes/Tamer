package com.jeecms.sms.schedule;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.common.page.Pagination;

public class SmsJob {

    private static final Logger log = LoggerFactory.getLogger(SmsJob.class);

    public void execute() {
        try {
            manager.updateAll_topic_today();
            log.info("update updateAll_topic_today success!");
        } catch (Exception e) {
            log.info("update updateAll_topic_today fail!");
        }
    }

    @Autowired
    private BbsForumMng manager;
}
