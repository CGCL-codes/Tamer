package pl.edu.agh.ssm.persistence.dao;

import java.util.List;
import pl.edu.agh.ssm.persistence.IBillingOperation;

public interface BillingOperationDAO extends GenericDao<IBillingOperation> {

    List<IBillingOperation> getBillingOperations(int serviceID);

    IBillingOperation getBillingOperation(int serviceId, String operationName);
}
