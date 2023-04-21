package symbolic.benchmark;

import piaba.driver.Command;
import piaba.driver.Spawner;
import piaba.driver.VarInfo;
import piaba.symlib.number.SymInt;
import piaba.symlib.number.SymIntConst;
import piaba.symlib.number.SymIntLiteral;
import piaba.util.Constants;
import junit.framework.TestCase;
import benchmark.ratpoly.instrumented.BigRational;
import benchmark.ratpoly.instrumented.RatPoly;

public class TestSymRatPoly extends TestCase {

    public static void symtest(SymInt a, SymInt b, SymInt c) {
        BigRational big1 = new BigRational(a, b);
        BigRational big2 = new BigRational(c);
        RatPoly p = new RatPoly(big1, SymInt.ONE);
        RatPoly q = new RatPoly(big2, SymInt.TWO);
        RatPoly r = p.plus(q);
        RatPoly s = p.times(q);
        RatPoly t = r.times(r);
        RatPoly u = t.minus(q.times(q));
        RatPoly v = t.divides(q);
        RatPoly w = v.times(q);
    }

    public static void test0() {
        Command test = new Command() {

            SymIntLiteral a = SymIntLiteral.create();

            SymIntLiteral b = SymIntLiteral.create();

            SymIntLiteral c = SymIntLiteral.create();

            public void exec() {
                symtest(a, b, c);
            }

            public VarInfo[] getNames() {
                return new VarInfo[] { new VarInfo(VarInfo.INT, a.getId()), new VarInfo(VarInfo.INT, b.getId()), new VarInfo(VarInfo.INT, c.getId()) };
            }
        };
        Constants.NUM_ATTEMPTS = 1000000;
        Constants.INFEASIBILITY_TRESH = 1;
        Constants.START_RANGE = -10;
        Constants.FINAL_RANGE = 10;
        Constants.MAX_DEPTH = 10;
        String DUMP = "logs/ratpoly.txt";
        String PCs = "logs/pcs_ratpoly.txt";
        Spawner.drive(test, DUMP, PCs);
    }

    public static void main(String[] args) {
        test0();
    }
}
