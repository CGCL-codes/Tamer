package org.maverickdbms.database.localdb;

import org.maverickdbms.basic.mvArray;
import org.maverickdbms.basic.mvConstantString;
import org.maverickdbms.basic.mvException;
import org.maverickdbms.basic.Key;
import org.maverickdbms.basic.mvString;
import org.maverickdbms.basic.Program;
import org.maverickdbms.basic.Factory;
import java.io.*;

/**
* ldbFile provides a representation of a MV type file.
*/
public class ldbFile implements org.maverickdbms.basic.File {

    private ldbDataInterface ldi;

    private File fdir;

    protected Factory factory;

    ldbFile(ldbDataInterface ldi, Factory factory, File fdir) {
        this.ldi = ldi;
        this.factory = factory;
        this.fdir = fdir;
    }

    public mvConstantString CLEARFILE(Program program, mvString status, boolean locking) {
        String[] allitems = fdir.list();
        for (int dindex = 0; dindex < allitems.length; dindex++) {
            File tf = new File(fdir, allitems[dindex]);
            tf.delete();
        }
        return RETURN_SUCCESS;
    }

    public mvConstantString CLOSE(Program program, mvString status) {
        fdir = null;
        return RETURN_SUCCESS;
    }

    public mvConstantString DELETE(Program program, mvConstantString record, mvString status, boolean locked) {
        File tf = new File(fdir, ldi.mangle(record));
        tf.delete();
        return RETURN_SUCCESS;
    }

    public mvConstantString DELETEU(Program program, mvConstantString record, mvString status, boolean locked) {
        File tf = new File(fdir, ldi.mangle(record));
        tf.delete();
        return RETURN_SUCCESS;
    }

    public mvString FILEINFO(mvString result, mvConstantString var, mvString status) throws mvException {
        throw new mvException(0, "Sorry FILEINFO is not implemented");
    }

    public mvConstantString MATREAD(mvArray var, mvConstantString record, mvString status) throws mvException {
        mvString dynrec = factory.getString();
        mvConstantString retval = READ(dynrec, record, status);
        if (retval != RETURN_SUCCESS) return retval;
        var.MATPARSE(null, dynrec, mvConstantString.ZERO, mvConstantString.ZERO, mvConstantString.AM);
        return RETURN_SUCCESS;
    }

    public mvConstantString MATREADU(Program program, mvArray var, mvConstantString record, mvString status, boolean locked) throws mvException {
        return MATREAD(var, record, status);
    }

    public mvConstantString MATWRITE(Program program, mvArray var, mvConstantString record, mvString status, boolean locked) throws mvException {
        mvString dynrec = factory.getString();
        var.MATBUILD(dynrec, factory.getConstant(0), factory.getConstant(0), mvConstantString.AM);
        return WRITE(program, dynrec, record, status, locked);
    }

    public mvConstantString READ(mvString var, mvConstantString record, mvString status) throws mvException {
        try {
            RandomAccessFile fvar = new RandomAccessFile(new File(fdir, ldi.mangle(record)), "r");
            var.clear();
            long fvarl = fvar.length();
            for (int i = 0; i < fvarl; i++) {
                int onebyte = fvar.readUnsignedByte();
                var.append((char) onebyte);
            }
            fvar.close();
            return RETURN_SUCCESS;
        } catch (FileNotFoundException e) {
            return RETURN_ELSE;
        } catch (IOException ioe) {
            throw new mvException(0, ioe);
        }
    }

    public mvConstantString READT(mvString var, mvConstantString record) throws mvException {
        throw new mvException(0, "Sorry READT isn not implemented");
    }

    public mvConstantString READU(Program program, mvString var, mvConstantString record, mvString status, boolean locked) throws mvException {
        return READ(var, record, status);
    }

    public mvConstantString READV(mvString var, mvConstantString record, mvConstantString attrib, mvString status) throws mvException {
        int attr = attrib.intValue();
        mvConstantString retval = READ(var, record, status);
        if (retval != RETURN_SUCCESS) return retval;
        var.set(var.EXTRACT(factory.getConstant(attr), factory.getConstant(0), factory.getConstant(0)));
        return RETURN_SUCCESS;
    }

    public mvConstantString READVU(Program program, mvString var, mvConstantString record, mvConstantString attribute, mvString status, boolean locked) throws mvException {
        return READV(var, record, attribute, status);
    }

    public void RELEASE(Program program, mvString status) {
    }

    public void RELEASE(Program program, mvConstantString record, mvString status) {
    }

    public void SELECT(Program program, mvString var, Key key) {
        ldbList mvl = new ldbList();
        var.setList(mvl);
        mvl.select(fdir);
    }

    public mvConstantString WRITE(Program program, mvConstantString var, mvConstantString record, mvString status, boolean locked) throws mvException {
        try {
            RandomAccessFile fvar = new RandomAccessFile(new File(fdir, ldi.mangle(record)), "rw");
            fvar.writeBytes(var.toString());
            fvar.close();
            return RETURN_SUCCESS;
        } catch (FileNotFoundException e) {
            return RETURN_ELSE;
        } catch (IOException ioe) {
            throw new mvException(0, ioe);
        }
    }

    public mvConstantString WRITET(mvConstantString var, mvConstantString record) throws mvException {
        throw new mvException(0, "Sorry WRITET is not implemented");
    }

    public mvConstantString WRITEU(Program program, mvConstantString var, mvConstantString record, mvString status, boolean locked) throws mvException {
        return WRITE(program, var, record, status, locked);
    }

    public mvConstantString WRITEV(Program program, mvConstantString var, mvConstantString record, mvConstantString attrib, mvString status, boolean locked) throws mvException {
        int attr = attrib.intValue();
        mvString dynrec = factory.getString();
        mvConstantString retval = READ(dynrec, record, status);
        dynrec.REPLACE(factory.getConstant(attr), factory.getConstant(0), factory.getConstant(0), var);
        return WRITE(program, dynrec, record, status, locked);
    }

    public mvConstantString WRITEVU(Program program, mvConstantString var, mvConstantString record, mvConstantString attrib, mvString status, boolean locked) throws mvException {
        int attr = attrib.intValue();
        mvString dynrec = factory.getString();
        mvConstantString retval = READ(dynrec, record, status);
        dynrec.REPLACE(factory.getConstant(attr), factory.getConstant(0), factory.getConstant(0), var);
        return WRITE(program, dynrec, record, status, locked);
    }
}
