package org.escapek.server.core.services.impl.internal;

import java.beans.PropertyChangeEvent;
import java.util.Date;
import java.util.Set;
import org.escapek.domain.Node;
import org.escapek.domain.orm.dao.IGenericDao;
import org.escapek.domain.orm.dao.IRegistryNodeDao;
import org.escapek.domain.registry.RegistryNode;
import org.escapek.domain.registry.RegistryNodeData;
import org.escapek.server.core.exceptions.BusinessException;
import org.escapek.server.core.services.IRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author nicolasjouanin
 *
 */
@Transactional
public class RepositoryService implements IRepositoryService {

    private Logger logger = LoggerFactory.getLogger(RepositoryService.class);

    private IGenericDao<Node, String> nodeDao;

    private IRegistryNodeDao registryNodeDao;

    private IGenericDao<RegistryNodeData, String> registryNodeDataDao;

    public RepositoryService() {
    }

    /**
	 * @deprecated
	 */
    public void fillNode(Node aNode) {
        Date now = new Date();
        aNode.setCreationDate(now);
        aNode.setLastModifiedDate(now);
    }

    public Node getNode(String nodeId) {
        return nodeDao.read(nodeId);
    }

    @Transactional(readOnly = true)
    public RegistryNode getRegistryNodeById(String id) {
        return registryNodeDao.read(id);
    }

    @Transactional(readOnly = true)
    public RegistryNode getRegistryNodeByPath(String path) {
        RegistryNode reg;
        cleanPath(path);
        reg = registryNodeDao.getByPath(path);
        if (logger.isDebugEnabled()) {
            if (reg == null) {
                logger.debug("Loading RegistryNode with path='" + path + "' : Not found");
            } else {
                logger.debug("Loading RegistryNode with path='" + path + "' : Success");
            }
        }
        return reg;
    }

    @Transactional(readOnly = true)
    public RegistryNodeData getRegistryNodeDefaultData(String path) {
        return getRegistryNodeData(path, "default");
    }

    @Transactional(readOnly = true)
    public RegistryNodeData getRegistryNodeData(String path, String dataName) {
        RegistryNode node = registryNodeDao.getByPath(path);
        return node.getDatas().get(dataName);
    }

    @Transactional(readOnly = true)
    public RegistryNodeData getRegistryNodeData(String path) {
        return getRegistryNodeData(path, "default");
    }

    public RegistryNode createRegistryNode(RegistryNode aNode) {
        return registryNodeDao.saveOrUpdate(aNode);
    }

    public RegistryNodeData addRegistryNodeData(String registryNodeId, RegistryNodeData nodeData) throws SecurityException {
        RegistryNode node = registryNodeDao.read(registryNodeId);
        if (nodeData.getName() == null || nodeData.getName().equals("")) {
            nodeData.setName("default");
        }
        nodeData.setNode(node);
        return registryNodeDataDao.saveOrUpdate(nodeData);
    }

    @Transactional(readOnly = true)
    public Set<RegistryNode> getChildren(String folderPath) {
        RegistryNode folder = registryNodeDao.getByPath(folderPath);
        return folder.getChildren();
    }

    private void cleanPath(String path) {
        if (path == null) return;
        if (path.endsWith("/")) path = path.substring(0, path.length() - 1);
    }

    public void recordChanges(Node newNode) throws BusinessException {
        for (String propertyName : newNode.getChanges().keySet()) {
            PropertyChangeEvent event = newNode.getChanges().get(propertyName);
            StringBuilder msg = new StringBuilder();
            msg.append("Node instance ").append(newNode.getInstanceName()).append(": property '").append(propertyName).append("' changed from '").append(event.getOldValue()).append("' to '").append(event.getNewValue()).append("'");
            logger.info(msg.toString());
        }
    }

    private boolean equalsValues(Object obj1, Object obj2) {
        if (obj1 == obj2) {
            return true;
        }
        if ((obj1 == null && obj2 != null) || (obj2 == null && obj1 != null) || (obj1.getClass() != obj2.getClass())) {
            return false;
        }
        return obj1.equals(obj2);
    }

    public void setNodeDao(IGenericDao<Node, String> nodeDao) {
        this.nodeDao = nodeDao;
    }

    public void setRegistryNodeDao(IRegistryNodeDao registryNodeDao) {
        this.registryNodeDao = registryNodeDao;
    }

    public void setRegistryNodeDataDao(IGenericDao<RegistryNodeData, String> registryNodeDataDao) {
        this.registryNodeDataDao = registryNodeDataDao;
    }
}
