package org.polepos.teams.jdo.data;

import org.polepos.data.*;
import org.polepos.framework.*;

public class JdoIndexedObject implements CheckSummable {

    public int _int;

    public String _string;

    public JdoIndexedObject() {
    }

    public JdoIndexedObject(int int_, String str) {
        _int = int_;
        _string = str;
    }

    public JdoIndexedObject(int int_) {
        this(int_, IndexedObject.queryString(int_));
    }

    @Override
    public long checkSum() {
        return _string.length();
    }

    public void updateString() {
        _string = _string.toUpperCase();
    }

    @Override
    public String toString() {
        return "JdoIndexedObject _int:" + _int + " _string:" + _string;
    }
}
