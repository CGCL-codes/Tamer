package org.photovault.replication;

/**
  Base class for classes describing field conflicts that occur during merging
 changes from different branches
 
 @author Harri Kaimio
 @since 0.6.0
 
 */
public abstract class FieldConflictBase {

    /**
     The merged change that contains this conflict
     */
    protected FieldChange change;

    /**
     Constructor
     @param ch The merged field change that contains this conflict
     */
    protected FieldConflictBase(FieldChange ch) {
        change = ch;
    }

    /**
     Returns the name of the conflicting field
     */
    public String getFieldName() {
        return change.name;
    }

    /**
     Resolve the conflict by setting state of the merged change to match that 
     from given branch. 
     @param winningBranch Number of the branch. Derived chasses should follow 
     convention that they priovide a method for retrieving a list of state change
     descriptions in all branches, adn this parameter should then be the order 
     number of the winning branch.
     */
    public abstract void resolve(int winningBranch);
}
