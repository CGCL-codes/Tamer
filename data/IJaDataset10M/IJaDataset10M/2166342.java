package org.maverickdbms.database.lh;

import org.maverickdbms.basic.ConstantString;
import org.maverickdbms.basic.Delimiter;
import org.maverickdbms.basic.DictionaryFile;
import org.maverickdbms.basic.MaverickException;
import org.maverickdbms.basic.Factory;
import org.maverickdbms.basic.Formatter;
import org.maverickdbms.basic.File;
import org.maverickdbms.basic.Key;
import org.maverickdbms.basic.List;
import org.maverickdbms.basic.Program;
import org.maverickdbms.basic.MaverickString;
import org.maverickdbms.basic.string.JavaInteger;
import org.maverickdbms.basic.string.JavaString;
import org.maverickdbms.basic.util.Tree;

/**
* IMasterDictionaryFile provides a implementation of Prime style Dictionaries.
*
*/
public class IMasterDictionaryFile extends MasterDictionaryFile {

    private static final ConstantString DEFAULT_FORMAT = new JavaString("10L");

    private static final ConstantString ALL = new JavaString("ALL");

    private static final ConstantString TYPE_DATA = new JavaString("D");

    private static final ConstantString TYPE_PHRASE = new JavaString("PH");

    private static final ConstantString TYPE_ITYPE = new JavaString("I");

    private static final ConstantString ID_STRING = new JavaString("@ID");

    private static final ConstantString ID = new JavaInteger(0);

    private static final ConstantString TYP = new JavaInteger(1);

    private static final ConstantString LOC = new JavaInteger(2);

    private static final ConstantString CONV = new JavaInteger(3);

    private static final ConstantString MNAME = new JavaInteger(4);

    private static final ConstantString FORMAT = new JavaInteger(5);

    private static final ConstantString SM = new JavaInteger(6);

    private static final ConstantString ASSOC = new JavaInteger(7);

    private static final char FM = Delimiter.FM;

    private static final ConstantString ZERO = ConstantString.ZERO;

    private static final ConstantString[] DICT_KEYS = { new JavaString("@ID"), new JavaString("TYP"), new JavaString("LOC"), new JavaString("CONV"), new JavaString("MNAME"), new JavaString("FORMAT"), new JavaString("SM"), new JavaString("ASSOC") };

    private static final ConstantString[] DICT_DATA = { new JavaString("D" + FM + "0" + FM + FM + "ID" + FM + "15L" + FM + "S" + FM + ""), new JavaString("D" + FM + "1" + FM + FM + "TYP" + FM + "3L" + FM + "S" + FM + ""), new JavaString("D" + FM + "2" + FM + FM + "LOC" + FM + "13R" + FM + "S" + FM + ""), new JavaString("D" + FM + "3" + FM + FM + "CONV" + FM + "4L" + FM + "S" + FM + ""), new JavaString("D" + FM + "4" + FM + FM + "MNAME" + FM + "15L" + FM + "S" + FM + ""), new JavaString("D" + FM + "5" + FM + FM + "FORMAT" + FM + "6L" + FM + "S" + FM + ""), new JavaString("D" + FM + "6" + FM + FM + "SM" + FM + "2L" + FM + "S" + FM + ""), new JavaString("D" + FM + "7" + FM + FM + "ASSOC" + FM + "10L" + FM + "S" + FM + "") };

    private Factory factory;

    private Tree dict;

    private boolean convNull = true;

    public IMasterDictionaryFile(Factory factory) throws MaverickException {
        this.factory = factory;
    }

    private synchronized Tree getDictionary() {
        if (dict == null) {
            dict = factory.getTree(Tree.TYPE_AVL);
            for (int i = 0; i < DICT_KEYS.length; i++) {
                MasterDictionaryNode mdn = new MasterDictionaryNode();
                mdn.record = DICT_KEYS[i];
                mdn.data = DICT_DATA[i];
                dict.probe(mdn);
            }
        }
        return dict;
    }

    public ConstantString getDisplayName(ConstantString rec) {
        return rec.EXTRACT(MNAME, ZERO, ZERO);
    }

