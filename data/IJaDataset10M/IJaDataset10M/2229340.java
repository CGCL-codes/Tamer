package edu.caece.langprocessor.semantic;

import java.util.HashSet;
import java.util.Set;

public class SimpleSymbolsTable implements SymbolsTable {

    private Set<SymbolsTableItem> table;

    public SimpleSymbolsTable() {
        this.table = new HashSet<SymbolsTableItem>();
    }

    public boolean add(String id, SymbolType type, Integer level) {
        boolean retval = false;
        SymbolsTableItem si = new SymbolsTableItem(id, type, level);
        if (!this.table.contains(si)) {
            retval = this.table.add(si);
        }
        return retval;
    }

    public boolean contains(String id, SymbolType type, Integer level) {
        boolean retval = false;
        SymbolsTableItem si = new SymbolsTableItem(id, type, level);
        retval = this.table.contains(si);
        return retval;
    }

    @Override
    public String toString() {
        return this.table.toString();
    }

    public Set<SymbolsTableItem> getSymbols() {
        return this.table;
    }
}
