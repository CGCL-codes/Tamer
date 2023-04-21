package org.datanucleus.query.symbol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.util.StringUtils;

/**
 * Table of symbols in a query.
 */
public class SymbolTable {

    /** SymbolTable for the parent query, if this is a subquery, otherwise null. */
    SymbolTable parentSymbolTable = null;

    Map<String, Symbol> symbols = new HashMap();

    List<Symbol> symbolsTable = new ArrayList();

    ClassLoaderResolver clr;

    SymbolResolver resolver;

    public SymbolTable(ClassLoaderResolver clr) {
        this.clr = clr;
    }

    public void setParentSymbolTable(SymbolTable tbl) {
        this.parentSymbolTable = tbl;
    }

    public SymbolTable getParentSymbolTable() {
        return parentSymbolTable;
    }

    public ClassLoaderResolver getClassLoaderResolver() {
        return clr;
    }

    Symbol getSymbol(int index) {
        synchronized (symbolsTable) {
            return symbolsTable.get(index);
        }
    }

    public void setSymbolResolver(SymbolResolver resolver) {
        this.resolver = resolver;
    }

    public SymbolResolver getSymbolResolver() {
        return resolver;
    }

    /**
     * Accessor for the names of the symbols in this table.
     * @return Names of the symbols
     */
    public Collection<String> getSymbolNames() {
        return new HashSet<String>(symbols.keySet());
    }

    public Symbol getSymbol(String name) {
        synchronized (symbolsTable) {
            return symbols.get(name);
        }
    }

    public Symbol getSymbolIgnoreCase(String name) {
        synchronized (symbolsTable) {
            Iterator<String> iter = symbols.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                if (key.equalsIgnoreCase(name)) {
                    return symbols.get(key);
                }
            }
            return null;
        }
    }

    public boolean hasSymbol(String name) {
        synchronized (symbolsTable) {
            return symbols.containsKey(name);
        }
    }

    public int addSymbol(Symbol symbol) {
        synchronized (symbolsTable) {
            if (symbols.containsKey(symbol.getQualifiedName())) {
                throw new NucleusException("Symbol " + symbol.getQualifiedName() + " already exists.");
            }
            symbols.put(symbol.getQualifiedName(), symbol);
            symbolsTable.add(symbol);
            return symbolsTable.size();
        }
    }

    public void removeSymbol(Symbol symbol) {
        synchronized (symbolsTable) {
            if (!symbols.containsKey(symbol.getQualifiedName())) {
                throw new NucleusException("Symbol " + symbol.getQualifiedName() + " doesnt exist.");
            }
            symbols.remove(symbol.getQualifiedName());
            symbolsTable.remove(symbol);
        }
    }

    public String toString() {
        return "SymbolTable : " + StringUtils.mapToString(symbols);
    }

    public Class getType(List tuples) {
        return resolver.getType(tuples);
    }
}
