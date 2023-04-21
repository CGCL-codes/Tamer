package net.moonbiter.ebs.validation;

import net.moonbiter.ebs.NamingUtil;
import net.moonbiter.ebs.NodeName;

/**
 * Errore di validazione riguardante un singolo parametro.
 * 
 * @author federico
 */
public class ValidationParamException extends ValidationException {

    private static final long serialVersionUID = 6133497821645528630L;

    private String paramName;

    private String desc;

    public ValidationParamException(String paramName, String desc) {
        super("problem regarding param \"" + paramName + "\": " + desc);
        this.paramName = paramName;
        this.desc = desc;
    }

    public ValidationParamException(String[] paramNamePortions, String desc) {
        this(NamingUtil.compose(paramNamePortions), desc);
    }

    public ValidationParamException(NodeName[] paramNamePortions, String desc) {
        this(NamingUtil.compose(paramNamePortions), desc);
    }

    public String getParamName() {
        return paramName;
    }
}
