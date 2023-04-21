package org.maverickdbms.database.localdb;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.maverickdbms.basic.Array;
import org.maverickdbms.basic.Delimiter;
import org.maverickdbms.basic.ConstantString;
import org.maverickdbms.basic.MaverickException;
import org.maverickdbms.basic.Key;
import org.maverickdbms.basic.MaverickString;
import org.maverickdbms.basic.Program;
import org.maverickdbms.basic.Factory;
import org.maverickdbms.basic.Session;

/**
* File provides a localdb representation of a MV type file.
*/
public class File implements org.maverickdbms.basic.File {

    private static final String PROP_NEWLINE_CONVERT = "org.maverickdbms.database.ldb.newline_convert";

    private static final boolean DEFAULT_NEWLINE_CONVERT = true;

    private static final char[] LINE_SEPARATOR = ConstantString.LINE_SEPARATOR.toString().toCharArray();

    private Resolver resolver;

    private java.io.File fdir;

    protected Factory factory;

    private boolean newlineConvert;

    File(Session session, Resolver resolver, java.io.File fdir) {
        this.resolver = resolver;
        this.factory = session.getFactory();
        this.fdir = fdir;
        newlineConvert = session.getProperty(PROP_NEWLINE_CONVERT, DEFAULT_NEWLINE_CONVERT);
    }

    void addProgram(Program program) {
    }

    public ConstantString CLEARFILE(Program program, MaverickString status, boolean locking) {
        String[] allitems = fdir.list();
        for (int dindex = 0; dindex < allitems.length; dindex++) {
            java.io.File tf = new java.io.File(fdir, allitems[dindex]);
            tf.delete();
        }
        return ConstantString.RETURN_SUCCESS;
    }

    public ConstantString CLOSE(Program program, MaverickString status) {
        fdir = null;
        return ConstantString.RETURN_SUCCESS;
    }

    public ConstantString DELETE(Program program, ConstantString record, MaverickString status, boolean locked) {
        java.io.File tf = new java.io.File(fdir, resolver.mangle(record));
        tf.delete();
        return ConstantString.RETURN_SUCCESS;
    }

    public ConstantString DELETEU(Program program, ConstantString record, MaverickString status, boolean locked) {
        java.io.File tf = new java.io.File(fdir, resolver.mangle(record));
        tf.delete();
        return ConstantString.RETURN_SUCCESS;
    }

    public MaverickString FILEINFO(MaverickString result, ConstantString var, MaverickString status) throws MaverickException {
        throw new MaverickException(0, "Sorry FILEINFO is not implemented");
    }

    boolean isClosed() {
        return (fdir == null);
    }

    public ConstantString MATREAD(Array var, ConstantString record, boolean overflowLast, MaverickString status) throws MaverickException {
        MaverickString dynrec = factory.getString();
        ConstantString retval = READ(dynrec, record, status);
        if (retval != ConstantString.RETURN_SUCCESS) return retval;
        var.MATPARSE(null, dynrec, ConstantString.ZERO, ConstantString.ZERO, ConstantString.AM);
        return ConstantString.RETURN_SUCCESS;
    }

    public ConstantString MATREADU(Program program, Array var, ConstantString record, boolean overflowLast, MaverickString status, boolean locked) throws MaverickException {
        return MATREAD(var, record, overflowLast, status);
    }

    public ConstantString MATWRITE(Program program, Array var, ConstantString record, MaverickString status, boolean locked) throws MaverickException {
        MaverickString dynrec = factory.getString();
        var.MATBUILD(dynrec, factory.getConstant(0), factory.getConstant(0), ConstantString.AM);
        return WRITE(program, dynrec, record, status, locked);
    }

    public ConstantString MATWRITEU(Program program, Array var, ConstantString record, MaverickString status, boolean locked) throws MaverickException {
        MaverickString dynrec = factory.getString();
        var.MATBUILD(dynrec, factory.getConstant(0), factory.getConstant(0), ConstantString.AM);
        return WRITEU(program, dynrec, record, status, locked);
    }

