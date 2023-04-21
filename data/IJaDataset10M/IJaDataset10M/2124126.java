package md.formator.sql.factory;

import md.formator.IFormator;
import md.formator.factory.IFormatorFactory;

/**
 * A formator factory to all constraints in the SQL translation.
 * @author Bruno da Silva
 * @date october 2008
 *
 */
public abstract class ConstraintFactory implements IFormatorFactory {

    /**
	 * Return the Iformator for the translation of cardinality constraint
	 * @return Iformator allowing translation a cardinality constraint
	 */
    public abstract IFormator getCardinalityConstraintFormator();

    /**
	 * Return the Iformator for the translation of MADS cover constraint
	 * @return Iformator allowing translation a cover constraint
	 */
    public abstract IFormator getCoverConstraintFormator();

    /**
	 * Return the Iformator for the translation of MADS partition constraint
	 * @return Iformator allowing translation a partition constraint
	 */
    public abstract IFormator getPartitionConstraintFormator();

    /**
	 * Return the Iformator for the translation of MADS disjoint constraint
	 * @return Iformator allowing translation a disjoint constraint
	 */
    public abstract IFormator getDisjointConstraintFormator();

    /**
	 * Return the Iformator for the translation of foreign key constraint
	 * @return Iformator allowing translation a foreign key constraint
	 */
    public abstract IFormator getForeignKeyConstraintFormator();

    /**
	 * Return the Iformator for the translation of role redefinition constraint
	 * @return Iformator allowing translation a role redefinition constraint
	 */
    public abstract IFormator getRoleRedefinitionConstraintFormator();
}
