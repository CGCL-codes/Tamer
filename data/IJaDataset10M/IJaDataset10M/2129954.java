package edu.rabbit.schema;

/**
 * Column's primary key constraint.
 * 
 * @author Yuanyan<yanyan.cao@gmail.com>
 * 
 */
public interface IColumnPrimaryKey extends IColumnConstraint {

    public Boolean isAscending();

    public boolean isAutoincremented();

    public ConflictAction getConflictAction();
}
