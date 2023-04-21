package com.wowd.wobly.generation.types.impl;

import static com.wowd.wobly.generation.WoblyCodeGenerator.*;
import static com.wowd.wobly.generation.WoblyGeneratorUtils.*;
import static com.wowd.wobly.generation.types.TypeCodeHandler.*;
import java.lang.reflect.Type;
import com.wowd.wobly.WoblyUtils.Format;
import com.wowd.wobly.annotations.WoblyField;
import com.wowd.wobly.generation.CodeBuilder;
import com.wowd.wobly.generation.types.TypeCodeGenerator;

public class MapTypeCodeGenerator implements TypeCodeGenerator {

    @Override
    public String appendWriteTypeCode(CodeBuilder result, Type type, Class<?> clazz, String obj, Object componentsDetails, Format special, WoblyField of, int depth) {
        String sizeString = null;
        if (isMap(clazz)) {
            Type keyType = extractNextType(type, 0);
            Type valType = extractNextType(type, 1);
            Object keyGen = extractComponentsDetails(componentsDetails, 0);
            Object valueGen = extractComponentsDetails(componentsDetails, 1);
            Format keyFormat = extractFormat(componentsDetails, 0);
            Format valueFormat = extractFormat(componentsDetails, 1);
            int keyPotentialStaticSize = calculateStaticSize(keyType, keyGen, keyFormat);
            int valuePotentialStaticSize = calculateStaticSize(valType, valueGen, valueFormat);
            int potentialStaticSize;
            if (keyPotentialStaticSize > 0 && valuePotentialStaticSize > 0) potentialStaticSize = keyPotentialStaticSize + valuePotentialStaticSize; else potentialStaticSize = -1;
            sizeString = appendWriteSizeOptimizedCode(result, potentialStaticSize, obj, of, false, depth);
            String var = "v" + depth;
            result.println("for (Map.Entry" + toShortGenericPart(type) + " " + var + " : " + obj + ".entrySet()) {");
            result.addTab();
            result.append(generateWriteTypeCode(keyType, var + ".getKey()", keyGen, depth + 1, keyFormat, of).first());
            result.append(generateWriteTypeCode(valType, var + ".getValue()", valueGen, depth + 100, valueFormat, of).first());
            result.removeTab();
            result.println("}");
        } else throw new IllegalArgumentException();
        return sizeString;
    }

    @Override
    public String appendReadTypeCode(CodeBuilder result, Type type, Class<?> clazz, String obj, Object componentsDetails, Format special, WoblyField of, int depth) {
        String sizeVariableName = null;
        if (isMap(clazz)) {
            sizeVariableName = appendReadTypeCodeImpl(result, type, clazz, obj, componentsDetails, of, depth);
        } else throw new IllegalArgumentException();
        return sizeVariableName;
    }

    public static String appendReadTypeCodeImpl(CodeBuilder result, Type type, Class<?> clazz, String obj, Object componentsDetails, WoblyField of, int depth) {
        String sizeVariableName;
        Type keyType = extractNextType(type, 0);
        Type valType = extractNextType(type, 1);
        Object keyGen = extractComponentsDetails(componentsDetails, 0);
        Object valueGen = extractComponentsDetails(componentsDetails, 1);
        Format keyFormat = extractFormat(componentsDetails, 0);
        Format valueFormat = extractFormat(componentsDetails, 1);
        String sizeVar = "size" + depth;
        int keyPotentialStaticSize = calculateStaticSize(keyType, keyGen, keyFormat);
        int valuePotentialStaticSize = calculateStaticSize(valType, valueGen, valueFormat);
        int potentialStaticSize;
        if (keyPotentialStaticSize > 0 && valuePotentialStaticSize > 0) potentialStaticSize = keyPotentialStaticSize + valuePotentialStaticSize; else potentialStaticSize = -1;
        sizeVariableName = appendChangeSizeOptimizedCode(result, sizeVar, potentialStaticSize, obj, of, depth);
        String classType = getIType(clazz);
        String initialSize = "";
        if (classType.contains("Hash")) initialSize = sizeVar + "*2";
        result.println(obj + " = new " + classType + toShortGenericPart(type) + "(" + initialSize + ");");
        String var = "i" + depth;
        String tmpKey = "tmpKey" + depth;
        String tmpVal = "tmpVal" + depth;
        result.println(toShortString(keyType) + " " + tmpKey + ";");
        result.println(toShortString(valType) + " " + tmpVal + ";");
        result.println("for (int " + var + " = 0; " + var + " < " + sizeVar + "; " + var + "++) {");
        result.addTab();
        result.append(generateReadTypeCode(keyType, tmpKey, keyGen, depth + 1, extractFormat(componentsDetails, 0), of).first());
        result.append(generateReadTypeCode(valType, tmpVal, valueGen, depth + 100, extractFormat(componentsDetails, 1), of).first());
        result.println(obj + ".put(" + tmpKey + ", " + tmpVal + ");");
        result.removeTab();
        result.println("}");
        return sizeVariableName;
    }

