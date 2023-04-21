package tudresden.ocl20.benchmark;

import junit.framework.Test;
import junit.framework.TestSuite;
import tudresden.ocl20.benchmark.common.InvariantExecuter;

public class B4Test extends TestSuite {

    /**
	 * Instantiates a new b4 test.
	 */
    public B4Test() {
        super("b4");
        this.addTest(new InvariantExecuter("common/DummyWorld.ecore", "bin/tudresden/ocl20/benchmark/testdata/common/ModelInstance.class", "bin/tudresden/ocl20/benchmark/testdata/b4/expressions/invariants.ocl"));
    }

    /**
	 * returns the test suite for single execution
	 */
    public static Test suite() {
        return new B4Test();
    }
}
