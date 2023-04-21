package annone.engine.local.builder.nodes;

public class EntityNode extends InheritingComponentNode {

    @Override
    public String getSubtype() {
        return "entity";
    }

    @Override
    public String toString() {
        return "entity " + super.toString();
    }
}
