package net.sourceforge.greenvine.testmodel.data.daos;

import java.util.List;
import net.sourceforge.greenvine.testmodel.data.entities.Contract;

public interface ContractDao {

    /**
	 * Returns the Contract with the specified identity
	 * or throws an unchecked {@link DaoException}  
	 * if an entity with matching identity not found
	 *	 
	 * @param employeeId the value of the identity
	 * @return Contract with matching identity
	 */
    public abstract Contract loadContract(java.lang.Integer employeeId);

    /**
	 * Returns the Contract with the specified identity
	 * or null if an entity with matching identity not found
     *
	 * @param employeeId the value of the identity
	 * @return Contract with matching identity (or null)
	 */
    public abstract Contract findContract(java.lang.Integer employeeId);

    /**
	 * Retrieve all Contract objects
	 * 
	 * @return List<Contract>of all Contract 
	 * objects in the database. 
	 */
    public abstract List<Contract> findAllContracts();

    /**
	 * Update a supplied Contract loaded in a separate transaction
	 * @param contract the Contract to update
	 */
    public abstract void updateContract(Contract contract);

    /**
	 * Create a supplied Contract object
	 * @param contract the Contract to create
	 */
    public abstract void createContract(Contract contract);

    /**
	 * Remove the Contract with the specified identity
	 * @param employeeId the value of the identity
	 */
    public abstract void removeContract(java.lang.Integer employeeId);
}
