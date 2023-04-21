package md.formator.sql;

import java.util.LinkedList;
import md.formator.sql.factory.ConstraintFactory;

/**
 * This class represents the MADS contraints. They could be
 * Cover, Disjoint or Partition
 * @author Bruno da Silva
 * @date october 2008
 *
 */
public abstract class MADSConstraint extends TriggerConstraint {

    /** The of tables who must be checked by the trigger */
    private LinkedList<AbstractTable> checkedTables = new LinkedList<AbstractTable>();

    /** The parent table. In case of role constraint, that's the table of the entity
	 * linked by the roles */
    private AbstractTable parent;

    /**
	 * Initialize a MADS constraint with the given parameters.
	 * @param cf The factory to format the constraints
	 * @param updatedTable The table where the trigger is applied
	 * @param checkedTables List of the who must be checked by the trigger
	 * @param parent The parent table in case of inheritance and the table entity
	 * 					in case of roles
	 * @param label The name constraint
	 * @param onInsert If the trigger must be activated before or after an insertion
	 * @param onUpdate If the trigger must be activated before or after an update
	 * @param onDelete If the trigger must be activated before or after a removal
	 */
    protected MADSConstraint(ConstraintFactory cf, AbstractTable updatedTable, LinkedList<AbstractTable> checkedTables, AbstractTable parent, String label, boolean onInsert, boolean onUpdate, boolean onDelete) {
        super(cf, updatedTable, label, onInsert, onUpdate, onDelete);
        this.checkedTables = checkedTables;
        this.parent = parent;
    }

    @SuppressWarnings("unchecked")
    public LinkedList<AbstractTable> checkedTables() {
        return (LinkedList<AbstractTable>) checkedTables.clone();
    }

    /**
	 * Get the parent table where in case of inheritance constraint corresponds
	 * to the parent, but in case of roles constraint corresponds to the entity
	 * linked by the roles.
	 * @return The parent table
	 */
    public AbstractTable parent() {
        return parent;
    }
}
