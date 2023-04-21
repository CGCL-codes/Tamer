package org.openexi.proc.io;

import org.openexi.proc.common.QName;

abstract class ValueScannerBase extends ValueScanner {

    private final QName m_name;

    ValueScannerBase(QName name) {
        m_name = name;
    }

    public final QName getName() {
        return m_name;
    }
}
