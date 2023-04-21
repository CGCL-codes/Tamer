package com.siemens.ct.exi.datatype.encoder;

import com.siemens.ct.exi.EXIFactory;
import com.siemens.ct.exi.helpers.BuiltInRestrictedCharacterSets;

/**
 * TODO Description
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.3.20081112
 */
public class TypeEncoderLexical extends AbstractTypeEncoderSchemaInformed {

    public TypeEncoderLexical(EXIFactory exiFactory) {
        super(exiFactory);
        binaryBase64DTE = new RestrictedCharacterSetDatatypeEncoder(this, BuiltInRestrictedCharacterSets.newXSDBase64BinaryInstance());
        binaryHexDTE = new RestrictedCharacterSetDatatypeEncoder(this, BuiltInRestrictedCharacterSets.newXSDHexBinaryInstance());
        booleanDTE = new RestrictedCharacterSetDatatypeEncoder(this, BuiltInRestrictedCharacterSets.newXSDBooleanInstance());
        booleanPatternDTE = booleanDTE;
        decimalDTE = new RestrictedCharacterSetDatatypeEncoder(this, BuiltInRestrictedCharacterSets.newXSDDecimalInstance());
        floatDTE = new RestrictedCharacterSetDatatypeEncoder(this, BuiltInRestrictedCharacterSets.newXSDDoubleInstance());
        integerDTE = new RestrictedCharacterSetDatatypeEncoder(this, BuiltInRestrictedCharacterSets.newXSDIntegerInstance());
        unsignedIntegerDTE = integerDTE;
        nBitIntegerDTE = integerDTE;
        datetimeDTE = new RestrictedCharacterSetDatatypeEncoder(this, BuiltInRestrictedCharacterSets.newXSDDateTimeInstance());
        enumerationDTE = new EnumerationDatatypeEncoder(this);
        listDTE = new ListDatatypeEncoder(this, exiFactory);
        stringDTE = new StringDatatypeEncoder(this);
    }
}
