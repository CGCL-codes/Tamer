package com.inature.oce.persistence.cmd;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import com.inature.oce.persistence.api.PersistenceException;
import com.inature.oce.persistence.api.PersistenceService;
import com.inature.oce.persistence.api.model.CompositeNode;
import com.inature.oce.persistence.model.NodeContentDTO;
import com.inature.oce.persistence.model.NodeDTO;
import com.inature.oce.persistence.model.NodeTreeDTO;
import com.inature.oce.persistence.model.NodeTreeParser;
import com.inature.oce.persistence.model.NodeVersionDTO;
import com.inature.oce.persistence.spi.PersistenceFactory;

/**
 * Copyright 2007 i-nature
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Yavor Mitkov Gologanov
 *
 */
public class UpdateNodeCommand {

    private PersistenceService service = null;

    /**
	 * 
	 * @param persistenceService
	 */
    public UpdateNodeCommand(PersistenceService persistenceService) {
        this.service = persistenceService;
    }

    /**
	 * 
	 * @param node
	 * @param userId
	 * @throws PersistenceException
	 */
    private void saveSubNodes(CompositeNode node, String userId) throws PersistenceException {
        PersistenceFactory factory = service.getPersistenceFactory();
        NodeDTO nodeDTO = node.getDto();
        NodeVersionDTO nodeVersionDTO = node.getCurrentVersion();
        boolean treeModified = false;
        List<CompositeNode> subNodeList = node.getSubnodes();
        if ((subNodeList != null) && !subNodeList.isEmpty()) {
            Iterator<CompositeNode> subNodeIt = subNodeList.iterator();
            while (subNodeIt.hasNext()) {
                CompositeNode nextNode = subNodeIt.next();
                nextNode.getDto().setRootId(nodeDTO.getRootId());
                if (nextNode.getDto().getId() == null) {
                    CreateNodeCommand createCommand = new CreateNodeCommand(service);
                    createCommand.execute(nextNode);
                    treeModified = true;
                } else {
                    UpdateNodeCommand updateCommand = new UpdateNodeCommand(service);
                    boolean updated = updateCommand.execute(nextNode, userId);
                    treeModified = treeModified || updated;
                }
            }
        } else {
            if (nodeVersionDTO.getTreeId() != null) {
                nodeVersionDTO.setModified(true);
            }
            nodeVersionDTO.setTreeId(null);
        }
        if (treeModified || node.isTreeModified()) {
            try {
                String treeContent = NodeTreeParser.toXML(subNodeList);
                NodeTreeDTO nodeTreeDTO = new NodeTreeDTO();
                nodeTreeDTO.setTreeContent(treeContent);
                factory.getNodeTreeDAM().insert(nodeTreeDTO);
                nodeVersionDTO.setTreeId(nodeTreeDTO.getId());
            } catch (Exception e) {
                throw new PersistenceException(e);
            }
            nodeVersionDTO.setModified(true);
        }
    }

    /**
	 * 
	 * @param node
	 * @param userId
	 * @return
	 * @throws PersistenceException
	 */
    public boolean execute(CompositeNode node, String userId) throws PersistenceException {
        PersistenceFactory factory = service.getPersistenceFactory();
        if (node.getDto().getBaseId() == null) {
            saveSubNodes(node, userId);
        }
        NodeDTO nodeDTO = node.getDto();
        NodeVersionDTO nodeVersionDTO = node.getCurrentVersion();
        NodeContentDTO contentDTO = node.getNodeContent();
        if ((contentDTO != null) && contentDTO.isModified()) {
            if (contentDTO.getContent() != null) {
                factory.getNodeContentDAM().insert(contentDTO);
                nodeVersionDTO.setContentId(contentDTO.getId());
            } else {
                nodeVersionDTO.setContentId(null);
            }
            nodeVersionDTO.setModified(true);
        }
        if (nodeVersionDTO.isModified()) {
            int lastNumber = factory.getNodeVersionDAM().getMaxVersionNumber(nodeDTO.getId());
            nodeVersionDTO.setNumber(lastNumber + 1);
            nodeVersionDTO.setName(String.valueOf(nodeVersionDTO.getNumber()));
            nodeVersionDTO.setId(null);
            nodeVersionDTO.setAuthorId(userId);
            factory.getNodeVersionDAM().insert(nodeVersionDTO);
            factory.getNodeDAM().updateCurrentVersion(nodeDTO.getId(), nodeVersionDTO.getId());
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            nodeDTO.setUpdateTime(currentTime);
            factory.getNodeDAM().update(nodeDTO);
            return true;
        }
        return false;
    }
}
