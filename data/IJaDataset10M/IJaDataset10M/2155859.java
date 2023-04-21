package test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ test.Test_ConfigurableCode.class, test.Test_ASCIIFormattable.class, test.Test_EBlock.class, test.Test_RawClassFactory.class, test.Test_ASNClassFactory.class, test.Test_NodeFactory.class, test.Test_CsvUtil.class })
public class Test_All_JUnit {

    public static void main(String[] args) {
        System.out.printf("\n CWD = (%s) ", System.getProperty("user.dir"));
        org.junit.runner.JUnitCore.main("test.Test_All_JUnit");
    }
}
