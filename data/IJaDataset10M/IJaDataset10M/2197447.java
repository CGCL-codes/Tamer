package cz.cuni.mff.ufal.volk;

import org.apache.log4j.Logger;
import cz.cuni.mff.ufal.volk.il.Node;
import cz.cuni.mff.ufal.volk.services.LanguageRecognizer;
import cz.cuni.mff.ufal.volk.services.Understander;

public class NabaztagInputProcessor implements Listener<String> {

    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(NabaztagInputProcessor.class);

    public NabaztagInputProcessor(LanguageRecognizer<Text> recognizer, Understander<Text> understander, Listener<Node> interpreter) {
        recognizer.getClass();
        understander.getClass();
        interpreter.getClass();
        this.recognizer = recognizer;
        this.understander = understander;
        this.interpreter = interpreter;
    }

    private final LanguageRecognizer<Text> recognizer;

    private final Understander<Text> understander;

    private final Listener<Node> interpreter;

    @Override
    public void process(String event) {
        Text text = new Text(null, event);
        String language = recognizer.recognize(text);
        Node node = understander.understand(new Text(language, event));
        interpreter.process(node);
    }
}
