package no.uio.ifi.kjetilos.javatraits.visitors;

import java.util.ArrayList;
import java.util.List;
import no.uio.ifi.kjetilos.javatraits.parser.ASTClassOrInterfaceType;
import no.uio.ifi.kjetilos.javatraits.parser.ASTImplementsList;
import no.uio.ifi.kjetilos.javatraits.parser.Node;

/**
 * This visitor is used to get the names of the implemented interfaces of any
 * declaration. It is based on the EmptyVisitor and catches visit calls to
 * <code>ASTImplementsList</code> nodes.
 * 
 * @author Kjetil �ster�s
 * 
 */
public class ImplementsVisitor extends EmptyVisitor {

    List<String> implementsId = new ArrayList<String>();

    List<ASTClassOrInterfaceType> implementsAST = new ArrayList<ASTClassOrInterfaceType>();

    /**
	 * Visits the implements lists and gets the list of implemented interfaces.
	 */
    @Override
    public void visit(ASTImplementsList implementsList) {
        for (Node child : implementsList.getChildren()) {
            ASTClassOrInterfaceType type = (ASTClassOrInterfaceType) child;
            implementsId.add(type.getImage());
            implementsAST.add(type);
        }
    }

    /**
	 * Gets the list of implemented interfaces.
	 * 
	 * @return the list of implemented interfaces as an array of strings.
	 */
    public String[] getImplements() {
        return implementsId.toArray(new String[0]);
    }

    /**
	 * Gets the list of implemented interfaces.
	 * 
	 * @return the list of implemented interfaces as an array of type nodes.
	 */
    public ASTClassOrInterfaceType[] getASTImplementsList() {
        return implementsAST.toArray(new ASTClassOrInterfaceType[0]);
    }
}
