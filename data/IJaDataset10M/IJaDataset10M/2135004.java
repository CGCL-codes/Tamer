package cloudspace.vm.javassist;

/**
 * The Class MacroCall. This class represents a macro call located in the
 * replacement text in a command. See macro class for more information
 */
public class MacroCall extends Macro {

    /**
     * Instantiates a new macro call.
     * 
     * @param userMacro
     *            the user macro call
     * 
     * @throws MalformedMacroBodyException
     *             the malformed macro body exception
     * @throws MalformedMacroNameException
     *             the malformed macro name exception
     * @throws MalformedMacroParametersException
     *             the malformed macro parameters exception
     */
    public MacroCall(String userMacro) throws MalformedMacroException {
        super(userMacro, "^%[a-zA-Z0-9]+\\(", "\\([^\\)]*\\)");
    }

    public String getMacroName() {
        String superName = super.getMacroName();
        return superName.substring(1);
    }
}
