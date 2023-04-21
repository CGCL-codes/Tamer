package com.javaeye.lonlysky.lforum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.test.SpringTransactionalTestCase;

/**
 * 附件操作集成测试
 * 
 * @author 黄磊
 *
 */
public class AttachmentManagerTest extends SpringTransactionalTestCase {

    @Autowired
    private AttachmentManager attachmentManager;

    public void test() {
        assertEquals(0, attachmentManager.getUploadFileSizeByuserid(1));
    }
}
