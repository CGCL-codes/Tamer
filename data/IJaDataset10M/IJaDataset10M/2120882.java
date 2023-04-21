package net.moonbiter.ebs.protocols.httpparams.tree.interp.types;

import java.util.Map;
import net.moonbiter.OperationFailureException;
import net.moonbiter.ebs.NodeName;
import net.moonbiter.ebs.protocols.httpparams.tree.Tree;
import net.moonbiter.ebs.protocols.httpparams.tree.interp.types.SimpleTreeTypeInterpreter;
import net.moonbiter.ebs.validation.ValidationException;
import net.moonbiter.ebs.validation.ValidationParamException;

public class LongInterpreter extends SimpleTreeTypeInterpreter<Long> {

    public boolean isComposed() {
        return false;
    }

    @Override
    public Long interpret(Tree tree, String strValue, NodeName[] namePortions) throws ValidationException, OperationFailureException {
        try {
            return Long.parseLong(strValue);
        } catch (NumberFormatException ex) {
            throw new ValidationParamException(namePortions, "is not a valid Long");
        }
    }
}
