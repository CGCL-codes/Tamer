package com.wowd.wobly.generation.types.impl;

import static com.wowd.wobly.generation.WoblyCodeGenerator.*;
import static com.wowd.wobly.generation.WoblyGeneratorUtils.*;
import static com.wowd.wobly.generation.types.TypeCodeHandler.*;
import java.lang.reflect.Type;
import com.wowd.wobly.WoblyUtils.Format;
import com.wowd.wobly.annotations.WoblyField;
import com.wowd.wobly.generation.CodeBuilder;
import com.wowd.wobly.generation.types.TypeCodeGenerator;

public class CollectionTypeCodeGenerator implements TypeCodeGenerator {

    @Override
    public String appendWriteTypeCode(CodeBuilder result, Type type, Class<?> clazz, String obj, Object componentsDetails, Format special, WoblyField of, int depth) {
        String sizeString = null;
        if (isCollection(clazz)) {
            Type elType = extractNextType(type, 0);
            Object elGen = extractComponentsDetails(componentsDetails, 0);
            Format elFormat = extractFormat(componentsDetails, 0);
            int potentialStaticSize = calculateStaticSize(elType, elGen, elFormat);
            sizeString = appendWriteSizeOptimizedCode(result, potentialStaticSize, obj, of, false, depth);
            String var = "v" + depth;
            result.println("for (" + toShortString(elType) + " " + var + " : " + obj + ") {");
            result.addTab();
            result.append(generateWriteTypeCode(elType, var, elGen, depth + 1, elFormat, of).first());
            result.removeTab();
            result.println("}");
        } else throw new IllegalArgumentException();
        return sizeString;
    }

    @Override
    public String appendReadTypeCode(CodeBuilder result, Type type, Class<?> clazz, String obj, Object componentsDetails, Format special, WoblyField of, int depth) {
        String sizeVariableName = null;
        if (isCollection(clazz)) {
            sizeVariableName = appendReadTypeCodeImpl(result, type, clazz, obj, componentsDetails, of, depth);
        } else throw new IllegalArgumentException();
        return sizeVariableName;
    }

    public static String appendReadTypeCodeImpl(CodeBuilder result, Type type, Class<?> clazz, String obj, Object componentsDetails, WoblyField of, int depth) {
        String sizeVariableName;
        Type elType = extractNextType(type, 0);
        Object elGen = extractComponentsDetails(componentsDetails, 0);
        Format elFormat = extractFormat(componentsDetails, 0);
        String sizeVar = "size" + depth;
        int potentialStaticSize = calculateStaticSize(elType, elGen, elFormat);
        sizeVariableName = appendChangeSizeOptimizedCode(result, sizeVar, potentialStaticSize, obj, of, depth);
        String classType = getIType(clazz);
        String initialSize = "";
        if (classType.contains("Hash")) initialSize = sizeVar + "*2"; else if (classType.contains("Array")) initialSize = sizeVar;
        result.println(obj + " = new " + classType + toShortGenericPart(type) + "(" + initialSize + ");");
        String var = "i" + depth;
        String tmp = "tmp" + depth;
        result.println("for (int " + var + " = 0; " + var + " < " + sizeVar + "; " + var + "++) {");
        result.addTab();
        result.println(toShortString(elType) + " " + tmp + ";");
        result.append(generateReadTypeCode(elType, tmp, elGen, depth + 1, elFormat, of).first());
        result.println(obj + ".add(" + tmp + ");");
        result.removeTab();
        result.println("}\n");
        return sizeVariableName;
    }

    @Override
    public void appendTypeSizeCode(CodeBuilder result, Type type, Class<?> clazz, String obj, Object componentsDetails, Format special, WoblyField of, int depth) {
        if (isCollection(clazz)) {
            Type elType = extractNextType(type, 0);
            Object elGen = extractComponentsDetails(componentsDetails, 0);
            Format elFormat = extractFormat(componentsDetails, 0);
            int potentialStaticSize = calculateStaticSize(elType, elGen, elFormat);
            if (!(potentialStaticSize > 0 && depth == 1)) result.println(sizeSizeVarCode(obj, of, false));
            if (potentialStaticSize > 0) {
                result.println("size += " + multiplyString(obj + ".size()", potentialStaticSize) + ";");
            } else {
                String var = "v" + depth;
                result.println("for (" + toShortString(elType) + " " + var + " : " + obj + ") {");
                result.addTab();
                result.append(generateTypeSizeCode(elType, var, elGen, depth + 1, elFormat, of));
                result.removeTab();
                result.println("}");
            }
        } else throw new IllegalArgumentException();
    }

    @Override
    public Format defaultTypeFormat(Type type, Object componentsDetails) {
        Type elType = extractNextType(type, 0);
        Object elGen = extractComponentsDetails(componentsDetails, 0);
        Format elFormat = extractFormat(componentsDetails, 0);
        int potentialStaticSize = calculateStaticSize(elType, elGen, elFormat, false);
        if (potentialStaticSize > 0) return Format.BYTES_SIZE_COMPRESSED;
        return Format.BYTES;
    }

    @Override
    public boolean generatingFor(Class<?> clazz) {
        return isCollection(clazz);
    }
}
