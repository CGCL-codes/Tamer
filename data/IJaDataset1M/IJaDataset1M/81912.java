package org.maverickdbms.server.database;

import org.maverickdbms.basic.ConstantString;
import org.maverickdbms.basic.MaverickException;
import org.maverickdbms.basic.List;
import org.maverickdbms.basic.MaverickString;

class ClientList implements List {

    private List list;

    private Integer handle;

    ClientList(List list, Integer handle) {
        this.list = list;
        this.handle = handle;
    }

    Integer getHandle() {
        return handle;
    }

    public void close() throws MaverickException {
        list.close();
    }

    public boolean isAfterLast() {
        return list.isAfterLast();
    }

    public long length() throws MaverickException {
        return list.length();
    }

    public ConstantString READLIST(MaverickString var) throws MaverickException {
        return list.READLIST(var);
    }

    public ConstantString READNEXT(MaverickString id, MaverickString val, MaverickString subval) throws MaverickException {
        return list.READNEXT(id, val, subval);
    }
}
