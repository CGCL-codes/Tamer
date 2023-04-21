package alchemy.nec.tree;

/**
 * Type of composite structure.
 * @author Sergey Basalaev
 */
public class StructureType extends NamedType {

    public Var[] fields;

    public StructureType(String name) {
        super(name);
    }
}
