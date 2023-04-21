package com.sin;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.widen.valet.RecordType;
import com.widen.valet.Route53Driver;
import com.widen.valet.Zone;
import com.widen.valet.ZoneChangeStatus;
import com.widen.valet.ZoneUpdateAction;
import com.widen.valet.util.NameQueryByRoute53APIService;
import com.widen.valet.util.NameQueryService;

public class Route53VsAppengineTest {

    private static String AWS_ACCESS_KEY = null;

    private static String AWS_SECRET_KEY = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        AWS_ACCESS_KEY = "0WBK8DTHYPSPJMNB5VR2";
        AWS_SECRET_KEY = "ZC4raYH2Xc5T4fpA1c5MTgvEpu8lfKS4pyAcZls5";
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public final void testMain() {
        String domain = "peppuseksi.fi.";
        Route53Driver driver = new Route53Driver(AWS_ACCESS_KEY, AWS_SECRET_KEY);
        Zone zone = driver.zoneDetailsForDomain(domain);
        assertFalse(zone.equals(Zone.NON_EXISTENT_ZONE));
        List<ZoneUpdateAction> actions = new ArrayList<ZoneUpdateAction>();
        String resourceWww = String.format("www.%s", domain);
        String resourceChat = String.format("chat.%s", domain);
        String resourcePorno = String.format("porno.%s", domain);
        String resourceIlamista = String.format("ilmaista.%s", domain);
        String[] arrMX = { "1 ASPMX.L.GOOGLE.COM.", "5 ALT1.ASPMX.L.GOOGLE.COM.", "5 ALT2.ASPMX.L.GOOGLE.COM.", "10 ASPMX2.GOOGLEMAIL.COM.", "10 ASPMX3.GOOGLEMAIL.COM." };
        String[] arrCNAME = { "ghs.google.com." };
        ZoneUpdateAction addCNAMEPorno = ZoneUpdateAction.createAction(resourcePorno, RecordType.CNAME, 3600, arrCNAME);
        actions.add(addCNAMEPorno);
        ZoneUpdateAction addCNAMEIlmaista = ZoneUpdateAction.createAction(resourceIlamista, RecordType.CNAME, 3600, arrCNAME);
        actions.add(addCNAMEIlmaista);
        ZoneChangeStatus updateChangeStatus = driver.updateZone(zone, "update", actions);
        driver.waitForSync(updateChangeStatus);
        NameQueryService queryService = new NameQueryByRoute53APIService(driver, zone);
        NameQueryService.LookupRecord lookup = queryService.lookup(resourcePorno, RecordType.CNAME);
        if (lookup.exists) {
            List<String> list = lookup.values;
            for (String value : list) {
                System.out.println("lookup: " + value);
            }
        } else {
            System.out.println("lookup: NOT EXIST ");
        }
    }
}
