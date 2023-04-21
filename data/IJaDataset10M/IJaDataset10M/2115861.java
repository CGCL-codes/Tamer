package org.argetr.resim.ui.parts;

import java.security.InvalidParameterException;
import org.eclipse.jface.viewers.ICellEditorValidator;

public class CellValidatorFactory {

    public static final int INTEGER_VALIDATOR = 1;

    public static final int DOUBLE_VALIDATOR = 2;

    public static final int FILE_PATH_VALIDATOR = 3;

    public static ICellEditorValidator Create(int ValidatorType) {
        switch(ValidatorType) {
            case INTEGER_VALIDATOR:
                return new CellIntegerValidator();
            case DOUBLE_VALIDATOR:
                return new CellDoubleValidator();
            case FILE_PATH_VALIDATOR:
                return new CellStringValidator();
        }
        throw new InvalidParameterException("validator type is not supported");
    }
}
