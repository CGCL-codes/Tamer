package org.fernwood.jbasic.opcodes;

import org.fernwood.jbasic.Status;
import org.fernwood.jbasic.runtime.JBFBinary;
import org.fernwood.jbasic.runtime.JBasicException;
import org.fernwood.jbasic.runtime.JBasicFile;
import org.fernwood.jbasic.value.Value;

/**
 * @author cole
 * 
 */
public class OpPUT extends AbstractOpcode {

    /**
	 * Write a record to a BINARY file. Top of stack is record definition array,
	 * second on stack is file identifier.
	 * 
	 * @see org.fernwood.jbasic.opcodes.AbstractOpcode#execute(org.fernwood.jbasic.opcodes.InstructionContext)
	 */
    public void execute(final InstructionContext env) throws JBasicException {
        JBFBinary randomFile = null;
        int n;
        boolean fromRecord = false;
        if (env.instruction.integerValid) fromRecord = (env.instruction.integerOperand == 1);
        Value source = null;
        if (fromRecord) source = env.pop();
        Value fieldList = null;
        if (env.instruction.integerOperand != 4) fieldList = env.pop();
        final Value fileID = env.pop();
        randomFile = (JBFBinary) JBasicFile.lookup(env.session, fileID);
        if (randomFile == null) throw new JBasicException(Status.FNOPENOUTPUT, fileID.toString());
        if (randomFile.getMode() != JBasicFile.MODE_BINARY) throw new JBasicException(Status.NOTBINARY);
        if (fieldList == null & env.instruction.integerOperand == 4) fieldList = fileID.getElement("FIELD");
        if (fieldList == null) throw new JBasicException(Status.INVRECDEF, new Status(Status.EXPREC));
        if (fieldList.getType() != Value.ARRAY) throw new JBasicException(Status.INVRECDEF, fieldList.toString());
        final int fCount = fieldList.size();
        if (fCount < 1) throw new JBasicException(Status.INVRECDEF, fieldList.toString());
        Value fieldRecord = null;
        for (n = 1; n <= fCount; n++) {
            fieldRecord = fieldList.getElement(n);
            if (fieldRecord.getType() != Value.RECORD) throw new JBasicException(Status.INVRECDEF, fieldList.toString());
        }
        for (n = 1; n <= fCount; n++) {
            fieldRecord = fieldList.getElement(n);
            final Value fieldName = fieldRecord.getElement("NAME");
            if (fieldName == null) throw new JBasicException(Status.INVRECDEF, new Status(Status.EXPMEMBER, "NAME"));
            final String nameString = fieldName.getString().toUpperCase();
            Value datum = null;
            if (fromRecord && source != null) datum = source.getElement(nameString); else datum = env.localSymbols.reference(nameString);
            final Value fieldType = fieldRecord.getElement("TYPE");
            if (fieldType == null) throw new JBasicException(Status.INVRECDEF, new Status(Status.EXPMEMBER, "TYPE"));
            final String typeString = fieldType.getString().toUpperCase();
            int size = 0;
            int kind = Value.UNDEFINED;
            if (datum == null) throw new JBasicException(Status.UNKVAR, nameString);
            if (typeString.equals("UNICODE")) {
                final String stringValue = datum.getString();
                final Value fieldSize = fieldRecord.getElement("SIZE");
                if (fieldSize == null) {
                    if (randomFile.defaultStringSize == null) {
                        randomFile.putInteger(stringValue.length(), 4);
                        size = stringValue.length();
                    } else size = randomFile.defaultStringSize.getInteger();
                } else size = fieldSize.getInteger();
                randomFile.putUnicode(stringValue, size);
                kind = Value.STRING;
            } else if (typeString.equals("VARYING")) {
                Value fieldSize = fieldRecord.getElement("SIZE");
                if (fieldSize == null) fieldSize = new Value(256);
                size = fieldSize.getInteger();
                String writeString = datum.getString();
                if (writeString.length() > size) writeString = writeString.substring(0, size);
                randomFile.putInteger(writeString.length(), 4);
                randomFile.putString(writeString, size);
                kind = Value.STRING;
            } else if (typeString.equals("STRING")) {
                final String stringValue = datum.getString();
                final Value fieldSize = fieldRecord.getElement("SIZE");
                if (fieldSize == null) {
                    if (randomFile.defaultStringSize == null) {
                        randomFile.putInteger(stringValue.length(), 4);
                        size = stringValue.length();
                    } else size = randomFile.defaultStringSize.getInteger();
                } else size = fieldSize.getInteger();
                randomFile.putString(stringValue, size);
                kind = Value.STRING;
            } else if (typeString.equals("INTEGER")) {
                Value fieldSize = fieldRecord.getElement("SIZE");
                if (fieldSize == null) size = 4; else size = fieldSize.getInteger();
                kind = Value.INTEGER;
                int v = datum.getInteger();
                if (size != 1 & size != 2 & size != 4) throw new JBasicException(Status.INVRECDEF, new Status(Status.INTSIZE, size));
                randomFile.putInteger(v, size);
            } else if (typeString.equals("BYTE")) {
                size = 1;
                kind = Value.INTEGER;
                randomFile.putInteger(datum.getInteger(), 1);
            } else if (typeString.equals("WORD")) {
                size = 2;
                kind = Value.INTEGER;
                randomFile.putInteger(datum.getInteger(), 2);
            } else if (typeString.equals("DOUBLE")) {
                size = 8;
                kind = Value.DOUBLE;
                randomFile.putDouble(datum.getDouble());
            } else if (typeString.equals("FLOAT")) {
                Value fieldSize = fieldRecord.getElement("SIZE");
                if (fieldSize == null) size = 4; else size = fieldSize.getInteger();
                kind = Value.DOUBLE;
                switch(size) {
                    case 4:
                        randomFile.putFloat(datum.getDouble());
                        break;
                    case 8:
                        randomFile.putDouble(datum.getDouble());
                        break;
                    default:
                        throw new JBasicException(Status.INVRECDEF, new Status(Status.FLTSIZE, size));
                }
            } else if (typeString.equals("BOOLEAN")) {
                size = 1;
                kind = Value.BOOLEAN;
                randomFile.putBoolean(datum.getBoolean());
            }
            if (kind == Value.UNDEFINED) throw new JBasicException(Status.INVRECDEF, new Status(Status.BADTYPE, typeString));
        }
        return;
    }
}
