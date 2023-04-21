package com.dynamicobjects.dbo;

import static org.junit.Assert.fail;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class test7 {

    private static final Log log = LogFactory.getLog(test7.class);

    @Before
    public void setUp() throws Exception {
        ApplicationContext context = null;
        try {
            org.apache.log4j.PropertyConfigurator.configure("src/main/resources/log4j.properties");
            context = new FileSystemXmlApplicationContext("src/main/resources/context.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        DynamicServer dboServer = DynamicServer.getInstance();
        dboServer.getDynamicClassPath().addJar("../DynamicBusiness/target/classes");
        dboServer.initialize(context);
    }

    @Test
    public void testSave() {
        DynamicService ds = new DynamicService();
        ds.getSession().beginTransaction();
        try {
            log.debug("Delete partners");
            DboSet dboSetPartners = ds.getDboSet("partners");
            dboSetPartners.removeAll();
            ds.save(dboSetPartners);
            log.debug("Add partner");
            for (int n = 1; n <= 2; n++) {
                Dbo dbopartner = dboSetPartners.addNewDbo(String.valueOf(n));
                dbopartner.setValue("partnerId", n);
                dbopartner.setValue("entryDate", Calendar.getInstance().getTime());
                dbopartner.setValue("personId", n);
                if (n == 1) {
                    dbopartner.setValue("workInput", 132D);
                    dbopartner.setValue("coopId", 1);
                } else {
                    dbopartner.setValue("capitalContribution", 100.2D);
                    dbopartner.setValue("corporationId", 2);
                }
            }
            ds.save(dboSetPartners);
            ds.getSession().commit();
        } catch (Exception e) {
            ds.getSession().rollBack();
            e.printStackTrace();
            fail("Rollback" + e.getMessage());
        }
    }
}
