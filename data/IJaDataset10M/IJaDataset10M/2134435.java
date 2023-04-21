package com.inature.oce.core.api;

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
public interface EDocument extends Document {

    /**
	 * 
	 * @param category
	 */
    public void setCategory(String category);

    /**
	 * 
	 * @param title
	 */
    public void setTitle(String title);

    /**
	 * 
	 * @param abstractInfo
	 */
    public void setAbstractInfo(String abstractInfo);

    /**
	 * 
	 * @param keywords
	 */
    public void setKeywords(String keywords);

    /**
	 * 
	 * @param license
	 */
    public void setLicense(String license);

    /**
	 * 
	 * @return
	 */
    public ENode getEditableRootNode();

    /**
	 * 
	 * @param nodeId
	 * @return
	 */
    public ENode getEditableNode(String nodeId);

    /**
	 * 
	 * @param node
	 * @param parentNodeId
	 */
    public void addNode(Node node, String parentNodeId);

    /**
	 * 
	 * @param node
	 * @param parentNodeId
	 * @param position
	 */
    public void addNode(Node node, String parentNodeId, int position);

    /**
	 * 
	 * @param nodeId
	 * @param parentNodeId
	 */
    public void removeNode(String nodeId, String parentNodeId);
}
