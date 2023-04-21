package org.stars.dao.config;

import org.junit.Test;
import org.stars.config.Message;
import org.stars.config.MessageBundle;
import org.stars.config.MessageBundleImpl;
import org.stars.dao.sqlmapper.parser.BaseTest;

public class MessageBundleTest extends BaseTest {

    @Test
    public void test01() {
        MessageBundle bundle = new MessageBundleImpl();
        bundle.load();
        String msg = bundle.getMessage(Message.DAOSTARS_START, "23");
        log.info(msg);
    }
}