    @Override
    public void appendTypeSizeCode(CodeBuilder result, Type type, Class<?> clazz, String obj, Object componentsDetails, Format special, WoblyField of, int depth) {
        if (isMap(clazz)) {
            Type keyType = extractNextType(type, 0);
            Type valType = extractNextType(type, 1);
            Object keyGen = extractComponentsDetails(componentsDetails, 0);
            Object valueGen = extractComponentsDetails(componentsDetails, 1);
            Format keyFormat = extractFormat(componentsDetails, 0);
            Format valueFormat = extractFormat(componentsDetails, 1);
            int keyPotentialStaticSize = calculateStaticSize(keyType, keyGen, keyFormat);
            int valuePotentialStaticSize = calculateStaticSize(valType, valueGen, valueFormat);
            if (!(keyPotentialStaticSize > 0 && valuePotentialStaticSize > 0 && depth == 1)) result.println(sizeSizeVarCode(obj, of, false));
            if (keyPotentialStaticSize > 0 && valuePotentialStaticSize > 0) {
                result.println("size += " + multiplyString(obj + ".size()", keyPotentialStaticSize + valuePotentialStaticSize) + ";");
            } else {
                String var = "v" + depth;
                result.println("for (Map.Entry" + toShortGenericPart(type) + " " + var + " : " + obj + ".entrySet()) {");
                result.addTab();
                result.append(generateTypeSizeCode(keyType, var + ".getKey()", keyGen, depth + 1, keyFormat, of));
                result.append(generateTypeSizeCode(valType, var + ".getValue()", valueGen, depth + 100, valueFormat, of));
                result.removeTab();
                result.println("}");
            }
        } else throw new IllegalArgumentException();
    }

    @Override
    public Format defaultTypeFormat(Type type, Object componentsDetails) {
        Type keyType = extractNextType(type, 0);
        Type valType = extractNextType(type, 1);
        Object keyGen = extractComponentsDetails(componentsDetails, 0);
        Object valueGen = extractComponentsDetails(componentsDetails, 1);
        Format keyFormat = extractFormat(componentsDetails, 0);
        Format valueFormat = extractFormat(componentsDetails, 1);
        int keyPotentialStaticSize = calculateStaticSize(keyType, keyGen, keyFormat, false);
        int valuePotentialStaticSize = calculateStaticSize(valType, valueGen, valueFormat, false);
        int potentialStaticSize;
        if (keyPotentialStaticSize > 0 && valuePotentialStaticSize > 0) potentialStaticSize = keyPotentialStaticSize + valuePotentialStaticSize; else potentialStaticSize = -1;
        if (potentialStaticSize > 0) return Format.BYTES_SIZE_COMPRESSED;
        return Format.BYTES;
    }

    @Override
    public boolean generatingFor(Class<?> clazz) {
        return isMap(clazz);
    }
}
