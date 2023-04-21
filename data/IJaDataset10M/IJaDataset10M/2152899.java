package xQuery;

/**
 * XQuery Module class  provides basic rdf functions 
 *  
 */
public class XQueryModule implements IXueryCompilable {

    private String modulename = null;

    private String modulenamespace = null;

    /**
     * Constructor of XQuery Module 
     * @param modname = Module name 
     * @param modnamespace = Module namespace
     */
    public XQueryModule(String modname, String modnamespace) {
        this.modulenamespace = modnamespace;
        this.modulename = modname;
    }

    /**
     * Adds the import module namespace by module name and namespace
     */
    public String Compile() {
        return String.format("import module namespace %s = \"%s\"; ", this.modulename, this.modulenamespace);
    }
}
