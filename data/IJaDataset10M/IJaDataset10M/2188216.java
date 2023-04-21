package org.oz.gridgain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.gridgain.grid.*;

/**
 * <img id="callout_img" src="{@docRoot}/img/callout_blue.gif"><span id="callout_blue">Start Here</span>&nbsp;This
 * example demonstrates a simple use of GridGain grid.
 * <p>
 * String "Hello, World!" is passed as an argument for execution
 * of <tt>HelloWorld</tt> task. As an outcome, two participating
 * nodes will print out a single word from "Hello World" string.
 * One node will print out "Hello, " and the other will printout
 * "World!". If there is only one node participating, then it will
 * print out both words.
 * <p>
 * Grid task {@link GridHelloWorldTask} handles actual splitting
 * into sub-jobs, remote execution, and result aggregation
 * (see {@link GridTask}).
 * <p>
 * <h1 class="header">Starting Remote Nodes</h1>
 * To try this example you should (but don't have to) start remote grid instances.
 * You can start as many as you like by executing the following script:
 * <pre class="snippet">{GRIDGAIN_HOME}/bin/gridgain.{bat|sh}</pre>
 * Once remote instances are started, you can execute this example from
 * Eclipse, Idea, or NetBeans (or any other IDE) by simply hitting run
 * button. You will witness that all nodes discover each other and
 * some of the nodes will participate in task execution (check node
 * output).
 * <p>
 * <h1 class="header">XML Configuration</h1>
 * If no specific configuration is provided, GridGain will start with
 * all defaults. For information about GridGain default configuration
 * refer to {@link GridFactory} documentation. If you would like to
 * try out different configurations you should pass a path to Spring
 * configuration file as 1st command line argument into this example.
 * The path can be relative to <tt>GRIGAIN_HOME</tt> environment variable.
 * You should also pass the same configuration file to all other
 * grid nodes by executing startup script as follows (you will need
 * to change the actual file name):
 * <pre class="snippet">{GRIDGAIN_HOME}/bin/gridgain.{bat|sh} examples/config/specific-config-file.xml</pre>
 * <p>
 * GridGain examples come with multiple configuration files you can try.
 * All configuration files are located under <tt>GRIDGAIN_HOME/examples/config</tt>
 * folder. You are free to try any of these configurations, but whenever
 * using 3rd party configurations, such as JBoss JMS, ActiveMQ JMS, Sun MQ JMS, or GigaSpaces,
 * make sure to download these respective products and include all the necessary
 * libraries into classpath at node startup. All these libraries are already
 * specified in commented format in <tt>GRIDGAIN_HOME/bin/setenv.{bat|sh}</tt> files
 * which get executed automatically by GridGain startup scripts. You can simply
 * uncomment the necessary classpath portions as you need.
 *
 * @author 2005-2008 Copyright (C) GridGain Systems. All Rights Reserved.
 * @version 2.0.0
 */
public final class GridHelloWorldExample {

    /**
     * Ensure singleton.
     */
    private GridHelloWorldExample() {
    }

    /**
     * Execute <tt>HelloWorld</tt> example on the grid.
     *
     * @param args Command line arguments, none required but if provided
     *      first one should point to the Spring XML configuration file. See
     *      <tt>"examples/config/"</tt> for configuration file examples.
     * @throws GridException If example execution failed.
     */
    public static void main(String[] args) throws GridException {
        if (args.length == 0) {
            GridFactory.start();
        } else {
            GridFactory.start(args[0]);
        }
        try {
            Grid grid = GridFactory.getGrid();
            Map<String, String> newHire = new HashMap<String, String>();
            newHire.put("firstName", "Tom");
            newHire.put("middleInit", "A");
            newHire.put("lastName", "McCuch");
            newHire.put("streetAddress", "481 Braceland Dr.");
            newHire.put("city", "Downingtown");
            newHire.put("state", "PA");
            newHire.put("zip", "19335");
            String newEmployee = (String) grid.execute(GridHelloWorldTask.class, newHire.toString().substring(1, newHire.toString().length() - 1)).get();
            String[] words = newEmployee.split(",");
            for (String word : words) {
                String[] values = word.split("=");
                newHire.put(values[0], values[1]);
            }
            System.out.println(">>> New Employee:" + newHire.toString());
            System.out.println(">>> Finished executing Grid \"Provision Employee\" example with custom tasks.");
            System.out.println(">>> You should see print out of 'provisionEmployeeUseridExecutor' on one node, 'provisionEmployeeEmailExecutor' on another node, and 'provisionEmployeeDirectoryExecutor' on another node.");
            System.out.println(">>> Check all nodes for output (this node is also part of the grid).");
            System.out.println(">>>");
        } finally {
            GridFactory.stop(true);
        }
    }
}
