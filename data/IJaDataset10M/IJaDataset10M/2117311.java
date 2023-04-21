package testcases;

import org.apache.log4j.Logger;

public class TestImplementation implements TestInterface {

    private static Logger logger = Logger.getLogger(TestImplementation.class);

    public String aMethod(String argument) {
        logger.info("Information message in aMethod");
        return "OK";
    }

    public static void main(String[] args) throws Exception {
        int i = 0;
        while (true) {
            Thread.sleep(1000);
            System.out.print(".");
            if (++i == 50) {
                System.out.println("");
                i = 0;
            }
        }
    }
}
