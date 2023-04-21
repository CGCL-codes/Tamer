package rules.test;

import static org.junit.Assert.assertTrue;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import org.aeg.model.Rscpi;
import org.aeg.services.validator.DroolsValidator;
import org.aeg.services.validator.ValidatorConfig;
import org.drools.RuleBase;
import org.drools.agent.RuleAgent;
import org.junit.Before;
import org.junit.Test;

public class PausaPsicofisicaTest extends org.aeg.services.validator.test.RuleTest {

    DroolsValidator dValidator;

    Map<String, Collection<Object>> getTestGlobalMap() {
        Collection<Object> recordsOfTableP61 = new Vector<Object>();
        Collection<Object> recordsOfTableP62 = new Vector<Object>();
        Map<String, Object> record;
        record = new HashMap<String, Object>();
        record.put("caus", "BOA");
        record.put("dscaus", null);
        recordsOfTableP61.add(record);
        record = new HashMap<String, Object>();
        record.put("caus", "BOR");
        record.put("dscaus", null);
        recordsOfTableP61.add(record);
        record = new HashMap<String, Object>();
        record.put("caus", "1");
        record.put("dscaus", "HH CONTINUATIVE");
        recordsOfTableP62.add(record);
        record = new HashMap<String, Object>();
        record.put("caus", "10");
        record.put("dscaus", "MINUTI PAUSA");
        recordsOfTableP62.add(record);
        record = new HashMap<String, Object>();
        record.put("caus", "L6H");
        record.put("dscaus", "CAUSALE LAVORO");
        recordsOfTableP62.add(record);
        record = new HashMap<String, Object>();
        record.put("caus", "P6H");
        record.put("dscaus", "CAUSALE PAUSA");
        recordsOfTableP62.add(record);
        Map<String, Collection<Object>> maps = new HashMap<String, Collection<Object>>();
        maps.put("p61", recordsOfTableP61);
        maps.put("p62", recordsOfTableP62);
        return maps;
    }

    @Before
    public void setUp() throws Exception {
        Properties props = new Properties();
        props.put("url", "http://localhost:8080/drools-jbrms/org.drools.brms.JBRMS/package/AEG.Pausa_Psicoisica/LATEST");
        props.put("name", "TestRscpi");
        props.put("auxTables", "p61 p62");
        RuleAgent agent = RuleAgent.newRuleAgent(props);
        RuleBase ruleBase = agent.getRuleBase();
        System.out.println("RUlebase " + ruleBase);
        ValidatorConfig vConfig = new ValidatorConfig(ruleBase, getTestGlobalMap());
        dValidator = new DroolsValidator();
        dValidator.setValidatorConfig(vConfig);
    }

    @Test
    public void testPP1() {
        Rscpi[] facts = new Rscpi[2];
        facts[0] = new Rscpi();
        facts[1] = new Rscpi();
        facts[0].setCdcausal("BOR");
        facts[0].setMtscompo((short) 800);
        facts[0].setMtfinefa((short) 1000);
        facts[1].setCdcausal("BOA");
        facts[1].setMtscompo((short) 1005);
        facts[1].setMtfinefa((short) 1100);
        Rscpi[] results = dValidator.validate(facts);
        for (int i = 0; i < results.length; i++) {
            System.out.println(results[i].getCdcausal() + " Inizio:" + results[i].getMtscompo() + " Inizio:" + results[i].getMtfinefa() + " durata:" + results[i].getMtinterv());
        }
        assertTrue(results.length == 2);
        Rscpi pausa, lavoro;
        if (results[0].getCdcausal() == "P6H") {
            assertTrue(results[1].getCdcausal() == "L6H");
            pausa = results[0];
            lavoro = results[1];
        } else {
            assertTrue(results[1].getCdcausal() == "P6H");
            assertTrue(results[0].getCdcausal() == "L6H");
            pausa = results[1];
            lavoro = results[0];
        }
        assertTrue(pausa.getMtinterv() == 5);
        assertTrue(lavoro.getMtinterv() == 300);
        assertTrue(lavoro.getMtscompo() == 800);
        assertTrue(lavoro.getMtfinefa() == 1100);
    }

