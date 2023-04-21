package kinky.parsers.java.ast.body;

import kinky.parsers.java.ast.Node;

/**
 * @author Julio Vilmar Gesser
 */
public abstract class BodyDeclaration extends Node {

    public BodyDeclaration(int line, int column) {
        super(line, column);
    }
}
