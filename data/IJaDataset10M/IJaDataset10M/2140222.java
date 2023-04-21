package demandpa;

/**
 * @author manu
 *
 */
public class FlowsToTestFieldsHarder {

    /**
   * @param args
   */
    public static void main(String[] args) {
        Object o1 = new FlowsToType();
        A a1 = new A();
        a1.f = o1;
        A a2 = new A();
        a2.f = a1;
        A a3 = (A) a2.f;
        Object o2 = a3.f;
        DemandPATestUtil.makeVarUsed(o2);
    }
}
