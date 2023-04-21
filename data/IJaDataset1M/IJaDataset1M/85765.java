package org.p4pp.util.commandline;

import org.p4pp.util.Utils;

/**
 * Represents specification for a set of (command-line) options that are allowed
 * (when calling a program, e.g.). For each specified option, some information is stored,
 * f.e. if the option will be followed by a value (<code>-x ABC</code>) or not
 * (<code>-x</code>).
 *
 * @author <a href="mailto:budzyn@ti.informatik.uni-kiel.de">Nikolaj Budzyn</a>
 */
public class OptionSpecification {

    String elementaryOptionsString;

    /**
     * Creates a new <code>PossibleOptions</code> instance, where exactly the letters of
     * <code>elementaryOptionsString</code> are valid options, each of which does not
     * take any parameter.
     */
    public OptionSpecification(String elementaryOptionsString) {
        this.elementaryOptionsString = elementaryOptionsString;
    }

    public boolean isElementaryOption(char letter) {
        return Utils.contains(elementaryOptionsString, letter);
    }

    public boolean isValueOption(char letter) {
        return false;
    }
}
