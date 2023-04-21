package able;

import java.io.InputStream;
import org.starmx.StarMXProperties;
import com.ibm.able.AbleException;
import com.ibm.able.rules.AbleParException;

public class Main {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        System.setProperty(StarMXProperties.CONFIG_PATH, "test/able/");
        framework.Main.main(null);
    }

    private static void test2() {
        try {
            TestAbleRuleSet ars = new TestAbleRuleSet();
            InputStream in = Main.class.getResourceAsStream("test2.arl");
            if (in != null) {
                ars.parseFromARL(in);
                ars.init();
                System.out.println("Input vars: " + ars.getInputVarsArl());
                System.out.println("Vars: " + ars.getVarsArl());
                ars.process(new Object[] { new SimpleBean(50), "reza", 40 });
                Object[] outVar = (Object[]) ars.getOutputBuffer();
                ars.quitAll();
                if (outVar != null) System.out.println("Executed successfully. result=" + outVar[0]);
            } else {
                System.out.println("ERROR: can not find arl file");
            }
        } catch (AbleParException e) {
            e.printStackTrace();
        } catch (AbleException e) {
            e.printStackTrace();
        }
    }
}
