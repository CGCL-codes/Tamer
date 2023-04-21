package net.sourceforge.xsdeclipse.transformer;

import java.io.PipedOutputStream;
import java.util.List;

public class TransformerManager implements ITransformerManager {

    private static final ITransformerManager INSTANCE = new TransformerManager();

    private TransformerManager() {
    }

    public static final ITransformerManager getInstance() {
        return INSTANCE;
    }

    public ITransformer createJAXPTransformer(List selection) {
        ITransformer transformer = new JAXPTransformer(selection);
        return transformer;
    }

    public ITransformer createJAXPTransformer(List selection, PipedOutputStream pipedTargetStream) {
        ITransformer transformer = new JAXPTransformer(selection, pipedTargetStream);
        return transformer;
    }

    public ITransformer createAntTransformer(List selection) {
        ITransformer transformer = new AntTransformer(selection);
        return transformer;
    }
}
