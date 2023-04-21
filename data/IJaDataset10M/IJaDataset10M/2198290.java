package javacc.parser.ast.body;

/**
 * @author Julio Vilmar Gesser
 */
public abstract class TypeDeclaration extends BodyDeclaration {

    public final String name;

    public TypeDeclaration(String name) {
        this.name = name;
    }
}
