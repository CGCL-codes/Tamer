package org.ocera.orte.types;

public class Status {

    private long strict;

    private long bestEffort;

    private long issues;

    /** 
   * Default constructor.
   */
    public Status() {
        System.out.println(":j: instance of 'org.ocera.orte.types.Status' created..");
    }

    /**
	 * Print actual fields state. Usable for example for check field.
	 */
    public String toString() {
        return ("Status - actual state: " + "strict = " + strict + " | bestEffort = " + bestEffort + " | issues = " + issues);
    }

    public long getStrict() {
        return this.strict;
    }

    public long getBestEffort() {
        return this.bestEffort;
    }

    public long getIssues() {
        return this.issues;
    }
}
