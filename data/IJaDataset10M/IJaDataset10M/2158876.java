package opennlp.tools.cmdline.parser;

import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.cmdline.ModelLoader;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.util.InvalidFormatException;

/**
 * Loads a Parser Model for the command line tools.
 * <p>
 * <b>Note:</b> Do not use this class, internal use only!
 */
final class ParserModelLoader extends ModelLoader<ParserModel> {

    public ParserModelLoader() {
        super("Parser");
    }

    @Override
    protected ParserModel loadModel(InputStream modelIn) throws IOException, InvalidFormatException {
        return new ParserModel(modelIn);
    }
}
