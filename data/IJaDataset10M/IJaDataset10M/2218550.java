package clp.metadata.change;

import javax.swing.JTree;
import clp.core.CLPException;
import clp.ide.CLPTreeNode;
import clp.ide.ObjectTreeMetadata;
import clp.metadata.BusinessObjectMetadata;
import clp.metadata.MetadataManager;
import clp.metadata.PropertyMetadata;

public class NewPropertyChange extends MetadataChange {

    private String entity;

    private PropertyMetadata pm;

    public NewPropertyChange(String entity, PropertyMetadata pm) {
        this.entity = entity;
        this.pm = pm;
    }

    public String getPrimaryName() {
        return entity + "." + pm.getName();
    }

    public void changeMetadata() {
        try {
            BusinessObjectMetadata bom = MetadataManager.getBusinessObject(entity);
            bom.addProperty(pm.getName(), pm.getType());
        } catch (CLPException e) {
            e.printStackTrace();
        }
    }

    public void undo() {
        try {
            BusinessObjectMetadata bom = MetadataManager.getBusinessObject(entity);
            if (bom != null) {
                bom.removeProperty(pm.getName());
            }
        } catch (CLPException e) {
            e.printStackTrace();
        }
    }

    public void draw(JTree tree) {
        CLPTreeNode entityNode = getEntityNode(tree, entity);
        if (entityNode != null) {
            String propType = pm.getType();
            CLPTreeNode propNode = new CLPTreeNode(propType + " " + pm.getName() + (pm.getValue() == null ? "" : "[" + pm.getValue() + "]"));
            propNode.setMetadata(new ObjectTreeMetadata("property", entity + "." + pm.getName()));
            entityNode.add(propNode);
            tree.updateUI();
        }
    }

    public void undraw(JTree tree) {
        CLPTreeNode entityNode = getEntityNode(tree, entity);
        removeNode(entityNode, pm.getName());
        tree.updateUI();
    }
}
