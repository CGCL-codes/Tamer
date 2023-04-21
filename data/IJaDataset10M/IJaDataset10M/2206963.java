package com.codestreet.bugunit;

/** A set of issues.
 * An issue is one of the following
 *
 * <UL>
 * <LI>a bug, identified by its id
 * <LI>a JUnit failure, identified by its location
 * <LI>a deadlock, identified by its location
 * </UL> 
 * 
 * @author apodehl
 *
 */
public interface Issues {

    /** Returns the number of deadlocks.
   * 
   * @return the number of deadlocks
   */
    public int getNumDeadlocks();

    /** Returns the number of open bugs.
   * 
   * @return the number of open bugs
   */
    public int getTotalBugsOpen();

    /** Returns the number of executed bug assertions.
   * 
   * @return the number of executed bug assertion
   */
    public int getTotalBugTests();

    /** Returns the number of failing assertions.
   * 
   * @return the number of failing assertions
   */
    public int getTotalBugFailures();

    /** Returns all deadlock locations.
   * 
   * @return
   */
    public Location[] getDeadlocks();

    /** Returns all tracked bugIDs of this bug map.
   * 
   * @return
   */
    public String[] getBugIDs();

    /** Returns all bugs as array.
   * 
   * @return
   */
    public Bug[] getBugs();

    /** Returns the number of bugs in the current issues.
   * 
   * @return
   */
    public int getNumBugsTracked();

    /** Returns the Bug for the given budID.
   * 
   * @param bugID
   * @return the Bug or null if it was not found
   */
    public BugImpl getBug(String bugID);

    /** Indiciates if this bug is open or not.
   * @param bugID the bug id
   * @return true if this bug is open of false if it was not found or all tests suceeded
   */
    public boolean isBugOpen(String bugID);

    /** Returns all TrackedAssertions for a given bugID.
   * 
   * @param bugID
   * @return
   */
    public TrackedAssertion[] getBugAssertions(String bugID);

    /** Returns the number of tests that test this bug.
   * 
   * @param bugID
   * @return -1 if this bug was not found
   */
    public int getNumBugTests(String bugID);

    /** Returns the number of failures for this bug.
   * 
   * @param bugID
   * @return - 1 if this bug was not found
   */
    public int getNumBugFailures(String bugID);
}
