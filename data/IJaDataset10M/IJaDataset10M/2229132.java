package worlds;

import junit.framework.TestCase;
import logic.Alg;
import contin.Command;
import java.util.Map;
import intf.World;

/**
 * Created by IntelliJ IDEA.
 * User: adenysenko
 * Date: May 28, 2009
 * Time: 6:44:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestWorlds extends TestCase {

    public void test1() {
        W1 w = new W1();
        Alg alg = new Alg(w);
        for (int i = 0; i < 200; i++) {
            String cmd = alg.nextCmd(null);
            w.command(cmd);
            double targ = targetSum(w);
            if (i > 100 && targ < 1) {
                fail("step " + i + " view=" + w.view());
            }
            alg.cmdCompleted();
        }
    }

    public void test2() {
        W2 w = new W2();
        Alg alg = new Alg(w);
        for (int i = 0; i < 200; i++) {
            String cmd = alg.nextCmd(null);
            if (i > 100 && w.commandSuboptimal(cmd)) {
                fail("step " + i + " view=" + w.view() + " cmd=" + cmd);
            }
            w.command(cmd);
            alg.cmdCompleted();
        }
    }

    double targetSum(World w) {
        double sum = 0;
        Map<String, Object> v = w.view();
        for (String s : w.targetSensors()) {
            sum += ((Number) v.get(s)).doubleValue();
        }
        return sum;
    }
}
