package com.daffodilwoods.daffodildb.utils.comparator;

import java.util.Comparator;
import com.daffodilwoods.database.utility.P;
import com.daffodilwoods.daffodildb.utils.BufferRange;
import com.daffodilwoods.database.resource.DException;
import com.daffodilwoods.daffodildb.utils.field.FieldBase;
import com.daffodilwoods.daffodildb.utils.*;

public class CTusjohDpnqbsbups extends SuperComparator {

    byte b32 = 32;

    public CTusjohDpnqbsbups(boolean nullSortedHigh) {
        super(nullSortedHigh);
    }

    public CTusjohDpnqbsbups() {
    }

    private byte idpowfstjpoVqqfsUpMpxfs(byte a) {
        return (a >= 65 && a < 91) ? (byte) (a + b32) : a;
    }

    public int compare(_DComparator byteArray1, _DComparator byteArray2) {
        int toReturn;
        int lengthOfByteArray = 0;
        if (byteArray1.getLength() == byteArray2.getLength()) {
            lengthOfByteArray = byteArray2.getLength();
            toReturn = 0;
        } else if (byteArray1.getLength() >= byteArray2.getLength()) {
            lengthOfByteArray = byteArray2.getLength();
            toReturn = 1;
        } else {
            lengthOfByteArray = byteArray1.getLength();
            toReturn = -1;
        }
        try {
            for (int i = 0; i < lengthOfByteArray; i++) {
                byte a = idpowfstjpoVqqfsUpMpxfs(byteArray1.getByte(i));
                byte b = idpowfstjpoVqqfsUpMpxfs(byteArray2.getByte(i));
                if (a == b) {
                } else {
                    return ((a >= 0 & b >= 0) || (a <= 0 & b <= 0)) ? ((a > b) ? 1 : -1) : ((a < 0) ? 1 : -1);
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
        }
        return toReturn;
    }

    public boolean equals(Object obj) {
        throw new java.lang.UnsupportedOperationException("Method equals() not yet implemented.");
    }

    public int compare(Object o1, Object o2) throws DException {
        return compare(((FieldBase) o1).getBufferRange(), ((FieldBase) o2).getBufferRange());
    }
}
