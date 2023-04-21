package ch.bbv.mda;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import ch.bbv.mda.uml.*;

/**
 * The visitor collects all references to classes in the model. The reference
 * list can be used to determine dependencies between classes.
 * @author MarcelBaumann
 * @version $Revision: 1.11 $
 */
public class VisitorCollectClassReferences implements Visitor {

    /**
   * Qualified name of the class which references are collected.
   */
    private String className;

    /**
   * Set of classes referencing the class being inferred.
   */
    private Set<MetaClass> classes;

    /**
   * Default constructor of the class.
   */
    public VisitorCollectClassReferences() {
        classes = new HashSet<MetaClass>();
    }

    /**
   * Returns the list of classes referencing the class defined in the visitor.
   * @return the list of classes referencing the defined class.
   */
    public Collection getReferences() {
        return classes;
    }

    /**
   * Sets the class which references should be looked for.
   * @param clazz class which references will be collected.
   */
    public void setReferencedClass(MetaClass clazz) {
        assert (clazz != null);
        className = clazz.getQualifiedName();
        classes.clear();
    }

    public void visit(MetaModel model) {
        assert (model != null);
    }

    public void visit(MetaPackage metaPackage) {
        assert (metaPackage != null);
    }

    public void visit(MetaClass clazz) {
        assert (clazz != null);
    }

    public void visit(MetaView view) {
        assert (view != null);
    }

    public void visit(MetaUseCase useCase) {
        assert (useCase != null);
    }

    public void visit(MetaProperty property) {
        assert (property != null);
        String typeName = property.getTypeName();
        if (className.equals(typeName)) {
            MetaClass type = (MetaClass) property.getContext();
            classes.add(type);
        }
    }

    public void visit(MetaIndexedProperty property) {
        assert (property != null);
        String typeName = property.getTypeName();
        if (className.equals(typeName)) {
            MetaClass type = (MetaClass) property.getContext();
            classes.add(type);
        }
    }

    public void visit(MetaDatatype datatype) {
        assert (datatype != null);
    }

    public void visit(Stereotype stereotype) {
        assert (stereotype != null);
    }

    public void visit(TaggedValue taggedValue) {
        assert (taggedValue != null);
    }
}
