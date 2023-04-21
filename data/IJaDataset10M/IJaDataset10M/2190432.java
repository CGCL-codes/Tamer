package ro.innovative.iml.lang.cf.compiler.node;

import java.io.*;
import ro.innovative.iml.lang.cf.compiler.Node;

public class CfDefaultCase extends Node {

    public void processStart(PrintWriter out) {
        out.print("	<cf:defaultcase>");
    }

    public void processFinish(PrintWriter out) {
        out.println("	</cf:defaultcase>");
    }
}
