package com.manning.sdmia.ch10.service.impl;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.manning.sdmia.ch10.domain.Contact;
import com.manning.sdmia.ch10.exception.BusinessException;
import com.manning.sdmia.ch10.service.ContactService;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ContactServiceTest {

    @Autowired
    private ContactService contactService;

    @Test
    public void authenticate() throws BusinessException {
        Contact contact = contactService.authenticate("acogoluegnes", "pwd4arno");
        Assert.assertNotNull(contact);
        Assert.assertEquals("acogoluegnes", contact.getLogin());
        try {
            contactService.authenticate("acogoluegnes", "bad");
            Assert.fail("bad password, exception should have been thrown");
        } catch (BusinessException e) {
        }
        try {
            contactService.authenticate("dummy", "");
            Assert.fail("bad login, exception should have been thrown");
        } catch (BusinessException e) {
        }
    }
}
