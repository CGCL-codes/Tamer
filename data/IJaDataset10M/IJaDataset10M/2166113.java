package wyq.test;

import wyq.tool.util.Logger;

public class TestProcess6 {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Logger.log("yes1!!!!!!");
        Logger.log(args);
        for (String arg : args) {
            Logger.log(arg);
        }
    }
}
