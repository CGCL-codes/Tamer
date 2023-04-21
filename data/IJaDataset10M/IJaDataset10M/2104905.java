package org.gems.designer.metamodel;

import java.util.Hashtable;
import org.gems.designer.model.AbstractConstraintsChecker;
import org.gems.designer.model.Container;
import org.gems.designer.model.ExecutableConstraint;
import org.gems.designer.model.ModelObject;
import org.gems.designer.model.Root;

public class MetaConstraintsChecker extends AbstractConstraintsChecker {

    private static final long serialVersionUID = 3487495895819393L;

    /**
     *  
     */
    public MetaConstraintsChecker() {
        super();
    }

    public void createConstraints() {
        addConnectionConstraint(Inheritable.class, Plugin.class, 0, 1, 0, Integer.MAX_VALUE, ContainmentConnection.INSTANCE);
        addConnectionConstraint(Atom.class, Model.class, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, ContainmentConnection.INSTANCE);
        addConnectionConstraint(Model.class, Model.class, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, ContainmentConnection.INSTANCE);
        addConnectionConstraint(Inheritance.class, Inheritable.class, 1, 1, 0, 1, InheritanceConnection.INSTANCE);
        addConnectionConstraint(Inheritable.class, Inheritance.class, 0, 1, 0, Integer.MAX_VALUE, InheritanceConnection.INSTANCE);
        addConnectionConstraint(Connection.class, Inheritance.class, 0, 1, 0, Integer.MAX_VALUE, InheritanceConnection.INSTANCE);
        addConnectionConstraint(Inheritance.class, Connection.class, 0, 1, 0, Integer.MAX_VALUE, InheritanceConnection.INSTANCE);
        addConnectionConstraint(Attribute.class, Model.class, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, AttributeContainmentConnection.INSTANCE);
        addConnectionConstraint(Attribute.class, Atom.class, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, AttributeContainmentConnection.INSTANCE);
        addConnectionConstraint(Connection.class, Attribute.class, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, "Association");
        addConnectionConstraint(Attribute.class, Connection.class, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, AttributeOnConnectionType.INSTANCE);
        addConnectionConstraint(Constraint.class, Model.class, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, "Association");
        addConnectionConstraint(Constraint.class, Atom.class, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, "Association", MetaModelProvider.MODEL_ID);
        addConnectionConstraint(Inheritable.class, Aspect.class, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, AspectConnection.INSTANCE);
        addConnectionConstraint(Connection.class, Aspect.class, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, AspectConnection.INSTANCE);
        addConnectionConstraint(Atom.class, Connection.class, 0, Integer.MAX_VALUE, 0, 1, ConnectionSource.INSTANCE);
        addConnectionConstraint(Model.class, Connection.class, 0, Integer.MAX_VALUE, 0, 1, ConnectionSource.INSTANCE);
        addConnectionConstraint(Connection.class, Inheritable.class, 0, 1, 0, Integer.MAX_VALUE, ConnectionTarget.INSTANCE);
        addConnectionConstraint(Atom.class, Connection.class, 0, Integer.MAX_VALUE, 0, 1, BiDirectionalConnection.INSTANCE);
        addConnectionConstraint(Model.class, Connection.class, 0, Integer.MAX_VALUE, 0, 1, BiDirectionalConnection.INSTANCE);
        addConnectionConstraint(Connection.class, Inheritable.class, 0, 1, 0, Integer.MAX_VALUE, BiDirectionalConnection.INSTANCE);
        addContainmentConstraint(Container.class, ModelObject.class, 0, Integer.MAX_VALUE);
        addContainmentConstraint(Root.class, Plugin.class, 1, 1);
        addContainmentConstraint(Model.class, ModelObject.class, 0, 0);
        addConstraint(ModelObject.class, new ExecutableConstraint() {

            private Hashtable<String, ModelObject> names_ = new Hashtable<String, ModelObject>();

            private String msg_ = "All elements must have unique names";

            public String getMessage() {
                return msg_;
            }

            public Class getConstrainedType() {
                return ModelObject.class;
            }

            public boolean valid(Object obj) {
                if (obj instanceof Inheritance) return true;
                ModelObject mo = names_.get(((ModelObject) obj).getName());
                if (mo != null && mo != obj) {
                    msg_ = "Element with ID:" + ((ModelObject) obj).getID() + " cannot have the same name as the element with ID:" + mo.getID();
                    return false;
                } else {
                    names_.put(((ModelObject) obj).getName(), ((ModelObject) obj));
                }
                return true;
            }
        });
    }
}