    public ConstantString READ(MaverickString var, ConstantString record, MaverickString status) throws MaverickException {
        try {
            RandomAccessFile fvar = new RandomAccessFile(new java.io.File(fdir, resolver.mangle(record)), "r");
            var.clear();
            int ch = fvar.read();
            while (ch >= 0) {
                while (newlineConvert && ch == LINE_SEPARATOR[0]) {
                    boolean match = true;
                    for (int i = 0; i < LINE_SEPARATOR.length; i++) {
                        if (ch != LINE_SEPARATOR[i]) {
                            match = false;
                            for (int j = 0; j < i; j++) {
                                var.append(LINE_SEPARATOR[j]);
                            }
                            break;
                        }
                        ch = fvar.read();
                    }
                    if (match) {
                        var.append(Delimiter.FM);
                    }
                }
                var.append((char) ch);
                ch = fvar.read();
            }
            fvar.close();
            return ConstantString.RETURN_SUCCESS;
        } catch (FileNotFoundException e) {
            return ConstantString.RETURN_ELSE;
        } catch (IOException ioe) {
            throw new MaverickException(0, ioe);
        }
    }

    public ConstantString READT(MaverickString var, ConstantString record) throws MaverickException {
        throw new MaverickException(0, "Sorry READT isn not implemented");
    }

    public ConstantString READU(Program program, MaverickString var, ConstantString record, MaverickString status, boolean locked) throws MaverickException {
        return READ(var, record, status);
    }

    public ConstantString READV(MaverickString var, ConstantString record, ConstantString attrib, MaverickString status) throws MaverickException {
        int attr = attrib.intValue();
        ConstantString retval = READ(var, record, status);
        if (retval != ConstantString.RETURN_SUCCESS) return retval;
        var.set(var.EXTRACT(factory.getConstant(attr), factory.getConstant(0), factory.getConstant(0)));
        return ConstantString.RETURN_SUCCESS;
    }

    public ConstantString READVU(Program program, MaverickString var, ConstantString record, ConstantString attribute, MaverickString status, boolean locked) throws MaverickException {
        return READV(var, record, attribute, status);
    }

    public void RELEASE(Program program, MaverickString status) {
    }

    public void RELEASE(Program program, ConstantString record, MaverickString status) {
    }

    public void SELECT(Program program, MaverickString var, Key key) throws MaverickException {
        List list = new List(program, fdir, key);
        var.setList(list);
    }

    public ConstantString WRITE(Program program, ConstantString var, ConstantString record, MaverickString status, boolean locked) throws MaverickException {
        try {
            RandomAccessFile fvar = new RandomAccessFile(new java.io.File(fdir, resolver.mangle(record)), "rw");
            int len = var.length();
            int count = 0;
            for (int i = 0; i < len; i++) {
                char c = var.charAt(i);
                if (newlineConvert && c == Delimiter.FM) {
                    for (int j = 0; j < LINE_SEPARATOR.length; j++) {
                        fvar.write((int) (LINE_SEPARATOR[j] & 0xff));
                        count++;
                    }
                } else {
                    fvar.write((int) (c & 0xff));
                    count++;
                }
            }
            fvar.setLength(count);
            fvar.close();
            return ConstantString.RETURN_SUCCESS;
        } catch (FileNotFoundException e) {
            return ConstantString.RETURN_ELSE;
        } catch (IOException ioe) {
            throw new MaverickException(0, ioe);
        }
    }

    public ConstantString WRITET(ConstantString var, ConstantString record) throws MaverickException {
        throw new MaverickException(0, "Sorry WRITET is not implemented");
    }

    public ConstantString WRITEU(Program program, ConstantString var, ConstantString record, MaverickString status, boolean locked) throws MaverickException {
        return WRITE(program, var, record, status, locked);
    }

    public ConstantString WRITEV(Program program, ConstantString var, ConstantString record, ConstantString attrib, MaverickString status, boolean locked) throws MaverickException {
        int attr = attrib.intValue();
        MaverickString dynrec = factory.getString();
        ConstantString retval = READ(dynrec, record, status);
        dynrec.REPLACE(factory.getConstant(attr), factory.getConstant(0), factory.getConstant(0), var);
        return WRITE(program, dynrec, record, status, locked);
    }

    public ConstantString WRITEVU(Program program, ConstantString var, ConstantString record, ConstantString attrib, MaverickString status, boolean locked) throws MaverickException {
        int attr = attrib.intValue();
        MaverickString dynrec = factory.getString();
        ConstantString retval = READ(dynrec, record, status);
        dynrec.REPLACE(factory.getConstant(attr), factory.getConstant(0), factory.getConstant(0), var);
        return WRITE(program, dynrec, record, status, locked);
    }
}
