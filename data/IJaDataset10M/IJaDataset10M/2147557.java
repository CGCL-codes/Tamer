package com.asoft.common.logs.view.foreveryone;

import org.apache.log4j.Logger;
import com.asoft.common.base.model.BaseObject;
import com.asoft.common.base.web.view.ToNodes;
import com.asoft.common.base.web.view.TreeNode;
import com.asoft.common.logs.model.Model;

/**
 * <p>Title: 实体节点view - 非根节点</p>
 * <p>Description: 类命名： 节点类型名 + "NodeOf" + 树类型名 + "Tree4" + 角色名</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: asoft</p>
 * @ $Author: author $
 * @ $Date: 2005/02/16 01:04:14 $
 * @ $Revision: 1.7 $
 * @ created in 2005-11-14
 *
 */
public class ModelNodeOfOperatingLogTree4EveryOne extends ToNodes {

    static Logger logger = Logger.getLogger(ModelNodeOfOperatingLogTree4EveryOne.class);

    public TreeNode model2Node(BaseObject bo) {
        Model model = (Model) bo;
        return new TreeNode(model.getId(), "Model", model.getName(), model.getModuleId(), "Module", "openfoldericon.png", "closedfoldericon.png", "javascript:onclickNode('" + model.getId() + "','" + "Model" + "')");
    }
}
