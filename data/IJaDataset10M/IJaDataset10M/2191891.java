package de.srcml.parser;

/**
 * A SymbolTableIdentifier is created for every identifier entered into the
 * SymbolTable and represents the starting point of the list of
 * SymbolTableEntry's for the identifier.
 *
 * @author Frank Raiser
 * @version $Revision: 1.7 $
 */
public class SymbolTableIdentifier {

    /**
     * The name of the identifier
     */
    private String m_name;

    /**
     * The first SymbolTableEntry for this Identifier
     */
    private SymbolTableEntry m_entry;

    /**
     * Constructor which takes the identifier's name.
     *
     * @param f_name Name of the identifier
     */
    public SymbolTableIdentifier(String f_name) {
        m_name = f_name;
        m_entry = null;
    }

    /**
     * Returns the name of the identifier.
     *
     * @return name of the identifier.
     */
    public String getName() {
        return m_name;
    }

    /**
     * Returns the currently active SymbolTableEntry for this identifier.
     *
     * @return the currently active SymbolTableEntry for this identifier.
     */
    public SymbolTableEntry getEntry() {
        return m_entry;
    }

    /**
     * Set the currently active SymbolTableEntry for this identifier
     *
     * @param f_entry new SymbolTableEntry instance to use
     */
    public void setEntry(SymbolTableEntry f_entry) {
        m_entry = f_entry;
    }
}
