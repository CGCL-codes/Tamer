package opennlp.tools.cmdline.postag;

import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.cmdline.ModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.util.InvalidFormatException;

/**
 * Loads a POS Tagger Model for the command line tools.
 * <p>
 * <b>Note:</b> Do not use this class, internal use only!
 */
public final class POSModelLoader extends ModelLoader<POSModel> {

    public POSModelLoader() {
        super("POS Tagger");
    }

    @Override
    protected POSModel loadModel(InputStream modelIn) throws IOException, InvalidFormatException {
        return new POSModel(modelIn);
    }
}
