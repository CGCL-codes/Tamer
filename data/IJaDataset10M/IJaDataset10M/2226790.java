package org.xenon.queryLanguageExpression;

import org.xenon.algebraMapping.*;
import org.xenon.xmlPath.path.XMLPath;

/**
 *
 * @author  thomasK
 * @version
 */
public class QLIn extends QLWithNextWord implements QLCanAddFunction, QLCanAddWhere, QLCanAddReturn, QLCanAddVariable, Word {

    Object nextWord;

    QLPath table;

    /** this means adding document (
    */
    public Object addFunction(String name) {
        table = QLPath.getQLPathWithFunction(name, this);
        return table;
    }

    /** this means adding $a
    */
    public Object addVariable(String name) {
        table = new QLPath(name, this);
        return table;
    }

    public XMLPath getTable() {
        return ((QLPath) table).getXMLPath();
    }

    /** Creates new QLIn */
    public QLIn() {
    }

    public java.lang.Object addVariable(final QLPath path) {
        table = new QLPath(path.toString(), this);
        return this;
    }
}
