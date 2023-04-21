package com.google.template.soy.examples;

import com.google.common.io.Resources;
import com.google.template.soy.SoyFileSet;
import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.tofu.SoyTofu;

/**
 * Usage of the simple examples.
 *
 */
public class SimpleUsage {

    private SimpleUsage() {
    }

    /** Counter for the number of examples written so far. */
    private static int numExamples = 0;

    /**
   * Prints the generated HTML to stdout.
   * @param args Not used.
   */
    public static void main(String[] args) {
        SoyFileSet sfs = (new SoyFileSet.Builder()).add(Resources.getResource("simple.soy")).build();
        SoyTofu tofu = sfs.compileToTofu();
        writeExampleHeader();
        System.out.println(tofu.newRenderer("soy.examples.simple.helloWorld").render());
        SoyTofu simpleTofu = tofu.forNamespace("soy.examples.simple");
        writeExampleHeader();
        System.out.println(simpleTofu.newRenderer(".helloName").setData(new SoyMapData("name", "Ana")).render());
        writeExampleHeader();
        System.out.println(simpleTofu.newRenderer(".helloNames").setData(new SoyMapData("names", new SoyListData("Bob", "Cid", "Dee"))).render());
    }

    /**
   * Private helper to write the header for each example.
   */
    private static void writeExampleHeader() {
        numExamples++;
        System.out.println("----------------------------------------------------------------");
        System.out.println("[" + numExamples + "]");
    }
}
