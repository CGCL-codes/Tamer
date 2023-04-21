package net.sf.jfling.basetypes.numbers.decimal;

import net.sf.jfling.validation.ValidationError;
import net.sf.jfling.basetypes.FlingRootWrapper;
import java.util.List;
import java.math.BigDecimal;

/**
 * Copyright 2008 Tom Rigole (tom.rigole@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 *
 *
 * @author tom.rigole@gmail.com
 *
 */
public class UnsignedFloatingPointWrapper extends FloatingPointWrapper {

    public UnsignedFloatingPointWrapper(int length, String format) {
        super(length, format);
    }

    public boolean isSigned() {
        return false;
    }

    protected int getMantissaLength() {
        return this.getLength() - getExponentLength();
    }

    protected String convertToString() {
        return null;
    }

    public List<ValidationError> validateValue(BigDecimal value) {
        List<ValidationError> errors = super.validateValue(value);
        if (value.compareTo(new BigDecimal("0")) < 0) {
            errors.add(new ValidationError("Value for '" + this.getClass() + "' must not be negative, but found: '" + value.toString() + "'"));
        }
        return errors;
    }

    public FlingRootWrapper getEmptyCopy() {
        return new UnsignedFloatingPointWrapper(getLength(), getFormat());
    }
}
