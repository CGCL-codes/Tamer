package de.fraunhofer.isst.axbench.myviews;

import java.util.ArrayList;
import java.util.List;
import de.fraunhofer.isst.axbench.axlang.api.IAXLangElement;
import de.fraunhofer.isst.axbench.axlang.elements.Model;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.AbstractArchitectureModel;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Component;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.SubComponent;

public class ComponentInstanceViewContentProvider extends MyContentProvider {

    @Override
    public Object[] getChildren(Object parentElement) {
        List<AXLTreeNode> theChildren = new ArrayList<AXLTreeNode>();
        if (parentElement instanceof Model) {
            Model theModel = (Model) parentElement;
            AXLTreeNode modelNode = new AXLTreeNode(null, theModel);
            theChildren.add(modelNode);
            return theChildren.toArray();
        }
        if (parentElement instanceof AXLTreeNode) {
            AXLTreeNode parentNode = (AXLTreeNode) parentElement;
            IAXLangElement parentAxlElement = parentNode.getAxlElement();
            if (parentNode.isAxlElementNode() && parentAxlElement instanceof Model) {
                for (AbstractArchitectureModel architectureModel : ((Model) parentAxlElement).getArchitectureModels()) {
                    AXLTreeNode architectureModelNode = new AXLTreeNode(parentNode, architectureModel);
                    theChildren.add(architectureModelNode);
                }
                return theChildren.toArray();
            }
            if (parentNode.isAxlElementNode()) {
                if (parentAxlElement instanceof AbstractArchitectureModel) {
                    Component topComponent = ((AbstractArchitectureModel) parentAxlElement).getTopComponent();
                    AXLTreeNode topComponentNode = new AXLTreeNode(parentNode, topComponent);
                    theChildren.add(topComponentNode);
                    return theChildren.toArray();
                }
                if (parentAxlElement instanceof Component) {
                    for (SubComponent subcomponent : ((Component) parentAxlElement).getSubComponents()) {
                        AXLTreeNode subcomponentNode = new AXLTreeNode(parentNode, subcomponent);
                        theChildren.add(subcomponentNode);
                    }
                    return theChildren.toArray();
                }
                if (parentAxlElement instanceof SubComponent) {
                    Component componentType = ((SubComponent) parentAxlElement).getComponentType();
                    if (componentType != null) {
                        for (SubComponent subcomponent : componentType.getSubComponents()) {
                            AXLTreeNode subcomponentNode = new AXLTreeNode(parentNode, subcomponent);
                            theChildren.add(subcomponentNode);
                        }
                        return theChildren.toArray();
                    }
                }
            }
            return theChildren.toArray();
        }
        return null;
    }
}
