package com.margo.eauctiontool;

import java.util.*;
import java.io.*;
import java.net.*;
import com.declarativa.interprolog.*;
import com.xsb.interprolog.*;

/**
 * <code>eAuctionOutput</code> is the Java representation of the output resulting from the computation
 * @author  Maxime MORGE <A HREF="mailto:morge@di.unipi.it">morge@di.unipi.it</A> 
 * @version August 8, 2008 initial version
 * @version August 18, 2008 clean version
 */
public class eAuctionOutput {

    String margo_dir;

    String pl_bin;

    java.util.List<Goal> goals;

    /**
   * Gets the goal in accordance with its index
   * @param index
   * @return goal
   */
    public Goal getGoal(int index) {
        return goals.get(index);
    }

    /**
   * Calculate the output
   * @return true if the computation is OK
   * @param kbase from which the output is calculate
   */
    public boolean calculate(eAuctionKBase kbase) {
        File file;
        System.out.println("eAuctionTool> eAuctionOutput> calculate");
        try {
            file = new File(margo_dir + File.separator + "examples" + File.separator + "eAuctionTool", "trycase.pl");
            FileWriter writer = new FileWriter(file);
            writer.write(kbase.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        PrologEngine engine = new SWISubprocessEngine(pl_bin);
        boolean consult = false;
        System.out.println("eAuctionTool> eAuctionOutput> calculate> " + engine.getPrologVersion());
        consult = engine.consultAbsolute(file);
        if (!consult) {
            System.out.println("eAuctionTool> eAuctionOutput> calculate> consult does not work");
            return false;
        }
        System.out.println("eAuctionTool> eAuctionOutput> calculate> consult works");
        for (ListIterator<Goal> it = goals.listIterator(); it.hasNext(); ) {
            Goal g = it.next();
            System.out.println("eAuctionTool> eAuctionOutput> calculate> " + g.toString());
            if (g.calculate(engine)) {
                System.out.println("eAuctionTool> eAuctionOutput> calculate> " + g.toString() + " done.");
            } else System.out.println("eAuctionTool> eAuctionOutput> calculate> " + g.toString() + " meet problem");
        }
        return true;
    }

    /**
   *  Returns a string representation of the output
   */
    public String toString() {
        final int sbSize = 1000;
        final String variableSeparator = "\n";
        final StringBuffer sb = new StringBuffer(sbSize);
        for (ListIterator<Goal> it = goals.listIterator(); it.hasNext(); ) {
            Goal g = it.next();
            System.out.println("eAuctionTool> eAuctionOutput> toString> " + g.toString());
        }
        return sb.toString();
    }

    /**
   * Creates a new  <code>eAuctionOutput</code> instance.
   * @param pl_bin an <code>String</code> for the path to pl 
   * @param margo_dir a <code>String</code> for the path to the MARGO directory 
   */
    public eAuctionOutput(String pl_bin, String margo_dir) {
        this.pl_bin = new String(pl_bin);
        this.margo_dir = new String(margo_dir);
        goals = new ArrayList<Goal>();
        goals.add(new Goal("suitable"));
        goals.add(new Goal("dp1"));
        goals.add(new Goal("dp2"));
        goals.add(new Goal("compenv"));
        goals.add(new Goal("procspec"));
        goals.add(new Goal("stratval"));
        goals.add(new Goal("nature"));
        System.out.println("eAuctionTool> eAuctionOutput> eAuctionOutput()>" + goals.toString());
    }

    /**
   * <code>main</code> method to test this class.
   * @param args command line
   * 
   */
    public static void main(String[] args) {
        eAuctionOutput output = new eAuctionOutput(args[0], args[1]);
        System.out.println(output.toString());
        eAuctionKBase kbase = new eAuctionKBase();
        kbase.update("specificationaccuracy", "low");
        System.out.println(kbase.toString());
        output.calculate(kbase);
        System.out.println(output.toString());
    }
}

/**
 * <code>Goal</code> is the Java representation of a goal
 * @author  Maxime MORGE <A HREF="mailto:morge@di.unipi.it">morge@di.unipi.it</A> 
 * @version August 8, 2008 initial version
 * @version August 18, 2008 clean version
 */
class Goal {

    /**
   * The name of the goal
   */
    private String name;

    /**
   * The value of the goal 
   */
    private String value;

    /**
   * Gets the name
   */
    public final String getName() {
        return this.name;
    }

    /**
   * Sets the name
   */
    public final void setName(final String argName) {
        this.name = argName;
    }

    /**
   * Gets the value
   * @return the value green|red|amber|black
   */
    public final String getValue() {
        return this.value;
    }

    /**
   * Sets the value of value
   * @param argValue the value green|red|amber|black
   */
    public final void setValue(final String argValue) {
        this.value = argValue;
    }

    /**
   *  Returns a string representation of the Belief.
   */
    public String toString() {
        final int sbSize = 1000;
        final StringBuffer sb = new StringBuffer(sbSize);
        sb.append(name);
        sb.append(": ");
        sb.append(value);
        return sb.toString();
    }

    /**
   * Calculate the goal value
   *
   * @return true if the computation is OK and false otherwise
   * @param engine from which the goal is calculated
   */
    public boolean calculate(PrologEngine engine) {
        System.out.println("eAuctionTool> Goal> calculate> " + name + "?");
        System.out.println("eAuctionTool> Goal> calculate> admissibleArgument(" + name + ", _, _)" + "?");
        if (engine.deterministicGoal("admissibleArgument(" + name + ", _, _)")) {
            value = "green";
            System.out.println("eAuctionTool> Goal> calculate> value= " + value);
            return true;
        }
        System.out.println("eAuctionTool> Goal> calculate> admissibleArgument(sn(" + name + ", _, _)" + "?");
        if (engine.deterministicGoal("admissibleArgument(sn(" + name + "), _, _)")) {
            value = "red";
            System.out.println("eAuctionTool> Goal> calculate> value= " + value);
            return true;
        }
        System.out.println("eAuctionTool> Goal> calculate> admissibleArgument(w" + name + ", _, _)" + "?");
        if (engine.deterministicGoal("admissibleArgument(w" + name + ", _, _)")) {
            value = "amber";
            System.out.println("eAuctionTool> Goal> calculate> value= " + value);
            return true;
        }
        value = "black";
        System.out.println("eAuctionTool> Goal> calculate> value= " + value);
        return false;
    }

    /**
   * Creates a new  <code>Goal</code> instance.
   * @param the <code>name</code> of this predicate
   * @param the <code>value</code> of this predicate 
  */
    public Goal(String name, String value) {
        this.name = new String(name);
        this.value = new String(value);
    }

    /**
   * Creates a new  <code>Goal</code> instance.
   * @param the <code>name</code> of this predicate
   * the default <code>value</code> of this predicate is unknown
   */
    public Goal(String name) {
        this.name = new String(name);
        this.value = new String("unknown");
    }
}
