package com.sun.org.apache.xerces.internal.util;

/**
 * Shadowed symbol table.
 *
 * The table has a reference to the main symbol table and is 
 * not allowed to add new symbols to the main symbol table. 
 * New symbols are added to the shadow symbol table and are local 
 * to the component using this table.
 * 
 * @author Andy Clark IBM
 * @version $Id: ShadowedSymbolTable.java,v 1.2.6.1 2005/09/05 08:51:15 neerajbj Exp $
 */
public final class ShadowedSymbolTable extends SymbolTable {

    /** Main symbol table. */
    protected SymbolTable fSymbolTable;

    /** Constructs a shadow of the specified symbol table. */
    public ShadowedSymbolTable(SymbolTable symbolTable) {
        fSymbolTable = symbolTable;
    }

    /**
     * Adds the specified symbol to the symbol table and returns a
     * reference to the unique symbol. If the symbol already exists, 
     * the previous symbol reference is returned instead, in order
     * guarantee that symbol references remain unique.
     * 
     * @param symbol The new symbol.
     */
    public String addSymbol(String symbol) {
        if (fSymbolTable.containsSymbol(symbol)) {
            return fSymbolTable.addSymbol(symbol);
        }
        return super.addSymbol(symbol);
    }

    /**
     * Adds the specified symbol to the symbol table and returns a
     * reference to the unique symbol. If the symbol already exists, 
     * the previous symbol reference is returned instead, in order
     * guarantee that symbol references remain unique.
     * 
     * @param buffer The buffer containing the new symbol.
     * @param offset The offset into the buffer of the new symbol.
     * @param length The length of the new symbol in the buffer.
     */
    public String addSymbol(char[] buffer, int offset, int length) {
        if (fSymbolTable.containsSymbol(buffer, offset, length)) {
            return fSymbolTable.addSymbol(buffer, offset, length);
        }
        return super.addSymbol(buffer, offset, length);
    }

    /**
     * Returns a hashcode value for the specified symbol. The value
     * returned by this method must be identical to the value returned
     * by the <code>hash(char[],int,int)</code> method when called
     * with the character array that comprises the symbol string.
     * 
     * @param symbol The symbol to hash.
     */
    public int hash(String symbol) {
        return fSymbolTable.hash(symbol);
    }

    /**
     * Returns a hashcode value for the specified symbol information. 
     * The value returned by this method must be identical to the value
     * returned by the <code>hash(String)</code> method when called
     * with the string object created from the symbol information.
     * 
     * @param buffer The character buffer containing the symbol.
     * @param offset The offset into the character buffer of the start
     *               of the symbol.
     * @param length The length of the symbol.
     */
    public int hash(char[] buffer, int offset, int length) {
        return fSymbolTable.hash(buffer, offset, length);
    }
}
