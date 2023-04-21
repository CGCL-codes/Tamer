package org.maverickdbms.database.mysql;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.SQLException;
import org.maverickdbms.basic.ConstantString;
import org.maverickdbms.basic.MaverickException;
import org.maverickdbms.basic.Factory;
import org.maverickdbms.basic.List;
import org.maverickdbms.basic.Session;
import org.maverickdbms.basic.MaverickString;

/**
* DictionaryFile provides a implementation of Dictionary.
*
*/
public class DictionaryFile extends File implements org.maverickdbms.basic.DictionaryFile {

    private Session session;

    private MasterDictionaryFile master;

    DictionaryFile(Session session, MasterDictionaryFile master, Factory factory, Connection conn, String fn) throws SQLException {
        super(factory, conn, fn);
        this.session = session;
        this.master = master;
    }

    public org.maverickdbms.basic.Field resolveField(ConstantString name) throws MaverickException {
        MaverickString var = factory.getString();
        if (name.equals(MasterDictionaryFile.ALL)) {
            SELECT(session.getProgram(), var, factory.getKey());
            List list = var.getList();
            MaverickString id = factory.getString();
            MaverickString a = factory.getString();
            MaverickString b = factory.getString();
            MaverickString c = factory.getString();
            org.maverickdbms.basic.Field f = null;
            while (list.READNEXT(id, a, b).equals(ConstantString.RETURN_SUCCESS)) {
                READ(c, id, factory.getStatus());
                ConstantString loc = master.extract(c, MasterDictionaryFile.LOC);
                ConstantString typ = master.extract(c, MasterDictionaryFile.TYP);
                if (typ.equals(MasterDictionaryFile.TYPE_DATA) && !loc.equals(ConstantString.ZERO)) {
                    if (f == null) {
                        f = resolveField(id);
                    } else {
                        org.maverickdbms.basic.Field f2 = f;
                        while (f2.getNext() != null) f2 = f2.getNext();
                        f2.setNext(resolveField(id));
                    }
                }
            }
            return f;
        } else {
            if (name.toString().equals("@ID")) {
                READ(var, factory.getConstant("ID"), factory.getStatus());
            } else {
                READ(var, name, factory.getStatus());
            }
            ConstantString typ = master.extract(var, MasterDictionaryFile.TYP);
            if (typ.equals(MasterDictionaryFile.TYPE_PHRASE)) {
            }
            return new Field(factory, master, var);
        }
    }
}
