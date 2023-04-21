package opennlp.tools.util.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.util.InvalidFormatException;

class DictionarySerializer implements ArtifactSerializer<Dictionary> {

    public Dictionary create(InputStream in) throws IOException, InvalidFormatException {
        return new Dictionary(in);
    }

    public void serialize(Dictionary dictionary, OutputStream out) throws IOException {
        dictionary.serialize(out);
    }

    static void register(Map<String, ArtifactSerializer> factories) {
        factories.put("dictionary", new DictionarySerializer());
    }
}
