package nl.dgl.rgb.treemodel;

import java.util.HashMap;
import java.util.Map;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class ClassTreeModel extends OsgsTreeModel {

    private ClassTreeModel(Resource classResource) {
        super(classResource);
    }

    private void createChildrenList(Model jenaModel) {
        Resource classResource = (Resource) nodeModel;
        ResourceTypesTreeModel resourceTypesTreeModel = new ResourceTypesTreeModel(classResource, jenaModel);
        if (resourceTypesTreeModel.getChildCount() > 0) {
            this.osgsChildren.add(resourceTypesTreeModel);
        }
        ResourceSubClassesTreeModel resourceSubClassesTreeModel = new ResourceSubClassesTreeModel(classResource, jenaModel);
        if (resourceSubClassesTreeModel.getChildCount() > 0) {
            this.osgsChildren.add(resourceSubClassesTreeModel);
        }
        ResourceSuperClassesTreeModel resourceSuperClassesTreeModel = new ResourceSuperClassesTreeModel(classResource, jenaModel);
        if (resourceSuperClassesTreeModel.getChildCount() > 0) {
            this.osgsChildren.add(resourceSuperClassesTreeModel);
        }
        ResourcePropertiesTreeModel resourcePropertiesTreeModel = new ResourcePropertiesTreeModel(classResource, jenaModel);
        if (resourcePropertiesTreeModel.getChildCount() > 0) {
            this.osgsChildren.add(resourcePropertiesTreeModel);
        }
        ResourceInstancesTreeModel resourceInstancesTreeModel = new ResourceInstancesTreeModel(classResource, jenaModel);
        if (resourceInstancesTreeModel.getChildCount() > 0) {
            this.osgsChildren.add(resourceInstancesTreeModel);
        }
        ClassExtensionPropertiesTreeModel classExtensionPropertiesTreeModel = new ClassExtensionPropertiesTreeModel(classResource, jenaModel);
        int childCount = classExtensionPropertiesTreeModel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            this.osgsChildren.add((OsgsTreeModel) classExtensionPropertiesTreeModel.getChild(i));
        }
    }

    private static Map<Object, ClassTreeModel> map = new HashMap<Object, ClassTreeModel>();

    public static ClassTreeModel getInstance(Resource classResource, Model jenaModel) {
        ClassTreeModel classResourceTreeModel = map.get(classResource);
        if (classResourceTreeModel == null) {
            classResourceTreeModel = new ClassTreeModel(classResource);
            map.put(classResource, classResourceTreeModel);
            classResourceTreeModel.createChildrenList(jenaModel);
        }
        return classResourceTreeModel;
    }
}
