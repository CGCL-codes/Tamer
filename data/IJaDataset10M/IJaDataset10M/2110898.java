package net.sf.jelly.apt.strategies;

import com.sun.mirror.declaration.ConstructorDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import java.util.Collection;
import java.util.ArrayList;
import net.sf.jelly.apt.TemplateBlock;

/**
 * Evaluates its body for all constructors of the specified type declaration.  If the specified type declaration is an
 * {@link com.sun.mirror.declaration.InterfaceDeclaration}, there will be no constructors.
 *
 * @author Ryan Heaton
 */
public class ConstructorDeclarationLoopStrategy<B extends TemplateBlock> extends ExecutableDeclarationLoopStrategy<ConstructorDeclaration, B> {

    /**
   * All the constructors of the given declaration declaration, or an empty list if the current declaration
   * is an interface declaration.
   *
   * @return All the constructors of the current declaration declaration.
   */
    protected Collection<ConstructorDeclaration> getMemberDeclarations(TypeDeclaration declaration) {
        if (declaration instanceof ClassDeclaration) {
            return ((ClassDeclaration) declaration).getConstructors();
        } else {
            return new ArrayList<ConstructorDeclaration>();
        }
    }
}
