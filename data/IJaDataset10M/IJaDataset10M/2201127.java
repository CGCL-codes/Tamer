package wyq.test;

import java.util.Arrays;
import wyq.tool.util.AbstractProcessor;
import wyq.tool.util.Processor;
import wyq.tool.util.Processor.InjectProperty;
import wyq.tool.util.ProcessorRunner;

@InjectProperty("test4.properties")
public class TestProcessor4 extends AbstractProcessor implements Processor {

    private String a1;

    private String a2;

    private String[] testArr;

    private int[] testArr2;

    @Override
    public void process(String[] args) throws Exception {
        println("Test processor....");
        println("args:" + Arrays.toString(args));
        println("a1:" + a1);
        println("a2:" + a2);
        println("testArr:" + testArr);
        println("testArr2:" + testArr2);
        Thread.sleep(1000);
    }

    private void println(Object o) {
        System.out.println(o);
    }

    public static void main(String[] args) {
        ProcessorRunner.run(TestProcessor4.class, args);
    }
}
