package org.maverickdbms.database.pgsql;

import java.sql.Connection;
import org.maverickdbms.basic.mvArray;
import org.maverickdbms.basic.mvConstants;
import org.maverickdbms.basic.mvConstantString;
import org.maverickdbms.basic.mvDictionaryFile;
import org.maverickdbms.basic.mvFactory;
import org.maverickdbms.basic.mvFile;
import org.maverickdbms.basic.mvField;
import org.maverickdbms.basic.mvKey;
import org.maverickdbms.basic.mvList;
import org.maverickdbms.basic.mvString;

/**
* pgsqlFile provides a implementation of mvDictionary.
*
*/
public class pgsqlMasterDictionaryFile implements mvDictionaryFile {

    private static final mvConstantString ZERO = mvConstants.STRING_ZERO;

    private static final mvConstantString ALL = mvFactory.getConstant("ALL");

    private static final mvConstantString TYPE_DATA = mvFactory.getConstant("D");

    private static final mvConstantString TYPE_PHRASE = mvFactory.getConstant("PH");

    private static final mvConstantString TYPE_ITYPE = mvFactory.getConstant("I");

    private static final mvConstantString ID_STRING = mvFactory.getConstant("@ID");

    private static final mvConstantString SQL_ID_STRING = mvFactory.getConstant("ID");

    private static final mvConstantString ID = mvFactory.getConstant(0);

    private static final mvConstantString TYP = mvFactory.getConstant(1);

    private static final mvConstantString LOC = mvFactory.getConstant(2);

    private static final mvConstantString CONV = mvFactory.getConstant(3);

    private static final mvConstantString MNAME = mvFactory.getConstant(4);

    private static final mvConstantString FORMAT = mvFactory.getConstant(5);

    private static final mvConstantString SM = mvFactory.getConstant(6);

    private static final mvConstantString ASSOC = mvFactory.getConstant(7);

    private static final char FM = mvConstants.FM;

    static final mvConstantString[] DICT_KEYS = { mvFactory.getConstant("ID"), mvFactory.getConstant("TYP"), mvFactory.getConstant("LOC"), mvFactory.getConstant("CONV"), mvFactory.getConstant("MNAME"), mvFactory.getConstant("FORMAT"), mvFactory.getConstant("SM"), mvFactory.getConstant("ASSOC") };

    static final mvConstantString[] DICT_DATA = { mvFactory.getConstant("D" + FM + "0" + FM + FM + "ID" + FM + "15L" + FM + "S" + FM + ""), mvFactory.getConstant("D" + FM + "1" + FM + FM + "TYP" + FM + "3L" + FM + "S" + FM + ""), mvFactory.getConstant("D" + FM + "2" + FM + FM + "LOC" + FM + "13L" + FM + "S" + FM + ""), mvFactory.getConstant("D" + FM + "3" + FM + FM + "CONV" + FM + "4L" + FM + "S" + FM + ""), mvFactory.getConstant("D" + FM + "4" + FM + FM + "MNAME" + FM + "15L" + FM + "S" + FM + ""), mvFactory.getConstant("D" + FM + "5" + FM + FM + "FORMAT" + FM + "6L" + FM + "S" + FM + ""), mvFactory.getConstant("D" + FM + "6" + FM + FM + "SM" + FM + "6L" + FM + "S" + FM + ""), mvFactory.getConstant("D" + FM + "7" + FM + FM + "SM" + FM + "10L" + FM + "S" + FM + "") };

    private mvFactory factory;

    pgsqlMasterDictionaryFile(mvFactory f, Connection c) {
        factory = f;
    }

    public mvConstantString CLEARFILE() {
        return mvConstants.RETURN_ERROR;
    }

    public mvConstantString CLOSE() {
        return mvConstants.RETURN_SUCCESS;
    }

    public void create(String a, String b, String[] c) {
    }

    public mvConstantString DELETE(mvConstantString a) {
        return mvConstants.RETURN_ERROR;
    }

    public void drop(String a, String b) {
    }

    public mvString FILEINFO(mvString a, mvConstantString b) {
        return a;
    }

    public mvConstantString MATREAD(mvArray a, mvConstantString b) {
        return mvConstants.RETURN_ERROR;
    }

    public mvConstantString MATREADU(mvArray a, mvConstantString b) {
        return mvConstants.RETURN_ERROR;
    }

    public mvConstantString MATWRITE(mvArray a, mvConstantString b) {
        return mvConstants.RETURN_ERROR;
    }

    public mvConstantString OPEN(mvConstantString type, mvConstantString name, int flags) {
        return mvConstants.RETURN_SUCCESS;
    }

    public mvConstantString READ(mvString var, mvConstantString record) {
        for (int i = 0; i < DICT_KEYS.length; i++) {
            if (DICT_KEYS[i].equals(record)) {
                var.set(DICT_DATA[i]);
                return mvConstants.RETURN_SUCCESS;
            }
        }
        return mvConstants.RETURN_ERROR;
    }

    public mvConstantString READT(mvString a, mvConstantString b) {
        return mvConstants.RETURN_ERROR;
    }

    public mvConstantString READU(mvString a, mvConstantString b) {
        return READ(a, b);
    }

    public mvConstantString READV(mvString a, mvConstantString b, mvConstantString c) {
        return mvConstants.RETURN_ERROR;
    }

    public mvConstantString READVU(mvString a, mvConstantString b, mvConstantString c) {
        return READV(a, b, c);
    }

    public mvConstantString RELEASE() {
        return mvConstants.RETURN_ERROR;
    }

    public mvConstantString RELEASE(mvConstantString a) {
        return mvConstants.RETURN_ERROR;
    }

    public mvField resolveField(mvConstantString name) {
        mvString var = factory.getString();
        if (name.equals(ALL)) {
            mvField f = null;
            for (int i = 0; i < DICT_KEYS.length; i++) {
                if (f == null) {
                    f = new pgsqlField(DICT_DATA[i]);
                } else {
                    mvField f2 = f;
                    while (f2.getNext() != null) f2 = f2.getNext();
                    f2.setNext(new pgsqlField(DICT_DATA[i]));
                }
            }
            return f;
        } else {
            if (name.equals(ID_STRING)) name = SQL_ID_STRING;
            READ(var, name);
            mvConstantString typ = var.EXTRACT(TYP, ZERO, ZERO);
            if (typ.equals(TYPE_PHRASE)) {
            }
            return new pgsqlField(var);
        }
    }

    public mvConstantString SELECT(mvString a, mvKey b) {
        return mvConstants.RETURN_ERROR;
    }

    public mvConstantString WRITE(mvConstantString a, mvConstantString b) {
        return mvConstants.RETURN_ERROR;
    }

    public mvConstantString WRITET(mvConstantString a, mvConstantString b) {
        return mvConstants.RETURN_ERROR;
    }

    public mvConstantString WRITEU(mvConstantString a, mvConstantString b) {
        return mvConstants.RETURN_ERROR;
    }

    public mvConstantString WRITEV(mvConstantString a, mvConstantString b, mvConstantString c) {
        return mvConstants.RETURN_ERROR;
    }
}
