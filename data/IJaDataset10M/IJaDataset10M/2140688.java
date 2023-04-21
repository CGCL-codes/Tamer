package org.monet.backservice.control.utils;

import java.util.HashMap;
import java.util.Map;

public class SymbolsTable {

    private Map<String, Symbol> names;

    private Map<String, Symbol> codes;

    private Map<String, Symbol> shortCodes;

    public static final String PATH_NAME_SEPARATOR = "/";

    public SymbolsTable() {
        names = new HashMap<String, Symbol>();
        codes = new HashMap<String, Symbol>();
        shortCodes = new HashMap<String, Symbol>();
    }

    public void add(String code, String name, String shortCode, String definitionName) {
        Symbol symbol = new Symbol(code, name, shortCode, definitionName);
        String keyCode = code;
        String keyName = definitionName + ((name.equals(definitionName)) ? "" : PATH_NAME_SEPARATOR + name);
        if (!name.isEmpty()) this.names.put(keyName, symbol);
        this.codes.put(keyCode, symbol);
        this.shortCodes.put(shortCode, symbol);
    }

    public boolean containName(String key) {
        return this.names.containsKey(key);
    }

    public boolean containCode(String key) {
        return this.codes.containsKey(key);
    }

    public boolean containShortCode(String key) {
        return this.shortCodes.containsKey(key);
    }

    public Symbol getSymbolForName(String key) {
        if (names.containsKey(key)) {
            Symbol symbol = this.names.get(key);
            return symbol;
        } else return null;
    }

    public Symbol getSymbolForCode(String key) {
        if (codes.containsKey(key)) {
            Symbol symbol = this.codes.get(key);
            return symbol;
        } else return null;
    }

    public Symbol getSymbolForShortCode(String key) {
        if (shortCodes.containsKey(key)) {
            Symbol symbol = this.shortCodes.get(key);
            return symbol;
        } else return null;
    }

    public void remove(String olderName, String code, String shortCode) {
        this.names.remove(olderName);
        this.codes.remove(code);
        this.shortCodes.remove(shortCode);
    }
}
