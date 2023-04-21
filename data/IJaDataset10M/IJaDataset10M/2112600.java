package BSH;

import ij.IJ;
import bsh.Interpreter;
import common.AbstractInterpreter;

public class BSH_Interpreter extends AbstractInterpreter {

    private Interpreter interp;

    public synchronized void run(String arg) {
        super.window.setTitle("BeanShell Interpreter");
        super.run(arg);
        interp = new Interpreter();
        print("Starting BeanShell...");
        println(" Ready -- have fun.\n>>>");
    }

    protected Object eval(final String text) throws Throwable {
        return interp.eval(text);
    }

    protected String getLineCommentMark() {
        return "//";
    }
}
