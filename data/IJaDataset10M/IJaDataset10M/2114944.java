package org.cesar.flip.flipex.ajdt.validators;

import java.util.LinkedList;
import org.cesar.flip.flipex.JavaRefactoringInfo;
import org.cesar.flip.flipex.PreprocessingRefactoringInfo;
import org.cesar.flip.flipex.RefactoringInfo;
import org.cesar.flip.flipex.validators.ValidationProblem;
import org.cesar.flip.flipex.validators.Validator;
import org.cesar.flip.flipex.validators.ValidatorStatus;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ImportDeclaration;

/**
 * Checks whether the selected code is valid for import extraction. Tests if the
 * selected nodes are import declarations.
 * 
 * @author Fernando Calheiros (fernando.calheiros@cesar.org.br)
 * 
 */
public class ImportValidator extends Validator {

    @Override
    public ValidatorStatus validateOrigin(RefactoringInfo refactoringInfo) {
        ValidatorStatus validatorStatus = new ValidatorStatus(this);
        LinkedList<ASTNode> selectedNodes = null;
        if (refactoringInfo instanceof JavaRefactoringInfo) {
            JavaRefactoringInfo info = (JavaRefactoringInfo) refactoringInfo;
            selectedNodes = info.getSelectedNodes();
        } else if (refactoringInfo instanceof PreprocessingRefactoringInfo) {
            PreprocessingRefactoringInfo info = (PreprocessingRefactoringInfo) refactoringInfo;
            selectedNodes = info.getSelectedNodes();
        }
        if (selectedNodes.size() < 1) {
            validatorStatus.addProblem(new ValidationProblem(ImportValidatorMessages.NO_CODE_SELECTED, refactoringInfo.getOrigin()));
        } else {
            for (ASTNode node : selectedNodes) {
                if (!(node instanceof ImportDeclaration)) {
                    validatorStatus.addProblem(new ValidationProblem(ImportValidatorMessages.NOT_ONLY_IMPORTS, refactoringInfo.getOrigin()));
                    break;
                }
            }
        }
        return validatorStatus;
    }

    @Override
    public ValidatorStatus validateDestination(RefactoringInfo refactoringInfo) {
        return new ValidatorStatus(this);
    }
}
