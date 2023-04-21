package com.javaeye.lonlysky.lforum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.test.SpringTransactionalTestCase;

/**
 * 论坛板块集成测试用例
 * 
 * @author 黄磊
 *
 */
public class ForumManagerTest extends SpringTransactionalTestCase {

    @Autowired
    private ForumManager forumManager;

    public void test() {
        forumManager.getIndexPageForum(1, 1, 1, null);
    }
}
