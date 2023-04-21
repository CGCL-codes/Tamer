package org.zeroexchange.collaboration.contract.flow.step;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.zeroexchange.dao.DAOFactory;
import org.zeroexchange.dao.collaboration.ContractDAO;
import org.zeroexchange.dao.collaboration.ResourcesAssignmentDAO;
import org.zeroexchange.model.collaboration.Contract;
import org.zeroexchange.model.collaboration.ContractStatus;
import org.zeroexchange.model.collaboration.ResourcesAssignment;
import org.zeroexchange.model.resource.Resource;
import org.zeroexchange.model.user.User;

/**
 * READY -> EXECUTED
 * READY -> UNCOMPLETED
 * 
 * @author black
 */
public class ReadyStep extends AbstractStep {

    @Autowired
    private DAOFactory daoFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public ContractStatus getProcessingStatus() {
        return ContractStatus.READY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ContractStatus calculateNextNearestStep(Contract contract) {
        ContractDAO contractDAO = daoFactory.getDAOForEntity(Contract.class);
        Collection<Resource> needs = contractDAO.getContractNeeds(contract.getId());
        ResourcesAssignmentDAO resourcesAssignmentDAO = daoFactory.getDAOForEntity(ResourcesAssignment.class);
        Collection<ResourcesAssignment> confirmedAssignments = resourcesAssignmentDAO.getConfirmedResourceAssignments(contract);
        for (ResourcesAssignment assignment : confirmedAssignments) {
            needs.remove(assignment.getNeed());
        }
        if (!needs.isEmpty()) {
            return ContractStatus.UNCOMPLETED;
        }
        Collection<User> contractUsers = contractDAO.getUsers(contract.getId());
        Collection<User> acceptors = contract.getAcceptors();
        for (User contractUser : contractUsers) {
            if (!acceptors.contains(contractUser)) {
                return contract.getStatus();
            }
        }
        return ContractStatus.EXECUTED;
    }
}
