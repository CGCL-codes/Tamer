package org.richfaces.tarkus.fmk.business;

import junit.framework.Assert;
import org.junit.Test;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringBeanByName;

public class MessageBeanTest extends AbstractFmkTest {

    @SpringBeanByName
    private IMessageService messageService;

    @Test
    @DataSet
    public void testList() throws Exception {
        Assert.assertTrue(true);
    }
}
