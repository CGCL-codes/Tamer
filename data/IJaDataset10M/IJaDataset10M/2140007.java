package de.binfalse.martin;

import org.rosuda.JRI.Rengine;

public class JRItest {

    public static void main(String[] args) {
        Rengine re = new Rengine(new String[] { "--vanilla" }, false, null);
        if (!re.waitForR()) {
            System.out.println("Cannot load R");
            return;
        }
        System.out.println(re.eval("runif(1)").asDouble());
        re.end();
    }
}
