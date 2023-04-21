package domain.simplex;

import domain.model.Variable;

/**
 * Iterador por filas.
 */
public class RowIterator implements VarIterator {

    int curRow, size;

    Resolver resolver;

    public RowIterator(Resolver resolver) {
        this.curRow = 0;
        this.size = resolver.getRowsCount();
        this.resolver = resolver;
    }

    public boolean hasNext() {
        if (curRow < this.size) return true; else return false;
    }

    public Variable next() {
        return this.resolver.getVarInBase(curRow++);
    }

    public int row() {
        return curRow - 1;
    }

    public int size() {
        return this.size;
    }

    public void remove() {
        throw new UnsupportedOperationException("No se puede borrar");
    }
}
