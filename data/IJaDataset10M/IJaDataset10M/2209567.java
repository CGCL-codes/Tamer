package org.simplx.program.bop;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class TestBopVars {

    private BopVars baseVars;

    @DataProvider
    public Iterator<Object[]> boilerplates() throws IOException {
        List<Object[]> tests = new ArrayList<Object[]>();
        String propFile = "getTempl = Returns %(returnDesc)%.\n" + "setTempl = Sets %(returnDesc)%; %(subjType)% must be positive.\n" + "subjType = the size\n" + "returnDesc = %(subjType)% of %(objType)%\n";
        basicProps(tests, propFile);
        addWithSpaces(tests, "${   }$", propFile.replaceAll("%\\(", "\\${").replaceAll("\\)%", "}\\$"));
        addWithSpaces(tests, "-( )-", propFile.replaceAll("%", "-"));
        addWithSpaces(tests, "|", propFile.replaceAll("%\\(", "|").replaceAll("\\)%", "|"));
        addWithSpaces(tests, "%%%", propFile.replaceAll("%\\(", "%%%").replaceAll("\\)%", "%%%"));
        return tests.iterator();
    }

    private static void addWithSpaces(List<Object[]> tests, String varSpec, String propFile) throws IOException {
        Properties props = basicProps(tests, propFile);
        props.put("varSpec", varSpec);
        props = (Properties) props.clone();
        props.put("varSpec", "   " + varSpec);
        tests.add(new Object[] { props });
        props = (Properties) props.clone();
        props.put("varSpec", varSpec + "   ");
        tests.add(new Object[] { props });
        props = (Properties) props.clone();
        props.put("varSpec", "    " + varSpec + "   ");
        tests.add(new Object[] { props });
    }

    private static Properties basicProps(List<Object[]> tests, String propFile) throws IOException {
        Properties props = new Properties();
        props.load(new StringReader(propFile));
        tests.add(new Object[] { props });
        return props;
    }

    @BeforeTest
    public void initExpand() throws IOException {
        String specFile = "objType = trench\n";
        baseVars = BopVars.build(new StringReader(specFile));
    }

    @Test(dataProvider = "boilerplates")
    public void testExpand(Properties props) throws IOException {
        BopVars bopVars = new BopVars(props, baseVars);
        assertEquals(bopVars.expandVar("getTempl"), "Returns the size of trench.");
        assertEquals(bopVars.expandVar("setTempl"), "Sets the size of trench; the size must be positive.");
        assertEquals(bopVars.expandVar("returnDesc"), "the size of trench");
        assertEquals(bopVars.expandVar("subjType"), "the size");
        assertEquals(bopVars.expandVar("objType"), "trench");
    }

    @DataProvider
    private Object[][] badVars() {
        return new String[][] { { "xy%()%z", "" }, { "xy%(q)%z", "q" }, { "xy%(\\))%", "\\)" }, { "xy%(\\)%)%", "\\" } };
    }

    @Test(dataProvider = "badVars")
    public void testBadVars(String toExpand, String varName) {
        Properties props = new Properties();
        BopVars bopVars = new BopVars(props);
        try {
            String str = bopVars.expandString(toExpand);
            fail("Should have exception: expand(\"" + toExpand + "\") returned \"" + str + "\"");
        } catch (BopVarException e) {
            BopVarException cause = (BopVarException) e.getCause();
            assertEquals(cause.getVarName(), varName);
        }
    }
}
