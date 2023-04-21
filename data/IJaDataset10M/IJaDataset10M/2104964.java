package com.daffodilwoods.daffodildb.server.sql99.expression.booleanvalueexpression.predicates;

import java.sql.*;
import com.daffodilwoods.daffodildb.server.datasystem.persistentsystem.*;
import com.daffodilwoods.daffodildb.utils.*;
import com.daffodilwoods.daffodildb.utils.field.*;
import com.daffodilwoods.database.resource.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class SimpleEscapeSpecialComparator extends SimpleEscapeComparator {

    public SimpleEscapeSpecialComparator(SRESERVEDWORD1206543922escapecharacter escpaeCharacter0, Object object0, boolean firstClob0, boolean secondClob0, boolean thirdClob0) {
        super(escpaeCharacter0, object0, firstClob0, secondClob0, thirdClob0);
    }

    public int compare(_DComparator leftMatchValue0, _DComparator rightPattern0) throws DException {
        FieldBase escapeCharacter1 = ((FieldBase[]) escpaeCharacter11.run(object))[0];
        char[] matchValue = firstClob ? getValueForClob(leftMatchValue0) : ((String) ((FieldBase) leftMatchValue0).getObject()).toLowerCase().toCharArray();
        char[] matchPattern = secondClob ? getValueForClob(rightPattern0) : ((String) ((FieldBase) rightPattern0).getObject()).toLowerCase().toCharArray();
        char[] escapeArray = thirdClob ? getValueForClob(escapeCharacter1) : ((String) escapeCharacter1.getObject()).toCharArray();
        if (escapeArray.length != 1) {
            throw new DException("DSE0", new Object[] { "Invalid escape character '" + new String(escapeArray) + "' was specified in a LIKE predicate." });
        }
        escapeCharacter = Character.toLowerCase(escapeArray[0]);
        return compareEscapeLike(matchValue, matchPattern);
    }

    private char[] getValueForClob(_DComparator value) throws DException {
        DClobUpdatable clob = (DClobUpdatable) value;
        try {
            return clob.getSubString(1, (int) clob.length()).toLowerCase().toCharArray();
        } catch (SQLException ex) {
            throw new DException("DSE0", new Object[] { "" + ex });
        }
    }
}
