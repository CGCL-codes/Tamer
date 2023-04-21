package opennlp.tools.util.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.featuregen.FeatureGeneratorFactory;

@Deprecated
public class FeatureGeneratorFactorySerializer implements ArtifactSerializer<FeatureGeneratorFactory> {

    private ClassSerializer classSerializer;

    public FeatureGeneratorFactorySerializer() {
        classSerializer = new ClassSerializer();
    }

    public FeatureGeneratorFactory create(InputStream in) throws IOException, InvalidFormatException {
        Class<?> generatorFactoryClass = classSerializer.create(in);
        try {
            return (FeatureGeneratorFactory) generatorFactoryClass.newInstance();
        } catch (InstantiationException e) {
            throw new InvalidFormatException(e);
        } catch (IllegalAccessException e) {
            throw new InvalidFormatException(e);
        }
    }

    public void serialize(FeatureGeneratorFactory artifact, OutputStream out) throws IOException {
        classSerializer.serialize(artifact.getClass(), out);
    }
}
