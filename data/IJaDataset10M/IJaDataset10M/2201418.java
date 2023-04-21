package com.ibm.realtime.flexotask.development.validation;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.ibm.realtime.flexotask.template.FlexotaskStableMode;

public class CodeAnalysisResult {

    /** The set of all Flexotask classes */
    private Set flexotaskClasses = new HashSet();

    /** The set of all classes used by the Flexotask classes in the template */
    private Set liveClasses = new HashSet();

    /** The set of reference immutable classes */
    private Set refImmClasses = new HashSet();

    /** The set of stable classes */
    private Set stableClasses = new HashSet();

    /** List of methods that are externally visible, i.e., can be invoked by
   * external threads */
    private Set extReachableMethods = new HashSet();

    /** Specifies the handling of stable classes. */
    private FlexotaskStableMode stableMode;

    /**
   * Constructor.
   * @param flexotaskClasses
   * @param liveClasses
   * @param refImmClasses
   * @param stableClasses
   * @param extReachableMethods
   */
    CodeAnalysisResult(FlexotaskStableMode stableMode, Set flexotaskClasses, Set liveClasses, Set refImmClasses, Set stableClasses, Set extReachableMethods) {
        this.stableMode = stableMode;
        this.flexotaskClasses = flexotaskClasses;
        this.liveClasses = liveClasses;
        this.refImmClasses = refImmClasses;
        this.stableClasses = stableClasses;
        this.extReachableMethods = extReachableMethods;
    }

    /**
   * Returns the stable mode used for the analysis.
   * @return the stable mode used for the analysis
   */
    public FlexotaskStableMode getStableMode() {
        return stableMode;
    }

    /**
   * Returns the set of Flexotask classes found during
   * code analysis.
   * @return set of Flexotask classes
   */
    public Set getFlexotaskClasses() {
        return flexotaskClasses;
    }

    /**
   * Returns the set of live classes found during code analysis, i.e., classes
   * used by the Flexotasks in the template.
   * @return set of live classes
   */
    public Set getLiveClasses() {
        return liveClasses;
    }

    /**
   * Returns the set of reference immutable classes found during
   * code analysis.
   * @return set of reference immutable classes
   */
    public Set getReferenceImmutableClasses() {
        return refImmClasses;
    }

    /**
   * Returns the set of classes implementing the <code>Stable
   * </code> interface found during code analysis.
   * @return set of stable classes
   */
    public Set getStableClasses() {
        return stableClasses;
    }

    /**
   * Returns the set of <code>MethodWrapper</code>s representing the methods
   * that were inferred to be externally reachable by the validator.
   * @return the set of <code>MethodWrapper</code>s representing the methods
   * that were inferred to be externally reachable by the validator.
   */
    public Set getExtReachableMethods() {
        return extReachableMethods;
    }

    /**
   * Fetches and displays on the console the classification of the classes
   * inspected by the validator.
   */
    public void printTypeClassification(PrintStream os) {
        os.println("\nThe following type classification was inferred from the processed Flexotask classes:");
        os.println("\n  Valid Stable classes: ");
        printClassList(os, getStableClasses());
        os.println("  Reference-immutable:");
        printClassList(os, getReferenceImmutableClasses());
        os.println("\n\n");
    }

    /**
   * Prints the provided list of class names to the console.
   */
    private void printClassList(PrintStream os, Set list) {
        if (list.size() > 0) {
            for (Iterator i = list.iterator(); i.hasNext(); ) {
                Class c = (Class) i.next();
                os.print("   - " + c.getName());
                os.println();
            }
        } else os.println("    -");
    }
}
