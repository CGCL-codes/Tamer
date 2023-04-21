package org.buildng.flexmetrics.domain.javamm;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class MetaMember extends MetaData {

    private MetaClass fContainingMetaClass;

    private int fLine;

    private int fColumn;

    MetaMember() {
    }

    public MetaMember(String pName, int pLine, int pColumn) {
        super(pName);
        fLine = pLine;
        fColumn = pColumn;
    }

    @ManyToOne
    public MetaClass getContainingMetaClass() {
        return fContainingMetaClass;
    }

    public void setContainingMetaClass(MetaClass pContainingMetaClass) {
        fContainingMetaClass = pContainingMetaClass;
    }

    @Column(name = "XCOLUMN")
    public int getColumn() {
        return fColumn;
    }

    public void setColumn(int pColumn) {
        fColumn = pColumn;
    }

    public int getLine() {
        return fLine;
    }

    public void setLine(int pLine) {
        fLine = pLine;
    }

    public boolean matches(String pName, int pLine) {
        return (pLine >= fLine) && getName().startsWith(pName);
    }
}
