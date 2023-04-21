package com.icteam.fiji.manager;

import static org.junit.Assert.assertNotNull;
import org.junit.Assert;
import org.junit.Test;
import com.icteam.fiji.model.UtenLite;
import com.icteam.fiji.test.AbstractTestCase;

public class UtenManagerTest extends AbstractTestCase {

    public UtenManager getManager() throws Exception {
        if (manager == null) createManager(UtenManagerBean.class);
        ((UtenManagerBean) manager).ejbCreate();
        return (UtenManager) manager;
    }

    @Test
    public void testPswDuration() throws Exception {
        Long duration = getManager().getPasswordTimeToLive("supporto");
        assertNotNull(duration);
    }

    @Test
    public void testGetutenLite() throws Exception {
        UtenLite ut = getManager().getUtenLite("salernof");
        System.out.println(ut);
        Assert.assertNotNull(ut.getFMustChangePassword());
    }
}
