package fr.gedeon.telnetservice.syntaxtree.ast;

public class ParentNodeNotRegisteredException extends RuntimeException {

    private static final long serialVersionUID = -4440129510307881573L;

    public ParentNodeNotRegisteredException(EntityName parentName) {
        super("Parent node " + parentName.toString() + " not registered, all nodes in path must be registered");
    }
}
