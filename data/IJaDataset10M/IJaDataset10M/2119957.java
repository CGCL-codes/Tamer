package org.firebirdsql.gds.impl.wire;

import org.firebirdsql.gds.ISCConstants;
import org.firebirdsql.gds.IscBlobHandle;

/**
 * Describe class <code>isc_blob_handle_impl</code> here.
 *
 * @author <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @version 1.0
 */
public final class isc_blob_handle_impl implements IscBlobHandle {

    private isc_db_handle_impl db;

    private isc_tr_handle_impl tr;

    private int rbl_id;

    private long blob_id;

    private int rbl_flags;

    private int position;

    isc_blob_handle_impl() {
    }

    ;

    public isc_tr_handle_impl getTr() {
        return tr;
    }

    public void setTr(isc_tr_handle_impl value) {
        tr = value;
    }

    public isc_db_handle_impl getDb() {
        return db;
    }

    public void setDb(isc_db_handle_impl value) {
        db = value;
    }

    public long getBlobId() {
        return blob_id;
    }

    public void setBlobId(long value) {
        blob_id = value;
    }

    public int getRbl_id() {
        return rbl_id;
    }

    public void setRbl_id(int value) {
        rbl_id = value;
    }

    public void rbl_flagsAdd(int value) {
        rbl_flags |= value;
    }

    public void rbl_flagsRemove(int value) {
        rbl_flags &= ~value;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isEof() {
        return (rbl_flags & ISCConstants.RBL_eof_pending) != 0;
    }
}
