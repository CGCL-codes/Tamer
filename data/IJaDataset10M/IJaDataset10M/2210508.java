package com.inature.oce.web.struts.action.document;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.inature.oce.core.api.nom.DocumentTemplate;
import com.inature.oce.core.api.nom.NodeType;
import com.inature.oce.core.service.OCEService;
import com.inature.oce.core.service.ServiceLocator;
import com.inature.oce.web.struts.common.FormValidator2;

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
public class CreateNodeForm extends ActionForm {

    public static final long serialVersionUID = -4001541072876170014L;

    public static final String BEAN_NAME = "createNodeForm";

    public static final String TITLE = "title";

    public static final String ORDER = "order";

    public static final String NODE_TYPE = "nodeType";

    public static final String GROUP_NAME = "groupName";

    private String title = "";

    private String order = "1";

    private String nodeType = "";

    private String groupName = null;

    /**
	 * 
	 */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        FormValidator2 validator = new FormValidator2();
        validator.validateRequiredField(NODE_TYPE, nodeType);
        if (nodeType != null) {
            OCEService oceService = ServiceLocator.getOCEService();
            DocumentTemplate documentTemplate = oceService.getDocumentTemplate();
            NodeType type = documentTemplate.getType(nodeType);
            if (type.isTitleRequired()) {
                validator.validateRequiredField(TITLE, title);
                validator.validateLength(TITLE, title, 1, 4000);
            }
        }
        validator.validateIntField(ORDER, order);
        validator.validateRequiredField(GROUP_NAME, groupName);
        validator.validateLength(ORDER, order, 1, 8);
        return validator.getErrors();
    }

    /**
	 * 
	 * @return String
	 */
    public String getOrder() {
        return order;
    }

    /**
	 * 
	 * @param order
	 */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
	 * 
	 * @return String
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * 
	 * @param title
	 */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * 
	 * @return String
	 */
    public String getNodeType() {
        return nodeType;
    }

    /**
	 * 
	 * @param nodeType
	 */
    public void setFragmentType(String nodeType) {
        this.nodeType = nodeType;
    }

    /**
	 * 
	 * @param nodeType
	 */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    /**
	 * 
	 * @return
	 */
    public String getGroupName() {
        return groupName;
    }

    /**
	 * 
	 * @param groupName
	 */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
