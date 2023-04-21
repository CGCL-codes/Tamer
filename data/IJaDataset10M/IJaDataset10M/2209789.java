package opennlp.tools.cmdline.chunker;

import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.ModelLoader;
import opennlp.tools.util.InvalidFormatException;

/**
 * Loads a Chunker Model for the command line tools.
 * <p>
 * <b>Note:</b> Do not use this class, internal use only!
 */
public class ChunkerModelLoader extends ModelLoader<ChunkerModel> {

    public ChunkerModelLoader() {
        super("Chunker");
    }

    @Override
    protected ChunkerModel loadModel(InputStream modelIn) throws IOException, InvalidFormatException {
        return new ChunkerModel(modelIn);
    }
}
