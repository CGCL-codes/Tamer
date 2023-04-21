package com.angel.io.validators.impl;

import java.lang.reflect.Method;
import com.angel.common.helpers.AnnotationHelper;
import com.angel.common.helpers.ReflectionHelper;
import com.angel.common.helpers.StringHelper;
import com.angel.io.annotations.ColumnRow;
import com.angel.io.annotations.RowChecker;
import com.angel.io.annotations.RowProcessorCommand;
import com.angel.io.exceptions.RowProcessorCommandValidatorException;
import com.angel.io.helpers.Helper;
import com.angel.io.type.rows.ImportRowFile;
import com.angel.io.type.rows.impl.HeaderRowFile;
import com.angel.io.validators.RowProcessorCommandValidator;

/**
 * @author William
 *
 */
public class ColumnsParametersRowCheckerAnnotationValidator implements RowProcessorCommandValidator {

    public void validate(Class<?> importRowCommandClass) throws RowProcessorCommandValidatorException {
        boolean hasAnnotation = AnnotationHelper.hasAnnotation(importRowCommandClass, RowProcessorCommand.class);
        if (hasAnnotation) {
            Method[] methods = ReflectionHelper.getAllMethodWith(RowChecker.class, importRowCommandClass);
            if (methods != null && methods.length > 0) {
                RowProcessorCommand rowProcessorCommand = AnnotationHelper.getAnnotation(importRowCommandClass, RowProcessorCommand.class);
                Method rowCheckerMethod = ReflectionHelper.getAllMethodWith(RowChecker.class, importRowCommandClass)[0];
                RowChecker rowChecker = AnnotationHelper.getAnnotation(rowCheckerMethod, RowChecker.class);
                ColumnRow[] columnRows = rowProcessorCommand.columnsRow();
                Class<?>[] methodTypeParameters = new Class<?>[Helper.calculateQuantityParameters(rowChecker, rowProcessorCommand.columnsRow().length)];
                int currentPosition = 0;
                if (rowChecker.importRow()) {
                    methodTypeParameters[currentPosition] = ImportRowFile.class;
                    currentPosition++;
                }
                if (rowChecker.headerRow()) {
                    methodTypeParameters[currentPosition] = HeaderRowFile.class;
                    currentPosition++;
                }
                if (rowChecker.columnsParameters().length == 0) {
                    for (ColumnRow cr : rowProcessorCommand.columnsRow()) {
                        methodTypeParameters[currentPosition] = cr.type();
                        currentPosition++;
                    }
                } else {
                    for (int pos : rowChecker.columnsParameters()) {
                        methodTypeParameters[currentPosition] = columnRows[pos].type();
                        currentPosition++;
                    }
                }
                boolean matchs = ReflectionHelper.matchParametersTypes(rowCheckerMethod, methodTypeParameters);
                if (!matchs) {
                    throw new RowProcessorCommandValidatorException("Row checker method's parameters [" + rowCheckerMethod.getName() + "] at class [" + importRowCommandClass.getName() + "] doesn't match " + "	with parameters [" + StringHelper.convertToPlainString(methodTypeParameters, ",") + "].");
                }
            }
        }
    }
}
