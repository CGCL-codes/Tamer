package es.nom.morenojuarez.modulipse.core.lang.ast;

import java.util.ArrayList;
import java.util.List;
import antlr.collections.AST;
import es.nom.morenojuarez.modulipse.core.lang.Modula2AST;
import es.nom.morenojuarez.modulipse.core.lang.Modula2TokenTypes;

/**
 * 
 */
public class TypeDeclarationBlock implements Node {

    private List<TypeDeclaration> types = new ArrayList<TypeDeclaration>();

    public static TypeDeclarationBlock create(Modula2AST ast) {
        if (ast != null && ast.getType() == Modula2TokenTypes.TYPE) {
            TypeDeclarationBlock node = new TypeDeclarationBlock();
            AST currentToken = ast.getFirstChild();
            while (currentToken != null) {
                node.getTypes().add(TypeDeclaration.create((Modula2AST) currentToken));
                currentToken = ((Modula2AST) currentToken).findFirstSiblingOfType(Modula2TokenTypes.SEMICOLON);
                if (currentToken != null) {
                    currentToken = currentToken.getNextSibling();
                }
            }
            return node;
        }
        return null;
    }

    public List<TypeDeclaration> getTypes() {
        return types;
    }

    public String toFormattedString() {
        StringBuffer formattedString = new StringBuffer("TYPE ");
        for (TypeDeclaration type : getTypes()) {
            formattedString.append(type.toFormattedString() + "\n");
        }
        return formattedString.toString();
    }
}
