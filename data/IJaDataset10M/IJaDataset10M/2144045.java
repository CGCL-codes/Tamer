package parser.valueObjects;

import java.util.List;

public class SubprogramSpecification {

    public Designator designator;

    public String returnType;

    public List<ArgumentDeclaration> arguments;

    public SubprogramSpecification(Designator designator, List<ArgumentDeclaration> arguments, String returnType) {
        this.designator = designator;
        this.arguments = arguments;
        this.returnType = returnType;
    }
}