    public org.maverickdbms.basic.Field getField(Program program, File file, ConstantString name) throws MaverickException {
        MaverickString var = factory.getString();
        if (name.equals(ALL)) {
            file.SELECT(program, var, factory.getKey());
            List list = var.getList();
            MaverickString id = factory.getString();
            MaverickString a = factory.getString();
            MaverickString b = factory.getString();
            org.maverickdbms.basic.Field f = null;
            while (list.READNEXT(id, a, b).equals(ConstantString.RETURN_SUCCESS)) {
                MaverickString c = factory.getString();
                file.READ(c, id, factory.getStatus());
                ConstantString loc = getLocation(c);
                ConstantString typ = getType(c);
                if (typ.equals(TYPE_DATA) && !loc.equals(ZERO)) {
                    if (f == null) {
                        f = new Field(factory, this, c);
                    } else {
                        org.maverickdbms.basic.Field f2 = f;
                        while (f2.getNext() != null) f2 = f2.getNext();
                        f2.setNext(new Field(factory, this, c));
                    }
                }
            }
            return f;
        } else {
            file.READ(var, name, factory.getStatus());
            ConstantString typ = getType(var);
            if (typ.equals(TYPE_PHRASE)) {
            }
            return new Field(factory, this, var);
        }
    }

    private Field getFields() {
        Field f = null;
        Tree dict = getDictionary();
        MasterDictionaryNode mdn = new MasterDictionaryNode();
        for (int i = 0; i < DICT_KEYS.length; i++) {
            mdn.record = DICT_KEYS[i];
            MasterDictionaryNode mdn2 = (MasterDictionaryNode) dict.probe(mdn);
            if (mdn2.record != null) {
                if (getLocation(mdn2.data).equals(ZERO)) {
                    continue;
                }
                if (f == null) {
                    f = new Field(factory, this, mdn2.data);
                } else {
                    org.maverickdbms.basic.Field f2 = f;
                    while (f2.getNext() != null) f2 = f2.getNext();
                    f2.setNext(new Field(factory, this, mdn2.data));
                }
            }
        }
        return f;
    }

    public Formatter getDisplayFormatter(ConstantString rec) throws MaverickException {
        ConstantString fmt = rec.EXTRACT(FORMAT, ZERO, ZERO);
        if (fmt.length() == 0) {
            fmt = DEFAULT_FORMAT;
        }
        return factory.getFormatter(Formatter.TYPE_FMT, fmt, convNull);
    }

    public ConstantString getIdString() {
        return ID_STRING;
    }

    public Formatter getInputFormatter(ConstantString rec) throws MaverickException {
        ConstantString fmt = rec.EXTRACT(CONV, ZERO, ZERO);
        return factory.getFormatter(Formatter.TYPE_ICONV, fmt, convNull);
    }

    public ConstantString getLocation(ConstantString rec) {
        return rec.EXTRACT(LOC, ZERO, ZERO);
    }

    public Formatter getOutputFormatter(ConstantString rec) throws MaverickException {
        ConstantString fmt = rec.EXTRACT(CONV, ZERO, ZERO);
        return factory.getFormatter(Formatter.TYPE_OCONV, fmt, convNull);
    }

    private ConstantString getType(ConstantString rec) {
        return rec.EXTRACT(TYP, ZERO, ZERO);
    }

    public ConstantString READ(MaverickString var, ConstantString record, MaverickString status) throws MaverickException {
        MasterDictionaryNode mdn = new MasterDictionaryNode();
        mdn.record = record;
        mdn = (MasterDictionaryNode) getDictionary().probe(mdn);
        if (mdn.data != null) {
            var.set(mdn.data);
            return ConstantString.RETURN_SUCCESS;
        }
        return ConstantString.RETURN_ELSE;
    }

    public org.maverickdbms.basic.Field resolveField(ConstantString name) throws MaverickException {
        if (name.equals(ALL)) {
            return getFields();
        } else {
            MaverickString var = factory.getString();
            READ(var, name, factory.getStatus());
            ConstantString typ = getLocation(var);
            if (typ.equals(TYPE_PHRASE)) {
            }
            return new Field(factory, this, var);
        }
    }

    public void SELECT(Program program, MaverickString result, Key key) throws MaverickException {
        MaverickString list = factory.getString();
        for (int i = 0; i < DICT_KEYS.length; i++) {
            if (i > 0) {
                list.append(ConstantString.FM);
            }
            list.append(DICT_KEYS[i]);
        }
        list.SELECT(program, result, key);
    }
}
