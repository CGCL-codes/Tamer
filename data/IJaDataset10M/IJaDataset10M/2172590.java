package edu.mit.csail.pag.amock.representation;

import java.util.*;
import edu.mit.csail.pag.amock.util.*;

public class ExplicitlyDeclaredPrimary extends AbstractPrimary {

    private final List<ProgramObject> constructorArguments;

    public ExplicitlyDeclaredPrimary(ClassName fullClassName, ProgramObject[] constructorArguments) {
        super(fullClassName);
        this.constructorArguments = Arrays.asList(constructorArguments);
    }

    protected List<ProgramObject> getConstructorArguments() {
        return constructorArguments;
    }

    public boolean needsDeclaration() {
        return true;
    }

    public void doesNotNeedDeclaration() {
    }
}
