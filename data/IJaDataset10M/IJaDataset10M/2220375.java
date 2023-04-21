package org.norecess.nolatte.primitives.system;

import org.norecess.nolatte.ast.Datum;
import org.norecess.nolatte.ast.IText;
import org.norecess.nolatte.ast.Primitive;
import org.norecess.nolatte.interpreters.IInterpreter;
import org.norecess.nolatte.system.NoLatteVariables;

/**
 * The file-contents primitive reads in a file as text <em>without</em>
 * interpretation.
 */
public class FileContentsPrimitive extends Primitive {

    private static final long serialVersionUID = 6119347760369034671L;

    public FileContentsPrimitive() {
        super();
        getParameters().addPositional(NoLatteVariables.FILENAME);
    }

    public Datum apply(IInterpreter interpreter) {
        IText filename = getFilename(interpreter);
        return interpreter.getDatumFactory().createText(interpreter.getSystem().readFile(filename));
    }

    private IText getFilename(IInterpreter interpreter) {
        return interpreter.getDataTypeFilter().getText(interpreter.getEnvironment().get(NoLatteVariables.FILENAME));
    }
}
