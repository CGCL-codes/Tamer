package org.norecess.nolatte.primitives.image;

import org.norecess.nolatte.ast.Datum;
import org.norecess.nolatte.ast.IGroupOfData;
import org.norecess.nolatte.ast.IPrimitiveDelegate;
import org.norecess.nolatte.ast.IText;
import org.norecess.nolatte.ast.support.IParameters;
import org.norecess.nolatte.ast.support.Parameters;
import org.norecess.nolatte.interpreters.IInterpreter;
import org.norecess.nolatte.system.IImage;
import org.norecess.nolatte.system.NoLatteVariables;

public class ImagePropertiesPrimitiveDelegate implements IPrimitiveDelegate {

    public ImagePropertiesPrimitiveDelegate() {
    }

    public Datum apply(IInterpreter interpreter) {
        IImage image = getImage(interpreter, getFilename(interpreter));
        IGroupOfData result = interpreter.getDatumFactory().createGroup();
        result.hash(NoLatteVariables.WIDTH, image.getWidth());
        result.hash(NoLatteVariables.HEIGHT, image.getHeight());
        return result;
    }

    private IImage getImage(IInterpreter interpreter, IText filename) {
        return interpreter.getSystem().getImage(filename);
    }

    private IText getFilename(IInterpreter interpreter) {
        return interpreter.getDataTypeFilter().getText(interpreter.getEnvironment().get(NoLatteVariables.FILENAME));
    }

    public IParameters getParameters() {
        return new Parameters().addPositional(NoLatteVariables.FILENAME);
    }
}
