package egu.plugin.util;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.IFunctionDeclaration;
import org.eclipse.cdt.internal.core.model.ASTStringUtil;

/**
 * @author pachdjian
 *
 */
@SuppressWarnings("restriction")
public class ASTFunctionCollector extends ASTVisitor implements IASTFunctionCollector {

    private String functionSignature;

    private IASTFunctionDeclarator foundDeclarator = null;

    private IASTFunctionDefinition foundDefinition = null;

    /**
	 * 
	 * @param funcDecl
	 * @throws CModelException
	 */
    public ASTFunctionCollector(IFunctionDeclaration funcDecl) throws CModelException {
        super(true);
        functionSignature = funcDecl.getSignature();
    }

    public int visit(IASTDeclarator declarator) {
        if (declarator instanceof IASTFunctionDeclarator) {
            IASTFunctionDeclarator functDecl = (IASTFunctionDeclarator) declarator;
            String newSignature = getSignature(functDecl);
            if (functionSignature.equals(newSignature)) {
                foundDeclarator = functDecl;
                return PROCESS_SKIP;
            }
        }
        return PROCESS_CONTINUE;
    }

    public int visit(IASTDeclaration declaration) {
        if (declaration instanceof IASTFunctionDefinition) {
            IASTFunctionDefinition functDef = (IASTFunctionDefinition) declaration;
            String newSignature = getSignature(functDef.getDeclarator());
            if (functionSignature.equals(newSignature)) {
                foundDefinition = functDef;
                return PROCESS_SKIP;
            }
        }
        return PROCESS_CONTINUE;
    }

    /**
	 * calculate the function name.
	 * @param functDecl
	 * @return
	 */
    private String getSignature(IASTFunctionDeclarator functDecl) {
        final IASTName name = functDecl.getName();
        StringBuffer functionName = new StringBuffer(name.toString());
        final String[] parameterTypes = ASTStringUtil.getParameterSignatureArray(functDecl);
        if (parameterTypes.length > 0) {
            functionName.append("(");
            int i = 0;
            functionName.append(parameterTypes[i++]);
            while (i < parameterTypes.length) {
                functionName.append(", ");
                functionName.append(parameterTypes[i++]);
            }
            functionName.append(")");
        } else {
            functionName.append("()");
        }
        return functionName.toString();
    }

    public IASTFunctionDeclarator getDeclarator() {
        return foundDeclarator;
    }

    public IASTFunctionDefinition getDefinition() {
        return foundDefinition;
    }

    public void clear() {
        foundDeclarator = null;
        foundDefinition = null;
    }
}
