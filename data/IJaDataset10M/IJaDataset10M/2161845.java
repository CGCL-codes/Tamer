package net.sf.opendf.cli.lib;

import java.io.InputStream;
import net.sf.opendf.cal.util.SourceReader;
import net.sf.opendf.hades.models.ModelInterface;
import net.sf.opendf.hades.models.lib.CalModelInterface;

public class PCalMLActorClassFactory extends AbstractGenericInterpreterModelClassFactory {

    @Override
    protected ModelInterface getModelInterface() {
        return MI;
    }

    @Override
    protected Object readModel(InputStream modelSource) {
        return SourceReader.readPreprocessedActorML(modelSource);
    }

    private static final ModelInterface MI = new CalModelInterface();
}
