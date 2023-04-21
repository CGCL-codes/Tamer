package crafty.metamodel;

public class MetaReference extends MetaTypedElement {

    public MetaReference() {
    }

    public MetaReference(String name, MetaClassifier type) {
        super(name, type);
    }

    public MetaReference(String name, TypeUsage type) {
        super(name, type);
    }

    public void accept(MetaModelVisitor v) {
        v.visitReference(this);
    }
}
