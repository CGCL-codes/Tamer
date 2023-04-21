package com.vmladenov.objects.objecttype;

import com.vmladenov.core.enumerations.ValueTypes;

/**
 * User: invincible
 * Date: 2006-12-4
 * Time: 0:45:54
 */
public class RuleTypeFactory {

    public static IType getRuleType(ValueTypes type) throws Exception {
        if (type == ValueTypes.STRING) {
            return new TextType();
        } else if (type == ValueTypes.FLOAT) {
            return new FloatType();
        } else {
            throw new Exception("Внимание!!! Неподържан тип на полето.");
        }
    }
}
