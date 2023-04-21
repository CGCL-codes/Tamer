package com.inature.oce.core.impl;

import java.util.ArrayList;
import java.util.Iterator;
import com.inature.oce.core.api.ENode;
import com.inature.oce.core.api.Node;
import com.inature.oce.persistence.api.model.CompositeNode;

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
public class EditableNode extends GenericNode implements ENode {

    public static final long serialVersionUID = 7865062963963841208L;

    /**
	 * 
	 * @param compositeNode
	 */
    protected EditableNode(CompositeNode compositeNode) {
        super(compositeNode);
    }

    /**
	 * 
	 *
	 */
    private void updateOrder() {
        int position = 1;
        Iterator<Node> nodeIt = subNodeList.iterator();
        while (nodeIt.hasNext()) {
            GenericNode nextNode = (GenericNode) nodeIt.next();
            nextNode.setOrder(position++);
        }
    }

    /**
	 * 
	 * @param node
	 */
    protected void addSubNode(Node node) {
        if (subNodeList == null) {
            subNodeList = new ArrayList<Node>();
        }
        GenericNode genericNode = (GenericNode) node;
        subNodeList.add(genericNode);
        compositeNode.addNode(genericNode.getCompositeNode());
        genericNode.setParentNode(this);
        updateOrder();
    }

    /**
	 * 
	 * @param node
	 * @param position
	 */
    protected void addSubNode(Node node, int position) {
        GenericNode genericNode = (GenericNode) node;
        if (subNodeList == null) {
            subNodeList = new ArrayList<Node>();
        }
        if (subNodeList.isEmpty()) {
            subNodeList.add(genericNode);
            compositeNode.addNode(genericNode.getCompositeNode());
        } else {
            int index = position - 1;
            if (index < 0) {
                index = 0;
            }
            if (index >= subNodeList.size()) {
                subNodeList.add(genericNode);
                compositeNode.addNode(genericNode.getCompositeNode());
            } else {
                subNodeList.add(index, genericNode);
                compositeNode.addNode(genericNode.getCompositeNode(), index);
            }
        }
        genericNode.setParentNode(this);
        updateOrder();
    }

    /**
	 * 
	 * @param nodeId
	 */
    protected void removeSubNode(String nodeId) {
        GenericNode nodeToRemove = null;
        Iterator<Node> nodeIt = subNodeList.iterator();
        while (nodeIt.hasNext()) {
            Node nextNode = nodeIt.next();
            if (nextNode.getId().equalsIgnoreCase(nodeId)) {
                nodeToRemove = (GenericNode) nextNode;
                break;
            }
        }
        subNodeList.remove(nodeToRemove);
        compositeNode.removeNode(nodeToRemove.getCompositeNode());
        updateOrder();
    }

    /**
	 * 
	 */
    public void setContent(String text) {
        compositeNode.setContent(text);
    }

    /**
	 * 
	 */
    public void setDefaultGroup(String defaultGroup) {
        compositeNode.setDefaultGroup(defaultGroup);
    }

    /**
	 * 
	 */
    public void setOpenForCommenting(boolean openForCommenting) {
        compositeNode.setOpenForCommenting(openForCommenting);
    }

    /**
	 * 
	 */
    public void setOpenForEditing(boolean openForEditing) {
        compositeNode.setOpenForEditing(openForEditing);
    }

    /**
	 * 
	 */
    public void setOpenForVoting(boolean openForVoting) {
        compositeNode.setOpenForVoting(openForVoting);
    }

    /**
	 * 
	 */
    public void setTitle(String title) {
        compositeNode.setTitle(title);
    }

    /**
	 * 
	 */
    public void setNodeType(String nodeType) {
        compositeNode.setType(nodeType);
    }
}
