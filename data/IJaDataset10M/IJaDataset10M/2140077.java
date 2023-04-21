package edu.cmu.sphinx.util.props;

/**
 * Represents an entry in the symbol table
 */
class Symbol {

    private String name;

    private Configurable object;

    private PropertySheet propertySheet;

    private Registry registry;

    /**
     * Creates a symbol table entry
     * 
     * @param name
     *            the name of the symbol
     *  
     */
    Symbol(String name, PropertySheet propertySheet, Registry registry, Configurable obj) {
        this.name = name;
        this.propertySheet = propertySheet;
        this.registry = registry;
        this.object = obj;
    }

    /**
     * @return Returns the name.
     */
    String getName() {
        return name;
    }

    /**
     * @return Returns the object.
     */
    Configurable getObject() {
        return object;
    }

    /**
     * @return Returns the propertySheet.
     */
    PropertySheet getPropertySheet() {
        return propertySheet;
    }

    /**
     * @return Returns the registry.
     */
    Registry getRegistry() {
        return registry;
    }
}