    @Test
    public void testPP2() {
        Rscpi[] facts = new Rscpi[1];
        facts[0] = new Rscpi();
        facts[0].setCdaziend((short) 1);
        facts[0].setCdcausal("BOR");
        facts[0].setCddipend(15);
        facts[0].setMtscompo((short) 800);
        facts[0].setMtfinefa((short) 1000);
        facts[0].setMtinterv((short) 200);
        Rscpi[] results = dValidator.validate(facts);
        for (int i = 0; i < results.length; i++) {
            System.out.println(results[i].getCdcausal() + " Inizio:" + results[i].getMtscompo() + " Inizio:" + results[i].getMtfinefa() + " durata:" + results[i].getMtinterv());
        }
        assertTrue(results.length == 2);
        Rscpi pausa, lavoro;
        if (results[0].getCdcausal() == "P6H") {
            assertTrue(results[1].getCdcausal() == "L6H");
            pausa = results[0];
            lavoro = results[1];
        } else {
            assertTrue(results[1].getCdcausal() == "P6H");
            assertTrue(results[0].getCdcausal() == "L6H");
            pausa = results[1];
            lavoro = results[0];
        }
        assertTrue(pausa.getMtinterv() == 0);
        assertTrue(lavoro.getMtinterv() == 200);
        assertTrue(lavoro.getMtscompo() == 800);
        assertTrue(lavoro.getMtfinefa() == 1000);
    }

    @Test
    public void testPP3() {
        Rscpi[] facts = new Rscpi[2];
        facts[0] = new Rscpi();
        facts[1] = new Rscpi();
        facts[0].setCdcausal("BOR");
        facts[0].setMtscompo((short) 800);
        facts[0].setMtfinefa((short) 1005);
        facts[1].setCdcausal("BOR");
        facts[1].setMtscompo((short) 1000);
        facts[1].setMtfinefa((short) 1100);
        Rscpi[] results = dValidator.validate(facts);
        for (int i = 0; i < results.length; i++) {
            System.out.println(results[i].getCdcausal() + " Inizio:" + results[i].getMtscompo() + " Inizio:" + results[i].getMtfinefa() + " durata:" + results[i].getMtinterv());
        }
        assertTrue(results.length == 2);
        Rscpi pausa, lavoro;
        if (results[0].getCdcausal() == "P6H") {
            assertTrue(results[1].getCdcausal() == "L6H");
            pausa = results[0];
            lavoro = results[1];
        } else {
            assertTrue(results[1].getCdcausal() == "P6H");
            assertTrue(results[0].getCdcausal() == "L6H");
            pausa = results[1];
            lavoro = results[0];
        }
        assertTrue(pausa.getMtinterv() == 0);
        assertTrue(lavoro.getMtscompo() == 800);
        assertTrue(lavoro.getMtfinefa() == 1100);
        assertTrue(lavoro.getMtinterv() == 300);
    }

    @Test
    public void testPP4() {
        Rscpi[] facts = new Rscpi[2];
        facts[0] = new Rscpi();
        facts[1] = new Rscpi();
        facts[0].setCdcausal("BOR");
        facts[0].setMtscompo((short) 800);
        facts[0].setMtfinefa((short) 1100);
        facts[1].setCdcausal("BOR");
        facts[1].setMtscompo((short) 900);
        facts[1].setMtfinefa((short) 1000);
        Rscpi[] results = dValidator.validate(facts);
        for (int i = 0; i < results.length; i++) {
            System.out.println(results[i].getCdcausal() + " Inizio:" + results[i].getMtscompo() + " Inizio:" + results[i].getMtfinefa() + " durata:" + results[i].getMtinterv());
        }
        assertTrue(results.length == 2);
        Rscpi pausa, lavoro;
        if (results[0].getCdcausal() == "P6H") {
            assertTrue(results[1].getCdcausal() == "L6H");
            pausa = results[0];
            lavoro = results[1];
        } else {
            assertTrue(results[1].getCdcausal() == "P6H");
            assertTrue(results[0].getCdcausal() == "L6H");
            pausa = results[1];
            lavoro = results[0];
        }
        assertTrue(lavoro.getMtscompo() == 800);
        assertTrue(lavoro.getMtfinefa() == 1100);
        assertTrue(pausa.getMtinterv() == 0);
    }

    @Test
    public void testPP5() {
        Rscpi[] facts = new Rscpi[3];
        facts[0] = new Rscpi();
        facts[1] = new Rscpi();
        facts[2] = new Rscpi();
        facts[0].setCdcausal("BOR");
        facts[0].setMtscompo((short) 800);
        facts[0].setMtfinefa((short) 1100);
        facts[1].setCdcausal("BOR");
        facts[1].setMtscompo((short) 1105);
        facts[1].setMtfinefa((short) 1300);
        facts[2].setCdcausal("BOR");
        facts[2].setMtscompo((short) 1301);
        facts[2].setMtfinefa((short) 1600);
        Rscpi[] results = dValidator.validate(facts);
        for (int i = 0; i < results.length; i++) {
            System.out.println(results[i].getCdcausal() + " Inizio:" + results[i].getMtscompo() + " Inizio:" + results[i].getMtfinefa() + " durata:" + results[i].getMtinterv());
        }
        assertTrue(results.length == 2);
        Rscpi pausa, lavoro;
        if (results[0].getCdcausal() == "P6H") {
            assertTrue(results[1].getCdcausal() == "L6H");
            pausa = results[0];
            lavoro = results[1];
        } else {
            assertTrue(results[1].getCdcausal() == "P6H");
            assertTrue(results[0].getCdcausal() == "L6H");
            pausa = results[1];
            lavoro = results[0];
        }
        assertTrue(pausa.getMtinterv() == 6);
        assertTrue(lavoro.getMtscompo() == 800);
        assertTrue(lavoro.getMtfinefa() == 1600);
    }

