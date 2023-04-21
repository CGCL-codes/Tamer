package com.entelience.test.test10raci;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import com.entelience.test.OurDbTestCase;
import com.entelience.util.Config;
import com.entelience.util.FileHelper;
import com.entelience.directory.PeopleFactory;
import com.entelience.directory.Location;
import com.entelience.directory.People;
import com.entelience.objects.directory.LocationId;
import com.entelience.directory.Company;
import com.entelience.objects.directory.LocationInfoLine;
import com.entelience.raci.admin.RaciLocation;
import com.entelience.objects.raci.RACI;
import com.entelience.objects.raci.RaciUnit;
import com.entelience.objects.raci.RaciObjectType;
import com.entelience.objects.raci.RaciRuleInference;
import com.entelience.raci.rules.RaciRulesEngine;
import com.entelience.util.Logs;

public class test05RuleEngineInference extends OurDbTestCase {

    private static RaciRulesEngine rre;

    private static int locId1 = -1;

    private static long salt = 0;

    private static int raciLocationId = -1;

    private static People p1 = null;

    @Test
    public void test00_init() throws Exception {
        Logs.logMethodName();
        try {
            db.begin();
            salt = System.currentTimeMillis();
            Integer cieId = Company.getDefaultCompany();
            assertNotNull("Company ID", cieId);
            p1 = PeopleFactory.createPeople(db, "test" + salt, "T0t0!1234", null, "test", "foobar", "000", "test@example.com", null, cieId, PeopleFactory.anonymousId);
            LocationInfoLine lille = new LocationInfoLine();
            lille.setLocationName("Site 1" + salt);
            lille.setCompanyId(cieId);
            lille.setBuilding("Building 1");
            lille.setSite("Main site");
            lille.setCity("Geneva");
            lille.setZipCode("CH-1204");
            locId1 = new Integer(Location.createLocation(db, lille, PeopleFactory.anonymousId));
            RaciLocation raciLocation = new RaciLocation(db, new LocationId(locId1, 0));
            raciLocationId = raciLocation.getRaciObjectId();
            assertNotNull(raciLocationId);
            raciLocation.addRaci(db, new RACI(RaciUnit.A, p1.getPeopleId(), RaciObjectType.LOCATION), PeopleFactory.anonymousId);
            String file = FileHelper.asString("data/rules/test_inference_rules.xml");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("/tmp/test_inference_rules_" + salt + ".xml")));
            out.println(file.replaceFirst("DYNAMICALLY_REPLACED_BY_TEST", Integer.toString(locId1)));
            out.close();
            Config.setProperty(db, "com.entelience.raci.useInferenceRules", "true", PeopleFactory.anonymousId);
            Config.setProperty(db, "com.entelience.raci.useAuthorizationRules", "false", PeopleFactory.anonymousId);
            Config.setProperty(db, "com.entelience.raci.rulesDirectory", "/tmp", PeopleFactory.anonymousId);
            Config.setProperty(db, "com.entelience.raci.rulesInferenceFile", "test_inference_rules_" + salt + ".xml", PeopleFactory.anonymousId);
            Config.setProperty(db, "com.entelience.raci.ruleServiceProvider", "org.jruleengine", PeopleFactory.anonymousId);
            rre = RaciRulesEngine.getRaciInferRulesEngine(db);
            assertNotNull("get rre", rre);
            assertEquals("singleton", rre, RaciRulesEngine.getRaciInferRulesEngine(db));
        } finally {
            db.commit();
        }
    }

    @Test
    public void test01_is_composite_local() throws Exception {
        Logs.logMethodName();
        RACI raci1 = new RACI();
        raci1.setA(true);
        raci1.setRaciObjectType(RaciObjectType.LOCATION);
        RACI raci2 = new RACI();
        raci2.setR(true);
        raci2.setRaciObjectType(RaciObjectType.STANDARD);
        List<RACI> input = new ArrayList<RACI>();
        input.add(raci1);
        input.add(raci2);
        RaciRuleInference rri = new RaciRuleInference(input, RaciObjectType.UNDEFINED);
        assertTrue(rri.isInputRaciComposite());
    }

    @Test
    public void test02_is_not_composite_local() throws Exception {
        Logs.logMethodName();
        RACI raci1 = new RACI();
        raci1.setA(true);
        raci1.setRaciObjectType(RaciObjectType.LOCATION);
        RACI raci2 = new RACI();
        raci2.setR(true);
        raci2.setRaciObjectType(RaciObjectType.LOCATION);
        List<RACI> input = new ArrayList<RACI>();
        input.add(raci1);
        input.add(raci2);
        RaciRuleInference rri = new RaciRuleInference(input, RaciObjectType.UNDEFINED);
        assertFalse(rri.isInputRaciComposite());
    }

    @Test
    public void test03_simple_addRaci() throws Exception {
        Logs.logMethodName();
        RACI raci1 = new RACI();
        raci1.setA(true);
        raci1.setRaciObjectType(RaciObjectType.LOCATION);
        List<RACI> raci = new ArrayList<RACI>();
        raci.add(raci1);
        RaciRuleInference rri = new RaciRuleInference(raci, RaciObjectType.UNDEFINED);
        List<RaciRuleInference> input = new ArrayList<RaciRuleInference>();
        input.add(rri);
        List<Object> output = rre.executeRules(input);
        assertEquals(3, output.size());
        Object obj = output.get(0);
        assertTrue("RaciRuleInference obj", obj instanceof RaciRuleInference);
        assertEquals("size", 1, ((RaciRuleInference) obj).getInferedRaciSize());
        List<RACI> lraci = ((RaciRuleInference) obj).getInferedRaci();
        assertTrue(lraci.get(0).isA());
    }

    @Test
    public void test04_simple_muteRaci() throws Exception {
        Logs.logMethodName();
        List<RACI> raci = new ArrayList<RACI>();
        raci.add(new RACI(RaciUnit.A, 123, RaciObjectType.RESERVED));
        raci.add(new RACI(RaciUnit.R, 1234, RaciObjectType.RESERVED));
        raci.add(new RACI(RaciUnit.A, 12345, RaciObjectType.RESERVED));
        raci.add(new RACI(RaciUnit.C, 123456, RaciObjectType.RESERVED));
        RaciRuleInference rri = new RaciRuleInference(raci, RaciObjectType.RESERVED);
        List<RaciRuleInference> input = new ArrayList<RaciRuleInference>();
        input.add(rri);
        List<Object> output = rre.executeRules(input);
        assertEquals(3, output.size());
        Object obj = output.get(0);
        assertTrue("RaciRuleInference obj", obj instanceof RaciRuleInference);
        assertEquals("size", 2, ((RaciRuleInference) obj).getInferedRaciSize());
        List<RACI> lraci = ((RaciRuleInference) obj).getInferedRaci();
        assertTrue("R", lraci.get(0).isR());
        assertTrue("R", lraci.get(1).isR());
    }

    @Test
    public void test04_filter_and_mute() throws Exception {
        Logs.logMethodName();
        List<RACI> raci = new ArrayList<RACI>();
        raci.add(new RACI(RaciUnit.A, p1.getPeopleId(), RaciObjectType.LOCATION, raciLocationId));
        raci.add(new RACI(RaciUnit.A, p1.getPeopleId(), RaciObjectType.AUDIT_AUDIT));
        raci.add(new RACI(RaciUnit.R, 123456, RaciObjectType.AUDIT_AUDIT));
        RaciRuleInference rri = new RaciRuleInference(raci, RaciObjectType.AUDIT_REC);
        rre = RaciRulesEngine.getRaciInferRulesEngine(db);
        List<Object> output = rre.executeRules(rri);
        assertEquals(3, output.size());
        Object obj = output.get(0);
        assertTrue("RaciRuleInference obj", obj instanceof RaciRuleInference);
        assertEquals("size", 3, ((RaciRuleInference) obj).getInferedRaciSize());
        List<RACI> lraci = ((RaciRuleInference) obj).getInferedRaci();
        assertTrue("R", lraci.get(0).isR());
        assertTrue("C", lraci.get(1).isC());
        assertTrue("I", lraci.get(2).isI());
    }
}
