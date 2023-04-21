package com.asoft.common.base.web.view;

import java.util.Set;
import com.asoft.common.base.model.BaseObject;

public interface ToNode {

    /** 设置根节点对应的model(BaseObject) or 非根节点对应的models(List) */
    public void setModelOrModels(Object models);

    /** 将节点or 节点s放入nodes容器 */
    public void putNodes(Set allNodes);

    /** model -> node */
    public TreeNode model2Node(BaseObject bo);
}