    @Test
    public void testPP6() {
        Rscpi[] facts = new Rscpi[2];
        facts[0] = new Rscpi();
        facts[1] = new Rscpi();
        facts[0].setCdcausal("BOR");
        facts[0].setMtscompo((short) 800);
        facts[0].setMtfinefa((short) 1200);
        facts[1].setCdcausal("BOR");
        facts[1].setMtscompo((short) 1400);
        facts[1].setMtfinefa((short) 1900);
        Rscpi[] results = dValidator.validate(facts);
        for (int i = 0; i < results.length; i++) {
            System.out.println(results[i].getCdcausal() + " Inizio:" + results[i].getMtscompo() + " Inizio:" + results[i].getMtfinefa() + " durata:" + results[i].getMtinterv());
        }
        assertTrue(results.length == 2);
        Rscpi pausa, lavoro;
        if (results[0].getCdcausal() == "P6H") {
            assertTrue(results[1].getCdcausal() == "L6H");
            pausa = results[0];
            lavoro = results[1];
        } else {
            assertTrue(results[1].getCdcausal() == "P6H");
            assertTrue(results[0].getCdcausal() == "L6H");
            pausa = results[1];
            lavoro = results[0];
        }
        assertTrue(pausa.getMtinterv() == 0);
        assertTrue(lavoro.getMtscompo() == 1400);
        assertTrue(lavoro.getMtfinefa() == 1900);
    }

    @Test
    public void testPP7() {
        Rscpi[] facts = new Rscpi[8];
        facts[0] = new Rscpi();
        facts[1] = new Rscpi();
        facts[2] = new Rscpi();
        facts[3] = new Rscpi();
        facts[4] = new Rscpi();
        facts[5] = new Rscpi();
        facts[6] = new Rscpi();
        facts[7] = new Rscpi();
        facts[0].setCdcausal("BOR");
        facts[0].setMtscompo((short) 800);
        facts[0].setMtfinefa((short) 900);
        facts[1].setCdcausal("BOR");
        facts[1].setMtscompo((short) 900);
        facts[1].setMtfinefa((short) 1200);
        facts[2].setCdcausal("BOR");
        facts[2].setMtscompo((short) 830);
        facts[2].setMtfinefa((short) 1205);
        facts[3].setCdcausal("BOR");
        facts[3].setMtscompo((short) 1206);
        facts[3].setMtfinefa((short) 1240);
        facts[4].setCdcausal("BOR");
        facts[4].setMtscompo((short) 1241);
        facts[4].setMtfinefa((short) 1500);
        facts[5].setCdcausal("BOR");
        facts[5].setMtscompo((short) 1500);
        facts[5].setMtfinefa((short) 1800);
        facts[6].setCdcausal("BOR");
        facts[6].setMtscompo((short) 1600);
        facts[6].setMtfinefa((short) 1630);
        facts[7].setCdcausal("BOR");
        facts[7].setMtscompo((short) 1900);
        facts[7].setMtfinefa((short) 2030);
        Rscpi[] results = dValidator.validate(facts);
        for (int i = 0; i < results.length; i++) {
            System.out.println(results[i].getCdcausal() + " Inizio:" + results[i].getMtscompo() + " Inizio:" + results[i].getMtfinefa() + " durata:" + results[i].getMtinterv());
        }
        assertTrue(results.length == 2);
        Rscpi pausa, lavoro;
        if (results[0].getCdcausal() == "P6H") {
            assertTrue(results[1].getCdcausal() == "L6H");
            pausa = results[0];
            lavoro = results[1];
        } else {
            assertTrue(results[1].getCdcausal() == "P6H");
            assertTrue(results[0].getCdcausal() == "L6H");
            pausa = results[1];
            lavoro = results[0];
        }
        assertTrue(pausa.getMtinterv() == 2);
        assertTrue(lavoro.getMtscompo() == 800);
        assertTrue(lavoro.getMtfinefa() == 1800);
    }
}
