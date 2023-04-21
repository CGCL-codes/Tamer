package org.activebpel.rt.bpel.def.validation.expr.functions;

import org.activebpel.rt.bpel.AeMessages;
import org.activebpel.rt.expr.def.AeScriptFuncDef;
import org.activebpel.rt.expr.validation.AeExpressionValidationResult;
import org.activebpel.rt.expr.validation.IAeExpressionValidationContext;

/**
 * Validates the ActiveBPEL extension function resolveURN(urnVariable) 
 */
public class AeResolveURNFunctionValidator extends AeAbstractActiveBpelExtensionFunctionValidator {

    /**
    * @see org.activebpel.rt.expr.validation.functions.IAeFunctionValidator#validate(org.activebpel.rt.expr.def.AeScriptFuncDef, org.activebpel.rt.expr.validation.AeExpressionValidationResult, org.activebpel.rt.expr.validation.IAeExpressionValidationContext)
    */
    public void validate(AeScriptFuncDef aScriptFunction, AeExpressionValidationResult aResult, IAeExpressionValidationContext aContext) {
        super.validate(aScriptFunction, aResult, aContext);
        int numArgs = aScriptFunction.getArgs().size();
        if (numArgs != 1) {
            addError(aResult, AeMessages.getString("AeAbstractActiveBpelExtensionFunctionValidator.ERROR_INCORRECT_ARGS_NUMBER"), new Object[] { aScriptFunction.getName(), new Integer(1), new Integer(numArgs), aResult.getParseResult().getExpression() });
        } else if (!(aScriptFunction.isStringArgument(0) || aScriptFunction.isExpressionArgument(0))) {
            addError(aResult, AeMessages.getString("AeAbstractActiveBpelExtensionFunctionValidator.ERROR_INCORRECT_ARG_TYPE"), new Object[] { aScriptFunction.getName(), "", "String", aResult.getParseResult().getExpression() });
        }
    }
}
